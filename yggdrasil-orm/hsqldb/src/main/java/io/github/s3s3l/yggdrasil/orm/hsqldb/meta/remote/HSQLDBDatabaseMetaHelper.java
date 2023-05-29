package io.github.s3s3l.yggdrasil.orm.hsqldb.meta.remote;

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
import io.github.s3s3l.yggdrasil.orm.hsqldb.meta.remote.columns.HSQLDBColumnMetaColumns;
import io.github.s3s3l.yggdrasil.orm.hsqldb.meta.remote.columns.HSQLDBPkColumns;
import io.github.s3s3l.yggdrasil.orm.hsqldb.meta.remote.columns.HSQLDBTableMetaColumns;
import io.github.s3s3l.yggdrasil.orm.meta.ColumnMeta;
import io.github.s3s3l.yggdrasil.orm.meta.DbType;
import io.github.s3s3l.yggdrasil.orm.meta.TableMeta;
import io.github.s3s3l.yggdrasil.orm.meta.remote.AbstractDatabaseMetaHelper;
import io.github.s3s3l.yggdrasil.utils.common.StringUtils;

public class HSQLDBDatabaseMetaHelper extends AbstractDatabaseMetaHelper {

    @Override
    protected TableMeta tableMeta(String tableName, DatasourceHolder datasourceHolder) {
        String realTableName = tableName.toUpperCase();
        try {
            return datasourceHolder.useConn(conn -> {
                DatabaseMetaData metaData = conn.getMetaData();
                ResultSet rs = metaData.getTables(null, conn.getSchema(), realTableName, new String[] { "TABLE" });
                if (!rs.next()) {
                    return null;
                }

                return TableMeta.builder()
                        .name(rs.getString(HSQLDBTableMetaColumns.TABLE_NAME.name()))
                        .build();
            });
        } catch (SQLException e) {
            throw new SqlExecutingException(e);
        }
    }

    @Override
    protected List<ColumnMeta> columnMetas(String tableName, DatasourceHolder datasourceHolder) {
        String realTableName = tableName.toUpperCase();
        try {
            return datasourceHolder.useConn(conn -> {
                List<ColumnMeta> columns = new LinkedList<>();
                DatabaseMetaData metaData = conn.getMetaData();
                String pk = "";
                String schema = conn.getSchema();

                // 获取主键
                ResultSet pkRs = metaData.getPrimaryKeys(null, schema, realTableName);
                if (pkRs.next()) {
                    pk = pkRs.getString(HSQLDBPkColumns.COLUMN_NAME.name());
                }

                boolean hasPk = StringUtils.isNotEmpty(pk);

                ResultSet rs = metaData.getColumns(null, schema, realTableName, null);
                while (rs.next()) {
                    // 获取字段类型
                    int dataType = rs.getInt(HSQLDBColumnMetaColumns.DATA_TYPE.name());
                    DbType dbType = DbType.builder()
                            .notNull(!strToBoolean(rs.getString(HSQLDBColumnMetaColumns.IS_NULLABLE.name())))
                            .type(JDBCType.valueOf(rs.getInt(HSQLDBColumnMetaColumns.DATA_TYPE.name())))
                            .build();
                    switch (dataType) {
                        // varchar
                        case 12:
                            dbType.setArgs(Arrays.asList(rs.getString(HSQLDBColumnMetaColumns.COLUMN_SIZE.name())));
                            break;
                    }

                    // 获取字段名
                    String colName = rs.getString(HSQLDBColumnMetaColumns.COLUMN_NAME.name());
                    // 设置字段是否为主键
                    dbType.setPrimary(hasPk && pk.equals(colName));

                    columns.add(ColumnMeta.builder()
                            .name(colName)
                            .dbType(dbType)
                            .tableName(realTableName)
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
        return DatabaseType.HSQLDB;
    }

}
