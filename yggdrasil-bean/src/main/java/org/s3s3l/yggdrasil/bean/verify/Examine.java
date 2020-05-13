package org.s3s3l.yggdrasil.bean.verify;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * <p>
 * </p>
 * ClassName:Examine <br>
 * Date: Apr 7, 2017 4:15:24 PM <br>
 * 
 * @author kehw_zwei
 * @version 1.0.0
 * @since JDK 1.8
 */
@Inherited
@Documented
@Retention(RUNTIME)
@Target({ TYPE, FIELD, METHOD })
@Repeatable(Examines.class)
public @interface Examine {

    /**
     * 
     * only effect in {@code FIELD}
     * 
     * @since JDK 1.8
     */
    Expectation value() default Expectation.NOT_NULL;

    /**
     * 
     * specify effective scope
     * 
     * @return
     * @since JDK 1.8
     */
    String scope() default "";

    /**
     * 
     * specify effective scopes
     * 
     * @return
     * @since JDK 1.8
     */
    String[] scopes() default {};

    /**
     * 
     * only effect in {@code FIELD}
     * 
     * @since JDK 1.8
     */
    long length() default 0L;

    /**
     * 
     * only effect in {@code FIELD}
     * 
     * @since JDK 1.8
     */
    Class<?> type() default Object.class;

    /**
     * 
     * only effect in {@code FIELD}
     * 
     * @since JDK 1.8
     */
    String msg() default "";

    /**
     * 
     * both effect in {@code FIELD} and {@code TYPE}
     * 
     * @since JDK 1.8
     */
    boolean ignoreNullValue() default false;

    /**
     * 
     * tells if this {@code Examine} is for {@code Object}s in
     * {@code Collection}s
     * 
     * @return
     * @since JDK 1.8
     */
    boolean withinTheCollection() default false;

    /**
     * 数字大小比较的标准值
     * 
     * @return
     */
    int standard() default 0;

    /**
     * 正则匹配的正则表达式
     * 
     * @return
     */
    String regex() default "";
}
