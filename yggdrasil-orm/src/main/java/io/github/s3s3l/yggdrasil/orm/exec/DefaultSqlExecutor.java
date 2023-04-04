package io.github.s3s3l.yggdrasil.orm.exec;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Proxy;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import io.github.s3s3l.yggdrasil.orm.bind.express.DataBindExpress;
import io.github.s3s3l.yggdrasil.orm.bind.sql.SqlStruct;
import io.github.s3s3l.yggdrasil.orm.ds.DatasourceHolder;
import io.github.s3s3l.yggdrasil.orm.exception.ProxyGenerateException;
import io.github.s3s3l.yggdrasil.orm.exception.SqlExecutingException;
import io.github.s3s3l.yggdrasil.orm.handler.StatementHelper;
import io.github.s3s3l.yggdrasil.orm.meta.MetaManager;
import io.github.s3s3l.yggdrasil.orm.pagin.ConditionForPagination;
import io.github.s3s3l.yggdrasil.orm.pagin.PaginResult;
import io.github.s3s3l.yggdrasil.orm.proxy.ExecutorProxyInvocationHandler;
import io.github.s3s3l.yggdrasil.orm.proxy.meta.ProxyMeta;
import io.github.s3s3l.yggdrasil.utils.collection.CollectionUtils;
import io.github.s3s3l.yggdrasil.utils.common.FreeMarkerHelper;
import io.github.s3s3l.yggdrasil.utils.verify.Verify;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;

/**
 * 
 * <p>
 * </p>
 * ClassName: DefaultSqlExecutor <br>
 * date: Sep 20, 2019 11:31:28 AM <br>
 * 
 * @author kehw_zwei
 * @version 1.0.0
 * @since JDK 1.8
 */
@Slf4j
public class DefaultSqlExecutor implements SqlExecutor {

    private final StatementHelper statementHelper;
    private final MetaManager metaManager;
    private final DataBindExpress dataBindExpress;
    private final FreeMarkerHelper freeMarkerHelper;
    private final DatasourceHolder datasourceHolder;

    @Builder
    public DefaultSqlExecutor(MetaManager metaManager, DataBindExpress dataBindExpress,
            FreeMarkerHelper freeMarkerHelper, DatasourceHolder datasourceHolder) {
        this.statementHelper = new StatementHelper(metaManager);
        this.metaManager = metaManager;
        this.dataBindExpress = dataBindExpress;
        this.freeMarkerHelper = freeMarkerHelper;
        this.datasourceHolder = datasourceHolder;
    }

    @Override
    public <S> int insert(List<S> sources) {
        Verify.notEmpty(sources);

        SqlStruct sqlStruct = dataBindExpress.getInsert(sources);
        String sql = sqlStruct.getSql();
        log.debug("Excuting sql [{}].", sql);
        try {
            return datasourceHolder.useConn(conn -> {
                try (PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
                    statementHelper.setParams(sqlStruct.getParams(), preparedStatement);

                    return preparedStatement.executeUpdate();
                }
            });
        } catch (SQLException e) {
            throw new SqlExecutingException(e);
        }
    }

    @Override
    public <C> int delete(C condition) {
        Verify.notNull(condition);

        SqlStruct sqlStruct = dataBindExpress.getDelete(condition);
        String sql = sqlStruct.getSql();
        log.debug("Excuting sql [{}].", sql);
        try {
            return datasourceHolder.useConn(conn -> {
                try (PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
                    statementHelper.setParams(sqlStruct.getParams(), preparedStatement);

                    return preparedStatement.executeUpdate();
                }
            });
        } catch (SQLException e) {
            throw new SqlExecutingException(e);
        }
    }

    @Override
    public <S, C> int update(S source, C condition) {
        Verify.notNull(source);
        Verify.notNull(condition);

        SqlStruct sqlStruct = dataBindExpress.getUpdate(source, condition);
        String sql = sqlStruct.getSql();
        log.debug("Excuting sql [{}].", sql);
        try {
            return datasourceHolder.useConn(conn -> {
                try (PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
                    statementHelper.setParams(sqlStruct.getParams(), preparedStatement);

                    return preparedStatement.executeUpdate();
                }
            });
        } catch (SQLException e) {
            throw new SqlExecutingException(e);
        }
    }

    @Override
    public <C, R> List<R> select(C condition, Class<R> resultType) {
        Verify.notNull(condition);
        Verify.notNull(resultType);

        SqlStruct sqlStruct = dataBindExpress.getSelect(condition);
        String sql = sqlStruct.getSql();
        log.debug("Excuting sql [{}].", sql);
        try {
            return datasourceHolder.useConn(conn -> {
                try (PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
                    statementHelper.setParams(sqlStruct.getParams(), preparedStatement);

                    ResultSet rs = preparedStatement.executeQuery();

                    return statementHelper.mapResultTo(resultType, rs);
                } catch (InvocationTargetException | NoSuchMethodException e) {
                    throw new SqlExecutingException(e);
                }
            });
        } catch (SQLException | SecurityException e) {
            throw new SqlExecutingException(e);
        }
    }

    @Override
    public <C extends ConditionForPagination, R> PaginResult<List<R>> selectByPagin(C condition, Class<R> resultType) {
        Verify.notNull(condition);
        Verify.notNull(resultType);

        PaginResult<List<R>> result = new PaginResult<>();
        condition.prepare();

        try {
            return datasourceHolder.useConn(conn -> {
                SqlStruct countSqlStruct = dataBindExpress.getSelectCount(condition);
                String countSql = countSqlStruct.getSql();
                log.debug("Excuting countSql [{}].", countSql);
                try (PreparedStatement preparedStatement = conn
                        .prepareStatement(countSql)) {
                    statementHelper.setParams(countSqlStruct.getParams(), preparedStatement);

                    ResultSet rs = preparedStatement.executeQuery();
                    if (!rs.next()) {
                        throw new SqlExecutingException("Count sql execute error.");
                    }

                    long count = rs.getLong(1);
                    log.debug("Count [{}].", count);
                    result.setRecordsCount(count);
                    result.setPageCount(-Math.floorDiv(-count, condition.getPageSize()));
                }
                SqlStruct sqlStruct = dataBindExpress.getSelect(condition);
                String sql = sqlStruct.getSql();
                log.debug("Excuting sql [{}].", sql);
                try (PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
                    statementHelper.setParams(sqlStruct.getParams(), preparedStatement);

                    ResultSet rs = preparedStatement.executeQuery();

                    List<R> resultData = statementHelper.mapResultTo(resultType, rs);

                    result.setData(resultData);

                    return result;
                } catch (InvocationTargetException | NoSuchMethodException e) {
                    throw new SqlExecutingException(e);
                }
            });
        } catch (SQLException | SecurityException e) {
            throw new SqlExecutingException(e);
        }
    }

    @Override
    public <C, R> R selectOne(C condition, Class<R> resultType) {
        return CollectionUtils.getFirst(select(condition, resultType));
    }

    @Override
    public boolean create(Class<?> tableType, CreateConfig config) {
        Verify.notNull(tableType);
        if (config.isDropFirst()) {
            drop(tableType);
        }
        SqlStruct sqlStruct = dataBindExpress.getCreate(tableType, config.isForce());
        String sql = sqlStruct.getSql();
        log.debug("Excuting sql [{}].", sql);
        try {
            return datasourceHolder.useConn(conn -> {
                try (Statement preparedStatement = conn.createStatement()) {
                    return preparedStatement.execute(sql);
                }
            });
        } catch (SQLException | SecurityException e) {
            throw new SqlExecutingException(e);
        }
    }

    @Override
    public boolean drop(Class<?> tableType) {
        Verify.notNull(tableType);
        SqlStruct sqlStruct = dataBindExpress.getDrop(tableType);
        String sql = sqlStruct.getSql();
        log.debug("Excuting sql [{}].", sql);
        try {
            return datasourceHolder.useConn(conn -> {
                try (Statement preparedStatement = conn.createStatement()) {
                    return preparedStatement.execute(sql);
                }
            });
        } catch (SQLException | SecurityException e) {
            throw new SqlExecutingException(e);
        }
    }

    @Override
    public boolean execute(String sql) {
        log.debug("Excuting sql [{}].", sql);
        try {
            return datasourceHolder.useConn(conn -> {
                try (Statement preparedStatement = conn.createStatement()) {
                    return preparedStatement.execute(sql);
                }
            });
        } catch (SQLException | SecurityException e) {
            throw new SqlExecutingException(e);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public <P> P getProxy(Class<P> proxyInterfaceType) {
        Verify.notNull(proxyInterfaceType);

        ProxyMeta proxyMeta = metaManager.getProxyMeta(proxyInterfaceType);
        if (proxyMeta == null) {
            throw new ProxyGenerateException("Proxy meta not found. " + proxyInterfaceType.getName());
        }
        return (P) Proxy.newProxyInstance(proxyMeta.getIface().getClassLoader(),
                new Class<?>[] { proxyMeta.getIface() },
                ExecutorProxyInvocationHandler.builder()
                        .proxyMeta(proxyMeta)
                        .freeMarkerHelper(freeMarkerHelper)
                        .datasourceHolder(datasourceHolder)
                        .statementHelper(statementHelper)
                        .build());
    }

    @Override
    public void transactional() throws SQLException {
        datasourceHolder.transactional();
    }

    @Override
    public void transactionalCommit() throws SQLException {
        datasourceHolder.transactionalCommit();
    }

    @Override
    public void rollback() throws SQLException {
        datasourceHolder.rollback();
    }

}
