package main.java.com.game;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.swing.ImageIcon;

public abstract class Piece {
  Color color;
  Vec2 pos;
  ImageIcon image;
  boolean firstMove;
  String lastMove;

  public Piece(Color color, Vec2 pos) {
    this.color = color;
    this.pos = pos;
    this.firstMove = true;
    this.lastMove = "";
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null || getClass() != obj.getClass())
      return false;

    Piece piece = (Piece) obj;
    return this.color == piece.color && this.pos.equals(piece.pos);
  }

  @Override
  public int hashCode() {
    return Objects.hash(color, pos);
  }

  public abstract char getChar();

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append(getChar()).append(pos.toAlgebraic());
    return sb.toString();
  }

  public abstract List<Vec2> getMoves(Board board);

  private String formatMove(Vec2 newPos, Piece attacked, Board board) {
    StringBuilder pgnMove = new StringBuilder();

    if (this.getChar() != ' ')
      pgnMove.append(this.getChar());

    if (attacked != null) {
      pgnMove.append('x');
    }

    if (hasSameMoveOnColumn(newPos, board) && hasSameMoveOnRow(newPos, board)) {
      pgnMove.append(this.toString());
    }

    else if (hasSameMoveOnColumn(newPos, board)) {
      pgnMove.append(this.toString().charAt(0));
    }

    else if (hasSameMoveOnRow(newPos, board)) {
      pgnMove.append(this.toString().charAt(1));
    }

    pgnMove.append(newPos.toAlgebraic());

    if (board.getNextPlayer().isMoveChecked(board, this, newPos, attacked, null)) {
      pgnMove.append('+');
    }

    else if (board.getNextPlayer().isCheckMate(newPos, board))
      pgnMove.append('#');
    return pgnMove.toString();
  }

  public boolean hasSameMoveOnRow(Vec2 newPos, Board board) {
    for (Piece p : board.getPlayer().getPieces()) {
      for (Vec2 m : p.getMoves(board)) {
        if (!p.equals(this) && p.pos.y == this.pos.y && newPos.equals(m))
          return true;
      }
    }
    return false;
  }

  public boolean hasSameMoveOnColumn(Vec2 newPos, Board board) {
    for (Piece p : board.getPlayer().getPieces()) {
      for (Vec2 m : p.getMoves(board)) {
        if (!p.equals(this) && p.pos.x == this.pos.x && newPos.equals(m))
          return true;
      }
    }
    return false;
  }

  protected void updateLastMove(Vec2 newPos, Piece attacked, Board board) {
    String moveString = formatMove(newPos, attacked, board);
    if (moveString != null) {
      lastMove = moveString;
    }
  }

  public boolean move(Vec2 newPos, Board board) {
    Piece attacked = board.getPieceAt(newPos, getOppositeColor());
    if (getMoves(board).contains(newPos) &&
        !board.getPlayer().isMoveChecked(board, this, newPos, attacked,
            null)) {
      updateLastMove(newPos, attacked, board);
      this.pos = newPos;
      return true;
    }
    return false;
  }

  public abstract Type getType();

  public abstract Piece clone();

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

  public Vec2 getPos() {
    return pos;
  }

  public void setPos(Vec2 pos) {
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
}