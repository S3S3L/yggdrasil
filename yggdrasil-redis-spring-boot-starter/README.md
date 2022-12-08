# yggdrasil-redis-spring-boot-starter

> 基于Jedis组件实现在Spring Boot项目中Redis的自动配置和加载

## 功能

- 多Redis实例
- 读写分离
- Cluster支持

## 使用

### Maven依赖

``` xml
<dependency>
    <groupId>io.github.s3s3l</groupId>
    <artifactId>yggdrasil-redis-spring-boot-starter</artifactId>
    <version>3.6.1-RELEASE</version>
</dependency>
```

### master-slave读写分离配置

``` yaml
yggdrasil:
  redis:
    enable: true # 组件开关
    primary: ${your-primary-IRedis-bean} # 定义主要的IRedis Bean
    requiredInstances: # 指定需要初始化的实例，如果未指定，所有配置的实例都会初始化
    - ${your-first-redis}
    - ${your-second-redis}
    masterSlave:
      your-first-redis:
        master:
          host: ${your-first-redis-master-host}
          port: ${your-first-redis-master-port}
          passowrd: ${your-first-redis-master-password}
          database: ${database-number}
          timeout: ${timeout}
          jedis:
            pool: # some pool config
              maxActive: ...
              maxIdle: ...
              maxWait: ...
              minIdle: ...
              ...
        slave: 
          host: ${your-first-redis-slave-host}
          port: ${your-first-redis-slave-port}
          passowrd: ${your-first-redis-slave-password}
          database: ${database-number}
          timeout: ${timeout}
          jedis:
            pool: # some pool config
              maxActive: ...
              maxIdle: ...
              maxWait: ...
              minIdle: ...
              ...
      your-second-redis:
        master:
          host: ${your-second-redis-master-host}
          port: ${your-second-redis-master-port}
          passowrd: ${your-second-redis-master-password}
          database: ${database-number}
          timeout: ${timeout}
          jedis:
            pool: # some pool config
              maxActive: ...
              maxIdle: ...
              maxWait: ...
              minIdle: ...
              ...
        slave: 
          host: ${your-second-redis-slave-host}
          port: ${your-second-redis-slave-port}
          passowrd: ${your-second-redis-slave-password}
          database: ${database-number}
          timeout: ${timeout}
          jedis:
            pool: # some pool config
              maxActive: ...
              maxIdle: ...
              maxWait: ...
              minIdle: ...
              ...
      ... # more redis instance config
```

### Cluster配置

``` yaml
yggdrasil:
  redis:
    enable: true # 组件开关
    primary: ${your-primary-IRedis-bean} # 定义主要的IRedis Bean
    requiredInstances: # 指定需要初始化的实例，如果未指定，所有配置的实例都会初始化
    - ${your-first-cluster}
    - ${your-second-cluster}
    cluster: 
      your-first-cluster:
        cluster-config:
        - host: ${first-node-host}
          port: ${first-node-port}
        - host: ${second-node-host}
          port: ${second-node-port}
        ... # more node config
        poolConfig: # some pool config
          maxTotal: ...
          maxIdle: ...
          maxWaitMillis: ...
          minIdle: ...
          testOnBorrow: ...
      your-second-cluster:
        cluster-config:
        - host: ${first-node-host}
          port: ${first-node-port}
        - host: ${second-node-host}
          port: ${second-node-port}
        ... # more node config
        poolConfig: # some pool config
          maxTotal: ...
          maxIdle: ...
          maxWaitMillis: ...
          minIdle: ...
          testOnBorrow: ...
```

### 在服务中使用

#### beans
- io.github.s3s3l.yggdrasil.redis.JedisClusterHelper
  - 以${your-redis-name} + Redis作为BeanName
- io.github.s3s3l.yggdrasil.redis.RedisClusterHelper
  - 以${your-redis-name} + RedisCluster作为BeanName

#### 注入

``` java
@Service
public class YourService {
  @Resource(name="${beanName}")
  private IRedis redis;
}
```