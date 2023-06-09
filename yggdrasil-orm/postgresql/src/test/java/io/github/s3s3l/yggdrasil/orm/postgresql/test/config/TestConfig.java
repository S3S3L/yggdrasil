package io.github.s3s3l.yggdrasil.orm.postgresql.test.config;

import io.github.s3s3l.yggdrasil.orm.bind.express.ExpressBuilderType;
import io.github.s3s3l.yggdrasil.orm.enumerations.DatabaseType;
import io.github.s3s3l.yggdrasil.orm.test.config.DatasourceConfig;

public class TestConfig {

    public static final DatasourceConfig POSTGRESQL_DEFAULT = DatasourceConfig.builder()
            .configFile("datasource.yaml")
            .databaseType(DatabaseType.POSTGRESQL)
            .expressBuilderType(ExpressBuilderType.DEFAULT)
            .tableDefinePackages(new String[] { "io.github.s3s3l.yggdrasil.orm.test",
                    "io.github.s3s3l.yggdrasil.orm.postgresql.test" })
            .build();
    public static final DatasourceConfig POSTGRESQL_JSQLPARSER = DatasourceConfig.builder()
            .configFile("datasource.yaml")
            .databaseType(DatabaseType.POSTGRESQL)
            .expressBuilderType(ExpressBuilderType.JSQLPARSER)
            .tableDefinePackages(new String[] { "io.github.s3s3l.yggdrasil.orm.test",
                    "io.github.s3s3l.yggdrasil.orm.postgresql.test" })
            .build();
}
