package io.github.s3s3l.yggdrasil.document.excel;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import io.github.s3s3l.yggdrasil.utils.common.StringUtils;

/**
 * <p>
 * </p>
 * ClassName:FieldMap <br>
 * Date: Nov 15, 2016 6:45:28 PM <br>
 * 
 * @author kehw_zwei
 * @version 1.0.0
 * @since JDK 1.8
 */
@Retention(RUNTIME)
@Target(FIELD)
@Inherited
public @interface FieldMap {

    String value() default StringUtils.EMPTY_STRING;

    int order();

    String prefix() default StringUtils.EMPTY_STRING;

    String suffix() default StringUtils.EMPTY_STRING;
}
