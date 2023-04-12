package io.github.s3s3l.yggdrasil.boot.old.component.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import io.github.s3s3l.yggdrasil.utils.common.StringUtils;

/**
 * ClassName:AutoInjected <br>
 * Date: 2016年5月4日 下午5:40:46 <br>
 * 
 * @author kehw_zwei
 * @version 1.0.0
 * @since JDK 1.8
 */
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AutoInjected {

	String value() default StringUtils.EMPTY_STRING;
}
