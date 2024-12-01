package test.java.com;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import main.java.com.game.Board;
import main.java.com.game.King;
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
        whitePawn = new Pawn(Color.WHITE, new Vec2(1, 6));
        blackPawn = new Pawn(Color.BLACK, new Vec2(1, 1));
        board.getPlayer().addPiece(whitePawn);
        board.getNextPlayer().addPiece(blackPawn);

        board.player1.addPiece(new King(Color.WHITE, new Vec2(4, 7)));
        board.player2.addPiece(new King(Color.BLACK, new Vec2(4, 0)));
    }

    @Test
    public void testPawnConstructor() {
        assertNotNull(whitePawn);
        assertNotNull(blackPawn);
        assertEquals(Color.WHITE, whitePawn.getColor());
        assertEquals(Color.BLACK, blackPawn.getColor());
        assertEquals(new Vec2(1, 6), whitePawn.getPos());
        assertEquals(new Vec2(1, 1), blackPawn.getPos());
    }

    @Test
    public void testGetType() {
        assertEquals(Type.PAWN, whitePawn.getType());
        assertEquals(Type.PAWN, blackPawn.getType());
    }

    @Test
    public void testClone() {
        Pawn clonedPawn = whitePawn.clone();
        assertNotNull(clonedPawn);
        assertEquals(whitePawn.getColor(), clonedPawn.getColor());
        assertEquals(whitePawn.getPos(), clonedPawn.getPos());
        assertEquals(whitePawn.getType(), clonedPawn.getType());
    }

    @Test
    public void testMove() {
        Vec2 newPos = new Vec2(1, 5);
        assertTrue(whitePawn.move(newPos, board));
        assertEquals(newPos, whitePawn.getPos());

        newPos = new Vec2(1, 2);
        assertFalse(whitePawn.move(newPos, board)); // Invalid move
    }

    @Test
    public void testGetMoves() {
        assertEquals(2, whitePawn.getMoves(board).size());
        assertEquals(2, blackPawn.getMoves(board).size());
    }

    @Test
    public void testIsPromotable() {
        whitePawn.setPos(new Vec2(1, 0));
        assertTrue(whitePawn.isPromotable());

        blackPawn.setPos(new Vec2(1, 7));
        assertTrue(blackPawn.isPromotable());
    }

    @Test
    public void testPromoteTo() {
        whitePawn.setPos(new Vec2(1, 7));
        whitePawn.promoteTo(Type.QUEEN, board);
        Piece promotedPiece = board.getPlayer().getPieceAt(new Vec2(1, 7), Color.WHITE);
        assertNotNull(promotedPiece);
        assertEquals(Type.QUEEN, promotedPiece.getType());
    }
}
