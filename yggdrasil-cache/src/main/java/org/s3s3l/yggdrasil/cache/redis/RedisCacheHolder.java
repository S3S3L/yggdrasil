package org.s3s3l.yggdrasil.cache.redis;

import java.util.function.Supplier;

import org.s3s3l.yggdrasil.cache.checker.VersionChecker;
import org.s3s3l.yggdrasil.redis.base.IRedis;

/**
 * <p>
 * </p>
 * ClassName:IRedisCacheHolder <br>
 * Date: May 16, 2019 8:15:23 PM <br>
 * 
 * @author kehw_zwei
 * @version 1.0.0
 * @since JDK 1.8
 */
public class RedisCacheHolder extends AbstractRedisCacheHolder {
    private final IRedis redis;

    public RedisCacheHolder(IRedis redis, VersionChecker versionChecker) {
        super(versionChecker);
        this.redis = redis;
    }

    public RedisCacheHolder(IRedis redis, VersionChecker versionChecker, String prefix) {
        super(versionChecker, prefix);
        this.redis = redis;
    }

    public RedisCacheHolder(IRedis redis, Supplier<VersionChecker> versionCheckerSupplier) {
        super(versionCheckerSupplier.get());
        this.redis = redis;
    }

    public RedisCacheHolder(IRedis redis, Supplier<VersionChecker> versionCheckerSupplier, String prefix) {
        super(versionCheckerSupplier.get(), prefix);
        this.redis = redis;
    }

    @Override
    protected byte[] getBytes(byte[] keyBuf) {
        return this.redis.getBytes(keyBuf);
    }

    @Override
    protected void set(byte[] keyBuf, byte[] value) {
        this.redis.set(keyBuf, value);
    }

    @Override
    protected void set(byte[] keyBuf, byte[] value, int seconds) {
        this.redis.set(keyBuf, value, seconds);
    }

    @Override
    protected void expire(byte[] keyBuf, int seconds) {
        this.redis.expire(keyBuf, seconds);
    }

    @Override
    protected void del(byte[] keyBuf) {
        this.redis.del(keyBuf);
    }

    @Override
    protected void del(String keyBuf) {
        this.redis.del(keyBuf);
    }

}
