package org.s3s3l.yggdrasil.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * ClassName:QueryLog <br>
 * Date: 2016年4月26日 下午2:46:36 <br>
 * 
 * @author kehw_zwei
 * @version 1.0.0
 * @since JDK 1.8
 */
@Target({ ElementType.TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface QueryLog {
	
	/**
	 * 
	 * Is enable query log.
	 * 
	 * @author kehw_zwei
	 * @return 
	 * @since JDK 1.8
	 */
	boolean value() default true;

	/**
	 * 
	 * Logger name.
	 * 
	 * @author kehw_zwei
	 * @return 
	 * @since JDK 1.8
	 */
	String logger() default "request";

	/**
	 * 
	 * Write request log.
	 * 
	 * @author kehw_zwei
	 * @return 
	 * @since JDK 1.8
	 */
	boolean isLogRequest() default true;

	/**
	 * 
	 * Write response log.
	 * 
	 * @author kehw_zwei
	 * @return 
	 * @since JDK 1.8
	 */
	boolean isLogResponse() default true;
	
	/**
	 * 
	 * Write request parameters.
	 * 
	 * @author kehw_zwei
	 * @return 
	 * @since JDK 1.8
	 */
	boolean isLogParameters() default true;
}
