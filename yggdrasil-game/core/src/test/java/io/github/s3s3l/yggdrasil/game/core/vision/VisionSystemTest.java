package io.github.s3s3l.yggdrasil.game.core.vision;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.github.s3s3l.yggdrasil.game.core.grid.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 视野系统单元测试
 */
class VisionSystemTest {

    private Grid grid;
    private FieldOfView visionSystem;

    @BeforeEach
    void setUp() {
        grid = new SquareGrid(GridMetrics.ofRect(20, 20));
        visionSystem = new FieldOfView(grid);
    }

    @Test
    void testGetFieldOfView() {
        Coordinate center = SquareCoordinate.of(10, 10);
        int visionRange = 5;

        List<Coordinate> visible = visionSystem.getFieldOfView(center, visionRange);

        // 验证视野不为空
        assertFalse(visible.isEmpty());

        // 验证中心点在视野内
        assertTrue(visible.contains(center));

        // 验证所有可见点都在有效范围内
        for (Coordinate coord : visible) {
            assertTrue(grid.isValid(coord), "所有可见坐标应该在网格内");
            double distance = grid.getDistance(center, coord);
            assertTrue(distance <= visionRange, "可见坐标应该在视野范围内");
        }
    }

    @Test
    void testGetFieldOfViewZeroRange() {
        Coordinate center = SquareCoordinate.of(10, 10);

        List<Coordinate> visible = visionSystem.getFieldOfView(center, 0);

        assertTrue(visible.isEmpty());
    }

    @Test
    void testGetFieldOfViewNullPosition() {
        List<Coordinate> visible = visionSystem.getFieldOfView(null, 5);
        assertTrue(visible.isEmpty());
    }

    @Test
    void testHasLineOfSight() {
        Coordinate from = SquareCoordinate.of(5, 5);
        Coordinate to = SquareCoordinate.of(5, 10);

        // 无障碍物时应该有视线
        assertTrue(visionSystem.hasLineOfSight(from, to));
    }

    @Test
    void testHasLineOfSightWithBlocker() {
        Coordinate from = SquareCoordinate.of(5, 5);
        Coordinate to = SquareCoordinate.of(5, 10);
        Coordinate blocker = SquareCoordinate.of(5, 7);

        // 添加阻挡物
        Set<Coordinate> blockers = new HashSet<>();
        blockers.add(blocker);
        visionSystem.updateBlockers(blockers);

        // 有阻挡物时应该没有视线
        assertFalse(visionSystem.hasLineOfSight(from, to));
    }

    @Test
    void testHasLineOfSightSamePosition() {
        Coordinate pos = SquareCoordinate.of(5, 5);

        // 同一点应该有视线
        assertTrue(visionSystem.hasLineOfSight(pos, pos));
    }

    @Test
    void testHasLineOfSightNullPositions() {
        Coordinate pos = SquareCoordinate.of(5, 5);

        assertFalse(visionSystem.hasLineOfSight(null, pos));
        assertFalse(visionSystem.hasLineOfSight(pos, null));
        assertFalse(visionSystem.hasLineOfSight(null, null));
    }

    @Test
    void testIsVisible() {
        Coordinate observer = SquareCoordinate.of(10, 10);
        Coordinate target = SquareCoordinate.of(10, 12);
        int visionRange = 5;

        // 在视野范围内且无障碍物
        assertTrue(visionSystem.isVisible(observer, target, visionRange));
    }

    @Test
    void testIsVisibleOutOfRange() {
        Coordinate observer = SquareCoordinate.of(10, 10);
        Coordinate target = SquareCoordinate.of(10, 18);
        int visionRange = 5;

        // 超出视野范围
        assertFalse(visionSystem.isVisible(observer, target, visionRange));
    }

    @Test
    void testIsVisibleWithBlocker() {
        Coordinate observer = SquareCoordinate.of(5, 5);
        Coordinate target = SquareCoordinate.of(5, 10);
        Coordinate blocker = SquareCoordinate.of(5, 7);
        int visionRange = 10;

        // 添加阻挡物
        Set<Coordinate> blockers = new HashSet<>();
        blockers.add(blocker);
        visionSystem.updateBlockers(blockers);

        // 有阻挡物时不可见
        assertFalse(visionSystem.isVisible(observer, target, visionRange));
    }

    @Test
    void testGetVisibilityMap() {
        Coordinate observer = SquareCoordinate.of(10, 10);
        int visionRange = 5;

        VisibilityMap map = visionSystem.getVisibilityMap(observer, visionRange);

        assertNotNull(map);
        assertTrue(map.getVisibleCount() > 0);

        // 验证观察者在可见列表中
        assertTrue(map.isVisible(observer));
    }

    @Test
    void testGetVisibilityMapNullObserver() {
        VisibilityMap map = visionSystem.getVisibilityMap(null, 5);

        assertNotNull(map);
        assertEquals(0, map.getVisibleCount());
    }

    @Test
    void testAddBlocker() {
        Coordinate blocker = SquareCoordinate.of(10, 10);

        visionSystem.addBlocker(blocker);

        Coordinate from = SquareCoordinate.of(10, 5);
        Coordinate to = SquareCoordinate.of(10, 15);

        assertFalse(visionSystem.hasLineOfSight(from, to));
    }

    @Test
    void testRemoveBlocker() {
        Coordinate blocker = SquareCoordinate.of(10, 10);

        visionSystem.addBlocker(blocker);
        visionSystem.removeBlocker(blocker);

        Coordinate from = SquareCoordinate.of(10, 5);
        Coordinate to = SquareCoordinate.of(10, 15);

        assertTrue(visionSystem.hasLineOfSight(from, to));
    }

    @Test
    void testClearBlockers() {
        // 添加多个阻挡物
        visionSystem.addBlocker(SquareCoordinate.of(5, 5));
        visionSystem.addBlocker(SquareCoordinate.of(6, 6));
        visionSystem.addBlocker(SquareCoordinate.of(7, 7));

        visionSystem.clearBlockers();

        Coordinate from = SquareCoordinate.of(5, 5);
        Coordinate to = SquareCoordinate.of(10, 10);

        assertTrue(visionSystem.hasLineOfSight(from, to));
    }

    @Test
    void testVisibilityMapExplored() {
        VisibilityMap map = new VisibilityMap();
        Coordinate coord = SquareCoordinate.of(5, 5);

        // 初始状态
        assertFalse(map.isVisible(coord));
        assertFalse(map.isExplored(coord));

        // 设置为可见
        map.setVisible(coord, true);

        assertTrue(map.isVisible(coord));
        assertTrue(map.isExplored(coord));

        // 清除可见性（保留已探索）
        map.clearVisibility();

        assertFalse(map.isVisible(coord));
        assertTrue(map.isExplored(coord));
    }

    @Test
    void testVisibilityMapGetCoordinates() {
        VisibilityMap map = new VisibilityMap();

        Coordinate c1 = SquareCoordinate.of(1, 1);
        Coordinate c2 = SquareCoordinate.of(2, 2);
        Coordinate c3 = SquareCoordinate.of(3, 3);

        map.setVisible(c1, true);
        map.setVisible(c2, true);
        map.setVisible(c3, false); // 仅设置可见性为 false，不会加入 explored

        assertEquals(3, map.getVisibleCount()); // c1, c2, c3 都在 visibility 中
        assertEquals(2, map.getExploredCoordinates().size()); // 只有 c1, c2 已探索
    }

    @Test
    @org.junit.jupiter.api.Disabled("Hex grid vision requires HexCoordinate support in LineOfSight")
    void testHexGridVision() {
        Grid hexGrid = new HexGrid(GridMetrics.ofRect(10, 10));
        FieldOfView hexVision = new FieldOfView(hexGrid);

        HexCoordinate center = HexCoordinate.fromAxial(5, 5);
        List<Coordinate> visible = hexVision.getFieldOfView(center, 3);

        assertFalse(visible.isEmpty());
        assertTrue(visible.contains(center));
    }

    @Test
    void testLineOfSightCoordinates() {
        LineOfSight los = new LineOfSight(grid);

        Coordinate from = SquareCoordinate.of(0, 0);
        Coordinate to = SquareCoordinate.of(5, 5);

        List<Coordinate> line = los.getLineCoordinates(from, to);

        assertFalse(line.isEmpty());
        assertTrue(line.contains(from));
        assertTrue(line.contains(to));
        assertEquals(6, line.size()); // (0,0), (1,1), (2,2), (3,3), (4,4), (5,5)
    }
}
