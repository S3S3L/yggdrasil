package io.github.s3s3l.yggdrasil.game._2d.event.mouse.handler;

import io.github.s3s3l.yggdrasil.game._2d.event.mouse.MouseMotionEvent;
import io.github.s3s3l.yggdrasil.game.core.event.handler.EventHandler;

/**
 * 鼠标移动事件处理器接口
 */
@FunctionalInterface
public interface MouseMotionEventHandler extends EventHandler<MouseMotionEvent> {

    /**
     * 处理鼠标移动事件
     *
     * @param event 鼠标移动事件
     */
    @Override
    void handle(MouseMotionEvent event);

    /**
     * 当鼠标移动时调用
     *
     * @param event 鼠标移动事件
     */
    default void onMouseMove(MouseMotionEvent event) {
        handle(event);
    }

    /**
     * 当鼠标快速移动时调用（移动距离超过阈值）
     *
     * @param event   鼠标移动事件
     * @param threshold 阈值
     */
    default void onMouseQuickMove(MouseMotionEvent event, float threshold) {
        if (event.isQuickMove(threshold)) {
            handle(event);
        }
    }
}
