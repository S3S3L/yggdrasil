package io.github.s3s3l.yggdrasil.orm.mysql.bind.express.bind.express;

import io.github.s3s3l.yggdrasil.orm.bind.express.common.DefaultDataBindExpress;
import io.github.s3s3l.yggdrasil.orm.bind.sql.DefaultSqlStruct;
import io.github.s3s3l.yggdrasil.orm.bind.sql.SqlStruct;
import io.github.s3s3l.yggdrasil.orm.enumerations.DatabaseType;
import io.github.s3s3l.yggdrasil.orm.factory.DbTypeHandlerFactory;
import io.github.s3s3l.yggdrasil.orm.meta.ColumnMeta;
import io.github.s3s3l.yggdrasil.orm.meta.DbType;
import io.github.s3s3l.yggdrasil.orm.meta.LimitMeta;
import io.github.s3s3l.yggdrasil.orm.meta.MetaManager;
import io.github.s3s3l.yggdrasil.orm.meta.OffsetMeta;
import io.github.s3s3l.yggdrasil.utils.collection.CollectionUtils;
import io.github.s3s3l.yggdrasil.utils.reflect.PropertyDescriptorReflectionBean;
import io.github.s3s3l.yggdrasil.utils.verify.Verify;

public class MysqlDefaultDataBindExpress extends DefaultDataBindExpress {

    public MysqlDefaultDataBindExpress(MetaManager metaManager, DbTypeHandlerFactory dbTypeHandlerFactory) {
        super(metaManager, dbTypeHandlerFactory);
    }

    @Override
    public DatabaseType databaseType() {
        return DatabaseType.MYSQL;
    }

    @Override
    protected void offsetLimit(DefaultSqlStruct struct,
            OffsetMeta offset,
            LimitMeta limit,
            PropertyDescriptorReflectionBean ref) {
        Object limitVal = ref.getFieldValue(limit.getField()
                .getName());
        Object offsetVal = ref.getFieldValue(offset.getField()
                .getName());
        if (limit != null && offset != null && limitVal != null && offsetVal != null) {
            struct.appendSql(" LIMIT ?, ?");
            struct.addParam(offsetVal);
            struct.addParam(limitVal);
        } else if (limit != null && limitVal != null) {
            struct.appendSql(" LIMIT ?");
            struct.addParam(limitVal);
        }
    }

    @Override
    public SqlStruct getColumnAdd(ColumnMeta columnMeta) {
        Verify.notNull(columnMeta);

        DefaultSqlStruct struct = new DefaultSqlStruct();
        DbType dbType = columnMeta.getDbType();

        struct.appendSql(String.format("ALTER TABLE %s ADD COLUMN %s %s", columnMeta.getTableName(),
                columnMeta.getName(), dbTypeHandler.getDataTypeStr(dbType)));
        if (CollectionUtils.isNotEmpty(dbType.getArgs())) {
            struct.appendSql(String.format("(%s)", String.join(", ", dbType.getArgs())));
        }

        if (dbType.isNotNull()) {
            struct.appendSql(" NOT NULL");
        } else {
            struct.appendSql(" NULL");
        }

        if (dbType.isPrimary()) {
            struct.appendSql(String.format(", ADD PRIMARY KEY (%s)", columnMeta.getName()));
        }
        struct.appendSql(";");

        return struct;
    }

    @Override
    public SqlStruct getColumnDrop(ColumnMeta columnMeta) {
        Verify.notNull(columnMeta);

        DefaultSqlStruct struct = new DefaultSqlStruct();

        struct.appendSql(
                String.format("ALTER TABLE %s DROP COLUMN %s;", columnMeta.getTableName(), columnMeta.getName()));

        return struct;
    }

    @Override
    public SqlStruct getColumnAlter(ColumnMeta columnMeta) {
        Verify.notNull(columnMeta);

        DefaultSqlStruct struct = new DefaultSqlStruct();
        DbType dbType = columnMeta.getDbType();

        struct.appendSql(String.format("ALTER TABLE %s CHANGE COLUMN %s %s %s", columnMeta.getTableName(),
                columnMeta.getName(), columnMeta.getName(), dbTypeHandler.getDataTypeStr(dbType)));
        if (CollectionUtils.isNotEmpty(dbType.getArgs())) {
            struct.appendSql(String.format("(%s)", String.join(", ", dbType.getArgs())));
        }

        if (dbType.isNotNull()) {
            struct.appendSql(" NOT NULL");
        } else {
            struct.appendSql(" NULL");
        }
        struct.appendSql(";");

        return struct;
    }

}
