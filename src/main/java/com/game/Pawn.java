package main.java.com.game;

import java.util.ArrayList;
import java.util.List;
import javax.swing.ImageIcon;

public class Pawn extends Piece {
  int moved2;
  int sign;

  public Pawn(Color color, Vec2 pos) {
    super(color, pos);
    moved2 = -1;
    if (color == Color.WHITE)
      this.image = new ImageIcon("src/main/java/com/images/Chess_plt45.png");
    else
      this.image = new ImageIcon("src/main/java/com/images/Chess_pdt45.png");
  }

  @Override
  public Type getType() {
    return Type.PAWN;
  }

  @Override
  public Pawn clone() {
    Pawn p = new Pawn(color, pos);
    p.firstMove = firstMove;
    p.moved2 = moved2;
    return p;
  }

  @Override
  public boolean move(Vec2 newPos, Board board) {
    boolean result = false;
    Piece attacked;
    if (newPos.equals(enpassantLeft(board))) {
      attacked = board.getPieceAt(new Vec2(pos.x - 1, pos.y), getOppositeColor());
      if (!board.getPlayer().isMoveChecked(board, this, newPos, attacked,
          null)) {
        if (super.move(newPos, board))
          board.getNextPlayer().removePiece(attacked);
          result = true;
      }
    }

    else if (newPos.equals(enpassantRight(board))) {
      attacked = board.getPieceAt(new Vec2(pos.x + 1, pos.y), getOppositeColor());
      if (!board.getPlayer().isMoveChecked(board, this, newPos, attacked,
          null)) {
        if (super.move(newPos, board))
          board.getNextPlayer().removePiece(attacked);
          result = true;
      }
    }

    else if (newPos.equals(moveTwo(board))) {
      if (super.move(newPos, board)) {
        moved2 = board.turn;
          result = true;
      }
    }

    else if (super.move(newPos, board)) {
          result = true;
    }

    return result;
  }

  public Vec2 moveTwo(Board board) {
    Vec2 move1 = new Vec2(pos.x, pos.y + 1 * sign);
    Vec2 move2 = new Vec2(pos.x, pos.y + 2 * sign);

    if (firstMove && !board.hasPieceAt(move1) && !board.hasPieceAt(move2))
      return move2;
    return null;
  }

  public Vec2 moveOne(Board board) {
    Vec2 move1 = new Vec2(pos.x, pos.y + 1 * sign);

    if (!board.hasPieceAt(move1))
      return move1;
    return null;
  }

  @Override
  public List<Vec2> getMoves(Board board) {
    List<Vec2> moves = new ArrayList<>();

    Vec2 passl = enpassantLeft(board);
    if (passl != null)
      moves.add(passl);

    Vec2 passr = enpassantRight(board);
    if (passr != null)
      moves.add(passr);

    sign = (color == Color.WHITE) ? 1 : -1;
    Vec2 diagonalLeft = new Vec2(pos.x - 1, pos.y + 1 * sign);
    Vec2 diagonalRight = new Vec2(pos.x + 1, pos.y + 1 * sign);

    if (board.hasPieceAt(diagonalLeft, getOppositeColor()))
      moves.add(diagonalLeft);

    if (board.hasPieceAt(diagonalRight, getOppositeColor()))
      moves.add(diagonalRight);

    if (moveOne(board) != null)
      moves.add(moveOne(board));
    if (moveTwo(board) != null)
      moves.add(moveTwo(board));

    moves = board.clipMovesToBoard(moves);
    return moves;
  }

  public Vec2 enpassantRight(Board board) {
    sign = (color == Color.WHITE) ? 1 : -1;
    Vec2 diagonalRight = new Vec2(pos.x + 1, pos.y + 1 * sign);
    Piece p = board.getPieceAt(new Vec2(pos.x + 1, pos.y), getOppositeColor());
    if (p != null) {
      if (p.getType() != Type.PAWN)
        return null;
      Pawn pawn = (Pawn) p;
      if (pawn.getType() == Type.PAWN && pawn.moved2 == board.turn - 1) {
        return diagonalRight;
      }
    }
    return null;
  }

  public Vec2 enpassantLeft(Board board) {
    sign = (color == Color.WHITE) ? 1 : -1;
    Vec2 diagonalLeft = new Vec2(pos.x - 1, pos.y + 1 * sign);
    Piece p = board.getPieceAt(new Vec2(pos.x - 1, pos.y), getOppositeColor());
    if (p != null) {
      if (p.getType() != Type.PAWN)
        return null;
      Pawn pawn = (Pawn) p;
      if (pawn.getType() == Type.PAWN && pawn.moved2 == board.turn - 1) {
        return diagonalLeft;
      }
    }
    return null;
  }

  public boolean isPromotable() {
    return (color == Color.WHITE && pos.y == 7) || (color == Color.BLACK && pos.y == 0);
  }

  public void promoteTo(Type type, Board board) {
    if (isPromotable()) {
      Piece newPiece = null;

      switch (type) {
        case QUEEN:
          newPiece = new Queen(color, pos);
          break;
        case ROOK:
          newPiece = new Rook(color, pos);
          break;
        case BISHOP:
          newPiece = new Bishop(color, pos);
          break;
        case KNIGHT:
          newPiece = new Knight(color, pos);
          break;
        default:
          break;
      }

      board.getPlayer().removePiece(this);
      board.getPlayer().addPiece(newPiece);
    }
  }
}