package game;

import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;

public class King extends Piece {

	public King(Color color, Vec2 pos) {
		super(color, pos);
		if (color == Color.WHITE)
			this.image = new ImageIcon("src/images/Chess_klt45.png");
		else
			this.image = new ImageIcon("src/images/Chess_kdt45.png");
	}

	public Vec2 kingsideCastle(Board board) {
		Piece rook = board.getPieceAt(new Vec2(pos.x + 3, pos.y), color);
		if (rook == null || rook.getType() != Type.ROOK)
			return null;

		if (!firstMove && !rook.firstMove)
			return null;

		if (board.hasPieceAt(new Vec2(pos.x + 1, pos.y)) || board.hasPieceAt(new Vec2(pos.x + 2, pos.y))) {
			return null;
		}

		Vec2 newPos = new Vec2(pos.x + 2, pos.y);
		if(board.isMoveChecked(newPos))
			return null;
		
		return newPos; 
	}

	public Vec2 queensideCastle(Board board) {
		Piece rook = board.getPieceAt(new Vec2(pos.x - 4, pos.y), color);
		if (rook == null || rook.getType() != Type.ROOK)
			return null;

		if (!firstMove && !rook.firstMove)
			return null;

		if (board.hasPieceAt(new Vec2(pos.x - 1, pos.y)) || board.hasPieceAt(new Vec2(pos.x - 2, pos.y))
				|| board.hasPieceAt(new Vec2(pos.x - 3, pos.y))) {
			return null;
		}

		Vec2 newPos = new Vec2(pos.x + 2, pos.y);
		if(board.isMoveChecked(newPos))
			return null;
		
		return newPos; 
	}

	@Override
	public List<Vec2> getMoves(Board board) {
		List<Vec2> moves = new ArrayList<>();
		moves.addAll(getDiagonalMoves(board));
		moves.addAll(getOrthogonalMoves(board));
		moves = limitMoves(moves);
//		Vec2 kingSide = kingsideCastle(board);
//		if (kingSide != null)
//			moves.add(kingSide);
//
//		Vec2 queenSide = queensideCastle(board);
//		if (queenSide != null)
//			moves.add(queenSide);

		return moves;
	}

	@Override
	public Type getType() {
		return Type.KING;
	}
}
