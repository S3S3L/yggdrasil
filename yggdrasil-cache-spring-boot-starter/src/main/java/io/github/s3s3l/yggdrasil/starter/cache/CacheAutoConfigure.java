package io.github.s3s3l.yggdrasil.starter.cache;

import javax.servlet.Filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import io.github.s3s3l.yggdrasil.bean.exception.ConfigurationException;
import io.github.s3s3l.yggdrasil.cache.checker.CacheChecker;
import io.github.s3s3l.yggdrasil.cache.checker.RedisCacheChecker;
import io.github.s3s3l.yggdrasil.cache.helper.ComplexCacheHelper;
import io.github.s3s3l.yggdrasil.cache.helper.ComplexCacheHelperBuilder;
import io.github.s3s3l.yggdrasil.cache.interceptor.ComplexCacheInterceptor;
import io.github.s3s3l.yggdrasil.cache.key.JacksonCacheKeyGenerator;
import io.github.s3s3l.yggdrasil.cache.redis.RedisCacheHolder;
import io.github.s3s3l.yggdrasil.redis.base.IRedis;
import io.github.s3s3l.yggdrasil.spring.BeanUtils;
import io.github.s3s3l.yggdrasil.utils.interceptor.Interceptor;
import io.github.s3s3l.yggdrasil.utils.stuctural.jackson.JacksonUtils;

/**
 * <p>
 * </p>
 * ClassName:CacheAutoConfigure <br>
 * Date: Apr 10, 2019 7:57:09 PM <br>
 * 
 * @author kehw_zwei
 * @version 1.0.0
 * @since JDK 1.8
 */
@ConditionalOnProperty(prefix = CacheConfiguration.PREFIX, name = "enable", havingValue = "true")
public class CacheAutoConfigure implements ImportBeanDefinitionRegistrar, EnvironmentAware {
    private static final String COMPLEX_CACHE_HELPER = "complexCacheHelper";
    private static final String BEAN_NAME = "cacheAutoConfigure";
    private static final String CACHE_HANDLER_INTERCEPTOR = "cacheHandlerInterceptor";
    private static final String WRAPPER_FILTER_REGISTRATION = "wrapperFilterRegistration";
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private CacheConfiguration configuration;
    private Environment environment;

    public CacheAutoConfigure() {

    }

    public CacheAutoConfigure(CacheConfiguration configuartion, Environment environment) {
        this.configuration = configuartion;
        this.environment = environment;
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        this.configuration = Binder.get(this.environment)
                .bind(CacheConfiguration.PREFIX, CacheConfiguration.class)
                .orElse(new CacheConfiguration());
        if (!configuration.isEnable()) {
            return;
        }
        logger.info("Cache configuration: {}", this.configuration);
        registry.registerBeanDefinition(BEAN_NAME, BeanUtils.buildBeanDefinition(null, null, null,
                new Object[] { this.configuration, this.environment }, CacheAutoConfigure.class));

        registry.registerBeanDefinition(COMPLEX_CACHE_HELPER,
                BeanUtils.buildBeanDefinitionForFactoryMethod(ComplexCacheHelper.class, BEAN_NAME, "redisCacheHelper",
                        null, this.configuration.getDataRedis(), this.configuration.getVersionRedis()));
        registry.registerBeanDefinition("complexCacheInterceptor", BeanUtils.buildBeanDefinitionForFactoryMethod(
                Interceptor.class, BEAN_NAME, "cacheInterceptor", null, COMPLEX_CACHE_HELPER));
        if (this.configuration.isWeb()) {
            registry.registerBeanDefinition(WRAPPER_FILTER_REGISTRATION, BeanUtils.buildBeanDefinitionForFactoryMethod(
                    FilterRegistrationBean.class, BEAN_NAME, "wrapperFilterRegistration", null));
            registry.registerBeanDefinition(CACHE_HANDLER_INTERCEPTOR, BeanUtils.buildBeanDefinitionForFactoryMethod(
                    HandlerInterceptor.class, BEAN_NAME, "handlerInterceptor", null, COMPLEX_CACHE_HELPER));
            registry.registerBeanDefinition("cacheInterceptorRegister", BeanUtils.buildBeanDefinitionForFactoryMethod(
                    WebMvcConfigurer.class, BEAN_NAME, "cacheInterceptorRegister", null, CACHE_HANDLER_INTERCEPTOR));
        }
    }

    public Interceptor cacheInterceptor(ComplexCacheHelper<byte[], byte[]> helper) {
        try {
            return new ComplexCacheInterceptor(helper,
                    new JacksonCacheKeyGenerator(JacksonUtils.create(new YAMLFactory())
                            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                            .include(Include.NON_NULL)));
        } catch (Exception e) {
            throw new ConfigurationException(e);
        }
    }

    public HandlerInterceptor handlerInterceptor(ComplexCacheHelper<byte[], byte[]> helper) {
        try {
            return new CacheHandlerInterceptor(helper,
                    new JacksonCacheKeyGenerator(JacksonUtils.create(new YAMLFactory())
                            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                            .include(Include.NON_NULL)));
        } catch (Exception e) {
            throw new ConfigurationException(e);
        }
    }

    public WebMvcConfigurer cacheInterceptorRegister(HandlerInterceptor... interceptors) {
        try {
            return new CacheInterceptorRegister(interceptors);
        } catch (Exception e) {
            throw new ConfigurationException(e);
        }
    }

    public ComplexCacheHelper<byte[], byte[]> redisCacheHelper(IRedis dataRedis, IRedis versionRedis) {
        try {
            CacheChecker checker = new RedisCacheChecker(versionRedis);
            return ComplexCacheHelperBuilder.newBuilder()
                    .checker(checker)
                    .remoteHolder(() -> new RedisCacheHolder(dataRedis, checker, this.configuration.getKeyPrefix()))
                    .compressProp(this.configuration.getCompress())
                    .compressor(this.configuration.getCompressorSupplier()
                            .getConstructor()
                            .newInstance())
                    .localExpireAfterAccess(this.configuration.getLocalExpireAfterAccess())
                    .localExpireAfterWrite(this.configuration.getLocalExpireAfterWrite())
                    .localMaxNum(this.configuration.getLocalMaxNum())
                    .remoteExpireAfterAccess(this.configuration.getRemoteExpireAfterAccess())
                    .remoteExpireAfterWrite(this.configuration.getRemoteExpireAfterWrite())
                    .build();
        } catch (Exception e) {
            throw new ConfigurationException(e);
        }
    }
    
    public FilterRegistrationBean<Filter> wrapperFilterRegistration() {
        FilterRegistrationBean<Filter> registration = new FilterRegistrationBean<>();
        registration.setFilter(new WrapperFilter());
        registration.addUrlPatterns("/*");
        registration.setName("wrapperFilter");
        registration.setOrder(1);
        return registration;
    }

}
