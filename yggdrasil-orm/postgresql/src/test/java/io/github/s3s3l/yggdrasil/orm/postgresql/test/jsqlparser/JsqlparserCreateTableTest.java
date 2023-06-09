package io.github.s3s3l.yggdrasil.orm.postgresql.test.jsqlparser;

import io.github.s3s3l.yggdrasil.orm.postgresql.test.config.TestConfig;
import io.github.s3s3l.yggdrasil.orm.postgresql.test.dao.PgUser;
import io.github.s3s3l.yggdrasil.orm.test.base.CreateTableTest;
import io.github.s3s3l.yggdrasil.orm.test.helper.TestHelper;

public class JsqlparserCreateTableTest extends CreateTableTest {

    @Override
    protected Class<?> tableType() {
        return PgUser.class;
    }

    @Override
    public TestHelper getHelper() {
        return new TestHelper(TestConfig.POSTGRESQL_JSQLPARSER);
    }
}
