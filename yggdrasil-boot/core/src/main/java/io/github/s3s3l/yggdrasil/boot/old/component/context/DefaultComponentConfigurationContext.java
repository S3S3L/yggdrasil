package io.github.s3s3l.yggdrasil.boot.old.component.context;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.cfg.ConfigFeature;

import io.github.s3s3l.yggdrasil.boot.old.component.annotation.Atom;
import io.github.s3s3l.yggdrasil.boot.old.component.annotation.AutoInjected;
import io.github.s3s3l.yggdrasil.boot.old.component.annotation.Configuration;
import io.github.s3s3l.yggdrasil.boot.old.component.annotation.Depot;
import io.github.s3s3l.yggdrasil.boot.old.component.annotation.Invocation;
import io.github.s3s3l.yggdrasil.boot.old.component.exceptions.ComponentAlreadyExistException;
import io.github.s3s3l.yggdrasil.boot.old.component.exceptions.ComponentInstantiateException;
import io.github.s3s3l.yggdrasil.boot.old.component.exceptions.ComponentNotRegistException;
import io.github.s3s3l.yggdrasil.boot.old.component.exceptions.ConfigurationLoadingException;
import io.github.s3s3l.yggdrasil.boot.old.component.factory.AtomFactory;
import io.github.s3s3l.yggdrasil.boot.old.component.factory.DefaultAtomFactory;
import io.github.s3s3l.yggdrasil.boot.old.component.factory.DefaultInvocationFactory;
import io.github.s3s3l.yggdrasil.boot.old.component.factory.InvocationFactory;
import io.github.s3s3l.yggdrasil.resource.yaml.YAMLResourceLoader;
import io.github.s3s3l.yggdrasil.utils.common.StringUtils;
import io.github.s3s3l.yggdrasil.utils.file.FileUtils;
import io.github.s3s3l.yggdrasil.utils.reflect.Reflection;
import io.github.s3s3l.yggdrasil.utils.reflect.ReflectionBean;
import io.github.s3s3l.yggdrasil.utils.reflect.ReflectionUtils;
import io.github.s3s3l.yggdrasil.utils.verify.Verify;

/**
 * ClassName:ComponentConfigurationContext <br>
 * Date: 2016年5月6日 上午10:47:33 <br>
 * 
 * @author kehw_zwei
 * @version 1.0.0
 * @since JDK 1.8
 */
public class DefaultComponentConfigurationContext implements ComponentConfigurationContext {

	/**
	 * 所有组件名称列表
	 */
	private final Set<String> componentName = new LinkedHashSet<>();
	/**
	 * 调用组件集合
	 */
	private final Map<String, Object> invocations = new ConcurrentHashMap<>();
	/**
	 * 调用组件工厂
	 */
	private final InvocationFactory invocationFactory = new DefaultInvocationFactory();
	/**
	 * 元子组件集合
	 */
	private final Map<String, Object> atoms = new ConcurrentHashMap<>();
	/**
	 * 元子组件工厂
	 */
	private final AtomFactory atomFactory = new DefaultAtomFactory(this);

	@Override
	public void registerInvocation(Collection<Class<?>> invocationClasses) {
		Verify.notNull(invocationClasses, "invocationClasses must not be NULL");
		for (Class<?> invocationClass : invocationClasses) {
			Verify.notNull(invocationClass, "invocationClass must not be NULL");
			String invocationName = StringUtils.EMPTY_STRING;
			if (invocationClass.isAnnotationPresent(Invocation.class)) {
				Invocation invocation = invocationClass.getDeclaredAnnotation(Invocation.class);
				invocationName = StringUtils.isEmpty(invocation.name()) ? invocationClass.getSimpleName()
				        : invocation.name();
			} else {
				invocationName = invocationClass.getSimpleName();
			}
			if (this.componentName.contains(invocationName)) {
				throw new ComponentAlreadyExistException(
				        String.format("Already has a component named '%s'", invocationName));
			}
			this.invocationFactory.registerInvocation(invocationName, invocationClass);
			this.componentName.add(invocationName);
		}

	}

	@Override
	public <T> T getInvocation(Class<T> invocationClass) {
		Verify.notNull(invocationClass, "invocationClass must not be NULL");
		T invocation = this.invocationFactory.getInvocation(invocationClass);
		ReflectionBean reflectUtils = Reflection.create(invocation);
		for (Field field : ReflectionUtils.getFieldsAnnotatedWith(invocationClass, AutoInjected.class)) {
			AutoInjected autoInjected = field.getAnnotation(AutoInjected.class);
			if (StringUtils.isEmpty(autoInjected.value())) {
				try {
					reflectUtils.setFieldValue(field.getName(), getComponent(field.getType()));
				} catch (IllegalArgumentException e) {
					throw new ComponentInstantiateException(e);
				}
			} else {
				try {
					reflectUtils.setFieldValue(field.getName(), getComponent(autoInjected.value(), field.getType()));
				} catch (IllegalArgumentException e) {
					throw new ComponentInstantiateException(e);
				}
			}
		}
		return invocation;
	}

	@Override
	public Object getInvocation(String invocationName) {
		Verify.hasText(invocationName, "invocationName must has text");
		if (!this.invocations.containsKey(invocationName)) {
			this.invocations.put(invocationName, this.invocationFactory.getInvocation(invocationName));
		}
		return this.invocations.get(invocationName);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T getInvocation(String componentName, Class<T> componentClass) {
		Verify.hasText(componentName, "invocationName must has text");
		Verify.notNull(componentClass, "invocationClass must not be NULL");
		return (T) getInvocation(componentName);
	}

	@Override
	public void registerAtom(Collection<Class<?>> atomClasses) {
		Verify.notNull(atomClasses, "atomClasses must not be NULL");
		for (Class<?> atomClass : atomClasses) {
			Verify.notNull(atomClass, "atomClass must not be NULL");
			String atomName = StringUtils.EMPTY_STRING;
			if (atomClass.isAnnotationPresent(Atom.class)) {
				Atom atom = atomClass.getDeclaredAnnotation(Atom.class);
				atomName = StringUtils.isEmpty(atom.value()) ? atomClass.getSimpleName() : atom.value();
			} else {
				atomName = atomClass.getSimpleName();
			}

			if (this.componentName.contains(atomName)) {
				throw new ComponentAlreadyExistException(String.format("Already has a component named '%s'", atomName));
			}

			this.atomFactory.registerAtom(atomName, atomClass);
			this.componentName.add(atomName);
		}
	}

	@Override
	public void registerAtom(Method... atomConstructors) {
		Verify.notNull(atomConstructors, "atomConstructors must not be NULL");
		for (Method atomConstructor : atomConstructors) {
			Verify.notNull(atomConstructor, "atomConstructor must not be NULL");
			String atomName = StringUtils.EMPTY_STRING;

			Class<?> depotClass = atomConstructor.getDeclaringClass();

			if (atomConstructor.isAnnotationPresent(Atom.class)) {
				Atom atom = atomConstructor.getDeclaredAnnotation(Atom.class);
				atomName = StringUtils.isEmpty(atom.value()) ? atomConstructor.getReturnType()
				        .getSimpleName() : atom.value();
			} else {
				atomName = atomConstructor.getReturnType()
				        .getSimpleName();
			}

			if (this.componentName.contains(atomName)) {
				throw new ComponentAlreadyExistException(String.format("Already has a component named '%s'", atomName));
			}

			if (!this.atomFactory.hasDepot(depotClass)) {

				Object depot;
				try {
					depot = depotClass.getConstructor()
					        .newInstance();
				} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
				        | InvocationTargetException | NoSuchMethodException | SecurityException e) {
					throw new IllegalArgumentException(e);
				}

				ReflectionBean depotReflect = Reflection.create(depot);

				for (Field field : ReflectionUtils.getFieldsAnnotatedWith(depotClass, AutoInjected.class)) {
					AutoInjected autoInjected = field.getAnnotation(AutoInjected.class);
					if (StringUtils.isEmpty(autoInjected.value())) {
						try {
							depotReflect.setFieldValue(field.getName(), getComponent(field.getType()));
						} catch (IllegalArgumentException e) {
							throw new ComponentInstantiateException(e);
						}
					} else {
						try {
							depotReflect.setFieldValue(field.getName(),
							        getComponent(autoInjected.value(), field.getType()));
						} catch (IllegalArgumentException e) {
							throw new ComponentInstantiateException(e);
						}
					}
				}

				this.atomFactory.registerDepot(depot);

			}
			this.atomFactory.registerAtom(atomName, atomConstructor);
			this.componentName.add(atomName);
		}
	}

	@Override
	public Object getAtom(String atomName) {
		Verify.hasText(atomName, "atomName must has text");
		if (!this.atoms.containsKey(atomName)) {
			this.atoms.put(atomName, this.atomFactory.getAtom(atomName));
		}
		return this.atoms.get(atomName);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T getAtom(String atomName, Class<T> atomClass) {
		Verify.hasText(atomName, "atomName must has text");
		Verify.notNull(atomClass, "atomClass must not be NULL");
		return (T) getAtom(atomName);
	}

	@Override
	public <T> T getAtom(Class<T> atomClass) {
		Verify.notNull(atomClass, "atomClass must not be NULL");
		T atom = this.atomFactory.getAtom(atomClass);
		ReflectionBean reflectUtils = Reflection.create(atom);
		for (Field field : ReflectionUtils.getFieldsAnnotatedWith(atomClass, AutoInjected.class)) {
			AutoInjected autoInjected = field.getDeclaredAnnotation(AutoInjected.class);
			if (StringUtils.isEmpty(autoInjected.value())) {
				try {
					reflectUtils.setFieldValue(field.getName(), getComponent(field.getType()));
				} catch (IllegalArgumentException e) {
					throw new ComponentInstantiateException(e);
				}
			} else {
				try {
					reflectUtils.setFieldValue(field.getName(), getComponent(autoInjected.value(), field.getType()));
				} catch (IllegalArgumentException e) {
					throw new ComponentInstantiateException(e);
				}
			}
		}

		return atom;
	}

	@Override
	public Object getComponent(String componentName) {
		Verify.hasText(componentName, "componentName must has text");
		if (!this.componentName.contains(componentName)) {
			throw new ComponentNotRegistException(String.format("Can not find component named '%s'", componentName));
		}

		if (this.invocations.containsKey(componentName)) {
			return this.invocations.get(componentName);
		}

		if (this.atoms.containsKey(componentName)) {
			return this.atoms.get(componentName);
		}

		if (this.invocationFactory.hasInvocation(componentName)) {
			return getInvocation(componentName);
		}

		if (this.atomFactory.hasAtom(componentName)) {
			return getAtom(componentName);
		}

		throw new ComponentNotRegistException(String.format("Can not find component named '%s'", componentName));
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T getComponent(String componentName, Class<T> componentClass) {
		Verify.hasText(componentName, "componentName must has text");
		Verify.notNull(componentClass, "componentClass must not be NULL");
		T component = (T) getComponent(componentName);
		return applyConfiguration(component, componentClass);
	}

	@Override
	public <T> T getComponent(Class<T> componentClass) {
		Verify.notNull(componentClass, "componentClass must not be NULL");
		T component;
		try{
			component = getInvocation(componentClass);
		} catch (ComponentNotRegistException e) {
			component = getAtom(componentClass);
		}
		return applyConfiguration(component, componentClass);
	}

	@Override
	public <T> T getFreshComponent(Class<T> componentClass) {
		Verify.notNull(componentClass, "componentClass must not be NULL");
		T component;
		try {
			component = componentClass.getConstructor()
			        .newInstance();
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
		        | NoSuchMethodException | SecurityException e) {
			throw new ComponentInstantiateException(e);
		}

		applyConfiguration(component, componentClass);

		Reflection<T> reflectUtils = Reflection.create(component);
		for (Field field : ReflectionUtils.getFieldsAnnotatedWith(componentClass, AutoInjected.class)) {
			AutoInjected autoInjected = field.getDeclaredAnnotation(AutoInjected.class);
			if (StringUtils.isEmpty(autoInjected.value())) {
				try {
					reflectUtils.setFieldValue(field.getName(), getComponent(field.getType()));
				} catch (IllegalArgumentException e) {
					throw new ComponentInstantiateException(e);
				}
			} else {
				try {
					reflectUtils.setFieldValue(field.getName(), getComponent(autoInjected.value(), field.getType()));
				} catch (IllegalArgumentException e) {
					throw new ComponentInstantiateException(e);
				}
			}
		}
		return component;
	}

	@Override
	public void registerComponent(Collection<Class<?>> componentClasses) {
		Verify.notNull(componentClasses, "componentClasses can not be NULL.");

		registerInvocation(componentClasses.stream()
		        .filter(r -> r.isAnnotationPresent(Invocation.class))
		        .collect(Collectors.toList()));

		registerAtom(componentClasses.stream()
		        .filter(r -> r.isAnnotationPresent(Atom.class))
		        .collect(Collectors.toList()));

		for (Class<?> componentClass : componentClasses.stream()
		        .filter(r -> r.isAnnotationPresent(Depot.class))
		        .collect(Collectors.toList())) {
			Verify.notNull(componentClass, "componentClass con not be NULL.");
			registerAtom(Arrays.asList(componentClass.getMethods())
			        .stream()
			        .filter(r -> r.isAnnotationPresent(Atom.class))
			        .collect(Collectors.toList())
			        .toArray(new Method[] {}));
		}

	}

	@Override
	public void addAtom(String atomName, Object atom) {
		Verify.hasText(atomName, "atomName must has text.");
		Verify.notNull(atom, "atom can not be NULL.");
		if (this.componentName.contains(atomName)) {
			throw new ComponentAlreadyExistException(String.format("Already has a component named '%s'", atomName));
		}
		this.componentName.add(atomName);
		this.atoms.put(atomName, atom);

	}

	private <T> T applyConfiguration(T component, Class<T> type) {
		if (!ReflectionUtils.isAnnotationedWith(type, Configuration.class)) {
			return component;
		}
		try {
			Configuration configuration = ReflectionUtils.getAnnotation(type, Configuration.class);
			String[] resourceLocations = configuration.value().length > 0 ? configuration.value()
			        : configuration.resources();
			if (resourceLocations.length <= 0) {
				return component;
			}

			String profileKey = configuration.profileKey();
			String profile = configuration.profile();
			boolean checkFileExist = configuration.checkFile();
			boolean combine = configuration.combine();

			YAMLResourceLoader yml;
			Map<ConfigFeature, Boolean> config = new HashMap<>();
			config.put(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			if (StringUtils.isEmpty(profile)) {
				yml = YAMLResourceLoader.create(config);
			} else {
				yml = YAMLResourceLoader.create(profileKey, config);
			}

			if (!combine) {
				File resource = FileUtils.getFirstExistFile(resourceLocations);
				yml.loadConfiguration(component, resource, type, profile);
			} else {
				File[] resources = Arrays.asList(resourceLocations)
				        .stream()
				        .map(r -> new File(r))
				        .collect(Collectors.toList())
				        .toArray(new File[] {});
				yml.loadConfiguration(component, type, checkFileExist, profile, resources);
			}
		} catch (InstantiationException | IllegalAccessException | IOException | IllegalArgumentException
		        | InvocationTargetException | NoSuchMethodException | SecurityException e) {
			throw new ConfigurationLoadingException(e);
		}

		return component;
	}

}
