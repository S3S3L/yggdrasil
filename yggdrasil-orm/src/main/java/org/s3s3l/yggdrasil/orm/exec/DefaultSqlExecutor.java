package org.s3s3l.yggdrasil.orm.exec;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.sql.DataSource;

import org.s3s3l.yggdrasil.orm.bind.annotation.Column;
import org.s3s3l.yggdrasil.orm.bind.express.DataBindExpress;
import org.s3s3l.yggdrasil.orm.bind.express.jsqlparser.JSqlParserDataBindExpress;
import org.s3s3l.yggdrasil.orm.bind.sql.SqlStruct;
import org.s3s3l.yggdrasil.orm.exception.DataMapException;
import org.s3s3l.yggdrasil.orm.exception.SqlExecutingException;
import org.s3s3l.yggdrasil.orm.meta.MetaManager;
import org.s3s3l.yggdrasil.utils.collection.CollectionUtils;
import org.s3s3l.yggdrasil.utils.reflect.PropertyDescriptorReflectionBean;
import org.s3s3l.yggdrasil.utils.reflect.ReflectionBean;
import org.s3s3l.yggdrasil.utils.reflect.ReflectionUtils;
import org.s3s3l.yggdrasil.utils.verify.Verify;

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

    private final MetaManager metaManager;
    private final DataBindExpress dataBindExpress;
    private DataSource datasource;

    public DefaultSqlExecutor(DataSource datasource, MetaManager metaManager) {
        this(datasource, new JSqlParserDataBindExpress(metaManager), metaManager);
    }

    public DefaultSqlExecutor(DataSource datasource, DataBindExpress dataBindExpress, MetaManager metaManager) {
        this.datasource = datasource;
        this.metaManager = metaManager;
        this.dataBindExpress = dataBindExpress;
    }

    @Override
    public void setDataSource(DataSource datasource) {
        this.datasource = datasource;
    }

    @Override
    public <S> int insert(List<S> sources) {
        Verify.notEmpty(sources);

        SqlStruct sqlStruct = dataBindExpress.getInsert(sources);
        String sql = sqlStruct.getSql();
        log.debug("Excuting sql [{}].", sql);
        try (Connection conn = datasource.getConnection()) {
            try (PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
                setParams(sqlStruct, preparedStatement);

                return preparedStatement.executeUpdate();
            }
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
        try (Connection conn = datasource.getConnection()) {
            try (PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
                setParams(sqlStruct, preparedStatement);

                return preparedStatement.executeUpdate();
            }
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
        try (Connection conn = datasource.getConnection()) {
            try (PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
                setParams(sqlStruct, preparedStatement);

                return preparedStatement.executeUpdate();
            }
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
        try (Connection conn = datasource.getConnection()) {
            try (PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
                setParams(sqlStruct, preparedStatement);

                ResultSet rs = preparedStatement.executeQuery();

                return mapResultTo(resultType, rs);
            }
        } catch (SQLException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
            throw new SqlExecutingException(e);
        }
    }

    @Override
    public boolean create(Class<?> tableType, boolean force) {
        Verify.notNull(tableType);
        SqlStruct sqlStruct = dataBindExpress.getCreate(tableType, force);
        String sql = sqlStruct.getSql();
        log.debug("Excuting sql [{}].", sql);
        try (Connection conn = datasource.getConnection()) {
            try (Statement preparedStatement = conn.createStatement()) {
                return preparedStatement.execute(sql);
            }
        } catch (SQLException | SecurityException e) {
            throw new SqlExecutingException(e);
        }
    }

    @Override
    public boolean execute(String sql) {
        log.debug("Excuting sql [{}].", sql);
        try (Connection conn = datasource.getConnection()) {
            try (Statement preparedStatement = conn.createStatement()) {
                return preparedStatement.execute(sql);
            }
        } catch (SQLException | SecurityException e) {
            throw new SqlExecutingException(e);
        }
    }

    private void setParams(SqlStruct sqlStruct, PreparedStatement preparedStatement) throws SQLException {
        Verify.notNull(sqlStruct);
        Verify.notNull(preparedStatement);
        for (int i = 1; i <= sqlStruct.getParams()
                .size(); i++) {
            Object param = sqlStruct.getParams()
                    .get(i - 1);
            log.debug("param{}: {}", i, param);
            preparedStatement.setObject(i, param);
        }
    }

    private <T> List<T> mapResultTo(Class<T> resultType, ResultSet rs)
            throws SQLException, InvocationTargetException, NoSuchMethodException, SecurityException {
        Verify.notNull(resultType);
        Verify.notNull(rs);

        List<T> resultList = new ArrayList<>();
        ResultSetMetaData metaData = rs.getMetaData();
        Set<Field> fields = ReflectionUtils.getFields(resultType);

        try {
            while (rs.next()) {
                T result = resultType.getConstructor().newInstance();
                ReflectionBean reflection = new PropertyDescriptorReflectionBean(result);

                for (int i = 1; i <= metaData.getColumnCount(); i++) {
                    String columnLabel = metaData.getColumnLabel(i);
                    String fieldName = metaManager.getAlias(resultType, metaData.getColumnName(i));

                    Field field = CollectionUtils.getFirst(fields, r -> r.getName()
                            .equalsIgnoreCase(fieldName));
                    Object resultData = rs.getObject(columnLabel);

                    if (field.isAnnotationPresent(Column.class)) {
                        Type fieldType = field.getGenericType();
                        Class<?> fieldClass = field.getType();

                        resultData = metaManager.getTypeHandlerManager().getOrNew(field.getAnnotation(Column.class)
                                .typeHandler())
                                .toJavaType(resultData, fieldClass, fieldType);
                    }

                    reflection.setFieldValue(field.getName(), resultData);
                }

                resultList.add(result);
            }
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException e) {
            throw new DataMapException(e);
        }

        return resultList;
    }

}
