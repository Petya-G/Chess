package ui;

import java.awt.BorderLayout;

import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import controller.BoardController;
import game.Vec2;

public class BoardPanel extends JPanel {
	class Tile extends JButton {
		public Tile(ImageIcon image) {
			super(image);
		}

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
		public PlayerNameLabel(String name) {
			super(name, JLabel.LEFT);
		}
	}

	BoardController boardCtrl;
	Tile[][] tiles;
	int size;
	PlayerNameLabel topLabel;
	PlayerNameLabel bottomLabel;
	JPanel gridPanel;

	public BoardPanel() {
		setLayout(new BorderLayout());
		boardCtrl = new BoardController(this);
		this.size = boardCtrl.getSize();

		topLabel = new PlayerNameLabel(boardCtrl.board.player1.name);
		add(topLabel, BorderLayout.NORTH);

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

		bottomLabel = new PlayerNameLabel(boardCtrl.board.player2.name);
		add(bottomLabel, BorderLayout.SOUTH);
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

	public static void main(String[] args) {
		BoardPanel panel = new BoardPanel();
		panel.boardCtrl.updateBoard();
	}
}
