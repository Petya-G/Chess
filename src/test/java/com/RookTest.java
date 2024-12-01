package test.java.com;

import main.java.com.game.*;
import main.java.com.game.Piece.Color;
import main.java.com.game.Piece.Type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;

public class RookTest {
    @Test
    public void testRookConstructor() {
        Vec2 pos = new Vec2(0, 0);
        Rook rookWhite = new Rook(Color.WHITE, pos);
        Rook rookBlack = new Rook(Color.BLACK, pos);

        assertEquals(Color.WHITE, rookWhite.getColor());
        assertEquals(Color.BLACK, rookBlack.getColor());
        assertEquals(pos, rookWhite.getPos());
        assertEquals(pos, rookBlack.getPos());
        assertNotNull(rookWhite.getImage());
        assertNotNull(rookBlack.getImage());
    }

    @Test
    public void testGetMoves() {
        Board board = new Board("Player1", "Player2", 8);
        Rook rook = new Rook(Color.WHITE, new Vec2(0, 0));
        board.player1.addPiece(rook);

        List<Vec2> moves = rook.getMoves(board);
        assertTrue(moves.contains(new Vec2(0, 1)));
        assertTrue(moves.contains(new Vec2(0, 2)));
        assertTrue(moves.contains(new Vec2(0, 3)));
        assertTrue(moves.contains(new Vec2(0, 4)));
        assertTrue(moves.contains(new Vec2(0, 5)));
        assertTrue(moves.contains(new Vec2(0, 6)));
        assertTrue(moves.contains(new Vec2(0, 7)));
        assertTrue(moves.contains(new Vec2(1, 0)));
        assertTrue(moves.contains(new Vec2(2, 0)));
        assertTrue(moves.contains(new Vec2(3, 0)));
        assertTrue(moves.contains(new Vec2(4, 0)));
        assertTrue(moves.contains(new Vec2(5, 0)));
        assertTrue(moves.contains(new Vec2(6, 0)));
        assertTrue(moves.contains(new Vec2(7, 0)));
    }

    @Test
    public void testGetType() {
        Rook rook = new Rook(Color.WHITE, new Vec2(0, 0));
        assertEquals(Type.ROOK, rook.getType());
    }

    @Test
    public void testClone() {
        Rook rook = new Rook(Color.WHITE, new Vec2(0, 0));
        Rook clonedRook = rook.clone();

        assertEquals(rook.getColor(), clonedRook.getColor());
        assertEquals(rook.getPos(), clonedRook.getPos());
    }

    @Test
    public void testGetChar() {
        Rook rook = new Rook(Color.WHITE, new Vec2(0, 0));
        assertEquals('R', rook.getChar());
    }
}
