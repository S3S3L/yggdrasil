package io.github.s3s3l.yggdrasil.game.core.terrain;

import io.github.s3s3l.yggdrasil.game.core.map.Terrain;

import lombok.Getter;

/**
 * 修改后的地形
 * 用于包装原始地形并应用效果修改
 */
@Getter
class ModifiedTerrain implements Terrain {

    /**
     * 原始地形
     */
    private final Terrain baseTerrain;

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

    /**
     * 地形名称
     */
    private final String name;

    /**
     * 地形描述
     */
    private final String description;

    ModifiedTerrain(Terrain base, float movementCost, int defenseBonus, int coverValue,
                    boolean blocksVision, boolean passable) {
        this.baseTerrain = base;
        this.movementCost = movementCost;
        this.defenseBonus = defenseBonus;
        this.coverValue = coverValue;
        this.blocksVision = blocksVision;
        this.passable = passable;
        this.name = base.getName() + " (修改)";
        this.description = base.getDescription();
    }

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
