package io.github.s3s3l.yggdrasil.orm.postgresql.test;

import java.sql.DriverManager;
import java.sql.SQLException;

import org.junit.jupiter.api.Test;

import io.github.s3s3l.yggdrasil.orm.exec.SqlExecutor;
import io.github.s3s3l.yggdrasil.orm.postgresql.test.config.DatasourceConfig;
import io.github.s3s3l.yggdrasil.orm.postgresql.test.helper.TestHelper;


public class TcJdbcTest {
    @Test
    public void connTest() throws SQLException {
        DriverManager.getConnection("jdbc:tc:postgresql:12.14:///test");
    }

    @Test
    public void ormTest() {
        SqlExecutor sqlExecutor = TestHelper.getSqlExecutor(DatasourceConfig.POSTGRESQL_DEFAULT);

        sqlExecutor.execute("select 1");
    }
}
