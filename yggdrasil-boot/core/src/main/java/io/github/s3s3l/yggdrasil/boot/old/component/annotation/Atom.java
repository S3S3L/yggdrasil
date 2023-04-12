package io.github.s3s3l.yggdrasil.boot.old.component.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import io.github.s3s3l.yggdrasil.utils.common.StringUtils;

/**
 * ClassName:Atom <br>
 * 
 * @author kehw_zwei
 * @version 1.0.0
 * @since JDK 1.8
 */
@Target({ ElementType.TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Atom {

	String value() default StringUtils.EMPTY_STRING;
}
