package ui;

import controller.BoardController;
import game.Vec2;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class BoardPanel extends JPanel {
  class Tile extends JButton {
    public Tile(ImageIcon image) { super(image); }

    public void setTile(int i, int j) {
      setBackground((i + j) % 2 == 0 ? Window.tileYellow : Window.tileGreen);
      setOpaque(true);
      setBorderPainted(false);
      setPreferredSize(new Dimension(50, 50));

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
