package org.s3s3l.yggdrasil.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.RetentionPolicy;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * <p>
 * </p>
 * ClassName:Cache <br>
 * Date: Sep 22, 2017 7:54:35 PM <br>
 * 
 * @author kehw_zwei
 * @version 1.0.0
 * @since JDK 1.8
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD })
public @interface Cache {

    String value() default "";
    
    /**
     * 
     * alias for value
     * 
     * @return 
     * @since JDK 1.8
     */
    String scope() default "";
    
    long expire() default 0;
}
