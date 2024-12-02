package main.java.com.game;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.swing.ImageIcon;

/**
 * A sakkfigurákat reprezentáló absztrakt osztály.
 */
public abstract class Piece implements Cloneable {
  Color color;
  Vec2 pos;
  ImageIcon image;
  boolean firstMove;
  String lastMove;

  /**
   * Létrehoz egy új Piece objektumot a megadott színnel és pozícióval.
   *
   * @param color A bábu színe
   * @param pos   A bábu pozíciója
   */
  public Piece(Color color, Vec2 pos) {
    this.color = color;
    this.pos = pos;
    this.firstMove = true;
    this.lastMove = "";
  }

  /**
   * Két Piece objektum egyenlőségét vizsgálja.
   *
   * @param obj Az összehasonlítandó objektum.
   * @return true, ha az objektumok egyenlőek, különben false.
   */
  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null || getClass() != obj.getClass())
      return false;

    Piece piece = (Piece) obj;
    return this.color == piece.color && this.pos.equals(piece.pos);
  }

  /**
   * Visszaadja a bábu hash kódját a szín és pozíció alapján.
   *
   * @return a bábu hash kódja
   */
  @Override
  public int hashCode() {
    return Objects.hash(color, pos);
  }

  /**
   * A bábú lehetséges lépéseit adja vissza a megadott táblán.
   *
   * @param board A tábla.
   * @return A lehetséges lépések listája.
   */
  public abstract char getChar();

  /**
   * Visszaadja a bábu karakterét és pozícióját algebrai formában.
   * 
   * @return A bábu karaktere és pozíciója.
   */
  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append(getChar()).append(pos.toAlgebraic());
    return sb.toString();
  }

  /**
   * Visszaadja a bábuval léphető lépéseket.
   * 
   * @return A bábval lépehtő lépések listája.
   */
  public abstract List<Vec2> getMoves(Board board);

  /**
   * Visszaadja a lehetséges lépéseket, amelyek nem vezetnek sakkhoz.
   *
   * @param board a játék tábla
   * @return a lépések listája, amelyek nem vezetnek sakkhoz
   */
  public List<Vec2> getMovesNotChecked(Board board) {
    return getMoves(board).stream()
        .filter(m -> {
          Piece attacked = board.getPlayer(color).getPieceAt(m, getColor().getOppositeColor());
          return !board.getPlayer(color).isMoveChecked(board, this, m, attacked, null, false);
        })
        .collect(Collectors.toList());
  }

  /**
   * Visszaadja a lépést PGN formátumban.
   * 
   * @param newPos   pozició ahova a bábu lép
   * @param attacked megtámadott bábu
   * @param board    a játéktábla
   * @return a lépés PGN formátumban
   */
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

    if (board.getNextPlayer().isMoveChecked(board, this, newPos, attacked, null, true)) {
      pgnMove.append('#');
    }

    else if (board.getNextPlayer().isMoveChecked(board, this, newPos, attacked, null, false)) {
      pgnMove.append('+');
    }

    return pgnMove.toString();
  }

  /**
   * Ellenőrzi, hogy a bábu ugyanarra a sorra tud-e lépni, mint egy másik bábu.
   * 
   * @param newPos az új pozíció
   * @param board  a játéktábla
   * @return igaz, ha a bábu ugyanarra a sorra tud lépni, egyébként hamis
   */
  private boolean hasSameMoveOnRow(Vec2 newPos, Board board) {
    for (Piece p : board.getPlayer().getPieces()) {
      for (Vec2 m : p.getMoves(board)) {
        if (!p.equals(this) && this.getType() == p.getType() && p.pos.y == this.pos.y && newPos.equals(m))
          return true;
      }
    }
    return false;
  }

  /**
   * Ellenőrzi, hogy a bábu ugyanarra az oszlopra tud-e lépni, mint egy másik
   * bábu.
   * 
   * @param newPos az új pozíció
   * @param board  a játéktábla
   * @return igaz, ha a bábu ugyanarra a oszlopra tud lépni, egyébként hamis
   */
  private boolean hasSameMoveOnColumn(Vec2 newPos, Board board) {
    for (Piece p : board.getPlayer().getPieces()) {
      for (Vec2 m : p.getMoves(board)) {
        if (!p.equals(this) && this.getType() == p.getType() && p.pos.x == this.pos.x && newPos.equals(m))
          return true;
      }
    }
    return false;
  }

  /**
   * Frissíti az utolsó lépést.
   * 
   * @param newPos   az új pozíció
   * @param attacked megtámadott bábu
   * @param board    a játéktábla
   */
  protected void updateLastMove(Vec2 newPos, Piece attacked, Board board) {
    String moveString = formatMove(newPos, attacked, board);
    if (moveString != null) {
      lastMove = moveString;
    }
  }

  /**
   * A bábút a newpos pozícióra mozgatja, ha az lehetséges.
   * 
   * @param newPos az új pozíció
   * @param board  a játéktábla
   * @return igaz, ha a lépés sikeres, egyébként hamis
   */
  public boolean move(Vec2 newPos, Board board) {
    Piece attacked = board.getPieceAt(newPos, getOppositeColor());
    if (getMoves(board).contains(newPos) &&
        !board.getPlayer().isMoveChecked(board, this, newPos, attacked,
            null, false)) {
      updateLastMove(newPos, attacked, board);
      this.pos = newPos;
      return true;
    }
    return false;
  }

  /**
   * Visszaadja a bábu típusát.
   * 
   * @return a bábu típusa
   */
  public abstract Type getType();

  public abstract Piece clone();

  /**
   * A bábu színét reprezentáló enum.
   */
  public enum Color {
    WHITE,
    BLACK;

    public Color getOppositeColor() {
      return (this == WHITE) ? BLACK : WHITE;
    }
  }

  /**
   * A bábu típusát reprezentáló enum.
   */
  public enum Type {
    PAWN,
    ROOK,
    KNIGHT,
    BISHOP,
    QUEEN,
    KING;

    /**
     * Visszaadja a bábu típusát a megadott karakter alapján.
     * 
     * @param pieceChar a bábu karaktere
     * @return a bábu típusa
     */
    public static Type getType(char pieceChar) {
      switch (pieceChar) {
        case 'Q':
          return Type.QUEEN;
        case 'R':
          return Type.ROOK;
        case 'B':
          return Type.BISHOP;
        case 'N':
          return Type.KNIGHT;
        case 'K':
          return Type.KING;
        default:
          return Type.PAWN;
      }
    }
  }

  /**
   * Az irányokat reprezentáló enum.
   */
  private enum Direction {
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

    /**
     * Létrehoz egy új irányt a megadott x és y eltolással.
     * 
     * @param xOffset
     * @param yOffset
     */
    Direction(int xOffset, int yOffset) {
      this.xOffset = xOffset;
      this.yOffset = yOffset;
    }
  }

  /**
   * Visszaadja a bábu pozícióját.
   * 
   * @return a bábu pozíciója
   */
  public Vec2 getPos() {
    return pos;
  }

  /**
   * Beállítja a bábu pozícióját.
   * 
   * @param pos a bábu új pozíciója
   */
  public void setPos(Vec2 pos) {
    this.pos = pos;
  }

  /**
   * Visszaadja a bábu általl használt ikont.
   * 
   * @return a bábu ikonja
   */
  public ImageIcon getImage() {
    return image;
  }

  /**
   * Visszaadja a bábu színét.
   * 
   * @return a bábu színe
   */
  public Color getColor() {
    return color;
  }

  /**
   * Visszaadja a bábu ellenkező színét.
   * 
   * @return a bábu ellenkező színe
   */
  public Color getOppositeColor() {
    return (color == Color.WHITE) ? Color.BLACK : Color.WHITE;
  }

  /**
   * Hozzáadja a lehetséges lépéseket egy adott irányban a táblán.
   *
   * @param direction Az irány, amelyben a lépéseket hozzá kell adni.
   * @param board     A tábla, amelyen a lépéseket ellenőrizni kell.
   * @return A lehetséges lépések listája az adott irányban.
   */
  private List<Vec2> addMovesInDirection(Direction direction, Board board) {
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

      if (board.hasPieceAt(move)) {
        if (!board.hasPieceAt(move, color)) {
          moves.add(move);
        }
        break;
      } else {
        moves.add(move);
      }
    }
    return moves;
  }

  /**
   * Visszaadja a bábu által léphető horizontális és vertikális lépéseket.
   * 
   * @param board a játéktábla
   * @return a bábu által léphető horizontális és vertikális lépések listája
   */
  protected List<Vec2> getOrthogonalMoves(Board board) {
    List<Vec2> moves = new ArrayList<>();
    moves.addAll(addMovesInDirection(Direction.LEFT, board));
    moves.addAll(addMovesInDirection(Direction.RIGHT, board));
    moves.addAll(addMovesInDirection(Direction.UP, board));
    moves.addAll(addMovesInDirection(Direction.DOWN, board));
    return moves;
  }

  /**
   * Visszaadja a bábu által léphető diagonális lépéseket.
   * 
   * @param board a játéktábla
   * @return a bábu által léphető diagonális lépések listája
   */
  protected List<Vec2> getDiagonalMoves(Board board) {
    List<Vec2> moves = new ArrayList<>();
    moves = board.clipMovesToBoard(moves);
    moves.addAll(addMovesInDirection(Direction.UP_LEFT, board));
    moves.addAll(addMovesInDirection(Direction.UP_RIGHT, board));
    moves.addAll(addMovesInDirection(Direction.DOWN_LEFT, board));
    moves.addAll(addMovesInDirection(Direction.DOWN_RIGHT, board));
    return moves;
  }

  /**
   * Korlátozza a megadott lépések listáját a táblán szereplő mezőkre.
   *
   * @param moves A lépések listája, amelyeket korlátozni kell.
   * @return A táblán szereplőkre korlátozott lépések listája.
   */
  protected List<Vec2> limitMoves(List<Vec2> moves) {
    List<Vec2> lMoves = new ArrayList<>();
    for (Vec2 v : moves) {
      if (Math.abs(pos.x - v.x) <= 1 && Math.abs(pos.y - v.y) <= 1) {
        lMoves.add(v);
      }
    }
    return lMoves;
  }
}