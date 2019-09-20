package org.s3s3l.yggdrasil.cache.caffeine;

import java.util.concurrent.TimeUnit;

import org.s3s3l.yggdrasil.cache.key.ScopeKeyGenerator;
import org.s3s3l.yggdrasil.redis.base.IRedis;
import org.s3s3l.yggdrasil.utils.common.ObjectSerializer;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;

/**
 * <p>
 * </p>
 * ClassName:DefaultPartitionCaffeineCacheBuilder <br>
 * Date: Apr 27, 2018 4:01:43 PM <br>
 * 
 * @author kehw_zwei
 * @version 1.0.0
 * @since JDK 1.8
 */
public class DefaultPartitionCaffeineCacheBuilder implements PartitionCaffeineCacheBuilder<byte[], Object> {
    private final IRedis redis;

    public DefaultPartitionCaffeineCacheBuilder(IRedis redis) {
        this.redis = redis;
    }

    @Override
    public LoadingCache<byte[], Object> getCache(String scope) {
        return Caffeine.newBuilder()
                .expireAfterAccess(60, TimeUnit.SECONDS)
                .maximumSize(500)
                .build(key -> {
                    byte[] value = redis.hget(ScopeKeyGenerator.redisKeyGenerator.apply(scope), key);
                    return value == null ? null : ObjectSerializer.desrialize(value);
                });
    }

}
