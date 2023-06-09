package io.github.s3s3l.yggdrasil.orm.mysql.test.def;

import io.github.s3s3l.yggdrasil.orm.mysql.test.config.TestConfig;
import io.github.s3s3l.yggdrasil.orm.test.base.TcJdbcTest;
import io.github.s3s3l.yggdrasil.orm.test.helper.TestHelper;

public class DefaultTcJdbcTest extends TcJdbcTest {

    @Override
    public TestHelper getHelper() {
        return new TestHelper(TestConfig.MYSQL_DEFAULT);
    }
}
