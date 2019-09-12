package org.s3s3l.yggdrasil.utils.interceptor;

import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.Collection;

import org.s3s3l.yggdrasil.utils.verify.Verify;

/**
 * ClassName:ProxyFactory <br>
 * Date: 2016年4月25日 下午3:49:13 <br>
 * 
 * @author kehw_zwei
 * @version 1.0.0
 * @since JDK 1.8
 */
public class ProxyFactory {

	/**
	 * 
	 * 为对象构建代理对象
	 * 
	 * @author kehw_zwei
	 * @param object
	 *            目标对象
	 * @param implCls
	 *            实现类
	 * @param handler
	 *            处理器
	 * @return 代理对象
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 * @since JDK 1.8
	 */
	public Object getProxy(Object object,
			Class<?> implCls,
			Class<? extends InterceptorHandler> handlerType,
			Interceptor... interceptors) throws InstantiationException, IllegalAccessException {
		Verify.notEmpty(interceptors);

		return getProxy(object, implCls, handlerType, Arrays.asList(interceptors));
	}

	/**
	 * 
	 * 为对象构建代理对象
	 * 
	 * @author kehw_zwei
	 * @param object
	 *            目标对象
	 * @param implCls
	 *            实现类
	 * @param handler
	 *            处理器
	 * @return 代理对象
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 * @since JDK 1.8
	 */
	public Object getProxy(Object object,
			Class<?> implCls,
			Class<? extends InterceptorHandler> handlerType,
			Collection<Interceptor> interceptors) throws InstantiationException, IllegalAccessException {
		Verify.notNull(object);
		Verify.notNull(implCls);
		Verify.notNull(handlerType);
		Verify.notNull(interceptors);

		InterceptorHandler handler = handlerType.newInstance();

		handler.setTarget(object);
		handler.addInterceptor(interceptors);

		return Proxy.newProxyInstance(implCls.getClassLoader(), implCls.getInterfaces(), handler);
	}
}
