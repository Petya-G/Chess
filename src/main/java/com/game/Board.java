package main.java.com.game;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import main.java.com.game.Piece.Color;
import main.java.com.game.Piece.Type;

public class Board {
  public Player player1, player2;
  int size;
  Color turnColor;
  int turn;
  public List<String> moves;

  public Board(String player1Name, String player2Name, int size) {
    this.player1 = new Player(player1Name, Color.WHITE);
    this.player2 = new Player(player2Name, Color.BLACK);
    this.size = size;
    this.turnColor = Color.WHITE;
    this.turn = 0;
    moves = new ArrayList<>();
  }

  public void setUpBoard() {
    player1.setUpPieces(size);
    player2.setUpPieces(size);
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
    return moves.stream().filter(m -> isWithinBounds(m)).collect(Collectors.toList());
  }

  public Color getTurn() {
    return turn % 2 == 0 ? Color.WHITE : Color.BLACK;
  }

  public Player getPlayer() {
    return getPlayer(getTurn());
  }

  public Player getNextPlayer() {
    return getPlayer((turn + 1) % 2 == 0 ? Color.WHITE : Color.BLACK);
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

  public Pawn getPromotable() {
    return (Pawn) getPlayer().pieces.stream()
        .filter(p -> p instanceof Pawn)
        .map(p -> (Pawn) p)
        .filter(Pawn::isPromotable)
        .findFirst()
        .orElse(null);
  }

  public String MovePieceTo(Piece piece, Vec2 pos) {
    if (isDraw()) {
      return "Draw";
    }

    if (piece.move(pos, this)) {
      if (piece.getColor() == Color.WHITE) {
        StringBuilder pgnMove = new StringBuilder();
        pgnMove.append((int) Math.floor((turn + 2) / 2) + ". ");
        pgnMove.append(piece.lastMove);
        moves.add(pgnMove.toString());
      }

      else {
        String lastMove = moves.getLast();
        StringBuilder pgnMove = new StringBuilder(lastMove); 
        pgnMove.append(" "); 
        pgnMove.append(piece.lastMove); 

        moves.removeLast();
        moves.add(pgnMove.toString());
      }

      Piece attacked = getPieceAt(pos, piece.getOppositeColor());
      if (attacked != null) {
        getNextPlayer().removePiece(attacked);
      }

      changeTurn();
      if (getPlayer().isCheckMate(pos, this)) {
        changeTurn();
        return getNextPlayer().name;
      }

    }
    return null;
  }
}