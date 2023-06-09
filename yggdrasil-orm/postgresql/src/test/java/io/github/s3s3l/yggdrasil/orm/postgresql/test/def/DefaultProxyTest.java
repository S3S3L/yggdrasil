package io.github.s3s3l.yggdrasil.orm.postgresql.test.def;

import io.github.s3s3l.yggdrasil.orm.postgresql.test.config.TestConfig;
import io.github.s3s3l.yggdrasil.orm.test.base.ProxyTest;
import io.github.s3s3l.yggdrasil.orm.test.helper.TestHelper;

public class DefaultProxyTest extends ProxyTest {

    @Override
    public TestHelper getHelper() {
        return new TestHelper(TestConfig.POSTGRESQL_DEFAULT);
    }
}
