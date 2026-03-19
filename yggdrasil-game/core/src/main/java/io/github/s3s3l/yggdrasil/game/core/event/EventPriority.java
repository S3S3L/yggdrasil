package io.github.s3s3l.yggdrasil.game.core.event;

/**
 * 事件优先级枚举
 * 用于控制多个监听器处理同一事件的顺序
 */
public enum EventPriority {
    /**
     * 最高优先级，最先处理
     */
    HIGHEST(5),

    /**
     * 高优先级
     */
    HIGH(4),

    /**
     * 普通优先级
     */
    NORMAL(3),

    /**
     * 低优先级
     */
    LOW(2),

    /**
     * 最低优先级，最后处理
     */
    LOWEST(1);

    private final int value;

    EventPriority(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
