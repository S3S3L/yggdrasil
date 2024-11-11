package io.github.s3s3l.yggdrasil.orm.handler;

import java.lang.reflect.Type;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class LocalTimeHandler implements TypeHandler {

    @Override
    public Object toJDBCType(Object param) {

        if (!LocalTime.class.isAssignableFrom(param.getClass())) {
            return param;
        }
        return ((LocalTime) param).format(DateTimeFormatter.ISO_LOCAL_TIME);
    }

    @Override
    public Object toJavaType(Object result, Class<?> clazz, Type type) {
        if (!(result instanceof String)) {
            return result;
        }
        return LocalTime.parse((String) result, DateTimeFormatter.ISO_LOCAL_TIME);
    }

}
