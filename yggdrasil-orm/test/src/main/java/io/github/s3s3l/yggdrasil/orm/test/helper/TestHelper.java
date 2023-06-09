package io.github.s3s3l.yggdrasil.orm.test.helper;

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
import io.github.s3s3l.yggdrasil.orm.test.config.DatasourceConfig;
import io.github.s3s3l.yggdrasil.orm.wrapper.SqlObjectWrapper;
import io.github.s3s3l.yggdrasil.utils.common.FreeMarkerHelper;
import io.github.s3s3l.yggdrasil.utils.file.FileUtils;
import io.github.s3s3l.yggdrasil.utils.stuctural.jackson.JacksonUtils;

public class TestHelper {

    public final DatasourceConfig config;

    public TestHelper(DatasourceConfig config) {
        this.config = config;
    }

    private DataSource dataSource;
    private MetaManager metaManager;
    private RemoteMetaManager remoteMetaManager;
    private DefaultDatasourceHolder datasourceHolder;
    private SqlExecutor sqlExecutor;

    public SqlExecutor getSqlExecutor() {

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

    public DefaultDatasourceHolder getDatasourceHolder(DatasourceConfig config) {
        if (datasourceHolder == null) {
            datasourceHolder = new DefaultDatasourceHolder(ConnManager.DEFAULT, getDatasource(config),
                    config.getDatabaseType());
        }
        return datasourceHolder;
    }

    public MetaManager getMetaManager() {
        if (metaManager == null) {
            metaManager = new MetaManager(MetaManagerConfig.defaultBuilder()
                    .tableDefinePackages(config.getTableDefinePackages())
                    .proxyDefinePackages(new String[] { "io.github.s3s3l.yggdrasil.orm.test.proxy" })
                    .proxyConfigLocations(new String[] { "proxy" })
                    .build());
        }

        return metaManager;
    }

    public RemoteMetaManager getRemoteMetaManager() {
        if (remoteMetaManager == null) {
            remoteMetaManager = new RemoteMetaManager(getDatasourceHolder(config));
        }

        return remoteMetaManager;
    }

    public DataSource getDatasource(DatasourceConfig config) {
        if (dataSource == null) {
            PoolProperties props = JacksonUtils.YAML.toObject(FileUtils.getFirstExistResource(config.getConfigFile()),
                    PoolProperties.class);
            dataSource = new DataSource(props);
        }
        return dataSource;
    }
}
