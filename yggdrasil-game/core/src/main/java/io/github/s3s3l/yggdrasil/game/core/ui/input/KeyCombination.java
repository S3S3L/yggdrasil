package io.github.s3s3l.yggdrasil.game.core.ui.input;

import java.util.*;

/**
 * 组合键
 * 支持修饰键（Ctrl、Alt、Shift）+ 主键的组合
 */
public class KeyCombination {

    /**
     * 修饰键枚举
     */
    public enum Modifier {
        CTRL,
        ALT,
        SHIFT
    }

    private final int mainKey;
    private final EnumSet<Modifier> modifiers;

    /**
     * 创建组合键
     *
     * @param mainKey 主键代码
     * @param modifiers 修饰键集合
     */
    public KeyCombination(int mainKey, Set<Modifier> modifiers) {
        this.mainKey = mainKey;
        this.modifiers = modifiers != null
                ? EnumSet.copyOf(modifiers)
                : EnumSet.noneOf(Modifier.class);
    }

    /**
     * 创建组合键（无修饰键）
     *
     * @param mainKey 主键代码
     */
    public KeyCombination(int mainKey) {
        this(mainKey, EnumSet.noneOf(Modifier.class));
    }

    /**
     * 创建组合键（单个修饰键）
     *
     * @param mainKey 主键代码
     * @param modifier 修饰键
     */
    public KeyCombination(int mainKey, Modifier modifier) {
        this(mainKey, EnumSet.of(modifier));
    }

    /**
     * 创建组合键（多个修饰键）
     *
     * @param mainKey 主键代码
     * @param modifier1 第一个修饰键
     * @param modifier2 第二个修饰键
     */
    public KeyCombination(int mainKey, Modifier modifier1, Modifier modifier2) {
        this(mainKey, EnumSet.of(modifier1, modifier2));
    }

    /**
     * 获取主键代码
     *
     * @return 主键代码
     */
    public int getMainKey() {
        return mainKey;
    }

    /**
     * 获取修饰键集合
     *
     * @return 修饰键集合
     */
    public Set<Modifier> getModifiers() {
        return Collections.unmodifiableSet(modifiers);
    }

    /**
     * 检查是否包含指定修饰键
     *
     * @param modifier 修饰键
     * @return true 如果包含
     */
    public boolean hasModifier(Modifier modifier) {
        return modifiers.contains(modifier);
    }

    /**
     * 检查是否有修饰键
     *
     * @return true 如果有修饰键
     */
    public boolean hasModifiers() {
        return !modifiers.isEmpty();
    }

    /**
     * 获取修饰键数量
     *
     * @return 修饰键数量
     */
    public int getModifierCount() {
        return modifiers.size();
    }

    /**
     * 检查组合键是否匹配
     *
     * @param mainKey 主键代码
     * @param pressedModifiers 按下的修饰键集合
     * @return true 如果匹配
     */
    public boolean matches(int mainKey, Set<Modifier> pressedModifiers) {
        return this.mainKey == mainKey && modifiers.equals(pressedModifiers);
    }

    /**
     * 检查组合键是否匹配（仅检查主键和已按下的修饰键）
     *
     * @param mainKey 主键代码
     * @param pressedModifiers 按下的修饰键集合
     * @return true 如果匹配
     */
    public boolean matchesExact(int mainKey, Set<Modifier> pressedModifiers) {
        return matches(mainKey, pressedModifiers);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        KeyCombination that = (KeyCombination) o;
        return mainKey == that.mainKey && Objects.equals(modifiers, that.modifiers);
    }

    @Override
    public int hashCode() {
        return Objects.hash(mainKey, modifiers);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (modifiers.contains(Modifier.CTRL)) sb.append("Ctrl+");
        if (modifiers.contains(Modifier.ALT)) sb.append("Alt+");
        if (modifiers.contains(Modifier.SHIFT)) sb.append("Shift+");
        sb.append("Key").append(mainKey);
        return sb.toString();
    }

    /**
     * 从按键代码创建组合键（解析修饰键）
     *
     * @param keyCode 按键代码
     * @param ctrlPressed Ctrl 是否按下
     * @param altPressed Alt 是否按下
     * @param shiftPressed Shift 是否按下
     * @return 组合键
     */
    public static KeyCombination create(int keyCode, boolean ctrlPressed, boolean altPressed, boolean shiftPressed) {
        Set<Modifier> modifiers = EnumSet.noneOf(Modifier.class);
        if (ctrlPressed) modifiers.add(Modifier.CTRL);
        if (altPressed) modifiers.add(Modifier.ALT);
        if (shiftPressed) modifiers.add(Modifier.SHIFT);
        return new KeyCombination(keyCode, modifiers);
    }
}
