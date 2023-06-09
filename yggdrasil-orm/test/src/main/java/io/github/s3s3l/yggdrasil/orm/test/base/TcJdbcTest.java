package io.github.s3s3l.yggdrasil.orm.test.base;

import java.sql.SQLException;

import org.junit.jupiter.api.Test;

import io.github.s3s3l.yggdrasil.orm.exec.SqlExecutor;
import io.github.s3s3l.yggdrasil.orm.test.helper.TestHelper;


public abstract class TcJdbcTest implements BaseTest {
    private final TestHelper testHelper = getHelper();
    
    @Test
    public void connTest() throws SQLException {
        testHelper.getDatasource(testHelper.config);
    }

    @Test
    public void ormTest() {
        SqlExecutor sqlExecutor = testHelper.getSqlExecutor();

        sqlExecutor.execute("select 1");
    }
}
