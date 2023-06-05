package io.github.s3s3l.yggdrasil.orm.postgresql.test.config;

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
    public static final DatasourceConfig POSTGRESQL_DEFAULT = DatasourceConfig.builder()
            .configFile("datasource-postgresql.yaml")
            .databaseType(DatabaseType.POSTGRESQL)
            .expressBuilderType(ExpressBuilderType.DEFAULT)
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
