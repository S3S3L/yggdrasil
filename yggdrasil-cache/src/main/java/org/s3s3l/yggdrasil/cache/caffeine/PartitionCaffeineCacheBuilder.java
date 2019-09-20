package org.s3s3l.yggdrasil.cache.caffeine;

import com.github.benmanes.caffeine.cache.LoadingCache;

/**
 * <p>
 * </p>
 * ClassName:PartitionCaffeineCacheBuilder <br>
 * Date: Apr 25, 2018 3:43:56 PM <br>
 * 
 * @author kehw_zwei
 * @version 1.0.0
 * @since JDK 1.8
 */
@FunctionalInterface
public interface PartitionCaffeineCacheBuilder<K, V> {

    LoadingCache<K, V> getCache(String scope);
}
