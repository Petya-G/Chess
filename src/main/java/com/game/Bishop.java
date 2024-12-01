package main.java.com.game;

import java.util.List;

import javax.swing.ImageIcon;

/**
 * A futót reprezentáló osztály.
 */
public class Bishop extends Piece {
	/**
	 * A Bishop osztály konstruktora.
	 *
	 * @param color A bábú színe.
	 * @param pos   A bábú pozíciója.
	 */
	public Bishop(Color color, Vec2 pos) {
		super(color, pos);
		if (color == Color.WHITE)
			this.image = new ImageIcon("src/main/java/com/images/Chess_blt45.png");
		else
			this.image = new ImageIcon("src/main/java/com/images/Chess_bdt45.png");
	}

	/**
	 * A bábú lehetséges lépéseit adja vissza a megadott táblán.
	 *
	 * @param board A tábla.
	 * @return A lehetséges lépések listája.
	 */
	@Override
	public List<Vec2> getMoves(Board board) {
		return getDiagonalMoves(board);
	}

	/**
	 * Visszaadja a futó típusát.
	 *
	 * @return a futó típusa, ami {@link Type#BISHOP}.
	 */
	@Override
	public Type getType() {
		return Type.BISHOP;
	}

	/**
	 * Létrehoz egy új Bishop objektumot az aktuális Bishop példány alapján.
	 *
	 * @return egy új Bishop objektum, amely az aktuális példány másolata
	 */
	@Override
	public Piece clone() {
		return new Bishop(color, pos);
	}

	/**
	 * Visszaadja a futó karakterét.
	 *
	 * @return 'B' karakter.
	 */
	@Override
	public char getChar() {
		return 'B';
	}
}