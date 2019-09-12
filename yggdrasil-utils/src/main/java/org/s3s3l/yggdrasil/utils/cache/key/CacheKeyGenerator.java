package org.s3s3l.yggdrasil.utils.cache.key;

/**
 * <p>
 * </p>
 * ClassName:CacheKeyGenerator <br>
 * Date: Sep 21, 2017 1:29:42 PM <br>
 * 
 * @author kehw_zwei
 * @version 1.0.0
 * @since JDK 1.8
 */
public interface CacheKeyGenerator<T> {

    T getKey(Object seed);
}
