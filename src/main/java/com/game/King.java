package main.java.com.game;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.ImageIcon;

/**
 * A királyt reprezentáló osztály.
 */
public class King extends Piece {

  /**
   * A King osztály konstruktora.
   *
   * @param color A király színe.
   * @param pos   A király pozíciója.
   */
  public King(Color color, Vec2 pos) {
    super(color, pos);
    if (color == Color.WHITE)
      this.image = new ImageIcon("src/main/java/com/images/Chess_klt45.png");
    else
      this.image = new ImageIcon("src/main/java/com/images/Chess_kdt45.png");
  }

  /**
   * A Királyt a newpos pozícióra mozgatja, ha az lehetséges.
   * 
   * @param newPos az új pozíció
   * @param board  a játéktábla
   * @return igaz, ha a lépés sikeres, egyébként hamis
   */
  @Override
  public boolean move(Vec2 newPos, Board board) {
    if (newPos.equals(kingsideCastle(board))) {
      Rook rook = getKingsideRook(board);
      if (!board.getPlayer().isMoveChecked(board, this, pos, rook,
          getKingsideRookPos(), false) &&
          rook.firstMove && this.firstMove) {
        if (super.move(newPos, board)) {
          board.getPlayer().removePiece(rook);
          rook.pos = getKingsideRookPos();
          rook.firstMove = false;
          board.getPlayer().addPiece(rook);
          lastMove = "O-O";
          return true;
        }
      }
    }

    else if (newPos.equals(queensideCastle(board))) {
      Rook rook = getQueensideRook(board);
      if (!board.getPlayer().isMoveChecked(board, this, pos, rook,
          getQueensideRookPos(), false) &&
          rook.firstMove && this.firstMove) {
        if (super.move(newPos, board)) {
          board.getPlayer().removePiece(rook);
          rook.pos = getQueensideRookPos();
          rook.firstMove = false;
          board.getPlayer().addPiece(rook);
          lastMove = "O-O-O";
          return true;
        }
      }
    }

    return super.move(newPos, board);
  }

  /**
   * Ellenőrzi, hogy a király tud-e sáncolni a királyszárnyon, és ha igen,
   * visszaadja az új pozícióját.
   *
   * @param board A tábla.
   * @return A király új pozíciója, ha a sáncolás lehetséges, különben null.
   */
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

  /**
   * Ellenőrzi, hogy a király tud-e sáncolni a vezérszárnyon, és ha igen,
   * visszaadja az új pozícióját.
   *
   * @param board A tábla.
   * @return A király új pozíciója, ha a sáncolás lehetséges, különben null.
   */
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

  /**
   * A király lehetséges lépéseit adja vissza a megadott táblán.
   *
   * @param board A tábla.
   * @return A király lehetséges lépéseinek listája.
   */
  @Override
  public List<Vec2> getMoves(Board board) {
    List<Vec2> moves = new ArrayList<>();
    moves.addAll(getDiagonalMoves(board));
    moves.addAll(getOrthogonalMoves(board));
    moves = limitMoves(moves);

    Vec2 qc = queensideCastle(board);
    if (qc != null)
      moves.add(qc);
    Vec2 kc = kingsideCastle(board);
    if (kc != null)
      moves.add(kc);
    return moves;
  }

  /**
   * Visszaadja a lehetséges lépéseket, amelyek nem vezetnek sakkhoz.
   *
   * @param board a játék tábla
   * @return a lépések listája, amelyek nem vezetnek sakkhoz
   */
  @Override
  public List<Vec2> getMovesNotChecked(Board board) {
    return getMoves(board).stream()
        .filter(m -> {
          Vec2 qc = queensideCastle(board);
          Vec2 kc = kingsideCastle(board);
          Piece attacked = board.getPieceAt(m, getColor().getOppositeColor());
          if (qc != null && m.equals(qc)) {
            attacked = getKingsideRook(board);
            return !board.getPlayer(color).isMoveChecked(board, this, m, attacked, getKingsideRookPos(), false);
          } else if (kc != null && m.equals(kc)) {
            attacked = getQueensideRook(board);
            return !board.getPlayer(color).isMoveChecked(board, this, m, attacked, getQueensideRookPos(), false);
          } else {
            return !board.getPlayer(color).isMoveChecked(board, this, m, attacked, null, false);
          }
        })
        .collect(Collectors.toList());
  }

  /**
   * Visszaadja a király oldalán lévő bástyát.
   *
   * @param board A játék tábla.
   * @return A király oldalán lévő bástya.
   */
  private Rook getKingsideRook(Board board) {
    return (Rook) board.getPlayer(color).getPieceAt(new Vec2(pos.x + 3, pos.y), color);
  }

  /**
   * Visszaadja a vezér oldalán lévő bástyát.
   *
   * @param board A játék tábla.
   * @return A vezér oldalán lévő bástya.
   */
  private Rook getQueensideRook(Board board) {
    return (Rook) board.getPlayer(color).getPieceAt(new Vec2(pos.x - 4, pos.y), color);
  }

  /**
   * Visszaadja a királyoldali bástya pozícióját.
   *
   * @return A királyoldali bástya pozíciója.
   */
  private Vec2 getKingsideRookPos() {
    return new Vec2(pos.x - 1, pos.y);
  }

  /**
   * Visszaadja a vezér oldali bástya pozícióját.
   *
   * @return A vezér oldali bástya pozíciója.
   */
  private Vec2 getQueensideRookPos() {
    return new Vec2(pos.x + 1, pos.y);
  }

  /**
   * Visszaadja a király típusát.
   *
   * @return a király típusát, ami {@link Type#KING}.
   */
  @Override
  public Type getType() {
    return Type.KING;
  }

  /**
   * Létrehoz egy új King objektumot az aktuális King példány alapján.
   *
   * @return egy új King objektum, amely az aktuális példány másolata
   */
  @Override
  public Piece clone() {
    King k = new King(color, pos);
    k.firstMove = this.firstMove;
    return k;
  }

  /**
   * Visszaadja a király karakterét.
   *
   * @return 'K' karakter.
   */
  @Override
  public char getChar() {
    return 'K';
  }
}