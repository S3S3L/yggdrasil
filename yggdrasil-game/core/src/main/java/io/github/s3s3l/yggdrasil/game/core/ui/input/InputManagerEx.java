package io.github.s3s3l.yggdrasil.game.core.ui.input;

import java.util.*;

/**
 * 增强的输入管理器（Iteration 30）
 * 在原有 InputManager 基础上添加：
 * - 键盘状态追踪（长按、连发）
 * - 组合键支持
 * - 全局快捷键管理
 */
public class InputManagerEx {

    private static InputManagerEx instance;

    private final InputManager legacyManager;  // 兼容旧版本
    private final KeyboardState keyboardState;
    private final ShortcutManager shortcutManager;
    private final Map<Integer, Set<KeyCombination.Modifier>> keyModifiers;  // 每个按键的修饰键状态

    /**
     * 私有构造函数
     */
    private InputManagerEx() {
        this.legacyManager = InputManager.getInstance();
        this.keyboardState = new KeyboardState();
        this.shortcutManager = new ShortcutManager();
        this.shortcutManager.setKeyboardState(keyboardState);
        this.keyModifiers = new HashMap<>();
    }

    /**
     * 获取单例实例
     *
     * @return InputManagerEx 实例
     */
    public static synchronized InputManagerEx getInstance() {
        if (instance == null) {
            instance = new InputManagerEx();
        }
        return instance;
    }

    /**
     * 重置单例（用于测试）
     */
    public static synchronized void resetInstance() {
        instance = null;
        InputManager.resetInstance();
    }

    /**
     * 获取键盘状态追踪器
     *
     * @return 键盘状态
     */
    public KeyboardState getKeyboardState() {
        return keyboardState;
    }

    /**
     * 获取快捷键管理器
     *
     * @return 快捷键管理器
     */
    public ShortcutManager getShortcutManager() {
        return shortcutManager;
    }

    /**
     * 获取旧版本 InputManager（用于兼容）
     *
     * @return InputManager 实例
     */
    public InputManager getLegacyManager() {
        return legacyManager;
    }

    /**
     * 处理按键按下
     *
     * @param keyCode 按键代码
     * @param timestamp 时间戳
     */
    public void keyDown(int keyCode, long timestamp) {
        // 更新键盘状态
        keyboardState.keyDown(keyCode, timestamp);

        // 更新修饰键状态
        updateModifiers(keyCode, true);

        // 通知旧版本
        legacyManager.keyDown(keyCode);
    }

    /**
     * 处理按键释放
     *
     * @param keyCode 按键代码
     */
    public void keyUp(int keyCode) {
        // 更新键盘状态
        keyboardState.keyUp(keyCode);

        // 更新修饰键状态
        updateModifiers(keyCode, false);

        // 通知旧版本
        legacyManager.keyUp(keyCode);
    }

    /**
     * 更新修饰键状态
     *
     * @param keyCode 按键代码
     * @param pressed 是否按下
     */
    private void updateModifiers(int keyCode, boolean pressed) {
        KeyCombination.Modifier modifier = getModifierForKey(keyCode);
        if (modifier != null) {
            Set<KeyCombination.Modifier> modifiers = keyModifiers.computeIfAbsent(
                    keyCode, k -> EnumSet.noneOf(KeyCombination.Modifier.class));
            if (pressed) {
                modifiers.add(modifier);
            } else {
                modifiers.remove(modifier);
            }
        }
    }

    /**
     * 根据按键代码获取修饰键类型
     *
     * @param keyCode 按键代码
     * @return 修饰键类型，如果不是修饰键返回 null
     */
    private KeyCombination.Modifier getModifierForKey(int keyCode) {
        // 这些 keycode 值需要根据实际使用的输入系统调整
        // 这里使用常见的 LibGDX keycode 值
        if (keyCode == 131 || keyCode == 29) return KeyCombination.Modifier.CTRL;  // CTRL_LEFT, CTRL_RIGHT
        if (keyCode == 134 || keyCode == 57) return KeyCombination.Modifier.ALT;   // ALT_LEFT, ALT_RIGHT
        if (keyCode == 133 || keyCode == 59) return KeyCombination.Modifier.SHIFT; // SHIFT_LEFT, SHIFT_RIGHT
        return null;
    }

    /**
     * 获取当前按下的修饰键集合
     *
     * @return 修饰键集合
     */
    public Set<KeyCombination.Modifier> getPressedModifiers() {
        Set<KeyCombination.Modifier> allModifiers = EnumSet.noneOf(KeyCombination.Modifier.class);
        for (Set<KeyCombination.Modifier> modifiers : keyModifiers.values()) {
            allModifiers.addAll(modifiers);
        }
        return allModifiers;
    }

    /**
     * 检查按键是否刚被按下
     *
     * @param keyCode 按键代码
     * @return true 如果刚被按下
     */
    public boolean isKeyPressed(int keyCode) {
        return keyboardState.isKeyPressed(keyCode);
    }

    /**
     * 检查按键是否持续按下
     *
     * @param keyCode 按键代码
     * @return true 如果持续按下
     */
    public boolean isKeyHeld(int keyCode) {
        return keyboardState.isKeyHeld(keyCode);
    }

    /**
     * 检查按键是否刚被释放
     *
     * @param keyCode 按键代码
     * @return true 如果刚被释放
     */
    public boolean isKeyReleased(int keyCode) {
        return keyboardState.isKeyReleased(keyCode);
    }

    /**
     * 检查按键是否未按下
     *
     * @param keyCode 按键代码
     * @return true 如果未按下
     */
    public boolean isKeyUp(int keyCode) {
        return keyboardState.isKeyUp(keyCode);
    }

    /**
     * 获取按键按下的持续时间
     *
     * @param keyCode 按键代码
     * @return 持续时间（毫秒）
     */
    public int getHoldDuration(int keyCode) {
        return keyboardState.getHoldDuration(keyCode, System.currentTimeMillis());
    }

    /**
     * 更新输入状态（每帧调用）
     */
    public void update() {
        long timestamp = System.currentTimeMillis();
        keyboardState.update(timestamp);
        shortcutManager.update(getPressedModifiers(), timestamp);
    }

    /**
     * 注册快捷键
     *
     * @param name 快捷键名称
     * @param mainKey 主键代码
     * @param action 触发动作
     */
    public void registerShortcut(String name, int mainKey, Runnable action) {
        shortcutManager.register(name, mainKey, action);
    }

    /**
     * 注册带修饰键的快捷键
     *
     * @param name 快捷键名称
     * @param mainKey 主键代码
     * @param modifiers 修饰键集合
     * @param action 触发动作
     */
    public void registerShortcut(String name, int mainKey, Set<KeyCombination.Modifier> modifiers, Runnable action) {
        shortcutManager.register(name, mainKey, modifiers, action);
    }

    /**
     * 注销快捷键
     *
     * @param name 快捷键名称
     */
    public void unregisterShortcut(String name) {
        shortcutManager.unregister(name);
    }

    /**
     * 绑定动作到按键（兼容旧 API）
     *
     * @param actionName 动作名称
     * @param keyCode 按键代码
     */
    public void mapAction(String actionName, int keyCode) {
        legacyManager.mapAction(actionName, keyCode);
    }

    /**
     * 检查动作是否被按下（兼容旧 API）
     *
     * @param actionName 动作名称
     * @return true 如果动作被按下
     */
    public boolean isActionPressed(String actionName) {
        return legacyManager.isActionPressed(actionName);
    }

    /**
     * 清空所有状态
     */
    public void clear() {
        keyboardState.reset();
        keyModifiers.clear();
        legacyManager.clearAll();
    }
}
