package main.java.com.game;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import main.java.com.game.Piece.Color;
import main.java.com.game.Piece.Type;

/**
 * Egy játékost reprezentáló osztály.
 */
public class Player {
  public String name;
  private Color color;
  private List<Piece> pieces;

  /**
   * A Player osztály konstruktora.
   *
   * @param name  A játékos neve.
   * @param color A játékos színe.
   */
  public Player(String name, Color color) {
    this.color = color;
    this.name = name;
    this.pieces = new ArrayList<>();
  }

  /**
   * Beállítja a játékos bábuit a megadott méret (és színe) alapján.
   *
   * @param size A bábuk száma.
   */
  public void setUpPieces(int size) {
    if (color == Color.BLACK) {
      for (int i = 0; i < size; i++) {
        pieces.add(new Pawn(color, new Vec2(i, 1)));
      }
      pieces.add(new Knight(color, new Vec2(1, 0)));
      pieces.add(new Bishop(color, new Vec2(2, 0)));
      pieces.add(new Queen(color, new Vec2(3, 0)));
      pieces.add(new Bishop(color, new Vec2(5, 0)));
      pieces.add(new Knight(color, new Vec2(6, 0)));
      pieces.add(new Rook(color, new Vec2(0, 0)));
      pieces.add(new Rook(color, new Vec2(7, 0)));
      pieces.add(new King(color, new Vec2(4, 0)));

    } else {
      for (int i = 0; i < size; i++) {
        pieces.add(new Pawn(color, new Vec2(i, 6)));
      }
      pieces.add(new Knight(color, new Vec2(1, 7)));
      pieces.add(new Bishop(color, new Vec2(2, 7)));
      pieces.add(new Queen(color, new Vec2(3, 7)));
      pieces.add(new Bishop(color, new Vec2(5, 7)));
      pieces.add(new Knight(color, new Vec2(6, 7)));
      pieces.add(new Rook(color, new Vec2(0, 7)));
      pieces.add(new Rook(color, new Vec2(7, 7)));
      pieces.add(new King(color, new Vec2(4, 7)));
    }
  }

  /**
   * Visszaadja a játékos bábuit.
   *
   * @return A játékos bábui.
   */
  public List<Piece> getPieces() {
    return pieces;
  }

  /**
   * Visszaadja a játékos színét.
   *
   * @return A játékos színe.
   */
  public Color getColor() {
    return color;
  }

  /**
   * Eltávolít egy bábut a játékos bábui közül.
   *
   * @param piece A bábu, amelyet el kell távolítani.
   */
  public void removePiece(Piece piece) {
    pieces.remove(piece);
  }

  /**
   * Hozzáad egy bábut a játékos bábuihoz.
   *
   * @param piece A bábu, amelyet hozzá kell adni.
   */
  public void addPiece(Piece piece) {
    pieces.add(piece);
  }

  /**
   * Visszaad egy bábut a megadott pozícióban és színben.
   * 
   * @param pos   a bábu pozíciója
   * @param color a bábu színe
   * @return a bábu a megadott pozícióban és színben, ha létezik, egyébként null
   */
  public Piece getPieceAt(Vec2 pos, Color color) {
    return pieces.stream()
        .filter(p -> p.pos.equals(pos) && p.color == color)
        .findFirst()
        .orElse(null);
  }

  /**
   * Visszaad egy bábut a megadott típus alapján.
   * 
   * @param type a bábu típusa
   * @return a bábu a megadott típus alapján, ha létezik, egyébként null
   */
  public Piece getPiece(Type type) {
    return pieces.stream()
        .filter(p -> p.getType() == type)
        .findFirst()
        .orElse(null);
  }

  /**
   * Megszámolja a bábukat a megadott típus alapján.
   * 
   * @param type a bábu típusa
   * @return a bábuk száma a megadott típus alapján
   */
  public int countType(Piece.Type type) {
    if (type == null)
      return pieces.size();
    return (int) pieces.stream().filter(p -> p.getType() == type).count();
  }

  /**
   * Megadja, hogy a játékos királya sakkban van-e.
   * 
   * @param board a játéktábla
   * @param turn  a játék lépésszáma
   * @return igaz, ha a játékos királya sakkban van, egyébként hamis
   */
  public boolean isChecked(Board board, int turn) {
    King king = (King) getPiece(Type.KING);
    List<Vec2> moves = new ArrayList<>();

    for (Piece p : board.getPlayer(color.getOppositeColor()).getPieces()) {
      moves.addAll(p.getMoves(board));
    }

    for (Vec2 move : moves) {
      if (move.equals(king.getPos()))
        return true;
    }

    return false;
  }

  /**
   * Megadja, hogy a játékos királya sakkban van-e a megadott lépés után.
   * 
   * @param board     a játéktábla
   * @param primary   az első bábu
   * @param pPos      az első bábu új pozíciója
   * @param secondary a második bábu
   * @param sPos      a második bábu új pozíciója (ha ennek nem null az értéket,
   *                  akkor a második bábút ide mozgatjuk)
   * @return igaz, ha a játékos királya sakkban van a megadott lépés után,
   *         egyébként hamis
   */
  public boolean isMoveChecked(Board board, Piece primary, Vec2 pPos, Piece secondary,
      Vec2 sPos) {
    Piece oPrimary = primary;
    board.getPlayer(primary.getColor()).removePiece(primary);

    Piece nPrimary = primary.clone();
    nPrimary.pos = pPos;
    board.getPlayer(primary.getColor()).addPiece(nPrimary);

    boolean checked;
    if (secondary == null && sPos == null) {
      checked = isChecked(board, board.turn);
    }

    else {
      Piece oSecondary = secondary.clone();

      if (sPos == null) {
        board.getPlayer(primary.getOppositeColor()).removePiece(secondary);
        checked = isChecked(board, board.turn);
        board.getPlayer(primary.getOppositeColor()).addPiece(oSecondary);
      }

      else {
        board.getPlayer(primary.getColor()).removePiece(secondary);
        Piece nSecondary = oSecondary.clone();
        nSecondary.pos = sPos;
        board.getPlayer(primary.getColor()).addPiece(nSecondary);

        checked = isChecked(board, board.turn);
        board.getPlayer(primary.getColor()).removePiece(nSecondary);
        board.getPlayer(primary.getColor()).addPiece(oSecondary);
      }
    }

    board.getPlayer(primary.getColor()).removePiece(nPrimary);
    board.getPlayer(primary.getColor()).addPiece(oPrimary);

    return checked;
  }

  /**
   * Visszadja a játékos összes lehetséges lépését, amik nem vezetnek sakkhoz.
   * 
   * @param board a játéktábla
   * @return a játékos lépései amik nem vezetnek sakkhoz
   */
  private List<Vec2> getMovesNotChecked(Board board) {
      List<Piece> ps = new ArrayList<>(pieces);
      return ps.stream()
               .flatMap(p -> p.getMovesNotChecked(board).stream())
               .collect(Collectors.toList());
  }

  /**
   * Megadja, hogy a játékos sakkmattot kapott-e.
   * 
   * @param board a játéktábla
   * @return igaz, ha a játékos sakkmattot kapott, egyébként hamis
   */
  public boolean isCheckMate(Board board) {
    return getMovesNotChecked(board).size() == 0 && isChecked(board, board.turn);
  }

  /**
   * Azon bábukat adja vissza, amelyekkel a megadott lépés lehetséges.
   * 
   * @param type  bábuk típusa
   * @param move  lépés helye
   * @param board játéktábla
   * @return azon bábuk listája, amelyekkel a lépés lehetséges
   */
  public List<Piece> getPiecesThatHaveMove(Type type, Vec2 move, Board board) {
    List<Piece> filteredPieces = new ArrayList<>();
    for (Piece p : getPieces()) {
      if (p.getType() == type && p.getMoves(board).contains(move)) {
        filteredPieces.add(p);
      }
    }
    return filteredPieces;
  }
}