package org.s3s3l.yggdrasil.annotation;

import java.lang.annotation.Annotation;
import java.lang.annotation.Inherited;

/**
 * ClassName:AnnotationHelper <br>
 * Date: 2016年4月27日 下午4:01:48 <br>
 * 
 * @author kehw_zwei
 * @version 1.0.0
 * @since JDK 1.8
 */
public class AnnotationHelper {

	/**
	 * 
	 * 获取{@link Class}上的指定注解，会扫描实现的接口上带@{@link Inherited}注解的注解
	 * 
	 * @param cls
	 *            目标类
	 * @param annotationCls
	 *            注解类
	 * @return 如果找到了，返回注解对象;如果没找到，返回null
	 * @since JDK 1.8
	 */
	public static <T extends Annotation> T getAnnotation(Class<?> cls, Class<T> annotationCls) {

		if (cls == null || annotationCls == null) {
			return null;
		}

		if (cls.isAnnotationPresent(annotationCls)) {
			return cls.getAnnotation(annotationCls);
		}

		Class<?>[] interfaces = cls.getInterfaces();

		for (Class<?> iface : interfaces) {
			if (iface.isAnnotationPresent(annotationCls)) {
				T annotation = iface.getAnnotation(annotationCls);
				if (annotation.annotationType().isAnnotationPresent(Inherited.class)) {
					return annotation;
				}
			}
		}

		return null;
	}

}
