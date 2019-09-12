
package org.s3s3l.yggdrasil.starter.redis.feature;

import org.s3s3l.yggdrasil.starter.redis.DefaultRedisClientConfiguration;
import org.s3s3l.yggdrasil.utils.spring.env.ConfigurationResolver;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.core.env.Environment;
import org.yaml.snakeyaml.Yaml;

/**
 * <p>
 * </p>
 * ClassName:RedisConfigurationResolver <br>
 * Date: Jun 3, 2019 2:53:16 PM <br>
 * 
 * @author kehw_zwei
 * @version 1.0.0
 * @since JDK 1.8
 */
public class RedisConfigurationResolver implements ConfigurationResolver<DefaultRedisClientConfiguration> {
    private static final Yaml snakeYaml = new Yaml();

    @Override
    public DefaultRedisClientConfiguration resolve(Environment environment) {
        return Binder.get(environment)
                .bind(DefaultRedisClientConfiguration.PREFIX, DefaultRedisClientConfiguration.class)
                .orElse(new DefaultRedisClientConfiguration());
    }

    @Override
    public DefaultRedisClientConfiguration resolve(String configuration) {
        return snakeYaml.loadAs(configuration, DefaultRedisClientConfiguration.class);
    }

}
