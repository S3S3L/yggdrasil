package io.github.s3s3l.yggdrasil.boot.old.component.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import io.github.s3s3l.yggdrasil.utils.common.StringUtils;

@Target({ ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Substantiality {

	String value() default StringUtils.EMPTY_STRING;
}
