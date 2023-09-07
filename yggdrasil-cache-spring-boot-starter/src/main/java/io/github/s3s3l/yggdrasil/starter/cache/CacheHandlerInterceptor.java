package io.github.s3s3l.yggdrasil.starter.cache;

import java.io.OutputStream;
import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.lang.Nullable;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.util.ContentCachingResponseWrapper;

import io.github.s3s3l.yggdrasil.annotation.Cache;
import io.github.s3s3l.yggdrasil.annotation.CacheExpire;
import io.github.s3s3l.yggdrasil.cache.helper.ComplexCacheHelper;
import io.github.s3s3l.yggdrasil.cache.key.CacheKeyGenerator;
import io.github.s3s3l.yggdrasil.utils.common.StringUtils;
import io.github.s3s3l.yggdrasil.utils.reflect.ReflectionUtils;
import io.github.s3s3l.yggdrasil.utils.stuctural.jackson.JacksonUtils;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CacheHandlerInterceptor implements HandlerInterceptor {
    /**
     * 缓存管理器
     */
    private final ComplexCacheHelper<byte[], byte[]> cacheHelper;
    /**
     * key构造器
     */
    private final CacheKeyGenerator<byte[]> keyGenerator;

    public CacheHandlerInterceptor(ComplexCacheHelper<byte[], byte[]> cacheHelper,
            CacheKeyGenerator<byte[]> keygenerator) {
        this.cacheHelper = cacheHelper;
        this.keyGenerator = keygenerator;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }

        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();
        // 检查缓存注解
        if (!ReflectionUtils.isAnnotationedWith(method, Cache.class)) {
            return true;
        }

        log.info("Method [{}] annotationed with [{}]. Fetching cache data.", method, Cache.class);
        // 获取缓存作用域
        Cache cache = ReflectionUtils.getAnnotation(method, Cache.class);
        String scope = StringUtils.isEmpty(cache.scope()) ? cache.value() : cache.scope();
        // 构造缓存key
        byte[] cacheKey = keyGenerator.getKey(HttpCacheKey.builder()
                .path(request.getRequestURI())
                .params(request.getParameterMap())
                .method(request.getMethod())
                .body(request.getInputStream()
                        .readAllBytes())
                .build());
        log.trace("fetching cache. cacheKey: {}", cacheKey);
        // 获取缓存数据
        byte[] cacheDataBytes = cacheHelper.get(cacheKey, scope);

        if (cacheDataBytes == null) {
            // 未获取到缓存
            log.info("No aviliable cache was found.");
            return true;
        }

        try (OutputStream os = response.getOutputStream()) {
            HttpCacheData cacheData = JacksonUtils.JSON.toObject(cacheDataBytes, HttpCacheData.class);
            cacheData.getHeaders()
                    .entrySet()
                    .forEach(header -> {
                        response.setHeader(header.getKey(), header.getValue());
                    });
            os.write(cacheData.getContent(), 0, cacheData.getContent().length);
            os.flush();
        }

        // 返回缓存数据
        log.info("Hit cache. Return cached data.");
        return false;
    }

    @Override
    public void afterCompletion(HttpServletRequest request,
            HttpServletResponse response,
            Object handler,
            @Nullable Exception ex) throws Exception {
        if (!(handler instanceof HandlerMethod)) {
            return;
        }

        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();
        // 检查缓存失效注解
        if (ReflectionUtils.isAnnotationedWith(method, CacheExpire.class)) {

            log.info("Method [{}] annotationed with [{}]. Invalidating cache data.", method, CacheExpire.class);

            CacheExpire cacheExpire = ReflectionUtils.getAnnotation(method, CacheExpire.class);
            for (String scope : cacheExpire.scopes().length <= 0 ? cacheExpire.value() : cacheExpire.scopes()) {
                // 过期缓存
                cacheHelper.expire(scope);
            }
        } else if (ex == null && ReflectionUtils.isAnnotationedWith(method, Cache.class)) {
            if (!(response instanceof ContentCachingResponseWrapper)) {
                log.trace("Response not instance of ContentCachingResponseWrapper. Skip.");
                return;
            }
            ContentCachingResponseWrapper res = (ContentCachingResponseWrapper) response;

            log.info("Method [{}] annotationed with [{}]. Putting cache data.", method, Cache.class);

            // 获取缓存作用域
            Cache cache = ReflectionUtils.getAnnotation(method, Cache.class);
            String scope = StringUtils.isEmpty(cache.scope()) ? cache.value() : cache.scope();
            // 构造缓存key
            byte[] cacheKey = keyGenerator.getKey(HttpCacheKey.builder()
                    .path(request.getRequestURI())
                    .params(request.getParameterMap())
                    .method(request.getMethod())
                    .body(request.getInputStream()
                            .readAllBytes())
                    .build());

            // 更新缓存
            log.trace("putting cache. cacheKey: {}", cacheKey);

            HttpCacheData cacheData = HttpCacheData.builder()
                    .content(res.getContentAsByteArray())
                    .build()
                    .setHeader("Content-Type", request.getContentType());
            cacheHelper.update(cacheKey, JacksonUtils.JSON.toStructuralBytes(cacheData), scope);
        }
    }

}
