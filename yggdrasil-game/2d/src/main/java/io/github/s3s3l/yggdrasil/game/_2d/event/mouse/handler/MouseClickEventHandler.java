package io.github.s3s3l.yggdrasil.game._2d.event.mouse.handler;

import io.github.s3s3l.yggdrasil.game._2d.event.mouse.MouseClickEvent;
import io.github.s3s3l.yggdrasil.game.core.event.handler.EventHandler;

/**
 * 鼠标点击事件处理器抽象基类
 * 所有鼠标点击事件处理器都应继承此类
 */
public abstract class MouseClickEventHandler implements EventHandler<MouseClickEvent> {

    @Override
    public void handle(MouseClickEvent event) {
        // 检查事件是否被取消
        if (event.isCancelled()) {
            return;
        }

        // 调用具体的处理方法
        onClick(event);
    }

    /**
     * 鼠标点击处理方法
     *
     * @param event 鼠标点击事件
     */
    protected abstract void onClick(MouseClickEvent event);

    /**
     * 处理左键点击
     *
     * @param event 鼠标点击事件
     */
    protected void onLeftClick(MouseClickEvent event) {
        // 默认不处理
    }

    /**
     * 处理右键点击
     *
     * @param event 鼠标点击事件
     */
    protected void onRightClick(MouseClickEvent event) {
        // 默认不处理
    }

    /**
     * 处理中键点击
     *
     * @param event 鼠标点击事件
     */
    protected void onMiddleClick(MouseClickEvent event) {
        // 默认不处理
    }

    /**
     * 处理双击
     *
     * @param event 鼠标点击事件
     */
    protected void onDoubleClick(MouseClickEvent event) {
        // 默认不处理
    }
}
