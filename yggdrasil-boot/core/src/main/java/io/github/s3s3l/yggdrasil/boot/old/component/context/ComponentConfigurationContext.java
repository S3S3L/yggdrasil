package io.github.s3s3l.yggdrasil.boot.old.component.context;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Collection;

import io.github.s3s3l.yggdrasil.boot.old.component.annotation.Invocation;

/**
 * ClassName:ConfigurationContext <br>
 * Date: 2016年5月6日 上午10:47:53 <br>
 * 
 * @author kehw_zwei
 * @version 1.0.0
 * @since JDK 1.8
 */
public interface ComponentConfigurationContext {

	/**
	 * 
	 * 注册调用组件 <br>
	 * 扫描@{@link Invocation}注解 <br>
	 * ?>如果注解存在 <br>
	 * ?>>如果注解定义了{@code name}属性 <br>
	 * 用{@code name}的值注册组件 <br>
	 * ?>>如果注解没定义{@code name}属性 <br>
	 * 用{@code Class}的{@code SimpleName}来注册组件 <br>
	 * ?>如果注解不存在 <br>
	 * 用{@code Class}的{@code SimpleName}来注册组件 <br>
	 * ==>注册组件 <br>
	 * ?>如果组件名称已注册 <br>
	 * 抛出{@link IllegalArgumentException}异常 <br>
	 * ?>如果组件名称未注册 <br>
	 * 注册组件
	 * 
	 * @param invocationClasses
	 *            需要注册的组件类集合
	 * @since JDK 1.8
	 */
	void registerInvocation(Collection<Class<?>> invocationClasses);

	/**
	 * 
	 * 通过组件名获取组件 <br>
	 * 
	 * @param componentName
	 *            组件名称
	 * @return 组件对象
	 * @since JDK 1.8
	 */
	Object getComponent(String componentName);

	/**
	 * 
	 * 通过组件名获取指定类型的组件 <br>
	 * 
	 * @param componentName
	 *            组件名称
	 * @param componentClass
	 *            组件类型
	 * @param <T>
	 *            组件类型
	 * @return 指定类型的组件对象
	 * @since JDK 1.8
	 */
	<T> T getComponent(String componentName, Class<T> componentClass);

	/**
	 * 
	 * 通过组件类型获取指定类型的组件 <br>
	 * 
	 * @param componentClass
	 *            组件类型
	 * @param <T>
	 *            组件类型
	 * @return 指定类型的组件对象
	 * @since JDK 1.8
	 */
	<T> T getComponent(Class<T> componentClass);

	/**
	 * 
	 * 获取未注册的组件，并注入其中的依赖
	 * 
	 * @param componentClass
	 *            组件类型
	 * @param <T>
	 *            组件类型
	 * @param <T>
	 *            组件类型
	 * @return 指定类型的组件对象
	 * @since JDK 1.8
	 */
	<T> T getFreshComponent(Class<T> componentClass);

	/**
	 * 
	 * 注册组件
	 * 
	 * @param componentClass
	 *            组件类型集合
	 * @since JDK 1.8
	 */
	void registerComponent(Collection<Class<?>> componentClass);

	/**
	 * 
	 * 通过组件名称获取调用组件 <br>
	 * 
	 * @param invocationName
	 *            组件名称
	 * @return 调用组件代理对象
	 * @since JDK 1.8
	 * @see Proxy
	 */
	Object getInvocation(String invocationName);

	/**
	 * 
	 * 通过组件名称获取指定类型的调用组件 <br>
	 * 
	 * @param invocationName
	 *            组件名称
	 * @param invocationClass
	 *            组件类型
	 * @param <T>
	 *            组件类型
	 * @return 指定类型的调用组件代理对象
	 * @since JDK 1.8
	 * @see Proxy
	 */
	<T> T getInvocation(String invocationName, Class<T> invocationClass);

	/**
	 * 
	 * 通过组件类型获取指定类型的调用组件 <br>
	 * 
	 * @param invocationClass
	 *            组件类型
	 * @param <T>
	 *            组件类型
	 * @return 指定类型的调用组件代理对象
	 * @since JDK 1.8
	 * @see Proxy
	 */
	<T> T getInvocation(Class<T> invocationClass);

	/**
	 * 
	 * 注册元子组件
	 * 
	 * @param atomClasses
	 *            需要注册的组件类集合
	 * @since JDK 1.8
	 */
	void registerAtom(Collection<Class<?>> atomClasses);

	/**
	 * 
	 * 注册元子组件
	 * 
	 * @param atomConstructors
	 *            需要注册的组件工厂方法集合
	 * @since JDK 1.8
	 */
	void registerAtom(Method... atomConstructors);

	/**
	 * 
	 * 通过组件名称获取元子组件 <br>
	 * 
	 * @param atomName
	 *            组件名称
	 * @return 元子组件对象
	 * @since JDK 1.8
	 */
	Object getAtom(String atomName);

	/**
	 * 
	 * 通过组件名称获取指定类型的元子组件 <br>
	 * 
	 * @param atomName
	 *            组件名称
	 * @param atomClass
	 *            组件类型
	 * @param <T>
	 *            组件类型
	 * @return 指定类型的元子组件对象
	 * @since JDK 1.8
	 */
	<T> T getAtom(String atomName, Class<T> atomClass);

	/**
	 * 
	 * 通过组件类型获取指定类型的元子组件
	 * 
	 * @param atomClass
	 *            组件类型
	 * @param <T>
	 *            组件类型
	 * @return 指定类型的元子组件对象
	 * @since JDK 1.8
	 */
	<T> T getAtom(Class<T> atomClass);

	void addAtom(String atomName, Object atom);
}
