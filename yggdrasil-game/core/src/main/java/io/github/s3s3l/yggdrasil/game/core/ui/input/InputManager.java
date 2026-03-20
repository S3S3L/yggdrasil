package io.github.s3s3l.yggdrasil.game.core.ui.input;

import java.util.*;

/**
 * 输入管理器
 * 管理输入映射和动作状态
 */
public class InputManager {

    private static InputManager instance;

    private final Map<String, InputMapping> mappings;
    private final Map<String, Boolean> actionStates;
    private final Map<Integer, String> keyToActionMap;
    private final Map<Gesture, String> gestureToActionMap;
    private final List<InputListener> listeners;

    /**
     * 私有构造函数
     */
    private InputManager() {
        this.mappings = new HashMap<>();
        this.actionStates = new HashMap<>();
        this.keyToActionMap = new HashMap<>();
        this.gestureToActionMap = new HashMap<>();
        this.listeners = new ArrayList<>();
    }

    /**
     * 获取单例实例
     *
     * @return InputManager 实例
     */
    public static synchronized InputManager getInstance() {
        if (instance == null) {
            instance = new InputManager();
        }
        return instance;
    }

    /**
     * 重置单例（用于测试）
     */
    public static synchronized void resetInstance() {
        instance = null;
    }

    /**
     * 绑定动作到按键
     *
     * @param actionName 动作名称
     * @param keyCode    按键代码
     */
    public void mapAction(String actionName, int keyCode) {
        InputMapping mapping = mappings.computeIfAbsent(actionName, InputMapping::new);
        mapping.addKeyBinding(keyCode);
        keyToActionMap.put(keyCode, actionName);
        actionStates.put(actionName, false);
    }

    /**
     * 绑定动作到手势
     *
     * @param actionName 动作名称
     * @param gesture    手势类型
     */
    public void mapAction(String actionName, Gesture gesture) {
        InputMapping mapping = mappings.computeIfAbsent(actionName, InputMapping::new);
        mapping.addGestureBinding(gesture);
        gestureToActionMap.put(gesture, actionName);
        actionStates.put(actionName, false);
    }

    /**
     * 解绑动作
     *
     * @param actionName 动作名称
     */
    public void unmapAction(String actionName) {
        InputMapping mapping = mappings.remove(actionName);
        if (mapping != null) {
            for (InputMapping.KeyBinding binding : mapping.getKeyBindings()) {
                keyToActionMap.remove(binding.keyCode);
            }
            for (InputMapping.GestureBinding binding : mapping.getGestureBindings()) {
                gestureToActionMap.remove(binding.gesture);
            }
        }
        actionStates.remove(actionName);
    }

    /**
     * 检查动作是否被按下
     *
     * @param actionName 动作名称
     * @return true 如果动作被按下
     */
    public boolean isActionPressed(String actionName) {
        Boolean state = actionStates.get(actionName);
        return state != null && state;
    }

    /**
     * 设置动作状态
     *
     * @param actionName 动作名称
     * @param pressed    是否按下
     */
    public void setActionState(String actionName, boolean pressed) {
        Boolean oldState = actionStates.get(actionName);
        if (oldState == null || oldState != pressed) {
            actionStates.put(actionName, pressed);
            if (pressed) {
                fireActionPressed(actionName);
            }
        }
    }

    /**
     * 根据按键代码获取动作名称
     *
     * @param keyCode 按键代码
     * @return 动作名称，如果没有绑定则返回 null
     */
    public String getActionByKey(int keyCode) {
        return keyToActionMap.get(keyCode);
    }

    /**
     * 根据手势获取动作名称
     *
     * @param gesture 手势类型
     * @return 动作名称，如果没有绑定则返回 null
     */
    public String getActionByGesture(Gesture gesture) {
        return gestureToActionMap.get(gesture);
    }

    /**
     * 获取输入映射
     *
     * @param actionName 动作名称
     * @return 输入映射
     */
    public InputMapping getMapping(String actionName) {
        return mappings.get(actionName);
    }

    /**
     * 获取所有动作名称
     *
     * @return 动作名称集合
     */
    public Set<String> getActions() {
        return mappings.keySet();
    }

    /**
     * 添加输入监听器
     *
     * @param listener 监听器
     */
    public void addListener(InputListener listener) {
        if (listener != null && !listeners.contains(listener)) {
            listeners.add(listener);
        }
    }

    /**
     * 移除输入监听器
     *
     * @param listener 监听器
     */
    public void removeListener(InputListener listener) {
        listeners.remove(listener);
    }

    /**
     * 处理按键按下
     *
     * @param keyCode 按键代码
     */
    public void keyDown(int keyCode) {
        String action = getActionByKey(keyCode);
        if (action != null) {
            setActionState(action, true);
        }
    }

    /**
     * 处理按键释放
     *
     * @param keyCode 按键代码
     */
    public void keyUp(int keyCode) {
        String action = getActionByKey(keyCode);
        if (action != null) {
            setActionState(action, false);
        }
    }

    /**
     * 处理手势
     *
     * @param gesture 手势类型
     */
    public void handleGesture(Gesture gesture) {
        String action = getActionByGesture(gesture);
        if (action != null) {
            setActionState(action, true);
            // 手势触发后自动重置状态
            actionStates.put(action, false);
        }
    }

    /**
     * 清空所有映射
     */
    public void clearAll() {
        mappings.clear();
        keyToActionMap.clear();
        gestureToActionMap.clear();
        actionStates.clear();
    }

    /**
     * 通知动作按下
     */
    private void fireActionPressed(String actionName) {
        for (InputListener listener : listeners) {
            try {
                listener.onActionPressed(actionName);
            } catch (Exception e) {
                // 忽略监听器异常
            }
        }
    }

    /**
     * 输入监听器接口
     */
    public interface InputListener {
        /**
         * 动作按下时调用
         *
         * @param actionName 动作名称
         */
        void onActionPressed(String actionName);
    }
}
