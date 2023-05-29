package io.github.s3s3l.yggdrasil.orm.postgresql.meta.remote;

import java.sql.DatabaseMetaData;
import java.sql.JDBCType;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import io.github.s3s3l.yggdrasil.orm.ds.DatasourceHolder;
import io.github.s3s3l.yggdrasil.orm.enumerations.DatabaseType;
import io.github.s3s3l.yggdrasil.orm.exception.SqlExecutingException;
import io.github.s3s3l.yggdrasil.orm.meta.ColumnMeta;
import io.github.s3s3l.yggdrasil.orm.meta.DbType;
import io.github.s3s3l.yggdrasil.orm.meta.TableMeta;
import io.github.s3s3l.yggdrasil.orm.meta.remote.AbstractDatabaseMetaHelper;
import io.github.s3s3l.yggdrasil.orm.postgresql.meta.remote.columns.PsqlColumnMetaColumns;
import io.github.s3s3l.yggdrasil.orm.postgresql.meta.remote.columns.PsqlPkColumns;
import io.github.s3s3l.yggdrasil.orm.postgresql.meta.remote.columns.PsqlTableMetaColumns;
import io.github.s3s3l.yggdrasil.utils.common.StringUtils;

public class PsqlDatabaseMetaHelper extends AbstractDatabaseMetaHelper {

    @Override
    protected TableMeta tableMeta(String tableName, DatasourceHolder datasourceHolder) {
        try {
            return datasourceHolder.useConn(conn -> {
                DatabaseMetaData metaData = conn.getMetaData();
                ResultSet rs = metaData.getTables(null, conn.getSchema(), tableName, new String[] { "TABLE" });
                if (!rs.next()) {
                    return null;
                }

                return TableMeta.builder()
                        .name(rs.getString(PsqlTableMetaColumns.TABLE_NAME.name()))
                        .build();
            });
        } catch (SQLException e) {
            throw new SqlExecutingException(e);
        }
    }

    @Override
    protected List<ColumnMeta> columnMetas(String tableName, DatasourceHolder datasourceHolder) {
        try {
            return datasourceHolder.useConn(conn -> {
                List<ColumnMeta> columns = new LinkedList<>();
                DatabaseMetaData metaData = conn.getMetaData();
                String pk = "";
                String schema = conn.getSchema();

                // 获取主键
                ResultSet pkRs = metaData.getPrimaryKeys(null, schema, tableName);
                if (pkRs.next()) {
                    pk = pkRs.getString(PsqlPkColumns.column_name.name());
                }

                boolean hasPk = StringUtils.isNotEmpty(pk);

                ResultSet rs = metaData.getColumns(null, schema, tableName, null);
                while (rs.next()) {
                    // 获取字段类型
                    int dataType = rs.getInt(PsqlColumnMetaColumns.DATA_TYPE.name());
                    DbType dbType = DbType.builder()
                            .notNull(!strToBoolean(rs.getString(PsqlColumnMetaColumns.IS_NULLABLE.name())))
                            .type(JDBCType.valueOf(rs.getInt(PsqlColumnMetaColumns.DATA_TYPE.name())))
                            .build();
                    switch (dataType) {
                        // varchar
                        case 12:
                            dbType.setArgs(Arrays.asList(rs.getString(PsqlColumnMetaColumns.COLUMN_SIZE.name())));
                            break;
                    }

                    // 获取字段名
                    String colName = rs.getString(PsqlColumnMetaColumns.COLUMN_NAME.name());
                    // 设置字段是否为主键
                    dbType.setPrimary(hasPk && pk.equals(colName));

                    columns.add(ColumnMeta.builder()
                            .name(colName)
                            .dbType(dbType)
                            .tableName(tableName)
                            .build());
                }

                return columns;
            });
        } catch (SQLException e) {
            throw new SqlExecutingException(e);
        }
    }

    @Override
    public DatabaseType databaseType() {
        return DatabaseType.POSTGRESQL;
    }

}
