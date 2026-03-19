package io.github.s3s3l.yggdrasil.game.core.map;

import io.github.s3s3l.yggdrasil.game.core.grid.Coordinate;
import io.github.s3s3l.yggdrasil.game.core.grid.Grid;
import io.github.s3s3l.yggdrasil.game.core.grid.GridMetrics;

import java.util.HashMap;
import java.util.Map;

/**
 * 网格地图实现
 * 基于网格系统的地图片实现
 */
public class GridMap implements GameMap {

    /**
     * 地图名称
     */
    private final String name;

    /**
     * 地图描述
     */
    private final String description;

    /**
     * 地图边界
     */
    private final MapBounds bounds;

    /**
     * 网格系统
     */
    private final Grid grid;

    /**
     * 地形存储（坐标 -> 地形）
     */
    private final Map<Coordinate, Terrain> terrains;

    /**
     * 默认地形
     */
    private final Terrain defaultTerrain;

    /**
     * 创建网格地图
     *
     * @param name    地图名称
     * @param grid    网格系统
     * @param defaultTerrain 默认地形
     */
    public GridMap(String name, Grid grid, Terrain defaultTerrain) {
        this(name, "", grid, MapBounds.ofSize(grid.getMetrics().getWidth(), grid.getMetrics().getHeight()), defaultTerrain);
    }

    /**
     * 创建网格地图
     *
     * @param name    地图名称
     * @param description 地图描述
     * @param grid    网格系统
     * @param bounds  地图边界
     * @param defaultTerrain 默认地形
     */
    public GridMap(String name, String description, Grid grid, MapBounds bounds, Terrain defaultTerrain) {
        this.name = name;
        this.description = description;
        this.grid = grid;
        this.bounds = bounds;
        this.terrains = new HashMap<>();
        this.defaultTerrain = defaultTerrain;
    }

    @Override
    public Grid getGrid() {
        return grid;
    }

    @Override
    public Terrain getTerrain(Coordinate coord) {
        if (!isValid(coord)) {
            return null;
        }

        Terrain terrain = terrains.get(coord);
        return terrain != null ? terrain : defaultTerrain;
    }

    @Override
    public void setTerrain(Coordinate coord, Terrain terrain) {
        if (!isValid(coord)) {
            throw new IllegalArgumentException("Coordinate " + coord + " is outside map bounds");
        }

        if (terrain == null) {
            terrains.remove(coord);
        } else {
            terrains.put(coord, terrain);
        }
    }

    @Override
    public boolean isValid(Coordinate coord) {
        if (coord == null) {
            return false;
        }

        // 检查网格有效性
        if (!grid.isValid(coord)) {
            return false;
        }

        // 检查地图边界
        return bounds.contains(coord);
    }

    @Override
    public MapBounds getBounds() {
        return bounds;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDescription() {
        return description;
    }

    /**
     * 获取所有已设置的地形
     *
     * @return 地形映射
     */
    public Map<Coordinate, Terrain> getTerrains() {
        return new HashMap<>(terrains);
    }

    /**
     * 清除所有自定义地形
     */
    public void clearTerrains() {
        terrains.clear();
    }

    /**
     * 获取地形数量
     *
     * @return 地形数量
     */
    public int getTerrainCount() {
        return terrains.size();
    }
}
