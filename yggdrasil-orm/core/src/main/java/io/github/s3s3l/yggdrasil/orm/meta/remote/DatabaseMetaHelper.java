package io.github.s3s3l.yggdrasil.orm.meta.remote;

import io.github.s3s3l.yggdrasil.orm.base.BaseOnDatabase;
import io.github.s3s3l.yggdrasil.orm.ds.DatasourceHolder;
import io.github.s3s3l.yggdrasil.orm.meta.TableMeta;

public interface DatabaseMetaHelper extends BaseOnDatabase {
    TableMeta getTableMeta(String tableName, DatasourceHolder datasourceHolder);
}
