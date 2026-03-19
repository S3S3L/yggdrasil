package io.github.s3s3l.yggdrasil.game.core.unit;

import io.github.s3s3l.yggdrasil.game.core.basic.ID;
import io.github.s3s3l.yggdrasil.game.core.grid.Coordinate;

/**
 * 单位接口
 * 定义游戏单位的基本属性和行为
 */
public interface Unit extends ID {

    /**
     * 获取单位名称
     *
     * @return 单位名称
     */
    String getName();

    /**
     * 获取单位类型
     *
     * @return 单位类型
     */
    UnitType getUnitType();

    /**
     * 获取单位当前位置
     *
     * @return 当前位置坐标
     */
    Coordinate getPosition();

    /**
     * 设置单位位置
     *
     * @param position 新位置
     */
    void setPosition(Coordinate position);

    /**
     * 获取单位属性
     *
     * @return 单位属性
     */
    UnitStats getStats();

    /**
     * 获取当前生命值
     *
     * @return 当前生命值
     */
    int getCurrentHp();

    /**
     * 获取当前行动点数
     *
     * @return 当前行动点数
     */
    int getCurrentAp();

    /**
     * 检查单位是否存活
     *
     * @return true 如果存活
     */
    boolean isAlive();

    /**
     * 受到伤害
     *
     * @param damage 伤害值
     * @return 实际受到的伤害
     */
    int takeDamage(int damage);

    /**
     * 恢复生命值
     *
     * @param amount 恢复量
     */
    void heal(int amount);

    /**
     * 消耗行动点数
     *
     * @param amount 消耗量
     * @return true 如果行动点足够
     */
    boolean spendAp(int amount);

    /**
     * 恢复行动点数
     *
     * @param amount 恢复量
     */
    void restoreAp(int amount);

    /**
     * 重置行动点数
     */
    void resetAp();

    /**
     * 单位是否属于指定阵营
     *
     * @param faction 阵营
     * @return true 如果属于该阵营
     */
    boolean isOfFaction(Faction faction);

    /**
     * 获取单位所属阵营
     *
     * @return 阵营
     */
    Faction getFaction();
}
