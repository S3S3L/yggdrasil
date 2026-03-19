package io.github.s3s3l.yggdrasil.game.core.strategy;

/**
 * 资源类型
 */
public enum ResourceType {
    /**
     * 材料资源
     */
    MATERIAL("材料资源"),
    /**
     * 人力资源
     */
    MANPOWER("人力资源"),
    /**
     * 能源资源
     */
    ENERGY("能源资源"),
    /**
     * 食物资源
     */
    FOOD("食物资源"),
    /**
     * 金钱资源
     */
    MONEY("金钱资源"),
    /**
     * 科技资源
     */
    SCIENCE("科技资源"),
    /**
     * 影响力资源
     */
    INFLUENCE("影响力资源");

    private final String displayName;

    ResourceType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
