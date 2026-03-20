package io.github.s3s3l.yggdrasil.game.core.physics.collider;

import io.github.s3s3l.yggdrasil.game.core.basic.Vector2;

/**
 * 碰撞信息数据类
 * 存储碰撞检测结果的详细信息
 */
public class CollisionInfo {

    /**
     * 是否发生碰撞
     */
    public boolean collided;

    /**
     * 碰撞点（世界坐标）
     */
    public Vector2 contactPoint;

    /**
     * 碰撞法线（从 colliderA 指向 colliderB）
     */
    public Vector2 normal;

    /**
     * 穿透深度
     */
    public float penetrationDepth;

    /**
     * 第一个碰撞体
     */
    public Collider colliderA;

    /**
     * 第二个碰撞体
     */
    public Collider colliderB;

    /**
     * 创建空的碰撞信息
     */
    public CollisionInfo() {
        this.collided = false;
        this.contactPoint = new Vector2();
        this.normal = new Vector2();
        this.penetrationDepth = 0;
    }

    /**
     * 设置碰撞信息
     *
     * @param collided          是否碰撞
     * @param contactPoint      碰撞点
     * @param normal            法线
     * @param penetrationDepth  穿透深度
     * @param colliderA         第一个碰撞体
     * @param colliderB         第二个碰撞体
     */
    public void set(boolean collided, Vector2 contactPoint, Vector2 normal,
                    float penetrationDepth, Collider colliderA, Collider colliderB) {
        this.collided = collided;
        if (contactPoint != null) {
            this.contactPoint.set(contactPoint);
        }
        if (normal != null) {
            this.normal.set(normal);
        }
        this.penetrationDepth = penetrationDepth;
        this.colliderA = colliderA;
        this.colliderB = colliderB;
    }

    /**
     * 清除碰撞信息
     */
    public void clear() {
        this.collided = false;
        this.contactPoint.set(0, 0);
        this.normal.set(0, 0);
        this.penetrationDepth = 0;
        this.colliderA = null;
        this.colliderB = null;
    }

    /**
     * 复制碰撞信息
     *
     * @return 新的碰撞信息副本
     */
    public CollisionInfo copy() {
        CollisionInfo copy = new CollisionInfo();
        copy.collided = this.collided;
        copy.contactPoint.set(this.contactPoint);
        copy.normal.set(this.normal);
        copy.penetrationDepth = this.penetrationDepth;
        copy.colliderA = this.colliderA;
        copy.colliderB = this.colliderB;
        return copy;
    }

    @Override
    public String toString() {
        if (collided) {
            return String.format("CollisionInfo[collided=true, point=%s, normal=%s, depth=%.2f]",
                    contactPoint, normal, penetrationDepth);
        }
        return "CollisionInfo[collided=false]";
    }
}
