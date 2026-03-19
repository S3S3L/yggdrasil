package io.github.s3s3l.yggdrasil.game.core.strategy;

/**
 * 科技等级
 */
public enum TechnologyTier {
    /**
     * 第一等级
     */
    TIER_1("T1"),
    /**
     * 第二等级
     */
    TIER_2("T2"),
    /**
     * 第三等级
     */
    TIER_3("T3"),
    /**
     * 第四等级
     */
    TIER_4("T4"),
    /**
     * 第五等级
     */
    TIER_5("T5");

    private final String displayName;

    TechnologyTier(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
