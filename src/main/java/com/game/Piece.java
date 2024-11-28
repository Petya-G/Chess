package main.java.com.game;

import java.util.ArrayList;
import java.util.List;
import javax.swing.ImageIcon;

public abstract class Piece {
  public enum Color {
    WHITE,
    BLACK;
  }

  public enum Type {
    PAWN,
    ROOK,
    KNIGHT,
    BISHOP,
    QUEEN,
    KING;
  }

  public enum Direction {
    UP(0, -1),
    DOWN(0, 1),
    LEFT(-1, 0),
    RIGHT(1, 0),
    UP_LEFT(-1, -1),
    UP_RIGHT(1, -1),
    DOWN_LEFT(-1, 1),
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
  boolean firstMove;

  public Piece(Color color, Vec2 pos) {
    this.color = color;
    this.pos = pos;
    this.firstMove = true;
  }

  public Vec2 getPos() { return pos; }

  void setPos(Vec2 pos) { this.pos = pos; }

  public ImageIcon getImage() { return image; }

  public Color getColor() { return color; }

  public Color getOppositeColor() {
    return (color == Color.WHITE) ? Color.BLACK : Color.WHITE;
  }

  public abstract List<Vec2> getMoves(Board board, int turn);

  public List<Vec2> addMovesInDirection(Direction direction, Board board) {
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

      if (!board.hasPieceAt(move, color)) {
        moves.add(move);
      } else {
        break;
      }
    }
    return moves;
  }

  public List<Vec2> getOrthogonalMoves(Board board) {
    List<Vec2> moves = new ArrayList<>();
    moves.addAll(addMovesInDirection(Direction.LEFT, board));
    moves.addAll(addMovesInDirection(Direction.RIGHT, board));
    moves.addAll(addMovesInDirection(Direction.UP, board));
    moves.addAll(addMovesInDirection(Direction.DOWN, board));
    return moves;
  }

  public List<Vec2> getDiagonalMoves(Board board) {
    List<Vec2> moves = new ArrayList<>();
    moves = board.clipMovesToBoard(moves);
    moves.addAll(addMovesInDirection(Direction.UP_LEFT, board));
    moves.addAll(addMovesInDirection(Direction.UP_RIGHT, board));
    moves.addAll(addMovesInDirection(Direction.DOWN_LEFT, board));
    moves.addAll(addMovesInDirection(Direction.DOWN_RIGHT, board));
    return moves;
  }

  public List<Vec2> limitMoves(List<Vec2> moves) {
    List<Vec2> lMoves = new ArrayList<>();
    for (Vec2 v : moves) {
      if (Math.abs(pos.x - v.x) <= 1 && Math.abs(pos.y - v.y) <= 1) {
        lMoves.add(v);
      }
    }
    return lMoves;
  }

  public abstract Type getType();

  public abstract Piece clone();
}
