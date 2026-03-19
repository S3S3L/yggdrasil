package io.github.s3s3l.yggdrasil.game.core.map;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 地形接口
 * 定义地图格点的地形属性
 */
public interface Terrain {

    /**
     * 获取地形名称
     *
     * @return 地形名称
     */
    String getName();

    /**
     * 获取地形描述
     *
     * @return 地形描述
     */
    String getDescription();

    /**
     * 获取移动消耗系数
     * 1.0 表示正常移动，>1.0 表示更难移动，0 表示不可通过
     *
     * @return 移动消耗系数
     */
    float getMovementCost();

    /**
     * 获取防御加成
     *
     * @return 防御加成值
     */
    int getDefenseBonus();

    /**
     * 获取掩体值（0-100）
     *
     * @return 掩体值
     */
    int getCoverValue();

    /**
     * 是否阻挡视野
     *
     * @return true 如果阻挡视野
     */
    boolean blocksVision();

    /**
     * 是否可通过
     *
     * @return true 如果可通过
     */
    boolean isPassable();
}
