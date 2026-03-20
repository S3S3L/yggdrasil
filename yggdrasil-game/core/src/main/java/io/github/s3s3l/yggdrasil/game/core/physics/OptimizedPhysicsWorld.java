package io.github.s3s3l.yggdrasil.game.core.physics;

import io.github.s3s3l.yggdrasil.game.core.basic.Vector2;
import io.github.s3s3l.yggdrasil.game.core.physics.collider.Collider;
import io.github.s3s3l.yggdrasil.game.core.physics.collider.CollisionDetector;
import io.github.s3s3l.yggdrasil.game.core.physics.collider.CollisionInfo;
import io.github.s3s3l.yggdrasil.game.core.physics.collider.Rectangle;
import io.github.s3s3l.yggdrasil.game.core.physics.spatial.QuadTree;

import java.util.*;

/**
 * 优化的物理世界实现
 * 使用四叉树空间划分进行高效碰撞检测
 * 适合碰撞体数量较多的场景（> 100）
 */
public class OptimizedPhysicsWorld implements PhysicsWorld {

    /**
     * 四叉树
     */
    private final QuadTree quadTree;

    /**
     * 所有碰撞体列表（用于遍历）
     */
    private final List<Collider> allColliders;

    /**
     * 碰撞监听器列表
     */
    private final List<CollisionListener> listeners;

    /**
     * 历史碰撞记录（用于检测碰撞开始/结束）
     */
    private final Map<ColliderPair, Boolean> previousCollisions;

    /**
     * 世界边界
     */
    private final Rectangle worldBounds;

    /**
     * 是否自动更新四叉树
     */
    private boolean autoRebuild;

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
     * 创建优化的物理世界
     *
     * @param worldBounds 世界边界
     * @param quadTreeCapacity 四叉树节点容量
     * @param quadTreeMaxDepth 四叉树最大深度
     */
    public OptimizedPhysicsWorld(Rectangle worldBounds, int quadTreeCapacity, int quadTreeMaxDepth) {
        this.worldBounds = worldBounds;
        this.quadTree = new QuadTree(worldBounds, quadTreeCapacity, quadTreeMaxDepth);
        this.allColliders = new ArrayList<>();
        this.listeners = new ArrayList<>();
        this.previousCollisions = new HashMap<>();
        this.autoRebuild = true;
    }

    /**
     * 创建优化的物理世界（默认参数）
     *
     * @param worldBounds 世界边界
     */
    public OptimizedPhysicsWorld(Rectangle worldBounds) {
        this(worldBounds, 4, 6);
    }

    @Override
    public void addCollider(Collider collider) {
        if (collider == null) {
            throw new IllegalArgumentException("Collider cannot be null");
        }
        if (!allColliders.contains(collider)) {
            allColliders.add(collider);
            quadTree.insert(collider);
        }
    }

    @Override
    public void removeCollider(Collider collider) {
        allColliders.remove(collider);
        quadTree.remove(collider);
        // 清理与该碰撞体相关的历史碰撞记录
        previousCollisions.keySet().removeIf(pair -> pair.a == collider || pair.b == collider);
    }

    @Override
    public void update(float deltaTime) {
        // 如果启用自动重建，每帧重建四叉树
        if (autoRebuild) {
            rebuildQuadTree();
        }
    }

    @Override
    public List<CollisionInfo> checkCollisions() {
        List<CollisionInfo> collisions = new ArrayList<>();
        Map<ColliderPair, Boolean> currentCollisions = new HashMap<>();

        // 使用四叉树进行宽相位检测
        for (Collider collider : allColliders) {
            Rectangle bounds = collider.getBounds();
            if (bounds == null) {
                continue;
            }

            // 查询可能与当前碰撞体相交的其他碰撞体
            List<Collider> candidates = quadTree.query(bounds);

            for (Collider other : candidates) {
                // 避免重复检测（只检测 ID 更大的）
                if (System.identityHashCode(collider) >= System.identityHashCode(other)) {
                    continue;
                }

                if (CollisionDetector.checkCollision(collider, other)) {
                    CollisionInfo info = CollisionDetector.getCollisionInfo(collider, other);
                    collisions.add(info);

                    ColliderPair pair = new ColliderPair(collider, other);
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
        allColliders.clear();
        previousCollisions.clear();
        // 重建四叉树
    }

    @Override
    public int getColliderCount() {
        return allColliders.size();
    }

    /**
     * 重建四叉树
     */
    public void rebuildQuadTree() {
        // 重新插入所有碰撞体
        for (Collider collider : allColliders) {
            quadTree.insert(collider);
        }
    }

    /**
     * 设置是否自动重建四叉树
     *
     * @param autoRebuild 是否自动重建
     */
    public void setAutoRebuild(boolean autoRebuild) {
        this.autoRebuild = autoRebuild;
    }

    /**
     * 获取四叉树
     *
     * @return 四叉树
     */
    public QuadTree getQuadTree() {
        return quadTree;
    }

    /**
     * 获取世界边界
     *
     * @return 世界边界
     */
    public Rectangle getWorldBounds() {
        return worldBounds;
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
