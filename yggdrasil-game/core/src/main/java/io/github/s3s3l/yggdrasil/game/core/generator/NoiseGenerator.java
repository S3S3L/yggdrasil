package io.github.s3s3l.yggdrasil.game.core.generator;

import io.github.s3s3l.yggdrasil.game.core.grid.Coordinate;
import io.github.s3s3l.yggdrasil.game.core.grid.Grid;
import io.github.s3s3l.yggdrasil.game.core.grid.GridMetrics;
import io.github.s3s3l.yggdrasil.game.core.map.GameMap;
import io.github.s3s3l.yggdrasil.game.core.map.GridMap;
import io.github.s3s3l.yggdrasil.game.core.map.Terrain;
import io.github.s3s3l.yggdrasil.game.core.map.TerrainType;

import java.util.Random;

/**
 * 噪声地图生成器
 * 使用 Perlin/Simplex 噪声生成地形
 */
public class NoiseGenerator implements MapGenerator {

    /**
     * 生成器名称
     */
    private static final String NAME = "噪声生成器";

    /**
     * 噪声缩放因子
     */
    private final float noiseScale;

    /**
     * 地形阈值配置
     */
    private final float[] thresholds;
    private final Terrain[] terrains;

    /**
     * 创建噪声生成器（默认配置）
     */
    public NoiseGenerator() {
        this(0.1f);
    }

    /**
     * 创建噪声生成器
     *
     * @param noiseScale 噪声缩放因子（越小地形越平滑）
     */
    public NoiseGenerator(float noiseScale) {
        this.noiseScale = noiseScale;

        // 默认地形配置
        this.thresholds = new float[]{0.3f, 0.5f, 0.7f, 0.85f};
        this.terrains = new Terrain[]{
                TerrainType.WATER,
                TerrainType.SWAMP,
                TerrainType.GRASSLAND,
                TerrainType.FOREST,
                TerrainType.MOUNTAIN
        };
    }

    /**
     * 创建噪声生成器（自定义配置）
     *
     * @param noiseScale 噪声缩放因子
     * @param thresholds 地形阈值（升序）
     * @param terrains 地形数组（比阈值多一个）
     */
    public NoiseGenerator(float noiseScale, float[] thresholds, Terrain[] terrains) {
        this.noiseScale = noiseScale;
        this.thresholds = thresholds;
        this.terrains = terrains;
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public GameMap generate(Grid grid, long seed) {
        GridMetrics metrics = grid.getMetrics();
        GameMap map = new GridMap("噪声地图", grid, TerrainType.GRASSLAND);

        SimplexNoise noise = new SimplexNoise(seed);

        for (int x = metrics.getMinX(); x < metrics.getMaxX(); x++) {
            for (int y = metrics.getMinY(); y < metrics.getMaxY(); y++) {
                Coordinate coord = grid.toCoordinate(x + y * metrics.getWidth());

                if (grid.isValid(coord)) {
                    Terrain terrain = getTerrainAt(grid, coord, seed, noise);
                    map.setTerrain(coord, terrain);
                }
            }
        }

        return map;
    }

    @Override
    public Terrain getTerrainAt(Grid grid, Coordinate coordinate, long seed) {
        return getTerrainAt(grid, coordinate, seed, new SimplexNoise(seed));
    }

    private Terrain getTerrainAt(Grid grid, Coordinate coordinate, long seed, SimplexNoise noise) {
        GridMetrics metrics = grid.getMetrics();

        // 生成噪声值
        float nx = coordinate.getX() * noiseScale;
        float ny = coordinate.getY() * noiseScale;
        float value = noise.noise2D(nx, ny);

        // 归一化到 0-1
        value = (value + 1) / 2;

        // 根据阈值选择地形
        for (int i = 0; i < thresholds.length; i++) {
            if (value < thresholds[i]) {
                return terrains[i];
            }
        }

        return terrains[terrains.length - 1];
    }

    /**
     * Simplex 噪声实现（简化版）
     */
    private static class SimplexNoise {
        private final Random random;
        private final int[] perm;
        private final int[] permMod12;

        public SimplexNoise(long seed) {
            this.random = new Random(seed);
            this.perm = new int[512];
            this.permMod12 = new int[512];

            // 初始化排列
            int[] p = new int[256];
            for (int i = 0; i < 256; i++) {
                p[i] = i;
            }

            // 洗牌
            for (int i = 255; i > 0; i--) {
                int j = random.nextInt(i + 1);
                int temp = p[i];
                p[i] = p[j];
                p[j] = temp;
            }

            // 复制到 perm
            for (int i = 0; i < 512; i++) {
                perm[i] = p[i & 255];
                permMod12[i] = perm[i] % 12;
            }
        }

        public float noise2D(float x, float y) {
            // 简化噪声实现
            int X = (int) Math.floor(x) & 255;
            int Y = (int) Math.floor(y) & 255;

            x -= Math.floor(x);
            y -= Math.floor(y);

            float u = fade(x);
            float v = fade(y);

            int A = perm[X] + Y;
            int B = perm[X + 1] + Y;

            return lerp(v,
                    lerp(u, grad(perm[A], x, y), grad(perm[B], x - 1, y)),
                    lerp(u, grad(perm[A + 1], x, y - 1), grad(perm[B + 1], x - 1, y - 1)));
        }

        private float fade(float t) {
            return t * t * t * (t * (t * 6 - 15) + 10);
        }

        private float lerp(float t, float a, float b) {
            return a + t * (b - a);
        }

        private float grad(int hash, float x, float y) {
            int h = hash & 15;
            float u = h < 8 ? x : y;
            float v = h < 4 ? y : h == 12 || h == 14 ? x : 0;
            return ((h & 1) == 0 ? u : -u) + ((h & 2) == 0 ? v : -v);
        }
    }
}
