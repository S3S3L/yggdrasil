package io.github.s3s3l.yggdrasil.game.core.ui;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * UI 矩形单元测试
 */
class UIRectTest {

    @Test
    void testRectCreation() {
        UIRect rect = new UIRect(10, 20, 100, 200);

        assertEquals(10, rect.getX());
        assertEquals(20, rect.getY());
        assertEquals(100, rect.getWidth());
        assertEquals(200, rect.getHeight());
    }

    @Test
    void testDefaultConstructor() {
        UIRect rect = new UIRect();

        assertEquals(0, rect.getX());
        assertEquals(0, rect.getY());
        assertEquals(0, rect.getWidth());
        assertEquals(0, rect.getHeight());
    }

    @Test
    void testCopyConstructor() {
        UIRect original = new UIRect(10, 20, 100, 200);
        UIRect copy = new UIRect(original);

        assertEquals(original.getX(), copy.getX());
        assertEquals(original.getY(), copy.getY());
        assertEquals(original.getWidth(), copy.getWidth());
        assertEquals(original.getHeight(), copy.getHeight());
    }

    @Test
    void testGetRight() {
        UIRect rect = new UIRect(10, 20, 100, 200);

        assertEquals(110, rect.getRight());
    }

    @Test
    void testSetRight() {
        UIRect rect = new UIRect(10, 20, 100, 200);
        rect.setRight(150);

        assertEquals(150, rect.getRight());
        assertEquals(140, rect.getWidth());
    }

    @Test
    void testGetBottom() {
        UIRect rect = new UIRect(10, 20, 100, 200);

        assertEquals(220, rect.getBottom());
    }

    @Test
    void testSetBottom() {
        UIRect rect = new UIRect(10, 20, 100, 200);
        rect.setBottom(250);

        assertEquals(250, rect.getBottom());
        assertEquals(230, rect.getHeight());
    }

    @Test
    void testGetCenterX() {
        UIRect rect = new UIRect(10, 20, 100, 200);

        assertEquals(60, rect.getCenterX());
    }

    @Test
    void testGetCenterY() {
        UIRect rect = new UIRect(10, 20, 100, 200);

        assertEquals(120, rect.getCenterY());
    }

    @Test
    void testContains() {
        UIRect rect = new UIRect(0, 0, 100, 100);

        assertTrue(rect.contains(50, 50));
        assertTrue(rect.contains(0, 0));
        assertTrue(rect.contains(100, 100));
        assertFalse(rect.contains(-1, 50));
        assertFalse(rect.contains(150, 50));
    }

    @Test
    void testIntersects() {
        UIRect rect1 = new UIRect(0, 0, 100, 100);
        UIRect rect2 = new UIRect(50, 50, 100, 100);
        UIRect rect3 = new UIRect(200, 200, 100, 100);

        assertTrue(rect1.intersects(rect2));
        assertFalse(rect1.intersects(rect3));
    }

    @Test
    void testUnion() {
        UIRect rect1 = new UIRect(0, 0, 100, 100);
        UIRect rect2 = new UIRect(50, 50, 100, 100);

        UIRect union = rect1.union(rect2);

        assertEquals(0, union.getX());
        assertEquals(0, union.getY());
        assertEquals(150, union.getWidth());
        assertEquals(150, union.getHeight());
    }

    @Test
    void testIntersection() {
        UIRect rect1 = new UIRect(0, 0, 100, 100);
        UIRect rect2 = new UIRect(50, 50, 100, 100);

        UIRect intersection = rect1.intersection(rect2);

        assertNotNull(intersection);
        assertEquals(50, intersection.getX());
        assertEquals(50, intersection.getY());
        assertEquals(50, intersection.getWidth());
        assertEquals(50, intersection.getHeight());
    }

    @Test
    void testIntersectionNone() {
        UIRect rect1 = new UIRect(0, 0, 100, 100);
        UIRect rect2 = new UIRect(200, 200, 100, 100);

        UIRect intersection = rect1.intersection(rect2);

        assertNull(intersection);
    }

    @Test
    void testOffset() {
        UIRect rect = new UIRect(10, 20, 100, 200);
        rect.offset(5, 10);

        assertEquals(15, rect.getX());
        assertEquals(30, rect.getY());
    }

    @Test
    void testScale() {
        UIRect rect = new UIRect(10, 20, 100, 200);
        rect.scale(2, 0.5f);

        assertEquals(10, rect.getX());
        assertEquals(20, rect.getY());
        assertEquals(200, rect.getWidth());
        assertEquals(100, rect.getHeight());
    }

    @Test
    void testEquals() {
        UIRect rect1 = new UIRect(10, 20, 100, 200);
        UIRect rect2 = new UIRect(10, 20, 100, 200);
        UIRect rect3 = new UIRect(0, 0, 100, 200);

        assertEquals(rect1, rect2);
        assertNotEquals(rect1, rect3);
    }

    @Test
    void testHashCode() {
        UIRect rect1 = new UIRect(10, 20, 100, 200);
        UIRect rect2 = new UIRect(10, 20, 100, 200);

        assertEquals(rect1.hashCode(), rect2.hashCode());
    }
}
