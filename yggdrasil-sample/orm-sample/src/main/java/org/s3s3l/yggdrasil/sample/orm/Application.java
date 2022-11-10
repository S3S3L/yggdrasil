package org.s3s3l.yggdrasil.sample.orm;

import java.util.Arrays;
import java.util.List;

import org.apache.tomcat.jdbc.pool.DataSource;
import org.apache.tomcat.jdbc.pool.PoolProperties;
import org.s3s3l.yggdrasil.orm.bind.express.common.DefaultDataBindExpress;
import org.s3s3l.yggdrasil.orm.exec.DefaultSqlExecutor;
import org.s3s3l.yggdrasil.orm.exec.SqlExecutor;
import org.s3s3l.yggdrasil.orm.meta.MetaManager;
import org.s3s3l.yggdrasil.sample.orm.condition.UserCondition;
import org.s3s3l.yggdrasil.sample.orm.dao.User;
import org.s3s3l.yggdrasil.utils.common.StringUtils;
import org.s3s3l.yggdrasil.utils.file.FileUtils;
import org.s3s3l.yggdrasil.utils.stuctural.jackson.JacksonUtils;

public class Application {
    public static void main(String[] args) {
        DataSource datasource = new DataSource(
                JacksonUtils.YAML.toObject(FileUtils.getFirstExistResource("datasource.yaml"), PoolProperties.class));
        MetaManager metaManager = new MetaManager("org.s3s3l.yggdrasil.sample.orm");
        SqlExecutor sqlExecutor = new DefaultSqlExecutor(datasource, new DefaultDataBindExpress(
                metaManager), metaManager);
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
        List<User> users = sqlExecutor.select(UserCondition.builder().id(id).build(), User.class);
        users.forEach(System.out::println);
    }
}
