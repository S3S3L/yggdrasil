package io.github.s3s3l.yggdrasil.game.core.physics.collider;

import io.github.s3s3l.yggdrasil.game.core.basic.Vector2;

/**
 * 碰撞体接口
 * 所有碰撞体的基础接口，提供碰撞检测和边界查询功能
 */
public interface Collider {

    /**
     * 获取碰撞体类型
     *
     * @return 碰撞体类型枚举
     */
    ColliderType getType();

    /**
     * 获取碰撞体中心位置
     *
     * @return 中心位置向量
     */
    Vector2 getPosition();

    /**
     * 设置碰撞体中心位置
     *
     * @param x X 坐标
     * @param y Y 坐标
     */
    void setPosition(float x, float y);

    /**
     * 设置碰撞体中心位置
     *
     * @param position 位置向量
     */
    void setPosition(Vector2 position);

    /**
     * 获取碰撞体的包围盒
     *
     * @return 包围盒矩形（ minX, minY, maxX, maxY）
     */
    Rectangle getBounds();

    /**
     * 检查点是否在碰撞体内
     *
     * @param x 点 X 坐标
     * @param y 点 Y 坐标
     * @return 点是否在碰撞体内
     */
    boolean contains(float x, float y);

    /**
     * 检查点是否在碰撞体内
     *
     * @param point 点向量
     * @return 点是否在碰撞体内
     */
    default boolean contains(Vector2 point) {
        return contains(point.x, point.y);
    }

    /**
     * 检查是否与另一个碰撞体相交
     *
     * @param other 另一个碰撞体
     * @return 是否相交
     */
    boolean intersects(Collider other);

    /**
     * 复制碰撞体
     *
     * @return 新的碰撞体副本
     */
    Collider copy();

    /**
     * 检查是否是触发器
     * <p>
     * 触发器不会产生物理碰撞响应，只会触发事件
     *
     * @return 是否是触发器
     */
    default boolean isTrigger() {
        return false;
    }

    /**
     * 设置是否是触发器
     *
     * @param isTrigger 是否是触发器
     */
    default void setTrigger(boolean isTrigger) {
        // 默认不支持设置
    }

    /**
     * 碰撞体类型枚举
     */
    enum ColliderType {
        BOX,
        CIRCLE,
        POLYGON
    }
}
