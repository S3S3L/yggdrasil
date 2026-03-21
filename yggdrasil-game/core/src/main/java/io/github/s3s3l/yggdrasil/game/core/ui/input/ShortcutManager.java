package io.github.s3s3l.yggdrasil.game.core.ui.input;

import java.util.*;

/**
 * 快捷键管理器
 * 管理全局快捷键注册和触发
 */
public class ShortcutManager {

    private final Map<KeyCombination, Shortcut> shortcuts;
    private final Map<String, Shortcut> namedShortcuts;
    private KeyboardState keyboardState;

    /**
     * 创建快捷键管理器
     */
    public ShortcutManager() {
        this.shortcuts = new HashMap<>();
        this.namedShortcuts = new HashMap<>();
    }

    /**
     * 设置键盘状态追踪器
     *
     * @param keyboardState 键盘状态
     */
    public void setKeyboardState(KeyboardState keyboardState) {
        this.keyboardState = keyboardState;
    }

    /**
     * 注册快捷键
     *
     * @param name 快捷键名称
     * @param combination 组合键
     * @param action 触发动作
     */
    public void register(String name, KeyCombination combination, Runnable action) {
        Shortcut shortcut = new Shortcut(name, combination, action);
        shortcuts.put(combination, shortcut);
        namedShortcuts.put(name, shortcut);
    }

    /**
     * 注册快捷键（简化版）
     *
     * @param name 快捷键名称
     * @param mainKey 主键代码
     * @param action 触发动作
     */
    public void register(String name, int mainKey, Runnable action) {
        register(name, new KeyCombination(mainKey), action);
    }

    /**
     * 注册带修饰键的快捷键
     *
     * @param name 快捷键名称
     * @param mainKey 主键代码
     * @param modifiers 修饰键
     * @param action 触发动作
     */
    public void register(String name, int mainKey, Set<KeyCombination.Modifier> modifiers, Runnable action) {
        register(name, new KeyCombination(mainKey, modifiers), action);
    }

    /**
     * 注销快捷键
     *
     * @param name 快捷键名称
     */
    public void unregister(String name) {
        Shortcut shortcut = namedShortcuts.remove(name);
        if (shortcut != null) {
            shortcuts.remove(shortcut.combination);
        }
    }

    /**
     * 注销快捷键
     *
     * @param combination 组合键
     */
    public void unregister(KeyCombination combination) {
        Shortcut shortcut = shortcuts.remove(combination);
        if (shortcut != null) {
            namedShortcuts.remove(shortcut.name);
        }
    }

    /**
     * 获取快捷键
     *
     * @param name 快捷键名称
     * @return 快捷键，不存在返回 null
     */
    public Shortcut getShortcut(String name) {
        return namedShortcuts.get(name);
    }

    /**
     * 获取所有注册的快捷键
     *
     * @return 快捷键列表
     */
    public List<Shortcut> getAllShortcuts() {
        return new ArrayList<>(namedShortcuts.values());
    }

    /**
     * 检查组合键是否已注册
     *
     * @param combination 组合键
     * @return true 如果已注册
     */
    public boolean isRegistered(KeyCombination combination) {
        return shortcuts.containsKey(combination);
    }

    /**
     * 更新快捷键状态（每帧调用）
     *
     * @param pressedModifiers 当前按下的修饰键
     * @param timestamp 时间戳
     */
    public void update(Set<KeyCombination.Modifier> pressedModifiers, long timestamp) {
        if (keyboardState == null) {
            return;
        }

        for (Shortcut shortcut : shortcuts.values()) {
            KeyCombination combo = shortcut.combination;

            // 检查主键是否刚按下
            if (keyboardState.isKeyPressed(combo.getMainKey())) {
                // 检查修饰键是否匹配
                if (combo.getModifiers().equals(pressedModifiers)) {
                    // 触发快捷键
                    if (shortcut.action != null) {
                        shortcut.action.run();
                    }
                }
            }
        }
    }

    /**
     * 快捷键类
     */
    public static class Shortcut {
        public final String name;
        public final KeyCombination combination;
        public final Runnable action;

        public Shortcut(String name, KeyCombination combination, Runnable action) {
            this.name = name;
            this.combination = combination;
            this.action = action;
        }

        /**
         * 获取快捷键名称
         *
         * @return 名称
         */
        public String getName() {
            return name;
        }

        /**
         * 获取组合键
         *
         * @return 组合键
         */
        public KeyCombination getCombination() {
            return combination;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Shortcut shortcut = (Shortcut) o;
            return Objects.equals(name, shortcut.name) &&
                   Objects.equals(combination, shortcut.combination);
        }

        @Override
        public int hashCode() {
            return Objects.hash(name, combination);
        }

        @Override
        public String toString() {
            return String.format("Shortcut{name='%s', combination=%s}", name, combination);
        }
    }
}
