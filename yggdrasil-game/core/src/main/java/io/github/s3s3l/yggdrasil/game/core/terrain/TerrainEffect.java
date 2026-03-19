package io.github.s3s3l.yggdrasil.game.core.terrain;

import io.github.s3s3l.yggdrasil.game.core.grid.Coordinate;
import io.github.s3s3l.yggdrasil.game.core.map.Terrain;

/**
 * 地形效果接口
 * 定义地形对游戏单位的各种影响效果
 */
public interface TerrainEffect {

    /**
     * 获取效果名称
     *
     * @return 效果名称
     */
    String getName();

    /**
     * 获取效果描述
     *
     * @return 效果描述
     */
    String getDescription();

    /**
     * 获取效果优先级（用于多个效果叠加时的计算顺序）
     *
     * @return 优先级值，值越大越先计算
     */
    int getPriority();

    /**
     * 应用效果到地形
     *
     * @param terrain 目标地形
     * @param coordinate 坐标位置
     * @return 应用效果后的地形属性
     */
    Terrain apply(Terrain terrain, Coordinate coordinate);

    /**
     * 是否可以与另一个效果叠加
     *
     * @param other 另一个效果
     * @return true 如果可以叠加
     */
    boolean canStackWith(TerrainEffect other);

    /**
     * 合并两个效果
     *
     * @param other 另一个效果
     * @return 合并后的效果
     */
    TerrainEffect merge(TerrainEffect other);
}
