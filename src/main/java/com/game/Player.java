package main.java.com.game;

import java.util.ArrayList;
import java.util.List;
import main.java.com.game.Board.Corner;
import main.java.com.game.Piece.Color;
import main.java.com.game.Piece.Type;

public class Player {
  public String name;
  Color color;
  List<Piece> pieces;

  public Player(String name, Color color) {
    this.color = color;
    this.name = name;
    this.pieces = new ArrayList<>();
  }

  public void setUpPieces(int size, Corner corner) {
    if (color == Color.WHITE) {
      for (int i = 0; i < size; i++) {
        pieces.add(new Pawn(color, new Vec2(i, 1)));
      }
      pieces.add(new Rook(color, new Vec2(0, 0)));
      // pieces.add(new Knight(color, new Vec2(1, 0)));
      // pieces.add(new Bishop(color, new Vec2(2, 0)));
      // pieces.add(new Queen(color, new Vec2(3, 0)));
      pieces.add(new King(color, new Vec2(4, 0)));
      // pieces.add(new Bishop(color, new Vec2(5, 0)));
      // pieces.add(new Knight(color, new Vec2(6, 0)));
      pieces.add(new Rook(color, new Vec2(7, 0)));

    } else {
      for (int i = 0; i < 1; i++) {
        pieces.add(new Pawn(color, new Vec2(i, 6)));
      }
      pieces.add(new Rook(color, new Vec2(0, 7)));
      pieces.add(new Knight(color, new Vec2(1, 7)));
      pieces.add(new Bishop(color, new Vec2(2, 7)));
      pieces.add(new Queen(color, new Vec2(3, 7)));
      pieces.add(new King(color, new Vec2(4, 7)));
      pieces.add(new Bishop(color, new Vec2(5, 7)));
      pieces.add(new Knight(color, new Vec2(6, 7)));
      pieces.add(new Rook(color, new Vec2(7, 7)));

      pieces.add(new Pawn(color, new Vec2(3, 3)));
    }
  }

  public List<Piece> getPieces() {
    return pieces;
  }

  public void removePiece(Piece piece) {
    pieces.remove(piece);
  }

  public void addPiece(Piece piece) {
    pieces.add(piece);
  }

  public Piece getPieceAt(Vec2 pos, Color color) {
    return pieces.stream()
        .filter(p -> p.pos.equals(pos) && p.color == color)
        .findFirst()
        .orElse(null);
  }

  public Piece getPiece(Piece.Type type) {
    return pieces.stream()
        .filter(p -> p.getType() == type)
        .findFirst()
        .orElse(null);
  }

  public int countType(Piece.Type type) {
    if (type == null)
      return pieces.size();
    return (int) pieces.stream().filter(p -> p.getType() == type).count();
  }

  public boolean isChecked(Board board, int turn) {
    King king = (King) getPiece(Type.KING);
    List<Vec2> moves = new ArrayList<>();

    if (color == Color.WHITE) {
      for (Piece p : board.player2.getPieces()) {
        moves.addAll(p.getMoves(board));
      }
    } else {
      for (Piece p : board.player1.getPieces()) {
        moves.addAll(p.getMoves(board));
      }
    }

    for (Vec2 move : moves) {
      if (move.equals(king.getPos()))
        return true;
    }

    return false;
  }

  public boolean isMoveChecked(Board board, Piece primary, Vec2 pPos, Piece secondary,
      Vec2 sPos) {
    Piece oPrimary = primary;
    removePiece(primary);

    Piece nPrimary = primary.clone();
    nPrimary.pos = pPos;
    addPiece(nPrimary);

    boolean checked;
    if (secondary == null && sPos == null) {
      checked = isChecked(board, board.turn);
    }

    else {
      Piece oSecondary = secondary.clone();

      if (sPos == null) {
        board.getNextPlayer().removePiece(secondary);
        checked = isChecked(board, board.turn);
        board.getNextPlayer().addPiece(oSecondary);
      }

      else {
        board.getPlayer().removePiece(secondary);
        Piece nSecondary = oSecondary.clone();
        nSecondary.pos = sPos;
        board.getPlayer().addPiece(nSecondary);

        checked = isChecked(board, board.turn);
        board.getPlayer().removePiece(nSecondary);
        board.getPlayer().addPiece(oSecondary);
      }
    }

    removePiece(nPrimary);
    addPiece(oPrimary);

    return checked;
  }
}