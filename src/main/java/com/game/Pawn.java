package main.java.com.game;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.ImageIcon;

/**
 * A gyalogot reprezentáló osztály.
 */
public class Pawn extends Piece {
  private int moved2;
  private int sign;

  /**
   * A Pawn osztály konstruktora.
   *
   * @param color A gyalog színe.
   * @param pos   A gyalog pozíciója.
   */
  public Pawn(Color color, Vec2 pos) {
    super(color, pos);
    moved2 = -1;
    if (color == Color.WHITE)
      this.image = new ImageIcon("src/main/java/com/images/Chess_plt45.png");
    else
      this.image = new ImageIcon("src/main/java/com/images/Chess_pdt45.png");
    sign = (color == Color.BLACK) ? 1 : -1;
  }

  /**
   * Visszaadja a gyalog típusát.
   *
   * @return a gyalog típusát, ami {@link Type#PAWN}.
   */
  @Override
  public Type getType() {
    return Type.PAWN;
  }

  /**
   * Létrehoz egy új Pawn objektumot az aktuális Pawn példány alapján.
   *
   * @return egy új Pawn objektum, amely az aktuális példány másolata
   */
  @Override
  public Pawn clone() {
    Pawn p = new Pawn(color, pos);
    p.firstMove = firstMove;
    p.moved2 = moved2;
    return p;
  }

  /**
   * A gyalogot a newpos pozícióra mozgatja, ha az lehetséges.
   * 
   * @param newPos az új pozíció
   * @param board  a játéktábla
   * @return igaz, ha a lépés sikeres, egyébként hamis
   */
  @Override
  public boolean move(Vec2 newPos, Board board) {
    boolean result = false;
    Piece attacked;
    if (newPos.equals(enpassantLeft(board))) {
      attacked = board.getNextPlayer().getPieceAt(new Vec2(pos.x - 1, pos.y), getOppositeColor());
      if (!board.getPlayer().isMoveChecked(board, this, newPos, attacked,
          null)) {
        if (super.move(newPos, board)) {
          super.updateLastMove(newPos, attacked, board);
          String s = lastMove;
          board.getNextPlayer().removePiece(attacked);
          lastMove = s;
          result = true;
        }

      }
    }

    else if (newPos.equals(enpassantRight(board))) {
      attacked = board.getNextPlayer().getPieceAt(new Vec2(pos.x + 1, pos.y), getOppositeColor());
      if (!board.getPlayer().isMoveChecked(board, this, newPos, attacked,
          null)) {
        if (super.move(newPos, board)) {
          super.updateLastMove(newPos, attacked, board);
          String s = lastMove;
          board.getNextPlayer().removePiece(attacked);
          lastMove = s;
          result = true;
        }
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

  /**
   * A gyalogot kettővel előre léptetéséhez tartozó lépés.
   * 
   * @param board a játéktábla
   * @return a gyalog kettővel előre lépésének pozíciója
   */
  private Vec2 moveTwo(Board board) {
    Vec2 move1 = new Vec2(pos.x, pos.y + 1 * sign);
    Vec2 move2 = new Vec2(pos.x, pos.y + 2 * sign);

    if (firstMove && !board.hasPieceAt(move1) && !board.hasPieceAt(move2))
      return move2;
    return null;
  }

  /**
   * A gyalogot egyel előre léptetéséhez tartozó lépés.
   * 
   * @param board a játéktábla
   * @return a gyalog egyel előre lépésének pozíciója
   */
  private Vec2 moveOne(Board board) {
    Vec2 move1 = new Vec2(pos.x, pos.y + 1 * sign);

    if (!board.hasPieceAt(move1))
      return move1;
    return null;
  }

  /**
   * Visszaadja a gyaloggal léphető lépéseket.
   * 
   * @return A gyaloggal léphető lépések listája.
   */
  @Override
  public List<Vec2> getMoves(Board board) {
    List<Vec2> moves = new ArrayList<>();

    Vec2 passl = enpassantLeft(board);
    if (passl != null)
      moves.add(passl);

    Vec2 passr = enpassantRight(board);
    if (passr != null)
      moves.add(passr);

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
          Vec2 passl = enpassantLeft(board);
          Vec2 passr = enpassantRight(board);

          Piece attacked = board.getPieceAt(m, getColor().getOppositeColor());
          if (passl != null && m.equals(passl)) {
            attacked = passantLeftPawn(board);
            return !board.getPlayer(color).isMoveChecked(board, this, m, attacked, null);
          } else if (passr != null && m.equals(passr)) {
            attacked = passantRightPawn(board);
            return !board.getPlayer(color).isMoveChecked(board, this, m, attacked, null);
          } else {
            return !board.getPlayer(color).isMoveChecked(board, this, m, attacked, null);
          }
        })
        .collect(Collectors.toList());
  }

  /**
   * Megkeresi a jobb oldali gyalogot, amelyik en passant támadható.
   *
   * @param board A játék tábla.
   * @return A jobb oldali gyalog, ha létezik és en passant támadható, különben
   *         null.
   */
  private Pawn passantRightPawn(Board board) {
    Piece piece = board.getPieceAt(new Vec2(pos.x + 1, pos.y), getOppositeColor());
    if (piece != null && piece.getType() == Type.PAWN) {
      return (Pawn) piece;
    }
    return null;
  }

  /**
   * Visszaadja a gyalog en passant jobbra lépését.
   * 
   * @param board a játéktábla
   * @return a gyalog en passant jobbra lépésének pozíciója
   */
  private Vec2 enpassantRight(Board board) {
    Vec2 diagonalRight = new Vec2(pos.x + 1, pos.y + 1 * sign);
    Pawn p = passantRightPawn(board);
    if (p != null) {
      if (p.moved2 == board.turn - 1) {
        return diagonalRight;
      }
    }
    return null;
  }

  /**
   * Megkeresi a bal oldali gyalogot, amelyik en passant támadható.
   *
   * @param board A játék tábla.
   * @return A bal oldali gyalog, ha létezik és en passant támadható, különben
   *         null.
   */
  private Pawn passantLeftPawn(Board board) {
    Piece piece = board.getPieceAt(new Vec2(pos.x - 1, pos.y), getOppositeColor());
    if (piece != null && piece.getType() == Type.PAWN) {
      return (Pawn) piece;
    }
    return null;
  }

  /**
   * Visszaadja a gyalog en passant jobbra lépését.
   * 
   * @param board a játéktábla
   * @return a gyalog en passant jobbra lépésének pozíciója
   */
  private Vec2 enpassantLeft(Board board) {
    Vec2 diagonalLeft = new Vec2(pos.x - 1, pos.y + 1 * sign);
    Pawn p = passantLeftPawn(board);
    if (p != null) {
      if (p.moved2 == board.turn - 1) {
        return diagonalLeft;
      }
    }
    return null;
  }

  /**
   * Megadja, hogy a gyalogot lehet-e promótálni.
   * 
   * @return igaz, ha a gyalogot lehet promótálni, egyébként hamis
   */
  public boolean isPromotable() {
    return (color == Color.WHITE && pos.y == 0) || (color == Color.BLACK && pos.y == 7);
  }

  /**
   * A gyalogot promoválja a megadott bábura.
   * 
   * @param type  a bábu típusa, amire a gyalogot promoválni kell
   * @param board a játéktábla
   */
  public void promoteTo(Type type, Board board) {
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

  /**
   * Visszaadja a gyalog karakterét.
   *
   * @return ' ' karakter.
   */
  @Override
  public char getChar() {
    return ' ';
  }
}