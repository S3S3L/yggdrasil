package io.github.s3s3l.yggdrasil.boot.old.component.annotation;

import java.io.FileNotFoundException;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import io.github.s3s3l.yggdrasil.utils.common.StringUtils;

@Atom
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Configuration {

	/**
	 * 
	 * Resources location.
	 * 
	 * @return
	 * @since JDK 1.8
	 */
	String[] value() default {};

	/**
	 * 
	 * Alias for value.
	 * 
	 * @return
	 * @since JDK 1.8
	 */
	String[] resources() default {};

	/**
	 * 
	 * profile key
	 * 
	 * @return
	 * @since JDK 1.8
	 */
	String profileKey() default "profile";

	/**
	 * 
	 * profile
	 * 
	 * @return
	 * @since JDK 1.8
	 */
	String profile() default StringUtils.EMPTY_STRING;

	/**
	 * 
	 * Is check file exist. If true, will throws {@link FileNotFoundException}
	 * when file not found.
	 * 
	 * @return
	 * @since JDK 1.8
	 */
	boolean checkFile() default false;

	/**
	 * 
	 * If true, all resources will be combined to one. If false, will only load
	 * first exists resource.
	 * 
	 * @return
	 * @since JDK 1.8
	 */
	boolean combine() default false;
}
