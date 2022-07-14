package org.s3s3l.yggdrasil.cache.caffeine;

import java.nio.charset.StandardCharsets;

import org.s3s3l.yggdrasil.redis.base.IRedis;
import org.s3s3l.yggdrasil.utils.common.ObjectSerializer;
import org.s3s3l.yggdrasil.utils.verify.Verify;

import com.github.benmanes.caffeine.cache.CacheLoader;

/**
 * <p>
 * </p>
 * ClassName:RedisObjectCacheLoader <br>
 * Date: Apr 25, 2018 1:55:02 PM <br>
 * 
 * @author kehw_zwei
 * @version 1.0.0
 * @since JDK 1.8
 */
public class RedisObjectCacheLoader implements CacheLoader<byte[], Object> {

    private final IRedis redis;
    private final byte[] scope;

    public RedisObjectCacheLoader(IRedis redis, String scope) {
        Verify.notNull(redis);
        this.redis = redis;
        this.scope = scope.getBytes(StandardCharsets.UTF_8);
    }

    @Override
    public Object load(byte[] key) throws Exception {
        return ObjectSerializer.deserialize(redis.hget(scope, key));
    }

}
