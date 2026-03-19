package io.github.s3s3l.yggdrasil.game.core.campaign;

/**
 * 战役地图配置
 */
public class CampaignMapConfig {

    /**
     * 地图宽度（如果是网格地图）
     */
    private int width;

    /**
     * 地图高度（如果是网格地图）
     */
    private int height;

    /**
     * 是否允许跨区域攻击
     */
    private boolean allowCrossRegionAttack;

    /**
     * 是否允许区域间移动
     */
    private boolean allowRegionMovement;

    /**
     * 默认移动消耗
     */
    private int defaultMovementCost;

    /**
     * 默认攻击加成
     */
    private int defaultAttackBonus;

    /**
     * 默认防御加成
     */
    private int defaultDefenseBonus;

    public CampaignMapConfig() {
        this.width = 0;
        this.height = 0;
        this.allowCrossRegionAttack = true;
        this.allowRegionMovement = true;
        this.defaultMovementCost = 1;
        this.defaultAttackBonus = 0;
        this.defaultDefenseBonus = 0;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public boolean isAllowCrossRegionAttack() {
        return allowCrossRegionAttack;
    }

    public void setAllowCrossRegionAttack(boolean allowCrossRegionAttack) {
        this.allowCrossRegionAttack = allowCrossRegionAttack;
    }

    public boolean isAllowRegionMovement() {
        return allowRegionMovement;
    }

    public void setAllowRegionMovement(boolean allowRegionMovement) {
        this.allowRegionMovement = allowRegionMovement;
    }

    public int getDefaultMovementCost() {
        return defaultMovementCost;
    }

    public void setDefaultMovementCost(int defaultMovementCost) {
        this.defaultMovementCost = defaultMovementCost;
    }

    public int getDefaultAttackBonus() {
        return defaultAttackBonus;
    }

    public void setDefaultAttackBonus(int defaultAttackBonus) {
        this.defaultAttackBonus = defaultAttackBonus;
    }

    public int getDefaultDefenseBonus() {
        return defaultDefenseBonus;
    }

    public void setDefaultDefenseBonus(int defaultDefenseBonus) {
        this.defaultDefenseBonus = defaultDefenseBonus;
    }
}
