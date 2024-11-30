package main.java.com.controller;

import main.java.com.game.Board;
import main.java.com.game.Pawn;
import main.java.com.game.Piece;
import main.java.com.game.Piece.Type;
import main.java.com.game.Vec2;
import main.java.com.ui.BoardPanel;

public class BoardController {
  BoardPanel boardPanel;
  public Board board;
  static int boardSize = 8;
  Piece selectedPiece;
  Vec2 selectedTilePos;
  String result;

  public BoardController(BoardPanel boardPanel, String player1Name,
      String player2Name) {
    this.boardPanel = boardPanel;
    this.board = new Board(player1Name, player2Name, boardSize);
    board.setUpBoard();
  }

  public int getSize() {
    return boardSize;
  }

  public void updateBoard() {
      Pawn pawn = board.getPromotable() ;
      if(pawn != null){
        promotion(pawn);
      }

    if (selectedPiece != null && selectedTilePos != null) {
      result = board.MovePieceTo(selectedPiece, selectedTilePos);
      boardPanel.updateMovesPanel();
      if (result != null) {
        boardPanel.showResult(result);
      }

      selectedPiece = null;
      selectedTilePos = null;
    }
    boardPanel.clearImages();
    for (Piece piece : board.getPieces()) {
      boardPanel.addImageTo(piece.getImage(), piece.getPos().x,
          piece.getPos().y);
    }
  }

  public void updateSelected(Vec2 selectedPos) {
    if (board.hasPieceAt(selectedPos, board.getTurn())) {
      selectedPiece = board.getPieceAt(selectedPos, board.getTurn());
    }

    if (selectedPiece != null) {
      if (!selectedPiece.getPos().equals(selectedPos)) {
        selectedTilePos = selectedPos;
      }
    }
    updateBoard();
  }

  public void resign() {
    boardPanel.showResult(board.getNextPlayer().name);
  }

  public void promotion(Pawn pawn) {
    Type type = boardPanel.promptForPromotion();
    pawn.promoteTo(type, board);
  }
}