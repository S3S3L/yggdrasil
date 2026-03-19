package io.github.s3s3l.yggdrasil.game.core.unit;

import io.github.s3s3l.yggdrasil.game.core.grid.Coordinate;
import io.github.s3s3l.yggdrasil.game.core.grid.SquareCoordinate;
import lombok.Getter;
import lombok.Setter;

/**
 * 单位抽象基类
 * 提供单位的通用实现
 */
@Getter
public abstract class AbstractUnit implements Unit {

    /**
     * 单位 ID
     */
    protected long id;

    /**
     * 单位名称
     */
    protected String name;

    /**
     * 单位类型
     */
    protected UnitType unitType;

    /**
     * 单位属性
     */
    protected UnitStats stats;

    /**
     * 所属阵营
     */
    protected Faction faction;

    /**
     * 当前位置
     */
    @Setter
    protected Coordinate position;

    /**
     * 当前生命值
     */
    protected int currentHp;

    /**
     * 当前行动点数
     */
    protected int currentAp;

    /**
     * 创建单位
     *
     * @param id       单位 ID
     * @param name     单位名称
     * @param unitType 单位类型
     * @param stats    单位属性
     * @param faction  所属阵营
     */
    protected AbstractUnit(long id, String name, UnitType unitType, UnitStats stats, Faction faction) {
        this.id = id;
        this.name = name;
        this.unitType = unitType;
        this.stats = stats;
        this.faction = faction;
        this.position = SquareCoordinate.of(0, 0);

        // 初始化生命值和行动点
        this.currentHp = stats.getMaxHp();
        this.currentAp = stats.getMaxAp();
    }

    @Override
    public int getCurrentHp() {
        return currentHp;
    }

    @Override
    public int getCurrentAp() {
        return currentAp;
    }

    @Override
    public boolean isAlive() {
        return currentHp > 0;
    }

    @Override
    public int takeDamage(int damage) {
        if (!isAlive()) {
            return 0;
        }

        // 应用防御减免（至少造成 1 点伤害）
        int actualDamage = Math.max(1, damage - stats.getDefense());

        // 应用类型克制修正
        float modifier = UnitType.getTypeModifier(this.unitType, this.unitType);
        actualDamage = (int) (actualDamage * modifier);

        currentHp = Math.max(0, currentHp - actualDamage);
        return actualDamage;
    }

    @Override
    public void heal(int amount) {
        if (!isAlive()) {
            return;
        }

        currentHp = Math.min(stats.getMaxHp(), currentHp + amount);
    }

    @Override
    public boolean spendAp(int amount) {
        if (currentAp >= amount) {
            currentAp -= amount;
            return true;
        }
        return false;
    }

    @Override
    public void restoreAp(int amount) {
        currentAp = Math.min(stats.getMaxAp(), currentAp + amount);
    }

    @Override
    public void resetAp() {
        currentAp = stats.getMaxAp();
    }

    @Override
    public boolean isOfFaction(Faction faction) {
        return this.faction == faction;
    }

    /**
     * 设置单位 ID（受保护，仅用于反序列化等场景）
     *
     * @param id 单位 ID
     */
    @Override
    public void setId(long id) {
        this.id = id;
    }
}
