package io.github.s3s3l.yggdrasil.orm.mysql.test.jsqlparser;

import io.github.s3s3l.yggdrasil.orm.mysql.test.config.TestConfig;
import io.github.s3s3l.yggdrasil.orm.test.base.ProxyTest;
import io.github.s3s3l.yggdrasil.orm.test.helper.TestHelper;

public class JsqlparserProxyTest extends ProxyTest {

    @Override
    public TestHelper getHelper() {
        return new TestHelper(TestConfig.MYSQL_JSQLPARSER);
    }

}
