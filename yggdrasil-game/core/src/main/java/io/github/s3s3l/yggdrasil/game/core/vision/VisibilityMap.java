package io.github.s3s3l.yggdrasil.game.core.vision;

import io.github.s3s3l.yggdrasil.game.core.grid.Coordinate;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 可见性地图
 * 记录每个坐标的可见性状态
 */
public class VisibilityMap {

    /**
     * 可见性映射
     */
    private final Map<Coordinate, Boolean> visibility;

    /**
     * 已探索区域（战争迷雾）
     */
    private final Map<Coordinate, Boolean> explored;

    /**
     * 创建可见性地图
     */
    public VisibilityMap() {
        this.visibility = new HashMap<>();
        this.explored = new HashMap<>();
    }

    /**
     * 设置坐标可见
     *
     * @param coord 坐标
     * @param visible 是否可见
     */
    public void setVisible(Coordinate coord, boolean visible) {
        visibility.put(coord, visible);
        if (visible) {
            explored.put(coord, true);
        }
    }

    /**
     * 检查坐标是否可见
     *
     * @param coord 坐标
     * @return true 如果可见
     */
    public boolean isVisible(Coordinate coord) {
        return visibility.getOrDefault(coord, false);
    }

    /**
     * 检查坐标是否已探索
     *
     * @param coord 坐标
     * @return true 如果已探索
     */
    public boolean isExplored(Coordinate coord) {
        return explored.getOrDefault(coord, false);
    }

    /**
     * 获取所有可见坐标
     *
     * @return 可见坐标集合
     */
    public Set<Coordinate> getVisibleCoordinates() {
        return visibility.keySet();
    }

    /**
     * 获取所有已探索坐标
     *
     * @return 已探索坐标集合
     */
    public Set<Coordinate> getExploredCoordinates() {
        return explored.keySet();
    }

    /**
     * 清除可见性（保留已探索）
     */
    public void clearVisibility() {
        visibility.clear();
    }

    /**
     * 获取可见坐标数量
     *
     * @return 可见坐标数量
     */
    public int getVisibleCount() {
        return visibility.size();
    }

    /**
     * 获取已探索坐标数量
     *
     * @return 已探索坐标数量
     */
    public int getExploredCount() {
        return explored.size();
    }
}
