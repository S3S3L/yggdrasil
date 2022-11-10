package org.s3s3l.yggdrasil.orm.handler;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.Array;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 
 * <p>
 * </p>
 * ClassName: ArrayTypeHandler <br>
 * date: Sep 20, 2019 11:31:53 AM <br>
 * 
 * @author kehw_zwei
 * @version 1.0.0
 * @since JDK 1.8
 */
public class ArrayTypeHandler implements TypeHandler {

    @Override
    public Object toJDBCType(Object param) {
        if (param == null) {
            return null;
        }
        Class<?> type = param.getClass();
        if (type.isArray()) {
            if (param instanceof double[]) {
                return Arrays.stream((double[]) param)
                        .boxed()
                        .map(Double::valueOf)
                        .toArray();
            } else if (param instanceof int[]) {
                return Arrays.stream((int[]) param)
                        .boxed()
                        .map(Integer::valueOf)
                        .toArray();
            }
            return param;
        } else if (Collection.class.isAssignableFrom(type)) {
            return ((Collection<?>) param).stream()
                    .toArray();
        }
        return param;
    }

    @Override
    public Object toJavaType(Object result, Class<?> clazz, Type type) {
        try {
            if (clazz.isArray()) {
                if (double[].class.isAssignableFrom(clazz)) {
                    return Arrays.stream((Object[]) ((Array) result).getArray())
                            .mapToDouble(r -> Double.valueOf(r.toString()))
                            .toArray();
                } else if (int[].class.isAssignableFrom(clazz)) {
                    return Arrays.stream((Object[]) ((Array) result).getArray())
                            .mapToInt(r -> Integer.valueOf(r.toString()))
                            .toArray();
                }
                return ((Array) result).getArray();
            } else if (List.class.isAssignableFrom(clazz)) {
                Class<?> actualType = (Class<?>) ((ParameterizedType) type).getActualTypeArguments()[0];
                return Arrays.stream((Object[]) ((Array) result).getArray())
                        .map(actualType::cast)
                        .collect(Collectors.toList());
            } else if (Set.class.isAssignableFrom(clazz)) {
                Class<?> actualType = (Class<?>) ((ParameterizedType) type).getActualTypeArguments()[0];
                return Arrays.stream((Object[]) ((Array) result).getArray())
                        .map(actualType::cast)
                        .collect(Collectors.toSet());
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

}
