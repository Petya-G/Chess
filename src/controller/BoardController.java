package controller;

import javax.swing.ImageIcon;

import game.Board;
import game.Piece;
import game.Vec2;
import ui.BoardPanel;
import ui.Window;

public class BoardController {
	BoardPanel boardPanel;
	Board board;
	Window window;
	static int boardSize = 8;
	Piece selectedPiece;
	Vec2 selectedTilePos;

	public BoardController() {
		this.boardPanel = new BoardPanel(this);
		this.window = new Window(boardPanel);
		this.board = new Board("player1", "player2", boardSize, null);
		board.setUpBoard();
	}

	public int getSize() {
		return boardSize;
	}

	public void updateBoard() {
		if (selectedPiece != null && selectedTilePos != null) {
			board.MovePieceTo(selectedPiece, selectedTilePos);
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

	public static void main(String[] args) {
		BoardController bc = new BoardController();
		bc.updateBoard();
	}
}