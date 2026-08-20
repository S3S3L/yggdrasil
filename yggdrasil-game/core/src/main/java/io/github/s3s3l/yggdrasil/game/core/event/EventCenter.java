package io.github.s3s3l.yggdrasil.game.core.event;

/**
 * 提供默认的全局事件分发器。
 */
public final class EventCenter {

    private static final EventDispatcher DEFAULT = new EventDispatcher();

    private EventCenter() {
    }

    public static EventDispatcher get() {
        return DEFAULT;
    }
}
