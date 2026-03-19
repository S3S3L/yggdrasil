package io.github.s3s3l.yggdrasil.game.core.grid;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * SquareGrid 单元测试
 */
class SquareGridTest {

    private SquareGrid grid;

    @BeforeEach
    void setUp() {
        GridMetrics metrics = GridMetrics.ofRect(10, 10);
        grid = new SquareGrid(metrics);
    }

    @Test
    void testToCoordinate() {
        Coordinate coord = grid.toCoordinate(5);

        assertEquals(5, coord.getX());
        assertEquals(0, coord.getY());
    }

    @Test
    void testToIndex() {
        SquareCoordinate coord = SquareCoordinate.of(3, 2);
        int index = grid.toIndex(coord);

        assertEquals(23, index); // 3 + 2 * 10
    }

    @Test
    void testGetNeighborsManhattan() {
        SquareGrid manhattanGrid = new SquareGrid(GridMetrics.ofRect(10, 10), SquareGrid.NeighborMode.MANHATTAN);
        SquareCoordinate center = SquareCoordinate.of(5, 5);
        List<Coordinate> neighbors = manhattanGrid.getNeighbors(center);

        assertEquals(4, neighbors.size());

        // 验证所有邻居都是有效的
        for (Coordinate neighbor : neighbors) {
            assertTrue(manhattanGrid.isValid(neighbor));
        }
    }

    @Test
    void testGetNeighborsChebyshev() {
        SquareCoordinate center = SquareCoordinate.of(5, 5);
        List<Coordinate> neighbors = grid.getNeighbors(center);

        assertEquals(8, neighbors.size());

        // 验证所有邻居都是有效的
        for (Coordinate neighbor : neighbors) {
            assertTrue(grid.isValid(neighbor));
        }
    }

    @Test
    void testGetNeighborsEdge() {
        SquareCoordinate corner = SquareCoordinate.of(0, 0);
        List<Coordinate> neighbors = grid.getNeighbors(corner);

        // 角落位置只有部分邻居有效
        assertTrue(neighbors.size() < 8);

        // 验证所有返回的邻居都是有效的
        for (Coordinate neighbor : neighbors) {
            assertTrue(grid.isValid(neighbor));
        }
    }

    @Test
    void testGetDistanceManhattan() {
        SquareGrid manhattanGrid = new SquareGrid(GridMetrics.ofRect(10, 10), SquareGrid.NeighborMode.MANHATTAN);
        SquareCoordinate a = SquareCoordinate.of(0, 0);
        SquareCoordinate b = SquareCoordinate.of(3, 4);

        double distance = manhattanGrid.getDistance(a, b);

        assertEquals(7.0, distance); // |3-0| + |4-0| = 7
    }

    @Test
    void testGetDistanceChebyshev() {
        SquareCoordinate a = SquareCoordinate.of(0, 0);
        SquareCoordinate b = SquareCoordinate.of(3, 4);

        double distance = grid.getDistance(a, b);

        assertEquals(4.0, distance); // max(|3-0|, |4-0|) = 4
    }

    @Test
    void testIsValid() {
        assertTrue(grid.isValid(SquareCoordinate.of(0, 0)));
        assertTrue(grid.isValid(SquareCoordinate.of(9, 9)));
        assertFalse(grid.isValid(SquareCoordinate.of(-1, 0)));
        assertFalse(grid.isValid(SquareCoordinate.of(10, 5)));
        assertFalse(grid.isValid(SquareCoordinate.of(5, 10)));
    }

    @Test
    void testGetPath() {
        SquareCoordinate from = SquareCoordinate.of(0, 0);
        SquareCoordinate to = SquareCoordinate.of(5, 0);

        List<Coordinate> path = grid.getPath(from, to);

        assertEquals(6, path.size()); // 包含起点和终点

        // 验证路径连续性
        for (int i = 0; i < path.size(); i++) {
            SquareCoordinate coord = (SquareCoordinate) path.get(i);
            assertEquals(i, coord.getX());
            assertEquals(0, coord.getY());
        }
    }

    @Test
    void testGetRangeManhattan() {
        SquareGrid manhattanGrid = new SquareGrid(GridMetrics.ofRect(10, 10), SquareGrid.NeighborMode.MANHATTAN);
        SquareCoordinate center = SquareCoordinate.of(5, 5);
        List<SquareCoordinate> range = manhattanGrid.getRange(center, 2);

        // 曼哈顿距离范围为 2 应该有 1 + 4 + 8 = 13 个坐标（菱形）
        // 公式：1 + 4 * (1 + 2 + ... + radius) = 1 + 4 * radius * (radius + 1) / 2
        // 半径 2: 1 + 4 * 3 = 13
        assertEquals(13, range.size());

        // 验证范围内所有坐标到中心的曼哈顿距离都 <= 2
        for (SquareCoordinate coord : range) {
            assertTrue(coord.manhattanDistance(center) <= 2);
        }
    }

    @Test
    void testGetRangeChebyshev() {
        SquareCoordinate center = SquareCoordinate.of(5, 5);
        List<SquareCoordinate> range = grid.getRange(center, 2);

        // 切比雪夫距离范围为 2 应该有 (2*2+1)^2 = 25 个坐标（正方形）
        assertEquals(25, range.size());

        // 验证范围内所有坐标到中心的切比雪夫距离都 <= 2
        for (SquareCoordinate coord : range) {
            assertTrue(coord.chebyshevDistance(center) <= 2);
        }
    }

    @Test
    void testGetNeighborMode() {
        assertEquals(SquareGrid.NeighborMode.CHEBYSHEV, grid.getNeighborMode());

        SquareGrid manhattanGrid = new SquareGrid(GridMetrics.ofRect(10, 10), SquareGrid.NeighborMode.MANHATTAN);
        assertEquals(SquareGrid.NeighborMode.MANHATTAN, manhattanGrid.getNeighborMode());
    }

    @Test
    void testGetCardinalNeighbors() {
        SquareCoordinate center = SquareCoordinate.of(5, 5);
        List<SquareCoordinate> neighbors = grid.getCardinalNeighbors(center);

        assertEquals(4, neighbors.size());
    }

    @Test
    void testGetAllNeighbors() {
        SquareCoordinate center = SquareCoordinate.of(5, 5);
        List<SquareCoordinate> neighbors = grid.getAllNeighbors(center);

        assertEquals(8, neighbors.size());
    }
}
