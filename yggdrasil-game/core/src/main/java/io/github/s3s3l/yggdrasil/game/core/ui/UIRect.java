package io.github.s3s3l.yggdrasil.game.core.ui;

import java.util.Objects;

/**
 * UI 矩形区域
 */
public class UIRect {

    private float x;
    private float y;
    private float width;
    private float height;

    public UIRect() {
        this(0, 0, 0, 0);
    }

    public UIRect(float x, float y, float width, float height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public UIRect(UIRect other) {
        this.x = other.x;
        this.y = other.y;
        this.width = other.width;
        this.height = other.height;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    /**
     * 获取右边界
     *
     * @return 右边界 X 坐标
     */
    public float getRight() {
        return x + width;
    }

    /**
     * 设置右边界
     *
     * @param right 右边界 X 坐标
     */
    public void setRight(float right) {
        this.width = right - x;
    }

    /**
     * 获取下边界
     *
     * @return 下边界 Y 坐标
     */
    public float getBottom() {
        return y + height;
    }

    /**
     * 设置下边界
     *
     * @param bottom 下边界 Y 坐标
     */
    public void setBottom(float bottom) {
        this.height = bottom - y;
    }

    /**
     * 获取中心 X 坐标
     *
     * @return 中心 X 坐标
     */
    public float getCenterX() {
        return x + width / 2f;
    }

    /**
     * 获取中心 Y 坐标
     *
     * @return 中心 Y 坐标
     */
    public float getCenterY() {
        return y + height / 2f;
    }

    /**
     * 检查点是否在矩形内
     *
     * @param pointX 点 X 坐标
     * @param pointY 点 Y 坐标
     * @return 是否在矩形内
     */
    public boolean contains(float pointX, float pointY) {
        return pointX >= x && pointX <= getRight() &&
               pointY >= y && pointY <= getBottom();
    }

    /**
     * 检查是否与另一个矩形相交
     *
     * @param other 另一个矩形
     * @return 是否相交
     */
    public boolean intersects(UIRect other) {
        return x < other.getRight() && getRight() > other.x &&
               y < other.getBottom() && getBottom() > other.y;
    }

    /**
     * 与另一个矩形合并
     *
     * @param other 另一个矩形
     * @return 合并后的矩形
     */
    public UIRect union(UIRect other) {
        float newX = Math.min(x, other.x);
        float newY = Math.min(y, other.y);
        float newRight = Math.max(getRight(), other.getRight());
        float newBottom = Math.max(getBottom(), other.getBottom());
        return new UIRect(newX, newY, newRight - newX, newBottom - newY);
    }

    /**
     * 计算与另一个矩形的交集
     *
     * @param other 另一个矩形
     * @return 交集矩形，如果没有交集则返回 null
     */
    public UIRect intersection(UIRect other) {
        float newX = Math.max(x, other.x);
        float newY = Math.max(y, other.y);
        float newRight = Math.min(getRight(), other.getRight());
        float newBottom = Math.min(getBottom(), other.getBottom());

        if (newX >= newRight || newY >= newBottom) {
            return null;
        }

        return new UIRect(newX, newY, newRight - newX, newBottom - newY);
    }

    /**
     * 偏移矩形
     *
     * @param dx X 方向偏移量
     * @param dy Y 方向偏移量
     */
    public void offset(float dx, float dy) {
        this.x += dx;
        this.y += dy;
    }

    /**
     * 缩放矩形
     *
     * @param scaleX X 方向缩放比例
     * @param scaleY Y 方向缩放比例
     */
    public void scale(float scaleX, float scaleY) {
        this.width *= scaleX;
        this.height *= scaleY;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UIRect uiRect = (UIRect) o;
        return Float.compare(uiRect.x, x) == 0 &&
               Float.compare(uiRect.y, y) == 0 &&
               Float.compare(uiRect.width, width) == 0 &&
               Float.compare(uiRect.height, height) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, width, height);
    }

    @Override
    public String toString() {
        return "UIRect{" +
                "x=" + x +
                ", y=" + y +
                ", width=" + width +
                ", height=" + height +
                '}';
    }
}
