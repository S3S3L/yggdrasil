package io.github.s3s3l.yggdrasil.orm.mysql.test.jsqlparser;

import io.github.s3s3l.yggdrasil.orm.mysql.test.config.TestConfig;
import io.github.s3s3l.yggdrasil.orm.test.base.CurdTest;
import io.github.s3s3l.yggdrasil.orm.test.dao.User;
import io.github.s3s3l.yggdrasil.orm.test.helper.TestHelper;

public class JsqlparserCurdTest extends CurdTest<User> {

    @Override
    public TestHelper getHelper() {
        return new TestHelper(TestConfig.MYSQL_JSQLPARSER);
    }

    @Override
    protected Class<User> tableType() {
        return User.class;
    }

    @Override
    protected User buildUser(String id, long index) {
        return User.builder()
                .id(id)
                .username("username" + index)
                .password("pwd" + index)
                .realName("realName" + index)
                .deleted(false)
                .build();
    }
}
