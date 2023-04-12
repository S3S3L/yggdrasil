package io.github.s3s3l.yggdrasil.boot.old.interceptor;

import io.github.s3s3l.yggdrasil.boot.old.interceptor.base.InvocationHandlerBase;

public interface InvocationHanlerFactory {

	/**
	 * 
	 * 根据类型获取{@code handler}实例
	 * 
	 * @param cls
	 *            {@code handler}类型
	 * @return {@code handler}实例
	 * @since JDK 1.8
	 */
	InvocationHandlerBase getHandler(Class<?> cls);
}
