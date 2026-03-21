package io.github.s3s3l.yggdrasil.game.core.resource;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 增强的缓存管理器
 * 支持多种缓存清理策略和统计信息
 *
 * @param <K> 缓存键类型
 * @param <V> 缓存值类型
 */
public class EnhancedCacheManager<K, V> {

    /**
     * 缓存数据
     */
    private final Map<K, CacheEntry<V>> cache;

    /**
     * 访问计数映射
     */
    private final Map<K, AtomicLong> accessCounts;

    /**
     * 清理策略
     */
    private CacheEvictionPolicy<K, V> evictionPolicy;

    /**
     * 最大缓存大小
     */
    private int maxSize;

    /**
     * 是否启用统计
     */
    private boolean statsEnabled;

    /**
     * 缓存统计信息
     */
    private CacheStats stats;

    /**
     * 创建缓存管理器
     *
     * @param maxSize 最大缓存大小
     */
    public EnhancedCacheManager(int maxSize) {
        this(maxSize, LRUEvictionPolicy.getInstance());
    }

    /**
     * 创建缓存管理器
     *
     * @param maxSize  最大缓存大小
     * @param policy   清理策略
     */
    public EnhancedCacheManager(int maxSize, CacheEvictionPolicy<K, V> policy) {
        this.cache = new ConcurrentHashMap<>();
        this.accessCounts = new ConcurrentHashMap<>();
        this.maxSize = maxSize;
        this.evictionPolicy = policy;
        this.statsEnabled = true;
        this.stats = new CacheStats();
    }

    /**
     * 获取缓存的值
     *
     * @param key 键
     * @return 缓存的值，不存在返回 null
     */
    public V get(K key) {
        CacheEntry<V> entry = cache.get(key);
        if (entry == null) {
            stats.recordMiss();
            return null;
        }

        // 更新访问统计
        accessCounts.computeIfAbsent(key, k -> new AtomicLong(0)).incrementAndGet();
        entry.recordAccess();
        stats.recordHit();

        return entry.value;
    }

    /**
     * 放入缓存
     *
     * @param key   键
     * @param value 值
     */
    public void put(K key, V value) {
        // 如果已存在，更新值
        if (cache.containsKey(key)) {
            cache.put(key, new CacheEntry<>(value));
            return;
        }

        // 检查是否需要清理
        while (cache.size() >= maxSize && maxSize > 0) {
            evict();
        }

        cache.put(key, new CacheEntry<>(value));
        stats.recordPut();
    }

    /**
     * 从缓存移除
     *
     * @param key 键
     * @return 被移除的值，不存在返回 null
     */
    public V remove(K key) {
        CacheEntry<V> entry = cache.remove(key);
        accessCounts.remove(key);
        if (entry != null) {
            stats.recordRemove();
            return entry.value;
        }
        return null;
    }

    /**
     * 清空缓存
     */
    public void clear() {
        cache.clear();
        accessCounts.clear();
        stats.recordClear();
    }

    /**
     * 获取缓存大小
     *
     * @return 缓存条目数
     */
    public int size() {
        return cache.size();
    }

    /**
     * 设置最大缓存大小
     *
     * @param maxSize 最大缓存大小
     */
    public void setMaxSize(int maxSize) {
        this.maxSize = Math.max(0, maxSize);
        // 如果当前大小超过新限制，执行清理
        while (cache.size() > this.maxSize && this.maxSize > 0) {
            evict();
        }
    }

    /**
     * 获取最大缓存大小
     *
     * @return 最大缓存大小
     */
    public int getMaxSize() {
        return maxSize;
    }

    /**
     * 设置清理策略
     *
     * @param policy 清理策略
     */
    public void setEvictionPolicy(CacheEvictionPolicy<K, V> policy) {
        this.evictionPolicy = policy;
    }

    /**
     * 获取清理策略
     *
     * @return 清理策略
     */
    public CacheEvictionPolicy<K, V> getEvictionPolicy() {
        return evictionPolicy;
    }

    /**
     * 执行一次清理
     */
    public void evict() {
        evict(1);
    }

    /**
     * 清理指定数量的条目
     *
     * @param count 清理数量
     */
    public void evict(int count) {
        if (count <= 0 || cache.isEmpty()) {
            return;
        }

        CacheContextImpl context = new CacheContextImpl();
        Collection<K> keysToEvict = evictionPolicy.selectForEviction(context, count);

        for (K key : keysToEvict) {
            cache.remove(key);
            accessCounts.remove(key);
            stats.recordEviction();
        }
    }

    /**
     * 获取缓存统计信息
     *
     * @return 缓存统计信息
     */
    public CacheStats getStats() {
        return stats;
    }

    /**
     * 启用/禁用统计
     *
     * @param enabled true 为启用
     */
    public void setStatsEnabled(boolean enabled) {
        this.statsEnabled = enabled;
        if (enabled && stats == null) {
            stats = new CacheStats();
        }
    }

    /**
     * 检查是否包含键
     *
     * @param key 键
     * @return true 如果包含
     */
    public boolean containsKey(K key) {
        return cache.containsKey(key);
    }

    /**
     * 获取所有键
     *
     * @return 键集合
     */
    public Collection<K> keys() {
        return Collections.unmodifiableSet(cache.keySet());
    }

    /**
     * 缓存条目类
     */
    private static class CacheEntry<V> {
        final V value;
        final long creationTime;
        volatile long lastAccessTime;

        CacheEntry(V value) {
            this.value = value;
            this.creationTime = System.currentTimeMillis();
            this.lastAccessTime = this.creationTime;
        }

        void recordAccess() {
            this.lastAccessTime = System.currentTimeMillis();
        }
    }

    /**
     * 缓存上下文实现（用于清理策略）
     */
    private class CacheContextImpl implements CacheEvictionPolicy.CacheContext<K, V> {
        @Override
        public V get(K key) {
            CacheEntry<V> entry = cache.get(key);
            return entry != null ? entry.value : null;
        }

        @Override
        public Collection<K> keys() {
            return cache.keySet();
        }

        @Override
        public int getAccessCount(K key) {
            AtomicLong count = accessCounts.get(key);
            return count != null ? count.intValue() : 0;
        }

        @Override
        public long getLastAccessTime(K key) {
            CacheEntry<V> entry = cache.get(key);
            return entry != null ? entry.lastAccessTime : 0;
        }

        @Override
        public long getCreationTime(K key) {
            CacheEntry<V> entry = cache.get(key);
            return entry != null ? entry.creationTime : 0;
        }

        @Override
        public long getSize(K key) {
            // 默认不支持大小
            return -1;
        }
    }

    /**
     * 缓存统计信息
     */
    public static class CacheStats {
        private final AtomicLong hits = new AtomicLong(0);
        private final AtomicLong misses = new AtomicLong(0);
        private final AtomicLong puts = new AtomicLong(0);
        private final AtomicLong removes = new AtomicLong(0);
        private final AtomicLong evictions = new AtomicLong(0);
        private final AtomicLong clears = new AtomicLong(0);

        void recordHit() {
            hits.incrementAndGet();
        }

        void recordMiss() {
            misses.incrementAndGet();
        }

        void recordPut() {
            puts.incrementAndGet();
        }

        void recordRemove() {
            removes.incrementAndGet();
        }

        void recordEviction() {
            evictions.incrementAndGet();
        }

        void recordClear() {
            clears.incrementAndGet();
        }

        /**
         * 获取命中次数
         */
        public long getHits() {
            return hits.get();
        }

        /**
         * 获取未命中次数
         */
        public long getMisses() {
            return misses.get();
        }

        /**
         * 获取放入次数
         */
        public long getPuts() {
            return puts.get();
        }

        /**
         * 获取移除次数
         */
        public long getRemoves() {
            return removes.get();
        }

        /**
         * 获取清理次数
         */
        public long getEvictions() {
            return evictions.get();
        }

        /**
         * 获取清空次数
         */
        public long getClears() {
            return clears.get();
        }

        /**
         * 获取命中率
         */
        public double getHitRate() {
            long total = hits.get() + misses.get();
            return total > 0 ? (double) hits.get() / total : 0.0;
        }

        @Override
        public String toString() {
            return String.format("CacheStats[hits=%d, misses=%d, puts=%d, removes=%d, evictions=%d, hitRate=%.2f%%]",
                    hits.get(), misses.get(), puts.get(), removes.get(), evictions.get(),
                    getHitRate() * 100);
        }
    }
}
