package io.github.s3s3l.yggdrasil.configuration.redis;

import org.springframework.boot.autoconfigure.data.redis.RedisProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * <p>
 * </p>
 * ClassName:RedisClusterConfiguration <br>
 * Date: Mar 29, 2018 4:08:50 PM <br>
 * 
 * @author kehw_zwei
 * @version 1.0.0
 * @since JDK 1.8
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class RedisClusterConfiguration {

    private RedisProperties master;
    private RedisProperties slave;
}
