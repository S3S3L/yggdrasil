package org.s3s3l.yggdrasil.sample.orm;

import org.apache.tomcat.jdbc.pool.DataSource;
import org.apache.tomcat.jdbc.pool.PoolProperties;
import org.s3s3l.yggdrasil.orm.exec.DefaultSqlExecutor;
import org.s3s3l.yggdrasil.orm.exec.SqlExecutor;
import org.s3s3l.yggdrasil.orm.meta.MetaManager;
import org.s3s3l.yggdrasil.utils.file.FileUtils;
import org.s3s3l.yggdrasil.utils.stuctural.jackson.JacksonUtils;

public class Application {
    public static void main(String[] args) {
        DataSource datasource = new DataSource(
                JacksonUtils.YAML.toObject(FileUtils.getFirstExistResource("datasource.yaml"), PoolProperties.class));
        SqlExecutor sqlExecutor = new DefaultSqlExecutor(datasource, new MetaManager("org.s3s3l.yggdrasil.sample.orm"));
    }
}
