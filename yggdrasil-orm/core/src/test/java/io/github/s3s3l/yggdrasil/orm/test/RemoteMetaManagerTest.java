package io.github.s3s3l.yggdrasil.orm.test;

import org.apache.tomcat.jdbc.pool.DataSource;
import org.apache.tomcat.jdbc.pool.PoolProperties;

import io.github.s3s3l.yggdrasil.orm.ds.DatasourceHolder;
import io.github.s3s3l.yggdrasil.orm.ds.DefaultDatasourceHolder;
import io.github.s3s3l.yggdrasil.orm.enumerations.DatabaseType;
import io.github.s3s3l.yggdrasil.orm.factory.DatabaseMetaHelperFacotry;
import io.github.s3s3l.yggdrasil.orm.meta.remote.DatabaseMetaHelper;
import io.github.s3s3l.yggdrasil.orm.pool.ConnManager;
import io.github.s3s3l.yggdrasil.utils.stuctural.jackson.JacksonUtils;

public class RemoteMetaManagerTest {

    public static void main(String[] args) {
        PoolProperties pool = new PoolProperties();
        // pool.setDriverClassName("org.hsqldb.jdbc.JDBCDriver");
        // pool.setUrl("jdbc:hsqldb:file:/home/kehewei/github/market/db/market");
        // pool.setUsername("market");
        // pool.setPassword("market");
        // DatabaseMetaHelper databaseMetaHelper = new
        // HSQLDBDatabaseMetaHelper(DefaultDatasourceHolder.builder()

        // pool.setDriverClassName("com.mysql.cj.jdbc.Driver");
        // pool.setUrl("jdbc:mysql://localhost:3306/market");
        // pool.setUsername("mysql");
        // pool.setPassword("admin123");
        // DatabaseMetaHelper databaseMetaHelper = new
        // MysqlDatabaseMetaHelper(DefaultDatasourceHolder.builder()

        pool.setDriverClassName("org.postgresql.Driver");
        pool.setUrl("jdbc:postgresql://localhost:5432/market");
        pool.setUsername("psql");
        pool.setPassword("Psql@1122");
        DatasourceHolder datasourceHolder = DefaultDatasourceHolder.builder()
                .connManager(ConnManager.DEFAULT)
                .datasource(new DataSource(pool))
                .build();
        DatabaseMetaHelper databaseMetaHelper = DatabaseMetaHelperFacotry.DEFAULT.getInstance(DatabaseType.MYSQL);

        System.out.println(JacksonUtils.JSON.prettyPrinter()
                .toStructuralString(databaseMetaHelper.getTableMeta("t_user", datasourceHolder)));
    }
}
