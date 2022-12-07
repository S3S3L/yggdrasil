package io.github.s3s3l.yggdrasil.cache;

import io.github.s3s3l.yggdrasil.cache.compressor.Compressable;

/**
 * <p>
 * </p> 
 * ClassName:CacheHolder <br> 
 * Date:     Apr 4, 2019 10:31:28 AM <br>
 *  
 * @author   kehw_zwei 
 * @version  1.0.0
 * @since    JDK 1.8
 */
public interface CacheHolder<K, V> extends Compressable {

    /**
     * 
     * Get cache from specific key in default scope.
     * 
     * @param key
     *            cache key
     * @return cached data
     * @since JDK 1.8
     */
    V get(K key);

    /**
     * 
     * Put cache into specific key in default scope.
     * 
     * @param key
     *            cache key
     * @param value
     *            data need to be cached
     * @since JDK 1.8
     */
    void put(K key, V value);

    /**
     * 
     * Invalidate all cached data.
     * 
     * @since JDK 1.8
     */
    void invalidateAll();

    /**
     * 
     * Refresh all cached data.
     *  
     * @since JDK 1.8
     */
    void refreshAll();

}
