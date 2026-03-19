package io.github.s3s3l.yggdrasil.game.core.strategy;

/**
 * 物品类型
 */
public enum ItemType {
    /**
     * 单位
     */
    UNIT("单位"),
    /**
     * 建筑
     */
    BUILDING("建筑"),
    /**
     * 科技
     */
    TECHNOLOGY("科技"),
    /**
     * 升级
     */
    UPGRADE("升级"),
    /**
     * 道具
     */
    ITEM("道具");

    private final String displayName;

    ItemType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
