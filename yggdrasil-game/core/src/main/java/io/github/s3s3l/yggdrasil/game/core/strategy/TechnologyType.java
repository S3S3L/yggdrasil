package io.github.s3s3l.yggdrasil.game.core.strategy;

/**
 * 科技类型
 */
public enum TechnologyType {
    /**
     * 通用科技
     */
    GENERAL("通用科技"),
    /**
     * 军事科技
     */
    MILITARY("军事科技"),
    /**
     * 工业科技
     */
    INDUSTRIAL("工业科技"),
    /**
     * 科学科技
     */
    SCIENCE("科学科技"),
    /**
     * 魔法科技
     */
    ARCANE("魔法科技");

    private final String displayName;

    TechnologyType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
