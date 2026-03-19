package io.github.s3s3l.yggdrasil.game.core.vision;

import io.github.s3s3l.yggdrasil.game.core.grid.Coordinate;
import io.github.s3s3l.yggdrasil.game.core.grid.Grid;

import java.util.List;
import java.util.Set;

/**
 * 视野系统接口
 * 计算单位的可见范围和视线
 */
public interface VisionSystem {

    /**
     * 获取指定位置的视野范围
     *
     * @param position   观察者位置
     * @param visionRange 视野范围
     * @return 可见的坐标列表
     */
    List<Coordinate> getFieldOfView(Coordinate position, int visionRange);

    /**
     * 检查两点之间是否有视线
     *
     * @param from 起点
     * @param to   终点
     * @return true 如果视线畅通
     */
    boolean hasLineOfSight(Coordinate from, Coordinate to);

    /**
     * 检查指定坐标是否可见
     *
     * @param observer 观察者位置
     * @param target   目标位置
     * @param visionRange 视野范围
     * @return true 如果目标可见
     */
    boolean isVisible(Coordinate observer, Coordinate target, int visionRange);

    /**
     * 更新视野阻挡物
     *
     * @param blockers 阻挡物坐标集合
     */
    void updateBlockers(Set<Coordinate> blockers);

    /**
     * 获取可见性地图
     *
     * @param observer 观察者位置
     * @param visionRange 视野范围
     * @return 可见性映射（坐标 -> 是否可见）
     */
    VisibilityMap getVisibilityMap(Coordinate observer, int visionRange);
}
