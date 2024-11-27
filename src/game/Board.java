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
	Color turnColor;
	int turn;
	PGN pgn;

	public Board(String player1Name, String player2Name, int size, Corner corner) {
		this.player1 = new Player(player1Name, Color.WHITE);
		this.player2 = new Player(player2Name, Color.BLACK);
		this.size = size;
		this.turnColor = Color.WHITE;
		this.turn = 0;
		this.pgn = new PGN();
	}

	public void setUpBoard() {
		player1.setUpPieces(size, corner);
		player2.setUpPieces(size, corner);
	}

	public List<Piece> getPieces() {
		List<Piece> pieces = new ArrayList<>();
		pieces.addAll(player1.getPieces());
		pieces.addAll(player2.getPieces());
		return pieces;
	}

	public boolean isWithinBounds(Vec2 move) {
		return move.x >= 0 && move.x < size && move.y >= 0 && move.y < size;
	}

	public List<Vec2> clipMovesToBoard(List<Vec2> moves) {
		for (Vec2 move : moves) {
			if (!isWithinBounds(move)) {
				moves.remove(move);
			}
		}
		return moves;
	}

	public Color getTurn() {
		return turn % 2 == 0 ? Color.WHITE : Color.BLACK;
	}

	public Player getCurrentPlayer() {
		return getPlayer(getTurn());
	}

	public Player getPlayer(Color color) {
		return color == Color.WHITE ? player1 : player2;
	}

	public void changeTurn() {
		turn++;
	}

	public boolean hasPieceAt(Vec2 pos, Color color) {
		for (Piece p : getPieces()) {
			if (pos.equals(p.getPos()) && color == p.getColor()) {
				return true;
			}
		}
		return false;
	}

	public boolean hasPieceAt(Vec2 pos) {
		for (Piece p : getPieces()) {
			if (pos.equals(p.getPos())) {
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

	public boolean isMoveChecked(Vec2 pos) {
		King oldKing = getCurrentPlayer().getKing();
		getCurrentPlayer().removePiece(oldKing);
		King newKing = new King(getTurn(), pos);
		getCurrentPlayer().addPiece(newKing);

		boolean checked = getCurrentPlayer().isChecked(this, turn);
		getCurrentPlayer().removePiece(newKing);
		getCurrentPlayer().addPiece(oldKing);
		return checked;
	}

	public List<Vec2> getMovesNotChecked(Vec2 pos, int turn) {
		List<Vec2> movesNotChecked = new ArrayList<Vec2>();
		for (Vec2 move : getCurrentPlayer().getKing().getMoves(this, turn)) {
			if (!isMoveChecked(move)) {
				movesNotChecked.add(move);
			}
		}
		return movesNotChecked;
	}

	public String MovePieceTo(Piece piece, Vec2 pos) {
		if (getCurrentPlayer().isChecked(this, turn)) {
			System.out.println(getCurrentPlayer().name + " checked");
			if (piece.getType() != Type.KING) {
				return null;
			}

			if (getMovesNotChecked(pos, turn).size() == 0) {
				changeTurn();
				return getCurrentPlayer().name;
			}

			if (isMoveChecked(pos))
				return null;
		}

		boolean moved = false;
		Piece attacked = getPieceAt(pos, piece.getOppositeColor());

		if (piece.getType() == Type.PAWN) {
			Pawn pawn = (Pawn) piece;
			if (pos.equals(pawn.moveTwo(this))) {
				pawn.pos = pos;
				pawn.moved2 = turn;
				moved = true;
			} else if (pos.equals(pawn.enpassantLeft(this, turn))) {
				attacked = getPieceAt(new Vec2(pawn.pos.x - 1, pawn.pos.y), pawn.getOppositeColor());
				pawn.pos = pos;
				moved = true;
			} else if (pos.equals(pawn.enpassantRight(this, turn))) {
				attacked = getPieceAt(new Vec2(pawn.pos.x + 1, pawn.pos.y), pawn.getOppositeColor());
				pawn.pos = pos;
				moved = true;
			} else if (piece.getMoves(this, turn).contains(pos)) {
				piece.pos = pos;
				moved = true;
			}
		}

		else if (piece.getType() == Type.KING) {
			King king = (King) piece;
			if (pos.equals(king.kingsideCastle(this))) {
				Rook rook = (Rook) getPieceAt(new Vec2(king.pos.x + 3, king.pos.y), king.color);
				king.pos = new Vec2(king.pos.x + 2, king.pos.y);
				rook.pos = new Vec2(rook.pos.x - 2, rook.pos.y);
				rook.firstMove = false;
				moved = true;
			} else if (pos.equals(king.queensideCastle(this))) {
				Rook rook = (Rook) getPieceAt(new Vec2(king.pos.x - 4, king.pos.y), king.color);
				king.pos = new Vec2(king.pos.x - 2, king.pos.y);
				rook.pos = new Vec2(king.pos.x + 1, king.pos.y);
				rook.firstMove = false;
				moved = true;
			} else if (piece.getMoves(this, turn).contains(pos)) {
				piece.pos = pos;
				moved = true;
			}
		}

		else if (piece.getMoves(this, turn).contains(pos)) {
			piece.pos = pos;
			moved = true;
		}

		if (moved) {
			if (attacked != null) {
				if (piece.getOppositeColor() == Color.WHITE)
					player1.removePiece(attacked);
				else
					player2.removePiece(attacked);
			}
			piece.firstMove = false;
			changeTurn();
		}
		return null;
	}
}