package main.java.com.game;

import java.util.ArrayList;
import java.util.List;
import javax.swing.ImageIcon;

/**
 * A vezért reprezentáló osztály.
 */
public class Queen extends Piece {

  /**
   * A Queen osztály konstruktora.
   *
   * @param color A vezér színe.
   * @param pos   A vezér pozíciója.
   */
  public Queen(Color color, Vec2 pos) {
    super(color, pos);
    if (color == Color.WHITE)
      this.image = new ImageIcon("src/main/java/com/images/Chess_qlt45.png");
    else
      this.image = new ImageIcon("src/main/java/com/images/Chess_qdt45.png");
  }

  /**
   * Visszaadja a vezérrel léphető lépéseket.
   * 
   * @return A vezérrel léphető lépések listája.
   */
  @Override
  public List<Vec2> getMoves(Board board) {
    List<Vec2> moves = new ArrayList<>();
    moves.addAll(getDiagonalMoves(board));
    moves.addAll(getOrthogonalMoves(board));
    return moves;
  }

   /**
   * Visszaadja a vezér típusát.
   *
   * @return a vezér típusát, ami {@link Type#QUEEN}.
   */
 @Override
  public Type getType() {
    return Type.QUEEN;
  }

  /**
   * Létrehoz egy új Queen objektumot az aktuális King példány alapján.
   *
   * @return egy új Queen objektum, amely az aktuális példány másolata
   */
  @Override
  public Queen clone() {
    return new Queen(color, pos);
  }

   /**
   * Visszaadja a vezér karakterét.
   *
   * @return 'Q' karakter.
   */
 @Override
  public char getChar() {
    return 'Q';
  }
}
