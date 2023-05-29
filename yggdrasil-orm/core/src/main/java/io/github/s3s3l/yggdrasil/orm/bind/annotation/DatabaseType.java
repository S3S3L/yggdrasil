package io.github.s3s3l.yggdrasil.orm.bind.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.sql.JDBCType;

@Target({ ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DatabaseType {
    JDBCType type();

    boolean array() default false;

    String[] args() default {};

    boolean primary() default false;

    boolean notNull() default false;

    boolean def() default false;

    String defValue() default "";
}
