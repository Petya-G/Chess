package main.java.com.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import main.java.com.controller.BoardController;
import main.java.com.game.Vec2;

public class BoardPanel extends JPanel {

  class Tile extends JButton {
    private JLabel rowLabel;
    private JLabel columnLabel;

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

    public void setTile(int i, int j) {
      setBackground((i + j) % 2 == 0 ? Window.tileYellow : Window.tileGreen);

      if (j == 0) {
        rowLabel.setText(Integer.toString(i));
        rowLabel.setForeground(i % 2 == 1 ? Window.tileYellow
                                          : Window.tileGreen);
      }

      if (i == size - 1) {
        columnLabel.setText(Character.toString((char)('A' + j)));
        columnLabel.setForeground(j % 2 == 0 ? Window.tileYellow
                                             : Window.tileGreen);
      }

      Vec2 pos = new Vec2(j, i);

      addActionListener(e -> {
        System.out.println("Tile selected: " + pos.x + ", " + pos.y);
        boardCtrl.updateSelected(pos);
      });
    }
  }

  class PlayerNameLabel extends JLabel {
    public PlayerNameLabel(String name) { super(name, JLabel.LEFT); }
  }

  class ControlPanel extends JPanel {
    class ControlButton extends JButton {
      public ControlButton(String text) {
        super(text);
        setBackground(new Color(0x383734));
        setForeground(Color.WHITE);
      }
    }

    ControlButton saveButton, resignButton, drawButton, exitButton;

    public ControlPanel() {

      setLayout(new BorderLayout());
      JPanel buttonPanel = new JPanel(new FlowLayout());

      saveButton = new ControlButton("Save");
      resignButton = new ControlButton("Resign");
      drawButton = new ControlButton("Ask for draw");
      exitButton = new ControlButton("Exit");

      buttonPanel.add(saveButton);
      buttonPanel.add(resignButton);
      resignButton.addActionListener(e -> { boardCtrl.resign(); });
      buttonPanel.add(drawButton);
      buttonPanel.add(exitButton);
      exitButton.addActionListener(e -> { window.showPanel("MainMenu"); });

      add(buttonPanel, BorderLayout.SOUTH);
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

  public BoardPanel(Window window) {
    this.window = window;
    setLayout(new BorderLayout());

    String[] playerNames = window.playerNames;
    String p1name = playerNames[0];
    String p2name = playerNames[1];

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

    tiles[4][4].doClick();

    controlPanel = new ControlPanel();
    add(controlPanel, BorderLayout.EAST);

    add(gridPanel, BorderLayout.CENTER);
  }

  public void clearImages() {
    for (int i = 0; i < size; i++) {
      for (int j = 0; j < size; j++) {
        tiles[j][i].setIcon(null);
      }
    }
  }

  public void addImageTo(ImageIcon image, int x, int y) {
    tiles[x][y].setIcon(image);
  }

  public void ShowResult(String result) {
    JPanel panel = new JPanel();
    panel.add(new JLabel(result));

    int optionResult = JOptionPane.showConfirmDialog(
        this, panel, "Show HighScores", JOptionPane.OK_CANCEL_OPTION,
        JOptionPane.PLAIN_MESSAGE);

    if (optionResult == JOptionPane.OK_OPTION) {
      window.showPanel("HighScores");
    }
  }
}
