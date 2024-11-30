package test.java.com;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import main.java.com.game.Board;
import main.java.com.game.Pawn;
import main.java.com.game.Piece;
import main.java.com.game.Vec2;
import main.java.com.game.Piece.Color;
import main.java.com.game.Piece.Type;

public class PawnTest {
    private Board board;
    private Pawn whitePawn;
    private Pawn blackPawn;

    @Before
    public void setUp() {
        board = new Board("Player1", "Player2", 8);
        board.setUpBoard();
        whitePawn = (Pawn) board.getPieceAt(new Vec2(0, 1), Color.WHITE);
        blackPawn = (Pawn) board.getPieceAt(new Vec2(1, 6), Color.BLACK);
    }

    @Test
    public void testMoveForwardOneSpace() {
        Vec2 newPos = whitePawn.moveOne(board);
        assertNotNull(newPos);
        boolean moved = whitePawn.move(newPos, board);
        assertTrue(moved);
        assertEquals(newPos, whitePawn.getPos());
    }

    @Test
    public void testMoveForwardTwoSpacesFirstMove() {
        Vec2 newPos = whitePawn.moveTwo(board);
        assertNotNull(newPos);
        boolean moved = whitePawn.move(newPos, board);
        assertTrue(moved);
        assertEquals(newPos, whitePawn.getPos());
    }

    @Test
    public void testCaptureDiagonally() {
        Vec2 capturePos = new Vec2(1, 2);
        board.player2.addPiece(new Pawn(Color.BLACK, capturePos));
        Vec2 newPos = whitePawn.getMoves(board).stream()
                .filter(move -> move.equals(capturePos))
                .findFirst()
                .orElse(null);

        assertNotNull(newPos);

        boolean moved = whitePawn.move(newPos, board);
        assertTrue(moved);
        assertEquals(newPos, whitePawn.getPos());
    }

    @Test
    public void testEnPassantLeft() {
        Vec2 enPassantPos = new Vec2(1, 3);
        Pawn pawn = new Pawn(Color.BLACK, enPassantPos);
        board.player1.addPiece(pawn);

        whitePawn.move(new Vec2(0, 3), board);
        board.changeTurn();
        Vec2 newPos = pawn.enpassantLeft(board);
        assertNotNull(newPos);

        boolean moved = pawn.move(newPos, board);
        assertTrue(moved);
    }

    @Test
    public void testPromotion() {
        assertFalse(whitePawn.isPromotable());

        whitePawn.promoteTo(Type.QUEEN, board);
        Piece promotedPiece = board.getPieceAt(whitePawn.getPos(), Color.WHITE);

        assertNotNull(promotedPiece);
        assertEquals(Type.QUEEN, promotedPiece.getType());
    }
}