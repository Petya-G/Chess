package main.java.com.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.io.File;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;

import main.java.com.controller.BoardController;
import main.java.com.game.Piece.Type;
import main.java.com.game.Vec2;

/**
 * Játéktábla megjelenítéséért felelős osztály.
 */
public class BoardPanel extends JPanel {

  /**
   * Egy mezőt reprezentáló gomb.
   */
  public class Tile extends JButton {
    private JLabel rowLabel;
    private JLabel columnLabel;

    /**
     * Tile konstuktora.
     * 
     * @param image A mezőhöz tartozó kép.
     */
    public Tile(ImageIcon image) {

      super(image);
      setPreferredSize(new Dimension(50, 50));

      setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
      setBorderPainted(false);
      setContentAreaFilled(false);
      setFocusPainted(false);
      setOpaque(true);

      JPanel labelPanel = new JPanel(new BorderLayout());
      labelPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
      labelPanel.setOpaque(false);

      rowLabel = new JLabel("", SwingConstants.LEFT);
      rowLabel.setOpaque(false);
      labelPanel.add(rowLabel, BorderLayout.NORTH);

      columnLabel = new JLabel("", SwingConstants.RIGHT);
      columnLabel.setOpaque(false);
      labelPanel.add(columnLabel, BorderLayout.SOUTH);

      add(labelPanel);

      setOpaque(true);
    }

    /**
     * Beállítja a mező háttérszínét, és a mezőhöz tartozó sor és oszlop számot (ha
     * szükséges).
     * 
     * @param i Az oszlop indexe.
     * @param j A sor indexe.
     */
    public void setTile(int i, int j) {
      setBackground((i + j) % 2 == 0 ? Window.tileYellow : Window.tileGreen);

      if (j == 0) {
        rowLabel.setText(Integer.toString(8 - i));
        rowLabel.setForeground(i % 2 == 1 ? Window.tileYellow
            : Window.tileGreen);
      }

      if (i == size - 1) {
        columnLabel.setText(Character.toString((char) ('A' + j)));
        columnLabel.setForeground(j % 2 == 0 ? Window.tileYellow
            : Window.tileGreen);
      }

      Vec2 pos = new Vec2(j, i);

      addActionListener(e -> {
        boardCtrl.updateSelected(pos);
      });
    }
  }

  /**
   * Játékos nevét megjelenítő címke.
   */
  public class PlayerNameLabel extends JLabel {
    public PlayerNameLabel(String name) {
      super(name, JLabel.LEFT);
    }
  }

  /**
   * A játék irányításáért felelős panel.
   */
  public class ControlPanel extends JPanel {
    /**
     * A játék irányításáért felelős gomb.
     */
    public class ControlButton extends JButton {
      public ControlButton(String text) {
        super(text);
        setBackground(new Color(0x383734));
        setForeground(Color.WHITE);
      }
    }

    ControlButton saveButton, resignButton, drawButton, exitButton;
    MovesPanel movesPanel;

    /**
     * ControlPanel konstruktora.
     */
    public ControlPanel() {

      setLayout(new BorderLayout());
      JPanel buttonPanel = new JPanel(new FlowLayout());

      saveButton = new ControlButton("Save");
      resignButton = new ControlButton("Resign");
      drawButton = new ControlButton("Ask for draw");
      exitButton = new ControlButton("Exit");

      buttonPanel.add(saveButton);
      saveButton.addActionListener(e -> {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Specify a file to save");
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.dir")));
        FileNameExtensionFilter filter = new FileNameExtensionFilter("PGN files (*.pgn)", "pgn");
        fileChooser.setFileFilter(filter);

        int userSelection = fileChooser.showSaveDialog(ControlPanel.this);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
          File fileToSave = fileChooser.getSelectedFile();
          if (!fileToSave.getName().endsWith(".pgn")) {
            fileToSave = new File(fileToSave.getAbsolutePath() + ".pgn");
          }
          boardCtrl.saveBoardTo(fileToSave);
        }
      });
      buttonPanel.add(resignButton);
      resignButton.addActionListener(e -> {
        boardCtrl.resign();
      });
      buttonPanel.add(drawButton);
      drawButton.addActionListener(e -> {
        JPanel panel = new JPanel();
        panel.add(new JLabel(boardCtrl.board.getPlayer().name +
            " is asking for draw."));

        int optionResult = JOptionPane.showConfirmDialog(
            this, panel, "Draw request", JOptionPane.OK_CANCEL_OPTION,
            JOptionPane.PLAIN_MESSAGE);

        if (optionResult == JOptionPane.OK_OPTION) {
          showResult("Draw");
        }
      });
      buttonPanel.add(exitButton);
      exitButton.addActionListener(e -> {
        window.showPanel("MainMenu");
      });

      movesPanel = new MovesPanel();
      add(movesPanel, BorderLayout.CENTER);
      add(buttonPanel, BorderLayout.SOUTH);
    }
  }

  /**
   * A lépések megjelenítéséért felelős panel.
   */
  public class MovesPanel extends JPanel {
    private JTable movesTable;
    private DefaultTableModel movesTableModel;

    /**
     * MovesPanel konstruktora.
     */
    public MovesPanel() {
      setLayout(new BorderLayout());

      movesTableModel = new DefaultTableModel() {
        @Override
        public boolean isCellEditable(int row, int column) {
          return false;
        }
      };

      movesTableModel.addColumn("Moves");

      movesTable = new JTable(movesTableModel);
      JScrollPane scrollPane = new JScrollPane(movesTable);
      add(scrollPane, BorderLayout.CENTER);
    }

    /**
     * Frissíti a lépések listáját.
     * 
     * @param moves A lépések listája.
     */
    public void updateMoves(List<String> moves) {
      movesTableModel.setRowCount(0);
      for (String move : moves) {
        movesTableModel.addRow(new Object[] { move });
      }
    }
  }

  Window window;
  BoardController boardCtrl;
  Tile[][] tiles;
  int size;
  PlayerNameLabel topLabel;
  PlayerNameLabel bottomLabel;
  JPanel gridPanel;
  ControlPanel controlPanel;

  /**
   * BoardPanel konstruktora.
   * 
   * @param window A főablak.
   */
  public BoardPanel(Window window) {
    this.window = window;
    setLayout(new BorderLayout());

    String p1name = "Player 1", p2name = "Player 2";
    if (window.playerNames != null) {
      p1name = window.playerNames[0];
      p2name = window.playerNames[1];
    }

    boardCtrl = new BoardController(this, p1name, p2name);
    this.size = boardCtrl.getSize();

    topLabel = new PlayerNameLabel(boardCtrl.board.player1.name);
    add(topLabel, BorderLayout.NORTH);

    bottomLabel = new PlayerNameLabel(boardCtrl.board.player2.name);
    add(bottomLabel, BorderLayout.SOUTH);

    gridPanel = new JPanel();
    gridPanel.setLayout(new GridLayout(size, size));
    tiles = new Tile[size][size];

    for (int i = 0; i < size; i++) {
      for (int j = 0; j < size; j++) {
        Tile tile = new Tile(null);
        tile.setTile(i, j);

        tiles[j][i] = tile;
        gridPanel.add(tile);
      }
    }

    init();

    controlPanel = new ControlPanel();
    JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, gridPanel, controlPanel);
    splitPane.setDividerLocation(400);
    splitPane.setResizeWeight(0.7);

    add(splitPane, BorderLayout.CENTER);
  }

  /**
   * Inicializálja a játékot.
   */
  public void init() {
    tiles[4][4].doClick();
  }

  /**
   * Frissíti a panelt
   */
  public void updateMovesPanel() {
    controlPanel.movesPanel.updateMoves(boardCtrl.board.moves);
  }

  /**
   * Törli a mezőkről a képeket.
   */
  public void clearImages() {
    for (int i = 0; i < size; i++) {
      for (int j = 0; j < size; j++) {
        tiles[j][i].setIcon(null);
      }
    }
  }

  /**
   * Képet ad hozzá a koordináták általl meghatározott mezőhöz.
   * 
   * @param image A kép.
   * @param x     x koordináta
   * @param y     y koordináta
   */
  public void addImageTo(ImageIcon image, int x, int y) {
    tiles[x][y].setIcon(image);
  }

  /**
   * Megjeleníti az eredménypanelt
   * 
   * @param result Az eredmény amivel véget ért a játék
   */
  public void showResult(String result) {
    JPanel panel = new JPanel();
    String message = "Draw".equals(result) ? result + "." : result + " won.";
    panel.add(new JLabel(message));

    JOptionPane.showMessageDialog(this, panel, "Game Result",
        JOptionPane.PLAIN_MESSAGE);
    if ("Draw".equals(result)) {
      window.hsPanel.highScoreManager.incrementHighScore(
          boardCtrl.board.player1.name, 0.5f);
      window.hsPanel.highScoreManager.incrementHighScore(
          boardCtrl.board.player2.name, 0.5f);
    } else {
      window.hsPanel.highScoreManager.incrementHighScore(result, 1);
    }

    window.hsPanel.updateDisplay();
    window.showPanel("HighScores");
  }

  /**
   * Megjeleníti a promóciós panelt ahol kiválaszthatja a típust amire
   * promoválnánk
   * 
   * @return A kiválasztott típus
   */
  public static Type promptForPromotion() {
    String[] options = { "Queen", "Rook", "Bishop", "Knight" };
    int choice = JOptionPane.showOptionDialog(
        null,
        "Promote your pawn to:",
        "Pawn Promotion",
        JOptionPane.DEFAULT_OPTION,
        JOptionPane.QUESTION_MESSAGE,
        null,
        options,
        options[0]);

    if (choice == -1) {
      return null;
    }

    switch (choice) {
      case 0:
        return Type.QUEEN;
      case 1:
        return Type.ROOK;
      case 2:
        return Type.BISHOP;
      case 3:
        return Type.KNIGHT;
      default:
        return null;
    }
  }
}