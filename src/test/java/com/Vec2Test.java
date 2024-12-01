package test.java.com;

import static org.junit.Assert.*;
import org.junit.Test;

import main.java.com.game.Vec2;

public class Vec2Test {

    @Test
    public void testConstructorWithIntegers() {
        Vec2 vec = new Vec2(3, 5);
        assertEquals(3, vec.x);
        assertEquals(5, vec.y);
    }

    @Test
    public void testConstructorWithAlgebraic() {
        Vec2 vec = new Vec2("d4");
        assertEquals(3, vec.x);
        assertEquals(4, vec.y);
    }

    @Test
    public void testEquals() {
        Vec2 vec1 = new Vec2(3, 5);
        Vec2 vec2 = new Vec2(3, 5);
        Vec2 vec3 = new Vec2(4, 5);
        assertTrue(vec1.equals(vec2));
        assertFalse(vec1.equals(vec3));
    }

    @Test
    public void testHashCode() {
        Vec2 vec1 = new Vec2(3, 5);
        Vec2 vec2 = new Vec2(3, 5);
        assertEquals(vec1.hashCode(), vec2.hashCode());
    }

    @Test
    public void testToAlgebraic() {
        Vec2 vec = new Vec2(3, 4);
        assertEquals("d4", vec.toAlgebraic());
    }
}
