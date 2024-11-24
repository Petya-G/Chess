package game;

import java.util.ArrayList;
import java.util.List;

import game.Board.Corner;
import game.Piece.Color;

public class Player {
	String name;
	Color color;
	List<Piece> pieces;

	public Player(String name, Color color) {
		this.color = color;
		if (color == Color.WHITE && name.length() == 0)
			this.name = "Player1";
		else if (color == Color.BLACK && name.length() == 0)
			this.name = "Player2";
		this.name = name;
		pieces = new ArrayList<>();
	}

	public void setUpPieces(int size, Corner corner) {
		Piece pawn1 = new Pawn(color, new Vec2(1, 1));
		pieces.add(pawn1);
	}

	public List<Piece> getPieces(){
		return pieces;
	}
}