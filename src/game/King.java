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

	@Override
	public List<Vec2> getMoves(Board board) {
		List<Vec2> moves = new ArrayList<>();
		moves.addAll(getDiagonalMoves(board));
		moves.addAll(getOrthogonalMoves(board));
		moves = limitMoves(moves);
		return moves;
	}

	@Override
	public Type getType() {
		return Type.KING;
	}
}
