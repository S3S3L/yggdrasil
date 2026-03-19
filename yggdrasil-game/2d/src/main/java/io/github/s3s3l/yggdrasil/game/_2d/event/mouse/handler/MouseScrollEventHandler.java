package io.github.s3s3l.yggdrasil.game._2d.event.mouse.handler;

import io.github.s3s3l.yggdrasil.game._2d.event.mouse.MouseScrollEvent;
import io.github.s3s3l.yggdrasil.game.core.event.handler.EventHandler;

/**
 * 鼠标滚轮事件处理器接口
 */
@FunctionalInterface
public interface MouseScrollEventHandler extends EventHandler<MouseScrollEvent> {

    /**
     * 处理鼠标滚轮事件
     *
     * @param event 鼠标滚轮事件
     */
    @Override
    void handle(MouseScrollEvent event);

    /**
     * 当鼠标向上滚动时调用
     *
     * @param event 鼠标滚轮事件
     */
    default void onScrollUp(MouseScrollEvent event) {
        if (event.isScrollUp()) {
            handle(event);
        }
    }

    /**
     * 当鼠标向下滚动时调用
     *
     * @param event 鼠标滚轮事件
     */
    default void onScrollDown(MouseScrollEvent event) {
        if (event.isScrollDown()) {
            handle(event);
        }
    }
}
