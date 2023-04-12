package io.github.s3s3l.yggdrasil.boot.old.interceptor;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import io.github.s3s3l.yggdrasil.annotation.AnnotationHelper;
import io.github.s3s3l.yggdrasil.boot.old.component.annotation.Invocation;
import io.github.s3s3l.yggdrasil.boot.old.interceptor.base.InterceptorHandlerBase;
import io.github.s3s3l.yggdrasil.boot.old.interceptor.base.InvocationHandlerBase;
import io.github.s3s3l.yggdrasil.utils.verify.Verify;

public class DefaultInvocationHanlerFactory implements InvocationHanlerFactory {

	@Override
	public InvocationHandlerBase getHandler(Class<?> cls) {
		Verify.notNull(cls, "Class can not be NULL");
		Invocation invocation = AnnotationHelper.getAnnotation(cls, Invocation.class);
		Class<? extends InvocationHandlerBase> handlerClass = getHandlerClass(invocation);
		if (handlerClass == null) {
			return null;
		}
		InvocationHandlerBase handler = null;
		try {
			handler = handlerClass.getConstructor()
			        .newInstance();
			handler.setInstance(cls.getConstructor()
			        .newInstance());
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
		        | NoSuchMethodException | SecurityException e) {
			throw new IllegalArgumentException(e);
		}
		handler.addInterceptor(getInterceptors(invocation).toArray(new InterceptorHandlerBase[] {}));
		handler.afterPropertiesSet();
		return handler;
	}

	/**
	 * 
	 * 从@{@link Invocation}注解获取{@code handler}的类型
	 * 
	 * @param invocation
	 *            注解对象
	 * @return 注解中指定的handler类型
	 * @since JDK 1.8
	 */
	private Class<? extends InvocationHandlerBase> getHandlerClass(Invocation invocation) {
		if (invocation == null) {
			return null;
		}
		return invocation.value();
	}

	/**
	 * 
	 * 从@{@link Invocation}注解获取拦截器列表
	 * 
	 * @param invocation
	 *            注解对象
	 * @return 注解中指定的拦截器列表
	 * @since JDK 1.8
	 */
	private List<InterceptorHandlerBase> getInterceptors(Invocation invocation) {
		List<InterceptorHandlerBase> result = new ArrayList<>();
		if (invocation == null) {
			return result;
		}

		for (Class<? extends InterceptorHandlerBase> interceptorCls : invocation.interceptors()) {
			InterceptorHandlerBase interceptor;
			try {
				interceptor = interceptorCls.getConstructor()
				        .newInstance();
			} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
			        | InvocationTargetException | NoSuchMethodException | SecurityException e) {
				throw new IllegalArgumentException(e);
			}
			interceptor.afterPropertiesSet();
			result.add(interceptor);
		}

		return result;
	}

}
