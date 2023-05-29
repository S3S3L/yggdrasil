package io.github.s3s3l.yggdrasil.orm.bind.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import io.github.s3s3l.yggdrasil.orm.enumerations.ComparePattern;
import io.github.s3s3l.yggdrasil.orm.validator.DefaultValidator;
import io.github.s3s3l.yggdrasil.orm.validator.Validator;

/**
 * 
 * <p>
 * </p>
 * ClassName: Condition <br>
 * date: Sep 20, 2019 11:27:02 AM <br>
 * 
 * @author kehw_zwei
 * @version 1.0.0
 * @since JDK 1.8
 */
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Condition {

    String column() default "";

    boolean forSelect() default true;

    boolean forUpdate() default false;

    boolean forDelete() default false;

    Class<? extends Validator> validator() default DefaultValidator.class;

    ComparePattern pattern() default ComparePattern.EQUAL;
}
