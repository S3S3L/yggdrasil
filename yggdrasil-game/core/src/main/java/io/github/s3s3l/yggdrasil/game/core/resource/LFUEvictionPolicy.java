package io.github.s3s3l.yggdrasil.game.core.resource;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * LFU（最不常用）缓存清理策略
 * 清理访问次数最少的资源
 */
public class LFUEvictionPolicy<K, V> implements CacheEvictionPolicy<K, V> {

    /**
     * 单例实例
     */
    private static final LFUEvictionPolicy<?, ?> INSTANCE = new LFUEvictionPolicy<>();

    @SuppressWarnings("unchecked")
    public static <K, V> LFUEvictionPolicy<K, V> getInstance() {
        return (LFUEvictionPolicy<K, V>) INSTANCE;
    }

    private LFUEvictionPolicy() {
    }

    @Override
    public Collection<K> selectForEviction(CacheContext<K, V> cache, int count) {
        if (count <= 0) {
            return Collections.emptyList();
        }

        List<K> keys = new ArrayList<>(cache.keys());

        // 按访问次数排序
        keys.sort(Comparator.comparingInt(cache::getAccessCount));

        // 返回访问最少的键
        int toRemove = Math.min(count, keys.size());
        return keys.subList(0, toRemove);
    }
}
