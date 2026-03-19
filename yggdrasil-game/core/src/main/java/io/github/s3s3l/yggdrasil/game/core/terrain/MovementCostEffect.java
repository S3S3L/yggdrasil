package io.github.s3s3l.yggdrasil.game.core.terrain;

import io.github.s3s3l.yggdrasil.game.core.grid.Coordinate;
import io.github.s3s3l.yggdrasil.game.core.map.Terrain;
import io.github.s3s3l.yggdrasil.game.core.map.TerrainType;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 移动消耗效果
 * 改变单位在地形上的移动力消耗
 */
@Getter
public class MovementCostEffect implements TerrainEffect {

    /**
     * 效果名称
     */
    private static final String NAME = "移动消耗";

    /**
     * 效果描述
     */
    private static final String DESCRIPTION = "改变单位在地形上的移动力消耗";

    /**
     * 效果优先级
     */
    private static final int PRIORITY = 100;

    /**
     * 移动消耗倍率（1.0 为正常，>1.0 为困难，0 为不可通过）
     */
    private final float costMultiplier;

    /**
     * 移动消耗加成（固定值增加）
     */
    private final float costBonus;

    /**
     * 创建移动消耗效果（仅倍率）
     *
     * @param costMultiplier 倍率
     */
    public MovementCostEffect(float costMultiplier) {
        this(costMultiplier, 0.0f);
    }

    /**
     * 创建移动消耗效果
     *
     * @param costMultiplier 倍率
     * @param costBonus 加成
     */
    public MovementCostEffect(float costMultiplier, float costBonus) {
        this.costMultiplier = costMultiplier;
        this.costBonus = costBonus;
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public String getDescription() {
        return DESCRIPTION + " (倍率：" + costMultiplier + ", 加成：" + costBonus + ")";
    }

    @Override
    public int getPriority() {
        return PRIORITY;
    }

    @Override
    public Terrain apply(Terrain terrain, Coordinate coordinate) {
        if (terrain == null) {
            return null;
        }

        float baseCost = terrain.getMovementCost();
        float newCost = baseCost * costMultiplier + costBonus;

        // 创建新地形（使用 TerrainType 中最接近的类型）
        return new ModifiedTerrain(terrain, newCost, terrain.getDefenseBonus(),
                terrain.getCoverValue(), terrain.blocksVision(), terrain.isPassable());
    }

    @Override
    public boolean canStackWith(TerrainEffect other) {
        return other instanceof MovementCostEffect;
    }

    @Override
    public TerrainEffect merge(TerrainEffect other) {
        if (!canStackWith(other)) {
            return this;
        }

        MovementCostEffect otherEffect = (MovementCostEffect) other;
        float mergedMultiplier = this.costMultiplier * otherEffect.costMultiplier;
        float mergedBonus = this.costBonus + otherEffect.costBonus;

        return new MovementCostEffect(mergedMultiplier, mergedBonus);
    }
}
