package io.github.s3s3l.yggdrasil.boot.old.component.factory;

import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import io.github.s3s3l.yggdrasil.boot.old.component.exceptions.ComponentNotRegistException;
import io.github.s3s3l.yggdrasil.boot.old.component.exceptions.ComponentRegistException;
import io.github.s3s3l.yggdrasil.boot.old.component.exceptions.MultipleComponentException;
import io.github.s3s3l.yggdrasil.boot.old.component.exceptions.NoImplementInterfaceException;
import io.github.s3s3l.yggdrasil.boot.old.interceptor.DefaultInvocationHanlerFactory;
import io.github.s3s3l.yggdrasil.boot.old.interceptor.InvocationHanlerFactory;
import io.github.s3s3l.yggdrasil.utils.common.StringUtils;
import io.github.s3s3l.yggdrasil.utils.reflect.ReflectionUtils;
import io.github.s3s3l.yggdrasil.utils.verify.Verify;

public final class DefaultInvocationFactory implements InvocationFactory {
	private final Map<String, Class<?>> registerInvocations = new ConcurrentHashMap<>(256);
	private final Set<String> invocationCurrentlyInCreation = Collections
			.newSetFromMap(new ConcurrentHashMap<String, Boolean>(16));
	private final InvocationHanlerFactory invocationHandlerFactory = new DefaultInvocationHanlerFactory();

	@Override
	public boolean hasInvocation(String invocationName) {
		return this.registerInvocations.containsKey(invocationName);
	}

	@Override
	public void registerInvocation(String invocationName, Class<?> invocationClass) {
		Verify.hasText(invocationName, "invocationName must has text");
		Verify.notNull(invocationClass, "invocationClass must not be NULL");
		if (invocationClass.getInterfaces().length <= 0) {
			throw new NoImplementInterfaceException("invocationClass must implement at least one interface");
		}
		synchronized (this.registerInvocations) {
			Class<?> oldClass = this.registerInvocations.get(invocationName);
			if (oldClass != null) {
				throw new ComponentRegistException("Could not register object [", invocationClass.getSimpleName(),
						"] under invocation name '", invocationName, "': there is already object [",
						oldClass.getSimpleName(), "] bound");
			}

			this.registerInvocations.put(invocationName, invocationClass);
		}

	}

	@Override
	public Object getInvocation(String invocationName) {
		Verify.hasText(invocationName, "invocationName must has text");
		if (!this.registerInvocations.containsKey(invocationName)) {
			throw new ComponentNotRegistException(String.format("Can not find invocation named '%s'", invocationName));
		}
		Class<?> invocationClass = this.registerInvocations.get(invocationName);

		return getInvocation(invocationClass, invocationName);
	}

	@Override
	public <T> T getInvocation(Class<T> invocationClass) {
		Verify.notNull(invocationClass, "invocationClass must not be NULL");
		return getInvocation(invocationClass, invocationClass.getSimpleName());
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T getInvocation(Class<T> invocationClass, String invocationName) {
		Verify.notNull(invocationClass, "invocationClass must not be NULL");

		// 如果名字不为空，则判断该名字是否已注册；如未注册则抛出异常
		if (!StringUtils.isEmpty(invocationName) && !this.registerInvocations.containsKey(invocationName)) {
			throw new ComponentNotRegistException(String.format("Can not find invocation named '%s'", invocationName));
		}

		Class<?> invocationImplClass = invocationClass;
		if (StringUtils.isEmpty(invocationName) && invocationClass.isInterface()) {

			// 如果类型为接口，且名字为空，则查询实现类，如有多个实现类则抛出异常
			List<Class<?>> implementClasses = ReflectionUtils.getClassesImplement(invocationClass,
					this.registerInvocations.values());
			if (implementClasses.size() != 1) {
				if (implementClasses.isEmpty()) {
					throw new ComponentNotRegistException(
							String.format("Can not find invocation match type '%s'", invocationClass.getSimpleName()));
				} else {
					throw new MultipleComponentException(String.format("Find multiple invocation implementation [%s]",
							String.join(",", implementClasses.stream().map(r -> r.getSimpleName())
									.collect(Collectors.toList()))));
				}
			}
			invocationImplClass = implementClasses.get(0);

		} else if (!StringUtils.isEmpty(invocationName)) {

			// 如果名字不为空，则获取指定的调用组件类型
			invocationImplClass = this.registerInvocations.get(invocationName);
		}

		if (!this.registerInvocations.containsValue(invocationImplClass)) {
			throw new ComponentNotRegistException(
					String.format("Can not find invocation match type '%s'", invocationImplClass.getSimpleName()));
		}
		return (T) Proxy.newProxyInstance(invocationImplClass.getClassLoader(), invocationImplClass.getInterfaces(),
				invocationHandlerFactory.getHandler(invocationImplClass));
	}

	@Override
	public List<String> getRegisterInvocationNamesByType(Class<?> invocationClass) {
		Verify.notNull(invocationClass, "invocationClass must not be NULL");
		List<String> result = new ArrayList<>();
		for (String key : this.registerInvocations.keySet()) {
			if (this.registerInvocations.get(key) == invocationClass) {
				result.add(key);
			}
		}
		return result;
	}

	@Override
	public boolean isCurrentInCreation(String invocationName) {
		Verify.hasText(invocationName, "invocationName must has text");
		return this.invocationCurrentlyInCreation.contains(invocationName);
	}

	@Override
	public void setCurrentInCreation(String invocationName, boolean inCreation) {
		Verify.hasText(invocationName, "invocationName must has text");
		if (inCreation) {
			this.invocationCurrentlyInCreation.add(invocationName);
		} else {
			this.invocationCurrentlyInCreation.remove(invocationName);
		}
	}

}
