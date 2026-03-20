package io.github.s3s3l.yggdrasil.game.core.ui.theme;

import io.github.s3s3l.yggdrasil.game.core.ui.UIColor;
import io.github.s3s3l.yggdrasil.game.core.ui.UIRect;

import java.util.Objects;

/**
 * UI 样式类
 * 定义 UI 组件的视觉样式属性
 */
public class UIStyle {

    /**
     * 默认样式（单例）
     */
    public static final UIStyle DEFAULT = new UIStyle();

    // 背景相关
    private UIColor backgroundColor;
    private boolean backgroundVisible;

    // 字体相关
    private Object font; // 使用 Object 避免依赖具体字体实现
    private String fontName;
    private float fontSize;
    private UIColor fontColor;

    // 边框相关
    private BorderStyle border;

    // 布局相关
    private UIRect padding;
    private UIRect margin;

    // 其他
    private float opacity;

    /**
     * 创建默认样式
     */
    public UIStyle() {
        this.backgroundColor = UIColor.TRANSPARENT;
        this.backgroundVisible = true;
        this.fontName = "default";
        this.fontSize = 16f;
        this.fontColor = UIColor.WHITE;
        this.border = BorderStyle.NONE;
        this.padding = new UIRect();
        this.margin = new UIRect();
        this.opacity = 1f;
    }

    /**
     * 复制样式
     *
     * @param other 要复制的样式
     */
    public UIStyle(UIStyle other) {
        if (other != null) {
            this.backgroundColor = other.backgroundColor;
            this.backgroundVisible = other.backgroundVisible;
            this.fontName = other.fontName;
            this.fontSize = other.fontSize;
            this.fontColor = other.fontColor;
            this.border = other.border != null ? other.border.copy() : BorderStyle.NONE;
            this.padding = new UIRect(other.padding);
            this.margin = new UIRect(other.margin);
            this.opacity = other.opacity;
        } else {
            this.backgroundColor = UIColor.TRANSPARENT;
            this.backgroundVisible = true;
            this.fontName = "default";
            this.fontSize = 16f;
            this.fontColor = UIColor.WHITE;
            this.border = BorderStyle.NONE;
            this.padding = new UIRect();
            this.margin = new UIRect();
            this.opacity = 1f;
        }
    }

    // ==================== 背景相关方法 ====================

    public UIColor getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(UIColor backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public boolean isBackgroundVisible() {
        return backgroundVisible;
    }

    public void setBackgroundVisible(boolean backgroundVisible) {
        this.backgroundVisible = backgroundVisible;
    }

    // ==================== 字体相关方法 ====================

    public Object getFont() {
        return font;
    }

    public void setFont(Object font) {
        this.font = font;
    }

    public String getFontName() {
        return fontName;
    }

    public void setFontName(String fontName) {
        this.fontName = fontName;
    }

    public float getFontSize() {
        return fontSize;
    }

    public void setFontSize(float fontSize) {
        this.fontSize = fontSize;
    }

    public UIColor getFontColor() {
        return fontColor;
    }

    public void setFontColor(UIColor fontColor) {
        this.fontColor = fontColor;
    }

    // ==================== 边框相关方法 ====================

    public BorderStyle getBorder() {
        return border;
    }

    public void setBorder(BorderStyle border) {
        this.border = border != null ? border : BorderStyle.NONE;
    }

    // ==================== 布局相关方法 ====================

    public UIRect getPadding() {
        return padding;
    }

    public void setPadding(UIRect padding) {
        if (padding != null) {
            this.padding.setX(padding.getX());
            this.padding.setY(padding.getY());
            this.padding.setWidth(padding.getWidth());
            this.padding.setHeight(padding.getHeight());
        }
    }

    public UIRect getMargin() {
        return margin;
    }

    public void setMargin(UIRect margin) {
        if (margin != null) {
            this.margin.setX(margin.getX());
            this.margin.setY(margin.getY());
            this.margin.setWidth(margin.getWidth());
            this.margin.setHeight(margin.getHeight());
        }
    }

    // ==================== 其他方法 ====================

    public float getOpacity() {
        return opacity;
    }

    public void setOpacity(float opacity) {
        this.opacity = Math.max(0, Math.min(1, opacity));
    }

    /**
     * 合并另一个样式
     * 非 null 的属性会覆盖当前样式
     *
     * @param other 要合并的样式
     */
    public void merge(UIStyle other) {
        if (other == null) return;

        if (other.backgroundColor != null) {
            this.backgroundColor = other.backgroundColor;
        }
        if (other.fontName != null) {
            this.fontName = other.fontName;
        }
        if (other.fontSize > 0) {
            this.fontSize = other.fontSize;
        }
        if (other.fontColor != null) {
            this.fontColor = other.fontColor;
        }
        if (other.border != null && other.border != BorderStyle.NONE) {
            this.border = other.border.copy();
        }
        if (other.padding != null) {
            setPadding(other.padding);
        }
        if (other.margin != null) {
            setMargin(other.margin);
        }
        if (other.opacity > 0) {
            this.opacity = other.opacity;
        }
    }

    /**
     * 创建副本
     *
     * @return 新的样式副本
     */
    public UIStyle copy() {
        return new UIStyle(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UIStyle uiStyle = (UIStyle) o;
        return Float.compare(uiStyle.fontSize, fontSize) == 0 &&
                Float.compare(uiStyle.opacity, opacity) == 0 &&
                Objects.equals(backgroundColor, uiStyle.backgroundColor) &&
                Objects.equals(fontName, uiStyle.fontName) &&
                Objects.equals(fontColor, uiStyle.fontColor) &&
                Objects.equals(border, uiStyle.border) &&
                Objects.equals(padding, uiStyle.padding) &&
                Objects.equals(margin, uiStyle.margin);
    }

    @Override
    public int hashCode() {
        return Objects.hash(backgroundColor, fontName, fontSize, fontColor, border, padding, margin, opacity);
    }

    @Override
    public String toString() {
        return "UIStyle{" +
                "backgroundColor=" + backgroundColor +
                ", fontName='" + fontName + '\'' +
                ", fontSize=" + fontSize +
                ", fontColor=" + fontColor +
                ", border=" + border +
                ", opacity=" + opacity +
                '}';
    }
}
