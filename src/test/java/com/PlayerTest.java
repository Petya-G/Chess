package test.java.com;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import main.java.com.game.Board;
import main.java.com.game.Pawn;
import main.java.com.game.Piece.Color;
import main.java.com.game.Player;
import main.java.com.game.Queen;
import main.java.com.game.Vec2;
import main.java.com.game.Piece;

public class PlayerTest {
    private Player playerWhite;
    private Player playerBlack;
    private Board board;

    @Before
    public void setUp() {
        board = new Board("WhitePlayer", "BlackPlayer", 8);
        board.setUpBoard();
        playerWhite = new Player("WhitePlayer", Color.WHITE);
        playerBlack = new Player("BlackPlayer", Color.BLACK);
        playerWhite.setUpPieces(8);
        playerBlack.setUpPieces(8);
    }

    @Test
    public void testSetUpPieces() {
        assertEquals(8, playerWhite.countType(Piece.Type.PAWN));
        assertNotNull(playerWhite.getPiece(Piece.Type.KING));
        assertNotNull(playerBlack.getPiece(Piece.Type.KING));
        assertTrue(playerBlack.getPieces().stream()
                .noneMatch(piece -> piece.getColor() == Color.WHITE));
    }

    @Test
    public void testRemovePiece() {
        Piece pawn = playerWhite.getPieces().get(0);
        playerWhite.removePiece(pawn);
        assertFalse(playerWhite.getPieces().contains(pawn));
        assertEquals(7, playerWhite.countType(Piece.Type.PAWN));
    }
}
