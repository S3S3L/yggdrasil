package io.github.s3s3l.yggdrasil.game.core.strategy;

/**
 * 建筑状态
 */
public enum BuildingState {
    /**
     * 建造中
     */
    UNDER_CONSTRUCTION("建造中"),
    /**
     * 已建造
     */
    CONSTRUCTED("已建造"),
    /**
     * 运行中
     */
    OPERATIONAL("运行中"),
    /**
     * 受损
     */
    DAMAGED("受损"),
    /**
     * 被摧毁
     */
    DESTROYED("被摧毁"),
    /**
     * 废弃
     */
    ABANDONED("废弃");

    private final String displayName;

    BuildingState(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
