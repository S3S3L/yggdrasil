package io.github.s3s3l.yggdrasil.game.core.event;

import io.github.s3s3l.yggdrasil.game.core.event.handler.EventHandler;

/**
 * 事件标记接口
 * 所有事件都应实现此接口
 *
 * @see EventContext
 * @see EventBus
 * @see EventHandler
 */
public interface Event {

    /**
     * 获取事件优先级
     * 默认优先级为普通
     *
     * @return 事件优先级
     */
    default EventPriority getPriority() {
        return EventPriority.NORMAL;
    }

    /**
     * 事件是否可取消
     * 可取消的事件可以通过 EventContext.cancel() 取消
     *
     * @return true 如果事件可取消
     */
    default boolean isCancellable() {
        return true;
    }
}
