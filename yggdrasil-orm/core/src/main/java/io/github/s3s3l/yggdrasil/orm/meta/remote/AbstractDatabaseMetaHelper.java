package io.github.s3s3l.yggdrasil.orm.meta.remote;

import java.util.List;

import io.github.s3s3l.yggdrasil.bean.exception.ModelConvertException;
import io.github.s3s3l.yggdrasil.orm.ds.DatasourceHolder;
import io.github.s3s3l.yggdrasil.orm.factory.DbTypeHandlerFactory;
import io.github.s3s3l.yggdrasil.orm.meta.ColumnMeta;
import io.github.s3s3l.yggdrasil.orm.meta.TableMeta;
import io.github.s3s3l.yggdrasil.orm.meta.type.DbTypeHandler;
import io.github.s3s3l.yggdrasil.utils.verify.Verify;

public abstract class AbstractDatabaseMetaHelper implements DatabaseMetaHelper {

    protected final DbTypeHandler dbTypeHandler = DbTypeHandlerFactory.DEFAULT.getInstance(databaseType());

    @Override
    public TableMeta getTableMeta(String tableName, DatasourceHolder datasourceHolder) {
        Verify.assertEquals(datasourceHolder.getDatabaseType(), databaseType(),
                () -> String.format("Database type not match. Database: %s, MetaHelper: %s",
                        datasourceHolder.getDatabaseType(), databaseType()));
                        
        TableMeta tableMeta = tableMeta(tableName, datasourceHolder);
        if (tableMeta == null) {
            return null;
        }
        List<ColumnMeta> columnMetas = columnMetas(tableName, datasourceHolder);

        tableMeta.setColumns(columnMetas);
        return tableMeta;
    }

    protected boolean strToBoolean(String booleanStr) {
        switch (booleanStr) {
            case "YES":
                return true;
            case "NO":
                return false;
            default:
                throw new ModelConvertException(booleanStr + " can not convert to boolean");
        }
    }

    protected abstract TableMeta tableMeta(String tableName, DatasourceHolder datasourceHolder);

    protected abstract List<ColumnMeta> columnMetas(String tableName, DatasourceHolder datasourceHolder);
    
}
