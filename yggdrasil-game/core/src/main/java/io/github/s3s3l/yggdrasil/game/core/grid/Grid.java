package io.github.s3s3l.yggdrasil.game.core.grid;

import java.util.List;

/**
 * 网格接口
 * 定义网格系统的基本行为，包括坐标转换、邻居获取、路径计算等
 */
public interface Grid {

    /**
     * 将索引转换为坐标
     *
     * @param index 线性索引
     * @return 坐标对象
     */
    Coordinate toCoordinate(int index);

    /**
     * 将坐标转换为索引
     *
     * @param coord 坐标对象
     * @return 线性索引
     */
    int toIndex(Coordinate coord);

    /**
     * 获取指定坐标的所有邻居坐标
     *
     * @param coord 中心坐标
     * @return 邻居坐标列表
     */
    List<Coordinate> getNeighbors(Coordinate coord);

    /**
     * 获取指定坐标的邻居坐标（指定数量）
     *
     * @param coord 中心坐标
     * @param count 邻居数量
     * @return 邻居坐标列表
     */
    List<Coordinate> getNeighbors(Coordinate coord, int count);

    /**
     * 计算两个坐标之间的距离
     *
     * @param a 起始坐标
     * @param b 目标坐标
     * @return 距离值
     */
    double getDistance(Coordinate a, Coordinate b);

    /**
     * 计算两个坐标之间的路径
     *
     * @param from 起始坐标
     * @param to   目标坐标
     * @return 路径坐标列表
     */
    List<Coordinate> getPath(Coordinate from, Coordinate to);

    /**
     * 验证坐标是否在网格范围内
     *
     * @param coord 待验证的坐标
     * @return true 如果坐标有效
     */
    boolean isValid(Coordinate coord);

    /**
     * 获取网格的度量信息
     *
     * @return 网格度量
     */
    GridMetrics getMetrics();
}
