package io.github.s3s3l.yggdrasil.cache.redis;

import java.nio.charset.StandardCharsets;

import io.github.s3s3l.yggdrasil.cache.RemoteCacheHolder;
import io.github.s3s3l.yggdrasil.cache.checker.VersionChecker;
import io.github.s3s3l.yggdrasil.cache.compressor.CompressConfiguration;
import io.github.s3s3l.yggdrasil.cache.exception.CacheOperationException;
import io.github.s3s3l.yggdrasil.compress.Compressor;
import io.github.s3s3l.yggdrasil.utils.collection.ArrayHelper;
import io.github.s3s3l.yggdrasil.utils.common.TypeUtils;
import io.github.s3s3l.yggdrasil.utils.verify.Verify;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>
 * </p>
 * ClassName:RedisCacheHolder <br>
 * Date: Apr 8, 2019 10:09:26 AM <br>
 * 
 * @author kehw_zwei
 * @version 1.0.0
 * @since JDK 1.8
 */
public abstract class AbstractRedisCacheHolder implements RemoteCacheHolder<byte[], byte[]> {
    private static final String MSG_OPERATION_NOT_SUPPORT = "Operation not support.";
    /**
     * 老版本中的缓存前缀，由于兼容性原因，触发缓存失效的时候需要清理老版本的缓存数据
     */
    private static final String OLD_CACHE_PREFIX = "CACHE";
    private static final String DEFAULT_SCOPE = "d";
    private static final byte[] DELIMITER = new byte[] { ':' };

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final byte[] prefix;
    private final VersionChecker versionChecker;

    protected boolean isCompressKey = false;
    protected boolean isCompressValue = false;
    protected boolean isExpireAfterAccess = false;
    protected boolean isExpireAfterWrite = false;
    protected Compressor keyCompressor;
    protected Compressor valueCompressor;
    protected int expireAfterAccess = -1;
    protected int expireAfterWrite = -1;

    public AbstractRedisCacheHolder(VersionChecker versionChecker) {
        this.versionChecker = versionChecker;
        this.prefix = "c".getBytes(StandardCharsets.UTF_8);
    }

    public AbstractRedisCacheHolder(VersionChecker versionChecker, String prefix) {
        this.versionChecker = versionChecker;
        this.prefix = prefix.getBytes(StandardCharsets.UTF_8);
    }

    protected byte[] getKey(byte[] originKey) {
        return getKey(originKey, DEFAULT_SCOPE);
    }

    protected byte[] getKey(byte[] originKey, String scope) {
        byte[] scopeBuf = scope.getBytes(StandardCharsets.UTF_8);
        byte[] versionBuf = TypeUtils.longToBytes(versionChecker.localVersion(scope));
        return ArrayHelper.join(DELIMITER, prefix, scopeBuf, versionBuf, compressKey(originKey));
    }
    
    protected abstract byte[] getBytes(byte[] keyBuf);

    protected abstract void set(byte[] keyBuf, byte[] value);

    protected abstract void set(byte[] keyBuf, byte[] value, int seconds);
    
    protected abstract void expire(byte[] keyBuf, int seconds);
    
    protected abstract void del(byte[] keyBuf);
    
    protected abstract void del(String keyBuf);

    @Override
    public byte[] get(byte[] key) {
        return get(key, DEFAULT_SCOPE);
    }

    @Override
    public void put(byte[] key, byte[] value) {
        put(key, value, DEFAULT_SCOPE);
    }

    @Override
    public byte[] get(byte[] key, String scope) {
        logger.trace("fetching cache from remoteCacheHelper. key: {}", key);
        byte[] keyBuf = getKey(key, scope);
        byte[] value = decompressValue(getBytes(keyBuf));

        if (isExpireAfterAccess) {
            expire(key, expireAfterAccess);
        }
        return value;
    }

    @Override
    public void put(byte[] key, byte[] value, String scope) {
        logger.trace("putting cache from remoteCacheHelper. key: {}", key);
        if (isExpireAfterWrite) {
            set(getKey(key, scope), compressValue(value), expireAfterWrite);
        } else {
            set(getKey(key, scope), compressValue(value));
        }
    }

    @Override
    public void invalidate(byte[] key, String scope) {
        del(getKey(key, scope));
    }

    @Override
    public void refresh(byte[] key, String scope) {
        del(getKey(key, scope));
    }

    /**
     * 
     * @deprecated 不支持的操作
     * @see io.github.s3s3l.yggdrasil.utils.cache.ScopeCacheHolder#invalidate(java.lang.String)
     */
    @Deprecated
    @Override
    public void invalidate(String scope) {
        throw new CacheOperationException(MSG_OPERATION_NOT_SUPPORT);
    }

    /**
     * 
     * @deprecated 不支持的操作
     * @see io.github.s3s3l.yggdrasil.utils.cache.CacheHolder#invalidateAll()
     */
    @Deprecated
    @Override
    public void invalidateAll() {
        throw new CacheOperationException(MSG_OPERATION_NOT_SUPPORT);
    }

    /**
     * 
     * @deprecated 不支持的操作
     * @see io.github.s3s3l.yggdrasil.utils.cache.CacheHolder#refreshAll()
     */
    @Deprecated
    @Override
    public void refreshAll() {
        throw new CacheOperationException(MSG_OPERATION_NOT_SUPPORT);
    }

    /**
     * 
     * @see io.github.s3s3l.yggdrasil.utils.cache.ScopeCacheHolder#refresh(java.lang.String)
     */
    @Override
    public void refresh(String scope) {
        del(String.join(":", OLD_CACHE_PREFIX, scope));
    }

    @Override
    public void configCompress(CompressConfiguration config) {
        Verify.notNull(config);
        Verify.notNull(config.getCompressorSupplier());
        this.isCompressKey = config.getProp()
                .isCompressKey();
        this.isCompressValue = config.getProp()
                .isCompressValue();
        if (config.getProp()
                .isCompressKey()) {
            Verify.notNull(config.getProp()
                    .getKeyConfig());
            this.keyCompressor = config.getCompressorSupplier()
                    .get()
                    .config(config.getProp()
                            .getKeyConfig());
        }
        if (config.getProp()
                .isCompressValue()) {
            Verify.notNull(config.getProp()
                    .getValueConfig());
            this.valueCompressor = config.getCompressorSupplier()
                    .get()
                    .config(config.getProp()
                            .getValueConfig());
        }
    }

    protected byte[] compressKey(byte[] key) {
        return isCompressKey ? keyCompressor.compress(key) : key;
    }

    protected byte[] compressValue(byte[] value) {
        return isCompressValue ? valueCompressor.compress(value) : value;
    }

    protected byte[] decompressValue(byte[] value) {
        return isCompressValue ? valueCompressor.decompress(value) : value;
    }

    @Override
    public RemoteCacheHolder<byte[], byte[]> expireAfterAccess(int seconds) {
        if (seconds > 0) {
            this.isExpireAfterAccess = true;
            this.expireAfterAccess = seconds;
        }
        return this;
    }

    @Override
    public RemoteCacheHolder<byte[], byte[]> expireAfterWrite(int seconds) {
        if (seconds > 0) {
            this.isExpireAfterWrite = true;
            this.expireAfterWrite = seconds;
        }
        return this;
    }
}
