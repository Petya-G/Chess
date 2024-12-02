package main.java.com.game;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.ImageIcon;

/**
 * A huszárt reprezentáló osztály.
 */
public class Knight extends Piece {

  /**
   * A Knight osztály konstruktora.
   *
   * @param color A huszár színe.
   * @param pos   A huszár pozíciója.
   */
  public Knight(Color color, Vec2 pos) {
    super(color, pos);
    if (color == Color.WHITE)
      this.image = new ImageIcon("src/main/java/com/images/Chess_nlt45.png");
    else
      this.image = new ImageIcon("src/main/java/com/images/Chess_ndt45.png");
  }

  /**
   * Visszaadja a huszárral léphető lépéseket.
   * 
   * @return A huszárral léphető lépések listája.
   */
  @Override
  public List<Vec2> getMoves(Board board) {
    List<Vec2> moves = new ArrayList<>();

    int[][] moveOffsets = { { 2, 1 }, { 2, -1 }, { -2, 1 }, { -2, -1 },
        { 1, 2 }, { 1, -2 }, { -1, 2 }, { -1, -2 } };

    for (int[] offset : moveOffsets) {
      Vec2 move = new Vec2(pos.x + offset[0], pos.y + offset[1]);
      if (board.isWithinBounds(move))
        moves.add(move);
    }

    return moves;
  }

  /**
   * Visszaadja a huszár típusát.
   *
   * @return a huszár típusát, ami {@link Type#KNIGHT}.
   */
  @Override
  public Type getType() {
    return Type.KNIGHT;
  }

  /**
   * Létrehoz egy új Knight objektumot az aktuális King példány alapján.
   *
   * @return egy új Knight objektum, amely az aktuális példány másolata
   */
  @Override
  public Piece clone() {
    return new Knight(color, pos);
  }

  /**
   * Visszaadja a huszár karakterét.
   *
   * @return 'N' karakter.
   */
  @Override
  public char getChar() {
    return 'N';
  }
}
