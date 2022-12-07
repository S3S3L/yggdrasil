package io.github.s3s3l.yggdrasil.utils.common;

/**
 * <p>
 * </p>
 * ClassName:Named <br>
 * Date: Apr 15, 2019 1:54:34 PM <br>
 * 
 * @author kehw_zwei
 * @version 1.0.0
 * @since JDK 1.8
 */
public interface Named {

    default String getName() {
        return getClass().getName();
    }
}
