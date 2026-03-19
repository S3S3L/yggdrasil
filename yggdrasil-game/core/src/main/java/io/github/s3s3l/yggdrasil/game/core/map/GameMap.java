package io.github.s3s3l.yggdrasil.game.core.map;

import io.github.s3s3l.yggdrasil.game.core.grid.Coordinate;
import io.github.s3s3l.yggdrasil.game.core.grid.Grid;

/**
 * 游戏地图接口
 * 定义地图的基本行为
 */
public interface GameMap {

    /**
     * 获取地图使用的网格系统
     *
     * @return 网格对象
     */
    Grid getGrid();

    /**
     * 获取指定坐标的地形
     *
     * @param coord 坐标
     * @return 地形对象，如果坐标无效则返回 null
     */
    Terrain getTerrain(Coordinate coord);

    /**
     * 设置指定坐标的地形
     *
     * @param coord   坐标
     * @param terrain 地形对象
     */
    void setTerrain(Coordinate coord, Terrain terrain);

    /**
     * 验证坐标是否在地图范围内
     *
     * @param coord 坐标
     * @return true 如果坐标有效
     */
    boolean isValid(Coordinate coord);

    /**
     * 获取地图边界
     *
     * @return 地图边界
     */
    MapBounds getBounds();

    /**
     * 获取地图名称
     *
     * @return 地图名称
     */
    String getName();

    /**
     * 获取地图描述
     *
     * @return 地图描述
     */
    String getDescription();
}
