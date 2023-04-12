package io.github.s3s3l.yggdrasil.boot.old.interceptor.base;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.NavigableSet;
import java.util.TreeSet;

import io.github.s3s3l.yggdrasil.boot.old.component.Initializer;
import io.github.s3s3l.yggdrasil.boot.old.interceptor.Interceptable;
import io.github.s3s3l.yggdrasil.utils.verify.Verify;

public abstract class InvocationHandlerBase implements InvocationHandler, Initializer, Interceptable {

	protected Object instance;
	protected final NavigableSet<InterceptorHandlerBase> interceptors = new TreeSet<>();

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		Object result = null;

		for (InterceptorHandlerBase interceptor : interceptors) {
			interceptor.before(proxy, method, args);
		}

		try {
			result = method.invoke(instance, args);
		} catch (Throwable e) {
			for (InterceptorHandlerBase interceptor : interceptors) {
				interceptor.exception(proxy, method, args, e);
			}
		} finally {
			for (InterceptorHandlerBase interceptor : interceptors) {
				interceptor.after(proxy, method, args);
			}
		}
		return result;
	}

	@Override
	public void afterPropertiesSet() {
		
	}

	@Override
	public void addInterceptor(InterceptorHandlerBase... interceptor) {
		Verify.notNull(interceptor, "interceptor can`t be NULL.");
		this.interceptors.addAll(Arrays.asList(interceptor));

	}

	public InvocationHandlerBase() {
	}

	public void setInstance(Object instance) {
		Verify.notNull(instance, "instance can`t be NULL.");
		this.instance = instance;
	}

	public NavigableSet<InterceptorHandlerBase> getInterceptors() {
		return interceptors;
	}

}
