package org.s3s3l.yggdrasil.starter.cache;

import org.s3s3l.yggdrasil.bean.exception.ConfigurationException;
import org.s3s3l.yggdrasil.utils.cache.checker.CacheChecker;
import org.s3s3l.yggdrasil.utils.cache.checker.RedisCacheChecker;
import org.s3s3l.yggdrasil.utils.cache.helper.ComplexCacheHelper;
import org.s3s3l.yggdrasil.utils.cache.helper.ComplexCacheHelperBuilder;
import org.s3s3l.yggdrasil.utils.cache.key.JacksonCacheKeyGenerator;
import org.s3s3l.yggdrasil.utils.cache.redis.RedisCacheHolder;
import org.s3s3l.yggdrasil.utils.interceptor.ComplexCacheInterceptor;
import org.s3s3l.yggdrasil.utils.interceptor.Interceptor;
import org.s3s3l.yggdrasil.utils.json.JacksonUtils;
import org.s3s3l.yggdrasil.utils.redis.base.IRedis;
import org.s3s3l.yggdrasil.utils.spring.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotationMetadata;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

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
		this.configuration = Binder.get(this.environment).bind(CacheConfiguration.PREFIX, CacheConfiguration.class)
				.orElse(new CacheConfiguration());
		logger.info("Cache configuration: {}", this.configuration);
		registry.registerBeanDefinition(BEAN_NAME, BeanUtils.buildBeanDefinition(null, null, null,
				new Object[] { this.configuration, this.environment }, CacheAutoConfigure.class));

		registry.registerBeanDefinition(COMPLEX_CACHE_HELPER,
				BeanUtils.buildBeanDefinitionForFactoryMethod(ComplexCacheHelper.class, BEAN_NAME, "redisCacheHelper",
						null, this.configuration.getDataRedis(), this.configuration.getVersionRedis()));
		registry.registerBeanDefinition("complexCacheInterceptor", BeanUtils.buildBeanDefinitionForFactoryMethod(
				Interceptor.class, BEAN_NAME, "cacheInterceptor", null, COMPLEX_CACHE_HELPER));
	}

	public Interceptor cacheInterceptor(ComplexCacheHelper<byte[], byte[]> helper) {
		logger.info("Cache compress configuration: {}", this.configuration.getCompress());
		try {
			return new ComplexCacheInterceptor(helper,
					new JacksonCacheKeyGenerator(JacksonUtils.create(new YAMLFactory())
							.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
							.include(Include.NON_NULL)));
		} catch (Exception e) {
			throw new ConfigurationException(e);
		}
	}

	public ComplexCacheHelper<byte[], byte[]> redisCacheHelper(IRedis dataRedis, IRedis versionRedis) {
		try {
			CacheChecker checker = new RedisCacheChecker(versionRedis);
			return ComplexCacheHelperBuilder.newBuilder().checker(checker)
					.remoteHolder(() -> new RedisCacheHolder(dataRedis, checker, this.configuration.getKeyPrefix()))
					.compressProp(this.configuration.getCompress())
					.compressor(this.configuration.getCompressorSupplier().newInstance())
					.localExpireAfterAccess(this.configuration.getLocalExpireAfterAccess())
					.localExpireAfterWrite(this.configuration.getLocalExpireAfterWrite())
					.localMaxNum(this.configuration.getLocalMaxNum())
					.remoteExpireAfterAccess(this.configuration.getRemoteExpireAfterAccess())
					.remoteExpireAfterWrite(this.configuration.getRemoteExpireAfterWrite()).build();
		} catch (Exception e) {
			throw new ConfigurationException(e);
		}
	}

}
