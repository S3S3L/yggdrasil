package io.github.s3s3l.yggdrasil.game.core.grid;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

/**
 * HexCoordinate 单元测试
 */
class HexCoordinateTest {

    @Test
    void testFromAxial() {
        HexCoordinate coord = HexCoordinate.fromAxial(3, 2);

        assertEquals(3, coord.getQ());
        assertEquals(2, coord.getR());
        assertEquals(3, coord.getX());
        assertEquals(2, coord.getY());
    }

    @Test
    void testFromCube() {
        HexCoordinate coord = HexCoordinate.fromCube(2, -3, 1);

        assertEquals(2, coord.getCubeX());
        assertEquals(-3, coord.getCubeY());
        assertEquals(1, coord.getCubeZ());
        assertEquals(2, coord.getQ());
        assertEquals(1, coord.getR());
    }

    @Test
    void testFromCubeInvalid() {
        // 立方体坐标必须满足 x + y + z = 0
        assertThrows(IllegalArgumentException.class, () -> {
            HexCoordinate.fromCube(2, -3, 2);
        });
    }

    @Test
    void testAdd() {
        HexCoordinate a = HexCoordinate.fromAxial(3, 2);
        HexCoordinate b = HexCoordinate.fromAxial(1, -1);

        HexCoordinate result = a.add(b);

        assertEquals(4, result.getQ());
        assertEquals(1, result.getR());
    }

    @Test
    void testSubtract() {
        HexCoordinate a = HexCoordinate.fromAxial(3, 2);
        HexCoordinate b = HexCoordinate.fromAxial(1, -1);

        HexCoordinate result = a.subtract(b);

        assertEquals(2, result.getQ());
        assertEquals(3, result.getR());
    }

    @Test
    void testMultiply() {
        HexCoordinate coord = HexCoordinate.fromAxial(3, 2);

        HexCoordinate result = coord.multiply(2);

        assertEquals(6, result.getQ());
        assertEquals(4, result.getR());
    }

    @Test
    void testEquals() {
        HexCoordinate a1 = HexCoordinate.fromAxial(3, 2);
        HexCoordinate a2 = HexCoordinate.fromAxial(3, 2);
        HexCoordinate b = HexCoordinate.fromAxial(2, 3);

        assertEquals(a1, a2);
        assertNotEquals(a1, b);
    }

    @Test
    void testHashCode() {
        HexCoordinate a1 = HexCoordinate.fromAxial(3, 2);
        HexCoordinate a2 = HexCoordinate.fromAxial(3, 2);

        assertEquals(a1.hashCode(), a2.hashCode());
    }

    @Test
    void testCubeCoordinateConsistency() {
        HexCoordinate coord = HexCoordinate.fromAxial(3, 2);

        // 验证立方体坐标转换
        assertEquals(3, coord.getCubeX());
        assertEquals(-5, coord.getCubeY()); // -q - r = -3 - 2
        assertEquals(2, coord.getCubeZ());

        // 验证约束：x + y + z = 0
        assertEquals(0, coord.getCubeX() + coord.getCubeY() + coord.getCubeZ());
    }

    @Test
    void testGetDirections() {
        HexCoordinate[] directions = HexCoordinate.getDirections();

        assertEquals(6, directions.length);

        // 验证所有方向的和为 0（六边形特性）
        HexCoordinate sum = HexCoordinate.fromAxial(0, 0);
        for (HexCoordinate dir : directions) {
            sum = sum.add(dir);
        }

        assertEquals(0, sum.getQ());
        assertEquals(0, sum.getR());
    }

    @Test
    void testToString() {
        HexCoordinate coord = HexCoordinate.fromAxial(3, 2);

        String result = coord.toString();

        assertTrue(result.contains("3"));
        assertTrue(result.contains("2"));
    }
}
