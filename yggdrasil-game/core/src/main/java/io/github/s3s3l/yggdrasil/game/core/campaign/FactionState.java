package io.github.s3s3l.yggdrasil.game.core.campaign;

/**
 * 势力状态
 */
public enum FactionState {
    /**
     * 活跃
     */
    ACTIVE("活跃"),
    /**
     * 被击败
     */
    DEFEATED("被击败"),
    /**
     * 胜利
     */
    VICTORIOUS("胜利"),
    /**
     * 中立
     */
    NEUTRAL("中立");

    private final String displayName;

    FactionState(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
