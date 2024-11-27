package game;

import java.util.ArrayList;
import java.util.List;

import game.Board.Corner;
import game.Piece.Color;
import game.Piece.Type;

public class Player {
	public String name;
	Color color;
	List<Piece> pieces;

	public Player(String name, Color color) {
		this.color = color;
		if (color == Color.WHITE && name.length() == 0)
			this.name = "Player1";
		else if (color == Color.BLACK && name.length() == 0)
			this.name = "Player2";
		this.name = name;
		this.pieces = new ArrayList<>();
	}

	public void setUpPieces(int size, Corner corner) {
		if (color == Color.WHITE) {
			for (int i = 0; i < size; i++) {
				pieces.add(new Pawn(color, new Vec2(i, 1)));
			}
			pieces.add(new Rook(color, new Vec2(0, 0)));
			pieces.add(new Knight(color, new Vec2(1, 0)));
			pieces.add(new Bishop(color, new Vec2(2, 0)));
			pieces.add(new Queen(color, new Vec2(3, 0)));
//			pieces.add(new King(color, new Vec2(4, 0)));
			pieces.add(new Bishop(color, new Vec2(5, 0)));
			pieces.add(new Knight(color, new Vec2(6, 0)));
			pieces.add(new Rook(color, new Vec2(7, 0)));

			pieces.add(new King(color, new Vec2(1, 5)));
		} else {
			for (int i = 0; i < 1; i++) {
				pieces.add(new Pawn(color, new Vec2(i, 6)));
			}
			// pieces.add(new Rook(color, new Vec2(0, 7)));
			// pieces.add(new Knight(color, new Vec2(1, 7)));
			// pieces.add(new Bishop(color, new Vec2(2, 7)));
			// pieces.add(new Queen(color, new Vec2(3, 7)));
			pieces.add(new King(color, new Vec2(4, 7)));
			// pieces.add(new Bishop(color, new Vec2(5, 7)));
			// pieces.add(new Knight(color, new Vec2(6, 7)));
			// pieces.add(new Rook(color, new Vec2(7, 7)));
		}
	}

	public List<Piece> getPieces() {
		return pieces;
	}

	public void removePiece(Piece piece) {
		pieces.remove(piece);
	}

	public void addPiece(Piece piece) {
		pieces.add(piece);
	}

	public King getKing() {
		return (King) pieces.stream().filter(p -> p.getType() == Type.KING).findFirst().orElse(null);
	}

	public boolean isChecked(Board board, int turn) {
		King king = getKing();
		List<Vec2> moves = new ArrayList<>();

		if (color == Color.WHITE) {
			for (Piece p : board.player2.getPieces()) {
				moves.addAll(p.getMoves(board, turn));
			}
		} else {
			for (Piece p : board.player1.getPieces()) {
				moves.addAll(p.getMoves(board, turn));
			}
		}

		for (Vec2 move : moves) {
			if (move.equals(king.getPos()))
				return true;
		}

		return false;
	}
}