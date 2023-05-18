package io.github.s3s3l.yggdrasil.orm.bind.express.common;

import io.github.s3s3l.yggdrasil.orm.bind.sql.DefaultSqlStruct;
import io.github.s3s3l.yggdrasil.orm.bind.sql.SqlStruct;
import io.github.s3s3l.yggdrasil.orm.enumerations.DatabaseType;
import io.github.s3s3l.yggdrasil.orm.meta.ColumnMeta;
import io.github.s3s3l.yggdrasil.orm.meta.DbType;
import io.github.s3s3l.yggdrasil.orm.meta.MetaManager;
import io.github.s3s3l.yggdrasil.utils.collection.CollectionUtils;
import io.github.s3s3l.yggdrasil.utils.verify.Verify;

public class PsqlDefaultDataBindExpress extends DefaultDataBindExpress {

    public PsqlDefaultDataBindExpress(MetaManager metaManager) {
        super(metaManager);
    }

    @Override
    public DatabaseType databaseType() {
        return DatabaseType.POSTGRESQL;
    }

    @Override
    public SqlStruct getColumnAdd(ColumnMeta columnMeta) {
        Verify.notNull(columnMeta);

        DefaultSqlStruct struct = new DefaultSqlStruct();
        DbType dbType = columnMeta.getDbType();

        struct.appendSql(String.format("ALTER TABLE IF EXISTS %s ADD COLUMN %s %s", columnMeta.getTableAlias(),
                columnMeta.getName(), dbType.getType()));
        if (CollectionUtils.isNotEmpty(dbType.getArgs())) {
            struct.appendSql(String.format("(%s)", String.join(", ", dbType.getArgs())));
        }

        if (dbType.isPrimary()) {
            struct.appendSql(" PRIMARY KEY");
        }

        if (dbType.isNotNull()) {
            struct.appendSql(" NOT NULL");
        }
        struct.appendSql(";");

        return struct;
    }

    @Override
    public SqlStruct getColumnDrop(ColumnMeta columnMeta) {
        Verify.notNull(columnMeta);

        DefaultSqlStruct struct = new DefaultSqlStruct();

        struct.appendSql(String.format("ALTER TABLE IF EXISTS %s DROP COLUMN IF EXISTS %s;", columnMeta.getTableAlias(),
                columnMeta.getName()));

        return struct;
    }

    @Override
    public SqlStruct getColumnAlter(ColumnMeta columnMeta) {
        Verify.notNull(columnMeta);

        DefaultSqlStruct struct = new DefaultSqlStruct();
        DbType dbType = columnMeta.getDbType();

        struct.appendSql(String.format("ALTER TABLE IF EXISTS %s ALTER COLUMN %s TYPE %s", columnMeta.getTableAlias(),
                columnMeta.getName(), dbType.getType()));
        if (CollectionUtils.isNotEmpty(dbType.getArgs())) {
            struct.appendSql(String.format("(%s)", String.join(", ", dbType.getArgs())));
        }
        struct.appendSql(";");

        if (dbType.isNotNull()) {
            struct.appendSql(String.format("\nALTER TABLE IF EXISTS %s ALTER COLUMN %s SET NOT NULL;",
                    columnMeta.getTableAlias(), columnMeta.getName()));
        } else {
            struct.appendSql(String.format("\nALTER TABLE IF EXISTS %s ALTER COLUMN %s DROP NOT NULL;",
                    columnMeta.getTableAlias(), columnMeta.getName()));
        }

        return struct;
    }

}
