package io.github.s3s3l.yggdrasil.game.core.ai.combat;

/**
 * 目标选择策略
 */
public enum TargetSelectionStrategy {
    /**
     * 最近的目标
     */
    NEAREST,

    /**
     * 生命最低的目标（最容易击杀）
     */
    WEAKEST,

    /**
     * 威胁最高的目标
     */
    MOST_DANGEROUS,

    /**
     * 伤害最高的目标
     */
    HIGHEST_DAMAGE
}
