package io.github.s3s3l.yggdrasil.boot.old.component.factory;

import java.util.List;

public interface InvocationFactory {

	boolean hasInvocation(String invocationName);

	void registerInvocation(String invocationName, Class<?> invocationClass);

	Object getInvocation(String invocationName);

	<T> T getInvocation(Class<T> invocationClass);

	<T> T getInvocation(Class<T> invocationClass, String invocationName);

	List<String> getRegisterInvocationNamesByType(Class<?> invocationClass);

	boolean isCurrentInCreation(String invocationName);

	void setCurrentInCreation(String invocationName, boolean inCreation);
}
