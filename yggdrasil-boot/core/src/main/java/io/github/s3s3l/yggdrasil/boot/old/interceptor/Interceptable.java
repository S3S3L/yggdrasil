package io.github.s3s3l.yggdrasil.boot.old.interceptor;

import io.github.s3s3l.yggdrasil.boot.old.interceptor.base.InterceptorHandlerBase;

public interface Interceptable {

	/**
	 * 
	 * 新增拦截器
	 * 
	 * @param interceptor
	 *            拦截器
	 * @since JDK 1.8
	 */
	void addInterceptor(InterceptorHandlerBase... interceptor);
}
