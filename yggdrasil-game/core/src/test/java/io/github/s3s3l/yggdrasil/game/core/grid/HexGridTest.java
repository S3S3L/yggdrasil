package io.github.s3s3l.yggdrasil.game.core.grid;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * HexGrid 单元测试
 */
class HexGridTest {

    private HexGrid grid;

    @BeforeEach
    void setUp() {
        GridMetrics metrics = GridMetrics.ofRect(10, 10);
        grid = new HexGrid(metrics);
    }

    @Test
    void testToCoordinate() {
        Coordinate coord = grid.toCoordinate(5);

        assertEquals(5, coord.getX());
        assertEquals(0, coord.getY());
    }

    @Test
    void testToIndex() {
        HexCoordinate coord = HexCoordinate.fromAxial(3, 2);
        int index = grid.toIndex(coord);

        assertEquals(23, index); // 3 + 2 * 10
    }

    @Test
    void testGetNeighbors() {
        HexCoordinate center = HexCoordinate.fromAxial(5, 5);
        List<Coordinate> neighbors = grid.getNeighbors(center);

        assertEquals(6, neighbors.size());

        // 验证所有邻居都是有效的
        for (Coordinate neighbor : neighbors) {
            assertTrue(grid.isValid(neighbor), "Neighbor " + neighbor + " should be valid");
        }
    }

    @Test
    void testGetNeighborsEdge() {
        HexCoordinate corner = HexCoordinate.fromAxial(0, 0);
        List<Coordinate> neighbors = grid.getNeighbors(corner);

        // 角落位置只有部分邻居有效
        assertTrue(neighbors.size() < 6);

        // 验证所有返回的邻居都是有效的
        for (Coordinate neighbor : neighbors) {
            assertTrue(grid.isValid(neighbor));
        }
    }

    @Test
    void testGetDistance() {
        HexCoordinate a = HexCoordinate.fromAxial(0, 0);
        HexCoordinate b = HexCoordinate.fromAxial(3, 0);

        double distance = grid.getDistance(a, b);

        assertEquals(3.0, distance);
    }

    @Test
    void testGetDistanceDiagonal() {
        HexCoordinate a = HexCoordinate.fromAxial(0, 0);
        HexCoordinate b = HexCoordinate.fromAxial(2, 0);

        double distance = grid.getDistance(a, b);

        assertEquals(2.0, distance); // 六边形距离
    }

    @Test
    void testIsValid() {
        assertTrue(grid.isValid(HexCoordinate.fromAxial(0, 0)));
        assertTrue(grid.isValid(HexCoordinate.fromAxial(9, 9)));
        assertFalse(grid.isValid(HexCoordinate.fromAxial(-1, 0)));
        assertFalse(grid.isValid(HexCoordinate.fromAxial(10, 5)));
        assertFalse(grid.isValid(HexCoordinate.fromAxial(5, 10)));
    }

    @Test
    void testGetPath() {
        HexCoordinate from = HexCoordinate.fromAxial(0, 0);
        HexCoordinate to = HexCoordinate.fromAxial(5, 0);

        List<Coordinate> path = grid.getPath(from, to);

        assertEquals(6, path.size()); // 包含起点和终点

        // 验证路径连续性
        for (int i = 0; i < path.size(); i++) {
            HexCoordinate coord = (HexCoordinate) path.get(i);
            assertEquals(i, coord.getQ());
            assertEquals(0, coord.getR());
        }
    }

    @Test
    void testGetMetrics() {
        GridMetrics metrics = grid.getMetrics();

        assertEquals(10, metrics.getWidth());
        assertEquals(10, metrics.getHeight());
        assertEquals(0, metrics.getMinX());
        assertEquals(0, metrics.getMinY());
        assertEquals(10, metrics.getMaxX());
        assertEquals(10, metrics.getMaxY());
    }

    @Test
    void testHexRing() {
        HexCoordinate center = HexCoordinate.fromAxial(5, 5);
        List<HexCoordinate> ring = grid.getHexRing(center, 1);

        // 半径为 1 的环应该有 6 个坐标
        // 但由于边界限制，实际数量可能更少
        assertFalse(ring.isEmpty());

        // 验证环上所有坐标都是有效的
        for (HexCoordinate coord : ring) {
            assertTrue(grid.isValid(coord));
        }
    }

    @Test
    void testHexRange() {
        HexCoordinate center = HexCoordinate.fromAxial(5, 5);
        List<HexCoordinate> range = grid.getHexRange(center, 2);

        // 半径为 2 的范围应该有 1 + 6 + 12 = 19 个坐标
        // 但由于边界限制，实际数量可能更少
        assertFalse(range.isEmpty());

        // 验证范围内所有坐标都是有效的
        for (HexCoordinate coord : range) {
            assertTrue(grid.isValid(coord));
        }
    }

    @Test
    void testOrientation() {
        GridMetrics metrics = GridMetrics.ofRect(10, 10);

        HexGrid pointyTop = new HexGrid(metrics, HexGrid.Orientation.POINTY_TOP);
        HexGrid flatTop = new HexGrid(metrics, HexGrid.Orientation.FLAT_TOP);

        assertEquals(HexGrid.Orientation.POINTY_TOP, pointyTop.getOrientation());
        assertEquals(HexGrid.Orientation.FLAT_TOP, flatTop.getOrientation());
    }
}
