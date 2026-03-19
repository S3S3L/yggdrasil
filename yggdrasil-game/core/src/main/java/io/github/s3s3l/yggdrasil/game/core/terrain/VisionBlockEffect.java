package io.github.s3s3l.yggdrasil.game.core.terrain;

import io.github.s3s3l.yggdrasil.game.core.grid.Coordinate;
import io.github.s3s3l.yggdrasil.game.core.map.Terrain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 视野阻挡效果
 * 影响单位的视野和视线
 */
@Getter
public class VisionBlockEffect implements TerrainEffect {

    /**
     * 效果名称
     */
    private static final String NAME = "视野阻挡";

    /**
     * 效果描述
     */
    private static final String DESCRIPTION = "影响单位的视野和视线";

    /**
     * 效果优先级
     */
    private static final int PRIORITY = 90;

    /**
     * 视野阻挡值（0-100）
     * 0 = 完全透明，100 = 完全阻挡
     */
    private final int blockValue;

    /**
     * 视野范围修正（正数增加视野，负数减少视野）
     */
    private final int visionRangeModifier;

    /**
     * 创建视野阻挡效果
     *
     * @param blockValue 阻挡值（0-100）
     * @param visionRangeModifier 视野范围修正
     */
    public VisionBlockEffect(int blockValue, int visionRangeModifier) {
        this.blockValue = blockValue;
        this.visionRangeModifier = visionRangeModifier;
    }

    /**
     * 创建视野阻挡效果（仅阻挡值）
     *
     * @param blockValue 阻挡值（0-100）
     */
    public VisionBlockEffect(int blockValue) {
        this(blockValue, 0);
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public String getDescription() {
        return DESCRIPTION + " (阻挡：" + blockValue + "%, 视野修正：" + visionRangeModifier + ")";
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

        boolean blocksVision = blockValue >= 100 || (terrain.blocksVision() && blockValue > 0);

        return new ModifiedTerrain(terrain, terrain.getMovementCost(), terrain.getDefenseBonus(),
                terrain.getCoverValue(), blocksVision, terrain.isPassable());
    }

    @Override
    public boolean canStackWith(TerrainEffect other) {
        return other instanceof VisionBlockEffect;
    }

    @Override
    public TerrainEffect merge(TerrainEffect other) {
        if (!canStackWith(other)) {
            return this;
        }

        VisionBlockEffect otherEffect = (VisionBlockEffect) other;
        int mergedBlockValue = Math.min(100, this.blockValue + otherEffect.blockValue);
        int mergedVisionModifier = this.visionRangeModifier + otherEffect.visionRangeModifier;

        return new VisionBlockEffect(mergedBlockValue, mergedVisionModifier);
    }
}
