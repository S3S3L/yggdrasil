package io.github.s3s3l.yggdrasil.orm.postgresql.test.helper;

import org.apache.tomcat.jdbc.pool.DataSource;
import org.apache.tomcat.jdbc.pool.PoolProperties;

import freemarker.template.Configuration;
import io.github.s3s3l.yggdrasil.orm.ds.DefaultDatasourceHolder;
import io.github.s3s3l.yggdrasil.orm.exec.DefaultSqlExecutor;
import io.github.s3s3l.yggdrasil.orm.exec.SqlExecutor;
import io.github.s3s3l.yggdrasil.orm.factory.DataBindExpressFactory;
import io.github.s3s3l.yggdrasil.orm.factory.DbTypeHandlerFactory;
import io.github.s3s3l.yggdrasil.orm.meta.MetaManager;
import io.github.s3s3l.yggdrasil.orm.meta.MetaManagerConfig;
import io.github.s3s3l.yggdrasil.orm.meta.remote.RemoteMetaManager;
import io.github.s3s3l.yggdrasil.orm.pool.ConnManager;
import io.github.s3s3l.yggdrasil.orm.postgresql.test.config.DatasourceConfig;
import io.github.s3s3l.yggdrasil.orm.wrapper.SqlObjectWrapper;
import io.github.s3s3l.yggdrasil.utils.common.FreeMarkerHelper;
import io.github.s3s3l.yggdrasil.utils.file.FileUtils;
import io.github.s3s3l.yggdrasil.utils.stuctural.jackson.JacksonUtils;

public abstract class TestHelper {

    private static DataSource dataSource;
    private static MetaManager metaManager;
    private static RemoteMetaManager remoteMetaManager;
    private static DefaultDatasourceHolder datasourceHolder;
    private static SqlExecutor sqlExecutor;

    public static SqlExecutor getSqlExecutor(DatasourceConfig config) {

        if (sqlExecutor == null) {
            MetaManager metaManager = getMetaManager();
            DataBindExpressFactory dataBindExpressFactory = new DataBindExpressFactory(metaManager,
                    DbTypeHandlerFactory.DEFAULT);
            DefaultDatasourceHolder datasourceHolder = getDatasourceHolder(config);
            sqlExecutor = DefaultSqlExecutor.builder()
                    .datasourceHolder(datasourceHolder)
                    .dataBindExpress(dataBindExpressFactory.getInstance(config.getDatabaseType(),
                            config.getExpressBuilderType()))
                    .metaManager(metaManager)
                    .freeMarkerHelper(new FreeMarkerHelper().config(c -> c
                            .setObjectWrapper(new SqlObjectWrapper(Configuration.DEFAULT_INCOMPATIBLE_IMPROVEMENTS))))
                    .build();
        }

        return sqlExecutor;
    }

    public static DefaultDatasourceHolder getDatasourceHolder(DatasourceConfig config) {
        if (datasourceHolder == null) {
            datasourceHolder = new DefaultDatasourceHolder(ConnManager.DEFAULT, getDatasource(config),
                    config.getDatabaseType());
        }
        return datasourceHolder;
    }

    public static MetaManager getMetaManager() {
        if (metaManager == null) {
            metaManager = new MetaManager(MetaManagerConfig.defaultBuilder()
                    .tableDefinePackages(new String[] { "io.github.s3s3l.yggdrasil.orm.postgresql.test" })
                    .proxyDefinePackages(new String[] { "io.github.s3s3l.yggdrasil.orm.postgresql.test.proxy" })
                    .proxyConfigLocations(new String[] { "proxy" })
                    .build());
        }

        return metaManager;
    }

    public static RemoteMetaManager getRemoteMetaManager(DatasourceConfig config) {
        if (remoteMetaManager == null) {
            remoteMetaManager = new RemoteMetaManager(getDatasourceHolder(config));
        }

        return remoteMetaManager;
    }

    public static DataSource getDatasource(DatasourceConfig config) {
        if (dataSource == null) {
            dataSource = new DataSource(JacksonUtils.YAML
                    .toObject(FileUtils.getFirstExistResource(config.getConfigFile()), PoolProperties.class));
        }
        return dataSource;
    }
}
