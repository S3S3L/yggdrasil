package io.github.s3s3l.yggdrasil.orm.test.base;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.github.s3s3l.yggdrasil.orm.exec.CreateConfig;
import io.github.s3s3l.yggdrasil.orm.exec.SqlExecutor;
import io.github.s3s3l.yggdrasil.orm.pagin.PaginResult;
import io.github.s3s3l.yggdrasil.orm.test.condition.UserCondition;
import io.github.s3s3l.yggdrasil.orm.test.dao.User;
import io.github.s3s3l.yggdrasil.orm.test.helper.TestHelper;
import io.github.s3s3l.yggdrasil.utils.collection.CollectionUtils;
import io.github.s3s3l.yggdrasil.utils.common.StringUtils;

public abstract class CurdTest<T extends User> implements BaseTest {
    private final TestHelper testHelper = getHelper();
    private SqlExecutor sqlExecutor = testHelper.getSqlExecutor();

    String id = StringUtils.getUUIDNoLine();
    String id2 = StringUtils.getUUIDNoLine();
    T user1 = buildUser(id, 1);
    T user2 = buildUser(id2, 2);

    @BeforeEach
    public void recreateTable() {
        sqlExecutor.create(tableType(), CreateConfig.builder()
                .dropFirst(true)
                .build());
    }

    @Test
    public void insert() {
        int result = sqlExecutor.insert(Arrays.asList(user1, user2));

        Assertions.assertEquals(2, result);
    }

    @Test
    public void delete() {
        sqlExecutor.insert(Arrays.asList(user1, user2));
        int result = sqlExecutor.delete(UserCondition.builder()
                .id(id)
                .build());
        Assertions.assertEquals(1, result);

        User user = sqlExecutor.selectOne(UserCondition.builder()
                .id(id)
                .build(), tableType());
        Assertions.assertNull(user);

        user = sqlExecutor.selectOne(UserCondition.builder()
                .id(id2)
                .build(), tableType());
        Assertions.assertNotNull(user);
    }

    @Test
    public void select() {
        sqlExecutor.insert(Arrays.asList(user1, user2));

        List<T> users = sqlExecutor.select(UserCondition.builder()
                .build(), tableType());
        Assertions.assertEquals(2, users.size());
        T user = CollectionUtils.getFirst(users, u -> u.getId()
                .equals(id));
        assertUser(user, user1);
    }

    @Test
    public void selectOne() {
        sqlExecutor.insert(Arrays.asList(user1, user2));

        T user = sqlExecutor.selectOne(UserCondition.builder()
                .id(id)
                .build(), tableType());
        assertUser(user, user1);
    }

    @Test
    public void selectPagin() {
        long recordsCount = 25;
        int pageSize = 10;
        long pageCount = -Math.floorDiv(-recordsCount, pageSize);

        sqlExecutor.insert(LongStream.range(0, recordsCount)
                .mapToObj(i -> buildUser(StringUtils.getUUIDNoLine(), i))
                .collect(Collectors.toList()));

        UserCondition paginCondition = UserCondition.builder()
                .pagin(true)
                .pageIndex(1)
                .pageSize(pageSize)
                .build();
        PaginResult<List<T>> paginResult = sqlExecutor.selectByPagin(paginCondition, tableType());
        Assertions.assertEquals(recordsCount, paginResult.getRecordsCount());
        Assertions.assertEquals(pageCount, paginResult.getPageCount());

        List<T> users = paginResult.getData();
        Assertions.assertEquals(pageSize, users.size());
    }

    @Test
    public void update() {
        sqlExecutor.insert(Arrays.asList(user1, user2));
        String newUsername = "updatedUserName";
        sqlExecutor.update(User.builder()
                .username(newUsername)
                .build(),
                UserCondition.builder()
                        .id(id)
                        .build());

        T user = sqlExecutor.selectOne(UserCondition.builder()
                .id(id)
                .build(), tableType());
        assertUser(newUsername, user, user1);

        user = sqlExecutor.selectOne(UserCondition.builder()
                .id(id2)
                .build(), tableType());
        assertUser(user, user2);
    }

    protected abstract Class<T> tableType();

    protected abstract T buildUser(String id, long index);

    protected void assertUser(T user, T other) {
        assertUser(other.getUsername(), user, other);
    }

    protected void assertUser(String newUsername, T user, T other) {
        Assertions.assertEquals(other.getId(), user.getId());
        Assertions.assertEquals(newUsername, user.getUsername());
        Assertions.assertEquals(other.getPassword(), user.getPassword());
        Assertions.assertEquals(other.getDeleted(), user.getDeleted());
        // Assertions.assertArrayEquals(user1.getRemarks(), user.getRemarks());
    }
}
