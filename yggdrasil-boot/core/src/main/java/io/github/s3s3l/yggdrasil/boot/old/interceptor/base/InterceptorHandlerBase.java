package io.github.s3s3l.yggdrasil.boot.old.interceptor.base;

import java.lang.reflect.Method;

import io.github.s3s3l.yggdrasil.boot.old.component.Initializer;
import io.github.s3s3l.yggdrasil.boot.old.interceptor.InterceptorHandler;

public abstract class InterceptorHandlerBase
		implements InterceptorHandler, Initializer, Comparable<InterceptorHandler> {

	private int level;

	/**
	 * 
	 * Creates a new instance of InterceptorHandlerBase.
	 * 
	 * @param level
	 *            执行权级
	 */
	protected InterceptorHandlerBase(int level) {
		this.level = level;
	}

	@Override
	public void before(Object proxy, Method method, Object[] args) {

	}

	@Override
	public void after(Object proxy, Method method, Object[] args) {

	}

	@Override
	public void exception(Object proxy, Method method, Object[] args, Throwable error) {

	}

	@Override
	public int getLevel() {
		return this.level;
	}

	@Override
	public void afterPropertiesSet() {

	}

	@Override
	public int compareTo(InterceptorHandler o) {
		return this.level - o.getLevel();
	}

}
