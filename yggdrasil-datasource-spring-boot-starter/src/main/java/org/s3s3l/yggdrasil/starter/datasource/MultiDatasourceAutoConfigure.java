
package org.s3s3l.yggdrasil.starter.datasource;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.sql.DataSource;

import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.s3s3l.yggdrasil.configuration.datasource.DatasourceConfiguration;
import org.s3s3l.yggdrasil.configuration.datasource.SwitchableDatasourceConfiguration;
import org.s3s3l.yggdrasil.configuration.mybatis.MybatisConfiguration;
import org.s3s3l.yggdrasil.starter.datasource.MultiDatasourceConfiguration.AutoSwitchDatasourceConfiguration;
import org.s3s3l.yggdrasil.starter.datasource.MultiDatasourceConfiguration.DatasourceName;
import org.s3s3l.yggdrasil.starter.datasource.MultiDatasourceConfiguration.ShardingDatasourceConfiguration;
import org.s3s3l.yggdrasil.utils.collection.CollectionUtils;
import org.s3s3l.yggdrasil.utils.collection.MapBuilder;
import org.s3s3l.yggdrasil.utils.common.StringUtils;
import org.s3s3l.yggdrasil.utils.datasource.AutoSwitchDatasource;
import org.s3s3l.yggdrasil.utils.json.JacksonUtils;
import org.s3s3l.yggdrasil.utils.reflect.ReflectionsHelper;
import org.s3s3l.yggdrasil.utils.spring.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanDefinitionStoreException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.boot.context.properties.bind.PropertySourcesPlaceholdersResolver;
import org.springframework.boot.context.properties.source.ConfigurationPropertySources;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.util.PropertyPlaceholderHelper;
import org.springframework.util.SystemPropertyUtils;

import io.shardingsphere.api.config.rule.ShardingRuleConfiguration;
import io.shardingsphere.core.yaml.sharding.YamlShardingRuleConfiguration;
import io.shardingsphere.shardingjdbc.api.ShardingDataSourceFactory;
import io.shardingsphere.shardingjdbc.jdbc.core.datasource.ShardingDataSource;

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
@ConditionalOnMissingBean(DataSource.class)
@ConditionalOnProperty(prefix = MultiDatasourceConfiguration.PREFIX, name = "enable", havingValue = "true")
public class MultiDatasourceAutoConfigure implements ImportBeanDefinitionRegistrar, EnvironmentAware {
	private static final String BEAN_NAME = "multiDatasourceAutoConfigure";
	private static final String DATASOURCE_TAIL = "Datasource";
	private static final String SHARDING_DATASOURCE_TAIL = "ShardingDatasource";
	private static final String SWITCHABLE_DATASOURCE_TAIL = "SwitchableDatasource";
	private static final String TRANSACTION_TAIL = "TransactionManager";
	private static final String SQL_SESSION_FACTORY_TAIL = "SqlSessionFactory";
	private static final String SESSION_TEMPLATE_TAIL = "SessionTemplate";
	private static final String CURRENT_TAIL = "Current";
	private static final String NEXT_TAIL = "Next";
	private final Logger logger = LoggerFactory.getLogger(getClass());
	private Environment environment;

	@Override
	public void setEnvironment(Environment environment) {
		this.environment = environment;
	}

	@Override
	public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
		try {
			registry.registerBeanDefinition(BEAN_NAME,
					BeanUtils.buildBeanDefinition(null, null, null, MultiDatasourceAutoConfigure.class));
			multipleDatasource(resolveConfiguration(), registry);
		} catch (Exception e) {
			throw new BeanDefinitionStoreException(e.getMessage(), e);
		}
	}

	private MultiDatasourceConfiguration resolveConfiguration() {
		PropertySourcesPlaceholdersResolver resolver = new PropertySourcesPlaceholdersResolver(
				((ConfigurableEnvironment) environment).getPropertySources(),
				new PropertyPlaceholderHelper(SystemPropertyUtils.PLACEHOLDER_PREFIX,
						SystemPropertyUtils.PLACEHOLDER_SUFFIX, SystemPropertyUtils.VALUE_SEPARATOR, true));
		return new Binder(ConfigurationPropertySources.get(environment), resolver)
				.bind(MultiDatasourceConfiguration.PREFIX, MultiDatasourceConfiguration.class)
				.orElse(new MultiDatasourceConfiguration());
	}

	public void multipleDatasource(MultiDatasourceConfiguration config, BeanDefinitionRegistry registry)
			throws Exception {
		logger.trace("Starting auto-configuring multiple datasource.");
		String configurationStr = JacksonUtils.defaultHelper.toJsonString(config);
		logger.debug("Multiple datasource configuration. '{}'", configurationStr);
		MybatisConfiguration mybatisConf = config.getMybatis();
		List<String> instances = config.getRequiredInstances();

		logger.trace("Starting registering common datasources.");
		for (Entry<String, SwitchableDatasourceConfiguration> entry : config.getDbs().entrySet().stream()
				.filter(entry -> CollectionUtils.isEmpty(instances) || instances.contains(entry.getKey()))
				.collect(Collectors.toList())) {
			String key = entry.getKey();
			SwitchableDatasourceConfiguration value = entry.getValue();

			String datasourceName = key.concat(DATASOURCE_TAIL);
			logger.trace("Starting building common datasource definition '{}'.", datasourceName);
			BeanDefinition dataSourceDefinition = buildDatasourceDefinition(registry, value, datasourceName);
			logger.trace("Finished building common datasource definition '{}'.", datasourceName);
			registerDatasource(mybatisConf, registry, key, datasourceName, dataSourceDefinition);

		}
		logger.trace("Finished registering common datasources.");

		logger.trace("Starting registering sharding datasources.");
		for (Entry<String, ShardingDatasourceConfiguration> entry : config.getSharding().entrySet().stream()
				.filter(entry -> CollectionUtils.isEmpty(instances) || instances.contains(entry.getKey()))
				.collect(Collectors.toList())) {
			String key = entry.getKey();
			ShardingDatasourceConfiguration value = entry.getValue();

			String datasourceName = key.concat(SHARDING_DATASOURCE_TAIL);
			logger.trace("Starting building sharding datasource definition '{}'.", datasourceName);
			BeanDefinition dataSourceDefinition = BeanUtils.buildBeanDefinitionForFactoryMethod(
					ShardingDataSource.class, BEAN_NAME, "getShardingDataSource", new Object[] { value });
			logger.trace("Finished building sharding datasource definition '{}'.", datasourceName);
			registerDatasource(mybatisConf, registry, key, datasourceName, dataSourceDefinition);
		}
		logger.trace("Finished registering sharding datasources.");

		logger.trace("Starting registering auto switch datasources.");
		for (Entry<String, AutoSwitchDatasourceConfiguration> entry : config.getAutoSwitchDbs().entrySet().stream()
				.filter(entry -> CollectionUtils.isEmpty(instances) || instances.contains(entry.getKey()))
				.collect(Collectors.toList())) {
			String key = entry.getKey();
			AutoSwitchDatasourceConfiguration value = entry.getValue();

			String datasourceName = key.concat(SWITCHABLE_DATASOURCE_TAIL);
			logger.trace("Starting building auto switch datasource definition '{}'.", datasourceName);
			LocalDateTime switchTime = value.getSwitchTime().toLocalDateTime();
			logger.trace("Switch time '{}'", switchTime);
			BeanDefinition dataSourceDefinition = BeanUtils.buildBeanDefinitionForFactoryMethod(DataSource.class,
					BEAN_NAME, "getAutoSwitchDataSource", new Object[] { switchTime },
					getDataSourceName(value.getCurrent()), getDataSourceName(value.getNext()));
			logger.trace("Finished building auto switch datasource definition '{}'.", datasourceName);
			registerDatasource(mybatisConf, registry, key, datasourceName, dataSourceDefinition);
		}
		logger.trace("Finished registering auto switch datasources.");

		logger.trace("Finished auto-configuring multiple datasource.");
	}

	private String getDataSourceName(DatasourceName name) {
		switch (name.getType()) {
		case COMMON:
			return name.getName().concat(DATASOURCE_TAIL);
		case SHARDING:
			return name.getName().concat(SHARDING_DATASOURCE_TAIL);
		default:
			return name.getName();
		}
	}

	private BeanDefinition buildDatasourceDefinition(BeanDefinitionRegistry registry,
			SwitchableDatasourceConfiguration value, String datasourceName) throws Exception {
		BeanDefinition dataSourceDefinition;
		if (value.getSwitchConf() != null) {
			String currentDatasourceName = datasourceName.concat(CURRENT_TAIL);
			logger.trace("Starting building current datasource definition '{}'.", currentDatasourceName);
			BeanDefinition currentDataSourceDefinition = buildDataSourceBeanDefinition(value);
			logger.trace("Finished building current datasource definition '{}'.", currentDatasourceName);
			registerDatasource(registry, currentDatasourceName, currentDataSourceDefinition);

			String nextDatasourceName = datasourceName.concat(NEXT_TAIL);
			logger.trace("Starting building next datasource definition '{}'.", nextDatasourceName);
			BeanDefinition nextDataSourceDefinition = buildDataSourceBeanDefinition(value.getSwitchConf().getDb());
			logger.trace("Finished building next datasource definition '{}'.", nextDatasourceName);
			registerDatasource(registry, nextDatasourceName, nextDataSourceDefinition);

			LocalDateTime switchTime = value.getSwitchConf().getTime().toLocalDateTime();
			logger.trace("Switch time '{}'", switchTime);
			dataSourceDefinition = BeanUtils.buildBeanDefinitionForFactoryMethod(DataSource.class, BEAN_NAME,
					"getAutoSwitchDataSource", new Object[] { switchTime }, currentDatasourceName, nextDatasourceName);
		} else {
			dataSourceDefinition = buildDataSourceBeanDefinition(value);
		}
		return dataSourceDefinition;
	}

	private BeanDefinition buildDataSourceBeanDefinition(DatasourceConfiguration datasourceConfiguration)
			throws Exception {

		return BeanUtils.buildBeanDefinition(
				new MapBuilder<>(new HashMap<String, Object>()).put("poolProperties", datasourceConfiguration).build(),
				null, null, org.apache.tomcat.jdbc.pool.DataSource.class);
	}

	private void registerDatasource(MybatisConfiguration mybatisConf, BeanDefinitionRegistry registry, String key,
			String datasourceName, BeanDefinition dataSource) throws IOException {
		registerDatasource(registry, datasourceName, dataSource);

		registerMybatisBean(mybatisConf, datasourceName, registry, key);
	}

	private void registerDatasource(BeanDefinitionRegistry registry, String datasourceName, BeanDefinition dataSource) {
		logger.trace("Starting registering datasource definition '{}'.", datasourceName);
		registry.registerBeanDefinition(datasourceName, dataSource);
		logger.trace("Finished registering datasource definition '{}'.", datasourceName);
	}

	private void registerMybatisBean(MybatisConfiguration mybatisConf, String datasourceName,
			BeanDefinitionRegistry registry, String key) throws IOException {
		// register transaction manager
		String transactionName = key.concat(TRANSACTION_TAIL);
		logger.trace("Starting building transaction manager definition '{}'", transactionName);
		BeanDefinition transactionManager = BeanUtils.buildBeanDefinition(null, null, new String[] { datasourceName },
				DataSourceTransactionManager.class);
		logger.trace("Finished building transaction manager definition '{}'", transactionName);

		logger.trace("Starting registering transaction manager definition '{}'", transactionName);
		registry.registerBeanDefinition(transactionName, transactionManager);
		logger.trace("Finished registering transaction manager definition '{}'", transactionName);

		// register sqlSession factory
		String sqlSessionFactoryName = key.concat(SQL_SESSION_FACTORY_TAIL);
		logger.trace("Starting building sqlSession factory definition '{}'", sqlSessionFactoryName);
		PathMatchingResourcePatternResolver pathResolver = new PathMatchingResourcePatternResolver();
		BeanDefinition sqlSessionFactoryBean = BeanUtils.buildBeanDefinition(
				new MapBuilder<>(new HashMap<String, Object>())
						.put("typeAliasesPackage", mybatisConf.getTypeAliasesPackage())
						.put("configLocation", pathResolver.getResource(mybatisConf.getConfig()))
						.put("mapperLocations", pathResolver.getResources(mybatisConf.getMapperLocations())).build(),
				new MapBuilder<>(new HashMap<String, String>()).put("dataSource", datasourceName).build(), null,
				SqlSessionFactoryBean.class);

		logger.trace("Finished building sqlSession factory definition '{}'", sqlSessionFactoryName);

		logger.trace("Starting registering sqlSession factory definition '{}'", sqlSessionFactoryName);
		registry.registerBeanDefinition(sqlSessionFactoryName, sqlSessionFactoryBean);
		logger.trace("Finished registering sqlSession factory definition '{}'", sqlSessionFactoryName);

		// register mapper
		String sessionTemplateName = key.concat(SESSION_TEMPLATE_TAIL);
		BeanDefinition sessionTemplate = BeanUtils.buildBeanDefinition(null, null,
				new String[] { sqlSessionFactoryName }, SqlSessionTemplate.class);
		registry.registerBeanDefinition(sessionTemplateName, sessionTemplate);
		for (Class<?> mapperType : ReflectionsHelper.getAllTypes(Optional
				.ofNullable(mybatisConf.getMapperPackages().get(key)).orElse(StringUtils.EMPTY_STRING).concat("."))) {
			String mapperName = mapperType.getSimpleName();

			logger.trace("Starting registering mapper definition '{}'", mapperName);
			BeanDefinition mapper = BeanUtils.buildBeanDefinitionForFactoryMethod(mapperType, sessionTemplateName,
					"getMapper", new Object[] { mapperType });
			registry.registerBeanDefinition(mapperName, mapper);
			logger.trace("Finished registering mapper definition '{}'", mapperName);
		}
	}

	public DataSource getShardingDataSource(ShardingDatasourceConfiguration datasourceConfiguration) throws Exception {
		String configurationStr = JacksonUtils.create().prettyPrinter().toJsonString(datasourceConfiguration);
		logger.trace("sharding configuration: {}", configurationStr);
		YamlShardingRuleConfiguration ruleConfiguration = datasourceConfiguration.getRule();
		ShardingRuleConfiguration shardingRuleConfiguration = ruleConfiguration.getShardingRuleConfiguration();

		return ShardingDataSourceFactory.createDataSource(buildDataSourceMap(datasourceConfiguration.getDbs()),
				shardingRuleConfiguration, datasourceConfiguration.getConfigMap(), datasourceConfiguration.getProps());
	}

	public DataSource getAutoSwitchDataSource(LocalDateTime switchTime, DataSource current, DataSource next) {
		AutoSwitchDatasource autoSwitchDatasource = new AutoSwitchDatasource();
		autoSwitchDatasource.setCurrent(current);
		autoSwitchDatasource.setNext(next);
		autoSwitchDatasource.setSwitchTime(switchTime);
		return autoSwitchDatasource;
	}

	private Map<String, DataSource> buildDataSourceMap(Map<String, SwitchableDatasourceConfiguration> dsConfs)
			throws Exception {
		Map<String, DataSource> dsMap = new HashMap<>();

		logger.trace("Starting building sharding datasource map");
		for (Entry<String, SwitchableDatasourceConfiguration> entry : dsConfs.entrySet()) {
			String key = entry.getKey();
			SwitchableDatasourceConfiguration value = entry.getValue();

			logger.trace("Starting building sharding db '{}'", key);
			DataSource dataSource = getSwitchableDataSource(value);
			logger.trace("Finished building sharding db '{}'", key);

			dsMap.put(key, dataSource);
		}
		logger.trace("Finished building sharding datasource map");

		return dsMap;
	}

	private DataSource getSwitchableDataSource(SwitchableDatasourceConfiguration datasourceConfiguration)
			throws Exception {
		DataSource current = getDataSource(datasourceConfiguration);
		if (datasourceConfiguration.getSwitchConf() == null) {
			return current;
		}

		AutoSwitchDatasource switchDatasource = new AutoSwitchDatasource();

		switchDatasource.setCurrent(current);
		switchDatasource.setNext(getDataSource(datasourceConfiguration.getSwitchConf().getDb()));
		switchDatasource.setSwitchTime(datasourceConfiguration.getSwitchConf().getTime().toLocalDateTime());
		return switchDatasource;
	}

	private DataSource getDataSource(DatasourceConfiguration datasourceConfiguration) throws Exception {
		org.apache.tomcat.jdbc.pool.DataSource datasource = new org.apache.tomcat.jdbc.pool.DataSource();
		datasource.setPoolProperties(datasourceConfiguration);
		return datasource;
	}

}
