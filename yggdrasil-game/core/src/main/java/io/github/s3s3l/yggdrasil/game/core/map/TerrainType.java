package io.github.s3s3l.yggdrasil.game.core.map;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 地形类型枚举
 * 定义常见的地形类型
 */
@Getter
@RequiredArgsConstructor
public enum TerrainType implements Terrain {
    /**
     * 草地
     */
    GRASSLAND("草地", "普通的草地地形", 1.0f, 0, 0, false, true),

    /**
     * 森林
     */
    FOREST("森林", "茂密的森林，提供掩体", 2.0f, 25, 75, true, true),

    /**
     * 山脉
     */
    MOUNTAIN("山脉", "陡峭的山地，难以通行", 3.0f, 50, 100, true, false),

    /**
     * 水域
     */
    WATER("水域", "河流或湖泊，单位无法通过", 0.0f, 0, 0, false, false),

    /**
     * 沙漠
     */
    DESERT("沙漠", "干燥的沙漠地形", 1.5f, 0, 10, false, true),

    /**
     * 沼泽
     */
    SWAMP("沼泽", "湿滑的沼泽，移动困难", 2.5f, 10, 25, false, true),

    /**
     * 道路
     */
    ROAD("道路", "铺设的道路，移动快速", 0.5f, -10, 0, false, true),

    /**
     * 建筑
     */
    BUILDING("建筑", "人工建筑，提供良好掩体", 0.0f, 75, 100, true, false),

    /**
     * 废墟
     */
    RUINS("废墟", "废弃的建筑残骸", 1.5f, 25, 50, true, true),

    /**
     * 雪地
     */
    SNOW("雪地", "被雪覆盖的地形", 1.5f, 0, 15, false, true);

    /**
     * 地形名称
     */
    private final String name;

    /**
     * 地形描述
     */
    private final String description;

    /**
     * 移动消耗系数
     */
    private final float movementCost;

    /**
     * 防御加成
     */
    private final int defenseBonus;

    /**
     * 掩体值
     */
    private final int coverValue;

    /**
     * 是否阻挡视野
     */
    private final boolean blocksVision;

    /**
     * 是否可通过
     */
    private final boolean passable;

    @Override
    public boolean blocksVision() {
        return blocksVision;
    }

    @Override
    public boolean isPassable() {
        return passable;
    }
}
