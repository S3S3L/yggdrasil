package org.s3s3l.yggdrasil.utils.cache.helper;

import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

import org.s3s3l.yggdrasil.utils.cache.LoadingCacheHolder;
import org.s3s3l.yggdrasil.utils.cache.RemoteCacheHolder;
import org.s3s3l.yggdrasil.utils.cache.caffeine.CompressedCaffeineCacheHolder;
import org.s3s3l.yggdrasil.utils.cache.checker.CacheChecker;
import org.s3s3l.yggdrasil.utils.cache.compressor.CompressConfiguration;
import org.s3s3l.yggdrasil.utils.cache.compressor.CompressProperties;
import org.s3s3l.yggdrasil.utils.compressor.Compressor;
import org.s3s3l.yggdrasil.utils.compressor.ZstdCompressor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.benmanes.caffeine.cache.Caffeine;

/**
 * <p>
 * </p>
 * ClassName:ComplexCacheHelperBuilder <br>
 * Date: Apr 8, 2019 7:28:17 PM <br>
 * 
 * @author kehw_zwei
 * @version 1.0.0
 * @since JDK 1.8
 */
public class ComplexCacheHelperBuilder {
    private CacheChecker checker;
    private Supplier<RemoteCacheHolder<byte[], byte[]>> remoteHolderSupplier;
    private CompressConfiguration compressConfig = new CompressConfiguration();
    private int remoteExpireAfterWrite = -1;
    private int remoteExpireAfterAccess = -1;
    private int localExpireAfterAccess = -1;
    private int localExpireAfterWrite = -1;
    private int localMaxNum = 500;

    private final Logger logger = LoggerFactory.getLogger(getClass());

    public static ComplexCacheHelperBuilder newBuilder() {
        return new ComplexCacheHelperBuilder();
    }

    private ComplexCacheHelperBuilder() {
    }

    public ComplexCacheHelperBuilder checker(CacheChecker checker) {
        this.checker = checker;
        return this;
    }

    public ComplexCacheHelperBuilder remoteHolder(Supplier<RemoteCacheHolder<byte[], byte[]>> remoteHolderSupplier) {
        this.remoteHolderSupplier = remoteHolderSupplier;
        return this;
    }

    public ComplexCacheHelperBuilder compressProp(CompressProperties prop) {
        this.compressConfig.setProp(prop);
        return this;
    }

    public ComplexCacheHelperBuilder compressKey(int level) {
        this.compressConfig.getProp()
                .setCompressKey(true);
        this.compressConfig.getProp()
                .getKeyConfig()
                .setLevel(level);
        return this;
    }

    public ComplexCacheHelperBuilder compressValue(int level) {
        this.compressConfig.getProp()
                .setCompressValue(true);
        this.compressConfig.getProp()
                .getValueConfig()
                .setLevel(level);
        return this;
    }

    public ComplexCacheHelperBuilder compressor(Supplier<Compressor> compressorSupplier) {
        this.compressConfig.setCompressorSupplier(compressorSupplier);
        return this;
    }

    public ComplexCacheHelperBuilder remoteExpireAfterWrite(int seconds) {
        if (seconds > 0) {
            this.remoteExpireAfterWrite = seconds;
        }
        return this;
    }

    public ComplexCacheHelperBuilder remoteExpireAfterAccess(int seconds) {
        if (seconds > 0) {
            this.remoteExpireAfterAccess = seconds;
        }
        return this;
    }

    public ComplexCacheHelperBuilder localExpireAfterAccess(int seconds) {
        if (seconds > 0) {
            this.localExpireAfterAccess = seconds;
        }
        return this;
    }

    public ComplexCacheHelperBuilder localExpireAfterWrite(int seconds) {
        if (seconds > 0) {
            this.localExpireAfterWrite = seconds;
        }
        return this;
    }

    public ComplexCacheHelperBuilder localMaxNum(int maxNum) {
        if (maxNum > 0) {
            this.localMaxNum = maxNum;
        }
        return this;
    }

    public ComplexCacheHelper<byte[], byte[]> build() {

        // 检查是否正确配置了压缩器构造器
        // 如果没有配置，则使用默认的zstd压缩器
        if (this.compressConfig.getCompressorSupplier() == null || this.compressConfig.getCompressorSupplier()
                .get() == null) {
            this.compressConfig.setCompressorSupplier(ZstdCompressor::new);
        }

        // 构造RemoteCacheHolder
        RemoteCacheHolder<byte[], byte[]> remoteCacheHolder = this.remoteHolderSupplier.get();
        if (remoteExpireAfterWrite > 0) {
            remoteCacheHolder.expireAfterWrite(remoteExpireAfterWrite);
        }
        if (remoteExpireAfterAccess > 0) {
            remoteCacheHolder.expireAfterAccess(remoteExpireAfterAccess);
        }
        // 构造noCompressRemoteCacheHolder用于localCacheLoading
        RemoteCacheHolder<byte[], byte[]> noCompressRemoteCacheHolder =this.remoteHolderSupplier.get();
        if (remoteExpireAfterWrite > 0) {
            noCompressRemoteCacheHolder.expireAfterWrite(remoteExpireAfterWrite);
        }
        if (remoteExpireAfterAccess > 0) {
            noCompressRemoteCacheHolder.expireAfterAccess(remoteExpireAfterAccess);
        }

        // 构造LoadingCacheHolder
        LoadingCacheHolder<byte[], byte[]> loadingCacheHolder = new CompressedCaffeineCacheHolder(scope -> {
            Caffeine<Object, Object> caffeine = Caffeine.newBuilder();
            if (localExpireAfterAccess > 0) {
                // 设置访问后失效时间
                caffeine.expireAfterAccess(localExpireAfterAccess, TimeUnit.SECONDS);
            }
            if (localExpireAfterWrite > 0) {
                // 设置写入后失效时间
                caffeine.expireAfterWrite(localExpireAfterWrite, TimeUnit.SECONDS);
            }
            // 设置本地缓存最大数量
            caffeine.maximumSize(localMaxNum);
            // 如果本地无法获取则从远端缓存获取数据
            return caffeine.build(key -> {
                logger.trace("fetching cache from remote. key: {}", key);
                if (key == null) {
                    return null;
                }
                return noCompressRemoteCacheHolder.get(key, scope);
            });
        });
        return new CompressedComplexCacheHelper(loadingCacheHolder, checker, remoteCacheHolder, this.compressConfig);
    }
}
