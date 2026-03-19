package io.github.s3s3l.yggdrasil.game.core.pathfinding;

import io.github.s3s3l.yggdrasil.game.core.grid.Coordinate;

import java.util.List;

/**
 * 寻路器接口
 * 定义寻路算法的基本行为
 */
public interface Pathfinder {

    /**
     * 寻找从起点到终点的路径
     *
     * @param start 起点
     * @param end   终点
     * @return 路径坐标列表，如果找不到路径则返回空列表
     */
    List<Coordinate> findPath(Coordinate start, Coordinate end);

    /**
     * 寻找从起点到终点的路径（带最大距离限制）
     *
     * @param start      起点
     * @param end        终点
     * @param maxDistance 最大距离
     * @return 路径坐标列表，如果找不到路径则返回空列表
     */
    List<Coordinate> findPath(Coordinate start, Coordinate end, int maxDistance);

    /**
     * 计算两点之间的距离
     *
     * @param start 起点
     * @param end   终点
     * @return 距离值
     */
    int calculateDistance(Coordinate start, Coordinate end);

    /**
     * 检查是否可以直接到达
     *
     * @param start 起点
     * @param end   终点
     * @return true 如果可以直接到达
     */
    boolean canReach(Coordinate start, Coordinate end);
}
