package io.github.s3s3l.yggdrasil.orm.helper;

import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 
 * <p>
 * </p>
 * ClassName: ArrayHelper <br>
 * date: Sep 20, 2019 11:33:58 AM <br>
 * 
 * @author kehw_zwei
 * @version 1.0.0
 * @since JDK 1.8
 */
public abstract class ArrayHelper {

    public static String toArray(double[] array) {
        return array == null ? null
                : String.format("array[%s]", String.join(",", Arrays.stream(array)
                        .boxed()
                        .filter(Objects::nonNull)
                        .map(String::valueOf)
                        .collect(Collectors.toList())));
    }

    public static String toArray(int[] array) {
        return array == null ? null
                : String.format("array[%s]", String.join(",", Arrays.stream(array)
                        .boxed()
                        .filter(Objects::nonNull)
                        .map(String::valueOf)
                        .collect(Collectors.toList())));
    }

    public static String toArray(String[] array) {
        return array == null ? null : String.format("array['%s']", String.join("','", array));
    }

    public static String toArray(Object[] array) {
        return array == null ? null
                : String.format("array['%s']", String.join("','", Arrays.stream(array)
                        .filter(Objects::nonNull)
                        .map(Object::toString)
                        .collect(Collectors.toList())));
    }

    public static String toArray(Collection<?> list) {
        return list == null ? null
                : String.format("array['%s']", String.join("','", list.stream()
                        .filter(Objects::nonNull)
                        .map(Object::toString)
                        .collect(Collectors.toList())));
    }

}
