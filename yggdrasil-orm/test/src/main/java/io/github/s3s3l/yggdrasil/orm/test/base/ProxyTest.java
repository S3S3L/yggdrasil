package io.github.s3s3l.yggdrasil.orm.test.base;

import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import io.github.s3s3l.yggdrasil.orm.exec.CreateConfig;
import io.github.s3s3l.yggdrasil.orm.exec.SqlExecutor;
import io.github.s3s3l.yggdrasil.orm.test.condition.UserCondition;
import io.github.s3s3l.yggdrasil.orm.test.dao.User;
import io.github.s3s3l.yggdrasil.orm.test.helper.TestHelper;
import io.github.s3s3l.yggdrasil.orm.test.proxy.UserProxy;
import io.github.s3s3l.yggdrasil.utils.collection.CollectionUtils;
import io.github.s3s3l.yggdrasil.utils.common.StringUtils;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public abstract class ProxyTest implements BaseTest {

    private final TestHelper testHelper = getHelper();
    private final SqlExecutor sqlExecutor = testHelper.getSqlExecutor();
    private final UserProxy userProxy = sqlExecutor.getProxy(UserProxy.class);

    String id = StringUtils.getUUIDNoLine();
    String id2 = StringUtils.getUUIDNoLine();
    User user1 = User.builder()
            .id(id)
            .username("username1")
            .password("pwd1")
            .realName("realName1")
            .deleted(false)
            // .remarks(new String[] { "remark1.1", "remark1.2" })
            .build();
    User user2 = User.builder()
            .id(id2)
            .username("username2")
            .password("pwd2")
            .realName("realName2")
            .deleted(false)
            // .remarks(new String[] { "remark2.1", "remark2.2" })
            .build();

    @BeforeEach
    public void recreateTable() {
        sqlExecutor.create(User.class, CreateConfig.builder()
                .dropFirst(true)
                .build());
        userProxy.addOne(user1);
        userProxy.addOne(user2);
    }

    @Order(1)
    @Test
    public void count() {
        Assertions.assertEquals(2, userProxy.userCount(UserCondition.builder()
                .ids(new String[] { id, id2 })
                .build()));
        Assertions.assertEquals(1, userProxy.userCount(UserCondition.builder()
                .ids(new String[] { id })
                .build()));
    }

    @Order(2)
    @Test
    public void get() {
        User user = userProxy.get(UserCondition.builder()
                .id(id)
                .build());
        Assertions.assertNull(user.getId());
        Assertions.assertEquals(user1.getUsername(), user.getUsername());
        Assertions.assertEquals(user1.getPassword(), user.getPassword());
        Assertions.assertNull(user.getDeleted());
        // Assertions.assertNull(user.getRemarks());
    }

    @Order(3)
    @Test
    public void list() {
        List<User> users = userProxy.list(UserCondition.builder()
                .ids(new String[] { id })
                .build());
        Assertions.assertEquals(1, users.size());
        User user = CollectionUtils.getFirst(users);
        Assertions.assertNull(user.getId());
        Assertions.assertEquals(user1.getUsername(), user.getUsername());
        Assertions.assertEquals(user1.getPassword(), user.getPassword());
        Assertions.assertNull(user.getDeleted());
        // Assertions.assertNull(user.getRemarks());
    }

}
