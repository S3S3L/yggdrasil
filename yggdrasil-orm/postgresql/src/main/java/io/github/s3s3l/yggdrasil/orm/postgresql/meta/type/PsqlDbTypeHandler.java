package io.github.s3s3l.yggdrasil.orm.postgresql.meta.type;

import java.sql.JDBCType;

import io.github.s3s3l.yggdrasil.orm.enumerations.DatabaseType;
import io.github.s3s3l.yggdrasil.orm.meta.DbType;
import io.github.s3s3l.yggdrasil.orm.meta.type.DbTypeHandler;
import io.github.s3s3l.yggdrasil.utils.collection.CollectionUtils;

public class PsqlDbTypeHandler implements DbTypeHandler {

    @Override
    public DatabaseType databaseType() {
        return DatabaseType.POSTGRESQL;
    }

    @Override
    public String getDataTypeStr(DbType dbType) {
        switch (dbType.getType()) {
            default: {
                if (dbType.isArray()) {
                    return dbType.getType()
                            .name() + "[]";
                }
                return dbType.getType()
                        .name();
            }
        }
    }

    @Override
    public String getFullTypeDef(DbType dbType) {
        StringBuilder typeBuf = new StringBuilder(getDataTypeStr(dbType));

        if (!CollectionUtils.isEmpty(dbType.getArgs())) {
            typeBuf.append(String.format(" (%s)", String.join(", ", dbType.getArgs())));
        }

        if (dbType.isPrimary()) {
            typeBuf.append(" PRIMARY KEY");
        }

        if (dbType.isNotNull()) {
            typeBuf.append(" NOT NULL");
        }

        return typeBuf.toString();
    }

    @Override
    public DbType toJdbcType(DbType dbType) {
        if (dbType == null) {
            return null;
        }

        DbType jdbcDbType = DbType.builder()
                .type(dbType.getType())
                .array(dbType.isArray())
                .primary(dbType.isPrimary())
                .notNull(dbType.isNotNull())
                .def(dbType.isDef())
                .defValue(dbType.getDefValue())
                .args(dbType.getArgs())
                .build();

        switch (dbType.getType()) {
            case ARRAY:
                jdbcDbType.setArray(true);
                jdbcDbType.setType(JDBCType.valueOf(dbType.getTypeName()
                        .substring(1)
                        .toUpperCase()));
                break;
            case BIT:
                switch (dbType.getTypeName()) {
                    case "bool":
                        jdbcDbType.setType(JDBCType.BOOLEAN);
                        break;
                    default:
                        break;
                }
                break;
            default:
                break;
        }

        return jdbcDbType;
    }

}
