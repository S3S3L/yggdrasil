package org.s3s3l.yggdrasil.orm.bind.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.s3s3l.yggdrasil.orm.handler.DefaultTypeHandler;
import org.s3s3l.yggdrasil.orm.handler.TypeHandler;
import org.s3s3l.yggdrasil.orm.validator.DefaultValidator;
import org.s3s3l.yggdrasil.orm.validator.Validator;

/**
 * 
 * <p>
 * </p>
 * ClassName: Column <br>
 * date: Sep 20, 2019 11:26:52 AM <br>
 * 
 * @author kehw_zwei
 * @version 1.0.0
 * @since JDK 1.8
 */
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Column {

    String name() default "";

    Class<? extends Validator> validator() default DefaultValidator.class;

    DatabaseType dbType();

    Class<? extends TypeHandler> typeHandler() default DefaultTypeHandler.class;
}
