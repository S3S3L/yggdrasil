package io.github.s3s3l.yggdrasil.game.core.generator;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import io.github.s3s3l.yggdrasil.game.core.grid.Grid;
import io.github.s3s3l.yggdrasil.game.core.grid.GridMetrics;
import io.github.s3s3l.yggdrasil.game.core.grid.HexCoordinate;
import io.github.s3s3l.yggdrasil.game.core.grid.HexGrid;
import io.github.s3s3l.yggdrasil.game.core.grid.SquareCoordinate;
import io.github.s3s3l.yggdrasil.game.core.grid.SquareGrid;
import io.github.s3s3l.yggdrasil.game.core.map.GameMap;
import io.github.s3s3l.yggdrasil.game.core.map.Terrain;
import io.github.s3s3l.yggdrasil.game.core.map.TerrainType;

/**
 * NoiseGenerator 单元测试
 */
class NoiseGeneratorTest {

    @Test
    void testGenerateSquareMap() {
        GridMetrics metrics = GridMetrics.ofRect(20, 20);
        SquareGrid grid = new SquareGrid(metrics);
        // 使用更大的缩放因子和不同的阈值来确保生成多种地形
        NoiseGenerator generator = new NoiseGenerator(0.3f);

        GameMap map = generator.generate(grid, 42L);

        assertNotNull(map);
        assertEquals(grid, map.getGrid());

        // 验证地图包含多种地形
        int waterCount = 0;
        int grassCount = 0;

        for (int x = 0; x < 20; x++) {
            for (int y = 0; y < 20; y++) {
                Terrain terrain = map.getTerrain(SquareCoordinate.of(x, y));
                if (terrain == TerrainType.WATER) waterCount++;
                if (terrain == TerrainType.GRASSLAND) grassCount++;
            }
        }

        // 验证生成了多种地形
        assertTrue(waterCount > 0, "Should have some water terrain");
        assertTrue(grassCount > 0, "Should have some grassland terrain");
    }

    @Test
    void testGenerateHexMap() {
        GridMetrics metrics = GridMetrics.ofRect(15, 15);
        HexGrid grid = new HexGrid(metrics);
        NoiseGenerator generator = new NoiseGenerator(0.15f);

        GameMap map = generator.generate(grid, 123L);

        assertNotNull(map);
        assertEquals(grid, map.getGrid());

        // 验证所有坐标都有地形
        for (int q = 0; q < 15; q++) {
            for (int r = 0; r < 15; r++) {
                HexCoordinate coord = HexCoordinate.fromAxial(q, r);
                if (grid.isValid(coord)) {
                    Terrain terrain = map.getTerrain(coord);
                    assertNotNull(terrain);
                }
            }
        }
    }

    @Test
    void testCustomThresholds() {
        GridMetrics metrics = GridMetrics.ofRect(10, 10);
        SquareGrid grid = new SquareGrid(metrics);

        // 自定义阈值，让水更多
        float[] thresholds = {0.6f, 0.8f, 0.9f, 0.95f};
        Terrain[] terrains = {
                TerrainType.WATER,
                TerrainType.GRASSLAND,
                TerrainType.FOREST,
                TerrainType.MOUNTAIN
        };

        NoiseGenerator generator = new NoiseGenerator(0.1f, thresholds, terrains);
        GameMap map = generator.generate(grid, 42L);

        assertNotNull(map);

        // 验证水地形较多
        int waterCount = 0;
        for (int x = 0; x < 10; x++) {
            for (int y = 0; y < 10; y++) {
                if (map.getTerrain(SquareCoordinate.of(x, y)) == TerrainType.WATER) {
                    waterCount++;
                }
            }
        }

        // 60% 的概率应该是水
        assertTrue(waterCount > 30, "Should have more water with custom thresholds");
    }

    @Test
    void testDeterministicGeneration() {
        GridMetrics metrics = GridMetrics.ofRect(10, 10);
        SquareGrid grid = new SquareGrid(metrics);
        NoiseGenerator generator = new NoiseGenerator(0.1f);

        GameMap map1 = generator.generate(grid, 999L);
        GameMap map2 = generator.generate(grid, 999L);

        // 相同种子应该生成相同地图
        for (int x = 0; x < 10; x++) {
            for (int y = 0; y < 10; y++) {
                SquareCoordinate coord = SquareCoordinate.of(x, y);
                assertEquals(map1.getTerrain(coord), map2.getTerrain(coord));
            }
        }
    }

    @Test
    void testDifferentSeedsProduceDifferentMaps() {
        GridMetrics metrics = GridMetrics.ofRect(10, 10);
        SquareGrid grid = new SquareGrid(metrics);
        NoiseGenerator generator = new NoiseGenerator(0.1f);

        GameMap map1 = generator.generate(grid, 1L);
        GameMap map2 = generator.generate(grid, 2L);

        // 不同种子应该产生不同地图（至少有一个坐标不同）
        boolean different = false;
        for (int x = 0; x < 10 && !different; x++) {
            for (int y = 0; y < 10 && !different; y++) {
                SquareCoordinate coord = SquareCoordinate.of(x, y);
                if (map1.getTerrain(coord) != map2.getTerrain(coord)) {
                    different = true;
                }
            }
        }

        assertTrue(different, "Different seeds should produce different maps");
    }

    @Test
    void testGetName() {
        NoiseGenerator generator = new NoiseGenerator();
        assertEquals("噪声生成器", generator.getName());
    }
}
