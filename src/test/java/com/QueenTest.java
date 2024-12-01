package test.java.com;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import main.java.com.game.*;
import main.java.com.game.Piece.Color;

public class QueenTest {

    @Test
    public void testQueenConstructorWhite() {
        Vec2 position = new Vec2(0, 0);
        Queen queen = new Queen(Color.WHITE, position);
        assertEquals(Color.WHITE, queen.getColor());
        assertEquals(position, queen.getPos());
        assertNotNull(queen.getImage());
        assertEquals("src/main/java/com/images/Chess_qlt45.png", queen.getImage().toString());
    }

    @Test
    public void testQueenConstructorBlack() {
        Vec2 position = new Vec2(0, 0);
        Queen queen = new Queen(Color.BLACK, position);
        assertEquals(Color.BLACK, queen.getColor());
        assertEquals(position, queen.getPos());
        assertNotNull(queen.getImage());
        assertEquals("src/main/java/com/images/Chess_qdt45.png", queen.getImage().toString());
    }
}
