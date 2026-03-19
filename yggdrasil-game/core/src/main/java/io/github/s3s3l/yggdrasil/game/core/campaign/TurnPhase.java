package io.github.s3s3l.yggdrasil.game.core.campaign;

/**
 * 回合阶段
 */
public enum TurnPhase {
    /**
     * 开始阶段
     */
    START("开始阶段"),
    /**
     * 行动阶段
     */
    ACTION("行动阶段"),
    /**
     * 结束阶段
     */
    END("结束阶段");

    private final String displayName;

    TurnPhase(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
