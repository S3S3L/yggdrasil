
package org.s3s3l.yggdrasil.starter.datasource;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.s3s3l.yggdrasil.annotation.apollo.ApolloConfiguration;
import org.s3s3l.yggdrasil.bean.time.JsonTimestampDateTimeDeserializer;
import org.s3s3l.yggdrasil.bean.time.JsonTimestampDateTimeSerializer;
import org.s3s3l.yggdrasil.configuration.mybatis.MybatisConfiguration;
import org.s3s3l.yggdrasil.orm.meta.MetaManagerConfig;
import org.s3s3l.yggdrasil.starter.datasource.config.SwitchableDatasourceConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import io.shardingsphere.core.yaml.sharding.YamlShardingRuleConfiguration;
import lombok.Data;

/**
 * <p>
 * </p>
 * ClassName:DefaultRedisClientConfiguration <br>
 * Date: Sep 10, 2018 8:53:45 PM <br>
 * 
 * @author kehw_zwei
 * @version 1.0.0
 * @since JDK 1.8
 */
@Data
@ApolloConfiguration
@ConfigurationProperties(prefix = MultiDatasourceConfiguration.PREFIX)
public class MultiDatasourceConfiguration {

    public static final String PREFIX = "yggdrasil.datasource";

    private Map<String, SwitchableDatasourceConfiguration> dbs = new HashMap<>();
    private Map<String, ShardingDatasourceConfiguration> sharding = new HashMap<>();
    private Map<String, AutoSwitchDatasourceConfiguration> autoSwitchDbs = new HashMap<>();
    private MybatisConfiguration mybatis;
    private List<String> requiredInstances;
    private boolean enable;
    private MetaManagerConfig meta = MetaManagerConfig.defaultBuilder().build();

    @Data
    public static class ShardingDatasourceConfiguration {
        private YamlShardingRuleConfiguration rule;
        private Map<String, Object> configMap = new LinkedHashMap<>();
        private Properties props = new Properties();
        private Map<String, SwitchableDatasourceConfiguration> dbs;
    }

    @Data
    public static class AutoSwitchDatasourceConfiguration {
        private DatasourceName current;
        private DatasourceName next;
        @JsonDeserialize(using = JsonTimestampDateTimeDeserializer.class)
        @JsonSerialize(using = JsonTimestampDateTimeSerializer.class)
        private Timestamp switchTime;
    }

    @Data
    public static class DatasourceName {
        private String name;
        private DataSourceType type;
    }

    public enum DataSourceType {
        COMMON, SHARDING
    }

}
