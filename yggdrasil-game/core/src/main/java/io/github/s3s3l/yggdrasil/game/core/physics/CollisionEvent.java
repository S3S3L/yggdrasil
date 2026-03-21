package io.github.s3s3l.yggdrasil.game.core.physics;

import io.github.s3s3l.yggdrasil.game.core.basic.Vector2;
import io.github.s3s3l.yggdrasil.game.core.physics.collider.Collider;

/**
 * 碰撞事件数据类
 * 用于传递碰撞相关信息
 */
public class CollisionEvent {

    /**
     * 碰撞类型枚举
     */
    public enum CollisionEventType {
        START,  // 碰撞开始
        STAY,   // 碰撞持续
        END     // 碰撞结束
    }

    private Collider colliderA;
    private Collider colliderB;
    private Vector2 contactPoint;
    private Vector2 normal;
    private float penetrationDepth;
    private CollisionEventType type;
    private boolean isTrigger;  // 是否是触发器事件

    /**
     * 创建碰撞事件
     *
     * @param colliderA 碰撞体 A
     * @param colliderB 碰撞体 B
     * @param contactPoint 碰撞点
     * @param normal 法线
     * @param penetrationDepth 穿透深度
     * @param type 碰撞类型
     * @param isTrigger 是否是触发器事件
     */
    public CollisionEvent(Collider colliderA, Collider colliderB, Vector2 contactPoint,
                          Vector2 normal, float penetrationDepth, CollisionEventType type, boolean isTrigger) {
        this.colliderA = colliderA;
        this.colliderB = colliderB;
        this.contactPoint = contactPoint;
        this.normal = normal;
        this.penetrationDepth = penetrationDepth;
        this.type = type;
        this.isTrigger = isTrigger;
    }

    /**
     * 创建碰撞事件（默认非触发器）
     *
     * @param colliderA 碰撞体 A
     * @param colliderB 碰撞体 B
     * @param contactPoint 碰撞点
     * @param normal 法线
     * @param penetrationDepth 穿透深度
     * @param type 碰撞类型
     */
    public CollisionEvent(Collider colliderA, Collider colliderB, Vector2 contactPoint,
                          Vector2 normal, float penetrationDepth, CollisionEventType type) {
        this(colliderA, colliderB, contactPoint, normal, penetrationDepth, type, false);
    }

    /**
     * 创建碰撞事件（无参构造）
     */
    public CollisionEvent() {
        this.isTrigger = false;
    }

    /**
     * 创建碰撞事件
     *
     * @param colliderA 碰撞体 A
     * @param colliderB 碰撞体 B
     * @param type 碰撞类型
     */
    public CollisionEvent(Collider colliderA, Collider colliderB, CollisionEventType type) {
        this(colliderA, colliderB, null, null, 0, type, false);
    }

    /**
     * 创建碰撞事件（简化版，支持触发器）
     *
     * @param colliderA 碰撞体 A
     * @param colliderB 碰撞体 B
     * @param type 碰撞类型
     * @param isTrigger 是否是触发器事件
     */
    public CollisionEvent(Collider colliderA, Collider colliderB, CollisionEventType type, boolean isTrigger) {
        this(colliderA, colliderB, null, null, 0, type, isTrigger);
    }

    /**
     * 设置碰撞事件数据
     *
     * @param colliderA 碰撞体 A
     * @param colliderB 碰撞体 B
     * @param contactPoint 碰撞点
     * @param normal 法线
     * @param penetrationDepth 穿透深度
     * @param type 碰撞类型
     */
    public void set(Collider colliderA, Collider colliderB, Vector2 contactPoint,
                    Vector2 normal, float penetrationDepth, CollisionEventType type) {
        this.colliderA = colliderA;
        this.colliderB = colliderB;
        this.contactPoint = contactPoint;
        this.normal = normal;
        this.penetrationDepth = penetrationDepth;
        this.type = type;
    }

    public Collider getColliderA() {
        return colliderA;
    }

    public Collider getColliderB() {
        return colliderB;
    }

    public Vector2 getContactPoint() {
        return contactPoint;
    }

    public Vector2 getNormal() {
        return normal;
    }

    public float getPenetrationDepth() {
        return penetrationDepth;
    }

    public CollisionEventType getType() {
        return type;
    }

    /**
     * 设置碰撞体 A
     *
     * @param colliderA 碰撞体 A
     */
    public void setColliderA(Collider colliderA) {
        this.colliderA = colliderA;
    }

    /**
     * 设置碰撞体 B
     *
     * @param colliderB 碰撞体 B
     */
    public void setColliderB(Collider colliderB) {
        this.colliderB = colliderB;
    }

    /**
     * 设置接触点
     *
     * @param contactPoint 接触点
     */
    public void setContactPoint(Vector2 contactPoint) {
        this.contactPoint = contactPoint;
    }

    /**
     * 设置法线
     *
     * @param normal 法线
     */
    public void setNormal(Vector2 normal) {
        this.normal = normal;
    }

    /**
     * 设置穿透深度
     *
     * @param penetrationDepth 穿透深度
     */
    public void setPenetrationDepth(float penetrationDepth) {
        this.penetrationDepth = penetrationDepth;
    }

    /**
     * 设置碰撞类型
     *
     * @param type 碰撞类型
     */
    public void setType(CollisionEventType type) {
        this.type = type;
    }

    /**
     * 检查是否是触发器事件
     *
     * @return 是否是触发器
     */
    public boolean isTrigger() {
        return isTrigger;
    }

    /**
     * 设置是否是触发器
     *
     * @param trigger 是否是触发器
     */
    public void setTrigger(boolean trigger) {
        isTrigger = trigger;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CollisionEvent that = (CollisionEvent) o;
        return type == that.type &&
               java.util.Objects.equals(colliderA, that.colliderA) &&
               java.util.Objects.equals(colliderB, that.colliderB);
    }

    @Override
    public int hashCode() {
        int result = java.util.Objects.hash(colliderA, colliderB, type);
        return result;
    }

    @Override
    public String toString() {
        return String.format("CollisionEvent{type=%s, A=%s, B=%s}", type, colliderA, colliderB);
    }
}
