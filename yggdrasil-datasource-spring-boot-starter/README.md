# datasource-spring-boot-starter

多种数据源以及orm框架自动初始化组件

## 配置

### 多数据源配置

``` yaml
yggdrasil:
  datasource:
    enable: true # 组件开关
    dbs:
      your_first_db:
        url: ${your-first-db-url}
        username: $${your-first-db-username}
        password: ${your-first-db-password}
        cryptographic: true # 密码是否是加密字符串
        switch-conf: # 自动切换配置
            db: # 另一个db配置，配置项跟当前这个一样
            time: # 切换时间
        # 连接池配置
        maxIdle: 5
        minIdle: 2
        initialSize: 2
        driverClassName: org.postgresql.Driver
        jmxEnabled: false
        testWhileIdle: false
        validationQuery: SELECT 1
        testOnBorrow: true
        testOnReturn: false
        validationInterval: 30000
        timeBetweenEvictionRunsMillis: 10000
        maxWait: 60000
        removeAbandoned: true
        removeAbandonedTimeout: 60000
        minEvictableIdleTimeMillis: 60000
        logAbandoned: true
      your_second_db:
        url: ${your-second-db-url}
        username: $${your-second-db-username}
        password: ${your-second-db-password}
        cryptographic: true # 密码是否是加密字符串
        switch-conf: # 自动切换配置
            db: # 另一个db配置，配置项跟当前这个一样
            time: # 切换时间
        # 连接池配置
        maxIdle: 5
        minIdle: 2
        initialSize: 2
        driverClassName: org.postgresql.Driver
        jmxEnabled: false
        testWhileIdle: false
        validationQuery: SELECT 1
        testOnBorrow: true
        testOnReturn: false
        validationInterval: 30000
        timeBetweenEvictionRunsMillis: 10000
        maxWait: 60000
        removeAbandoned: true
        removeAbandonedTimeout: 60000
        minEvictableIdleTimeMillis: 60000
        logAbandoned: true
      # some other datasource configuration
      ...
```

### sharding-jdbc配置

``` yaml
yggdrasil:
  datasource:
    enable: true # 组件开关
    sharding: 
      your_first_sharding_datasource_name:
        rule: # sharding rule, see io.shardingsphere.core.yaml.sharding.YamlShardingRuleConfiguration
          ...
        config-map: # sharding config map, see https://github.com/apache/incubator-shardingsphere
        props: # sharding props, see https://github.com/apache/incubator-shardingsphere
        dbs:
          db_shard_0: # shard0 datasource config
            ...
          db_shard_1: # shard1 datasource config
            ...
        ...
```

### mybatis配置

``` yaml
yggdrasil:
  datasource:
    enable: true # 组件开关
    mybatis:
      check-config-location: true # 检查配置文件是否存在，true：当配置文件不存在的时候抛出错误，false：配置文件不存在时使用默认配置
      config: file:config/mybatis/mybatis-config.xml # 配置文件路径
      mapper-locations: file:config/mybatis/mapper/**/*.xml # mapper文件路径
      type-aliases-package: your.model.package # 类型定义包名
      mapper-packages:
        ${your-db-name}: your.mapper.package # mapper接口所在包，key为任意已配置数据源的名称
```

### 数据源实例选择配置

``` yaml
yggdrasil:
  datasource:
    enable: true # 组件开关
    required-instances: # 该配置用于限制只初始化指定的数据源，如未配置，则初始化所有已配置的数据源
    - ${your-db-name}
```

### MetaManager配置

#### 全局配置

``` yaml
yggdrasil: 
  datasource: 
    enable: true
    meta: 
      tableDefinePackages: 
      - org.s3s3l.yggdrasil.sample.dss.condition
      - org.s3s3l.yggdrasil.sample.dss.dao
      proxy-define-packages:
      - org.s3s3l.yggdrasil.sample.dss.proxy
      proxy-config-locations:
      - proxy
      validatorFactory: org.s3s3l.yggdrasil.orm.validator.DefaultValidatorFactory
      typeHandlerManager: org.s3s3l.yggdrasil.orm.handler.TypeHandlerManager
      scanner: org.s3s3l.yggdrasil.utils.reflect.scan.ClassScanner
```

#### 针对个别数据源配置

``` yaml
yggdrasil: 
  datasource: 
    enable: true
    dbs:
      test:
        meta: 
          proxy-define-packages:
          - org.s3s3l.yggdrasil.sample.dss.proxy
          proxy-config-locations:
          - proxy
```

> 针对数据源配置优先级较高，针对配置中未配置的配置项会使用全局配置

## 使用

### Spring Bean

- javax.sql.DataSource
  - 普通数据源以${your-db-name} + Datasource作为BeanName
  - Sharding数据源以${your-db-name} + ShardingDatasource作为BeanName
  - 定时切换数据源以${your-db-name} + SwitchableDatasource作为BeanName
- org.springframework.jdbc.datasource.DataSourceTransactionManager
  - 以${your-db-name} + TransactionManager作为BeanName
- org.mybatis.spring.SqlSessionFactoryBean
  - 以${your-db-name} + SqlSessionFactory作为BeanName
- org.mybatis.spring.SqlSessionTemplate
  - 以${your-db-name} + SessionTemplate作为BeanName
- Mapper
  - 可直接以类型注入
- org.s3s3l.yggdrasil.orm.exec.SqlExecutor
  - 以${your-db-name} + Executor作为BeanName
- ExecutorProxy
  - 可直接以类型注入

### 示例

> application.yaml

``` yaml
yggdrasil: 
  datasource: 
    enable: true
    meta: 
      tableDefinePackages: 
      - org.s3s3l.yggdrasil.sample.dss.condition
      - org.s3s3l.yggdrasil.sample.dss.dao
    requiredInstances: 
    - test
    mybatis:
      check-config-location: true
      config: config/mybatis-config.xml
      mapper-locations: config/mapper/**/*.xml
      type-aliases-package: org.s3s3l.yggdrasil.sample.dss.dao
      mapper-packages:
        test: org.s3s3l.yggdrasil.sample.dss.mapper
    dbs:
      test:
        autoCreate: 
          enable: true
        driverClassName: org.hsqldb.jdbc.JDBCDriver
        url: jdbc:hsqldb:file:./db/test
        username: test
        password: test
        jmxEnabled: false
        testWhileIdle: false
        validationQuery: SELECT 1 FROM INFORMATION_SCHEMA.SYSTEM_USERS
        testOnBorrow: true
        testOnReturn: false
        validationInterval: 30000
        timeBetweenEvictionRunsMillis: 10000
        maxActive: 10
        maxIdle: 2
        minIdle: 1
        initialSize: 2
        maxWait: 60000
        removeAbandoned: true
        logAbandoned: true
        minEvictableIdleTimeMillis: 60000
        removeAbandonedTimeout: 60000
        meta: 
          proxy-define-packages:
          - org.s3s3l.yggdrasil.sample.dss.proxy
          proxy-config-locations:
          - proxy
```

> org.s3s3l.yggdrasil.sample.dss.dao.User

``` java
package org.s3s3l.yggdrasil.sample.dss.dao;

import org.s3s3l.yggdrasil.orm.bind.annotation.Column;
import org.s3s3l.yggdrasil.orm.bind.annotation.DatabaseType;
import org.s3s3l.yggdrasil.orm.bind.annotation.TableDefine;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@TableDefine(table = "t_user")
public class User {
    @Column(dbType = @DatabaseType(type = "varchar", args = { "64" }, primary = true))
    private String id;
    @Column(dbType = @DatabaseType(type = "varchar", args = { "32" }))
    private String realName;
    @Column(dbType = @DatabaseType(type = "varchar", args = { "32" }))
    private String phone;
    @Column(dbType = @DatabaseType(type = "varchar", args = { "32" }, notNull = true))
    private String username;
    @Column(dbType = @DatabaseType(type = "varchar", args = { "64" }, notNull = true))
    private String password;
    @Column(dbType = @DatabaseType(type = "int"))
    private int age;
}
```

> org.s3s3l.yggdrasil.sample.dss.condition.UserCondition

``` java
package org.s3s3l.yggdrasil.sample.dss.condition;

import org.s3s3l.yggdrasil.orm.bind.annotation.Condition;
import org.s3s3l.yggdrasil.orm.bind.annotation.SqlModel;
import org.s3s3l.yggdrasil.orm.pagin.ConditionForPagination;
import org.s3s3l.yggdrasil.sample.dss.dao.User;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@SqlModel(table = User.class)
public class UserCondition extends ConditionForPagination {
    @Condition(forUpdate = true, forDelete = true)
    private String id;
    private String[] ids;
}
```

> org.s3s3l.yggdrasil.sample.dss.proxy.UserProxy

``` java
package org.s3s3l.yggdrasil.sample.dss.proxy;

import java.util.List;

import org.s3s3l.yggdrasil.orm.bind.annotation.ExecutorProxy;
import org.s3s3l.yggdrasil.orm.bind.annotation.Param;
import org.s3s3l.yggdrasil.sample.dss.condition.UserCondition;
import org.s3s3l.yggdrasil.sample.dss.dao.User;

@ExecutorProxy
public interface UserProxy {

    long userCount(UserCondition condition);

    List<User> list(@Param("condition") UserCondition condition);

    User get(@Param("condition") UserCondition condition);
}
```

> proxy/UserProxy.yml

``` yaml
iface: org.s3s3l.yggdrasil.sample.dss.proxy.UserProxy
methods: 
  - method: userCount
    sql: > 
      select 
        count(*)
      from t_user
        where id in (${arg0.ids?join(", ")})
  - method: list
    sql: >
      select 
        username,
        password
      from t_user
        where id in (${condition.ids?join(", ")})
  - method: get
    sql: >
      select 
        username,
        password
      from t_user
        where id = #condition.id#
```

> org.s3s3l.yggdrasil.sample.dss.mapper.UserMapper

``` java
package org.s3s3l.yggdrasil.sample.dss.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.s3s3l.yggdrasil.sample.dss.condition.UserCondition;
import org.s3s3l.yggdrasil.sample.dss.dao.User;

public interface UserMapper {
    long userCount(@Param("condition") UserCondition condition);

    List<User> list(@Param("condition") UserCondition condition);

    User get(@Param("condition") UserCondition condition);
}
```

> config/mapper/UserMapper.xml

``` xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="org.s3s3l.yggdrasil.sample.dss.mapper.UserMapper">
    <select id="userCount" resultType="java.lang.Long">
      select 
        count(*)
      from t_user
        where id in 
        <foreach collection="condition.ids" item="id" open="(" close=")" separator=",">
            #{id}
        </foreach>
    </select>

    <select id="list" resultType="org.s3s3l.yggdrasil.sample.dss.dao.User">
        
      select 
        username,
        password
      from t_user
        where id in 
        <foreach collection="condition.ids" item="id" open="(" close=")" separator=",">
            #{id}
        </foreach>
    </select>

    <select id="get" resultType="org.s3s3l.yggdrasil.sample.dss.dao.User">
      select 
        username,
        password
      from t_user
        where id = #{condition.id}
    </select>
</mapper>
```

> config/mybatis-config.xml

``` xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE configuration PUBLIC "-//mybatis.org//DTD Config 3.0//EN" "http://mybatis.org/dtd/mybatis-3-config.dtd">
<configuration>
    <settings>
        <setting name="cacheEnabled" value="false" />
        <setting name="mapUnderscoreToCamelCase" value="true" />
    </settings>

    <typeHandlers>
        <typeHandler handler="org.apache.ibatis.type.InstantTypeHandler" />
        <typeHandler handler="org.apache.ibatis.type.LocalDateTimeTypeHandler" />
        <typeHandler handler="org.apache.ibatis.type.LocalDateTypeHandler" />
        <typeHandler handler="org.apache.ibatis.type.LocalTimeTypeHandler" />
        <typeHandler handler="org.apache.ibatis.type.OffsetDateTimeTypeHandler" />
        <typeHandler handler="org.apache.ibatis.type.OffsetTimeTypeHandler" />
        <typeHandler handler="org.apache.ibatis.type.ZonedDateTimeTypeHandler" />
    </typeHandlers>
</configuration>
```

> org.s3s3l.yggdrasil.sample.dss.Application

``` java
package org.s3s3l.yggdrasil.sample.dss;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import org.s3s3l.yggdrasil.orm.exec.CreateConfig;
import org.s3s3l.yggdrasil.orm.exec.SqlExecutor;
import org.s3s3l.yggdrasil.orm.pagin.PaginResult;
import org.s3s3l.yggdrasil.sample.dss.condition.UserCondition;
import org.s3s3l.yggdrasil.sample.dss.dao.User;
import org.s3s3l.yggdrasil.sample.dss.mapper.UserMapper;
import org.s3s3l.yggdrasil.sample.dss.proxy.UserProxy;
import org.s3s3l.yggdrasil.utils.common.StringUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootApplication
public class Application {
    public static void main(String[] args) throws SQLException {
        ConfigurableApplicationContext ctx = SpringApplication.run(Application.class, args);

        SqlExecutor sqlExecutor = ctx.getBean(SqlExecutor.class);
        sqlExecutor.create(User.class, CreateConfig.builder()
                .dropFirst(true)
                .force(false)
                .build());
        String id = StringUtils.getUUIDNoLine();
        String id2 = StringUtils.getUUIDNoLine();
        sqlExecutor.insert(Arrays.asList(
                User.builder()
                        .id(id)
                        .username("username1")
                        .password("pwd1")
                        .realName("realName1")
                        .age(18)
                        .build(),
                User.builder()
                        .id(id2)
                        .username("username2")
                        .password("pwd2")
                        .realName("realName2")
                        .age(22)
                        .build()));
        log.info(">>>>>>>>>>>>>>>>>> select one");
        System.out.println(sqlExecutor.selectOne(UserCondition.builder().id(id).build(), User.class));
        log.info(">>>>>>>>>>>>>>>>>> select all");
        sqlExecutor.select(UserCondition.builder().build(), User.class).forEach(System.out::println);

        // update age
        sqlExecutor.update(User.builder().age(19).build(), UserCondition.builder().id(id).build());
        log.info(">>>>>>>>>>>>>>>>>> select one after update");
        sqlExecutor.select(UserCondition.builder().id(id).build(), User.class).forEach(System.out::println);
        log.info(">>>>>>>>>>>>>>>>>> select all after update");
        sqlExecutor.select(UserCondition.builder().build(), User.class).forEach(System.out::println);

        // delete by id
        sqlExecutor.delete(UserCondition.builder().id(id).build());
        log.info(">>>>>>>>>>>>>>>>>> select one after delete");
        sqlExecutor.select(UserCondition.builder().id(id).build(), User.class).forEach(System.out::println);
        log.info(">>>>>>>>>>>>>>>>>> select all after delete");
        sqlExecutor.select(UserCondition.builder().build(), User.class).forEach(System.out::println);

        UserProxy userProxy = ctx.getBean(UserProxy.class);

        log.info(">>>>>>>>>>>>>>>>>> proxy, count for in condition");
        System.out.println(userProxy.userCount(UserCondition.builder().ids(new String[] { id, id2 }).build()));

        log.info(">>>>>>>>>>>>>>>>>> proxy, list for in condition");
        userProxy.list(UserCondition.builder().ids(new String[] { id, id2 }).build()).forEach(System.out::println);

        log.info(">>>>>>>>>>>>>>>>>> proxy, get one by id");
        System.out.println(userProxy.get(UserCondition.builder().id(id2).build()));

        log.info(">>>>>>>>>>>>>>>>>> transactional, commit");
        sqlExecutor.transactional();
        String id3 = StringUtils.getUUIDNoLine();
        sqlExecutor.insert(Arrays.asList(
                User.builder()
                        .id(id3)
                        .username("username3")
                        .password("pwd3")
                        .realName("realName3")
                        .age(18)
                        .build()));
        sqlExecutor.transactionalCommit();
        System.out.println(userProxy.get(UserCondition.builder().id(id3).build()));

        log.info(">>>>>>>>>>>>>>>>>> transactional, rollback");
        sqlExecutor.transactional();
        String id4 = StringUtils.getUUIDNoLine();
        sqlExecutor.insert(Arrays.asList(
                User.builder()
                        .id(id4)
                        .username("username4")
                        .password("pwd4")
                        .realName("realName4")
                        .age(18)
                        .build()));
        sqlExecutor.rollback();
        System.out.println(userProxy.get(UserCondition.builder().id(id4).build()));

        log.info(">>>>>>>>>>>>>>>>>> Pagin");
        for (int i = 10; i < 100; i++) {
            sqlExecutor.insert(Arrays.asList(
                    User.builder()
                            .id(StringUtils.getUUIDNoLine())
                            .username("username" + i)
                            .password("pwd" + i)
                            .realName("realName" + i)
                            .age(i)
                            .build()));
        }
        UserCondition paginCondition = new UserCondition();
        paginCondition.setPageIndex(1);
        paginCondition.setPageSize(10);
        boolean nextPage = true;

        while (nextPage) {
            PaginResult<List<User>> pr = sqlExecutor.selectByPagin(paginCondition, User.class);
            log.info("pagecount: {}, recordscount: {}, resultRecordsCount: {}", pr.getPageCount(),
                    pr.getRecordsCount(), pr.getData().size());
            nextPage = pr.getPageCount() > paginCondition.getPageIndex();
            paginCondition.setPageIndex(paginCondition.getPageIndex() + 1);
        }

        log.info(">>>>>>>>>>>>>>>>>> mybatis");

        UserMapper userMapper = ctx.getBean(UserMapper.class);
        log.info(">>>>>>>>>>>>>>>>>> mapper, count for in condition");
        System.out.println(userMapper.userCount(UserCondition.builder().ids(new String[] { id, id2 }).build()));

        log.info(">>>>>>>>>>>>>>>>>> mapper, list for in condition");
        userMapper.list(UserCondition.builder().ids(new String[] { id, id2 }).build()).forEach(System.out::println);

        log.info(">>>>>>>>>>>>>>>>>> mapper, get one by id");
        System.out.println(userMapper.get(UserCondition.builder().id(id2).build()));
    }
}
```