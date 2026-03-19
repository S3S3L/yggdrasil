package io.github.s3s3l.yggdrasil.game.core.turn;

/**
 * 回合阶段枚举
 * 定义回合制游戏中每个回合的阶段
 */
public enum TurnPhase {
    /**
     * 准备阶段
     * - 恢复行动力
     * - 减少技能冷却
     * - 处理持续效果
     */
    PREPARATION("准备阶段"),

    /**
     * 行动阶段
     * - 单位可以移动
     * - 单位可以使用技能
     * - 单位可以攻击
     */
    ACTION("行动阶段"),

    /**
     * 结束阶段
     * - 清理临时效果
     * - 检查胜利条件
     * - 切换到下一回合
     */
    END("结束阶段");

    /**
     * 阶段名称
     */
    private final String displayName;

    TurnPhase(String displayName) {
        this.displayName = displayName;
    }

    /**
     * 获取显示名称
     *
     * @return 显示名称
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * 检查是否是行动阶段
     *
     * @return true 如果是行动阶段
     */
    public boolean isActionPhase() {
        return this == ACTION;
    }

    /**
     * 检查是否是准备阶段
     *
     * @return true 如果是准备阶段
     */
    public boolean isPreparationPhase() {
        return this == PREPARATION;
    }

    /**
     * 检查是否是结束阶段
     *
     * @return true 如果是结束阶段
     */
    public boolean isEndPhase() {
        return this == END;
    }
}
