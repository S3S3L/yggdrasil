package io.github.s3s3l.yggdrasil.game.core.ui.theme;

import io.github.s3s3l.yggdrasil.game.core.ui.UIColor;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * UI 主题接口
 * 定义主题的契约，包含组件样式和资源
 */
public class UITheme {

    private final String name;
    private final Map<String, UIStyle> componentStyles;
    private final Map<String, UIColor> colors;
    private final Map<String, String> fonts;
    private UITheme parent;

    /**
     * 创建主题
     *
     * @param name 主题名称
     */
    public UITheme(String name) {
        this.name = name;
        this.componentStyles = new HashMap<>();
        this.colors = new HashMap<>();
        this.fonts = new HashMap<>();
    }

    /**
     * 创建主题
     *
     * @param name   主题名称
     * @param parent 父主题（用于继承）
     */
    public UITheme(String name, UITheme parent) {
        this(name);
        this.parent = parent;
    }

    /**
     * 获取主题名称
     *
     * @return 主题名称
     */
    public String getName() {
        return name;
    }

    /**
     * 获取父主题
     *
     * @return 父主题，如果没有则返回 null
     */
    public UITheme getParent() {
        return parent;
    }

    /**
     * 设置父主题
     *
     * @param parent 父主题
     */
    public void setParent(UITheme parent) {
        this.parent = parent;
    }

    // ==================== 组件样式相关方法 ====================

    /**
     * 注册组件样式
     *
     * @param componentName 组件类型名称（如 "Button", "Label"）
     * @param style         样式
     */
    public void setStyle(String componentName, UIStyle style) {
        if (componentName != null && style != null) {
            componentStyles.put(componentName, style);
        }
    }

    /**
     * 获取组件样式
     *
     * @param componentName 组件类型名称
     * @return 样式，如果没有则返回 null
     */
    public UIStyle getStyle(String componentName) {
        UIStyle style = componentStyles.get(componentName);
        if (style == null && parent != null) {
            style = parent.getStyle(componentName);
        }
        return style != null ? style.copy() : null;
    }

    /**
     * 检查是否定义了组件样式
     *
     * @param componentName 组件类型名称
     * @return true 如果定义了样式
     */
    public boolean hasStyle(String componentName) {
        return componentStyles.containsKey(componentName) ||
                (parent != null && parent.hasStyle(componentName));
    }

    /**
     * 移除组件样式
     *
     * @param componentName 组件类型名称
     */
    public void removeStyle(String componentName) {
        componentStyles.remove(componentName);
    }

    /**
     * 获取所有已定义的组件样式名称
     *
     * @return 组件样式名称集合
     */
    public Map<String, UIStyle> getStyles() {
        return new HashMap<>(componentStyles);
    }

    // ==================== 颜色资源相关方法 ====================

    /**
     * 注册颜色资源
     *
     * @param colorName 颜色名称
     * @param color     颜色值
     */
    public void setColor(String colorName, UIColor color) {
        if (colorName != null && color != null) {
            colors.put(colorName, color);
        }
    }

    /**
     * 获取颜色资源
     *
     * @param colorName 颜色名称
     * @return 颜色值，如果没有则返回 null
     */
    public UIColor getColor(String colorName) {
        UIColor color = colors.get(colorName);
        if (color == null && parent != null) {
            color = parent.getColor(colorName);
        }
        return color;
    }

    /**
     * 检查是否定义了颜色资源
     *
     * @param colorName 颜色名称
     * @return true 如果定义了颜色
     */
    public boolean hasColor(String colorName) {
        return colors.containsKey(colorName) ||
                (parent != null && parent.hasColor(colorName));
    }

    /**
     * 移除颜色资源
     *
     * @param colorName 颜色名称
     */
    public void removeColor(String colorName) {
        colors.remove(colorName);
    }

    /**
     * 获取所有已定义的颜色
     *
     * @return 颜色映射
     */
    public Map<String, UIColor> getColors() {
        return new HashMap<>(colors);
    }

    // ==================== 字体资源相关方法 ====================

    /**
     * 注册字体资源
     *
     * @param fontName 字体名称
     * @param fontPath 字体文件路径
     */
    public void setFont(String fontName, String fontPath) {
        if (fontName != null) {
            fonts.put(fontName, fontPath);
        }
    }

    /**
     * 获取字体文件路径
     *
     * @param fontName 字体名称
     * @return 字体文件路径，如果没有则返回 null
     */
    public String getFont(String fontName) {
        String fontPath = fonts.get(fontName);
        if (fontPath == null && parent != null) {
            fontPath = parent.getFont(fontName);
        }
        return fontPath;
    }

    /**
     * 检查是否定义了字体资源
     *
     * @param fontName 字体名称
     * @return true 如果定义了字体
     */
    public boolean hasFont(String fontName) {
        return fonts.containsKey(fontName) ||
                (parent != null && parent.hasFont(fontName));
    }

    /**
     * 移除字体资源
     *
     * @param fontName 字体名称
     */
    public void removeFont(String fontName) {
        fonts.remove(fontName);
    }

    /**
     * 获取所有已定义的字体
     *
     * @return 字体映射
     */
    public Map<String, String> getFonts() {
        return new HashMap<>(fonts);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UITheme uiTheme = (UITheme) o;
        return Objects.equals(name, uiTheme.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return "UITheme{" +
                "name='" + name + '\'' +
                ", parent=" + (parent != null ? parent.name : "null") +
                ", styles=" + componentStyles.size() +
                ", colors=" + colors.size() +
                ", fonts=" + fonts.size() +
                '}';
    }
}
