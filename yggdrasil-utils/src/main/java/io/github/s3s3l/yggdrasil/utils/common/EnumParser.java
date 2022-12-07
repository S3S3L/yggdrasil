package io.github.s3s3l.yggdrasil.utils.common;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

import io.github.s3s3l.yggdrasil.utils.reflect.exception.ReflectException;

/**
 * <p>
 * </p>
 * ClassName:EnumParser <br>
 * Date: Nov 22, 2018 11:05:13 AM <br>
 * 
 * @author kehw_zwei
 * @version 1.0.0
 * @since JDK 1.8
 */
public abstract class EnumParser {

    public static <T extends Enum<T>> T parse(String name, Class<T> type) {
        T[] values = getEnumValues(type);
        return Arrays.stream(values)
                .filter(r -> r.name()
                        .equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }

    @SuppressWarnings("unchecked")
    private static <E extends Enum<E>> E[] getEnumValues(Class<E> enumClass) {
        try {
            Method method = enumClass.getDeclaredMethod("values");
            Object o = method.invoke(null);
            return (E[]) o;
        } catch (SecurityException | IllegalArgumentException | IllegalAccessException | NoSuchMethodException
                | InvocationTargetException e) {
            throw new ReflectException(e);
        }
    }
}
