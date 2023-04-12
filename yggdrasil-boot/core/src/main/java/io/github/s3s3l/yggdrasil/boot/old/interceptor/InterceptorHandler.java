package io.github.s3s3l.yggdrasil.boot.old.interceptor;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public interface InterceptorHandler {

	/**
	 * 
	 * 方法执行前调用
	 * 
	 * @param proxy
	 *            代理对象
	 * @param method
	 *            方法
	 * @param args
	 *            参数
	 * @since JDK 1.8
	 * @see Proxy
	 */
	void before(Object proxy, Method method, Object[] args);

	/**
	 * 
	 * 方法执行后调用
	 * 
	 * @param proxy
	 *            代理对象
	 * @param method
	 *            方法
	 * @param args
	 *            参数
	 * @since JDK 1.8
	 * @see Proxy
	 */
	void after(Object proxy, Method method, Object[] args);

	/**
	 * 
	 * 方法执行出现异常时调用
	 * 
	 * @param proxy
	 *            代理对象
	 * @param method
	 *            方法
	 * @param args
	 *            参数
	 * @param error
	 *            异常
	 * @since JDK 1.8
	 * @see Proxy
	 */
	void exception(Object proxy, Method method, Object[] args, Throwable error);

	/**
	 * 
	 * 获取执行权级
	 * 
	 * @return 执行权级
	 * @since JDK 1.8
	 */
	int getLevel();
}
