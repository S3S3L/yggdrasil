package io.github.s3s3l.yggdrasil.game.core.physics;

import io.github.s3s3l.yggdrasil.game.core.basic.Vector2;

/**
 * 2D 射线
 * 用于射线投射检测
 */
public class Ray {

    /**
     * 射线原点
     */
    public Vector2 origin;

    /**
     * 射线方向（已归一化）
     */
    public Vector2 direction;

    /**
     * 创建射线
     *
     * @param origin    原点
     * @param direction 方向（会自动归一化）
     */
    public Ray(Vector2 origin, Vector2 direction) {
        this.origin = new Vector2(origin);
        this.direction = new Vector2(direction);
        this.direction.normalize();
    }

    /**
     * 创建射线
     *
     * @param originX 原点 X 坐标
     * @param originY 原点 Y 坐标
     * @param directionX 方向 X 分量
     * @param directionY 方向 Y 分量
     */
    public Ray(float originX, float originY, float directionX, float directionY) {
        this.origin = new Vector2(originX, originY);
        this.direction = new Vector2(directionX, directionY);
        this.direction.normalize();
    }

    /**
     * 获取射线上指定距离的点
     *
     * @param distance 距离
     * @return 点坐标
     */
    public Vector2 getPoint(float distance) {
        return new Vector2(
                origin.x + direction.x * distance,
                origin.y + direction.y * distance
        );
    }

    /**
     * 检查点是否在射线上
     *
     * @param point 点
     * @param epsilon 误差容限
     * @return 点是否在射线上
     */
    public boolean containsPoint(Vector2 point, float epsilon) {
        Vector2 toPoint = new Vector2(point.x - origin.x, point.y - origin.y);
        float distance = toPoint.length();

        if (distance < epsilon) {
            return true;
        }

        Vector2 toPointNormalized = toPoint.normalized();
        float dot = direction.dot(toPointNormalized);

        return Math.abs(dot - 1.0f) < epsilon;
    }

    /**
     * 设置射线
     *
     * @param origin 原点
     * @param direction 方向
     * @return this
     */
    public Ray set(Vector2 origin, Vector2 direction) {
        this.origin.set(origin);
        this.direction.set(direction);
        this.direction.normalize();
        return this;
    }

    @Override
    public String toString() {
        return String.format("Ray[origin=%s, direction=%s]", origin, direction);
    }
}
