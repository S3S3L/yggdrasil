
package io.github.s3s3l.yggdrasil.starter.redis;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.springframework.boot.context.properties.ConfigurationProperties;

import io.github.s3s3l.yggdrasil.annotation.apollo.ApolloConfiguration;
import io.github.s3s3l.yggdrasil.bean.redis.HAPClusterNode;
import io.github.s3s3l.yggdrasil.configuration.redis.RedisClusterConfiguration;
import lombok.Data;
import redis.clients.jedis.Jedis;

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
@ConfigurationProperties(prefix = DefaultRedisClientConfiguration.PREFIX)
public class DefaultRedisClientConfiguration {
    public static final String PREFIX = "yggdrasil.redis";

    private boolean enable;
    private String primary;
    private Set<String> requiredInstances;
    private Map<String, RedisClusterConfiguration> masterSlave = new HashMap<>();
    private Map<String, HAPClusterNode<Jedis>> cluster = new HashMap<>();
}
