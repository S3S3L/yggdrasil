package org.s3s3l.yggdrasil.orm.exec;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.sql.DataSource;

import org.s3s3l.yggdrasil.orm.bind.annotation.Column;
import org.s3s3l.yggdrasil.orm.bind.express.DataBindExpress;
import org.s3s3l.yggdrasil.orm.bind.express.ExpressFactory;
import org.s3s3l.yggdrasil.orm.bind.express.common.DefaultExpressFactory;
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
    private ExpressFactory expressFactory = new DefaultExpressFactory();
    private DataSource datasource;

    public DefaultSqlExecutor(DataSource datasource, MetaManager metaManager) {
        this.datasource = datasource;
        this.metaManager = metaManager;
    }

    public DefaultSqlExecutor(DataSource datasource, ExpressFactory expressFactory, MetaManager metaManager) {
        this.datasource = datasource;
        this.expressFactory = expressFactory;
        this.metaManager = metaManager;
    }

    @Override
    public void setDataSource(DataSource datasource) {
        this.datasource = datasource;
    }

    @Override
    public <T> int insert(List<T> model, Class<T> modelClass) {
        Verify.notEmpty(model);
        Verify.notNull(modelClass);

        DataBindExpress express = this.expressFactory.getDataBindExpress(modelClass, metaManager);
        SqlStruct sqlStruct = express.getInsert(model);
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
    public <T> int delete(T model, Class<T> modelClass) {
        Verify.notEmpty(model);
        Verify.notNull(modelClass);

        DataBindExpress express = this.expressFactory.getDataBindExpress(modelClass, metaManager);
        SqlStruct sqlStruct = express.getDelete(model);
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
    public <T> int update(T model, Class<T> modelClass) {
        Verify.notNull(model);
        Verify.notNull(modelClass);

        DataBindExpress express = this.expressFactory.getDataBindExpress(modelClass, metaManager);
        SqlStruct sqlStruct = express.getUpdate(model);
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
    public <T> List<T> select(T model, Class<T> modelClass) {
        Verify.notNull(model);
        Verify.notNull(modelClass);

        DataBindExpress express = this.expressFactory.getDataBindExpress(modelClass, metaManager);
        SqlStruct sqlStruct = express.getSelect(model);
        String sql = sqlStruct.getSql();
        log.debug("Excuting sql [{}].", sql);
        try (Connection conn = datasource.getConnection()) {
            try (PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
                setParams(sqlStruct, preparedStatement);

                ResultSet rs = preparedStatement.executeQuery();

                return mapResultTo(modelClass, rs);
            }
        } catch (SQLException e) {
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

    private <T> List<T> mapResultTo(Class<T> resultType, ResultSet rs) throws SQLException {
        Verify.notNull(resultType);
        Verify.notNull(rs);

        DataBindExpress express = this.expressFactory.getDataBindExpress(resultType, metaManager);
        List<T> resultList = new ArrayList<>();
        ResultSetMetaData metaData = rs.getMetaData();
        Set<Field> fields = ReflectionUtils.getFields(resultType);

        try {
            while (rs.next()) {
                T result = resultType.newInstance();
                ReflectionBean reflection = new PropertyDescriptorReflectionBean(result);

                for (int i = 1; i <= metaData.getColumnCount(); i++) {
                    String columnLabel = metaData.getColumnLabel(i);
                    String fieldName = express.getAlias(metaData.getColumnName(i));

                    Field field = CollectionUtils.getFirst(fields, r -> r.getName()
                            .equalsIgnoreCase(fieldName));
                    Object resultData = rs.getObject(columnLabel);

                    if (field.isAnnotationPresent(Column.class)) {
                        Type fieldType = field.getGenericType();
                        Class<?> fieldClass = field.getType();

                        resultData = field.getAnnotation(Column.class)
                                .typeHandler()
                                .newInstance()
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
