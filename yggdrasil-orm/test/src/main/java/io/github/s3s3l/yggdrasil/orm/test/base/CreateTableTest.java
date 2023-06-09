package io.github.s3s3l.yggdrasil.orm.test.base;

import java.util.stream.Stream;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import io.github.s3s3l.yggdrasil.orm.exec.CreateConfig;
import io.github.s3s3l.yggdrasil.orm.exec.SqlExecutor;
import io.github.s3s3l.yggdrasil.orm.meta.ColumnMeta;
import io.github.s3s3l.yggdrasil.orm.meta.MetaManager;
import io.github.s3s3l.yggdrasil.orm.meta.TableMeta;
import io.github.s3s3l.yggdrasil.orm.meta.remote.RemoteMetaManager;
import io.github.s3s3l.yggdrasil.orm.test.dao.OldUser;
import io.github.s3s3l.yggdrasil.orm.test.dao.User;
import io.github.s3s3l.yggdrasil.orm.test.helper.TestHelper;
import io.github.s3s3l.yggdrasil.utils.collection.CollectionUtils;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public abstract class CreateTableTest implements BaseTest {
    private final TestHelper testHelper = getHelper();

    private int oldColumnSize;
    private int columnSize;
    private long totalColumnSize;
    private SqlExecutor sqlExecutor = testHelper.getSqlExecutor();
    private MetaManager metaManager = testHelper.getMetaManager();
    private RemoteMetaManager remoteMetaManager = testHelper.getRemoteMetaManager();

    protected Class<?> tableType() {
        return User.class;
    }

    @BeforeEach
    public void createOld() {

        TableMeta oldTable = metaManager.getTable(OldUser.class);
        TableMeta table = metaManager.getTable(tableType());

        oldColumnSize = oldTable.getColumns()
                .size();
        columnSize = table.getColumns()
                .size();
        totalColumnSize = Stream.concat(oldTable.getColumns()
                .stream()
                .map(ColumnMeta::getName),
                table.getColumns()
                        .stream()
                        .map(ColumnMeta::getName))
                .distinct()
                .count();
        sqlExecutor.create(OldUser.class, CreateConfig.builder()
                .dropFirst(true)
                .build());
        checkTable(oldColumnSize);
    }

    @AfterEach
    public void drop() {
        sqlExecutor.drop(tableType());
        checkNoTable();
    }

    @Order(1)
    @Test
    public void dropFirst() {
        sqlExecutor.create(tableType(), CreateConfig.builder()
                .dropFirst(true)
                .build());

        checkTable(columnSize);
    }

    @Order(2)
    @Test
    public void autoAddColumn() {
        sqlExecutor.create(tableType(), CreateConfig.builder()
                .build());

        checkTable(totalColumnSize);
    }

    @Order(3)
    @Test
    public void autoDropColumn() {
        sqlExecutor.create(tableType(), CreateConfig.builder()
                .autoDropColumn(true)
                .build());

        checkTable(columnSize);
    }

    @Order(4)
    @Test
    public void autoAlterColumn() {
        sqlExecutor.create(tableType(), CreateConfig.builder()
                .autoAlterColumn(true)
                .build());

        checkTable(totalColumnSize);
        TableMeta defTableMeta = metaManager.getTable(tableType());

        remoteMetaManager.refresh();
        TableMeta tableMeta = remoteMetaManager.getTable(User.TABLE_NAME);
        Assertions.assertEquals(CollectionUtils.getFirst(defTableMeta.getColumns(), r -> r.getName()
                .equalsIgnoreCase("password"))
                .getDbType()
                .getArgs()
                .get(0),
                CollectionUtils.getFirst(tableMeta.getColumns(), r -> r.getName()
                        .equalsIgnoreCase("password"))
                        .getDbType()
                        .getArgs()
                        .get(0));
    }

    private void checkNoTable() {
        remoteMetaManager.refresh();
        TableMeta tableMeta = remoteMetaManager.getTable(User.TABLE_NAME);
        Assertions.assertNull(tableMeta);
    }

    private void checkTable(long columnSize) {
        remoteMetaManager.refresh();
        TableMeta tableMeta = remoteMetaManager.getTable(User.TABLE_NAME);
        Assertions.assertNotNull(tableMeta);
        Assertions.assertEquals(columnSize, tableMeta.getColumns()
                .size());
    }
}
