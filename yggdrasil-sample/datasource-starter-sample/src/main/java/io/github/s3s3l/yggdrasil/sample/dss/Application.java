package io.github.s3s3l.yggdrasil.sample.dss;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import io.github.s3s3l.yggdrasil.orm.exec.CreateConfig;
import io.github.s3s3l.yggdrasil.orm.exec.SqlExecutor;
import io.github.s3s3l.yggdrasil.orm.pagin.PaginResult;
import io.github.s3s3l.yggdrasil.sample.dss.condition.UserCondition;
import io.github.s3s3l.yggdrasil.sample.dss.dao.User;
import io.github.s3s3l.yggdrasil.sample.dss.mapper.UserMapper;
import io.github.s3s3l.yggdrasil.sample.dss.proxy.UserProxy;
import io.github.s3s3l.yggdrasil.utils.common.StringUtils;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootApplication(scanBasePackages = { "io.github.s3s3l.yggdrasil.sample.dss" })
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
        sqlExecutor.insert(Arrays.asList(User.builder()
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
        System.out.println(sqlExecutor.selectOne(UserCondition.builder()
                .id(id)
                .build(), User.class));
        log.info(">>>>>>>>>>>>>>>>>> select all");
        sqlExecutor.select(UserCondition.builder()
                .build(), User.class)
                .forEach(System.out::println);

        // update age
        sqlExecutor.update(User.builder()
                .age(19)
                .build(),
                UserCondition.builder()
                        .id(id)
                        .build());
        log.info(">>>>>>>>>>>>>>>>>> select one after update");
        sqlExecutor.select(UserCondition.builder()
                .id(id)
                .build(), User.class)
                .forEach(System.out::println);
        log.info(">>>>>>>>>>>>>>>>>> select all after update");
        sqlExecutor.select(UserCondition.builder()
                .build(), User.class)
                .forEach(System.out::println);

        // delete by id
        sqlExecutor.delete(UserCondition.builder()
                .id(id)
                .build());
        log.info(">>>>>>>>>>>>>>>>>> select one after delete");
        sqlExecutor.select(UserCondition.builder()
                .id(id)
                .build(), User.class)
                .forEach(System.out::println);
        log.info(">>>>>>>>>>>>>>>>>> select all after delete");
        sqlExecutor.select(UserCondition.builder()
                .build(), User.class)
                .forEach(System.out::println);

        UserProxy userProxy = ctx.getBean(UserProxy.class);

        log.info(">>>>>>>>>>>>>>>>>> proxy, count for in condition");
        System.out.println(userProxy.userCount(UserCondition.builder()
                .ids(new String[] { id, id2 })
                .build()));

        log.info(">>>>>>>>>>>>>>>>>> proxy, list for in condition");
        userProxy.list(UserCondition.builder()
                .ids(new String[] { id, id2 })
                .build())
                .forEach(System.out::println);

        log.info(">>>>>>>>>>>>>>>>>> proxy, get one by id");
        System.out.println(userProxy.get(UserCondition.builder()
                .id(id2)
                .build()));

        log.info(">>>>>>>>>>>>>>>>>> transactional, commit");
        sqlExecutor.transactional();
        String id3 = StringUtils.getUUIDNoLine();
        sqlExecutor.insert(Arrays.asList(User.builder()
                .id(id3)
                .username("username3")
                .password("pwd3")
                .realName("realName3")
                .age(18)
                .build()));
        sqlExecutor.transactionalCommit();
        System.out.println(userProxy.get(UserCondition.builder()
                .id(id3)
                .build()));

        log.info(">>>>>>>>>>>>>>>>>> transactional, rollback");
        sqlExecutor.transactional();
        String id4 = StringUtils.getUUIDNoLine();
        sqlExecutor.insert(Arrays.asList(User.builder()
                .id(id4)
                .username("username4")
                .password("pwd4")
                .realName("realName4")
                .age(18)
                .build()));
        sqlExecutor.rollback();
        System.out.println(userProxy.get(UserCondition.builder()
                .id(id4)
                .build()));

        log.info(">>>>>>>>>>>>>>>>>> Pagin");
        for (int i = 10; i < 100; i++) {
            sqlExecutor.insert(Arrays.asList(User.builder()
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
            log.info("pagecount: {}, recordscount: {}, resultRecordsCount: {}", pr.getPageCount(), pr.getRecordsCount(),
                    pr.getData()
                            .size());
            nextPage = pr.getPageCount() > paginCondition.getPageIndex();
            paginCondition.setPageIndex(paginCondition.getPageIndex() + 1);
        }

        log.info(">>>>>>>>>>>>>>>>>> mybatis");

        UserMapper userMapper = ctx.getBean(UserMapper.class);
        log.info(">>>>>>>>>>>>>>>>>> mapper, count for in condition");
        System.out.println(userMapper.userCount(UserCondition.builder()
                .ids(new String[] { id, id2 })
                .build()));

        log.info(">>>>>>>>>>>>>>>>>> mapper, list for in condition");
        userMapper.list(UserCondition.builder()
                .ids(new String[] { id, id2 })
                .build())
                .forEach(System.out::println);

        log.info(">>>>>>>>>>>>>>>>>> mapper, get one by id");
        System.out.println(userMapper.get(UserCondition.builder()
                .id(id2)
                .build()));
    }
}
