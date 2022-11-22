package org.s3s3l.yggdrasil.sample.dss;

import java.util.Arrays;

import org.s3s3l.yggdrasil.orm.exec.SqlExecutor;
import org.s3s3l.yggdrasil.sample.dss.condition.UserCondition;
import org.s3s3l.yggdrasil.sample.dss.dao.User;
import org.s3s3l.yggdrasil.utils.common.StringUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        ConfigurableApplicationContext ctx = SpringApplication.run(Application.class, args);
        // MetaManager metaManager = ctx.getBean(MetaManager.class);
        SqlExecutor sqlExecutor = ctx.getBean(SqlExecutor.class);
        // sqlExecutor.create(User.class, false);
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
        log.info(">>>>>>>>>>>>>>>>>>>>  select all");
        sqlExecutor.select(UserCondition.builder().build(), User.class).forEach(System.out::println);
        log.info(">>>>>>>>>>>>>>>>>>>>  select one");
        sqlExecutor.select(UserCondition.builder().id(id).build(), User.class).forEach(System.out::println);
    }
}
