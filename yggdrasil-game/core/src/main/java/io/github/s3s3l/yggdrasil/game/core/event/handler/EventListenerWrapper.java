package io.github.s3s3l.yggdrasil.game.core.event.handler;

import io.github.s3s3l.yggdrasil.game.core.event.Event;
import io.github.s3s3l.yggdrasil.game.core.event.EventPriority;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 事件监听器包装器
 * 包含监听器实例和优先级信息
 *
 * @param <E> 事件类型
 */
@RequiredArgsConstructor
public class EventListenerWrapper<E extends Event> {

    /**
     * 事件监听器实例
     */
    @Getter
    private final EventHandler<E> listener;

    /**
     * 监听器注册的优先级
     */
    @Getter
    private final EventPriority priority;

    /**
     * 监听器是否已被移除
     */
    private volatile boolean removed = false;

    /**
     * 调用监听器
     *
     * @param event 事件对象
     */
    public void invoke(E event) {
        if (!removed) {
            listener.handle(event);
        }
    }

    /**
     * 移除监听器
     */
    public void remove() {
        this.removed = true;
    }

    /**
     * 检查监听器是否有效
     *
     * @return true 如果监听器未被移除
     */
    public boolean isValid() {
        return !removed;
    }
}
