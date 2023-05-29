package io.github.s3s3l.yggdrasil.orm.handler;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import io.github.s3s3l.yggdrasil.orm.bind.annotation.Column;
import io.github.s3s3l.yggdrasil.orm.exception.DataMapException;
import io.github.s3s3l.yggdrasil.orm.meta.MetaManager;
import io.github.s3s3l.yggdrasil.utils.collection.CollectionUtils;
import io.github.s3s3l.yggdrasil.utils.reflect.PropertyDescriptorReflectionBean;
import io.github.s3s3l.yggdrasil.utils.reflect.ReflectionBean;
import io.github.s3s3l.yggdrasil.utils.reflect.ReflectionUtils;
import io.github.s3s3l.yggdrasil.utils.verify.Verify;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
public class StatementHelper {
    private final MetaManager metaManager;

    public void setParams(List<Object> params, PreparedStatement preparedStatement) throws SQLException {
        Verify.notNull(params);
        Verify.notNull(preparedStatement);
        for (int i = 1; i <= params.size(); i++) {
            Object param = params.get(i - 1);
            log.debug("param{}: {}", i, param);
            preparedStatement.setObject(i, param);
        }
    }

    public <T> List<T> mapResultTo(Class<T> resultType, ResultSet rs)
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
