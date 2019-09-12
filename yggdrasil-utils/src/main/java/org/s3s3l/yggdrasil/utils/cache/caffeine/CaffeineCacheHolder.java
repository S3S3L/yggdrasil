package org.s3s3l.yggdrasil.utils.cache.caffeine;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.annotation.Nonnull;

import org.s3s3l.yggdrasil.utils.cache.LoadingCacheHolder;
import org.s3s3l.yggdrasil.utils.common.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.benmanes.caffeine.cache.LoadingCache;

/**
 * <p>
 * </p>
 * ClassName:CaffeineCacheHelper <br>
 * Date: Sep 21, 2017 1:58:58 PM <br>
 * 
 * @author kehw_zwei
 * @version 1.0.0
 * @since JDK 1.8
 */
public abstract class CaffeineCacheHolder<K, V> implements LoadingCacheHolder<K, V> {
    private static final String DEFAULT_SCOPE = "default";
    private static final String MSG_DOING_NOTHING = "cant find provided scope [{}] in local cache, do nothing.";

    private final Logger logger = LoggerFactory.getLogger(CaffeineCacheHolder.class);

    /**
     * Key: scope; Value: {@link LoadingCache} for scope
     */
    private final ConcurrentMap<String, LoadingCache<K, V>> caches = new ConcurrentHashMap<>();
    private final PartitionCaffeineCacheBuilder<K, V> cacheBuilder;
    private final Lock buildingCacheLock = new ReentrantLock();

    public CaffeineCacheHolder(@Nonnull PartitionCaffeineCacheBuilder<K, V> cacheBuilder) {
        this.cacheBuilder = cacheBuilder;
    }

    @Override
    public V get(K key) {
        return get(key, DEFAULT_SCOPE);
    }

    @Override
    public V get(K key, String scope) {
        if (StringUtils.isEmpty(scope)) {
            logger.debug("scope not provide, get data from default scope '{}'.", DEFAULT_SCOPE);
            scope = DEFAULT_SCOPE;
        }

        if (!caches.containsKey(scope)) {
            logger.debug("cant find provided scope '{}' in local cache. trying to build local cache.", scope);
            buildCache(scope);
        }

        logger.debug("Fetching data from scope '{}' key '{}' in local cache.", scope, key);
        return decompressValue(caches.get(scope)
                .get(compressKey(key)));
    }

    @Override
    public void put(K key, V value) {
        put(key, value, DEFAULT_SCOPE);
    }

    @Override
    public void put(K key, V value, String scope) {
        if (StringUtils.isEmpty(scope)) {
            logger.debug("scope not provide, put value into default scope '{}'.", DEFAULT_SCOPE);
            scope = DEFAULT_SCOPE;
        }

        if (!caches.containsKey(scope)) {
            logger.debug("cant find provided scope '{}' in local cache. trying to build local cache.", scope);
            buildCache(scope);
        }

        logger.debug("Putting data to scope '{}' key '{}' in local cache.", scope, key);
        caches.get(scope)
                .put(compressKey(key), compressValue(value));
    }

    @Override
    public void invalidate(K key, String scope) {
        if (!caches.containsKey(scope)) {
            logger.debug(MSG_DOING_NOTHING, scope);
            return;
        }

        logger.debug("Invalidating local cache from scope '{}' key '{}'.", scope, key);
        caches.get(scope)
                .invalidate(compressKey(key));
    }

    @Override
    public void invalidate(String scope) {
        if (StringUtils.isEmpty(scope)) {
            logger.debug("scope not provide, invalidate default scope '{}'.", DEFAULT_SCOPE);
            scope = DEFAULT_SCOPE;
        }

        logger.debug("refresh last update time for provided scope '{}' in cache checker.", scope);

        if (!caches.containsKey(scope)) {
            logger.debug(MSG_DOING_NOTHING, scope);
            return;
        }

        logger.debug("Invalidating cache from scope '{}'.", scope);
        caches.get(scope)
                .invalidateAll();
    }

    @Override
    public void invalidateAll() {

        logger.debug("Invalidating all local cache.");
        caches.values()
                .forEach(LoadingCache::invalidateAll);
    }

    @Override
    public void refresh(K key, String scope) {
        if (!caches.containsKey(scope)) {
            logger.debug(MSG_DOING_NOTHING, scope);
            return;
        }

        logger.debug("Refreshing cache for from scope '{}' key '{}'.", scope, key);
        caches.get(scope)
                .refresh(compressKey(key));
    }

    @Override
    public void refresh(String scope) {
        if (StringUtils.isEmpty(scope)) {
            logger.debug("scope '{}' not provide, refresh default scope.", scope);
            scope = DEFAULT_SCOPE;
        }
        if (caches.containsKey(scope)) {
            caches.remove(scope);
        }

        logger.debug("Refreshing cache for scope '{}'.", scope);
    }

    @Override
    public void refreshAll() {

        logger.debug("Refreshing all local cache.");
        caches.clear();
    }

    @Override
    public void updateCache(String scope, K key, V value) {
        caches.get(scope)
                .put(compressKey(key), compressValue(value));
    }

    protected abstract K compressKey(K key);

    protected abstract V compressValue(V value);

    protected abstract V decompressValue(V value);

    /**
     * 
     * Build local cache for scope.
     * 
     * @param scope
     *            cache scope
     * @return new {@link LoadingCache} instance.
     * @since JDK 1.8
     */
    private LoadingCache<K, V> buildCache(String scope) {
        buildingCacheLock.lock();
        try {
            if (caches.containsKey(scope)) {
                return caches.get(scope);
            }

            logger.debug("Building cache for scope '{}'.", scope);

            LoadingCache<K, V> loadingCache = cacheBuilder.getCache(scope);
            caches.put(scope, loadingCache);
            return loadingCache;
        } finally {
            buildingCacheLock.unlock();
        }
    }
}
