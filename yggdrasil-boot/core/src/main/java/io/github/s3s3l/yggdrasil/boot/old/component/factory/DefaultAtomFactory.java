package io.github.s3s3l.yggdrasil.boot.old.component.factory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import io.github.s3s3l.yggdrasil.boot.old.component.annotation.Substantiality;
import io.github.s3s3l.yggdrasil.boot.old.component.context.ComponentConfigurationContext;
import io.github.s3s3l.yggdrasil.boot.old.component.exceptions.ComponentInstantiateException;
import io.github.s3s3l.yggdrasil.boot.old.component.exceptions.ComponentNotRegistException;
import io.github.s3s3l.yggdrasil.boot.old.component.exceptions.ComponentRegistException;
import io.github.s3s3l.yggdrasil.boot.old.component.exceptions.MultipleComponentException;
import io.github.s3s3l.yggdrasil.boot.old.component.exceptions.ParameterNotValidateException;
import io.github.s3s3l.yggdrasil.utils.common.StringUtils;
import io.github.s3s3l.yggdrasil.utils.verify.Verify;

public final class DefaultAtomFactory implements AtomFactory {
	private final Map<String, Class<?>> registerAtoms = new ConcurrentHashMap<>(256);
	private final Map<String, AtomConstructor> atomConstructors = new ConcurrentHashMap<>(256);
	private final Map<String, Object> registerDepots = new ConcurrentHashMap<>(16);
	private final Set<String> atomCurrentlyInCreation = Collections
			.newSetFromMap(new ConcurrentHashMap<String, Boolean>(16));
	private ComponentConfigurationContext ctx;
	
	public DefaultAtomFactory(ComponentConfigurationContext context){
		setComponentConfiguration(context);
	}

	@Override
	public void setComponentConfiguration(ComponentConfigurationContext context) {
		this.ctx = context;
	}

	@Override
	public boolean hasAtom(String atomName) {
		return checkAtomName(atomName);
	}

	@Override
	public boolean hasDepot(Class<?> depotClass) {
		return this.registerDepots.containsKey(depotClass.getName());
	}

	@Override
	public void registerAtom(String atomName, Class<?> atomClass) {
		Verify.hasText(atomName, "atomName must has text");
		Verify.notNull(atomClass, "atomClass must not be NULL");
		synchronized (this) {
			Class<?> oldClass = getAtomClass(atomName);
			if (oldClass != null) {
				throw new ComponentRegistException("Could not register object [", atomClass.getName(),
						"] under atom name '", atomName, "': there is already object [", oldClass.getName(), "] bound");
			}

			this.registerAtoms.put(atomName, atomClass);
		}

	}

	@Override
	public void registerAtom(String atomName, Method creator) {
		Verify.hasText(atomName, "atomName must has text");
		Verify.notNull(creator, "creator must not be NULL");
		synchronized (this) {
			Class<?> oldClass = getAtomClass(atomName);
			if (oldClass != null) {
				throw new ComponentRegistException("Could not register object [", creator.getReturnType().getName(),
						"] under atom name '", atomName, "': there is already object [", oldClass.getName(), "] bound");
			}
			Class<?> depotClass = creator.getDeclaringClass();
			if (!registerDepots.containsKey(depotClass.getName())) {
				throw new ComponentNotRegistException("There is no depot named '", depotClass.getName(), "'");
			}

			AtomConstructor constructor = new AtomConstructor();
			constructor.setCreator(creator);
			constructor.setDepotName(depotClass.getName());
			atomConstructors.put(atomName, constructor);

			this.registerAtoms.put(atomName, creator.getReturnType());
		}

	}

	@Override
	public void registerDepot(Object depot) {
		Class<?> depotClass = depot.getClass();
		if (this.registerDepots.containsKey(depotClass.getName())) {
			throw new ComponentRegistException("Could not register depot [", depotClass.getSimpleName(),
					"] under depot name '", depotClass.getName(), "': already registed");
		}

		this.registerDepots.put(depotClass.getName(), depot);
	}

	@Override
	public Object getAtom(String atomName) {
		Verify.hasText(atomName, "atomName must has text");
		if (!checkAtomName(atomName)) {
			throw new ComponentNotRegistException(String.format("Can not find atom named '%s'", atomName));
		}
		Class<?> atomClass = this.registerAtoms.get(atomName);

		return getAtom(atomClass, atomName);
	}

	@Override
	public <T> T getAtom(Class<T> atomClass) {
		Verify.notNull(atomClass, "atomClass must not be NULL");
		return getAtom(atomClass, atomClass.getSimpleName());
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T getAtom(Class<T> atomClass, String atomName) {
		Verify.notNull(atomClass, "atomClass must not be NULL");
		if (!this.registerAtoms.containsValue(atomClass)) {
			throw new ComponentNotRegistException(
					String.format("Can not find atom match type '%s'", atomClass.getName()));
		}

		List<String> atomNames = getRegisterAtomNamesByType(atomClass);
		if (StringUtils.isEmpty(atomName) && atomNames.size() > 1) {
			throw new MultipleComponentException(String.format("There are multiple atom match type '%s' named [%s]",
					atomClass.getName(), String.join(",", atomNames)));
		}

		if (!atomNames.contains(atomName)) {
			atomName = atomNames.get(0);
		}

		T atom;
		try {
			if (this.atomConstructors.containsKey(atomName)) {
				atom = (T) getAtom(this.atomConstructors.get(atomName));
			} else {
				atom = atomClass.newInstance();
			}
		} catch (InstantiationException | IllegalAccessException e) {
			throw new ComponentInstantiateException(e);
		}
		return atom;

	}

	private Object getAtom(AtomConstructor constructor) {
		Verify.notNull(atomConstructors, "constructor must not be NULL");
		if (!registerDepots.containsKey(constructor.getDepotName())) {
			throw new ComponentNotRegistException(
					String.format("Can not find depot named '%s'", constructor.getDepotName()));
		}

		Method creator = constructor.getCreator();
		List<Object> params = new ArrayList<>();

		for (Parameter param : creator.getParameters()) {
			if (!param.isAnnotationPresent(Substantiality.class)) {
				throw new ParameterNotValidateException(
						String.format("Parameter '%s' is not validate", param.getName()));
			}
			Substantiality substantiality = param.getAnnotation(Substantiality.class);
			if (!StringUtils.isEmpty(substantiality.value())) {
				params.add(ctx.getComponent(substantiality.value()));
			} else {
				params.add(ctx.getComponent(param.getType()));
			}
		}
		try {
			return creator.invoke(registerDepots.get(constructor.getDepotName()), params.toArray());
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			throw new ComponentInstantiateException(e);
		}
	}

	@Override
	public List<String> getRegisterAtomNamesByType(Class<?> atomClass) {
		Verify.notNull(atomClass, "atomClass must not be NULL");
		List<String> result = new ArrayList<>();
		for (String key : this.registerAtoms.keySet()) {
			if (this.registerAtoms.get(key) == atomClass) {
				result.add(key);
			}
		}
		return result;
	}

	@Override
	public boolean isCurrentInCreation(String atomName) {
		Verify.hasText(atomName, "atomName must has text");
		return this.atomCurrentlyInCreation.contains(atomName);
	}

	@Override
	public void setCurrentInCreation(String atomName, boolean inCreation) {
		Verify.hasText(atomName, "invocationName must has text");
		if (inCreation) {
			this.atomCurrentlyInCreation.add(atomName);
		} else {
			this.atomCurrentlyInCreation.remove(atomName);
		}
	}

	private boolean checkAtomName(String atomName) {
		return registerAtoms.containsKey(atomName) || atomConstructors.containsKey(atomName);
	}

	private Class<?> getAtomClass(String atomName) {
		if (registerAtoms.containsKey(atomName)) {
			return registerAtoms.get(atomName);
		} else if (atomConstructors.containsKey(atomName)) {
			return atomConstructors.get(atomName).getCreator().getReturnType();
		}

		return null;
	}

}
