package io.github.s3s3l.yggdrasil.sample.orm.config;

import io.github.s3s3l.yggdrasil.orm.bind.express.ExpressBuilderType;
import io.github.s3s3l.yggdrasil.orm.enumerations.DatabaseType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class DatasourceConfig {

    public static final DatasourceConfig HSQLDB_DEFAULT = DatasourceConfig.builder()
            .configFile("datasource-hsqldb.yaml")
            .databaseType(DatabaseType.HSQLDB)
            .expressBuilderType(ExpressBuilderType.DEFAULT)
            .build();
    public static final DatasourceConfig MYSQL_DEFAULT = DatasourceConfig.builder()
            .configFile("datasource-mysql.yaml")
            .databaseType(DatabaseType.MYSQL)
            .expressBuilderType(ExpressBuilderType.DEFAULT)
            .build();
    public static final DatasourceConfig POSTGRESQL_DEFAULT = DatasourceConfig.builder()
            .configFile("datasource-postgresql.yaml")
            .databaseType(DatabaseType.POSTGRESQL)
            .expressBuilderType(ExpressBuilderType.DEFAULT)
            .build();

    public static final DatasourceConfig HSQLDB_JSQLPARSER = DatasourceConfig.builder()
            .configFile("datasource-hsqldb.yaml")
            .databaseType(DatabaseType.HSQLDB)
            .expressBuilderType(ExpressBuilderType.JSQLPARSER)
            .build();
    public static final DatasourceConfig MYSQL_JSQLPARSER = DatasourceConfig.builder()
            .configFile("datasource-mysql.yaml")
            .databaseType(DatabaseType.MYSQL)
            .expressBuilderType(ExpressBuilderType.JSQLPARSER)
            .build();
    public static final DatasourceConfig POSTGRESQL_JSQLPARSER = DatasourceConfig.builder()
            .configFile("datasource-postgresql.yaml")
            .databaseType(DatabaseType.POSTGRESQL)
            .expressBuilderType(ExpressBuilderType.JSQLPARSER)
            .build();

    private String configFile;
    private DatabaseType databaseType;
    private ExpressBuilderType expressBuilderType;
}
