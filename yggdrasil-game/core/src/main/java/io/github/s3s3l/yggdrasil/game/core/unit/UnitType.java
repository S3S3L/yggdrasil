package io.github.s3s3l.yggdrasil.game.core.unit;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 单位类型枚举
 * 定义不同单位的类型和克制关系
 */
@Getter
@RequiredArgsConstructor
public enum UnitType {
    /**
     * 步兵
     */
    INFANTRY("步兵", "近战基础单位，平衡型"),

    /**
     * 骑兵
     */
    CAVALRY("骑兵", "高机动性单位，克制弓箭手"),

    /**
     * 弓箭手
     */
    ARCHER("弓箭手", "远程单位，克制步兵"),

    /**
     * 枪兵
     */
    LANCER("枪兵", "克制骑兵的近战单位"),

    /**
     * 法师
     */
    MAGE("法师", "远程魔法单位，高特攻低防御"),

    /**
     * 治疗者
     */
    HEALER("治疗者", "支援单位，可以恢复生命值"),

    /**
     * 坦克
     */
    TANK("坦克", "高防御单位，移动力较低"),

    /**
     * 刺客
     */
    ASSASSIN("刺客", "高暴击高闪避，低生命值");

    /**
     * 类型名称
     */
    private final String displayName;

    /**
     * 类型描述
     */
    private final String description;

    /**
     * 检查是否克制其他类型
     *
     * @param other 其他类型
     * @return true 如果克制
     */
    public boolean hasAdvantageOver(UnitType other) {
        if (other == null) return false;

        // 经典三角克制：步兵 > 弓箭手 > 骑兵 > 步兵
        if (this == INFANTRY && other == ARCHER) return true;
        if (this == ARCHER && other == CAVALRY) return true;
        if (this == CAVALRY && other == INFANTRY) return true;

        // 枪兵克制骑兵
        if (this == LANCER && other == CAVALRY) return true;

        // 刺客克制法师和治疗者
        if (this == ASSASSIN && (other == MAGE || other == HEALER)) return true;

        return false;
    }

    /**
     * 检查是否被其他类型克制
     *
     * @param other 其他类型
     * @return true 如果被克制
     */
    public boolean hasDisadvantageAgainst(UnitType other) {
        return other != null && other.hasAdvantageOver(this);
    }

    /**
     * 获取克制关系的伤害修正
     *
     * @param attacker 攻击方类型
     * @param defender 防御方类型
     * @return 伤害修正（1.0 为正常，>1.0 为克制，<1.0 为被克）
     */
    public static float getTypeModifier(UnitType attacker, UnitType defender) {
        if (attacker == null || defender == null) {
            return 1.0f;
        }

        if (attacker.hasAdvantageOver(defender)) {
            return 1.5f; // 克制时 1.5 倍伤害
        }

        if (attacker.hasDisadvantageAgainst(defender)) {
            return 0.8f; // 被克时 0.8 倍伤害
        }

        return 1.0f; // 正常伤害
    }
}
