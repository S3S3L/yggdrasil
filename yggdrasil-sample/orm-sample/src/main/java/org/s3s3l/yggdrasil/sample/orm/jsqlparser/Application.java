package org.s3s3l.yggdrasil.sample.orm.jsqlparser;

import java.util.Arrays;

import org.apache.tomcat.jdbc.pool.DataSource;
import org.apache.tomcat.jdbc.pool.PoolProperties;
import org.s3s3l.yggdrasil.orm.bind.express.jsqlparser.JSqlParserDataBindExpress;
import org.s3s3l.yggdrasil.orm.ds.DefaultDatasourceHolder;
import org.s3s3l.yggdrasil.orm.exec.DefaultSqlExecutor;
import org.s3s3l.yggdrasil.orm.exec.SqlExecutor;
import org.s3s3l.yggdrasil.orm.meta.MetaManager;
import org.s3s3l.yggdrasil.orm.meta.MetaManagerConfig;
import org.s3s3l.yggdrasil.orm.pool.ConnManager;
import org.s3s3l.yggdrasil.sample.orm.condition.UserCondition;
import org.s3s3l.yggdrasil.sample.orm.dao.User;
import org.s3s3l.yggdrasil.utils.common.FreeMarkerHelper;
import org.s3s3l.yggdrasil.utils.common.StringUtils;
import org.s3s3l.yggdrasil.utils.file.FileUtils;
import org.s3s3l.yggdrasil.utils.stuctural.jackson.JacksonUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Application {
    public static void main(String[] args) {
        DataSource datasource = new DataSource(
                JacksonUtils.YAML.toObject(FileUtils.getFirstExistResource("datasource.yaml"), PoolProperties.class));
        MetaManager metaManager = new MetaManager(MetaManagerConfig.builder().tableDefinePackages(new String[] {
                "org.s3s3l.yggdrasil.sample.orm" }).build());
        SqlExecutor sqlExecutor = DefaultSqlExecutor.builder()
                .datasourceHolder(new DefaultDatasourceHolder(
                        ConnManager.DEFAULT, datasource))
                .dataBindExpress(new JSqlParserDataBindExpress(metaManager))
                .metaManager(metaManager)
                .freeMarkerHelper(new FreeMarkerHelper())
                .build();
        sqlExecutor.create(User.class, false);
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
        sqlExecutor.select(UserCondition.builder().id(id).build(), User.class).forEach(System.out::println);
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
    }
}
