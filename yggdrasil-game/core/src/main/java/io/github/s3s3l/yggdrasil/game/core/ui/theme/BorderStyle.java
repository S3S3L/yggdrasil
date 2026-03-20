package io.github.s3s3l.yggdrasil.game.core.ui.theme;

import io.github.s3s3l.yggdrasil.game.core.ui.UIColor;

import java.util.Objects;

/**
 * 边框样式
 * 定义边框的宽度、颜色和圆角半径
 */
public class BorderStyle {

    /**
     * 无边框样式（单例）
     */
    public static final BorderStyle NONE = new BorderStyle(0, UIColor.TRANSPARENT, 0);

    private float width;
    private UIColor color;
    private float radius;

    /**
     * 创建边框样式
     *
     * @param width  边框宽度
     * @param color  边框颜色
     * @param radius 圆角半径
     */
    public BorderStyle(float width, UIColor color, float radius) {
        this.width = width;
        this.color = color;
        this.radius = radius;
    }

    /**
     * 创建边框样式（无圆角）
     *
     * @param width 边框宽度
     * @param color 边框颜色
     */
    public BorderStyle(float width, UIColor color) {
        this(width, color, 0);
    }

    /**
     * 获取边框宽度
     *
     * @return 边框宽度
     */
    public float getWidth() {
        return width;
    }

    /**
     * 设置边框宽度
     *
     * @param width 边框宽度
     */
    public void setWidth(float width) {
        this.width = width;
    }

    /**
     * 获取边框颜色
     *
     * @return 边框颜色
     */
    public UIColor getColor() {
        return color;
    }

    /**
     * 设置边框颜色
     *
     * @param color 边框颜色
     */
    public void setColor(UIColor color) {
        this.color = color;
    }

    /**
     * 获取圆角半径
     *
     * @return 圆角半径
     */
    public float getRadius() {
        return radius;
    }

    /**
     * 设置圆角半径
     *
     * @param radius 圆角半径
     */
    public void setRadius(float radius) {
        this.radius = radius;
    }

    /**
     * 检查是否有边框
     *
     * @return true 如果有边框（宽度 > 0）
     */
    public boolean hasBorder() {
        return width > 0;
    }

    /**
     * 检查是否有圆角
     *
     * @return true 如果有圆角（半径 > 0）
     */
    public boolean hasRoundedCorners() {
        return radius > 0;
    }

    /**
     * 复制当前边框样式
     *
     * @return 新的边框样式副本
     */
    public BorderStyle copy() {
        return new BorderStyle(width, color, radius);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BorderStyle that = (BorderStyle) o;
        return Float.compare(that.width, width) == 0 &&
                Float.compare(that.radius, radius) == 0 &&
                Objects.equals(color, that.color);
    }

    @Override
    public int hashCode() {
        return Objects.hash(width, color, radius);
    }

    @Override
    public String toString() {
        return "BorderStyle{" +
                "width=" + width +
                ", color=" + color +
                ", radius=" + radius +
                '}';
    }
}
