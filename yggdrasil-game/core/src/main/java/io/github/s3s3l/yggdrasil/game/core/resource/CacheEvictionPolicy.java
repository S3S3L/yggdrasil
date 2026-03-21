package io.github.s3s3l.yggdrasil.game.core.resource;

import java.util.Collection;

/**
 * 缓存清理策略接口
 * 用于决定哪些资源应该被清理
 *
 * @param <K> 缓存键类型
 * @param <V> 缓存值类型
 */
public interface CacheEvictionPolicy<K, V> {

    /**
     * 选择要清理的资源
     *
     * @param cache 当前缓存内容
     * @param count 需要清理的数量
     * @return 要清理的键集合
     */
    Collection<K> selectForEviction(CacheContext<K, V> cache, int count);

    /**
     * 缓存上下文接口
     * 提供缓存的访问和统计信息
     */
    interface CacheContext<K, V> {
        /**
         * 获取缓存的值
         */
        V get(K key);

        /**
         * 获取缓存的键集合
         */
        Collection<K> keys();

        /**
         * 获取值的访问次数
         */
        int getAccessCount(K key);

        /**
         * 获取值的最后访问时间戳
         */
        long getLastAccessTime(K key);

        /**
         * 获取值的创建时间戳
         */
        long getCreationTime(K key);

        /**
         * 获取值的大小（如果支持）
         */
        long getSize(K key);
    }
}
