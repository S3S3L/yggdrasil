package io.github.s3s3l.yggdrasil.starter.datasource.feature;

import javax.sql.DataSource;

import freemarker.template.Configuration;
import io.github.s3s3l.yggdrasil.orm.ds.DefaultDatasourceHolder;
import io.github.s3s3l.yggdrasil.orm.exec.DefaultSqlExecutor;
import io.github.s3s3l.yggdrasil.orm.exec.SqlExecutor;
import io.github.s3s3l.yggdrasil.orm.factory.DataBindExpressFactory;
import io.github.s3s3l.yggdrasil.orm.meta.MetaManager;
import io.github.s3s3l.yggdrasil.orm.wrapper.SqlObjectWrapper;
import io.github.s3s3l.yggdrasil.starter.datasource.config.AutoCreateConfig;
import io.github.s3s3l.yggdrasil.starter.datasource.config.DatasourceConfiguration;
import io.github.s3s3l.yggdrasil.utils.common.FreeMarkerHelper;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SqlExecutorFactory {

    public static final String BEAN_NAME = "sqlExecutorFactory";

    /**
     * 构建数据源对应的SqlExecutor
     * 
     * @param packages
     *            数据结构定义类型所在包
     * @param datasource
     *            数据源
     * @return
     */
    public SqlExecutor sqlExecutor(DatasourceConfiguration datasourceConfiguration,
            MetaManager metaManager,
            DataSource datasource,
            DataBindExpressFactory dataBindExpressFactory) {
        SqlExecutor sqlExecutor = DefaultSqlExecutor.builder()
                .datasourceHolder(new DefaultDatasourceHolder(new SpringConnManager(), datasource,
                        datasourceConfiguration.getDatabaseType()))
                .dataBindExpress(dataBindExpressFactory.getInstance(datasourceConfiguration.getDatabaseType(),
                        datasourceConfiguration.getExpressBuilderType()))
                .metaManager(metaManager)
                .freeMarkerHelper(new FreeMarkerHelper().config(config -> config
                        .setObjectWrapper(new SqlObjectWrapper(Configuration.DEFAULT_INCOMPATIBLE_IMPROVEMENTS))))
                .build();

        AutoCreateConfig autoCreateConfig = datasourceConfiguration.getAutoCreate();

        if (autoCreateConfig != null && autoCreateConfig.isEnable()) {
            for (Class<?> table : metaManager.allTableTypes()) {
                log.info("Auto create table for type: {}", table.getName());
                sqlExecutor.create(table, autoCreateConfig);
            }
        }

        return sqlExecutor;
    }
}
