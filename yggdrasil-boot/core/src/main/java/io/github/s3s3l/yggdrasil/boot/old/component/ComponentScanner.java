package io.github.s3s3l.yggdrasil.boot.old.component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;

import io.github.s3s3l.yggdrasil.boot.old.component.annotation.Atom;
import io.github.s3s3l.yggdrasil.boot.old.component.annotation.Depot;
import io.github.s3s3l.yggdrasil.boot.old.component.annotation.Invocation;
import io.github.s3s3l.yggdrasil.boot.old.component.context.ComponentConfigurationContext;
import io.github.s3s3l.yggdrasil.boot.old.component.context.DefaultComponentConfigurationContext;
import io.github.s3s3l.yggdrasil.utils.reflect.ReflectionsHelper;

public abstract class ComponentScanner {

	public static ComponentConfigurationContext scan(String... packages) {
		Set<Class<?>> classes = ReflectionsHelper.getTypesAnnotatedWith(
				new ArrayList<>(Arrays.asList(Invocation.class, Atom.class, Depot.class)), true, packages);
		ComponentConfigurationContext ctx = new DefaultComponentConfigurationContext();
		ctx.registerComponent(classes);
		return ctx;
	}

	public static ComponentConfigurationContext scan(ComponentConfigurationContext ctx, String... packages) {
		Set<Class<?>> classes = ReflectionsHelper.getTypesAnnotatedWith(
				new ArrayList<>(Arrays.asList(Invocation.class, Atom.class, Depot.class)), true, packages);
		ctx.registerComponent(classes);
		return ctx;
	}
}
