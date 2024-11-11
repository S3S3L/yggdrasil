package io.github.s3s3l.yggdrasil.orm.handler;

import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LocalDateTimeHandler implements TypeHandler {

    @Override
    public Object toJDBCType(Object param) {

        if (!LocalDateTime.class.isAssignableFrom(param.getClass())) {
            return param;
        }
        return ((LocalDateTime) param).format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    }

    @Override
    public Object toJavaType(Object result, Class<?> clazz, Type type) {
        if (!(result instanceof String)) {
            return result;
        }
        return LocalDateTime.parse((String) result, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    }

}
