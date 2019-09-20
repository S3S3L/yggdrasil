package org.s3s3l.yggdrasil.orm.bind.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.s3s3l.yggdrasil.orm.enumerations.ComparePattern;

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

    boolean forSelect() default true;

    boolean forUpdate() default false;

    boolean forDelete() default false;

    ComparePattern pattern() default ComparePattern.EQUAL;
}
