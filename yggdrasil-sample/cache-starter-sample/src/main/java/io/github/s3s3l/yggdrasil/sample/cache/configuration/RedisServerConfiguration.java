package io.github.s3s3l.yggdrasil.sample.cache.configuration;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.context.config.ConfigDataEnvironmentPostProcessor;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.Ordered;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.PropertySource;
import org.testcontainers.utility.DockerImageName;

import com.redis.testcontainers.RedisContainer;

/**
 * 为示例代码启动一个redis实例，并非正常使用时的标准代码
 */
public class RedisServerConfiguration implements EnvironmentPostProcessor, Ordered {
    private final RedisContainer redis = new RedisContainer(DockerImageName.parse("redis:6.2.6"));

    public RedisServerConfiguration() {
        redis.start();
    }

    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        environment.getPropertySources()
                .addFirst(new PropertySource<String>("redis-config") {

                    @Override
                    public Object getProperty(String name) {
                        switch (name.toLowerCase()) {
                            case "yggdrasil.redis.masterslave.test.master.port":
                                return redis.getMappedPort(RedisContainer.REDIS_PORT);
                            default:
                                return null;
                        }
                    }

                });
    }

    @Override
    public int getOrder() {
        return ConfigDataEnvironmentPostProcessor.ORDER + 1;
    }

}
