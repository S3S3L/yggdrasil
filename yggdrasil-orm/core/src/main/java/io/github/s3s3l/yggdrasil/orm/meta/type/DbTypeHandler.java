package io.github.s3s3l.yggdrasil.orm.meta.type;

import io.github.s3s3l.yggdrasil.orm.base.BaseOnDatabase;
import io.github.s3s3l.yggdrasil.orm.meta.DbType;

public interface DbTypeHandler extends BaseOnDatabase {
    
    String getDataTypeStr(DbType dbType);

    String getFullTypeDef(DbType dbType);
}
