package io.github.s3s3l.yggdrasil.game.core.campaign;

/**
 * 区域类型
 */
public enum RegionType {
    /**
     * 普通区域
     */
    NORMAL("普通区域"),
    /**
     * 首都
     */
    CAPITAL("首都"),
    /**
     * 军事基地
     */
    MILITARY_BASE("军事基地"),
    /**
     * 资源点
     */
    RESOURCE_POINT("资源点"),
    /**
     * 战略要地
     */
    STRATEGIC_POINT("战略要地"),
    /**
     * 中立区域
     */
    NEUTRAL("中立区域");

    private final String displayName;

    RegionType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
