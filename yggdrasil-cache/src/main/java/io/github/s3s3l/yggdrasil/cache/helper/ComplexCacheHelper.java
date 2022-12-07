package io.github.s3s3l.yggdrasil.cache.helper;

/**
 * <p>
 * </p>
 * ClassName:TowLevelCacheHelper <br>
 * Date: Apr 24, 2018 4:44:41 PM <br>
 * 
 * @author kehw_zwei
 * @version 1.0.0
 * @since JDK 1.8
 */
public interface ComplexCacheHelper<K, V> {

    V get(K key, String scope);

    void update(K key, V newValue, String scope);

    void expire(String scope);
}
