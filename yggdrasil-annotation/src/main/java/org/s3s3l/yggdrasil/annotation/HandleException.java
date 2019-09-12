package org.s3s3l.yggdrasil.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * ClassName:HandleException <br>
 * Date: 2016年4月25日 下午5:45:48 <br>
 * 
 * @author kehw_zwei
 * @version 1.0.0
 * @since JDK 1.8
 */
@Target({ ElementType.TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface HandleException {

	/**
	 * 
	 * Exceptions need to be handling. <br>
	 * If this is empty. All exceptions will be handling.
	 * 
	 * @author kehw_zwei
	 * @return
	 * @since JDK 1.8
	 */
	Class<? extends Throwable>[] value() default {};

	/**
	 * 
	 * Exceptions will not throw after handled.
	 * 
	 * @author kehw_zwei
	 * @return
	 * @since JDK 1.8
	 */
	Class<? extends Throwable>[] keepExceptions() default {};

	/**
	 * 
	 * Throw exception after handle it.
	 * 
	 * @author kehw_zwei
	 * @return
	 * @since JDK 1.8
	 */
	boolean continueThrow() default false;
}
