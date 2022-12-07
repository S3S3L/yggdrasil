package io.github.s3s3l.yggdrasil.bean.verify;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * <p>
 * </p>
 * ClassName:Examines <br>
 * Date: Apr 27, 2017 7:06:06 PM <br>
 * 
 * @author kehw_zwei
 * @version 1.0.0
 * @since JDK 1.8
 */
@Inherited
@Documented
@Retention(RUNTIME)
@Target({ TYPE, FIELD })
public @interface Examines {
    Examine[] value();
}
