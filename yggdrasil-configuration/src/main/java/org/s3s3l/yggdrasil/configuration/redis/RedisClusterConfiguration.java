package org.s3s3l.yggdrasil.configuration.redis;

import org.springframework.boot.autoconfigure.data.redis.RedisProperties;

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
public class RedisClusterConfiguration {

    private RedisProperties master;
    private RedisProperties slave;

    public RedisProperties getMaster() {
        return master;
    }

    public void setMaster(RedisProperties master) {
        this.master = master;
    }

    public RedisProperties getSlave() {
        return slave;
    }

    public void setSlave(RedisProperties slave) {
        this.slave = slave;
    }
}
