package game;

import java.util.List;

import javax.swing.ImageIcon;

public class Rook extends Piece {

	public Rook(Color color, Vec2 pos) {
		super(color, pos);
		if (color == Color.WHITE)
			this.image = new ImageIcon("src/images/Chess_rlt45.png");
		else
			this.image = new ImageIcon("src/images/Chess_rdt45.png");
	}

	@Override
	public List<Vec2> getMoves(Board board) {
		return getOrthogonalMoves(board);
	}

	@Override
	public Type getType() {
		return Type.ROOK;
	}
}
