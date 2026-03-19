package io.github.s3s3l.yggdrasil.game.core.unit;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 阵营枚举
 * 定义单位所属的阵营
 */
@Getter
@RequiredArgsConstructor
public enum Faction {
    /**
     * 玩家阵营
     */
    PLAYER("玩家", "玩家控制的角色"),

    /**
     * 盟友阵营
     */
    ALLY("盟友", "友方 NPC"),

    /**
     * 敌人阵营
     */
    ENEMY("敌人", "敌方单位"),

    /**
     * 中立阵营
     */
    NEUTRAL("中立", "中立单位");

    /**
     * 阵营名称
     */
    private final String displayName;

    /**
     * 阵营描述
     */
    private final String description;

    /**
     * 检查是否与指定阵营友好
     *
     * @param other 其他阵营
     * @return true 如果友好
     */
    public boolean isFriendlyWith(Faction other) {
        if (other == null) return false;
        // 玩家和盟友是友好的
        return (this == PLAYER && other == ALLY) ||
               (this == ALLY && other == PLAYER) ||
               (this == other);
    }

    /**
     * 检查是否与指定阵营敌对
     *
     * @param other 其他阵营
     * @return true 如果敌对
     */
    public boolean isHostileTo(Faction other) {
        if (other == null) return true;
        // 玩家/盟友与敌人敌对
        return (this == PLAYER && other == ENEMY) ||
               (this == ALLY && other == ENEMY) ||
               (this == ENEMY && (other == PLAYER || other == ALLY));
    }
}
