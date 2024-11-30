package main.java.com.game;

import java.util.ArrayList;
import java.util.List;
import javax.swing.ImageIcon;

public class Knight extends Piece {

  public Knight(Color color, Vec2 pos) {
    super(color, pos);
    if (color == Color.WHITE)
      this.image = new ImageIcon("src/main/java/com/images/Chess_nlt45.png");
    else
      this.image = new ImageIcon("src/main/java/com/images/Chess_ndt45.png");
  }

  @Override
  public List<Vec2> getMoves(Board board) {
    List<Vec2> moves = new ArrayList<>();

    int[][] moveOffsets = { { 2, 1 }, { 2, -1 }, { -2, 1 }, { -2, -1 },
        { 1, 2 }, { 1, -2 }, { -1, 2 }, { -1, -2 } };

    for (int[] offset : moveOffsets) {
      Vec2 move = new Vec2(pos.x + offset[0], pos.y + offset[1]);
      if (board.isWithinBounds(move))
        moves.add(move);
    }

    return moves;
  }

  @Override
  public Type getType() {
    return Type.KNIGHT;
  }

  @Override
  public Piece clone() {
    return new Knight(color, pos);
  }

  @Override
  public char getChar() {
    return 'N';
  }
}
