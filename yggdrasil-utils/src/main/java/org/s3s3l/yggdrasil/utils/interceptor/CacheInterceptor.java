package org.s3s3l.yggdrasil.utils.interceptor;

import java.lang.reflect.Method;

import org.s3s3l.yggdrasil.annotation.Cache;
import org.s3s3l.yggdrasil.annotation.CacheExpire;
import org.s3s3l.yggdrasil.utils.cache.ScopeCacheHolder;
import org.s3s3l.yggdrasil.utils.cache.exception.CacheFetchException;
import org.s3s3l.yggdrasil.utils.cache.key.CacheKey;
import org.s3s3l.yggdrasil.utils.cache.key.CacheKeyGenerator;
import org.s3s3l.yggdrasil.utils.common.StringUtils;
import org.s3s3l.yggdrasil.utils.log.base.LogHelper;
import org.s3s3l.yggdrasil.utils.reflect.ReflectionUtils;
import org.slf4j.Logger;

/**
 * <p>
 * </p>
 * ClassName:CacheInterceptor <br>
 * Date: Sep 22, 2017 7:30:42 PM <br>
 * 
 * @author kehw_zwei
 * @version 1.0.0
 * @since JDK 1.8
 */
public class CacheInterceptor implements Interceptor {
    private final ScopeCacheHolder<String, Object> cacheHolder;
    private final CacheKeyGenerator<String> keyGenerator;
    private final Logger logger = LogHelper.getLogger(CacheInterceptor.class);

    public CacheInterceptor(ScopeCacheHolder<String, Object> cacheHolder, CacheKeyGenerator<String> keygenerator) {
        this.cacheHolder = cacheHolder;
        this.keyGenerator = keygenerator;
    }

    @Override
    public Object before(String serialKey, Object proxy, Method method, Object... params) {
        if (!ReflectionUtils.isAnnotationedWith(method, Cache.class)) {
            return null;
        }

        logger.info("Method [{}] annotationed with [{}]. Fetching cache data.", method, Cache.class);

        Cache cache = ReflectionUtils.getAnnotation(method, Cache.class);
        String scope = StringUtils.isEmpty(cache.scope()) ? cache.value() : cache.scope();
        String cacheKey = keyGenerator.getKey(new CacheKey().setType(method.getDeclaringClass()
                .getName())
                .setMethod(method.toString())
                .setParams(params));

        Object cacheData = cacheHolder.get(cacheKey, scope);

        if (cacheData == null) {
            logger.info("No aviliable cache was found.");
            return null;
        }

        if (!method.getReturnType()
                .isAssignableFrom(cacheData.getClass())) {
            throw new CacheFetchException(String.format("Return type [%s] is not assignable from cached data type [%s]",
                    method.getReturnType()
                            .getName(),
                    cacheData.getClass()
                            .getName()));
        }

        logger.info("Hit cache. Return cached data.");

        return cacheData;
    }

    @Override
    public <T> T after(T response, String serialKey, Object proxy, Method method, Object... params) {
        if (!ReflectionUtils.isAnnotationedWith(method, Cache.class)) {
            return response;
        }

        logger.info("Method [{}] annotationed with [{}]. Putting cache data.", method, Cache.class);

        Cache cache = ReflectionUtils.getAnnotation(method, Cache.class);
        String scope = StringUtils.isEmpty(cache.scope()) ? cache.value() : cache.scope();
        String cacheKey = keyGenerator.getKey(new CacheKey().setType(method.getDeclaringClass()
                .getName())
                .setMethod(method.toString())
                .setParams(params));

        cacheHolder.put(cacheKey, response, scope);
        return response;
    }

    @Override
    public Object
            exception(Object response, String serialKey, Object proxy, Method method, Throwable e, Object... params) {
        return response;
    }

    @Override
    public void always(Object response, Object proxy, Method method, Object... params) {
        if (!ReflectionUtils.isAnnotationedWith(method, CacheExpire.class)) {
            return;
        }

        logger.info("Method [{}] annotationed with [{}]. Invalidating cache data.", method, CacheExpire.class);

        CacheExpire cacheExpire = ReflectionUtils.getAnnotation(method, CacheExpire.class);
        for (String scope : cacheExpire.scopes().length <= 0 ? cacheExpire.value() : cacheExpire.scopes()) {
            cacheHolder.refresh(scope);
        }
    }

}
