package io.github.s3s3l.yggdrasil.game.core.physics;

import io.github.s3s3l.yggdrasil.game.core.basic.Vector2;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Vector2 单元测试
 */
class Vector2Test {

    @Test
    void testConstructor() {
        Vector2 v = new Vector2(3, 4);
        assertEquals(3, v.x, 0.001f);
        assertEquals(4, v.y, 0.001f);
    }

    @Test
    void testCopyConstructor() {
        Vector2 original = new Vector2(3, 4);
        Vector2 copy = new Vector2(original);
        assertEquals(original.x, copy.x, 0.001f);
        assertEquals(original.y, copy.y, 0.001f);
    }

    @Test
    void testSet() {
        Vector2 v = new Vector2();
        v.set(5, 10);
        assertEquals(5, v.x, 0.001f);
        assertEquals(10, v.y, 0.001f);
    }

    @Test
    void testAdd() {
        Vector2 a = new Vector2(1, 2);
        Vector2 b = new Vector2(3, 4);
        Vector2 result = a.add(b);
        assertEquals(4, result.x, 0.001f);
        assertEquals(6, result.y, 0.001f);
        // 原向量不变
        assertEquals(1, a.x, 0.001f);
        assertEquals(2, a.y, 0.001f);
    }

    @Test
    void testSub() {
        Vector2 a = new Vector2(5, 7);
        Vector2 b = new Vector2(2, 3);
        Vector2 result = a.sub(b);
        assertEquals(3, result.x, 0.001f);
        assertEquals(4, result.y, 0.001f);
    }

    @Test
    void testScale() {
        Vector2 v = new Vector2(2, 3);
        Vector2 result = v.scale(2);
        assertEquals(4, result.x, 0.001f);
        assertEquals(6, result.y, 0.001f);
    }

    @Test
    void testDot() {
        Vector2 a = new Vector2(1, 2);
        Vector2 b = new Vector2(3, 4);
        float dot = a.dot(b);
        assertEquals(11, dot, 0.001f);
    }

    @Test
    void testCross() {
        Vector2 a = new Vector2(1, 2);
        Vector2 b = new Vector2(3, 4);
        float cross = a.cross(b);
        assertEquals(-2, cross, 0.001f);
    }

    @Test
    void testLength() {
        Vector2 v = new Vector2(3, 4);
        assertEquals(5, v.length(), 0.001f);
    }

    @Test
    void testLengthSquared() {
        Vector2 v = new Vector2(3, 4);
        assertEquals(25, v.lengthSquared(), 0.001f);
    }

    @Test
    void testNormalize() {
        Vector2 v = new Vector2(3, 4);
        Vector2 normalized = v.normalized();
        assertEquals(0.6f, normalized.x, 0.001f);
        assertEquals(0.8f, normalized.y, 0.001f);
        assertEquals(1, normalized.length(), 0.001f);
    }

    @Test
    void testNormalizeZeroVector() {
        Vector2 v = new Vector2(0, 0);
        Vector2 normalized = v.normalized();
        assertEquals(0, normalized.x, 0.001f);
        assertEquals(0, normalized.y, 0.001f);
    }

    @Test
    void testDistance() {
        Vector2 a = new Vector2(0, 0);
        Vector2 b = new Vector2(3, 4);
        assertEquals(5, a.distance(b), 0.001f);
    }

    @Test
    void testLerp() {
        Vector2 a = new Vector2(0, 0);
        Vector2 b = new Vector2(10, 10);
        Vector2 result = a.lerp(b, 0.5f);
        assertEquals(5, result.x, 0.001f);
        assertEquals(5, result.y, 0.001f);
    }

    @Test
    void testRotate() {
        Vector2 v = new Vector2(1, 0);
        Vector2 rotated = v.rotate((float) Math.PI / 2);
        assertEquals(0, rotated.x, 0.01f);
        assertEquals(1, rotated.y, 0.01f);
    }

    @Test
    void testNormal() {
        Vector2 v = new Vector2(1, 0);
        Vector2 normal = v.normal();
        assertEquals(0, normal.x, 0.001f);
        assertEquals(1, normal.y, 0.001f);
    }

    @Test
    void testEquals() {
        Vector2 a = new Vector2(3, 4);
        Vector2 b = new Vector2(3, 4);
        Vector2 c = new Vector2(4, 3);
        assertEquals(a, b);
        assertNotEquals(a, c);
    }

    @Test
    void testConstants() {
        assertEquals(0, Vector2.ZERO.x, 0.001f);
        assertEquals(0, Vector2.ZERO.y, 0.001f);
        assertEquals(1, Vector2.RIGHT.x, 0.001f);
        assertEquals(0, Vector2.RIGHT.y, 0.001f);
        assertEquals(0, Vector2.UP.x, 0.001f);
        assertEquals(1, Vector2.UP.y, 0.001f);
    }
}
