package io.github.s3s3l.yggdrasil.boot.old.component.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import io.github.s3s3l.yggdrasil.boot.old.interceptor.DefaultInvocationHandler;
import io.github.s3s3l.yggdrasil.boot.old.interceptor.base.InterceptorHandlerBase;
import io.github.s3s3l.yggdrasil.boot.old.interceptor.base.InvocationHandlerBase;
import io.github.s3s3l.yggdrasil.utils.common.StringUtils;


/**
 * ClassName:InvocationHandler <br>
 * Date: 2016年5月6日 下午5:33:55 <br>
 * 
 * @author kehw_zwei
 * @version 1.0.0
 * @since JDK 1.8
 */
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface Invocation {

	Class<? extends InvocationHandlerBase> value() default DefaultInvocationHandler.class;

	Class<? extends InterceptorHandlerBase>[] interceptors() default {};

	String name() default StringUtils.EMPTY_STRING;
}
