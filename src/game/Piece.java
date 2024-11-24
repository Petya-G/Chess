package game;

import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;

public abstract class Piece {
	public enum Color {
		WHITE, BLACK;
	}

	public enum Type {
		PAWN, ROOK, KNIGTH, BISHOP, QUEEN, KING;
	}

	public enum Direction {
		UP(0, -1), DOWN(0, 1), LEFT(-1, 0), RIGHT(1, 0), UP_LEFT(-1, -1), UP_RIGHT(1, -1), DOWN_LEFT(-1, 1),
		DOWN_RIGHT(1, 1);

		public final int xOffset;
		public final int yOffset;

		Direction(int xOffset, int yOffset) {
			this.xOffset = xOffset;
			this.yOffset = yOffset;
		}
	}

	Color color;
	Vec2 pos;
	ImageIcon image;

	public Piece(Color color, Vec2 pos) {
		this.color = color;
		this.pos = pos;
	}

	public Vec2 getPos() {
		return pos;
	}

	void setPos(Vec2 pos) {
		this.pos = pos;
	}

	public ImageIcon getImage() {
		return image;
	}

	public Color getColor() {
		return color;
	}

	public Color getOppositeColor() {
		return (color == Color.WHITE) ? Color.BLACK : Color.WHITE;
	}

	public abstract List<Vec2> getMoves(Board board);

	public List<Vec2> addMovesInDirection(Vec2 pos, Direction direction, Color color, Board board) {
		List<Vec2> moves = new ArrayList<>();
		int x = pos.x;
		int y = pos.y;

		while (true) {
			x += direction.xOffset;
			y += direction.yOffset;

			Vec2 move = new Vec2(x, y);

			if (!board.isWithinBounds(move)) {
				break;
			}

			if (board.hasPieceAt(move, color)) {
				moves.add(move);
			} else {
				break;
			}
		}
		return moves;
	}

	public List<Vec2> getOrthogonalMoves(Board board) {
		List<Vec2> moves = new ArrayList<>();

//		for (int i = pos.x - 1; i >= 0; i--) {
//			Vec2 move = new Vec2(i, pos.y);
//			if (board.hasPieceAt(move, color))
//				moves.add(move);
//			else
//				break;
//		}
//		for (int i = pos.x + 1; i < board.size; i++) {
//			Vec2 move = new Vec2(i, pos.y);
//			if (board.hasPieceAt(move, color))
//				moves.add(move);
//			else
//				break;
//		}
//
//		moves = board.clipMovesToBoard(moves);
		return moves;

	}

	public abstract Type getType();
}
