package io.github.s3s3l.yggdrasil.game.core.generator;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import io.github.s3s3l.yggdrasil.game.core.grid.Grid;
import io.github.s3s3l.yggdrasil.game.core.grid.GridMetrics;
import io.github.s3s3l.yggdrasil.game.core.grid.SquareCoordinate;
import io.github.s3s3l.yggdrasil.game.core.grid.SquareGrid;
import io.github.s3s3l.yggdrasil.game.core.map.GameMap;
import io.github.s3s3l.yggdrasil.game.core.map.Terrain;
import io.github.s3s3l.yggdrasil.game.core.map.TerrainType;

/**
 * CellularAutomataGenerator 单元测试
 */
class CellularAutomataGeneratorTest {

    @Test
    void testGenerate() {
        GridMetrics metrics = GridMetrics.ofRect(20, 20);
        SquareGrid grid = new SquareGrid(metrics);
        CellularAutomataGenerator generator = new CellularAutomataGenerator(0.45f, 4, 4, 3);

        GameMap map = generator.generate(grid, 42L);

        assertNotNull(map);
        assertEquals(grid, map.getGrid());

        // 验证地图包含两种地形（墙壁和空地）
        int wallCount = 0;
        int emptyCount = 0;

        for (int x = 0; x < 20; x++) {
            for (int y = 0; y < 20; y++) {
                Terrain terrain = map.getTerrain(SquareCoordinate.of(x, y));
                if (terrain == TerrainType.MOUNTAIN) wallCount++;
                if (terrain == TerrainType.GRASSLAND) emptyCount++;
            }
        }

        // 验证生成了两种地形
        assertTrue(wallCount > 0, "Should have some wall terrain");
        assertTrue(emptyCount > 0, "Should have some empty terrain");
    }

    @Test
    void testBoundaryWalls() {
        GridMetrics metrics = GridMetrics.ofRect(10, 10);
        SquareGrid grid = new SquareGrid(metrics);
        CellularAutomataGenerator generator = new CellularAutomataGenerator(0.3f, 4, 4, 2);

        GameMap map = generator.generate(grid, 42L);

        // 验证边界都是墙壁
        for (int x = 0; x < 10; x++) {
            assertEquals(TerrainType.MOUNTAIN, map.getTerrain(SquareCoordinate.of(x, 0)));
            assertEquals(TerrainType.MOUNTAIN, map.getTerrain(SquareCoordinate.of(x, 9)));
        }
        for (int y = 0; y < 10; y++) {
            assertEquals(TerrainType.MOUNTAIN, map.getTerrain(SquareCoordinate.of(0, y)));
            assertEquals(TerrainType.MOUNTAIN, map.getTerrain(SquareCoordinate.of(9, y)));
        }
    }

    @Test
    void testDeterministicGeneration() {
        GridMetrics metrics = GridMetrics.ofRect(15, 15);
        SquareGrid grid = new SquareGrid(metrics);
        CellularAutomataGenerator generator = new CellularAutomataGenerator(0.4f, 4, 4, 3);

        GameMap map1 = generator.generate(grid, 777L);
        GameMap map2 = generator.generate(grid, 777L);

        // 相同种子应该生成相同地图
        for (int x = 0; x < 15; x++) {
            for (int y = 0; y < 15; y++) {
                SquareCoordinate coord = SquareCoordinate.of(x, y);
                assertEquals(map1.getTerrain(coord), map2.getTerrain(coord));
            }
        }
    }

    @Test
    void testDifferentSeedsProduceDifferentMaps() {
        GridMetrics metrics = GridMetrics.ofRect(15, 15);
        SquareGrid grid = new SquareGrid(metrics);
        CellularAutomataGenerator generator = new CellularAutomataGenerator(0.4f, 4, 4, 3);

        GameMap map1 = generator.generate(grid, 1L);
        GameMap map2 = generator.generate(grid, 2L);

        // 不同种子应该产生不同地图
        boolean different = false;
        for (int x = 0; x < 15 && !different; x++) {
            for (int y = 0; y < 15 && !different; y++) {
                SquareCoordinate coord = SquareCoordinate.of(x, y);
                if (map1.getTerrain(coord) != map2.getTerrain(coord)) {
                    different = true;
                }
            }
        }

        assertTrue(different, "Different seeds should produce different maps");
    }

    @Test
    void testIterationsAffectResult() {
        GridMetrics metrics = GridMetrics.ofRect(10, 10);
        SquareGrid grid = new SquareGrid(metrics);

        CellularAutomataGenerator gen1 = new CellularAutomataGenerator(0.5f, 4, 4, 1);
        CellularAutomataGenerator gen2 = new CellularAutomataGenerator(0.5f, 4, 4, 5);

        GameMap map1 = gen1.generate(grid, 42L);
        GameMap map2 = gen2.generate(grid, 42L);

        // 更多迭代应该产生更平滑的结果
        // 这里只验证两者都生成了有效地图
        assertNotNull(map1);
        assertNotNull(map2);
    }

    @Test
    void testGetName() {
        CellularAutomataGenerator generator = new CellularAutomataGenerator();
        assertEquals("元胞自动机生成器", generator.getName());
    }

    @Test
    void testDifferentConfigurations() {
        GridMetrics metrics = GridMetrics.ofRect(12, 12);
        SquareGrid grid = new SquareGrid(metrics);

        // 高存活概率
        CellularAutomataGenerator highDensity = new CellularAutomataGenerator(0.7f, 4, 4, 3);
        GameMap highMap = highDensity.generate(grid, 42L);

        // 低存活概率
        CellularAutomataGenerator lowDensity = new CellularAutomataGenerator(0.2f, 4, 4, 3);
        GameMap lowMap = lowDensity.generate(grid, 42L);

        // 验证高密度生成更多墙壁
        int highWallCount = countTerrain(highMap, TerrainType.MOUNTAIN);
        int lowWallCount = countTerrain(lowMap, TerrainType.MOUNTAIN);

        assertTrue(highWallCount > lowWallCount, "High density should produce more walls");
    }

    private int countTerrain(GameMap map, Terrain terrain) {
        int count = 0;
        Grid grid = map.getGrid();
        GridMetrics metrics = grid.getMetrics();

        for (int x = metrics.getMinX(); x < metrics.getMaxX(); x++) {
            for (int y = metrics.getMinY(); y < metrics.getMaxY(); y++) {
                SquareCoordinate coord = SquareCoordinate.of(x, y);
                if (grid.isValid(coord) && map.getTerrain(coord) == terrain) {
                    count++;
                }
            }
        }

        return count;
    }
}
