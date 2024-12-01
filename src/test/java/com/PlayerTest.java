package test.java.com;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import main.java.com.game.Board;
import main.java.com.game.Piece.Color;
import main.java.com.game.Piece;
import main.java.com.game.Piece.Type;
import main.java.com.game.Vec2;
import main.java.com.game.Pawn;
import main.java.com.game.King;
import java.util.List;

public class PlayerTest {
    private Board board;

    @Before
    public void setUp() {
        board = new Board("Player1", "Player2", 8);
    }

    @Test
    public void testPlayerConstructor() {
        assertEquals("Player1", board.player1.name);
        assertEquals(Color.WHITE, board.player1.getColor());
        assertNotNull(board.player1.getPieces());
        assertTrue(board.player1.getPieces().isEmpty());

        assertEquals("Player2", board.player2.name);
        assertEquals(Color.BLACK, board.player2.getColor());
        assertNotNull(board.player2.getPieces());
        assertTrue(board.player2.getPieces().isEmpty());
    }

    @Test
    public void testSetUpPieces() {
        board.setUpBoard();

        assertEquals(16, board.player1.getPieces().size());
        assertEquals(16, board.player2.getPieces().size());

        assertTrue(board.player1.getPieces().stream().anyMatch(p -> p instanceof King));
        assertTrue(board.player2.getPieces().stream().anyMatch(p -> p instanceof King));
    }

    @Test
    public void testGetPieces() {
        board.player1.setUpPieces(8);
        List<Piece> pieces = board.player1.getPieces();
        assertEquals(16, pieces.size());
    }

    @Test
    public void testRemovePiece() {
        board.player1.setUpPieces(8);
        Piece piece = board.player1.getPieces().get(0);
        board.player1.removePiece(piece);
        assertEquals(15, board.player1.getPieces().size());
    }

    @Test
    public void testAddPiece() {
        Piece piece = new Pawn(Color.WHITE, new Vec2(0, 1));
        board.player1.addPiece(piece);
        assertEquals(1, board.player1.getPieces().size());
    }

    @Test
    public void testGetPieceAt() {
        board.player1.setUpPieces(8);
        Piece piece = board.player1.getPieceAt(new Vec2(0, 6), Color.WHITE);
        assertNotNull(piece);
        assertTrue(piece instanceof Pawn);
    }

    @Test
    public void testGetPiece() {
        board.player1.setUpPieces(8);
        Piece piece = board.player1.getPiece(Type.KING);
        assertNotNull(piece);
        assertTrue(piece instanceof King);
    }

    @Test
    public void testCountType() {
        board.player1.setUpPieces(8);
        int count = board.player1.countType(Type.PAWN);
        assertEquals(8, count);
    }

    @Test
    public void testIsChecked() {
        board.player1.setUpPieces(8);
        board.player2.setUpPieces(8);
        boolean isChecked = board.player1.isChecked(board, 0);
        assertFalse(isChecked);
    }

    @Test
    public void testIsMoveChecked() {
        board.player1.setUpPieces(8);
        board.player2.setUpPieces(8);
        Piece primary = board.player1.getPiece(Type.KING);
        Vec2 pPos = new Vec2(4, 5);
        boolean isMoveChecked = board.player1.isMoveChecked(board, primary, pPos, null, null);
        assertFalse(isMoveChecked);
    }

    @Test
    public void testIsCheckMate() {
        board.player1.setUpPieces(8);
        board.player2.setUpPieces(8);
        boolean isCheckMate = board.player1.isCheckMate(board);
        assertFalse(isCheckMate);
    }

    @Test
    public void testGetPiecesThatHaveMove() {
        board.player1.setUpPieces(8);
        List<Piece> pieces = board.player1.getPiecesThatHaveMove(Type.PAWN, new Vec2(0, 5), board);
        assertTrue(!pieces.isEmpty());
    }
}
