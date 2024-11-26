package game;

import java.util.ArrayList;
import java.util.List;
import javax.swing.ImageIcon;

public class Pawn extends Piece {
	public Pawn(Color color, Vec2 pos) {
		super(color, pos);
		if (color == Color.WHITE)
			this.image = new ImageIcon("src/images/Chess_plt45.png");
		else
			this.image = new ImageIcon("src/images/Chess_pdt45.png");
	}

	@Override
	public List<Vec2> getMoves(Board board) {
		List<Vec2> moves = new ArrayList<>();
		int sign = (color == Color.WHITE) ? 1 : -1;

		Vec2 movefwd = new Vec2(pos.x, pos.y + 1 * sign);
		if (!board.hasPieceAt(movefwd, Color.BLACK) && !board.hasPieceAt(movefwd, Color.WHITE))
			moves.add(movefwd);

		Vec2 movefwd2 = new Vec2(pos.x, pos.y + 2 * sign);
		if (firstMove && !moves.isEmpty() && !board.hasPieceAt(movefwd2, Color.BLACK)
				&& !board.hasPieceAt(movefwd2, Color.WHITE)) {
			firstMove = false;
			moves.add(movefwd2);
		}

		moves = board.clipMovesToBoard(moves);
		return moves;
	}

	public List<Vec2> getAttacks(Board board) {
		List<Vec2> moves = new ArrayList<>();
		int sign = (color == Color.WHITE) ? 1 : -1;

		Vec2 diagonalLeft = new Vec2(pos.x - 1, pos.y + 1 * sign);
		if (board.hasPieceAt(diagonalLeft, getOppositeColor()))
			moves.add(diagonalLeft);

		Vec2 diagonalRight = new Vec2(pos.x + 1, pos.y + 1 * sign);
		if (board.hasPieceAt(diagonalRight, getOppositeColor()))
			moves.add(diagonalRight);

		moves = board.clipMovesToBoard(moves);
		return moves;

	}

	@Override
	public Type getType() {
		return Type.PAWN;
	}
}