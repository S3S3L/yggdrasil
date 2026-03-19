package io.github.s3s3l.yggdrasil.game.core.campaign;

/**
 * 战役状态
 */
public enum CampaignState {
    /**
     * 未开始
     */
    NOT_STARTED("未开始"),
    /**
     * 进行中
     */
    IN_PROGRESS("进行中"),
    /**
     * 暂停
     */
    PAUSED("暂停"),
    /**
     * 已结束
     */
    FINISHED("已结束");

    private final String displayName;

    CampaignState(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
