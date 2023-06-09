package io.github.s3s3l.yggdrasil.orm.postgresql.test.def;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import io.github.s3s3l.yggdrasil.orm.postgresql.test.config.TestConfig;
import io.github.s3s3l.yggdrasil.orm.postgresql.test.dao.PgUser;
import io.github.s3s3l.yggdrasil.orm.test.base.CurdTest;
import io.github.s3s3l.yggdrasil.orm.test.helper.TestHelper;

public class DefaultCurdTest extends CurdTest<PgUser> {

    @Test
    public void select() {
        super.select();
    }

    @Override
    public TestHelper getHelper() {
        return new TestHelper(TestConfig.POSTGRESQL_DEFAULT);
    }

    @Override
    protected Class<PgUser> tableType() {
        return PgUser.class;
    }

    @Override
    protected void assertUser(String newUsername, PgUser user, PgUser other) {
        super.assertUser(newUsername, user, other);
        Assertions.assertArrayEquals(other.getRemarks(), user.getRemarks());
    }

    @Override
    protected PgUser buildUser(String id, long index) {
        return PgUser.builder()
                .id(id)
                .username("username" + index)
                .password("pwd" + index)
                .realName("realName" + index)
                .deleted(false)
                .remarks(new String[] { "remark1." + index, "remark2." + index })
                .build();
    }
}
