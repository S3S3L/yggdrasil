package io.github.s3s3l.yggdrasil.orm.meta.remote;

import io.github.s3s3l.yggdrasil.orm.ds.DatasourceHolder;
import io.github.s3s3l.yggdrasil.orm.enumerations.DatabaseType;
import io.github.s3s3l.yggdrasil.orm.meta.TableMeta;

public interface DatabaseMetaHelper {
    TableMeta getTableMeta(String tableName, DatasourceHolder datasourceHolder);

    DatabaseType databaseType();
}
