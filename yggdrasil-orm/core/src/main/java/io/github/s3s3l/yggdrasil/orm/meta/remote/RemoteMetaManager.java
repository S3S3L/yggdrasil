package io.github.s3s3l.yggdrasil.orm.meta.remote;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import io.github.s3s3l.yggdrasil.orm.ds.DatasourceHolder;
import io.github.s3s3l.yggdrasil.orm.factory.DatabaseMetaHelperFacotry;
import io.github.s3s3l.yggdrasil.orm.meta.TableMeta;

public class RemoteMetaManager {
    private final Map<String, TableMeta> tables = new ConcurrentHashMap<>();

    private final DatabaseMetaHelper databaseMetaHelper;

    private final DatasourceHolder datasourceHolder;

    public RemoteMetaManager(DatasourceHolder datasourceHolder) {
        this.datasourceHolder = datasourceHolder;
        this.databaseMetaHelper = DatabaseMetaHelperFacotry.DEFAULT.getInstance(datasourceHolder.getDatabaseType());
    }

    public TableMeta getTable(String tableName) {
        return tables.computeIfAbsent(tableName, k -> databaseMetaHelper.getTableMeta(k, datasourceHolder));
    }

    public void refresh() {
        tables.clear();
    }
}
