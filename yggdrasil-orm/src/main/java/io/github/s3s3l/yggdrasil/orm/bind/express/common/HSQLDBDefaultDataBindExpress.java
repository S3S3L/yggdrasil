package io.github.s3s3l.yggdrasil.orm.bind.express.common;

import io.github.s3s3l.yggdrasil.orm.bind.sql.DefaultSqlStruct;
import io.github.s3s3l.yggdrasil.orm.bind.sql.SqlStruct;
import io.github.s3s3l.yggdrasil.orm.enumerations.DatabaseType;
import io.github.s3s3l.yggdrasil.orm.meta.ColumnMeta;
import io.github.s3s3l.yggdrasil.orm.meta.DbType;
import io.github.s3s3l.yggdrasil.orm.meta.MetaManager;
import io.github.s3s3l.yggdrasil.utils.collection.CollectionUtils;
import io.github.s3s3l.yggdrasil.utils.verify.Verify;

public class HSQLDBDefaultDataBindExpress extends DefaultDataBindExpress {

    public HSQLDBDefaultDataBindExpress(MetaManager metaManager) {
        super(metaManager);
    }

    @Override
    public DatabaseType databaseType() {
        return DatabaseType.HSQLDB;
    }

    @Override
    public SqlStruct getColumnAdd(ColumnMeta columnMeta) {
        Verify.notNull(columnMeta);

        DefaultSqlStruct struct = new DefaultSqlStruct();
        DbType dbType = columnMeta.getDbType();

        struct.appendSql(String.format("ALTER TABLE %s ADD %s %s", columnMeta.getTableAlias(), columnMeta.getName(),
                dbType.getType()));
        if (CollectionUtils.isNotEmpty(dbType.getArgs())) {
            struct.appendSql(String.format("(%s)", String.join(", ", dbType.getArgs())));
        }

        if (dbType.isNotNull() && dbType.isDef()) {
            struct.appendSql(String.format(" NOT NULL DEFAULT '%s'", dbType.getDefValue()));
        } else if(dbType.isDef()) {
            struct.appendSql(String.format(" DEFAULT '%s'", dbType.getDefValue()));
        }
        struct.appendSql(";");

        return struct;
    }

    @Override
    public SqlStruct getColumnDrop(ColumnMeta columnMeta) {
        Verify.notNull(columnMeta);

        DefaultSqlStruct struct = new DefaultSqlStruct();

        struct.appendSql(
                String.format("ALTER TABLE %s DROP COLUMN %s;", columnMeta.getTableAlias(), columnMeta.getName()));

        return struct;
    }

    @Override
    public SqlStruct getColumnAlter(ColumnMeta columnMeta) {
        Verify.notNull(columnMeta);

        DefaultSqlStruct struct = new DefaultSqlStruct();
        DbType dbType = columnMeta.getDbType();

        struct.appendSql(String.format("ALTER TABLE %s ALTER COLUMN %s %s", columnMeta.getTableAlias(),
                columnMeta.getName(), dbType.getType()));
        if (CollectionUtils.isNotEmpty(dbType.getArgs())) {
            struct.appendSql(String.format("(%s)", String.join(", ", dbType.getArgs())));
        }
        struct.appendSql(";");
        return struct;
    }

}
