package main.java.com.game;

import java.util.List;
import javax.swing.ImageIcon;

/**
 * A bástyát reprezentáló osztály.
 */
public class Rook extends Piece {
  /**
   * A Rook osztály konstruktora.
   *
   * @param color A bástya színe.
   * @param pos   A bástya pozíciója.
   */
  public Rook(Color color, Vec2 pos) {
    super(color, pos);
    if (color == Color.WHITE)
      this.image = new ImageIcon("src/main/java/com/images/Chess_rlt45.png");
    else
      this.image = new ImageIcon("src/main/java/com/images/Chess_rdt45.png");
  }

  /**
   * Visszaadja a bástyával léphető lépéseket.
   * 
   * @return A bástyával léphető lépések listája.
   */
  @Override
  public List<Vec2> getMoves(Board board) {
    return getOrthogonalMoves(board);
  }

  /**
   * Visszaadja a bástya típusát.
   *
   * @return a bástya típusát, ami {@link Type#ROOk}.
   */
  @Override
  public Type getType() {
    return Type.ROOK;
  }

  /**
   * Létrehoz egy új Rook objektumot az aktuális King példány alapján.
   *
   * @return egy új Rook objektum, amely az aktuális példány másolata
   */
  @Override
  public Rook clone() {
    Rook r = new Rook(color, pos);
    r.firstMove = firstMove;
    return r;
  }

  /**
   * Visszaadja a bástya karakterét.
   *
   * @return 'R' karakter.
   */
  @Override
  public char getChar() {
    return 'R';
  }
}
