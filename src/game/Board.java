package game;

import java.util.ArrayList;
import java.util.List;

import game.Piece.Color;
import game.Piece.Type;

public class Board {
	enum Corner {
		TOPRIGHT, BOTTOMRIGHT, BOTTOMLEFT, TOPLEFT;
	}

	public Player player1, player2;
	int size;
	Corner corner;
	Color turn;

	public Board(String player1Name, String player2Name, int size, Corner corner) {
		this.player1 = new Player(player1Name, Color.WHITE);
		this.player2 = new Player(player2Name, Color.BLACK);
		this.size = size;
		turn = Color.WHITE;
	}

	public void setUpBoard() {
		player1.setUpPieces(size, corner);
	}

	public List<Piece> getPieces() {
		List<Piece> pieces = new ArrayList<>();
		pieces.addAll(player1.getPieces());
		pieces.addAll(player2.getPieces());
		return pieces;
	}

	public boolean isWithinBounds(Vec2 move) {
		return move.x < 0 || move.x > size || move.y < 0 || move.y > size;
	}

	public List<Vec2> clipMovesToBoard(List<Vec2> moves) {
		for (Vec2 move : moves) {
			if (isWithinBounds(move)) {
				moves.remove(move);
			}
		}
		return moves;
	}

	public Color getTurn() {
		return turn;
	}

	public void changeTurn() {
		turn = (turn == Color.WHITE) ? Color.BLACK : Color.WHITE;
	}

	public boolean hasPieceAt(Vec2 pos, Color color) {
		for (Piece p : getPieces()) {
			if (pos.equals(p.getPos()) && color == p.getColor()) {
				return true;
			}
		}
		return false;
	}

	public Piece getPieceAt(Vec2 pos, Color color) {
		for (Piece p : getPieces()) {
			if (pos.equals(p.getPos()) && color == p.getColor()) {
				return p;
			}
		}
		return null;
	}

	public void MovePieceTo(Piece piece, Vec2 pos) {
		if (piece.getMoves(this).contains(pos)) {
			if (piece.getType() == Type.PAWN) {
				piece.pos = pos;
			}
			changeTurn();
		}
	}
}
