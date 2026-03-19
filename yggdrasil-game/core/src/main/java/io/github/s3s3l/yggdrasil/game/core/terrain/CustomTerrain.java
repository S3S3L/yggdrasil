package io.github.s3s3l.yggdrasil.game.core.terrain;

import io.github.s3s3l.yggdrasil.game.core.map.Terrain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 自定义地形
 * 允许用户定义自己的地形类型
 */
@Getter
@RequiredArgsConstructor
public class CustomTerrain implements Terrain {

    /**
     * 地形名称
     */
    private final String name;

    /**
     * 地形描述
     */
    private final String description;

    /**
     * 移动消耗
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
    public String getName() {
        return name;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public float getMovementCost() {
        return movementCost;
    }

    @Override
    public int getDefenseBonus() {
        return defenseBonus;
    }

    @Override
    public int getCoverValue() {
        return coverValue;
    }

    @Override
    public boolean blocksVision() {
        return blocksVision;
    }

    @Override
    public boolean isPassable() {
        return passable;
    }
}
