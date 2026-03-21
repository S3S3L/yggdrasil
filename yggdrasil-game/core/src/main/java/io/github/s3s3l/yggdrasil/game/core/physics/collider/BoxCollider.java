package io.github.s3s3l.yggdrasil.game.core.physics.collider;

import io.github.s3s3l.yggdrasil.game.core.basic.Vector2;

/**
 * 矩形碰撞体
 * 使用 AABB（轴对齐包围盒）进行碰撞检测
 */
public class BoxCollider implements Collider {

    private float centerX;
    private float centerY;
    private float width;
    private float height;
    private float halfWidth;
    private float halfHeight;
    private boolean isTrigger;  // 是否是触发器

    /**
     * 创建矩形碰撞体
     *
     * @param centerX 中心 X 坐标
     * @param centerY 中心 Y 坐标
     * @param width   宽度
     * @param height  高度
     */
    public BoxCollider(float centerX, float centerY, float width, float height) {
        this.centerX = centerX;
        this.centerY = centerY;
        setSize(width, height);
    }

    /**
     * 设置大小
     *
     * @param width  宽度
     * @param height 高度
     */
    public void setSize(float width, float height) {
        this.width = Math.abs(width);
        this.height = Math.abs(height);
        this.halfWidth = this.width / 2f;
        this.halfHeight = this.height / 2f;
    }

    public float getCenterY() {
        return centerY;
    }

    public float getHalfWidth() {
        return halfWidth;
    }

    public float getHalfHeight() {
        return halfHeight;
    }

    /**
     * 获取宽度
     *
     * @return 宽度
     */
    public float getWidth() {
        return width;
    }

    /**
     * 获取高度
     *
     * @return 高度
     */
    public float getHeight() {
        return height;
    }

    /**
     * 获取左边界
     *
     * @return 左边界 X 坐标
     */
    public float getLeft() {
        return centerX - halfWidth;
    }

    /**
     * 获取右边界
     *
     * @return 右边界 X 坐标
     */
    public float getRight() {
        return centerX + halfWidth;
    }

    /**
     * 获取上边界
     *
     * @return 上边界 Y 坐标
     */
    public float getTop() {
        return centerY + halfHeight;
    }

    /**
     * 获取下边界
     *
     * @return 下边界 Y 坐标
     */
    public float getBottom() {
        return centerY - halfHeight;
    }

    @Override
    public ColliderType getType() {
        return ColliderType.BOX;
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
                width, height
        );
    }

    @Override
    public boolean contains(float x, float y) {
        return x >= getLeft() && x <= getRight() &&
               y >= getBottom() && y <= getTop();
    }

    /**
     * 检查是否与另一个矩形碰撞体相交
     *
     * @param other 另一个矩形碰撞体
     * @return 是否相交
     */
    public boolean intersects(BoxCollider other) {
        return Math.abs(this.centerX - other.centerX) < (this.halfWidth + other.halfWidth) &&
               Math.abs(this.centerY - other.centerY) < (this.halfHeight + other.halfHeight);
    }

    /**
     * 检查是否与圆形碰撞体相交
     *
     * @param circle 圆形碰撞体
     * @return 是否相交
     */
    public boolean intersects(CircleCollider circle) {
        float closestX = clamp(circle.getCenterX(), getLeft(), getRight());
        float closestY = clamp(circle.getCenterY(), getBottom(), getTop());
        float dx = circle.getCenterX() - closestX;
        float dy = circle.getCenterY() - closestY;
        float radiusSquared = circle.getRadius() * circle.getRadius();
        return (dx * dx + dy * dy) < radiusSquared;
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
        if (other instanceof BoxCollider) {
            return intersects((BoxCollider) other);
        } else if (other instanceof CircleCollider) {
            return intersects((CircleCollider) other);
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
    public BoxCollider copy() {
        BoxCollider copy = new BoxCollider(centerX, centerY, width, height);
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
        BoxCollider that = (BoxCollider) o;
        return Float.compare(that.centerX, centerX) == 0 &&
               Float.compare(that.centerY, centerY) == 0 &&
               Float.compare(that.width, width) == 0 &&
               Float.compare(that.height, height) == 0;
    }

    @Override
    public int hashCode() {
        int result = (centerX != 0.0f ? Float.floatToIntBits(centerX) : 0);
        result = 31 * result + (centerY != 0.0f ? Float.floatToIntBits(centerY) : 0);
        result = 31 * result + (width != 0.0f ? Float.floatToIntBits(width) : 0);
        result = 31 * result + (height != 0.0f ? Float.floatToIntBits(height) : 0);
        return result;
    }

    @Override
    public String toString() {
        return String.format("BoxCollider[center=(%.2f, %.2f), size=%.2fx%.2f]",
                centerX, centerY, width, height);
    }
}
