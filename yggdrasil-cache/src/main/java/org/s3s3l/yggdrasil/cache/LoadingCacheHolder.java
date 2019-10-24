package org.s3s3l.yggdrasil.cache;

/**
 * <p>
 * </p>
 * ClassName:CacheHelper <br>
 * Date: Sep 21, 2017 1:33:07 PM <br>
 * 
 * @author kehw_zwei
 * @version 1.0.0
 * @since JDK 1.8
 */
public interface LoadingCacheHolder<K, V> extends ScopeCacheHolder<K, V> {

    /**
     * 
     * Update local cache and remote cache.
     * 
     * @param scope
     * @param key
     * @param value 
     * @since JDK 1.8
     */
    void updateCache(String scope, K key, V value);
    
}
