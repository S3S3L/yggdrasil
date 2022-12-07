package io.github.s3s3l.yggdrasil.starter.datasource.feature;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.sql.DataSource;

import io.github.s3s3l.yggdrasil.starter.datasource.MultiDatasourceConfiguration.ShardingDatasourceConfiguration;
import io.github.s3s3l.yggdrasil.starter.datasource.config.DatasourceConfiguration;
import io.github.s3s3l.yggdrasil.starter.datasource.config.SwitchableDatasourceConfiguration;
import io.github.s3s3l.yggdrasil.utils.stuctural.jackson.JacksonUtils;

import io.shardingsphere.api.config.rule.ShardingRuleConfiguration;
import io.shardingsphere.core.yaml.sharding.YamlShardingRuleConfiguration;
import io.shardingsphere.shardingjdbc.api.ShardingDataSourceFactory;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DatasourceFactory {

    public static final String BEAN_NAME = "datasourceFactory";
    
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
