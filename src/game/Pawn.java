package game;

import java.util.ArrayList;
import java.util.List;
import javax.swing.ImageIcon;

public class Pawn extends Piece {
	int moved2;

	public Pawn(Color color, Vec2 pos) {
		super(color, pos);
		moved2 = -1;
		if (color == Color.WHITE)
			this.image = new ImageIcon("src/images/Chess_plt45.png");
		else
			this.image = new ImageIcon("src/images/Chess_pdt45.png");
	}

	int sign = (color == Color.WHITE) ? 1 : -1;

	public Vec2 moveTwo(Board board) {
		Vec2 move1 = new Vec2(pos.x, pos.y + 1 * sign);
		Vec2 move2 = new Vec2(pos.x, pos.y + 2 * sign);

		if (firstMove && !board.hasPieceAt(move1) && !board.hasPieceAt(move2))
			return move2;
		return null;
	}

	Vec2 diagonalLeft = new Vec2(pos.x - 1, pos.y + 1 * sign);
	Vec2 diagonalRight = new Vec2(pos.x + 1, pos.y + 1 * sign);

	@Override
	public List<Vec2> getMoves(Board board, int turn) {
		List<Vec2> moves = new ArrayList<>();
		Vec2 move1 = new Vec2(pos.x, pos.y + 1 * sign);

		if (!board.hasPieceAt(move1))
			moves.add(move1);
		
		if(moveTwo(board) != null)
			moves.add(moveTwo(board));
		
		Vec2 passl = enpassantLeft(board, turn);
		if(passl != null)
			moves.add(passl);
		
		Vec2 passr = enpassantRight(board, turn);
		if(passr != null)
			moves.add(passr);

		if (board.hasPieceAt(diagonalLeft, getOppositeColor()))
			moves.add(diagonalLeft);

		if (board.hasPieceAt(diagonalRight, getOppositeColor()))
			moves.add(diagonalRight);

		moves = board.clipMovesToBoard(moves);
		return moves;
	}

	public Vec2 enpassantRight(Board board, int turn) {
		Piece p = board.getPieceAt(new Vec2(pos.x + 1, pos.y), getOppositeColor());
		if (p != null) {
			Pawn pawn = (Pawn) p;
			if (pawn.getType() == Type.PAWN && pawn.moved2 == turn - 1) {
				return diagonalRight;
			}
		}
		return null;
	}

	public Vec2 enpassantLeft(Board board, int turn) {
		Piece p = board.getPieceAt(new Vec2(pos.x - 1, pos.y), getOppositeColor());
		if (p != null) {
			Pawn pawn = (Pawn) p;
			if (pawn.getType() == Type.PAWN && pawn.moved2 == turn - 1) {
				return diagonalLeft;
			}
		}
		return null;
	}

	@Override
	public Type getType() {
		return Type.PAWN;
	}

	@Override
	public Pawn clone() {
		Pawn p = new Pawn(color, pos);
		p.firstMove = firstMove;
		p.moved2 = moved2;
		return p;
	}
}