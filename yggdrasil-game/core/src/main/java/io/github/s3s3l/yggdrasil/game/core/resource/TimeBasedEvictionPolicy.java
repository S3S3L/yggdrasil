package io.github.s3s3l.yggdrasil.game.core.resource;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * 基于时间的缓存清理策略
 * 清理超过指定时间的资源
 */
public class TimeBasedEvictionPolicy<K, V> implements CacheEvictionPolicy<K, V> {

    /**
     * 最大存活时间（毫秒）
     */
    private final long maxAgeMillis;

    /**
     * 创建基于时间的清理策略
     *
     * @param maxAgeMillis 最大存活时间（毫秒）
     */
    public TimeBasedEvictionPolicy(long maxAgeMillis) {
        this.maxAgeMillis = maxAgeMillis;
    }

    @Override
    public Collection<K> selectForEviction(CacheContext<K, V> cache, int count) {
        if (count <= 0) {
            return Collections.emptyList();
        }

        long now = System.currentTimeMillis();
        List<K> keysToEvict = new ArrayList<>();

        for (K key : cache.keys()) {
            long age = now - cache.getCreationTime(key);
            if (age > maxAgeMillis) {
                keysToEvict.add(key);
                if (keysToEvict.size() >= count) {
                    break;
                }
            }
        }

        return keysToEvict;
    }

    /**
     * 创建策略（指定存活时间）
     *
     * @param seconds 存活时间（秒）
     * @return 策略实例
     */
    public static <K, V> TimeBasedEvictionPolicy<K, V> withTTL(int seconds) {
        return new TimeBasedEvictionPolicy<>(seconds * 1000L);
    }

    /**
     * 创建策略（指定存活时间）
     *
     * @param minutes 存活时间（分钟）
     * @return 策略实例
     */
    public static <K, V> TimeBasedEvictionPolicy<K, V> withTTLMMinutes(int minutes) {
        return new TimeBasedEvictionPolicy<>(minutes * 60L * 1000L);
    }
}
