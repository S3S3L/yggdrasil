
package org.s3s3l.yggdrasil.utils.cache;

/**
 * <p>
 * </p> 
 * ClassName:ScopeCacheHolder <br> 
 * Date:     Apr 4, 2019 10:29:51 AM <br>
 *  
 * @author   kehw_zwei 
 * @version  1.0.0
 * @since    JDK 1.8
 */
public interface ScopeCacheHolder<K, V> extends CacheHolder<K, V> {

    /**
     * 
     * Get cache from specific key in provided scope.
     * 
     * @param key
     *            cache key
     * @param scope
     *            cache scope
     * @return cached data
     * @since JDK 1.8
     */
    V get(K key, String scope);

    /**
     * 
     * Put cache into specific key in provided scope.
     * 
     * @param key
     *            cache key
     * @param value
     *            data need to be cached
     * @param scope
     *            cache scope
     * @since JDK 1.8
     */
    void put(K key, V value, String scope);

    /**
     * 
     * Invalidate specific key in provided scope.
     * 
     * @param key
     *            cache key
     * @param scope
     *            cache scope
     * @since JDK 1.8
     */
    void invalidate(K key, String scope);

    /**
     * 
     * Invalidate provided scope.
     * 
     * @param scope
     *            cache scope
     * @since JDK 1.8
     */
    void invalidate(String scope);

    /**
     * 
     * Refresh specific key in provided scope.
     * 
     * @param key
     *            cache key
     * @param scope
     *            cache scope
     * @since JDK 1.8
     */
    void refresh(K key, String scope);

    /**
     * 
     * Refresh provided scope.
     * 
     * @param scope
     *            cache scope
     * @since JDK 1.8
     */
    void refresh(String scope);

}
