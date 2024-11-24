package ui;

import java.awt.BorderLayout;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JButton;
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
			setPreferredSize(new Dimension(100, 100));

			Vec2 pos = new Vec2(j ,i);

			addActionListener(e -> {
				System.out.println("Tile selected: " + pos.x + ", " + pos.y);
				boardCtrl.updateSelected(pos);
			});
		}

	}

	BoardController boardCtrl;
	Tile[][] tiles;
	int size;

	public BoardPanel(BoardController boardCtrl) {
		this.boardCtrl = boardCtrl;
		this.size = boardCtrl.getSize();
		setLayout(new GridLayout(size, size));
		tiles = new Tile[size][size];

		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				Tile tile = new Tile(null);
				tile.setTile(i, j);

				tiles[j][i] = tile;
				add(tile);
			}
		}
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
