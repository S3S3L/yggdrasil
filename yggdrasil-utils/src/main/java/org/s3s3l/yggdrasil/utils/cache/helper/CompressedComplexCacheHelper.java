package org.s3s3l.yggdrasil.utils.cache.helper;

import org.s3s3l.yggdrasil.utils.cache.LoadingCacheHolder;
import org.s3s3l.yggdrasil.utils.cache.RemoteCacheHolder;
import org.s3s3l.yggdrasil.utils.cache.checker.CacheChecker;
import org.s3s3l.yggdrasil.utils.cache.compressor.CompressConfiguration;

/**
 * <p>
 * </p>
 * ClassName:CompressedComplexCacheHelper <br>
 * Date: Apr 3, 2019 7:23:07 PM <br>
 * 
 * @author kehw_zwei
 * @version 1.0.0
 * @since JDK 1.8
 */
public class CompressedComplexCacheHelper implements ComplexCacheHelper<byte[], byte[]> {
    private final LoadingCacheHolder<byte[], byte[]> local;
    private final RemoteCacheHolder<byte[], byte[]> remote;
    private final CacheChecker cacheChecker;

    CompressedComplexCacheHelper(LoadingCacheHolder<byte[], byte[]> localHolder, CacheChecker cacheChecker,
            RemoteCacheHolder<byte[], byte[]> remoteHolder, CompressConfiguration compressConfig) {
        this.local = localHolder;
        this.cacheChecker = cacheChecker;
        this.remote = remoteHolder;
        this.local.configCompress(compressConfig);
        this.remote.configCompress(compressConfig);
    }

    /**
     * 
     * @see org.s3s3l.yggdrasil.utils.cache.helper.ComplexCacheHelper#get(java.lang.Object,
     *      java.lang.String)
     */
    @Override
    public byte[] get(byte[] key, String scope) {
        if (!this.cacheChecker.isFresh(scope)) {
            this.cacheChecker.syncRemote(scope);
            this.local.refresh(scope);
        }
        return this.local.get(key, scope);
    }

    @Override
    public void update(byte[] key, byte[] newValue, String scope) {
        this.local.put(key, newValue, scope);
        this.remote.put(key, newValue, scope);
    }

    @Override
    public void expire(String scope) {
        this.local.refresh(scope);
        this.cacheChecker.syncLocal(scope);
        this.remote.refresh(scope);
    }

}
