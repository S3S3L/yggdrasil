package io.github.s3s3l.yggdrasil.game.core.ui.theme;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * UI 样式管理器
 * 管理主题的注册、应用和切换
 */
public class UIStyleManager {

    private static UIStyleManager instance;

    private final Map<String, UITheme> themes;
    private UITheme currentTheme;
    private final List<ThemeChangeListener> themeChangeListeners;

    /**
     * 私有构造函数
     */
    private UIStyleManager() {
        this.themes = new HashMap<>();
        this.themeChangeListeners = new ArrayList<>();
    }

    /**
     * 获取单例实例
     *
     * @return UIStyleManager 实例
     */
    public static synchronized UIStyleManager getInstance() {
        if (instance == null) {
            instance = new UIStyleManager();
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
     * 注册主题
     *
     * @param theme 主题
     */
    public void registerTheme(UITheme theme) {
        if (theme != null) {
            themes.put(theme.getName(), theme);
        }
    }

    /**
     * 注销主题
     *
     * @param themeName 主题名称
     */
    public void unregisterTheme(String themeName) {
        themes.remove(themeName);
    }

    /**
     * 获取主题
     *
     * @param themeName 主题名称
     * @return 主题，如果没有则返回 null
     */
    public UITheme getTheme(String themeName) {
        return themes.get(themeName);
    }

    /**
     * 获取所有已注册的主题
     *
     * @return 主题映射
     */
    public Map<String, UITheme> getThemes() {
        return Collections.unmodifiableMap(themes);
    }

    /**
     * 应用主题
     *
     * @param themeName 主题名称
     */
    public void applyTheme(String themeName) {
        UITheme theme = themes.get(themeName);
        if (theme != null) {
            UITheme oldTheme = currentTheme;
            currentTheme = theme;
            notifyThemeChanged(oldTheme, theme);
        }
    }

    /**
     * 应用主题
     *
     * @param theme 主题
     */
    public void applyTheme(UITheme theme) {
        if (theme != null) {
            if (!themes.containsKey(theme.getName())) {
                registerTheme(theme);
            }
            UITheme oldTheme = currentTheme;
            currentTheme = theme;
            notifyThemeChanged(oldTheme, theme);
        }
    }

    /**
     * 获取当前主题
     *
     * @return 当前主题，如果没有则返回 null
     */
    public UITheme getCurrentTheme() {
        return currentTheme;
    }

    /**
     * 获取当前主题名称
     *
     * @return 当前主题名称，如果没有则返回 null
     */
    public String getCurrentThemeName() {
        return currentTheme != null ? currentTheme.getName() : null;
    }

    /**
     * 添加主题变化监听器
     *
     * @param listener 监听器
     */
    public void addThemeChangeListener(ThemeChangeListener listener) {
        if (listener != null && !themeChangeListeners.contains(listener)) {
            themeChangeListeners.add(listener);
        }
    }

    /**
     * 移除主题变化监听器
     *
     * @param listener 监听器
     */
    public void removeThemeChangeListener(ThemeChangeListener listener) {
        themeChangeListeners.remove(listener);
    }

    /**
     * 通知主题变化
     *
     * @param oldTheme 旧主题
     * @param newTheme 新主题
     */
    private void notifyThemeChanged(UITheme oldTheme, UITheme newTheme) {
        for (ThemeChangeListener listener : themeChangeListeners) {
            try {
                listener.onThemeChanged(oldTheme, newTheme);
            } catch (Exception e) {
                // 忽略监听器异常
            }
        }
    }

    /**
     * 检查是否有主题
     *
     * @param themeName 主题名称
     * @return true 如果有该主题
     */
    public boolean hasTheme(String themeName) {
        return themes.containsKey(themeName);
    }

    /**
     * 清除所有主题
     */
    public void clearThemes() {
        themes.clear();
        currentTheme = null;
    }

    /**
     * 主题变化监听器接口
     */
    public interface ThemeChangeListener {
        /**
         * 主题变化时调用
         *
         * @param oldTheme 旧主题
         * @param newTheme 新主题
         */
        void onThemeChanged(UITheme oldTheme, UITheme newTheme);
    }
}
