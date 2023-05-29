package io.github.s3s3l.yggdrasil.orm.base;

import io.github.s3s3l.yggdrasil.orm.enumerations.DatabaseType;

public interface BaseOnDatabase {

    /**
     * @return 对应的数据库类型
     */
    DatabaseType databaseType();

}
