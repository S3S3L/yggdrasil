package io.github.s3s3l.yggdrasil.test.base;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.METHOD, 
        ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Timeout {
    
    /**
     * timeout of test in ms.
     * @return
     */
    long value() default 0;
}
