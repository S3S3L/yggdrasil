package io.github.s3s3l.yggdrasil.game.core.grid;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

/**
 * SquareCoordinate 单元测试
 */
class SquareCoordinateTest {

    @Test
    void testOf() {
        SquareCoordinate coord = SquareCoordinate.of(3, 4);

        assertEquals(3, coord.getX());
        assertEquals(4, coord.getY());
        assertEquals(0, coord.getZ());
    }

    @Test
    void testAdd() {
        SquareCoordinate a = SquareCoordinate.of(3, 4);
        SquareCoordinate b = SquareCoordinate.of(1, -2);

        SquareCoordinate result = a.add(b);

        assertEquals(4, result.getX());
        assertEquals(2, result.getY());
    }

    @Test
    void testSubtract() {
        SquareCoordinate a = SquareCoordinate.of(3, 4);
        SquareCoordinate b = SquareCoordinate.of(1, -2);

        SquareCoordinate result = a.subtract(b);

        assertEquals(2, result.getX());
        assertEquals(6, result.getY());
    }

    @Test
    void testMultiply() {
        SquareCoordinate coord = SquareCoordinate.of(3, 4);

        SquareCoordinate result = coord.multiply(2);

        assertEquals(6, result.getX());
        assertEquals(8, result.getY());
    }

    @Test
    void testManhattanDistance() {
        SquareCoordinate a = SquareCoordinate.of(0, 0);
        SquareCoordinate b = SquareCoordinate.of(3, 4);

        int distance = a.manhattanDistance(b);

        assertEquals(7, distance); // |3-0| + |4-0| = 7
    }

    @Test
    void testChebyshevDistance() {
        SquareCoordinate a = SquareCoordinate.of(0, 0);
        SquareCoordinate b = SquareCoordinate.of(3, 4);

        int distance = a.chebyshevDistance(b);

        assertEquals(4, distance); // max(|3-0|, |4-0|) = 4
    }

    @Test
    void testEquals() {
        SquareCoordinate a1 = SquareCoordinate.of(3, 4);
        SquareCoordinate a2 = SquareCoordinate.of(3, 4);
        SquareCoordinate b = SquareCoordinate.of(4, 3);

        assertEquals(a1, a2);
        assertNotEquals(a1, b);
    }

    @Test
    void testHashCode() {
        SquareCoordinate a1 = SquareCoordinate.of(3, 4);
        SquareCoordinate a2 = SquareCoordinate.of(3, 4);

        assertEquals(a1.hashCode(), a2.hashCode());
    }

    @Test
    void testGetCardinalDirections() {
        SquareCoordinate[] directions = SquareCoordinate.getCardinalDirections();

        assertEquals(4, directions.length);

        // 验证所有方向的和为 0
        SquareCoordinate sum = SquareCoordinate.of(0, 0);
        for (SquareCoordinate dir : directions) {
            sum = sum.add(dir);
        }

        assertEquals(0, sum.getX());
        assertEquals(0, sum.getY());
    }

    @Test
    void testGetAllDirections() {
        SquareCoordinate[] directions = SquareCoordinate.getAllDirections();

        assertEquals(8, directions.length);
    }

    @Test
    void testToString() {
        SquareCoordinate coord = SquareCoordinate.of(3, 4);

        String result = coord.toString();

        assertTrue(result.contains("3"));
        assertTrue(result.contains("4"));
    }
}
