package io.github.s3s3l.yggdrasil.game.core.pool;

import io.github.s3s3l.yggdrasil.game.core.event.Event;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 事件对象池
 * 统一管理各类事件的池化，减少事件对象创建和销毁的开销
 */
public class EventPool {

    /**
     * 单例实例
     */
    private static final EventPool INSTANCE = new EventPool();

    /**
     * 事件池映射表（事件类型 -> 对象池）
     */
    private final Map<Class<? extends Event>, ObjectPool<? extends PoolableObject<?>>> eventPools;

    /**
     * 默认初始池大小
     */
    private static final int DEFAULT_INITIAL_POOL_SIZE = 10;

    /**
     * 默认最大池大小
     */
    private static final int DEFAULT_MAX_POOL_SIZE = 50;

    private EventPool() {
        this.eventPools = new ConcurrentHashMap<>();
    }

    /**
     * 获取事件池实例
     *
     * @return 事件池单例
     */
    public static EventPool get() {
        return INSTANCE;
    }

    /**
     * 注册事件类型到对象池
     *
     * @param eventType 事件类型
     * @param factory 事件对象工厂
     * @param <T> 事件类型
     */
    public <T extends Event & PoolableObject<T>> void registerEvent(Class<T> eventType, PoolableObjectFactory<T> factory) {
        registerEvent(eventType, factory, DEFAULT_INITIAL_POOL_SIZE, DEFAULT_MAX_POOL_SIZE);
    }

    /**
     * 注册事件类型到对象池（指定池大小）
     *
     * @param eventType 事件类型
     * @param factory 事件对象工厂
     * @param initialSize 初始池大小
     * @param maxSize 最大池大小
     * @param <T> 事件类型
     */
    public <T extends Event & PoolableObject<T>> void registerEvent(Class<T> eventType,
                                                                     PoolableObjectFactory<T> factory,
                                                                     int initialSize,
                                                                     int maxSize) {
        ObjectPool<T> pool = new ObjectPool<>(factory, initialSize, maxSize);
        eventPools.put(eventType, pool);
    }

    /**
     * 借用事件对象
     *
     * @param eventType 事件类型
     * @return 事件对象，如果池耗尽则返回 null
     */
    @SuppressWarnings("unchecked")
    public <T extends Event & PoolableObject<T>> T borrowEvent(Class<T> eventType) {
        ObjectPool<T> pool = (ObjectPool<T>) eventPools.get(eventType);
        if (pool == null) {
            // 池未注册，直接创建新对象
            return createNewEvent(eventType);
        }
        return pool.borrow();
    }

    /**
     * 归还事件对象
     *
     * @param event 要归还的事件对象
     * @param <T> 事件类型
     */
    @SuppressWarnings("unchecked")
    public <T extends Event & PoolableObject<T>> void returnEvent(T event) {
        if (event == null) {
            return;
        }

        Class<? extends Event> eventType = event.getClass();
        ObjectPool<T> pool = (ObjectPool<T>) eventPools.get(eventType);

        if (pool != null) {
            pool.returnObject(event);
        }
        // 如果池不存在，对象将被 GC 回收
    }

    /**
     * 创建新的事件对象（用于未注册池的事件类型）
     *
     * @param eventType 事件类型
     * @return 新创建的事件对象
     */
    private <T extends Event & PoolableObject<T>> T createNewEvent(Class<T> eventType) {
        try {
            return eventType.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException("Failed to create event: " + eventType.getName(), e);
        }
    }

    /**
     * 获取事件池统计信息
     *
     * @param eventType 事件类型
     * @return 池统计信息，池不存在返回 null
     */
    @SuppressWarnings("unchecked")
    public ObjectPool.PoolStats getStats(Class<? extends Event> eventType) {
        ObjectPool<? extends PoolableObject<?>> pool = eventPools.get(eventType);
        if (pool != null) {
            return pool.getStats();
        }
        return null;
    }

    /**
     * 获取已注册的事件类型数量
     *
     * @return 事件类型数量
     */
    public int getRegisteredEventCount() {
        return eventPools.size();
    }

    /**
     * 清空所有事件池
     */
    public void clear() {
        for (ObjectPool<? extends PoolableObject<?>> pool : eventPools.values()) {
            pool.clear();
        }
    }

    /**
     * 清空指定事件类型的池
     *
     * @param eventType 事件类型
     */
    @SuppressWarnings("unchecked")
    public void clear(Class<? extends Event> eventType) {
        ObjectPool<? extends PoolableObject<?>> pool = eventPools.get(eventType);
        if (pool != null) {
            pool.clear();
        }
    }

    /**
     * 关闭所有事件池
     */
    public void shutdown() {
        clear();
        eventPools.clear();
    }
}
