package test.java.com;

import main.java.com.game.Board;
import main.java.com.game.King;
import main.java.com.game.Piece;
import main.java.com.game.Piece.Color;
import main.java.com.game.Player;
import main.java.com.game.Vec2;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class BoardTest {
    private Board board;

    @Before
    public void setUp() {
        board = new Board("Player1", "Player2", 8);
        board.setUpBoard();
    }

    @Test
    public void testBoardInitialization() {
        assertEquals("Player1", board.player1.name);
        assertEquals("Player2", board.player2.name);
        assertEquals(Color.WHITE, board.getTurn());
    }

    @Test
    public void testSetUpBoard() {
        assertFalse(board.getPieces().isEmpty());
        assertEquals(32, board.getPieces().size()); // 16 pieces for each player
    }

    @Test
    public void testIsWithinBounds() {
        assertTrue(board.isWithinBounds(new Vec2(0, 0)));
        assertTrue(board.isWithinBounds(new Vec2(7, 7)));
        assertFalse(board.isWithinBounds(new Vec2(-1, 0)));
        assertFalse(board.isWithinBounds(new Vec2(8, 8)));
    }

    @Test
    public void testClipMovesToBoard() {
        List<Vec2> moves = List.of(new Vec2(0, 0), new Vec2(7, 7), new Vec2(-1, 0), new Vec2(8, 8));
        List<Vec2> clippedMoves = board.clipMovesToBoard(moves);
        assertEquals(2, clippedMoves.size());
        assertTrue(clippedMoves.contains(new Vec2(0, 0)));
        assertTrue(clippedMoves.contains(new Vec2(7, 7)));
    }

    @Test
    public void testGetTurn() {
        assertEquals(Color.WHITE, board.getTurn());
    }

    @Test
    public void testGetPlayer() {
        assertEquals(board.player1, board.getPlayer(Color.WHITE));
        assertEquals(board.player2, board.getPlayer(Color.BLACK));
    }

    @Test
    public void testHasPieceAt() {
        Vec2 pos = new Vec2(0, 1);
        assertTrue(board.hasPieceAt(pos, Color.BLACK));
        assertFalse(board.hasPieceAt(pos, Color.WHITE));
    }

    @Test
    public void testGetPieceAt() {
        Vec2 pos = new Vec2(0, 1);
        assertNotNull(board.getPieceAt(pos, Color.BLACK));
        assertNull(board.getPieceAt(pos, Color.WHITE));
    }

    @Test
    public void testIsDraw() {
        Board board2 = new Board("Player1", "Player2", 8);
        board2.player1.addPiece(new King(Color.WHITE, new Vec2(0, 0)));
        board2.player2.addPiece(new King(Color.BLACK, new Vec2(7, 7)));
        assertTrue(board2.isDraw());
    }

    @Test
    public void testMovePieceTo() {
        Vec2 startPos = new Vec2(1, 6);
        Vec2 endPos = new Vec2(1, 5);
        Piece piece = board.getPieceAt(startPos, Color.WHITE);
        assertNotNull(piece);
        String result = board.movePieceTo(piece, endPos);
        assertNull(result);
        assertEquals(endPos, piece.getPos());
    }
}
