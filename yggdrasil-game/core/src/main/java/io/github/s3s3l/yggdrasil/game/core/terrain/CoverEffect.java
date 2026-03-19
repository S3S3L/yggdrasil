package io.github.s3s3l.yggdrasil.game.core.terrain;

import io.github.s3s3l.yggdrasil.game.core.grid.Coordinate;
import io.github.s3s3l.yggdrasil.game.core.map.Terrain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 掩体效果
 * 为_unit_提供防御加成
 */
@Getter
public class CoverEffect implements TerrainEffect {

    /**
     * 效果名称
     */
    private static final String NAME = "掩体";

    /**
     * 效果描述
     */
    private static final String DESCRIPTION = "为单位提供防御加成";

    /**
     * 效果优先级
     */
    private static final int PRIORITY = 80;

    /**
     * 掩体值（0-100）
     * 表示伤害减少百分比
     */
    private final int coverValue;

    /**
     * 防御加成值
     */
    private final int defenseBonus;

    /**
     * 创建掩体效果
     *
     * @param coverValue 掩体值（0-100）
     * @param defenseBonus 防御加成
     */
    public CoverEffect(int coverValue, int defenseBonus) {
        this.coverValue = coverValue;
        this.defenseBonus = defenseBonus;
    }

    /**
     * 创建掩体效果（仅掩体值）
     *
     * @param coverValue 掩体值（0-100）
     */
    public CoverEffect(int coverValue) {
        this(coverValue, 0);
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public String getDescription() {
        return DESCRIPTION + " (掩体：" + coverValue + "%, 防御加成：" + defenseBonus + ")";
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

        int newCoverValue = Math.min(100, Math.max(0, terrain.getCoverValue() + coverValue));
        int newDefenseBonus = terrain.getDefenseBonus() + defenseBonus;

        return new ModifiedTerrain(terrain, terrain.getMovementCost(), newDefenseBonus,
                newCoverValue, terrain.blocksVision(), terrain.isPassable());
    }

    @Override
    public boolean canStackWith(TerrainEffect other) {
        return other instanceof CoverEffect;
    }

    @Override
    public TerrainEffect merge(TerrainEffect other) {
        if (!canStackWith(other)) {
            return this;
        }

        CoverEffect otherEffect = (CoverEffect) other;
        int mergedCoverValue = Math.min(100, this.coverValue + otherEffect.coverValue);
        int mergedDefenseBonus = this.defenseBonus + otherEffect.defenseBonus;

        return new CoverEffect(mergedCoverValue, mergedDefenseBonus);
    }
}
