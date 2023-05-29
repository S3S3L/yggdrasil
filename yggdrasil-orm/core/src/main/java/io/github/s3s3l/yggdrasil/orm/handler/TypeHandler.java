package io.github.s3s3l.yggdrasil.orm.handler;

import java.lang.reflect.Type;

/**
 * 
 * <p>
 * </p>
 * ClassName: TypeHandler <br>
 * date: Sep 20, 2019 11:33:50 AM <br>
 * 
 * @author kehw_zwei
 * @version 1.0.0
 * @since JDK 1.8
 */
public interface TypeHandler {

    Object toJDBCType(Object param);

    Object toJavaType(Object result, Class<?> clazz, Type type);
}
