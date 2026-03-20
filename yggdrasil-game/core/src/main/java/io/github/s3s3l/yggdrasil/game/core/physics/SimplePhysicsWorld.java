package io.github.s3s3l.yggdrasil.game.core.physics;

import io.github.s3s3l.yggdrasil.game.core.physics.collider.Collider;
import io.github.s3s3l.yggdrasil.game.core.physics.collider.CollisionDetector;
import io.github.s3s3l.yggdrasil.game.core.physics.collider.CollisionInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 简单物理世界实现
 * 使用线性遍历进行碰撞检测
 * 适合碰撞体数量较少的场景（< 100）
 */
public class SimplePhysicsWorld implements PhysicsWorld {

    private final List<Collider> colliders;
    private final List<CollisionListener> listeners;
    private final Map<ColliderPair, Boolean> previousCollisions;

    /**
     * 碰撞体对（用于追踪碰撞状态）
     */
    private static class ColliderPair {
        private final Collider a;
        private final Collider b;

        public ColliderPair(Collider a, Collider b) {
            // 确保配对顺序一致，避免重复
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
     * 创建简单物理世界
     */
    public SimplePhysicsWorld() {
        this.colliders = new ArrayList<>();
        this.listeners = new ArrayList<>();
        this.previousCollisions = new HashMap<>();
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

    @Override
    public void removeCollider(Collider collider) {
        colliders.remove(collider);
        // 清理与该碰撞体相关的历史碰撞记录
        previousCollisions.keySet().removeIf(pair -> pair.a == collider || pair.b == collider);
    }

    @Override
    public void update(float deltaTime) {
        // 简单实现不需要每帧更新
        // 子类可以重写此方法实现更复杂的逻辑
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

                    // 检查是否是新的碰撞
                    if (!previousCollisions.containsKey(pair)) {
                        fireCollisionStart(info, pair);
                    } else {
                        fireCollisionStay(info, pair);
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

        // 更新历史碰撞记录
        previousCollisions.clear();
        previousCollisions.putAll(currentCollisions);

        return collisions;
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
        previousCollisions.clear();
    }

    @Override
    public int getColliderCount() {
        return colliders.size();
    }

    /**
     * 触发碰撞开始事件
     */
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
                // 忽略监听器异常，继续通知其他监听器
            }
        }
    }

    /**
     * 触发碰撞持续事件
     */
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
                // 忽略监听器异常，继续通知其他监听器
            }
        }
    }

    /**
     * 触发碰撞结束事件
     */
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
                // 忽略监听器异常，继续通知其他监听器
            }
        }
    }
}
