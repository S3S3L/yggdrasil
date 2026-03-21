package io.github.s3s3l.yggdrasil.game.core.resource;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * LRU（最近最少使用）缓存清理策略
 * 清理最长时间未被访问的资源
 */
public class LRUEvictionPolicy<K, V> implements CacheEvictionPolicy<K, V> {

    /**
     * 单例实例
     */
    private static final LRUEvictionPolicy<?, ?> INSTANCE = new LRUEvictionPolicy<>();

    @SuppressWarnings("unchecked")
    public static <K, V> LRUEvictionPolicy<K, V> getInstance() {
        return (LRUEvictionPolicy<K, V>) INSTANCE;
    }

    private LRUEvictionPolicy() {
    }

    @Override
    public Collection<K> selectForEviction(CacheContext<K, V> cache, int count) {
        if (count <= 0) {
            return Collections.emptyList();
        }

        List<K> keys = new ArrayList<>(cache.keys());

        // 按最后访问时间排序
        keys.sort(Comparator.comparingLong(cache::getLastAccessTime));

        // 返回最久未访问的键
        int toRemove = Math.min(count, keys.size());
        return keys.subList(0, toRemove);
    }
}
