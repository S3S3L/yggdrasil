package io.github.s3s3l.yggdrasil.game.core.physics;

import io.github.s3s3l.yggdrasil.game.core.basic.Vector2;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Ray 单元测试
 */
class RayTest {

    @Test
    void testConstructor() {
        Ray ray = new Ray(new Vector2(0, 0), new Vector2(1, 0));
        assertEquals(0, ray.origin.x, 0.001f);
        assertEquals(0, ray.origin.y, 0.001f);
        assertEquals(1, ray.direction.x, 0.001f);
        assertEquals(0, ray.direction.y, 0.001f);
    }

    @Test
    void testConstructorNormalizesDirection() {
        Ray ray = new Ray(new Vector2(0, 0), new Vector2(3, 4));
        assertEquals(0.6f, ray.direction.x, 0.001f);
        assertEquals(0.8f, ray.direction.y, 0.001f);
    }

    @Test
    void testConstructorWithComponents() {
        Ray ray = new Ray(10, 20, 1, 0);
        assertEquals(10, ray.origin.x, 0.001f);
        assertEquals(20, ray.origin.y, 0.001f);
        assertEquals(1, ray.direction.x, 0.001f);
        assertEquals(0, ray.direction.y, 0.001f);
    }

    @Test
    void testGetPoint() {
        Ray ray = new Ray(new Vector2(0, 0), new Vector2(1, 0));
        Vector2 point = ray.getPoint(5);
        assertEquals(5, point.x, 0.001f);
        assertEquals(0, point.y, 0.001f);
    }

    @Test
    void testGetPointDiagonal() {
        Ray ray = new Ray(new Vector2(0, 0), new Vector2(1, 1));
        ray.direction.normalize();
        Vector2 point = ray.getPoint(10);
        assertEquals(10 * Math.cos(Math.PI / 4), point.x, 0.1f);
        assertEquals(10 * Math.sin(Math.PI / 4), point.y, 0.1f);
    }

    @Test
    void testContainsPoint() {
        Ray ray = new Ray(new Vector2(0, 0), new Vector2(1, 0));
        assertTrue(ray.containsPoint(new Vector2(5, 0), 0.01f));
        assertTrue(ray.containsPoint(new Vector2(0, 0), 0.01f));
        assertFalse(ray.containsPoint(new Vector2(0, 1), 0.01f));
        assertFalse(ray.containsPoint(new Vector2(-5, 0), 0.01f));
    }

    @Test
    void testSet() {
        Ray ray = new Ray(new Vector2(0, 0), new Vector2(1, 0));
        ray.set(new Vector2(10, 20), new Vector2(0, 1));
        assertEquals(10, ray.origin.x, 0.001f);
        assertEquals(20, ray.origin.y, 0.001f);
        assertEquals(0, ray.direction.x, 0.001f);
        assertEquals(1, ray.direction.y, 0.001f);
    }

    @Test
    void testToString() {
        Ray ray = new Ray(new Vector2(0, 0), new Vector2(1, 0));
        String str = ray.toString();
        assertTrue(str.contains("Ray"));
        assertTrue(str.contains("origin"));
        assertTrue(str.contains("direction"));
    }
}
