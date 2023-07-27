package io.github.s3s3l.yggdrasil.orm.handler;

import java.lang.reflect.Type;

import io.github.s3s3l.yggdrasil.utils.stuctural.jackson.JacksonUtils;

public class EnumTypeHandler implements TypeHandler {

    @Override
    public Object toJDBCType(Object param) {
        if (!param.getClass()
                .isEnum()) {
            return param;
        }
        return JacksonUtils.JSON.toStructuralString(param);
    }

    @Override
    public Object toJavaType(Object result, Class<?> clazz, Type type) {
        if (!(result instanceof String)) {
            return result;
        }
        return JacksonUtils.JSON.toObject((String) result, clazz);
    }
}
