package io.github.s3s3l.yggdrasil.cache.helper;

import java.util.function.Function;

import io.github.s3s3l.yggdrasil.redis.base.IRedis;
import io.github.s3s3l.yggdrasil.utils.common.StringUtils;
import io.github.s3s3l.yggdrasil.utils.log.base.LogHelper;
import io.github.s3s3l.yggdrasil.utils.stuctural.jackson.JacksonHelper;
import io.github.s3s3l.yggdrasil.utils.stuctural.jackson.JacksonUtils;
import io.github.s3s3l.yggdrasil.utils.verify.Verify;

import com.fasterxml.jackson.core.type.TypeReference;

/**
 * <p>
 * </p>
 * ClassName:RedisCacheHelper <br>
 * Date: Aug 9, 2017 1:43:48 PM <br>
 * 
 * @author kehw_zwei
 * @version 1.0.0
 * @since JDK 1.8
 */
public class RedisCacheHelper implements CacheHelper<String> {

    private IRedis redis;
    private JacksonHelper json = JacksonUtils.JSON;

    public RedisCacheHelper(IRedis redis) {
        Verify.notNull(redis);
        this.redis = redis;
    }

    @Override
    public <T, U>
            T
            tryCache(String key, Function<U, T> call, U condition, Class<T> resultType, Class<U> conditionType) {
        return tryCache(key, call, condition, resultType, conditionType, 0);
    }

    @Override
    public <T, U> T tryCache(String key,
            Function<U, T> call,
            U condition,
            Class<T> resultType,
            Class<U> conditionType,
            long expire) {
        return tryCache(key, call, condition, resultType, conditionType, result -> expire);
    }

    @Override
    public <T, U> T tryCache(String key,
            Function<U, T> call,
            U condition,
            TypeReference<T> resultType,
            TypeReference<U> conditionType) {
        String redisData = redis.get(key);
        if (!StringUtils.isEmpty(redisData)) {
            try {
                return json.toObject(redisData, resultType);
            } catch (Exception e) {
                LogHelper.getLogger(RedisCacheHelper.class)
                        .warn("Fail to deserialize cache data: {} to type {}", redisData, resultType);
            }
        }

        T resultData = call.apply(condition);
        if (resultData != null) {
            redis.set(key, json.toStructuralString(resultData));
        }
        return resultData;
    }

    @Override
    public <T, U> T tryCache(String key,
            Function<U, T> call,
            U condition,
            Class<T> resultType,
            Class<U> conditionType,
            Function<T, Long> expireByResult) {
        String redisData = redis.get(key);
        if (!StringUtils.isEmpty(redisData)) {
            try {
                return json.toObject(redisData, resultType);
            } catch (Exception e) {
                LogHelper.getLogger(RedisCacheHelper.class)
                        .warn("Fail to deserialize cache data: {} to type {}", redisData, resultType.getName());
            }
        }

        T resultData = call.apply(condition);
        if (resultData != null) {
            long expire = expireByResult.apply(resultData);
            if (expire > 0) {
                redis.set(key, json.toStructuralString(resultData), expire);
            } else {
                redis.set(key, json.toStructuralString(resultData));
            }
        }
        return resultData;
    }

}
