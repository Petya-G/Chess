package test.java.com;

import static org.junit.Assert.*;
import org.junit.Test;

import main.java.com.game.Vec2;

public class Vec2Test {

    @Test
    public void testEquals() {
        Vec2 vec1 = new Vec2(2, 3);
        Vec2 vec2 = new Vec2(2, 3);

        assertTrue("vec1 = vec2", vec1.equals(vec2));
    }

    @Test
    public void testNotEquals() {
        Vec2 vec1 = new Vec2(2, 3);
        Vec2 vec2 = new Vec2(3, 4);

        assertFalse("vec1 != vec2", vec1.equals(vec2));
    }

    @Test
    public void testHashCode() {
        Vec2 vec1 = new Vec2(5, 6);
        Vec2 vec2 = new Vec2(5, 6);

        assertEquals("vec1.hash = vec2.hash", vec1.hashCode(), vec2.hashCode());
    }
}
