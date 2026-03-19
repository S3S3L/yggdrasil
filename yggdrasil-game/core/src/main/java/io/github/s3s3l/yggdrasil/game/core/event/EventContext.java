package io.github.s3s3l.yggdrasil.game.core.event;

import lombok.Getter;

/**
 * 事件上下文
 * 包含事件的元数据和状态信息
 */
public abstract class EventContext<T extends Event> {

    /**
     * 事件源对象
     */
    @Getter
    private final Object source;

    /**
     * 事件是否被取消
     * 取消的事件将不会被后续监听器处理
     */
    @Getter
    private boolean cancelled = false;

    /**
     * 事件处理时间戳（毫秒）
     */
    @Getter
    private final long timestamp;

    public EventContext(Object source) {
        this.source = source;
        this.timestamp = System.currentTimeMillis();
    }

    /**
     * 取消事件处理
     * 取消后，后续的低优先级监听器将不会收到此事件
     */
    public void cancel() {
        this.cancelled = true;
    }

    /**
     * 恢复事件处理
     */
    public void restore() {
        this.cancelled = false;
    }

    /**
     * 获取实际的事件对象
     * @return 事件对象
     */
    @SuppressWarnings("unchecked")
    public T getEvent() {
        return (T) this;
    }
}
