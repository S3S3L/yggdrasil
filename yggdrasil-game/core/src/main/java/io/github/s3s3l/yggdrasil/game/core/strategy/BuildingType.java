package io.github.s3s3l.yggdrasil.game.core.strategy;

/**
 * 建筑类型
 */
public enum BuildingType {
    /**
     * 普通建筑
     */
    NORMAL("普通建筑"),
    /**
     * 军事建筑
     */
    MILITARY("军事建筑"),
    /**
     * 经济建筑
     */
    ECONOMIC("经济建筑"),
    /**
     * 科技建筑
     */
    RESEARCH("科技建筑"),
    /**
     * 防御建筑
     */
    DEFENSE("防御建筑"),
    /**
     * 奇观建筑
     */
    WONDER("奇观建筑");

    private final String displayName;

    BuildingType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
