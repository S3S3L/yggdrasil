package io.github.s3s3l.yggdrasil.cache.interceptor;

import java.lang.reflect.Method;

import io.github.s3s3l.yggdrasil.annotation.Cache;
import io.github.s3s3l.yggdrasil.annotation.CacheExpire;
import io.github.s3s3l.yggdrasil.cache.exception.CacheFetchException;
import io.github.s3s3l.yggdrasil.cache.helper.ComplexCacheHelper;
import io.github.s3s3l.yggdrasil.cache.key.CacheKey;
import io.github.s3s3l.yggdrasil.cache.key.CacheKeyGenerator;
import io.github.s3s3l.yggdrasil.utils.common.StringUtils;
import io.github.s3s3l.yggdrasil.utils.interceptor.Interceptor;
import io.github.s3s3l.yggdrasil.utils.log.base.LogHelper;
import io.github.s3s3l.yggdrasil.utils.reflect.ReflectionUtils;
import io.github.s3s3l.yggdrasil.utils.stuctural.jackson.JacksonHelper;
import io.github.s3s3l.yggdrasil.utils.stuctural.jackson.JacksonUtils;
import org.slf4j.Logger;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.DeserializationFeature;

/**
 * <p>
 * </p>
 * ClassName:ComplexCacheInterceptor <br>
 * Date: Apr 26, 2018 10:06:35 AM <br>
 * 
 * @author kehw_zwei
 * @version 1.0.0
 * @since JDK 1.8
 */
public class ComplexCacheInterceptor implements Interceptor {
    private final JacksonHelper json = JacksonUtils.create()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .include(Include.NON_NULL);

    private final Logger logger = LogHelper.getLogger(CacheInterceptor.class);
    /**
     * 缓存管理器
     */
    private final ComplexCacheHelper<byte[], byte[]> cacheHelper;
    /**
     * key构造器
     */
    private final CacheKeyGenerator<byte[]> keyGenerator;

    public ComplexCacheInterceptor(ComplexCacheHelper<byte[], byte[]> cacheHelper,
            CacheKeyGenerator<byte[]> keygenerator) {
        this.cacheHelper = cacheHelper;
        this.keyGenerator = keygenerator;
    }

    @Override
    public Object before(String serialKey, Object proxy, Method method, Object... params) {
        // 检查缓存注解
        if (!ReflectionUtils.isAnnotationedWith(method, Cache.class)) {
            return null;
        }

        logger.info("Method [{}] annotationed with [{}]. Fetching cache data.", method, Cache.class);

        // 获取缓存作用域
        Cache cache = ReflectionUtils.getAnnotation(method, Cache.class);
        String scope = StringUtils.isEmpty(cache.scope()) ? cache.value() : cache.scope();
        // 构造缓存key
        byte[] cacheKey = keyGenerator.getKey(new CacheKey().setType(method.getDeclaringClass()
                .getName())
                .setMethod(method.toString())
                .setParams(params));
        logger.trace("fetching cache. cacheKey: {}", cacheKey);
        // 获取缓存数据
        byte[] cacheData = cacheHelper.get(cacheKey, scope);

        if (cacheData == null) {
            // 未获取到缓存
            logger.info("No aviliable cache was found.");
            return null;
        }

        Object result;

        try {
            // 尝试解析缓存
            result = json.toObject(cacheData, method.getReturnType());
        } catch (Exception e) {
            throw new CacheFetchException(String.format("Return type [%s] is not assignable from cached data type [%s]",
                    method.getReturnType()
                            .getName(),
                    cacheData.getClass()
                            .getName()),
                    e);
        }

        // 返回缓存数据
        logger.info("Hit cache. Return cached data.");
        return result;
    }

    @Override
    public <T> T after(T response, String serialKey, Object proxy, Method method, Object... params) {
        // 检查缓存注解
        if (!ReflectionUtils.isAnnotationedWith(method, Cache.class)) {
            return response;
        }

        logger.info("Method [{}] annotationed with [{}]. Putting cache data.", method, Cache.class);

        // 获取缓存作用域
        Cache cache = ReflectionUtils.getAnnotation(method, Cache.class);
        String scope = StringUtils.isEmpty(cache.scope()) ? cache.value() : cache.scope();
        // 构造缓存key
        byte[] cacheKey = keyGenerator.getKey(new CacheKey().setType(method.getDeclaringClass()
                .getName())
                .setMethod(method.toString())
                .setParams(params));
        logger.trace("putting cache. cacheKey: {}", cacheKey);

        // 更新缓存
        cacheHelper.update(cacheKey, json.toStructuralBytes(response), scope);
        return response;
    }

    @Override
    public Object
            exception(Object response, String serialKey, Object proxy, Method method, Throwable e, Object... params) {
        return response;
    }

    @Override
    public void always(Object response, Object proxy, Method method, Object... params) {
        // 检查缓存失效注解
        if (!ReflectionUtils.isAnnotationedWith(method, CacheExpire.class)) {
            return;
        }

        logger.info("Method [{}] annotationed with [{}]. Invalidating cache data.", method, CacheExpire.class);

        CacheExpire cacheExpire = ReflectionUtils.getAnnotation(method, CacheExpire.class);
        for (String scope : cacheExpire.scopes().length <= 0 ? cacheExpire.value() : cacheExpire.scopes()) {
            // 过期缓存
            cacheHelper.expire(scope);
        }
    }

}
