package io.github.s3s3l.yggdrasil.orm.handler;

import java.lang.reflect.Type;

/**
 * 
 * <p>
 * </p>
 * ClassName: DefaultTypeHandler <br>
 * date: Sep 20, 2019 11:33:42 AM <br>
 * 
 * @author kehw_zwei
 * @version 1.0.0
 * @since JDK 1.8
 */
public class DefaultTypeHandler implements TypeHandler {

    @Override
    public Object toJDBCType(Object param) {
        return param;
    }

    @Override
    public Object toJavaType(Object result, Class<?> clazz, Type type) {
        return result;
    }

}
