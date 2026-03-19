package io.github.s3s3l.yggdrasil.game._2d.event.mouse;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import io.github.s3s3l.yggdrasil.game.core.event.EventBus;
import io.github.s3s3l.yggdrasil.game.core.event.handler.EventHandler;
import io.github.s3s3l.yggdrasil.game._2d.event.mouse.handler.MouseClickEventHandler;
import io.github.s3s3l.yggdrasil.game._2d.event.mouse.handler.MouseMotionEventHandler;
import io.github.s3s3l.yggdrasil.game._2d.event.mouse.handler.MouseScrollEventHandler;

/**
 * 鼠标事件管理器
 * 处理和管理所有鼠标事件的分发和处理
 */
public class MouseEventManager {

    private static final MouseEventManager INSTANCE = new MouseEventManager();

    /**
     * 鼠标点击事件处理器列表
     */
    private final List<MouseClickEventHandler> clickHandlers = new ArrayList<>();

    /**
     * 鼠标移动事件处理器列表
     */
    private final List<MouseMotionEventHandler> motionHandlers = new ArrayList<>();

    /**
     * 鼠标滚轮事件处理器列表
     */
    private final List<MouseScrollEventHandler> scrollHandlers = new ArrayList<>();

    /**
     * 注册的鼠标点击监听器
     */
    private final Map<MouseButton, List<EventHandler<MouseClickEvent>>> clickListeners = new ConcurrentHashMap<>();

    private MouseEventManager() {
        // 初始化点击监听器映射
        for (MouseButton button : MouseButton.values()) {
            clickListeners.put(button, new ArrayList<>());
        }
    }

    public static MouseEventManager get() {
        return INSTANCE;
    }

    // ==================== 鼠标点击事件 ====================

    /**
     * 注册鼠标点击事件处理器
     *
     * @param handler 鼠标点击事件处理器
     */
    public void addClickHandler(MouseClickEventHandler handler) {
        clickHandlers.add(handler);
    }

    /**
     * 移除鼠标点击事件处理器
     *
     * @param handler 鼠标点击事件处理器
     */
    public void removeClickHandler(MouseClickEventHandler handler) {
        clickHandlers.remove(handler);
    }

    /**
     * 注册特定鼠标按钮的点击监听器
     *
     * @param button  鼠标按钮
     * @param listener 监听器
     */
    public void addButtonListener(MouseButton button, EventHandler<MouseClickEvent> listener) {
        List<EventHandler<MouseClickEvent>> listeners = clickListeners.get(button);
        if (listeners != null) {
            listeners.add(listener);
        }
    }

    /**
     * 移除特定鼠标按钮的点击监听器
     *
     * @param button   鼠标按钮
     * @param listener 监听器
     */
    public void removeButtonListener(MouseButton button, EventHandler<MouseClickEvent> listener) {
        List<EventHandler<MouseClickEvent>> listeners = clickListeners.get(button);
        if (listeners != null) {
            listeners.remove(listener);
        }
    }

    /**
     * 触发鼠标点击事件
     *
     * @param button     鼠标按钮
     * @param x          X 坐标
     * @param y          Y 坐标
     * @param clickCount 点击次数
     */
    public void dispatchMouseClick(int button, float x, float y, int clickCount) {
        MouseClickEvent event = MouseClickEvent.create(this, button, x, y, clickCount);

        // 通过 EventBus 分发事件
        EventBus.get().post(event);

        // 调用注册的处理器
        for (MouseClickEventHandler handler : clickHandlers) {
            if (!event.isCancelled()) {
                handler.handle(event);
            }
        }

        // 调用特定按钮的监听器
        MouseButton mb = MouseButton.fromCode(button);
        if (mb != null) {
            List<EventHandler<MouseClickEvent>> listeners = clickListeners.get(mb);
            if (listeners != null) {
                for (EventHandler<MouseClickEvent> listener : listeners) {
                    if (!event.isCancelled()) {
                        listener.handle(event);
                    }
                }
            }
        }
    }

    // ==================== 鼠标移动事件 ====================

    /**
     * 注册鼠标移动事件处理器
     *
     * @param handler 鼠标移动事件处理器
     */
    public void addMotionHandler(MouseMotionEventHandler handler) {
        motionHandlers.add(handler);
    }

    /**
     * 移除鼠标移动事件处理器
     *
     * @param handler 鼠标移动事件处理器
     */
    public void removeMotionHandler(MouseMotionEventHandler handler) {
        motionHandlers.remove(handler);
    }

    /**
     * 触发鼠标移动事件
     *
     * @param x X 坐标
     * @param y Y 坐标
     * @param deltaX X 方向移动量
     * @param deltaY Y 方向移动量
     */
    public void dispatchMouseMotion(float x, float y, float deltaX, float deltaY) {
        MouseMotionEvent event = MouseMotionEvent.create(this, x, y, deltaX, deltaY);

        for (MouseMotionEventHandler handler : motionHandlers) {
            handler.handle(event);
        }
    }

    // ==================== 鼠标滚轮事件 ====================

    /**
     * 注册鼠标滚轮事件处理器
     *
     * @param handler 鼠标滚轮事件处理器
     */
    public void addScrollHandler(MouseScrollEventHandler handler) {
        scrollHandlers.add(handler);
    }

    /**
     * 移除鼠标滚轮事件处理器
     *
     * @param handler 鼠标滚轮事件处理器
     */
    public void removeScrollHandler(MouseScrollEventHandler handler) {
        scrollHandlers.remove(handler);
    }

    /**
     * 触发鼠标滚轮事件
     *
     * @param amount 滚动量
     * @param x      X 坐标
     * @param y      Y 坐标
     */
    public void dispatchMouseScroll(float amount, float x, float y) {
        MouseScrollEvent event = MouseScrollEvent.create(this, amount, x, y);

        for (MouseScrollEventHandler handler : scrollHandlers) {
            handler.handle(event);
        }
    }

    /**
     * 清除所有事件处理器和监听器
     */
    public void clear() {
        clickHandlers.clear();
        motionHandlers.clear();
        scrollHandlers.clear();
        for (List<EventHandler<MouseClickEvent>> listeners : clickListeners.values()) {
            listeners.clear();
        }
    }
}
