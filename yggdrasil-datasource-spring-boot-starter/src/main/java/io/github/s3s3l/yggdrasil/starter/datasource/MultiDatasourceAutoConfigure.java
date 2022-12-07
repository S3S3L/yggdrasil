
package io.github.s3s3l.yggdrasil.starter.datasource;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.sql.DataSource;

import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import io.github.s3s3l.yggdrasil.configuration.mybatis.MybatisConfiguration;
import io.github.s3s3l.yggdrasil.starter.datasource.MultiDatasourceConfiguration.AutoSwitchDatasourceConfiguration;
import io.github.s3s3l.yggdrasil.starter.datasource.MultiDatasourceConfiguration.DatasourceName;
import io.github.s3s3l.yggdrasil.starter.datasource.MultiDatasourceConfiguration.ShardingDatasourceConfiguration;
import io.github.s3s3l.yggdrasil.starter.datasource.config.DatasourceConfiguration;
import io.github.s3s3l.yggdrasil.starter.datasource.config.SwitchableDatasourceConfiguration;
import io.github.s3s3l.yggdrasil.starter.datasource.feature.DatasourceFactory;
import io.github.s3s3l.yggdrasil.utils.collection.CollectionUtils;
import io.github.s3s3l.yggdrasil.utils.common.StringUtils;
import io.github.s3s3l.yggdrasil.utils.reflect.ReflectionsHelper;
import io.github.s3s3l.yggdrasil.utils.reflect.scan.ClassScanner;
import io.github.s3s3l.yggdrasil.utils.stuctural.jackson.JacksonUtils;
import org.springframework.beans.factory.BeanDefinitionStoreException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
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

import io.github.s3s3l.yggdrasil.orm.bind.annotation.ExecutorProxy;
import io.github.s3s3l.yggdrasil.orm.exec.DefaultSqlExecutor;
import io.github.s3s3l.yggdrasil.orm.meta.MetaManager;
import io.github.s3s3l.yggdrasil.orm.meta.MetaManagerConfig;
import io.github.s3s3l.yggdrasil.spring.BeanUtils;
import io.github.s3s3l.yggdrasil.starter.datasource.feature.SqlExecutorFactory;
import io.github.s3s3l.yggdrasil.utils.collection.MapBuilder;
import io.github.s3s3l.yggdrasil.utils.reflect.ReflectionUtils;
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
public class MultiDatasourceAutoConfigure implements ImportBeanDefinitionRegistrar, EnvironmentAware {
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
     * executor proxy bean名称后缀
     */
    private static final String EXECUTOR_PROXY_TAIL = "ExecutorProxy";
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
            // 注册工厂bean
            registry.registerBeanDefinition(
                    DatasourceFactory.BEAN_NAME, BeanUtils.buildBeanDefinition(null, null, null,
                            DatasourceFactory.class));
            registry.registerBeanDefinition(
                    SqlExecutorFactory.BEAN_NAME, BeanUtils.buildBeanDefinition(null, null, null,
                            SqlExecutorFactory.class));
            // 注册数据源相关bean
            MultiDatasourceConfiguration resolvedConfiguration = resolveConfiguration();
            if (!resolvedConfiguration.isEnable()) {
                return;
            }
            multipleDatasource(resolvedConfiguration, registry);
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
                BeanUtils.buildBeanDefinition(new Object[] { config.getMeta() }, MetaManager.class));

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
            registerDatasource(mybatisConf, registry, datasourceName, datasourceBeanName, dataSourceDefinition,
                    datasourceConfig);

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
                    ShardingDataSource.class,
                    DatasourceFactory.BEAN_NAME, "getShardingDataSource",
                    new Object[] { datasourceConfig });
            log.trace("Finished building sharding datasource definition '{}'.", datasourceBeanName);
            registerDatasource(mybatisConf, registry, datasourceName, datasourceBeanName, dataSourceDefinition, null);
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
                    DataSource.class,
                    DatasourceFactory.BEAN_NAME, "getAutoSwitchDataSource",
                    new Object[] { switchTime }, getDataSourceName(datasourceConfig.getCurrent()),
                    getDataSourceName(datasourceConfig.getNext()));
            log.trace("Finished building auto switch datasource definition '{}'.", datasourceBeanName);
            registerDatasource(mybatisConf, registry, datasourceName, datasourceBeanName, dataSourceDefinition, null);
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
            registerDatasource(registry, currentDatasourceName, currentDataSourceDefinition,
                    datasourceConfig);

            // 构建待切换数据源，并注册
            String nextDatasourceName = datasourceName.concat(NEXT_TAIL);
            log.trace("Starting building next datasource definition '{}'.", nextDatasourceName);
            BeanDefinition nextDataSourceDefinition = buildDataSourceBeanDefinition(datasourceConfig.getSwitchConf()
                    .getDb());
            log.trace("Finished building next datasource definition '{}'.", nextDatasourceName);
            registerDatasource(registry, nextDatasourceName, nextDataSourceDefinition,
                    datasourceConfig.getSwitchConf().getDb());

            // 构建可切换数据源
            LocalDateTime switchTime = datasourceConfig.getSwitchConf()
                    .getTime()
                    .toLocalDateTime();
            log.trace("Switch time '{}'", switchTime);
            dataSourceDefinition = BeanUtils.buildBeanDefinitionForFactoryMethod(DataSource.class,
                    DatasourceFactory.BEAN_NAME, "getAutoSwitchDataSource", new Object[] { switchTime },
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
            BeanDefinition dataSource,
            DatasourceConfiguration datasourceConfiguration) throws IOException {
        // 注册数据源
        registerDatasource(registry, datasourceBeanName, dataSource, datasourceConfiguration);

        registerTransactionManager(registry, datasourceName, datasourceBeanName);

        if (mybatisConf != null) {
            // 注册mybatis相关组件
            registerMybatisBean(mybatisConf, datasourceBeanName, registry, datasourceName);
        }
    }

    private void registerTransactionManager(BeanDefinitionRegistry registry, String datasourceName,
            String datasourceBeanName) {
        // register transaction manager
        String transactionName = datasourceName.concat(TRANSACTION_TAIL);
        log.trace("Starting building transaction manager definition '{}'", transactionName);
        BeanDefinition transactionManager = BeanUtils.buildBeanDefinition(null, null,
                new String[] { datasourceBeanName }, DataSourceTransactionManager.class);
        log.trace("Finished building transaction manager definition '{}'", transactionName);

        log.trace("Starting registering transaction manager definition '{}'", transactionName);
        registry.registerBeanDefinition(transactionName, transactionManager);
        log.trace("Finished registering transaction manager definition '{}'", transactionName);
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
            BeanDefinition dataSource,
            DatasourceConfiguration datasourceConfiguration) {
        // 注册数据源
        log.trace("Starting registering datasource definition '{}'.", datasourceBeanName);
        registry.registerBeanDefinition(datasourceBeanName, dataSource);
        log.trace("Finished registering datasource definition '{}'.", datasourceBeanName);

        MetaManagerConfig specificMetaConfig = datasourceConfiguration.getMeta();
        MetaManager metaManager = null;
        boolean isSpecificMetaConfig = specificMetaConfig != null;
        if (isSpecificMetaConfig) {
            MetaManagerConfig metaConfig = ReflectionUtils.clone(this.configuration.getMeta(), MetaManagerConfig.class);
            metaConfig = JacksonUtils.NONNULL_JSON.update(metaConfig,
                    JacksonUtils.NONNULL_JSON.valueToTree(datasourceConfiguration.getMeta()));
            metaManager = new MetaManager(metaConfig);
        }

        // 注册数据源对应的SqlExecutor
        log.trace("Starting registering datasource executor definition of database: '{}'.", datasourceBeanName);
        String sqlExecutorName = datasourceBeanName + EXECUTOR_TAIL;
        if (isSpecificMetaConfig) {
            registry.registerBeanDefinition(sqlExecutorName,
                    BeanUtils.buildBeanDefinitionForFactoryMethod(DefaultSqlExecutor.class,
                            SqlExecutorFactory.BEAN_NAME,
                            "sqlExecutor",
                            new Object[] { datasourceConfiguration, metaManager },
                            datasourceBeanName));
        } else {
            registry.registerBeanDefinition(sqlExecutorName,
                    BeanUtils.buildBeanDefinitionForFactoryMethod(DefaultSqlExecutor.class,
                            SqlExecutorFactory.BEAN_NAME,
                            "sqlExecutor",
                            new Object[] { datasourceConfiguration },
                            META_MANAGER_BEAN_NAME,
                            datasourceBeanName));
        }
        log.trace("Register bean SqlExecutor: {}", sqlExecutorName);
        log.trace("Finished registering datasource executor definition of database: '{}'.", datasourceBeanName);

        // 注册数据源对应的ExecutorProxy
        log.trace("Starting registering executor proxy definition of database: '{}'.", datasourceBeanName);
        Set<Class<?>> proxyTypes = null;
        if (metaManager != null) {
            proxyTypes = metaManager.allProxies();
        } else {
            proxyTypes = new ClassScanner().scan(this.configuration.getMeta().getProxyDefinePackages()).stream()
                    .filter(type -> ReflectionUtils.isAnnotationedWith(type, ExecutorProxy.class))
                    .collect(Collectors.toSet());
        }
        for (Class<?> proxyType : proxyTypes) {
            String executorProxyName = datasourceBeanName + proxyType.getSimpleName() + EXECUTOR_PROXY_TAIL;
            registry.registerBeanDefinition(
                    executorProxyName,
                    BeanUtils.buildBeanDefinitionForFactoryMethod(
                            proxyType,
                            sqlExecutorName,
                            "getProxy",
                            new Object[] { proxyType }));
            log.trace("Register bean ExecutorProxy: {}", executorProxyName);
        }
        log.trace("Finished registering executor proxy definition of database: '{}'.", datasourceBeanName);
    }

    // public <T> T executorProxy() {

    // }

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

}
