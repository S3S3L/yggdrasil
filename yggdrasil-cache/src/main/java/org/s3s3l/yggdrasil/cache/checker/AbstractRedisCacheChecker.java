package org.s3s3l.yggdrasil.cache.checker;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.s3s3l.yggdrasil.utils.common.StringUtils;
import org.s3s3l.yggdrasil.utils.concurrent.ResourceHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>
 * Using redis to check if a cache is fresh.
 * </p>
 * ClassName:RedisCacheChecker <br>
 * Date: Sep 26, 2017 2:52:26 PM <br>
 * 
 * @author kehw_zwei
 * @version 1.0.0
 * @since JDK 1.8
 */
public abstract class AbstractRedisCacheChecker implements CacheChecker {
    protected static final String SYNC_LAST_UPDATE = "redis.replicate_commands();\n"
            + "local times = redis.call('time');\n"
            + "local time = string.format(\"%s%s\",times[1],string.sub(times[2],0,3));\n"
            + "local oldTime = redis.call('get',\"{key}\");\n"
            + "if redis.call('exists',\"{key}\") > 0 and oldTime > time then time = string.format(\"%s%s\",times[1],string.sub(times[2],0,3)); end \n"
            + "redis.call('set',\"{key}\",time);\n" + "return time;";
    protected static final String SCOPE_KEY_PREFIX = "cacheScope:";
    
    private final ConcurrentMap<String, Long> lastUpdateTime = new ConcurrentHashMap<>();
    private final ReadWriteLock syncLock = new ReentrantReadWriteLock();

    protected final Logger logger = LoggerFactory.getLogger(getClass());
    
    protected abstract String get(String scope);
    
    protected abstract Object updateAndGet(String scope);

    @Override
    public boolean isFresh(String scope) {
        return ResourceHelper.readFromResource(() -> {
            String lastUpdate = get(cacheScopeKey(scope));
            if (StringUtils.isEmpty(lastUpdate)) {
                return false;
            }

            if (!lastUpdateTime.containsKey(scope)) {
                return false;
            }

            logger.trace("CacheChecker isFresh. local version: {}, remote version: {}", lastUpdateTime.get(scope),
                    lastUpdate);
            return Long.valueOf(lastUpdate) <= lastUpdateTime.get(scope);
        }, syncLock);
    }

    @Override
    public void syncRemote(String scope) {
        ResourceHelper.reloadResource(() -> {
            String lastUpdate = get(cacheScopeKey(scope));
            if (StringUtils.isEmpty(lastUpdate)) {
                syncLocal(scope);
                return;
            }
            logger.trace("CacheChecker syncRemote. new version: {}", lastUpdate);
            lastUpdateTime.put(scope, Long.valueOf(lastUpdate));
        }, syncLock);
    }

    @Override
    public void syncLocal(String scope) {
        ResourceHelper.reloadResource(() -> {
            Object response = updateAndGet(SCOPE_KEY_PREFIX.concat(scope));
            logger.trace("CacheChecker syncLocal. new version: {}", response);
            lastUpdateTime.put(scope, Long.valueOf(response.toString()));
        }, syncLock);
    }

    private String cacheScopeKey(String scope) {
        return SCOPE_KEY_PREFIX.concat(scope);
    }

    @Override
    public Long localVersion(String scope) {
        Long version = ResourceHelper.readFromResource(() -> lastUpdateTime.get(scope), syncLock);
        if (version != null) {
            return version;
        }

        syncRemote(scope);

        return ResourceHelper.readFromResource(() -> lastUpdateTime.get(scope), syncLock);
    }
}
