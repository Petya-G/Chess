
package test.java.com;

import main.java.com.game.Bishop;
import main.java.com.game.Piece.Color;
import main.java.com.game.Vec2;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import javax.swing.ImageIcon;

import org.junit.Test;

public class BishopTest {
    @Test
    public void testBishopConstructorWhite() {
        Vec2 position = new Vec2(2, 0);
        Bishop bishop = new Bishop(Color.WHITE, position);

        assertEquals(Color.WHITE, bishop.getColor());
        assertEquals(position, bishop.getPos());
        assertNotNull(bishop.getImage());
        assertEquals("src/main/java/com/images/Chess_blt45.png", ((ImageIcon) bishop.getImage()).getDescription());
    }

    @Test
    public void testBishopConstructorBlack() {
        Vec2 position = new Vec2(5, 7);
        Bishop bishop = new Bishop(Color.BLACK, position);

        assertEquals(Color.BLACK, bishop.getColor());
        assertEquals(position, bishop.getPos());
        assertNotNull(bishop.getImage());
        assertEquals("src/main/java/com/images/Chess_bdt45.png", ((ImageIcon) bishop.getImage()).getDescription());
    }
}