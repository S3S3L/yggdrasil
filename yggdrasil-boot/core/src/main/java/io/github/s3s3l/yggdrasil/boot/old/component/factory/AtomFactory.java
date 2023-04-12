package io.github.s3s3l.yggdrasil.boot.old.component.factory;

import java.lang.reflect.Method;
import java.util.List;

import io.github.s3s3l.yggdrasil.boot.old.component.context.ComponentConfigurationContext;

public interface AtomFactory {

	boolean hasAtom(String atomName);

	boolean hasDepot(Class<?> depotClass);

	void registerAtom(String atomName, Class<?> atomClass);

	void registerAtom(String atomName, Method creator);

	void registerDepot(Object depot);

	Object getAtom(String atomName);

	<T> T getAtom(Class<T> atomClass);

	<T> T getAtom(Class<T> atomClass, String atomName);

	List<String> getRegisterAtomNamesByType(Class<?> atomClass);

	boolean isCurrentInCreation(String atomName);

	void setCurrentInCreation(String atomName, boolean inCreation);
	
	void setComponentConfiguration(ComponentConfigurationContext context);

}
