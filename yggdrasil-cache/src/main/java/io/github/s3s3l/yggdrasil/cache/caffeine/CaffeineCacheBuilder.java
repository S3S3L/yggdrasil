package io.github.s3s3l.yggdrasil.cache.caffeine;

import com.github.benmanes.caffeine.cache.LoadingCache;

/**
 * <p>
 * </p>
 * ClassName:CaffeineCacheBuilder <br>
 * Date: Sep 21, 2017 2:22:55 PM <br>
 * 
 * @author kehw_zwei
 * @version 1.0.0
 * @since JDK 1.8
 */
public interface CaffeineCacheBuilder<K, V> {

    LoadingCache<K, V> getCache();
}
