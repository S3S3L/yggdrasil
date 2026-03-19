package io.github.s3s3l.yggdrasil.game.core.unit;

import lombok.Builder;
import lombok.Getter;

/**
 * 单位属性
 * 定义单位的各项基础属性
 */
@Getter
@Builder
public class UnitStats {

    /**
     * 最大生命值
     */
    private final int maxHp;

    /**
     * 最大行动点数
     */
    private final int maxAp;

    /**
     * 移动力（每回合可移动的最大格数）
     */
    private final int movement;

    /**
     * 物理攻击力
     */
    private final int attack;

    /**
     * 物理防御力
     */
    private final int defense;

    /**
     * 特殊攻击力（用于技能等）
     */
    private final int specialAttack;

    /**
     * 特殊防御力
     */
    private final int specialDefense;

    /**
     * 暴击率（0-100）
     */
    private final int criticalRate;

    /**
     * 闪避率（0-100）
     */
    private final int evasion;

    /**
     * 视野范围
     */
    private final int vision;

    /**
     * 攻击范围
     */
    private final int range;

    /**
     * 创建默认单位属性
     *
     * @return 默认属性
     */
    public static UnitStats createDefault() {
        return UnitStats.builder()
                .maxHp(100)
                .maxAp(10)
                .movement(5)
                .attack(20)
                .defense(10)
                .specialAttack(0)
                .specialDefense(0)
                .criticalRate(10)
                .evasion(5)
                .vision(5)
                .range(1)
                .build();
    }

    /**
     * 创建步兵单位属性
     *
     * @return 步兵属性
     */
    public static UnitStats createInfantry() {
        return UnitStats.builder()
                .maxHp(100)
                .maxAp(10)
                .movement(5)
                .attack(20)
                .defense(15)
                .specialAttack(0)
                .specialDefense(10)
                .criticalRate(10)
                .evasion(10)
                .vision(5)
                .range(1)
                .build();
    }

    /**
     * 创建骑兵单位属性
     *
     * @return 骑兵属性
     */
    public static UnitStats createCavalry() {
        return UnitStats.builder()
                .maxHp(120)
                .maxAp(10)
                .movement(7)
                .attack(25)
                .defense(10)
                .specialAttack(0)
                .specialDefense(5)
                .criticalRate(15)
                .evasion(5)
                .vision(6)
                .range(1)
                .build();
    }

    /**
     * 创建弓箭手单位属性
     *
     * @return 弓箭手属性
     */
    public static UnitStats createArcher() {
        return UnitStats.builder()
                .maxHp(80)
                .maxAp(10)
                .movement(5)
                .attack(22)
                .defense(8)
                .specialAttack(0)
                .specialDefense(5)
                .criticalRate(20)
                .evasion(15)
                .vision(7)
                .range(3)
                .build();
    }
}
