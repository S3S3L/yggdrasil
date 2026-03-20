package io.github.s3s3l.yggdrasil.game.core.basic;

/**
 * 二维向量类
 * 用于表示 2D 空间中的点和方向
 */
public class Vector2 {

    public float x;
    public float y;

    /**
     * 创建零向量
     */
    public Vector2() {
        this(0, 0);
    }

    /**
     * 创建向量
     *
     * @param x X 分量
     * @param y Y 分量
     */
    public Vector2(float x, float y) {
        this.x = x;
        this.y = y;
    }

    /**
     * 从另一个向量创建副本
     *
     * @param other 源向量
     */
    public Vector2(Vector2 other) {
        this(other.x, other.y);
    }

    /**
     * 设置向量值
     *
     * @param x X 分量
     * @param y Y 分量
     * @return this
     */
    public Vector2 set(float x, float y) {
        this.x = x;
        this.y = y;
        return this;
    }

    /**
     * 设置向量值
     *
     * @param other 源向量
     * @return this
     */
    public Vector2 set(Vector2 other) {
        this.x = other.x;
        this.y = other.y;
        return this;
    }

    /**
     * 向量加法
     *
     * @param other 另一个向量
     * @return 结果向量（新对象）
     */
    public Vector2 add(Vector2 other) {
        return new Vector2(this.x + other.x, this.y + other.y);
    }

    /**
     * 向量加法（原地）
     *
     * @param other 另一个向量
     * @return this
     */
    public Vector2 addLocal(Vector2 other) {
        this.x += other.x;
        this.y += other.y;
        return this;
    }

    /**
     * 向量减法
     *
     * @param other 另一个向量
     * @return 结果向量（新对象）
     */
    public Vector2 sub(Vector2 other) {
        return new Vector2(this.x - other.x, this.y - other.y);
    }

    /**
     * 向量减法（原地）
     *
     * @param other 另一个向量
     * @return this
     */
    public Vector2 subLocal(Vector2 other) {
        this.x -= other.x;
        this.y -= other.y;
        return this;
    }

    /**
     * 向量数乘
     *
     * @param scalar 标量
     * @return 结果向量（新对象）
     */
    public Vector2 scale(float scalar) {
        return new Vector2(this.x * scalar, this.y * scalar);
    }

    /**
     * 向量数乘（原地）
     *
     * @param scalar 标量
     * @return this
     */
    public Vector2 scaleLocal(float scalar) {
        this.x *= scalar;
        this.y *= scalar;
        return this;
    }

    /**
     * 向量点积
     *
     * @param other 另一个向量
     * @return 点积结果
     */
    public float dot(Vector2 other) {
        return this.x * other.x + this.y * other.y;
    }

    /**
     * 向量叉积（返回 Z 分量，2D 叉积为标量）
     *
     * @param other 另一个向量
     * @return 叉积结果
     */
    public float cross(Vector2 other) {
        return this.x * other.y - this.y * other.x;
    }

    /**
     * 获取向量长度
     *
     * @return 向量长度
     */
    public float length() {
        return (float) Math.sqrt(x * x + y * y);
    }

    /**
     * 获取向量长度的平方
     *
     * @return 向量长度的平方
     */
    public float lengthSquared() {
        return x * x + y * y;
    }

    /**
     * 归一化向量
     *
     * @return 归一化后的向量（新对象）
     */
    public Vector2 normalized() {
        float len = length();
        if (len > 0) {
            return new Vector2(x / len, y / len);
        }
        return new Vector2(0, 0);
    }

    /**
     * 归一化向量（原地）
     *
     * @return this
     */
    public Vector2 normalize() {
        float len = length();
        if (len > 0) {
            x /= len;
            y /= len;
        }
        return this;
    }

    /**
     * 获取向量与另一个向量的距离
     *
     * @param other 另一个向量
     * @return 距离
     */
    public float distance(Vector2 other) {
        float dx = x - other.x;
        float dy = y - other.y;
        return (float) Math.sqrt(dx * dx + dy * dy);
    }

    /**
     * 获取向量与另一个向量的距离的平方
     *
     * @param other 另一个向量
     * @return 距离的平方
     */
    public float distanceSquared(Vector2 other) {
        float dx = x - other.x;
        float dy = y - other.y;
        return dx * dx + dy * dy;
    }

    /**
     * 向量插值
     *
     * @param other 另一个向量
     * @param alpha 插值因子（0-1）
     * @return 插值结果向量
     */
    public Vector2 lerp(Vector2 other, float alpha) {
        return new Vector2(
                x + (other.x - x) * alpha,
                y + (other.y - y) * alpha
        );
    }

    /**
     * 旋转向量
     *
     * @param angle 旋转角度（弧度）
     * @return 旋转后的向量（新对象）
     */
    public Vector2 rotate(float angle) {
        float cos = (float) Math.cos(angle);
        float sin = (float) Math.sin(angle);
        return new Vector2(
                x * cos - y * sin,
                x * sin + y * cos
        );
    }

    /**
     * 获取向量的法线（逆时针旋转 90 度）
     *
     * @return 法线向量
     */
    public Vector2 normal() {
        return new Vector2(-y, x);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vector2 vector2 = (Vector2) o;
        return Float.compare(vector2.x, x) == 0 &&
               Float.compare(vector2.y, y) == 0;
    }

    @Override
    public int hashCode() {
        int result = (x != 0.0f ? Float.floatToIntBits(x) : 0);
        result = 31 * result + (y != 0.0f ? Float.floatToIntBits(y) : 0);
        return result;
    }

    @Override
    public String toString() {
        return String.format("Vector2(%.2f, %.2f)", x, y);
    }

    /**
     * 零向量常量
     */
    public static final Vector2 ZERO = new Vector2(0, 0);

    /**
     * X 轴单位向量常量
     */
    public static final Vector2 RIGHT = new Vector2(1, 0);

    /**
     * Y 轴单位向量常量
     */
    public static final Vector2 UP = new Vector2(0, 1);
}
