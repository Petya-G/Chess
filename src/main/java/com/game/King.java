package main.java.com.game;

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

    if (board.hasPieceAt(new Vec2(pos.x + 1, pos.y)) ||
        board.hasPieceAt(new Vec2(pos.x + 2, pos.y))) {
      return null;
    }

    Vec2 newPos = new Vec2(pos.x + 2, pos.y);

    return newPos;
  }

  public Vec2 queensideCastle(Board board) {
    Piece rook = board.getPieceAt(new Vec2(pos.x - 4, pos.y), color);
    if (rook == null || rook.getType() != Type.ROOK)
      return null;

    if (!firstMove && !rook.firstMove)
      return null;

    if (board.hasPieceAt(new Vec2(pos.x - 1, pos.y)) ||
        board.hasPieceAt(new Vec2(pos.x - 2, pos.y)) ||
        board.hasPieceAt(new Vec2(pos.x - 3, pos.y))) {
      return null;
    }

    Vec2 newPos = new Vec2(pos.x - 2, pos.y);

    return newPos;
  }

  @Override
  public List<Vec2> getMoves(Board board, int turn) {
    List<Vec2> moves = new ArrayList<>();
    moves.addAll(getDiagonalMoves(board));
    moves.addAll(getOrthogonalMoves(board));
    moves = limitMoves(moves);

    Vec2 qc = queensideCastle(board);
    if (qc != null)
      moves.add(qc);
    Vec2 kc = queensideCastle(board);
    if (kc != null)
      moves.add(kc);

    return moves;
  }

  public Rook getKingsideRook(Board board) {
    return (Rook)board.getPieceAt(new Vec2(pos.x + 3, pos.y), color);
  }

  public Rook getQueensideRook(Board board) {
    return (Rook)board.getPieceAt(new Vec2(pos.x - 4, pos.y), color);
  }

  public Vec2 getKingsideRookPos() {
    return new Vec2(pos.x - 1, pos.y);
  }

  public Vec2 getQueensideRookPos() {
    return new Vec2(pos.x + 1, pos.y);

  }

  public List<Vec2> getMovesNotChecked(Vec2 pos, int turn, Board board) {
    List<Vec2> movesNotChecked = new ArrayList<Vec2>();
    for (Vec2 move : getMoves(board, turn)) {
      Piece secondary = null;
      Vec2 sPos = null;

      if (move.equals(kingsideCastle(board))) {
        secondary = getKingsideRook(board);
        sPos = getKingsideRookPos();
      }

      else if (move.equals(queensideCastle(board))) {
        secondary = getQueensideRook(board);
        sPos = getQueensideRookPos();
      }

      if (!board.isMoveChecked(this, move, secondary, sPos)) {
        movesNotChecked.add(move);
      }
    }
    return movesNotChecked;
  }

  @Override
  public Type getType() {
    return Type.KING;
  }

  @Override
  public Piece clone() {
    King k = new King(color, pos);
    k.firstMove = this.firstMove;
    return k;
  }
}