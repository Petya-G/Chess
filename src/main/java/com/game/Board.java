package main.java.com.game;

import java.util.ArrayList;
import java.util.List;
import main.java.com.game.Piece.Color;
import main.java.com.game.Piece.Type;

public class Board {
  enum Corner {
    TOPRIGHT,
    BOTTOMRIGHT,
    BOTTOMLEFT,
    TOPLEFT;
  }

  public Player player1, player2;
  int size;
  Corner corner;
  Color turnColor;
  int turn;

  public Board(String player1Name, String player2Name, int size,
      Corner corner) {
    this.player1 = new Player(player1Name, Color.WHITE);
    this.player2 = new Player(player2Name, Color.BLACK);
    this.size = size;
    this.turnColor = Color.WHITE;
    this.turn = 0;
  }

  public void setUpBoard() {
    player1.setUpPieces(size, corner);
    player2.setUpPieces(size, corner);
  }

  public List<Piece> getPieces() {
    List<Piece> pieces = new ArrayList<>();
    pieces.addAll(player1.getPieces());
    pieces.addAll(player2.getPieces());
    return pieces;
  }

  public boolean isWithinBounds(Vec2 move) {
    return move.x >= 0 && move.x < size && move.y >= 0 && move.y < size;
  }

  public List<Vec2> clipMovesToBoard(List<Vec2> moves) {
    for (Vec2 move : moves) {
      if (!isWithinBounds(move)) {
        moves.remove(move);
      }
    }
    return moves;
  }

  public Color getTurn() {
    return turn % 2 == 0 ? Color.WHITE : Color.BLACK;
  }

  public Player getPlayer() {
    return getPlayer(getTurn());
  }

  public Player getNextPlayer() {
    return getPlayer(turn + 1 % 2 == 0 ? Color.WHITE : Color.BLACK);
  }

  public Player getPlayer(Color color) {
    return color == Color.WHITE ? player1 : player2;
  }

  public void changeTurn() {
    turn++;
  }

  public boolean hasPieceAt(Vec2 pos, Color color) {
    for (Piece p : getPieces()) {
      if (pos.equals(p.getPos()) && color == p.getColor()) {
        return true;
      }
    }
    return false;
  }

  public boolean hasPieceAt(Vec2 pos) {
    for (Piece p : getPieces()) {
      if (pos.equals(p.getPos())) {
        return true;
      }
    }
    return false;
  }

  public Piece getPieceAt(Vec2 pos, Color color) {
    for (Piece p : getPieces()) {
      if (pos.equals(p.getPos()) && color == p.getColor()) {
        return p;
      }
    }
    return null;
  }

  public void removePiece(Piece p) {
    if (p.getColor() == Color.WHITE)
      player1.removePiece(p);
    else
      player2.removePiece(p);
  }

  public boolean isOnSameColoredTile(Piece p1, Piece p2) {
    return (p1.pos.x + p1.pos.y % 2) == (p2.pos.x + p2.pos.y % 2);
  }

  public boolean isDraw() {
    if (player1.countType(null) == 1 && player2.countType(null) == 1)
      return true;
    if (player1.countType(null) == 1 && player2.countType(null) == 2 &&
        player2.countType(Type.BISHOP) == 1)
      return true;
    if (player2.countType(null) == 1 && player1.countType(null) == 2 &&
        player1.countType(Type.BISHOP) == 1)
      return true;

    if (player1.countType(null) == 1 && player2.countType(null) == 2 &&
        player2.countType(Type.KNIGHT) == 1)
      return true;
    if (player2.countType(null) == 1 && player1.countType(null) == 2 &&
        player1.countType(Type.KNIGHT) == 1)
      return true;

    if (player1.countType(null) == 2 && player1.countType(Type.BISHOP) == 1 &&
        player2.countType(null) == 2 && player2.countType(Type.BISHOP) == 1 &&
        isOnSameColoredTile(player1.getPiece(Type.BISHOP),
            player2.getPiece(Type.BISHOP)))
      return true;
    return false;
  }

  public String MovePieceTo(Piece piece, Vec2 pos) {
    if (getPlayer().isChecked(this, turn)) {
      if (((King) getPlayer().getPiece(Type.KING))
          .getMovesNotChecked(pos, turn, this)
          .size() == 0) {
        changeTurn();
        return getNextPlayer().name;
      }
    }

    if (isDraw()) {
      return "Draw";
    }

    Piece attacked = getPieceAt(pos, piece.getOppositeColor());

    if (piece.move(pos, this)) {
      if (attacked != null) {
        if (piece.getOppositeColor() == Color.WHITE)
          player1.removePiece(attacked);
        else
          player2.removePiece(attacked);
      }
      changeTurn();
    }
    return null;
  }
}