package io.github.s3s3l.yggdrasil.game.core.physics.collider;

import io.github.s3s3l.yggdrasil.game.core.basic.Vector2;

/**
 * 矩形数据结构
 * 用于表示轴对齐矩形边界
 */
public class Rectangle {

    public float minX;
    public float minY;
    public float maxX;
    public float maxY;

    /**
     * 创建空矩形
     */
    public Rectangle() {
        this(0, 0, 0, 0);
    }

    /**
     * 创建矩形
     *
     * @param minX 最小 X 坐标
     * @param minY 最小 Y 坐标
     * @param maxX 最大 X 坐标
     * @param maxY 最大 Y 坐标
     */
    public Rectangle(float minX, float minY, float maxX, float maxY) {
        this.minX = minX;
        this.minY = minY;
        this.maxX = maxX;
        this.maxY = maxY;
    }

    /**
     * 从中心点和宽高创建矩形
     *
     * @param centerX 中心 X 坐标
     * @param centerY 中心 Y 坐标
     * @param width   宽度
     * @param height  高度
     * @return 矩形
     */
    public static Rectangle fromCenter(float centerX, float centerY, float width, float height) {
        float halfWidth = width / 2f;
        float halfHeight = height / 2f;
        return new Rectangle(
                centerX - halfWidth,
                centerY - halfHeight,
                centerX + halfWidth,
                centerY + halfHeight
        );
    }

    /**
     * 获取矩形宽度
     *
     * @return 宽度
     */
    public float getWidth() {
        return maxX - minX;
    }

    /**
     * 获取矩形高度
     *
     * @return 高度
     */
    public float getHeight() {
        return maxY - minY;
    }

    /**
     * 获取中心 X 坐标
     *
     * @return 中心 X 坐标
     */
    public float getCenterX() {
        return (minX + maxX) / 2f;
    }

    /**
     * 获取中心 Y 坐标
     *
     * @return 中心 Y 坐标
     */
    public float getCenterY() {
        return (minY + maxY) / 2f;
    }

    /**
     * 获取中心点
     *
     * @return 中心点向量
     */
    public Vector2 getCenter() {
        return new Vector2(getCenterX(), getCenterY());
    }

    /**
     * 检查点是否在矩形内
     *
     * @param x 点 X 坐标
     * @param y 点 Y 坐标
     * @return 点是否在矩形内
     */
    public boolean contains(float x, float y) {
        return x >= minX && x <= maxX && y >= minY && y <= maxY;
    }

    /**
     * 检查点是否在矩形内
     *
     * @param point 点向量
     * @return 点是否在矩形内
     */
    public boolean contains(Vector2 point) {
        return contains(point.x, point.y);
    }

    /**
     * 检查是否与另一个矩形相交
     *
     * @param other 另一个矩形
     * @return 是否相交
     */
    public boolean intersects(Rectangle other) {
        return minX < other.maxX && maxX > other.minX &&
               minY < other.maxY && maxY > other.minY;
    }

    /**
     * 移动矩形
     *
     * @param dx X 方向位移
     * @param dy Y 方向位移
     */
    public void translate(float dx, float dy) {
        minX += dx;
        maxX += dx;
        minY += dy;
        maxY += dy;
    }

    /**
     * 扩展矩形以包含另一个矩形
     *
     * @param other 另一个矩形
     */
    public void expand(Rectangle other) {
        minX = Math.min(minX, other.minX);
        minY = Math.min(minY, other.minY);
        maxX = Math.max(maxX, other.maxX);
        maxY = Math.max(maxY, other.maxY);
    }

    @Override
    public String toString() {
        return String.format("Rectangle[minX=%.2f, minY=%.2f, maxX=%.2f, maxY=%.2f]",
                minX, minY, maxX, maxY);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Rectangle rectangle = (Rectangle) o;
        return Float.compare(rectangle.minX, minX) == 0 &&
               Float.compare(rectangle.minY, minY) == 0 &&
               Float.compare(rectangle.maxX, maxX) == 0 &&
               Float.compare(rectangle.maxY, maxY) == 0;
    }

    @Override
    public int hashCode() {
        int result = (minX != 0.0f ? Float.floatToIntBits(minX) : 0);
        result = 31 * result + (minY != 0.0f ? Float.floatToIntBits(minY) : 0);
        result = 31 * result + (maxX != 0.0f ? Float.floatToIntBits(maxX) : 0);
        result = 31 * result + (maxY != 0.0f ? Float.floatToIntBits(maxY) : 0);
        return result;
    }
}
