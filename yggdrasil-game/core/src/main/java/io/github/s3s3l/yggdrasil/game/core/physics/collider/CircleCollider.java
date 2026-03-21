package io.github.s3s3l.yggdrasil.game.core.physics.collider;

import io.github.s3s3l.yggdrasil.game.core.basic.Vector2;
import io.github.s3s3l.yggdrasil.game.core.pool.PoolableObject;

/**
 * 圆形碰撞体
 * 使用圆心和半径定义，支持圆 - 圆、圆 - 矩形碰撞检测
 * 支持对象池化
 */
public class CircleCollider implements Collider, PoolableObject<CircleCollider> {

    private float centerX;
    private float centerY;
    private float radius;
    private float radiusSquared;
    private boolean isTrigger;  // 是否是触发器

    /**
     * 创建圆形碰撞体
     *
     * @param centerX 中心 X 坐标
     * @param centerY 中心 Y 坐标
     * @param radius  半径
     */
    public CircleCollider(float centerX, float centerY, float radius) {
        this.centerX = centerX;
        this.centerY = centerY;
        setRadius(radius);
    }

    /**
     * 设置半径
     *
     * @param radius 半径
     */
    public void setRadius(float radius) {
        this.radius = Math.abs(radius);
        this.radiusSquared = this.radius * this.radius;
    }

    /**
     * 获取半径
     *
     * @return 半径
     */
    public float getRadius() {
        return radius;
    }

    /**
     * 获取中心 X 坐标
     *
     * @return 中心 X 坐标
     */
    public float getCenterX() {
        return centerX;
    }

    /**
     * 获取中心 Y 坐标
     *
     * @return 中心 Y 坐标
     */
    public float getCenterY() {
        return centerY;
    }

    /**
     * 获取中心点
     *
     * @return 中心点向量
     */
    public Vector2 getCenter() {
        return new Vector2(centerX, centerY);
    }

    @Override
    public ColliderType getType() {
        return ColliderType.CIRCLE;
    }

    @Override
    public Vector2 getPosition() {
        return new Vector2(centerX, centerY);
    }

    @Override
    public void setPosition(float x, float y) {
        this.centerX = x;
        this.centerY = y;
    }

    @Override
    public void setPosition(Vector2 position) {
        this.centerX = position.x;
        this.centerY = position.y;
    }

    @Override
    public Rectangle getBounds() {
        return Rectangle.fromCenter(
                centerX, centerY,
                radius * 2, radius * 2
        );
    }

    @Override
    public boolean contains(float x, float y) {
        float dx = x - centerX;
        float dy = y - centerY;
        return (dx * dx + dy * dy) <= radiusSquared;
    }

    /**
     * 检查是否与另一个圆形碰撞体相交
     *
     * @param other 另一个圆形碰撞体
     * @return 是否相交
     */
    public boolean intersects(CircleCollider other) {
        float dx = this.centerX - other.centerX;
        float dy = this.centerY - other.centerY;
        float radiiSum = this.radius + other.radius;
        return (dx * dx + dy * dy) <= (radiiSum * radiiSum);
    }

    /**
     * 检查是否与矩形碰撞体相交
     *
     * @param box 矩形碰撞体
     * @return 是否相交
     */
    public boolean intersects(BoxCollider box) {
        float closestX = clamp(centerX, box.getLeft(), box.getRight());
        float closestY = clamp(centerY, box.getBottom(), box.getTop());
        float dx = centerX - closestX;
        float dy = centerY - closestY;
        return (dx * dx + dy * dy) <= radiusSquared;
    }

    /**
     * 检查是否与多边形碰撞体相交
     *
     * @param polygon 多边形碰撞体
     * @return 是否相交
     */
    public boolean intersects(PolygonCollider polygon) {
        return polygon.intersects(this);
    }

    @Override
    public boolean intersects(Collider other) {
        if (other instanceof CircleCollider) {
            return intersects((CircleCollider) other);
        } else if (other instanceof BoxCollider) {
            return intersects((BoxCollider) other);
        } else if (other instanceof PolygonCollider) {
            return intersects((PolygonCollider) other);
        }
        return false;
    }

    @Override
    public boolean isTrigger() {
        return isTrigger;
    }

    @Override
    public void setTrigger(boolean isTrigger) {
        this.isTrigger = isTrigger;
    }

    @Override
    public CircleCollider copy() {
        CircleCollider copy = new CircleCollider(centerX, centerY, radius);
        copy.isTrigger = this.isTrigger;
        return copy;
    }

    /**
     * 钳制值到指定范围
     */
    private float clamp(float value, float min, float max) {
        return Math.max(min, Math.min(max, value));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CircleCollider that = (CircleCollider) o;
        return Float.compare(that.centerX, centerX) == 0 &&
               Float.compare(that.centerY, centerY) == 0 &&
               Float.compare(that.radius, radius) == 0;
    }

    @Override
    public int hashCode() {
        int result = (centerX != 0.0f ? Float.floatToIntBits(centerX) : 0);
        result = 31 * result + (centerY != 0.0f ? Float.floatToIntBits(centerY) : 0);
        result = 31 * result + (radius != 0.0f ? Float.floatToIntBits(radius) : 0);
        return result;
    }

    @Override
    public String toString() {
        return String.format("CircleCollider[center=(%.2f, %.2f), radius=%.2f]",
                centerX, centerY, radius);
    }

    @Override
    public void reset() {
        this.centerX = 0;
        this.centerY = 0;
        this.radius = 0;
        this.radiusSquared = 0;
        this.isTrigger = false;
    }

    @Override
    public void onReturn() {
        reset();
    }

    @Override
    public boolean isValid() {
        return true;
    }

    /**
     * 设置碰撞体数据（用于从池中借出后初始化）
     *
     * @param centerX 中心 X 坐标
     * @param centerY 中心 Y 坐标
     * @param radius 半径
     */
    public void set(float centerX, float centerY, float radius) {
        this.centerX = centerX;
        this.centerY = centerY;
        setRadius(radius);
    }
}
