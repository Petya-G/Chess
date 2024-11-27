package controller;

import game.Board;
import game.Piece;
import game.Vec2;
import ui.BoardPanel;
import ui.Window;

public class BoardController {
	BoardPanel boardPanel;
	public Board board;
	Window window;
	static int boardSize = 8;
	Piece selectedPiece;
	Vec2 selectedTilePos;

	public BoardController(BoardPanel boardPanel) {
		this.boardPanel = boardPanel;
		this.window = new Window(boardPanel);
		this.board = new Board("player1", "player2", boardSize, null);
		board.setUpBoard();
	}

	public int getSize() {
		return boardSize;
	}

	public void updateBoard() {
		if (selectedPiece != null && selectedTilePos != null) {
			if(board.MovePieceTo(selectedPiece, selectedTilePos) != null) {
				
			}
			selectedPiece = null;
			selectedTilePos = null;
		}
		boardPanel.clearImages();
		for (Piece piece : board.getPieces()) {
			boardPanel.addImageTo(piece.getImage(), piece.getPos().x, piece.getPos().y);
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


}