package ui;

import controller.BoardController;
import game.Vec2;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class BoardPanel extends JPanel {
  class Tile extends JButton {
    private JLabel rowLabel;
    private JLabel columnLabel;

    public Tile(ImageIcon image) {

      super(image);
      setPreferredSize(new Dimension(50, 50));

      setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
      setBorderPainted(false);
      setContentAreaFilled(false); // Prevents the default white background
      setFocusPainted(false);      // Removes focus indicator
      setOpaque(true);             // Ensures the background color is painted

      // Create a panel to hold the row and column labels
      JPanel labelPanel = new JPanel(new BorderLayout());
      labelPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
      labelPanel.setOpaque(false); // Make the panel itself transparent

      // Row label on top left (left side)
      rowLabel = new JLabel("", SwingConstants.LEFT);
      rowLabel.setOpaque(false);
      labelPanel.add(rowLabel, BorderLayout.NORTH);

      // Column label on bottom right (right side)
      columnLabel = new JLabel("", SwingConstants.RIGHT);
      columnLabel.setOpaque(false);
      labelPanel.add(columnLabel, BorderLayout.SOUTH);

      // Add the panel to the button
      add(labelPanel);

      // Set the button to be opaque and set its background
      setOpaque(true);
    }

    public void setTile(int i, int j) {
      setBackground((i + j) % 2 == 0 ? Window.tileYellow : Window.tileGreen);

      if (j == 0) {
        rowLabel.setText(Integer.toString(i));
        rowLabel.setForeground(i % 2 == 1 ? Window.tileYellow
                                          : Window.tileGreen);
      }

      if (i == 7) {
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

  Window window;
  BoardController boardCtrl;
  Tile[][] tiles;
  int size;
  PlayerNameLabel topLabel;
  PlayerNameLabel bottomLabel;
  JPanel gridPanel;

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

    add(gridPanel, BorderLayout.CENTER);
    revalidate();
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
}
