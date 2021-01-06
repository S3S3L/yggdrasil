package org.s3s3l.yggdrasil.annotation;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * <p>
 * </p>
 * ClassName:From <br>
 * Date: Jan 19, 2018 1:59:34 PM <br>
 * 
 * @author kehw_zwei
 * @version 1.0.0
 * @since JDK 1.8
 */
@Documented
@Retention(RUNTIME)
@Target(FIELD)
public @interface FromObject {

    String value();
}
