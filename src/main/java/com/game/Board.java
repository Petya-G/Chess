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

  public Color getTurn() { return turn % 2 == 0 ? Color.WHITE : Color.BLACK; }

  public Player getCurrentPlayer() { return getPlayer(getTurn()); }

  public Player getNextPlayer() {
    return getPlayer(turn + 1 % 2 == 0 ? Color.WHITE : Color.BLACK);
  }

  public Player getPlayer(Color color) {
    return color == Color.WHITE ? player1 : player2;
  }

  public void changeTurn() { turn++; }

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

  public boolean isMoveChecked(Piece primary, Vec2 pPos, Piece secondary,
                               Vec2 sPos) {
    Piece oPrimary = primary;
    getCurrentPlayer().removePiece(primary);

    Piece nPrimary = primary.clone();
    nPrimary.pos = pPos;
    getCurrentPlayer().addPiece(nPrimary);

    boolean checked;
    if (secondary == null && sPos == null) {
      checked = getCurrentPlayer().isChecked(this, turn);
    }

    else {
      Piece oSecondary = secondary.clone();
      getCurrentPlayer().removePiece(oSecondary);

      if (sPos == null) {
        checked = getCurrentPlayer().isChecked(this, turn);
      }

      else {
        Piece nSecondary = secondary.clone();
        nSecondary.pos = sPos;
        getCurrentPlayer().addPiece(nSecondary);

        checked = getCurrentPlayer().isChecked(this, turn);
        getCurrentPlayer().removePiece(nSecondary);
      }
      getCurrentPlayer().addPiece(oSecondary);
    }

    getCurrentPlayer().removePiece(nPrimary);
    getCurrentPlayer().addPiece(oPrimary);

    return checked;
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
    if (getCurrentPlayer().isChecked(this, turn)) {
      System.out.println(getCurrentPlayer().name + " checked");
      if (piece.getType() != Type.KING) {
        return null;
      }

      if (((King)getCurrentPlayer().getPiece(Type.KING))
              .getMovesNotChecked(pos, turn, this)
              .size() == 0) {
        changeTurn();
        return getNextPlayer().name;
      }
    }

    if(isDraw()){
      return "Draw";
    }

    boolean moved = false;
    Piece attacked = getPieceAt(pos, piece.getOppositeColor());

    if (piece.getType() == Type.PAWN) {
      Pawn pawn = (Pawn)piece;
      if (pos.equals(pawn.moveOne(this))) {
        if (!isMoveChecked(pawn, pos, null, null)) {
          pawn.pos = pos;
          pawn.moved2 = turn;
          moved = true;
        }
      } else if (pos.equals(pawn.moveTwo(this))) {
        if (!isMoveChecked(pawn, pos, null, null)) {
          pawn.pos = pos;
          pawn.moved2 = turn;
          moved = true;
        }
      } else if (pos.equals(pawn.enpassantLeft(this, turn))) {
        attacked = getPieceAt(new Vec2(pawn.pos.x - 1, pawn.pos.y),
                              pawn.getOppositeColor());

        if (!isMoveChecked(pawn, pos, attacked, null)) {
          pawn.pos = pos;
          moved = true;
        }
      } else if (pos.equals(pawn.enpassantRight(this, turn))) {
        attacked = getPieceAt(new Vec2(pawn.pos.x + 1, pawn.pos.y),
                              pawn.getOppositeColor());

        if (!isMoveChecked(pawn, pos, attacked, null)) {
          pawn.pos = pos;
          moved = true;
        }
      } else if (piece.getMoves(this, turn).contains(pos)) {

        if (!isMoveChecked(pawn, pos, attacked, null)) {
          piece.pos = pos;
          moved = true;
        }
      }
    }

    else if (piece.getType() == Type.KING) {
      King king = (King)piece;
      if (pos.equals(king.kingsideCastle(this))) {
        Rook rook = king.getKingsideRook(this);
        king.pos = pos;
        rook.pos = king.getKingsideRookPos();

        if (!isMoveChecked(king, pos, rook, rook.pos)) {
          rook.firstMove = false;
          moved = true;
        }
      } else if (pos.equals(king.queensideCastle(this))) {
        Rook rook = king.getQueensideRook(this);
        king.pos = pos;
        rook.pos = king.getQueensideRookPos();
        if (!isMoveChecked(king, pos, rook, rook.pos)) {
          rook.firstMove = false;
          moved = true;
        }
      } else if (piece.getMoves(this, turn).contains(pos)) {
        if (!isMoveChecked(king, pos, null, null)) {
          piece.pos = pos;
          moved = true;
        }
      }
    }

    else if (piece.getMoves(this, turn).contains(pos)) {
      if (!isMoveChecked(piece, pos, null, null)) {
        piece.pos = pos;
        moved = true;
      }
    }

    if (moved) {
      if (attacked != null) {
        if (piece.getOppositeColor() == Color.WHITE)
          player1.removePiece(attacked);
        else
          player2.removePiece(attacked);
      }
      piece.firstMove = false;
      changeTurn();
    }
    return null;
  }
}