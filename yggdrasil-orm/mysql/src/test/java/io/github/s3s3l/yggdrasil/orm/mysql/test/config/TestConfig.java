package io.github.s3s3l.yggdrasil.orm.mysql.test.config;

import io.github.s3s3l.yggdrasil.orm.bind.express.ExpressBuilderType;
import io.github.s3s3l.yggdrasil.orm.enumerations.DatabaseType;
import io.github.s3s3l.yggdrasil.orm.test.config.DatasourceConfig;

public class TestConfig {
    
    public static final DatasourceConfig MYSQL_DEFAULT = DatasourceConfig.builder()
            .configFile("datasource.yaml")
            .databaseType(DatabaseType.MYSQL)
            .expressBuilderType(ExpressBuilderType.DEFAULT)
            .build();
    public static final DatasourceConfig MYSQL_JSQLPARSER = DatasourceConfig.builder()
            .configFile("datasource.yaml")
            .databaseType(DatabaseType.MYSQL)
            .expressBuilderType(ExpressBuilderType.JSQLPARSER)
            .build();
}
