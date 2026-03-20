package io.github.s3s3l.yggdrasil.game.core.physics.collider;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Rectangle 单元测试
 */
class RectangleTest {

    @Test
    void testConstructor() {
        Rectangle rect = new Rectangle(10, 20, 30, 40);
        assertEquals(10, rect.minX, 0.001f);
        assertEquals(20, rect.minY, 0.001f);
        assertEquals(30, rect.maxX, 0.001f);
        assertEquals(40, rect.maxY, 0.001f);
    }

    @Test
    void testFromCenter() {
        Rectangle rect = Rectangle.fromCenter(50, 50, 20, 10);
        assertEquals(40, rect.minX, 0.001f);
        assertEquals(45, rect.minY, 0.001f);
        assertEquals(60, rect.maxX, 0.001f);
        assertEquals(55, rect.maxY, 0.001f);
    }

    @Test
    void testGetWidth() {
        Rectangle rect = new Rectangle(0, 0, 100, 50);
        assertEquals(100, rect.getWidth(), 0.001f);
    }

    @Test
    void testGetHeight() {
        Rectangle rect = new Rectangle(0, 0, 100, 50);
        assertEquals(50, rect.getHeight(), 0.001f);
    }

    @Test
    void testGetCenter() {
        Rectangle rect = new Rectangle(0, 0, 100, 50);
        assertEquals(50, rect.getCenterX(), 0.001f);
        assertEquals(25, rect.getCenterY(), 0.001f);
    }

    @Test
    void testContains() {
        Rectangle rect = new Rectangle(0, 0, 100, 50);
        assertTrue(rect.contains(50, 25));
        assertTrue(rect.contains(0, 0));
        assertTrue(rect.contains(100, 50));
        assertFalse(rect.contains(-1, 25));
        assertFalse(rect.contains(50, 51));
    }

    @Test
    void testIntersects() {
        Rectangle a = new Rectangle(0, 0, 50, 50);
        Rectangle b = new Rectangle(25, 25, 75, 75);
        Rectangle c = new Rectangle(100, 100, 150, 150);

        assertTrue(a.intersects(b));
        assertFalse(a.intersects(c));
    }

    @Test
    void testTranslate() {
        Rectangle rect = new Rectangle(0, 0, 50, 50);
        rect.translate(10, 20);
        assertEquals(10, rect.minX, 0.001f);
        assertEquals(20, rect.minY, 0.001f);
        assertEquals(60, rect.maxX, 0.001f);
        assertEquals(70, rect.maxY, 0.001f);
    }

    @Test
    void testExpand() {
        Rectangle a = new Rectangle(0, 0, 50, 50);
        Rectangle b = new Rectangle(60, 60, 100, 100);
        a.expand(b);
        assertEquals(0, a.minX, 0.001f);
        assertEquals(0, a.minY, 0.001f);
        assertEquals(100, a.maxX, 0.001f);
        assertEquals(100, a.maxY, 0.001f);
    }

    @Test
    void testEquals() {
        Rectangle a = new Rectangle(0, 0, 50, 50);
        Rectangle b = new Rectangle(0, 0, 50, 50);
        Rectangle c = new Rectangle(10, 10, 60, 60);
        assertEquals(a, b);
        assertNotEquals(a, c);
    }
}
