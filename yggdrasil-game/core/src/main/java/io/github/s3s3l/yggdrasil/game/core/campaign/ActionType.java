package io.github.s3s3l.yggdrasil.game.core.campaign;

/**
 * 行动类型
 */
public enum ActionType {
    /**
     * 移动
     */
    MOVE("移动"),
    /**
     * 攻击
     */
    ATTACK("攻击"),
    /**
     * 建造
     */
    BUILD("建造"),
    /**
     * 研究
     */
    RESEARCH("研究"),
    /**
     * 生产
     */
    PRODUCE("生产"),
    /**
     * 交易
     */
    TRADE("交易"),
    /**
     * 外交
     */
    DIPLOMACY("外交"),
    /**
     * 等待
     */
    WAIT("等待");

    private final String displayName;

    ActionType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
