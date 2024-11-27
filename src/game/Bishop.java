package game;

import java.util.List;

import javax.swing.ImageIcon;

public class Bishop extends Piece {

	public Bishop(Color color, Vec2 pos) {
		super(color, pos);
		if (color == Color.WHITE)
			this.image = new ImageIcon("src/images/Chess_blt45.png");
		else
			this.image = new ImageIcon("src/images/Chess_bdt45.png");
	}

	@Override
	public List<Vec2> getMoves(Board board, int turn) {
		return getDiagonalMoves(board);
	}

	@Override
	public Type getType() {
		return Type.BISHOP;
	}

	@Override
	public Piece clone(){
		return new Bishop(color, pos);
	}
}