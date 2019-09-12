package org.s3s3l.yggdrasil.starter.redis;

import java.util.Collections;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;

import org.s3s3l.yggdrasil.starter.redis.feature.RedisConfigurationResolver;
import org.s3s3l.yggdrasil.utils.collection.CollectionUtils;
import org.s3s3l.yggdrasil.utils.redis.JedisClusterHelper;
import org.s3s3l.yggdrasil.utils.redis.RedisClusterHelper;
import org.s3s3l.yggdrasil.utils.redis.base.IRedis;
import org.s3s3l.yggdrasil.utils.redis.base.InitializableRedis;
import org.s3s3l.yggdrasil.utils.spring.BeanUtils;
import org.s3s3l.yggdrasil.utils.spring.env.ConfigurationResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanDefinitionStoreException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotationMetadata;

/**
 * <p>
 * </p>
 * ClassName:RedisAutoConfigure <br>
 * Date: Sep 10, 2018 9:00:44 PM <br>
 * 
 * @author kehw_zwei
 * @version 1.0.0
 * @since JDK 1.8
 */
@ConditionalOnMissingBean(IRedis.class)
@ConditionalOnProperty(prefix = DefaultRedisClientConfiguration.PREFIX, name = "enable", havingValue = "true")
public class MultiRedisAutoConfigure implements ImportBeanDefinitionRegistrar, EnvironmentAware {
	private static final String BEAN_NAME = "multiRedisAutoConfigure";
	private static final String MASTER_SLAVE_TAIL = "Redis";
	private static final String CLUSTER_TAIL = "RedisCluster";

	private final Logger logger = LoggerFactory.getLogger(getClass());
	private final ConfigurationResolver<DefaultRedisClientConfiguration> resolver = new RedisConfigurationResolver();

	private DefaultRedisClientConfiguration configuration;
	private Environment environment;

	@Override
	public void setEnvironment(Environment environment) {
		this.environment = environment;
	}

	@Override
	public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
		try {
			registry.registerBeanDefinition(BEAN_NAME,
					BeanUtils.buildBeanDefinition(null, null, null, MultiRedisAutoConfigure.class));
			this.configuration = this.resolver.resolve(this.environment);
			multipleRedis(registry);
		} catch (Exception e) {
			throw new BeanDefinitionStoreException(e.getMessage(), e);
		}
	}

	public void multipleRedis(BeanDefinitionRegistry registry) {
		logger.trace("Starting auto-configuring multiple redis.");
		logger.debug("Multiple redis configuration. '{}'", this.configuration);
		Set<String> instances = Optional.ofNullable(this.configuration.getRequiredInstances())
				.orElse(Collections.emptySet());

		logger.trace("Starting registering master-slave redises.");
		this.configuration.getMasterSlave().entrySet().stream()
				.filter(entry -> CollectionUtils.isEmpty(instances) || instances.contains(entry.getKey()))
				.forEach(entry -> registerRedisBeanDefinition(entry, MASTER_SLAVE_TAIL, registry,
						JedisClusterHelper.class));
		logger.trace("Finished registering master-slave redises.");

		logger.trace("Starting registering cluster redises.");
		this.configuration.getCluster().entrySet().stream()
				.filter(entry -> CollectionUtils.isEmpty(instances) || instances.contains(entry.getKey()))
				.forEach(entry -> registerRedisBeanDefinition(entry, CLUSTER_TAIL, registry, RedisClusterHelper.class));
		logger.trace("Finished registering cluster redises.");

		logger.trace("Finished auto-configuring multiple redis.");
	}

	private <T> void registerRedisBeanDefinition(Entry<String, T> entry, String nameTail,
			BeanDefinitionRegistry registry, Class<? extends InitializableRedis<T>> type) {
		String key = entry.getKey();
		T value = entry.getValue();
		String beanName = key.concat(nameTail);

		logger.trace("Starting building redis definition '{}'.", beanName);
		BeanDefinition redis = BeanUtils.buildBeanDefinitionForFactoryMethod(IRedis.class, BEAN_NAME, "redis",
				new Object[] { type, value });
		if (this.configuration.getPrimary() != null && key.equals(this.configuration.getPrimary())) {
			redis.setPrimary(true);
		}
		logger.trace("Finished building redis definition '{}'.", beanName);

		logger.trace("Starting registering redis definition '{}'.", beanName);
		registry.registerBeanDefinition(beanName, redis);
		logger.trace("Finished registering redis definition '{}'.", beanName);
	}

	public <T, U extends InitializableRedis<T>> IRedis redis(Class<U> type, T configuration)
			throws InstantiationException, IllegalAccessException {
		U redis = type.newInstance();
		redis.init(configuration);
		return redis;
	}
}
