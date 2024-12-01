package main.java.com.controller;

import java.io.File;
import java.io.IOException;

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

  /**
   * Létrehoz egy új BoardController példányt a megadott játéktábla panellel és
   * játékosnevekkel.
   *
   * @param boardPanel  A játék tábla megjelenítő panelje.
   * @param player1Name Az első játékos neve.
   * @param player2Name A második játékos neve.
   */
  public BoardController(BoardPanel boardPanel, String player1Name, String player2Name) {
    this.boardPanel = boardPanel;
    this.board = new Board(player1Name, player2Name, boardSize);
    board.setUpBoard();
  }

  /**
   * Visszaadja a játéktábla méretét.
   *
   * @return A tábla mérete.
   */
  public int getSize() {
    return boardSize;
  }

  /**
   * Frissíti a tábla állapotát, kezelve a bábuk mozgatását és a promóciót.
   */
  public void updateBoard() {
    Pawn pawn = board.getPromotable();
    if (pawn != null) {
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
      boardPanel.addImageTo(piece.getImage(), piece.getPos().x, piece.getPos().y);
    }
  }

  /**
   * Frissíti a kiválasztott bábút és a célpozíciót a felhasználó választása
   * alapján.
   *
   * @param selectedPos A felhasználó által kiválasztott pozíció.
   */
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

  /**
   * Kezeli a jelenlegi játékos lemondását, és megjeleníti az eredményt.
   */
  public void resign() {
    boardPanel.showResult(board.getNextPlayer().name);
  }

  /**
   * Promotálja a bábút a megadott bábútípusra.
   *
   * @param pawn A promotálni kívánt bábú.
   */
  public void promotion(Pawn pawn) {
    Type type = boardPanel.promptForPromotion();
    pawn.promoteTo(type, board);
  }

  /**
   * Elmenti a jelenlegi tábla állapotát a megadott fájlba.
   *
   * @param fileToSave A fájl, ahová a tábla állapotát el kell menteni.
   */
  public void saveBoardTo(File fileToSave) {
    board.saveBoardTo(fileToSave);
  }

  /**
   * Betölti a tábla állapotát a megadott fájlból.
   *
   * @param selectedFile A fájl, ahonnan a tábla állapotát be kell tölteni.
   */
  public void loadBoardFrom(File selectedFile) {
    try {
      board.loadBoardFrom(selectedFile);
    } catch (IOException e) {
      e.printStackTrace();
      System.out.println(e.getMessage());
    }
    boardPanelInit();
  }

  /**
   * Inicializálja a tábla panelt egy új játékhoz vagy egy betöltött játék után.
   */
  public void boardPanelInit() {
    boardPanel.init();
  }
}