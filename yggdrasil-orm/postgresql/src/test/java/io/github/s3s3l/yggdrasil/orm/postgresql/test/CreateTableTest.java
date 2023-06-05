package io.github.s3s3l.yggdrasil.orm.postgresql.test;

import java.util.stream.Stream;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.github.s3s3l.yggdrasil.orm.exec.CreateConfig;
import io.github.s3s3l.yggdrasil.orm.exec.SqlExecutor;
import io.github.s3s3l.yggdrasil.orm.meta.ColumnMeta;
import io.github.s3s3l.yggdrasil.orm.meta.MetaManager;
import io.github.s3s3l.yggdrasil.orm.meta.TableMeta;
import io.github.s3s3l.yggdrasil.orm.meta.remote.RemoteMetaManager;
import io.github.s3s3l.yggdrasil.orm.postgresql.test.config.DatasourceConfig;
import io.github.s3s3l.yggdrasil.orm.postgresql.test.dao.OldUser;
import io.github.s3s3l.yggdrasil.orm.postgresql.test.dao.User;
import io.github.s3s3l.yggdrasil.orm.postgresql.test.helper.TestHelper;
import io.github.s3s3l.yggdrasil.utils.collection.CollectionUtils;

public class CreateTableTest {

    private static final DatasourceConfig CONFIG = DatasourceConfig.POSTGRESQL_DEFAULT;

    private static int oldColumnSize;
    private static int columnSize;
    private static long totalColumnSize;
    private static SqlExecutor sqlExecutor;
    private static MetaManager metaManager;
    private static RemoteMetaManager remoteMetaManager;

    @BeforeAll
    public static void beforeAll() {
        sqlExecutor = TestHelper.getSqlExecutor(CONFIG);
        metaManager = TestHelper.getMetaManager();
        remoteMetaManager = TestHelper.getRemoteMetaManager(CONFIG);

        TableMeta oldTable = metaManager.getTable(OldUser.class);
        TableMeta table = metaManager.getTable(User.class);

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
    }

    @BeforeEach
    public void createOld() {
        sqlExecutor.create(OldUser.class, CreateConfig.builder()
                .dropFirst(true)
                .build());
        checkTable(oldColumnSize);
    }

    @AfterEach
    public void drop() {
        sqlExecutor.drop(User.class);
        checkNoTable();
    }

    @Test
    public void dropFirst() {
        sqlExecutor.create(User.class, CreateConfig.builder()
                .dropFirst(true)
                .build());

        checkTable(columnSize);
    }

    @Test
    public void autoAddColumn() {
        sqlExecutor.create(User.class, CreateConfig.builder()
                .build());

        checkTable(totalColumnSize);
    }

    @Test
    public void autoDropColumn() {
        sqlExecutor.create(User.class, CreateConfig.builder()
                .autoDropColumn(true)
                .build());

        checkTable(columnSize);
    }

    @Test
    public void autoAlterColumn() {
        sqlExecutor.create(User.class, CreateConfig.builder()
                .autoAlterColumn(true)
                .build());

        checkTable(totalColumnSize);
        TableMeta defTableMeta = metaManager.getTable(User.class);
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
