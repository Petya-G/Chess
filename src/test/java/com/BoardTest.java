package test.java.com;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import main.java.com.game.Board;
import main.java.com.game.Piece;
import main.java.com.game.Piece.Color;
import main.java.com.game.Vec2;

public class BoardTest {

    private Board board;

    @Before
    public void setUp() {
        board = new Board("Player1", "Player2", 8);
        board.setUpBoard();
    }

    @Test
    public void testSettingUpBoard() {
        assertFalse(board.getPieces().isEmpty());

        assertTrue(board.getPieces().stream().anyMatch(piece -> piece.getColor() == Color.WHITE));

        assertTrue(board.getPieces().stream().anyMatch(piece -> piece.getColor() == Color.BLACK));
    }

    @Test
    public void testIsWithinBounds() {
        Vec2 validMove = new Vec2(0, 0);
        Vec2 validMove2 = new Vec2(7, 7);
        assertTrue(board.isWithinBounds(validMove));
        assertTrue(board.isWithinBounds(validMove2));

        Vec2 invalidMove1 = new Vec2(-1, 0);
        Vec2 invalidMove2 = new Vec2(8, 8);
        assertFalse(board.isWithinBounds(invalidMove1));
        assertFalse(board.isWithinBounds(invalidMove2));
    }

    @Test
    public void testMovePieceTo() {
        Piece pieceToMove = board.getPlayer().getPieces().get(0);
        Vec2 targetPosition = new Vec2(0, 2);

        String result = board.MovePieceTo(pieceToMove, targetPosition);
        assertNull(result);

        assertTrue(board.hasPieceAt(targetPosition, pieceToMove.getColor()));

        assertEquals(Color.BLACK, board.getTurn());
    }
}