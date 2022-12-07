package io.github.s3s3l.yggdrasil.cache.checker;

import io.github.s3s3l.yggdrasil.redis.base.IRedis;

/**
 * <p>
 * </p> 
 * ClassName:RedisCacheChecker <br> 
 * Date:     May 16, 2019 9:53:38 PM <br>
 *  
 * @author   kehw_zwei 
 * @version  1.0.0
 * @since    JDK 1.8
 */
public class RedisCacheChecker extends AbstractRedisCacheChecker {
    private final IRedis redis;
    
    public RedisCacheChecker(IRedis redis) {
        this.redis = redis;
    }

    @Override
    protected String get(String key) {
        return redis.get(key);
    }

    @Override
    protected Object updateAndGet(String key) {
        String lua = SYNC_LAST_UPDATE.replaceAll("\\{key\\}", key);
        return redis.eval(lua, key);
    }

}
  