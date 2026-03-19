package io.github.s3s3l.yggdrasil.game._2d.event.keyboard;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import io.github.s3s3l.yggdrasil.game.core.event.EventBus;
import io.github.s3s3l.yggdrasil.game.core.event.handler.EventHandler;
import io.github.s3s3l.yggdrasil.game._2d.event.keyboard.handler.KeyboardEventHandler;

/**
 * 键盘事件管理器
 * 处理和管理所有键盘事件的分发和处理
 */
public class KeyboardEventManager {

    private static final KeyboardEventManager INSTANCE = new KeyboardEventManager();

    /**
     * 键盘事件处理器列表
     */
    private final List<KeyboardEventHandler> handlers = new ArrayList<>();

    /**
     * 注册的特定按键监听器
     */
    private final Map<Integer, List<EventHandler<KeyboardEvent>>> keyListeners = new ConcurrentHashMap<>();

    /**
     * 组合键监听器
     */
    private final List<ComboKeyHandler> comboKeyHandlers = new ArrayList<>();

    private KeyboardEventManager() {
    }

    public static KeyboardEventManager get() {
        return INSTANCE;
    }

    /**
     * 注册键盘事件处理器
     *
     * @param handler 键盘事件处理器
     */
    public void addHandler(KeyboardEventHandler handler) {
        handlers.add(handler);
    }

    /**
     * 移除键盘事件处理器
     *
     * @param handler 键盘事件处理器
     */
    public void removeHandler(KeyboardEventHandler handler) {
        handlers.remove(handler);
    }

    /**
     * 注册特定按键的监听器
     *
     * @param keyCode  按键代码
     * @param listener 监听器
     */
    public void addKeyListener(int keyCode, EventHandler<KeyboardEvent> listener) {
        keyListeners.computeIfAbsent(keyCode, k -> new ArrayList<>()).add(listener);
    }

    /**
     * 移除特定按键的监听器
     *
     * @param keyCode  按键代码
     * @param listener 监听器
     */
    public void removeKeyListener(int keyCode, EventHandler<KeyboardEvent> listener) {
        List<EventHandler<KeyboardEvent>> listeners = keyListeners.get(keyCode);
        if (listeners != null) {
            listeners.remove(listener);
        }
    }

    /**
     * 注册组合键处理器
     *
     * @param handler 组合键处理器
     */
    public void addComboKeyHandler(ComboKeyHandler handler) {
        comboKeyHandlers.add(handler);
    }

    /**
     * 移除组合键处理器
     *
     * @param handler 组合键处理器
     */
    public void removeComboKeyHandler(ComboKeyHandler handler) {
        comboKeyHandlers.remove(handler);
    }

    /**
     * 触发按键按下事件
     *
     * @param keyCode     按键代码
     * @param controlDown Ctrl 键状态
     * @param shiftDown   Shift 键状态
     * @param altDown     Alt 键状态
     */
    public void dispatchKeyDown(int keyCode, boolean controlDown, boolean shiftDown, boolean altDown) {
        KeyboardEvent event = KeyboardEvent.keyDown(this, keyCode, controlDown, shiftDown, altDown);
        handleEvent(event);
    }

    /**
     * 触发按键释放事件
     *
     * @param keyCode     按键代码
     * @param controlDown Ctrl 键状态
     * @param shiftDown   Shift 键状态
     * @param altDown     Alt 键状态
     */
    public void dispatchKeyUp(int keyCode, boolean controlDown, boolean shiftDown, boolean altDown) {
        KeyboardEvent event = KeyboardEvent.keyUp(this, keyCode, controlDown, shiftDown, altDown);
        handleEvent(event);
    }

    /**
     * 触发字符输入事件
     *
     * @param character   输入字符
     * @param controlDown Ctrl 键状态
     * @param shiftDown   Shift 键状态
     * @param altDown     Alt 键状态
     */
    public void dispatchKeyTyped(char character, boolean controlDown, boolean shiftDown, boolean altDown) {
        KeyboardEvent event = KeyboardEvent.keyTyped(this, character, controlDown, shiftDown, altDown);
        handleEvent(event);
    }

    /**
     * 处理键盘事件
     *
     * @param event 键盘事件
     */
    private void handleEvent(KeyboardEvent event) {
        // 通过 EventBus 分发
        EventBus.get().post(event);

        // 调用注册的处理器
        for (KeyboardEventHandler handler : handlers) {
            if (!event.isCancelled()) {
                handler.handle(event);
            }
        }

        // 调用特定按键的监听器
        List<EventHandler<KeyboardEvent>> listeners = keyListeners.get(event.getKeyCode());
        if (listeners != null) {
            for (EventHandler<KeyboardEvent> listener : listeners) {
                if (!event.isCancelled()) {
                    listener.handle(event);
                }
            }
        }

        // 调用组合键处理器
        for (ComboKeyHandler handler : comboKeyHandlers) {
            if (!event.isCancelled()) {
                handler.handle(event);
            }
        }
    }

    /**
     * 清除所有事件处理器和监听器
     */
    public void clear() {
        handlers.clear();
        keyListeners.clear();
        comboKeyHandlers.clear();
    }

    /**
     * 组合键处理器接口
     */
    @FunctionalInterface
    public interface ComboKeyHandler {
        /**
         * 处理组合键事件
         *
         * @param event 键盘事件
         */
        void handle(KeyboardEvent event);
    }
}
