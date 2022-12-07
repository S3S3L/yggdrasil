package io.github.s3s3l.yggdrasil.sample.orm.def;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import org.apache.tomcat.jdbc.pool.DataSource;
import org.apache.tomcat.jdbc.pool.PoolProperties;
import io.github.s3s3l.yggdrasil.orm.bind.express.common.DefaultDataBindExpress;
import io.github.s3s3l.yggdrasil.orm.ds.DefaultDatasourceHolder;
import io.github.s3s3l.yggdrasil.orm.exec.CreateConfig;
import io.github.s3s3l.yggdrasil.orm.exec.DefaultSqlExecutor;
import io.github.s3s3l.yggdrasil.orm.exec.SqlExecutor;
import io.github.s3s3l.yggdrasil.orm.meta.MetaManager;
import io.github.s3s3l.yggdrasil.orm.meta.MetaManagerConfig;
import io.github.s3s3l.yggdrasil.orm.pagin.PaginResult;
import io.github.s3s3l.yggdrasil.orm.pool.ConnManager;
import io.github.s3s3l.yggdrasil.orm.wrapper.SqlObjectWrapper;
import io.github.s3s3l.yggdrasil.sample.orm.condition.UserCondition;
import io.github.s3s3l.yggdrasil.sample.orm.dao.User;
import io.github.s3s3l.yggdrasil.sample.orm.proxy.UserProxy;
import io.github.s3s3l.yggdrasil.utils.common.FreeMarkerHelper;
import io.github.s3s3l.yggdrasil.utils.common.StringUtils;
import io.github.s3s3l.yggdrasil.utils.file.FileUtils;
import io.github.s3s3l.yggdrasil.utils.stuctural.jackson.JacksonUtils;

import freemarker.template.Configuration;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Application {
    public static void main(String[] args) throws SQLException {
        DataSource datasource = new DataSource(
                JacksonUtils.YAML.toObject(FileUtils.getFirstExistResource("datasource.yaml"), PoolProperties.class));
        MetaManager metaManager = new MetaManager(MetaManagerConfig.defaultBuilder()
                .tableDefinePackages(new String[] { "io.github.s3s3l.yggdrasil.sample.orm" })
                .proxyDefinePackages(new String[] { "io.github.s3s3l.yggdrasil.sample.orm.proxy" })
                .proxyConfigLocations(new String[] { "proxy" })
                .build());
        SqlExecutor sqlExecutor = DefaultSqlExecutor.builder()
                .datasourceHolder(new DefaultDatasourceHolder(ConnManager.DEFAULT, datasource))
                .dataBindExpress(new DefaultDataBindExpress(metaManager))
                .metaManager(metaManager)
                .freeMarkerHelper(new FreeMarkerHelper().config(config -> config
                        .setObjectWrapper(new SqlObjectWrapper(Configuration.DEFAULT_INCOMPATIBLE_IMPROVEMENTS))))
                .build();
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

        UserProxy userProxy = sqlExecutor.getProxy(UserProxy.class);

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

        log.info(">>>>>>>>>>>>>>>>>> transactional, proxy, commit");
        sqlExecutor.transactional();
        String id5 = StringUtils.getUUIDNoLine();
        userProxy.addOne(User.builder()
                .id(id5)
                .username("username5")
                .password("pwd5")
                .realName("realName5")
                .age(18)
                .build());
        sqlExecutor.transactionalCommit();
        System.out.println(userProxy.get(UserCondition.builder()
                .id(id5)
                .build()));

        log.info(">>>>>>>>>>>>>>>>>> transactional, proxy, rollback");
        sqlExecutor.transactional();
        String id6 = StringUtils.getUUIDNoLine();
        userProxy.addOne(User.builder()
                .id(id6)
                .username("username6")
                .password("pwd6")
                .realName("realName6")
                .age(18)
                .build());
        sqlExecutor.rollback();
        System.out.println(userProxy.get(UserCondition.builder()
                .id(id6)
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

    }
}
