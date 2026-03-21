package io.github.s3s3l.yggdrasil.game.core.physics;

import io.github.s3s3l.yggdrasil.game.core.basic.Vector2;
import io.github.s3s3l.yggdrasil.game.core.physics.collider.Collider;
import io.github.s3s3l.yggdrasil.game.core.physics.collider.CollisionDetector;
import io.github.s3s3l.yggdrasil.game.core.physics.collider.CollisionInfo;
import io.github.s3s3l.yggdrasil.game.core.physics.collider.Rectangle;

import java.util.*;

/**
 * 带碰撞响应的物理世界
 * <p>
 * 在 SimplePhysicsWorld 基础上添加：
 * - 刚体组件管理
 * - 碰撞响应计算
 * - 位置修正
 */
public class PhysicsWorldWithResponse implements PhysicsWorld {

    private final List<Collider> colliders;
    private final List<CollisionListener> listeners;
    private final Map<ColliderPair, Boolean> previousCollisions;
    private final Map<Collider, RigidbodyComponent> rigidbodies;

    /**
     * 穿透容差（用于位置修正）
     */
    private float positionSlop = 0.01f;

    /**
     * 是否启用位置修正
     */
    private boolean positionCorrectionEnabled = true;

    /**
     * 是否启用摩擦
     */
    private boolean frictionEnabled = true;

    /**
     * 碰撞体对（用于追踪碰撞状态）
     */
    private static class ColliderPair {
        private final Collider a;
        private final Collider b;

        public ColliderPair(Collider a, Collider b) {
            if (System.identityHashCode(a) < System.identityHashCode(b)) {
                this.a = a;
                this.b = b;
            } else {
                this.a = b;
                this.b = a;
            }
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            ColliderPair that = (ColliderPair) o;
            return (a == that.a && b == that.b);
        }

        @Override
        public int hashCode() {
            return System.identityHashCode(a) + System.identityHashCode(b);
        }
    }

    /**
     * 创建带响应的物理世界
     */
    public PhysicsWorldWithResponse() {
        this.colliders = new ArrayList<>();
        this.listeners = new ArrayList<>();
        this.previousCollisions = new HashMap<>();
        this.rigidbodies = new HashMap<>();
    }

    /**
     * 创建带响应的物理世界（指定世界边界）
     *
     * @param worldBounds 世界边界
     */
    public PhysicsWorldWithResponse(Rectangle worldBounds) {
        this();
    }

    @Override
    public void addCollider(Collider collider) {
        if (collider == null) {
            throw new IllegalArgumentException("Collider cannot be null");
        }
        if (!colliders.contains(collider)) {
            colliders.add(collider);
        }
    }

    /**
     * 添加刚体组件
     *
     * @param rigidbody 刚体组件
     */
    public void addRigidbody(RigidbodyComponent rigidbody) {
        if (rigidbody == null) {
            throw new IllegalArgumentException("Rigidbody cannot be null");
        }
        Collider collider = rigidbody.getCollider();
        if (collider != null) {
            addCollider(collider);
            rigidbodies.put(collider, rigidbody);
        }
    }

    @Override
    public void removeCollider(Collider collider) {
        colliders.remove(collider);
        rigidbodies.remove(collider);
        previousCollisions.keySet().removeIf(pair -> pair.a == collider || pair.b == collider);
    }

    /**
     * 移除刚体组件
     *
     * @param rigidbody 刚体组件
     */
    public void removeRigidbody(RigidbodyComponent rigidbody) {
        if (rigidbody != null && rigidbody.getCollider() != null) {
            removeCollider(rigidbody.getCollider());
        }
    }

    @Override
    public void update(float deltaTime) {
        // 更新所有刚体
        for (RigidbodyComponent rb : rigidbodies.values()) {
            if (rb != null && !rb.isKinematic()) {
                rb.update(deltaTime);
            }
        }
    }

    @Override
    public List<CollisionInfo> checkCollisions() {
        List<CollisionInfo> collisions = new ArrayList<>();
        Map<ColliderPair, Boolean> currentCollisions = new HashMap<>();

        int n = colliders.size();
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                Collider a = colliders.get(i);
                Collider b = colliders.get(j);

                if (CollisionDetector.checkCollision(a, b)) {
                    CollisionInfo info = CollisionDetector.getCollisionInfo(a, b);
                    collisions.add(info);

                    ColliderPair pair = new ColliderPair(a, b);
                    currentCollisions.put(pair, true);

                    RigidbodyComponent rb1 = rigidbodies.get(a);
                    RigidbodyComponent rb2 = rigidbodies.get(b);

                    // 如果是新碰撞，应用冲量响应
                    if (!previousCollisions.containsKey(pair)) {
                        fireCollisionStart(info, pair);

                        // 应用碰撞响应
                        if (rb1 != null && rb2 != null) {
                            resolveCollision(info, rb1, rb2);
                        }
                    } else {
                        fireCollisionStay(info, pair);

                        // 持续碰撞：应用位置修正
                        if (positionCorrectionEnabled && rb1 != null && rb2 != null) {
                            CollisionResponse.positionCorrection(info, rb1, rb2, positionSlop);
                        }
                    }
                }
            }
        }

        // 检查结束的碰撞
        for (ColliderPair pair : previousCollisions.keySet()) {
            if (!currentCollisions.containsKey(pair)) {
                fireCollisionEnd(pair);
            }
        }

        previousCollisions.clear();
        previousCollisions.putAll(currentCollisions);

        return collisions;
    }

    /**
     * 解析碰撞（应用冲量、摩擦、位置修正）
     *
     * @param info 碰撞信息
     * @param rb1  第一个刚体
     * @param rb2  第二个刚体
     */
    private void resolveCollision(CollisionInfo info, RigidbodyComponent rb1, RigidbodyComponent rb2) {
        // 1. 应用冲量响应（反弹）
        CollisionResponse.applyImpulse(info, rb1, rb2);

        // 2. 应用摩擦力
        if (frictionEnabled) {
            CollisionResponse.applyFriction(info, rb1, rb2);
        }

        // 3. 位置修正
        if (positionCorrectionEnabled) {
            CollisionResponse.positionCorrection(info, rb1, rb2, positionSlop);
        }
    }

    @Override
    public void addCollisionListener(CollisionListener listener) {
        if (listener == null) {
            throw new IllegalArgumentException("Listener cannot be null");
        }
        if (!listeners.contains(listener)) {
            listeners.add(listener);
        }
    }

    @Override
    public void removeCollisionListener(CollisionListener listener) {
        listeners.remove(listener);
    }

    @Override
    public void clear() {
        colliders.clear();
        rigidbodies.clear();
        previousCollisions.clear();
    }

    @Override
    public int getColliderCount() {
        return colliders.size();
    }

    /**
     * 获取刚体数量
     *
     * @return 刚体数量
     */
    public int getRigidbodyCount() {
        return rigidbodies.size();
    }

    /**
     * 获取穿透容差
     *
     * @return 穿透容差
     */
    public float getPositionSlop() {
        return positionSlop;
    }

    /**
     * 设置穿透容差
     *
     * @param positionSlop 穿透容差
     */
    public void setPositionSlop(float positionSlop) {
        this.positionSlop = positionSlop;
    }

    /**
     * 检查是否启用位置修正
     *
     * @return 是否启用
     */
    public boolean isPositionCorrectionEnabled() {
        return positionCorrectionEnabled;
    }

    /**
     * 设置是否启用位置修正
     *
     * @param enabled 是否启用
     */
    public void setPositionCorrectionEnabled(boolean enabled) {
        this.positionCorrectionEnabled = enabled;
    }

    /**
     * 检查是否启用摩擦
     *
     * @return 是否启用
     */
    public boolean isFrictionEnabled() {
        return frictionEnabled;
    }

    /**
     * 设置是否启用摩擦
     *
     * @param enabled 是否启用
     */
    public void setFrictionEnabled(boolean enabled) {
        this.frictionEnabled = enabled;
    }

    private void fireCollisionStart(CollisionInfo info, ColliderPair pair) {
        CollisionEvent event = new CollisionEvent(
                pair.a, pair.b,
                info.contactPoint, info.normal, info.penetrationDepth,
                CollisionEvent.CollisionEventType.START
        );
        for (CollisionListener listener : listeners) {
            try {
                listener.onCollisionStart(event);
            } catch (Exception e) {
                // 忽略监听器异常
            }
        }
    }

    private void fireCollisionStay(CollisionInfo info, ColliderPair pair) {
        CollisionEvent event = new CollisionEvent(
                pair.a, pair.b,
                info.contactPoint, info.normal, info.penetrationDepth,
                CollisionEvent.CollisionEventType.STAY
        );
        for (CollisionListener listener : listeners) {
            try {
                listener.onCollisionStay(event);
            } catch (Exception e) {
                // 忽略监听器异常
            }
        }
    }

    private void fireCollisionEnd(ColliderPair pair) {
        CollisionEvent event = new CollisionEvent(
                pair.a, pair.b,
                null, null, 0,
                CollisionEvent.CollisionEventType.END
        );
        for (CollisionListener listener : listeners) {
            try {
                listener.onCollisionEnd(event);
            } catch (Exception e) {
                // 忽略监听器异常
            }
        }
    }
}
