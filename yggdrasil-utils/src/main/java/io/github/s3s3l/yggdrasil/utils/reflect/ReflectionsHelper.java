package io.github.s3s3l.yggdrasil.utils.reflect;

import java.lang.annotation.Annotation;
import java.lang.annotation.Inherited;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.reflections.Configuration;
import org.reflections.Reflections;
import org.reflections.scanners.FieldAnnotationsScanner;
import org.reflections.scanners.MethodAnnotationsScanner;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;

/**
 * ClassName:ReflectionsHelper <br>
 * Date: 2016年5月9日 下午1:23:59 <br>
 * 
 * @author kehw_zwei
 * @version 1.0.0
 * @since JDK 1.8
 * @since <a href="https://github.com/ronmamo/reflections">Reflections
 *        0.9.10</a>
 * 
 */
public abstract class ReflectionsHelper {

    /**
     * 
     * 获取指定包下所有拥有指定注解的方法
     * 
     * @param annotationClass
     *            注解类型
     * @param packages
     *            包名，不指定则扫描所有
     * @return 指定包下所有拥有指定注解的方法
     * @since JDK 1.8
     * @since <a href="https://github.com/ronmamo/reflections">Reflections
     *        0.9.10</a>
     */
    public static Set<Method> getMethodAnnotatedWith(Class<? extends Annotation> annotationClass, String... packages) {

        Reflections reflections = new Reflections(getConfiguration(packages));

        return reflections.getMethodsAnnotatedWith(annotationClass);
    }

    /**
     * 
     * 获取指定包下的指定父类的所有子类（包括父类）
     * 
     * @param <T>
     *            class type
     * @param cls
     *            父类
     * @param packages
     *            包名，不指定则扫描所有
     * @return 指定包下的指定父类的所有子类（包括父类）
     * @since JDK 1.8
     * @since <a href="https://github.com/ronmamo/reflections">Reflections
     *        0.9.10</a>
     * 
     */
    public static <T> Set<Class<? extends T>> getSubTypesOf(Class<T> cls, String... packages) {

        Reflections reflections = new Reflections(getConfiguration(packages));

        return reflections.getSubTypesOf(cls);
    }

    /**
     * 
     * 获取指定包下的所有类
     * 
     * @param packages
     *            包名，不指定则扫描所有
     * @return 指定包下的所有类
     * @since JDK 1.8
     * @since <a href="https://github.com/ronmamo/reflections">Reflections
     *        0.9.10</a>
     * 
     */
    public static Set<Class<?>> getAllTypes(String... packages) {

        return getSubTypesOf(Object.class, packages);
    }

    /**
     * 
     * 获取指定包下的所有拥有指定注解的类
     * 
     * @param annotationClass
     *            注解类
     * @param honorInherited
     *            是否通过@{@link Inherited}注解来判断继承关系
     * @param packages
     *            包名，不指定则扫描所有
     * @return 指定包下的所有拥有指定注解的类
     * @since JDK 1.8
     * @since <a href="https://github.com/ronmamo/reflections">Reflections
     *        0.9.10</a>
     * 
     */
    public static Set<Class<?>> getTypesAnnotatedWith(Class<? extends Annotation> annotationClass,
            boolean honorInherited,
            String... packages) {

        Reflections reflections = new Reflections(getConfiguration(packages));

        return reflections.getTypesAnnotatedWith(annotationClass, honorInherited);
    }

    /**
     * 
     * 获取指定包下的所有拥有指定注解的类
     * 
     * @param annotationClasses
     *            注解类集合
     * @param honorInherited
     *            是否通过@{@link Inherited}注解来判断继承关系
     * @param packages
     *            包名，不指定则扫描所有
     * @return 指定包下的所有拥有指定注解的类
     * @since JDK 1.8
     * @since <a href="https://github.com/ronmamo/reflections">Reflections
     *        0.9.10</a>
     * 
     */
    public static Set<Class<?>> getTypesAnnotatedWith(List<Class<? extends Annotation>> annotationClasses,
            boolean honorInherited,
            String... packages) {
        Set<Class<?>> result = new HashSet<>();

        Reflections reflections = new Reflections(getConfiguration(packages));

        for (Class<? extends Annotation> annotationClass : annotationClasses) {
            result.addAll(reflections.getTypesAnnotatedWith(annotationClass, honorInherited));
        }

        return result;
    }

    /**
     * 
     * 获取指定包下的所有拥有指定注解的类
     * 
     * @param annotationClass
     *            注解类
     * @param packages
     *            包名，不指定则扫描所有
     * @return 指定包下的所有拥有指定注解的类
     * @since JDK 1.8
     * @since <a href="https://github.com/ronmamo/reflections">Reflections
     *        0.9.10</a>
     * 
     */
    public static Set<Class<?>> getTypesAnnotatedWith(Class<? extends Annotation> annotationClass, String... packages) {

        return getTypesAnnotatedWith(annotationClass, false, packages);
    }

    private static Configuration getConfiguration(String... packages) {
        List<URL> urls = new LinkedList<>();
        urls.addAll(ClasspathHelper.forJavaClassPath());
        urls.addAll(ClasspathHelper.forClassLoader());
        ConfigurationBuilder config = new ConfigurationBuilder()
                .setScanners(new MethodAnnotationsScanner(), new SubTypesScanner(false), new TypeAnnotationsScanner(),
                        new FieldAnnotationsScanner())
                .setUrls(urls.stream()
                        .distinct()
                        .collect(Collectors.toList()));
        if (packages != null && packages.length > 0) {
            config.filterInputsBy(new FilterBuilder().includePackage(packages));
        }

        return config;
    }
}
