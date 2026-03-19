package io.github.s3s3l.yggdrasil.game.core.generator;

import io.github.s3s3l.yggdrasil.game.core.grid.Coordinate;
import io.github.s3s3l.yggdrasil.game.core.grid.Grid;
import io.github.s3s3l.yggdrasil.game.core.grid.GridMetrics;
import io.github.s3s3l.yggdrasil.game.core.map.GameMap;
import io.github.s3s3l.yggdrasil.game.core.map.GridMap;
import io.github.s3s3l.yggdrasil.game.core.map.Terrain;
import io.github.s3s3l.yggdrasil.game.core.map.TerrainType;

import java.util.*;

/**
 * 元胞自动机地图生成器
 * 使用元胞自动机算法生成洞穴状地形
 */
public class CellularAutomataGenerator implements MapGenerator {

    /**
     * 生成器名称
     */
    private static final String NAME = "元胞自动机生成器";

    /**
     * 初始存活概率
     */
    private final float initialAliveProbability;

    /**
     * 出生阈值（邻居数量 >= 此值则出生）
     */
    private final int birthThreshold;

    /**
     * 存活阈值（邻居数量 >= 此值则存活）
     */
    private final int survivalThreshold;

    /**
     * 迭代次数
     */
    private final int iterations;

    /**
     * 创建元胞自动机生成器（默认配置）
     */
    public CellularAutomataGenerator() {
        this(0.45f, 4, 4, 3);
    }

    /**
     * 创建元胞自动机生成器
     *
     * @param initialAliveProbability 初始存活概率
     * @param birthThreshold 出生阈值
     * @param survivalThreshold 存活阈值
     * @param iterations 迭代次数
     */
    public CellularAutomataGenerator(float initialAliveProbability, int birthThreshold,
                                     int survivalThreshold, int iterations) {
        this.initialAliveProbability = initialAliveProbability;
        this.birthThreshold = birthThreshold;
        this.survivalThreshold = survivalThreshold;
        this.iterations = iterations;
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public GameMap generate(Grid grid, long seed) {
        GridMetrics metrics = grid.getMetrics();
        int width = metrics.getWidth();
        int height = metrics.getHeight();

        // 初始化网格
        boolean[][] cells = initializeCells(width, height, seed);

        // 迭代演化
        for (int i = 0; i < iterations; i++) {
            cells = evolve(cells, width, height);
        }

        // 创建地图
        GameMap map = new GridMap("元胞自动机地图", grid, TerrainType.GRASSLAND);

        // 应用地形
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                Coordinate coord = grid.toCoordinate(x + y * width);
                if (grid.isValid(coord)) {
                    Terrain terrain = cells[x][y] ? TerrainType.MOUNTAIN : TerrainType.GRASSLAND;
                    map.setTerrain(coord, terrain);
                }
            }
        }

        return map;
    }

    @Override
    public Terrain getTerrainAt(Grid grid, Coordinate coordinate, long seed) {
        // 元胞自动机需要全局计算，此方法不适用
        return TerrainType.GRASSLAND;
    }

    private boolean[][] initializeCells(int width, int height, long seed) {
        Random random = new Random(seed);
        boolean[][] cells = new boolean[width][height];

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                // 边界总是存活（墙壁）
                if (x == 0 || x >= width - 1 || y == 0 || y >= height - 1) {
                    cells[x][y] = true;
                } else {
                    cells[x][y] = random.nextFloat() < initialAliveProbability;
                }
            }
        }

        return cells;
    }

    private boolean[][] evolve(boolean[][] cells, int width, int height) {
        boolean[][] newCells = new boolean[width][height];

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (x == 0 || x >= width - 1 || y == 0 || y >= height - 1) {
                    newCells[x][y] = true; // 边界保持存活
                    continue;
                }

                int neighbors = countAliveNeighbors(cells, x, y, width, height);

                if (cells[x][y]) {
                    // 当前存活
                    newCells[x][y] = neighbors >= survivalThreshold;
                } else {
                    // 当前死亡
                    newCells[x][y] = neighbors >= birthThreshold;
                }
            }
        }

        return newCells;
    }

    private int countAliveNeighbors(boolean[][] cells, int x, int y, int width, int height) {
        int count = 0;

        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                if (dx == 0 && dy == 0) continue;

                int nx = x + dx;
                int ny = y + dy;

                if (nx >= 0 && nx < width && ny >= 0 && ny < height) {
                    if (cells[nx][ny]) {
                        count++;
                    }
                }
            }
        }

        return count;
    }

    /**
     * 填充地图中的小洞穴（可选的后处理）
     *
     * @param map 地图
     * @param minSize 最小洞穴大小
     */
    public void fillSmallCaves(GameMap map, int minSize) {
        // 后处理逻辑，用于平滑地图
        // 可以通过泛洪填充算法识别小洞穴并填充
    }
}
