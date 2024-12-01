package test.java.com;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;

import main.java.com.game.Board;
import main.java.com.game.Knight;
import main.java.com.game.Piece;
import main.java.com.game.Piece.Color;
import main.java.com.game.Piece.Type;
import main.java.com.game.Vec2;

public class KnightTest {
    @Test
    public void testKnightConstructor() {
        Vec2 pos = new Vec2(1, 0);
        Knight knight = new Knight(Color.WHITE, pos);
        assertEquals(Color.WHITE, knight.getColor());
        assertEquals(pos, knight.getPos());
        assertNotNull(knight.getImage());
    }

    @Test
    public void testGetMoves() {
        Board board = new Board("Player1", "Player2", 8);
        Knight knight = new Knight(Color.WHITE, new Vec2(4, 4));
        List<Vec2> moves = knight.getMoves(board);

        assertTrue(moves.contains(new Vec2(6, 5)));
        assertTrue(moves.contains(new Vec2(6, 3)));
        assertTrue(moves.contains(new Vec2(2, 5)));
        assertTrue(moves.contains(new Vec2(2, 3)));
        assertTrue(moves.contains(new Vec2(5, 6)));
        assertTrue(moves.contains(new Vec2(5, 2)));
        assertTrue(moves.contains(new Vec2(3, 6)));
        assertTrue(moves.contains(new Vec2(3, 2)));
    }

    @Test
    public void testGetType() {
        Knight knight = new Knight(Color.WHITE, new Vec2(1, 0));
        assertEquals(Type.KNIGHT, knight.getType());
    }

    @Test
    public void testClone() {
        Knight knight = new Knight(Color.WHITE, new Vec2(1, 0));
        Piece clonedKnight = knight.clone();
        assertEquals(knight.getColor(), clonedKnight.getColor());
        assertEquals(knight.getPos(), clonedKnight.getPos());
        assertEquals(knight.getType(), clonedKnight.getType());
    }

    @Test
    public void testGetChar() {
        Knight knight = new Knight(Color.WHITE, new Vec2(1, 0));
        assertEquals('N', knight.getChar());
    }
}
