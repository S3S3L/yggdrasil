package io.github.s3s3l.yggdrasil.game._2d.event.keyboard.handler;

import io.github.s3s3l.yggdrasil.game._2d.event.keyboard.KeyboardEvent;
import io.github.s3s3l.yggdrasil.game.core.event.handler.EventHandler;

/**
 * 键盘事件处理器接口
 */
@FunctionalInterface
public interface KeyboardEventHandler extends EventHandler<KeyboardEvent> {

    /**
     * 处理键盘事件
     *
     * @param event 键盘事件
     */
    @Override
    void handle(KeyboardEvent event);

    /**
     * 当按键按下时调用
     *
     * @param event 键盘事件
     */
    default void onKeyDown(KeyboardEvent event) {
        if (event.getType() == KeyboardEvent.KeyboardEventType.KEY_DOWN) {
            handle(event);
        }
    }

    /**
     * 当按键释放时调用
     *
     * @param event 键盘事件
     */
    default void onKeyUp(KeyboardEvent event) {
        if (event.getType() == KeyboardEvent.KeyboardEventType.KEY_UP) {
            handle(event);
        }
    }

    /**
     * 当字符输入时调用
     *
     * @param event 键盘事件
     */
    default void onKeyTyped(KeyboardEvent event) {
        if (event.getType() == KeyboardEvent.KeyboardEventType.KEY_TYPED) {
            handle(event);
        }
    }

    /**
     * 当按下特定键时调用
     *
     * @param keyCode 按键代码
     * @param event   键盘事件
     */
    default void onKeyPressed(int keyCode, KeyboardEvent event) {
        if (event.getKeyCode() == keyCode && event.getType() == KeyboardEvent.KeyboardEventType.KEY_DOWN) {
            handle(event);
        }
    }

    /**
     * 当按下 Ctrl 键时调用
     *
     * @param event 键盘事件
     */
    default void onCtrlDown(KeyboardEvent event) {
        if (event.isControlDown() && event.getType() == KeyboardEvent.KeyboardEventType.KEY_DOWN) {
            handle(event);
        }
    }

    /**
     * 当按下 Shift 键时调用
     *
     * @param event 键盘事件
     */
    default void onShiftDown(KeyboardEvent event) {
        if (event.isShiftDown() && event.getType() == KeyboardEvent.KeyboardEventType.KEY_DOWN) {
            handle(event);
        }
    }

    /**
     * 当按下 Alt 键时调用
     *
     * @param event 键盘事件
     */
    default void onAltDown(KeyboardEvent event) {
        if (event.isAltDown() && event.getType() == KeyboardEvent.KeyboardEventType.KEY_DOWN) {
            handle(event);
        }
    }
}
