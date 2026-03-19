package io.github.s3s3l.yggdrasil.game.core.unit;

import io.github.s3s3l.yggdrasil.game.core.grid.Coordinate;

/**
 * 基础单位实现
 */
public class BasicUnit extends AbstractUnit {

    /**
     * 创建基础单位
     *
     * @param id       单位 ID
     * @param name     单位名称
     * @param unitType 单位类型
     * @param stats    单位属性
     * @param faction  所属阵营
     */
    public BasicUnit(long id, String name, UnitType unitType, UnitStats stats, Faction faction) {
        super(id, name, unitType, stats, faction);
    }

    /**
     * 创建基础单位（带初始位置）
     *
     * @param id       单位 ID
     * @param name     单位名称
     * @param unitType 单位类型
     * @param stats    单位属性
     * @param faction  所属阵营
     * @param position 初始位置
     */
    public BasicUnit(long id, String name, UnitType unitType, UnitStats stats, Faction faction, Coordinate position) {
        super(id, name, unitType, stats, faction);
        this.position = position;
    }

    @Override
    public String toString() {
        return String.format("Unit[%s](%s, HP:%d/%d, AP:%d/%d)",
                name, unitType.getDisplayName(),
                currentHp, stats.getMaxHp(),
                currentAp, stats.getMaxAp());
    }
}
