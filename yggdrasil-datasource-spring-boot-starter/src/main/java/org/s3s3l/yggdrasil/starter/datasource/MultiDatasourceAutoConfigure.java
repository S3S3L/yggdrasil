
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
import org.s3s3l.yggdrasil.orm.exec.DefaultSqlExecutor;
import org.s3s3l.yggdrasil.orm.exec.SqlExecutor;
import org.s3s3l.yggdrasil.orm.meta.MetaManager;
import org.s3s3l.yggdrasil.spring.BeanUtils;
import org.s3s3l.yggdrasil.starter.datasource.MultiDatasourceConfiguration.AutoSwitchDatasourceConfiguration;
import org.s3s3l.yggdrasil.starter.datasource.MultiDatasourceConfiguration.DatasourceName;
import org.s3s3l.yggdrasil.starter.datasource.MultiDatasourceConfiguration.ShardingDatasourceConfiguration;
import org.s3s3l.yggdrasil.starter.datasource.feature.AutoSwitchDatasource;
import org.s3s3l.yggdrasil.utils.collection.CollectionUtils;
import org.s3s3l.yggdrasil.utils.collection.MapBuilder;
import org.s3s3l.yggdrasil.utils.common.StringUtils;
import org.s3s3l.yggdrasil.utils.reflect.ReflectionsHelper;
import org.s3s3l.yggdrasil.utils.stuctural.jackson.JacksonUtils;
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
import lombok.extern.slf4j.Slf4j;

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
@Slf4j
@ConditionalOnMissingBean(DataSource.class)
@ConditionalOnProperty(prefix = MultiDatasourceConfiguration.PREFIX, name = "enable", havingValue = "true")
public class MultiDatasourceAutoConfigure implements ImportBeanDefinitionRegistrar, EnvironmentAware {
    private static final String BEAN_NAME = "multiDatasourceAutoConfigure";
    private static final String META_MANAGER_BEAN_NAME = "metaManager";
    /**
     * datasource bean名称后缀
     */
    private static final String DATASOURCE_TAIL = "Datasource";
    /**
     * executor bean名称后缀
     */
    private static final String EXECUTOR_TAIL = "Executor";
    /**
     * 分片datasource bean名称后缀
     */
    private static final String SHARDING_DATASOURCE_TAIL = "ShardingDatasource";
    /**
     * 可切换datasource bean名称后缀
     */
    private static final String SWITCHABLE_DATASOURCE_TAIL = "SwitchableDatasource";
    /**
     * TransactionManager bean名称后缀
     */
    private static final String TRANSACTION_TAIL = "TransactionManager";
    /**
     * SqlSessionFactory bean名称后缀
     */
    private static final String SQL_SESSION_FACTORY_TAIL = "SqlSessionFactory";
    /**
     * SessionTemplate bean名称后缀
     */
    private static final String SESSION_TEMPLATE_TAIL = "SessionTemplate";
    /**
     * 可切换数据源中，当前数据源 bean名称后缀
     */
    private static final String CURRENT_TAIL = "Current";
    /**
     * 可切换数据源中，待切换数据源 bean名称后缀
     */
    private static final String NEXT_TAIL = "Next";
    /**
     * spring环境变量
     */
    private Environment environment;
    /**
     * 多数据源配置
     */
    private MultiDatasourceConfiguration configuration;

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    /**
     * 注册bean
     */
    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata,
            BeanDefinitionRegistry registry) {
        try {
            // 注册当前工厂bean
            registry.registerBeanDefinition(BEAN_NAME, BeanUtils.buildBeanDefinition(null, null, null,
                    MultiDatasourceAutoConfigure.class));
            // 注册数据源相关bean
            multipleDatasource(resolveConfiguration(), registry);
        } catch (Exception e) {
            throw new BeanDefinitionStoreException(e.getMessage(), e);
        }
    }

    /**
     * 从spring环境变量中解析数据源相关配置
     * 
     * @return
     */
    private MultiDatasourceConfiguration resolveConfiguration() {
        PropertySourcesPlaceholdersResolver resolver = new PropertySourcesPlaceholdersResolver(
                ((ConfigurableEnvironment) environment).getPropertySources(),
                new PropertyPlaceholderHelper(SystemPropertyUtils.PLACEHOLDER_PREFIX,
                        SystemPropertyUtils.PLACEHOLDER_SUFFIX,
                        SystemPropertyUtils.VALUE_SEPARATOR, true));
        this.configuration = new Binder(ConfigurationPropertySources.get(environment), resolver)
                .bind(MultiDatasourceConfiguration.PREFIX, MultiDatasourceConfiguration.class)
                .orElse(new MultiDatasourceConfiguration());
        return configuration;
    }

    /**
     * 构建并注册数据源相关bean
     * 
     * @param config   数据源配置
     * @param registry 注册器
     * @throws IOException
     */
    private void multipleDatasource(MultiDatasourceConfiguration config, BeanDefinitionRegistry registry)
            throws IOException {
        log.trace("Starting auto-configuring multiple datasource.");
        if (log.isDebugEnabled()) {
            String configurationStr = JacksonUtils.JSON.toStructuralString(config);
            log.debug("Multiple datasource configuration. '{}'", configurationStr);
        }

        MybatisConfiguration mybatisConf = config.getMybatis();
        List<String> instances = config.getRequiredInstances();

        // 注册MetaManager
        registry.registerBeanDefinition(META_MANAGER_BEAN_NAME,
                BeanUtils.buildBeanDefinition(new Object[] { config.getTableDefinePackages() }, MetaManager.class));

        // 构建所有需要启动的数据源实例
        log.trace("Starting registering common datasources.");
        for (Entry<String, SwitchableDatasourceConfiguration> entry : config.getDbs()
                .entrySet()
                .stream()
                .filter(entry -> CollectionUtils.isEmpty(instances)
                        || instances.contains(entry.getKey()))
                .collect(Collectors.toList())) {
            String datasourceName = entry.getKey();
            SwitchableDatasourceConfiguration datasourceConfig = entry.getValue();

            String datasourceBeanName = datasourceName.concat(DATASOURCE_TAIL);
            log.trace("Starting building common datasource definition '{}'.", datasourceBeanName);
            BeanDefinition dataSourceDefinition = buildDatasourceDefinition(registry, datasourceConfig,
                    datasourceBeanName);
            log.trace("Finished building common datasource definition '{}'.", datasourceBeanName);
            registerDatasource(mybatisConf, registry, datasourceName, datasourceBeanName, dataSourceDefinition);

        }
        log.trace("Finished registering common datasources.");

        // 构建所有需要启动的分片数据源实例
        log.trace("Starting registering sharding datasources.");
        for (Entry<String, ShardingDatasourceConfiguration> entry : config.getSharding()
                .entrySet()
                .stream()
                .filter(entry -> CollectionUtils.isEmpty(instances)
                        || instances.contains(entry.getKey()))
                .collect(Collectors.toList())) {
            String datasourceName = entry.getKey();
            ShardingDatasourceConfiguration datasourceConfig = entry.getValue();

            String datasourceBeanName = datasourceName.concat(SHARDING_DATASOURCE_TAIL);
            log.trace("Starting building sharding datasource definition '{}'.", datasourceBeanName);
            BeanDefinition dataSourceDefinition = BeanUtils.buildBeanDefinitionForFactoryMethod(
                    ShardingDataSource.class, BEAN_NAME, "getShardingDataSource",
                    new Object[] { datasourceConfig });
            log.trace("Finished building sharding datasource definition '{}'.", datasourceBeanName);
            registerDatasource(mybatisConf, registry, datasourceName, datasourceBeanName, dataSourceDefinition);
        }
        log.trace("Finished registering sharding datasources.");

        // 构建所有需要启动的可切换数据源实例
        log.trace("Starting registering auto switch datasources.");
        for (Entry<String, AutoSwitchDatasourceConfiguration> entry : config.getAutoSwitchDbs()
                .entrySet()
                .stream()
                .filter(entry -> CollectionUtils.isEmpty(instances)
                        || instances.contains(entry.getKey()))
                .collect(Collectors.toList())) {
            String datasourceName = entry.getKey();
            AutoSwitchDatasourceConfiguration datasourceConfig = entry.getValue();

            String datasourceBeanName = datasourceName.concat(SWITCHABLE_DATASOURCE_TAIL);
            log.trace("Starting building auto switch datasource definition '{}'.", datasourceBeanName);
            LocalDateTime switchTime = datasourceConfig.getSwitchTime()
                    .toLocalDateTime();
            log.trace("Switch time '{}'", switchTime);
            BeanDefinition dataSourceDefinition = BeanUtils.buildBeanDefinitionForFactoryMethod(
                    DataSource.class, BEAN_NAME, "getAutoSwitchDataSource",
                    new Object[] { switchTime }, getDataSourceName(datasourceConfig.getCurrent()),
                    getDataSourceName(datasourceConfig.getNext()));
            log.trace("Finished building auto switch datasource definition '{}'.", datasourceBeanName);
            registerDatasource(mybatisConf, registry, datasourceName, datasourceBeanName, dataSourceDefinition);
        }
        log.trace("Finished registering auto switch datasources.");

        log.trace("Finished auto-configuring multiple datasource.");
    }

    /**
     * 获取数据源bean名称
     * 
     * @param name 数据源名称
     * @return
     */
    private String getDataSourceName(DatasourceName name) {
        switch (name.getType()) {
            case COMMON:
                return name.getName()
                        .concat(DATASOURCE_TAIL);
            case SHARDING:
                return name.getName()
                        .concat(SHARDING_DATASOURCE_TAIL);
            default:
                return name.getName();
        }
    }

    /**
     * 构建可切换数据源的BeanDefinition，并注册
     * 
     * @param registry         注册器
     * @param datasourceConfig 可切换数据源配置
     * @param datasourceName   数据源名称
     * @return
     */
    private BeanDefinition buildDatasourceDefinition(BeanDefinitionRegistry registry,
            SwitchableDatasourceConfiguration datasourceConfig,
            String datasourceName) {
        BeanDefinition dataSourceDefinition;

        // 检查是否配置了自动切换配置
        if (datasourceConfig.getSwitchConf() != null) {

            // 构建当前数据源，并注册
            String currentDatasourceName = datasourceName.concat(CURRENT_TAIL);
            log.trace("Starting building current datasource definition '{}'.", currentDatasourceName);
            BeanDefinition currentDataSourceDefinition = buildDataSourceBeanDefinition(datasourceConfig);
            log.trace("Finished building current datasource definition '{}'.", currentDatasourceName);
            registerDatasource(registry, currentDatasourceName, currentDataSourceDefinition);

            // 构建待切换数据源，并注册
            String nextDatasourceName = datasourceName.concat(NEXT_TAIL);
            log.trace("Starting building next datasource definition '{}'.", nextDatasourceName);
            BeanDefinition nextDataSourceDefinition = buildDataSourceBeanDefinition(datasourceConfig.getSwitchConf()
                    .getDb());
            log.trace("Finished building next datasource definition '{}'.", nextDatasourceName);
            registerDatasource(registry, nextDatasourceName, nextDataSourceDefinition);

            // 构建可切换数据源
            LocalDateTime switchTime = datasourceConfig.getSwitchConf()
                    .getTime()
                    .toLocalDateTime();
            log.trace("Switch time '{}'", switchTime);
            dataSourceDefinition = BeanUtils.buildBeanDefinitionForFactoryMethod(DataSource.class,
                    BEAN_NAME, "getAutoSwitchDataSource", new Object[] { switchTime },
                    currentDatasourceName, nextDatasourceName);
        } else {
            // 构建普通数据源
            dataSourceDefinition = buildDataSourceBeanDefinition(datasourceConfig);
        }
        return dataSourceDefinition;
    }

    /**
     * 构建普通数据源
     * 
     * @param datasourceConfiguration 数据源配置
     * @return
     */
    private BeanDefinition buildDataSourceBeanDefinition(DatasourceConfiguration datasourceConfiguration) {

        return BeanUtils.buildBeanDefinition(new MapBuilder<>(new HashMap<String, Object>())
                .put("poolProperties", datasourceConfiguration)
                .build(), null, null, org.apache.tomcat.jdbc.pool.DataSource.class);
    }

    /**
     * 注册数据源和mybatis相关BeanDefinition
     * 
     * @param mybatisConf        mybatis配置
     * @param registry           注册器
     * @param datasourceName     数据源名称
     * @param datasourceBeanName 数据源bean名称
     * @param dataSource         数据源BeanDefinition
     * @throws IOException
     */
    private void registerDatasource(MybatisConfiguration mybatisConf,
            BeanDefinitionRegistry registry,
            String datasourceName,
            String datasourceBeanName,
            BeanDefinition dataSource) throws IOException {
        // 注册数据源
        registerDatasource(registry, datasourceBeanName, dataSource);

        if (mybatisConf != null) {
            // 注册mybatis相关组件
            registerMybatisBean(mybatisConf, datasourceBeanName, registry, datasourceName);
        }
    }

    /**
     * 注册数据源
     * 
     * @param registry           注册器
     * @param datasourceBeanName 数据源bean名称
     * @param dataSource         数据源BeanDefinition
     */
    private void registerDatasource(BeanDefinitionRegistry registry,
            String datasourceBeanName,
            BeanDefinition dataSource) {
        // 注册数据源
        log.trace("Starting registering datasource definition '{}'.", datasourceBeanName);
        registry.registerBeanDefinition(datasourceBeanName, dataSource);
        log.trace("Finished registering datasource definition '{}'.", datasourceBeanName);

        // 注册数据源对应的SqlExecutor
        log.trace("Starting registering datasource executor definition '{}'.", datasourceBeanName);
        registry.registerBeanDefinition(datasourceBeanName + EXECUTOR_TAIL,
                BeanUtils.buildBeanDefinitionForFactoryMethod(DefaultSqlExecutor.class, BEAN_NAME,
                        "sqlExecutor",
                        null,
                        META_MANAGER_BEAN_NAME,
                        datasourceBeanName));
        log.trace("Finished registering datasource executor definition '{}'.", datasourceBeanName);
    }

    /**
     * 构建数据源对应的SqlExecutor
     * 
     * @param packages   数据结构定义类型所在包
     * @param datasource 数据源
     * @return
     */
    public SqlExecutor sqlExecutor(MetaManager metaManager, DataSource datasource) {
        return new DefaultSqlExecutor(datasource, metaManager);
    }

    /**
     * 构建并注册mybatis相关BeanDefinition
     * 
     * @param mybatisConf        mybatis配置
     * @param datasourceBeanName 数据源bean名称
     * @param registry           注册器
     * @param datasourceName     数据源名称
     * @throws IOException
     */
    private void registerMybatisBean(MybatisConfiguration mybatisConf,
            String datasourceBeanName,
            BeanDefinitionRegistry registry,
            String datasourceName) throws IOException {
        // register transaction manager
        String transactionName = datasourceName.concat(TRANSACTION_TAIL);
        log.trace("Starting building transaction manager definition '{}'", transactionName);
        BeanDefinition transactionManager = BeanUtils.buildBeanDefinition(null, null,
                new String[] { datasourceBeanName }, DataSourceTransactionManager.class);
        log.trace("Finished building transaction manager definition '{}'", transactionName);

        log.trace("Starting registering transaction manager definition '{}'", transactionName);
        registry.registerBeanDefinition(transactionName, transactionManager);
        log.trace("Finished registering transaction manager definition '{}'", transactionName);

        // register sqlSession factory
        String sqlSessionFactoryName = datasourceName.concat(SQL_SESSION_FACTORY_TAIL);
        log.trace("Starting building sqlSession factory definition '{}'", sqlSessionFactoryName);
        PathMatchingResourcePatternResolver pathResolver = new PathMatchingResourcePatternResolver();
        BeanDefinition sqlSessionFactoryBean = BeanUtils
                .buildBeanDefinition(
                        new MapBuilder<>(new HashMap<String, Object>())
                                .put("typeAliasesPackage",
                                        mybatisConf.getTypeAliasesPackage())
                                .put("configLocation",
                                        pathResolver.getResource(mybatisConf
                                                .getConfig()))
                                .put("mapperLocations",
                                        pathResolver.getResources(mybatisConf
                                                .getMapperLocations()))
                                .build(),
                        new MapBuilder<>(new HashMap<String, String>())
                                .put("dataSource", datasourceBeanName)
                                .build(),
                        null, SqlSessionFactoryBean.class);

        log.trace("Finished building sqlSession factory definition '{}'", sqlSessionFactoryName);

        log.trace("Starting registering sqlSession factory definition '{}'", sqlSessionFactoryName);
        registry.registerBeanDefinition(sqlSessionFactoryName, sqlSessionFactoryBean);
        log.trace("Finished registering sqlSession factory definition '{}'", sqlSessionFactoryName);

        // register mapper
        String sessionTemplateName = datasourceName.concat(SESSION_TEMPLATE_TAIL);
        BeanDefinition sessionTemplate = BeanUtils.buildBeanDefinition(null, null,
                new String[] { sqlSessionFactoryName }, SqlSessionTemplate.class);
        registry.registerBeanDefinition(sessionTemplateName, sessionTemplate);
        for (Class<?> mapperType : ReflectionsHelper.getAllTypes(Optional
                .ofNullable(mybatisConf.getMapperPackages()
                        .get(datasourceName))
                .orElse(StringUtils.EMPTY_STRING)
                .concat("."))) {
            String mapperName = mapperType.getSimpleName();

            log.trace("Starting registering mapper definition '{}'", mapperName);
            BeanDefinition mapper = BeanUtils.buildBeanDefinitionForFactoryMethod(mapperType,
                    sessionTemplateName, "getMapper", new Object[] { mapperType });
            registry.registerBeanDefinition(mapperName, mapper);
            log.trace("Finished registering mapper definition '{}'", mapperName);
        }
    }

    /**
     * 构建自动切换数据源
     * 
     * @param switchTime 切换时间
     * @param current    当前数据源
     * @param next       待切换数据源
     * @return
     */
    public DataSource getAutoSwitchDataSource(LocalDateTime switchTime, DataSource current, DataSource next) {
        AutoSwitchDatasource autoSwitchDatasource = new AutoSwitchDatasource();
        autoSwitchDatasource.setCurrent(current);
        autoSwitchDatasource.setNext(next);
        autoSwitchDatasource.setSwitchTime(switchTime);
        return autoSwitchDatasource;
    }

    /**
     * 构建分片数据源
     * 
     * @param datasourceConfiguration 数据源配置
     * @return
     * @throws Exception
     */
    public DataSource getShardingDataSource(ShardingDatasourceConfiguration datasourceConfiguration)
            throws Exception {
        if (log.isTraceEnabled()) {
            String configurationStr = JacksonUtils.create()
                    .prettyPrinter()
                    .toStructuralString(datasourceConfiguration);
            log.trace("sharding configuration: {}", configurationStr);
        }
        YamlShardingRuleConfiguration ruleConfiguration = datasourceConfiguration.getRule();
        ShardingRuleConfiguration shardingRuleConfiguration = ruleConfiguration.getShardingRuleConfiguration();

        return ShardingDataSourceFactory.createDataSource(buildDataSourceMap(datasourceConfiguration.getDbs()),
                shardingRuleConfiguration, datasourceConfiguration.getConfigMap(),
                datasourceConfiguration.getProps());
    }

    /**
     * 构建数据源Map
     * 
     * @param dsConfs 数据源配置
     * @return
     * @throws Exception
     */
    private Map<String, DataSource> buildDataSourceMap(Map<String, SwitchableDatasourceConfiguration> dsConfs)
            throws Exception {
        Map<String, DataSource> dsMap = new HashMap<>();

        log.trace("Starting building sharding datasource map");
        for (Entry<String, SwitchableDatasourceConfiguration> entry : dsConfs.entrySet()) {
            String key = entry.getKey();
            SwitchableDatasourceConfiguration value = entry.getValue();

            log.trace("Starting building sharding db '{}'", key);
            DataSource dataSource = getSwitchableDataSource(value);
            log.trace("Finished building sharding db '{}'", key);

            dsMap.put(key, dataSource);
        }
        log.trace("Finished building sharding datasource map");

        return dsMap;
    }

    /**
     * 构建可切换数据源
     * 
     * @param datasourceConfiguration 可切换数据源配置
     * @return
     * @throws Exception
     */
    private DataSource getSwitchableDataSource(SwitchableDatasourceConfiguration datasourceConfiguration)
            throws Exception {
        DataSource current = getDataSource(datasourceConfiguration);
        if (datasourceConfiguration.getSwitchConf() == null) {
            return current;
        }

        AutoSwitchDatasource switchDatasource = new AutoSwitchDatasource();

        switchDatasource.setCurrent(current);
        switchDatasource.setNext(getDataSource(datasourceConfiguration.getSwitchConf()
                .getDb()));
        switchDatasource.setSwitchTime(datasourceConfiguration.getSwitchConf()
                .getTime()
                .toLocalDateTime());
        return switchDatasource;
    }

    /**
     * 构建普通数据源
     * 
     * @param datasourceConfiguration 普通数据源配置
     * @return
     * @throws Exception
     */
    private DataSource getDataSource(DatasourceConfiguration datasourceConfiguration) throws Exception {
        org.apache.tomcat.jdbc.pool.DataSource datasource = new org.apache.tomcat.jdbc.pool.DataSource();
        datasource.setPoolProperties(datasourceConfiguration);
        return datasource;
    }

}
