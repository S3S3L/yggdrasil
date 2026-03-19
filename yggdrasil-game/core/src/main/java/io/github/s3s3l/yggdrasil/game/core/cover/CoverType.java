package io.github.s3s3l.yggdrasil.game.core.cover;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 掩体类型枚举
 * 定义不同类型的掩体
 */
@Getter
@RequiredArgsConstructor
public enum CoverType {
    /**
     * 无掩体
     */
    NONE("无掩体", 0, 0),

    /**
     * 轻掩体（草丛、小石头）
     */
    LIGHT("轻掩体", 25, 5),

    /**
     * 半掩体（矮墙、箱子）
     */
    HALF("半掩体", 50, 10),

    /**
     * 全掩体（高墙、建筑物）
     */
    FULL("全掩体", 80, 20),

    /**
     * 坚固掩体（钢筋混凝土）
     */
    HARD("坚固掩体", 90, 25);

    /**
     * 掩体名称
     */
    private final String name;

    /**
     * 伤害减免百分比（0-100）
     */
    private final int damageReduction;

    /**
     * 防御加成
     */
    private final int defenseBonus;

    /**
     * 检查是否是有效掩体
     *
     * @return true 如果是有效掩体（非无掩体）
     */
    public boolean isValid() {
        return this != NONE;
    }

    /**
     * 计算实际伤害减免
     *
     * @param damage 原始伤害
     * @return 减免后的伤害
     */
    public int applyDamageReduction(int damage) {
        if (damage <= 0) {
            return 0;
        }
        int reduced = damage * (100 - damageReduction) / 100;
        return Math.max(1, reduced); // 至少造成 1 点伤害
    }

    /**
     * 计算总防御加成
     *
     * @param baseDefense 基础防御
     * @return 总防御
     */
    public int getTotalDefense(int baseDefense) {
        return baseDefense + defenseBonus;
    }
}
