package io.github.s3s3l.yggdrasil.game.core.campaign;

/**
 * 外交状态
 */
public enum DiplomaticState {
    /**
     * 联盟
     */
    ALLIED("联盟"),
    /**
     * 友好
     */
    FRIENDLY("友好"),
    /**
     * 中立
     */
    NEUTRAL("中立"),
    /**
     * 冷淡
     */
    UNFRIENDLY("冷淡"),
    /**
     * 敌对
     */
    HOSTILE("敌对"),
    /**
     * 战争
     */
    WAR("战争");

    private final String displayName;

    DiplomaticState(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
