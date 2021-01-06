package org.s3s3l.yggdrasil.utils.common;

import java.util.Objects;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * <p>
 * </p>
 * ClassName:OptionalUtils <br>
 * Date: Oct 12, 2018 7:32:49 PM <br>
 * 
 * @author kehw_zwei
 * @version 1.0.0
 * @since JDK 1.8
 */
public abstract class OptionalUtils {

    @SafeVarargs
    public static <T> T firstAvailable(Supplier<T>... suppliers) {
        return firstAvailable(Objects::nonNull, suppliers);
    }

    @SafeVarargs
    public static <T> T firstAvailable(Predicate<T> predicate, Supplier<T>... suppliers) {
        for (Supplier<T> supplier : suppliers) {
            T data = supplier.get();
            if (predicate.test(data)) {
                return data;
            }
        }

        return null;
    }
}
