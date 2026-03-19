package io.github.s3s3l.yggdrasil.game.core.campaign;

/**
 * 回合状态
 */
public enum TurnState {
    /**
     * 进行中
     */
    IN_PROGRESS("进行中"),
    /**
     * 已结束
     */
    FINISHED("已结束"),
    /**
     * 已跳过
     */
    SKIPPED("已跳过");

    private final String displayName;

    TurnState(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
