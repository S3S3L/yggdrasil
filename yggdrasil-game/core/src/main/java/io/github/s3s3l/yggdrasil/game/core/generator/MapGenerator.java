package io.github.s3s3l.yggdrasil.game.core.generator;

import io.github.s3s3l.yggdrasil.game.core.grid.Coordinate;
import io.github.s3s3l.yggdrasil.game.core.grid.Grid;
import io.github.s3s3l.yggdrasil.game.core.map.GameMap;
import io.github.s3s3l.yggdrasil.game.core.map.Terrain;

/**
 * 地图生成器接口
 * 定义地图生成的基本行为
 */
public interface MapGenerator {

    /**
     * 获取生成器名称
     *
     * @return 生成器名称
     */
    String getName();

    /**
     * 生成地图
     *
     * @param grid 网格系统
     * @param seed 随机种子
     * @return 生成的地图
     */
    GameMap generate(Grid grid, long seed);

    /**
     * 生成地图（使用默认种子）
     *
     * @param grid 网格系统
     * @return 生成的地图
     */
    default GameMap generate(Grid grid) {
        return generate(grid, System.nanoTime());
    }

    /**
     * 获取指定坐标的地形
     *
     * @param grid 网格系统
     * @param coordinate 坐标
     * @param seed 随机种子
     * @return 地形
     */
    Terrain getTerrainAt(Grid grid, Coordinate coordinate, long seed);
}
