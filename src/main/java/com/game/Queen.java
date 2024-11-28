package main.java.com.game;

import java.util.ArrayList;
import java.util.List;
import javax.swing.ImageIcon;

public class Queen extends Piece {

  public Queen(Color color, Vec2 pos) {
    super(color, pos);
    if (color == Color.WHITE)
      this.image = new ImageIcon("src/main/java/comimages/Chess_qlt45.png");
    else
      this.image = new ImageIcon("src/main/java/comimages/Chess_qdt45.png");
  }

  @Override
  public List<Vec2> getMoves(Board board, int turn) {
    List<Vec2> moves = new ArrayList<>();
    moves.addAll(getDiagonalMoves(board));
    moves.addAll(getOrthogonalMoves(board));
    return moves;
  }

  @Override
  public Type getType() {
    return Type.QUEEN;
  }

  @Override
  public Queen clone() {
    return new Queen(color, pos);
  }
}
