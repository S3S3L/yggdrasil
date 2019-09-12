package org.s3s3l.yggdrasil.utils.cache.helper;

import java.util.function.Function;

import com.fasterxml.jackson.core.type.TypeReference;

/**
 * <p>
 * </p>
 * ClassName:ICacheHelper <br>
 * Date: Aug 8, 2017 5:09:14 PM <br>
 * 
 * @author kehw_zwei
 * @version 1.0.0
 * @since JDK 1.8
 */
public interface CacheHelper<K> {

    <T, U> T tryCache(K key, Function<U, T> call, U condition, Class<T> resultType, Class<U> conditionType);

    <T, U> T tryCache(K key,
            Function<U, T> call,
            U condition,
            Class<T> resultType,
            Class<U> conditionType,
            long expire);

    <T, U> T tryCache(K key,
            Function<U, T> call,
            U condition,
            Class<T> resultType,
            Class<U> conditionType,
            Function<T, Long> expireByResult);

    <T, U> T tryCache(K key,
            Function<U, T> call,
            U condition,
            TypeReference<T> resultType,
            TypeReference<U> conditionType);
}
