package org.s3s3l.yggdrasil.utils.cache.helper;

import javax.annotation.Nonnull;

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

    V get(@Nonnull K key, @Nonnull String scope);

    void update(@Nonnull K key, @Nonnull V newValue, @Nonnull String scope);

    void expire(@Nonnull String scope);
}
