package io.github.s3s3l.yggdrasil.orm.test.config;

import io.github.s3s3l.yggdrasil.orm.bind.express.ExpressBuilderType;
import io.github.s3s3l.yggdrasil.orm.enumerations.DatabaseType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class DatasourceConfig {

    private String configFile;
    private DatabaseType databaseType;
    private ExpressBuilderType expressBuilderType;
    @Builder.Default
    private String[] tableDefinePackages = new String[] { "io.github.s3s3l.yggdrasil.orm.test" };
}
