package io.github.s3s3l.yggdrasil.game.core.pool;

import io.github.s3s3l.yggdrasil.game.core.physics.collider.BoxCollider;
import io.github.s3s3l.yggdrasil.game.core.physics.collider.CircleCollider;
import io.github.s3s3l.yggdrasil.game.core.physics.collider.Collider;
import io.github.s3s3l.yggdrasil.game.core.physics.collider.PolygonCollider;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 碰撞体对象池
 * 统一管理各类碰撞体的池化，减少碰撞体对象创建和销毁的开销
 */
public class ColliderPool {

    /**
     * 单例实例
     */
    private static final ColliderPool INSTANCE = new ColliderPool();

    /**
     * 碰撞体池映射表
     */
    private final Map<Collider.ColliderType, ObjectPool<? extends PoolableObject<?>>> colliderPools;

    /**
     * 默认初始池大小
     */
    private static final int DEFAULT_INITIAL_POOL_SIZE = 20;

    /**
     * 默认最大池大小
     */
    private static final int DEFAULT_MAX_POOL_SIZE = 100;

    private ColliderPool() {
        this.colliderPools = new ConcurrentHashMap<>();
        // 注册默认池
        registerDefaultPools();
    }

    /**
     * 注册默认池
     */
    private void registerDefaultPools() {
        // BoxCollider 池
        registerCollider(Collider.ColliderType.BOX,
                new BoxColliderFactory(),
                DEFAULT_INITIAL_POOL_SIZE,
                DEFAULT_MAX_POOL_SIZE);

        // CircleCollider 池
        registerCollider(Collider.ColliderType.CIRCLE,
                new CircleColliderFactory(),
                DEFAULT_INITIAL_POOL_SIZE,
                DEFAULT_MAX_POOL_SIZE);

        // PolygonCollider 池
        registerCollider(Collider.ColliderType.POLYGON,
                new PolygonColliderFactory(),
                DEFAULT_INITIAL_POOL_SIZE / 2,  // 多边形碰撞体更复杂，初始池小一些
                DEFAULT_MAX_POOL_SIZE / 2);
    }

    /**
     * 获取碰撞体池实例
     *
     * @return 碰撞体池单例
     */
    public static ColliderPool get() {
        return INSTANCE;
    }

    /**
     * 注册碰撞体类型到对象池
     *
     * @param type 碰撞体类型
     * @param factory 碰撞体工厂
     * @param <T> 碰撞体类型
     */
    public <T extends Collider & PoolableObject<T>> void registerCollider(
            Collider.ColliderType type,
            PoolableObjectFactory<T> factory) {
        registerCollider(type, factory, DEFAULT_INITIAL_POOL_SIZE, DEFAULT_MAX_POOL_SIZE);
    }

    /**
     * 注册碰撞体类型到对象池（指定池大小）
     *
     * @param type 碰撞体类型
     * @param factory 碰撞体工厂
     * @param initialSize 初始池大小
     * @param maxSize 最大池大小
     * @param <T> 碰撞体类型
     */
    public <T extends Collider & PoolableObject<T>> void registerCollider(
            Collider.ColliderType type,
            PoolableObjectFactory<T> factory,
            int initialSize,
            int maxSize) {
        ObjectPool<T> pool = new ObjectPool<>(factory, initialSize, maxSize);
        colliderPools.put(type, pool);
    }

    /**
     * 借用矩形碰撞体
     *
     * @param centerX 中心 X 坐标
     * @param centerY 中心 Y 坐标
     * @param width 宽度
     * @param height 高度
     * @return 矩形碰撞体
     */
    public BoxCollider borrowBox(float centerX, float centerY, float width, float height) {
        ObjectPool<BoxCollider> pool = getPool(Collider.ColliderType.BOX);
        if (pool == null) {
            return new BoxCollider(centerX, centerY, width, height);
        }
        BoxCollider box = pool.borrow();
        if (box != null) {
            box.set(centerX, centerY, width, height);
        }
        return box;
    }

    /**
     * 借用矩形碰撞体（默认原点）
     *
     * @param width 宽度
     * @param height 高度
     * @return 矩形碰撞体
     */
    public BoxCollider borrowBox(float width, float height) {
        return borrowBox(0, 0, width, height);
    }

    /**
     * 借用圆形碰撞体
     *
     * @param centerX 中心 X 坐标
     * @param centerY 中心 Y 坐标
     * @param radius 半径
     * @return 圆形碰撞体
     */
    public CircleCollider borrowCircle(float centerX, float centerY, float radius) {
        ObjectPool<CircleCollider> pool = getPool(Collider.ColliderType.CIRCLE);
        if (pool == null) {
            return new CircleCollider(centerX, centerY, radius);
        }
        CircleCollider circle = pool.borrow();
        if (circle != null) {
            circle.set(centerX, centerY, radius);
        }
        return circle;
    }

    /**
     * 借用圆形碰撞体（默认原点）
     *
     * @param radius 半径
     * @return 圆形碰撞体
     */
    public CircleCollider borrowCircle(float radius) {
        return borrowCircle(0, 0, radius);
    }

    /**
     * 借用多边形碰撞体
     *
     * @param centerX 中心 X 坐标
     * @param centerY 中心 Y 坐标
     * @param vertices 顶点数组
     * @return 多边形碰撞体
     */
    public PolygonCollider borrowPolygon(float centerX, float centerY, java.util.List<io.github.s3s3l.yggdrasil.game.core.basic.Vector2> vertices) {
        ObjectPool<PolygonCollider> pool = getPool(Collider.ColliderType.POLYGON);
        if (pool == null) {
            return new PolygonCollider(centerX, centerY, vertices);
        }
        PolygonCollider polygon = pool.borrow();
        if (polygon != null) {
            polygon.set(centerX, centerY, vertices);
        }
        return polygon;
    }

    /**
     * 归还碰撞体
     *
     * @param collider 要归还的碰撞体
     */
    @SuppressWarnings("unchecked")
    public <T extends Collider & PoolableObject<T>> void returnCollider(T collider) {
        if (collider == null) {
            return;
        }

        ObjectPool<T> pool = (ObjectPool<T>) colliderPools.get(collider.getType());
        if (pool != null) {
            pool.returnObject(collider);
        }
        // 如果池不存在，对象将被 GC 回收
    }

    /**
     * 获取碰撞体池
     *
     * @param type 碰撞体类型
     * @return 对象池，不存在返回 null
     */
    @SuppressWarnings("unchecked")
    public <T extends Collider & PoolableObject<T>> ObjectPool<T> getPool(Collider.ColliderType type) {
        return (ObjectPool<T>) colliderPools.get(type);
    }

    /**
     * 获取碰撞体池统计信息
     *
     * @param type 碰撞体类型
     * @return 池统计信息，池不存在返回 null
     */
    public ObjectPool.PoolStats getStats(Collider.ColliderType type) {
        ObjectPool<? extends PoolableObject<?>> pool = colliderPools.get(type);
        if (pool != null) {
            return pool.getStats();
        }
        return null;
    }

    /**
     * 清空所有碰撞体池
     */
    public void clear() {
        for (ObjectPool<? extends PoolableObject<?>> pool : colliderPools.values()) {
            pool.clear();
        }
    }

    /**
     * 清空指定类型的碰撞体池
     *
     * @param type 碰撞体类型
     */
    @SuppressWarnings("unchecked")
    public void clear(Collider.ColliderType type) {
        ObjectPool<? extends PoolableObject<?>> pool = colliderPools.get(type);
        if (pool != null) {
            pool.clear();
        }
    }

    /**
     * 关闭所有碰撞体池
     */
    public void shutdown() {
        clear();
        colliderPools.clear();
    }

    // ==================== 工厂实现 ====================

    private static class BoxColliderFactory implements PoolableObjectFactory<BoxCollider> {
        @Override
        public BoxCollider create() {
            return new BoxCollider(0, 0, 1, 1);
        }

        @Override
        public void destroy(BoxCollider obj) {
            // BoxCollider 不需要特殊销毁逻辑
        }

        @Override
        public boolean validate(BoxCollider obj) {
            return obj != null;
        }
    }

    private static class CircleColliderFactory implements PoolableObjectFactory<CircleCollider> {
        @Override
        public CircleCollider create() {
            return new CircleCollider(0, 0, 1);
        }

        @Override
        public void destroy(CircleCollider obj) {
            // CircleCollider 不需要特殊销毁逻辑
        }

        @Override
        public boolean validate(CircleCollider obj) {
            return obj != null;
        }
    }

    private static class PolygonColliderFactory implements PoolableObjectFactory<PolygonCollider> {
        @Override
        public PolygonCollider create() {
            return new PolygonCollider(0, 0, new java.util.ArrayList<>());
        }

        @Override
        public void destroy(PolygonCollider obj) {
            // PolygonCollider 不需要特殊销毁逻辑
        }

        @Override
        public boolean validate(PolygonCollider obj) {
            return obj != null;
        }
    }
}
