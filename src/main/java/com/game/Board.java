package main.java.com.game;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
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
        pgnMove.append((int) Math.floor((turn + 2) / 2) + ".");
        pgnMove.append(piece.lastMove);
        moves.add(pgnMove.toString());
      }

      else {
        String lastMove = moves.getLast();
        StringBuilder pgnMove = new StringBuilder(lastMove);
        pgnMove.append(' ');
        pgnMove.append(piece.lastMove);
        pgnMove.append(' ');

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

  public void saveBoardTo(File fileToSave) {
    try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileToSave))) {
      writer.write("[Event \"Unnamed\"]");
      writer.newLine();
      writer.write("[Site \"?\"]");
      writer.newLine();
      writer.write("[Date \"" + java.time.LocalDate.now() + "\"]");
      writer.newLine();
      writer.write("[Round \"1\"]");
      writer.newLine();
      writer.write("[White \"" + player1.name + "\"]");
      writer.newLine();
      writer.write("[Black \"" + player2.name + "\"]");
      writer.newLine();
      writer.write("\n");

      for (int i = 0; i < moves.size(); i++) {
        writer.write(moves.get(i));
      }
      writer.write("\n");

    } catch (IOException e) {
      e.printStackTrace();
      System.out.println("Error saving the board to file: " + e.getMessage());
    }
  }

  public void loadBoardFrom(File fileToLoad) throws IOException {
    List<String> lines = Files.readAllLines(fileToLoad.toPath());

    String player1Name = null, player2Name = null;
    for (String line : lines) {
      if (line.startsWith("[White ")) {
        player1Name = line.substring(line.indexOf('"') + 1, line.lastIndexOf('"'));
      }

      else if (line.startsWith("[Black ")) {
        player2Name = line.substring(line.indexOf('"') + 1, line.lastIndexOf('"'));
      }
    }
    player1.name = player1Name;
    player2.name = player2Name;

    for (String line : lines) {
      if (line.matches("\\d.*")) {
        String[] parts = line.split("\\s+");
        for (int i = 0; i < parts.length; i++) {
          Color color = i % 3 == 1 ? Color.WHITE : Color.BLACK;
          String regex = "\\d+\\.|\\+|#";
          String moveString = parts[i].replaceAll(regex, "");
          Vec2 move = null;
          Vec2 pos = null;
          Type type = null;

          if (moveString.matches("[a-h][1-8]")) {
            move = new Vec2(moveString);
            type = Type.PAWN;
          } else if (moveString.matches("[a-h]?[1-8]?x[a-h][1-8]")) {
            String[] s = moveString.split("x");
            String posString = s[0].substring(1);
            if (posString.length() != 0)
              pos = new Vec2(posString);
            move = new Vec2(s[1]);
            type = Type.PAWN;
          } else if (moveString.matches("[a-h][1-8]=[QRBN]")) {
            move = new Vec2(moveString.substring(0, 2));
            type = Type.PAWN;
          } else if (moveString.matches("[NBRQK][a-h]?[1-8]?[x][a-h][1-8]")) {
            String[] s = moveString.split("x");
            type = Type.getType(s[0].charAt(0));
            String posString = s[0].substring(1);
            if (posString.length() != 0)
              pos = new Vec2(posString);
            move = new Vec2(s[1]);
          } else if (moveString.matches("[NBRQK][a-h]?[1-8]?[a-h][1-8]")) {
            String[] s = splitString(moveString);
            type = Type.getType(s[0].charAt(0));
            String posString = s[0].substring(1);
            if (posString.length() != 0)
              pos = new Vec2(posString);
            move = new Vec2(s[1]);
          } else if (moveString.equals("O-O")) {
            type = Type.KING;
            move = ((King) getPlayer().getPiece(type)).kingsideCastle(this);
          } else if (moveString.equals("O-O-O")) {
            type = Type.KING;
            move = ((King) getPlayer().getPiece(type)).queensideCastle(this);
          }

          if (move != null && type != null) {
            for (Piece p : getPlayer(color).getPiecesThatHaveMove(type, move, this)) {
              if (pos != null) {
                if (pos.equals(p.getPos())) {
                  p.move(move, this);
                }
              } else {
                p.move(move, this);
              }
            }
          } else {
            System.out.println("Invalid move: " + moveString);
          }

        }
      }
    }
  }

  public static String[] splitString(String s) {
    // Find the position of the last alphabetic character
    int lastAlphabeticIndex = -1;

    // Traverse the string to find the index of the last alphabetic character
    for (int i = 0; i < s.length(); i++) {
      if (Character.isLetter(s.charAt(i))) {
        lastAlphabeticIndex = i;
      }
    }

    // If a last alphabetic character was found
    if (lastAlphabeticIndex != -1) {
      String firstPart = s.substring(0, lastAlphabeticIndex); // Everything before the last alphabetic character
      String secondPart = s.substring(lastAlphabeticIndex); // The last alphabetic character and everything after

      return new String[] { firstPart, secondPart }; // Return the parts
    }
    return new String[2];
  }
}
