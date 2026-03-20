package io.github.s3s3l.yggdrasil.game.core.ui.input;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 输入映射
 * 将按键或手势绑定到动作名称
 */
public class InputMapping {

    private final String actionName;
    private final List<KeyBinding> keyBindings;
    private final List<GestureBinding> gestureBindings;

    /**
     * 创建输入映射
     *
     * @param actionName 动作名称
     */
    public InputMapping(String actionName) {
        this.actionName = actionName;
        this.keyBindings = new ArrayList<>();
        this.gestureBindings = new ArrayList<>();
    }

    /**
     * 获取动作名称
     *
     * @return 动作名称
     */
    public String getActionName() {
        return actionName;
    }

    /**
     * 添加按键绑定
     *
     * @param keyCode 按键代码
     */
    public void addKeyBinding(int keyCode) {
        keyBindings.add(new KeyBinding(keyCode));
    }

    /**
     * 添加手势绑定
     *
     * @param gesture 手势类型
     */
    public void addGestureBinding(Gesture gesture) {
        gestureBindings.add(new GestureBinding(gesture));
    }

    /**
     * 移除按键绑定
     *
     * @param keyCode 按键代码
     */
    public void removeKeyBinding(int keyCode) {
        keyBindings.removeIf(b -> b.keyCode == keyCode);
    }

    /**
     * 移除手势绑定
     *
     * @param gesture 手势类型
     */
    public void removeGestureBinding(Gesture gesture) {
        gestureBindings.removeIf(b -> b.gesture == gesture);
    }

    /**
     * 检查是否包含按键绑定
     *
     * @param keyCode 按键代码
     * @return true 如果包含
     */
    public boolean hasKeyBinding(int keyCode) {
        for (KeyBinding binding : keyBindings) {
            if (binding.keyCode == keyCode) {
                return true;
            }
        }
        return false;
    }

    /**
     * 检查是否包含手势绑定
     *
     * @param gesture 手势类型
     * @return true 如果包含
     */
    public boolean hasGestureBinding(Gesture gesture) {
        for (GestureBinding binding : gestureBindings) {
            if (binding.gesture == gesture) {
                return true;
            }
        }
        return false;
    }

    /**
     * 获取所有按键绑定
     *
     * @return 按键绑定列表
     */
    public List<KeyBinding> getKeyBindings() {
        return new ArrayList<>(keyBindings);
    }

    /**
     * 获取所有手势绑定
     *
     * @return 手势绑定列表
     */
    public List<GestureBinding> getGestureBindings() {
        return new ArrayList<>(gestureBindings);
    }

    /**
     * 清空所有绑定
     */
    public void clear() {
        keyBindings.clear();
        gestureBindings.clear();
    }

    /**
     * 按键绑定
     */
    public static class KeyBinding {
        public final int keyCode;

        public KeyBinding(int keyCode) {
            this.keyCode = keyCode;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            KeyBinding that = (KeyBinding) o;
            return keyCode == that.keyCode;
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(keyCode);
        }
    }

    /**
     * 手势绑定
     */
    public static class GestureBinding {
        public final Gesture gesture;

        public GestureBinding(Gesture gesture) {
            this.gesture = gesture;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            GestureBinding that = (GestureBinding) o;
            return gesture == that.gesture;
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(gesture);
        }
    }
}
