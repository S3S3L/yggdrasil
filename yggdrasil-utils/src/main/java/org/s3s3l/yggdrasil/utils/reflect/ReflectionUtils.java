package org.s3s3l.yggdrasil.utils.reflect;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang3.StringUtils;
import org.s3s3l.yggdrasil.annotation.FromJson;
import org.s3s3l.yggdrasil.annotation.FromObject;
import org.s3s3l.yggdrasil.utils.collection.CollectionUtils;
import org.s3s3l.yggdrasil.utils.reflect.exception.ReflectException;
import org.s3s3l.yggdrasil.utils.stuctural.jackson.JacksonUtils;

import com.fasterxml.jackson.core.type.TypeReference;

import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * </p>
 * ClassName:ReflectUtils <br>
 * Date: Aug 10, 2016 7:11:40 PM <br>
 * 
 * @author kehw_zwei
 * @version 1.0.0
 * @since JDK 1.8
 */
@Slf4j
public abstract class ReflectionUtils {

    public static final Map<Class<?>, Set<Field>> FIELD_CACHE = new ConcurrentHashMap<>();

    /**
     * 
     * cast json field to object field
     * 
     * @param data
     * @param type
     * @return
     * @since JDK 1.8
     */
    public static <T> T deserialize(T data, Class<T> type) {
        ReflectionBean ref = new PropertyDescriptorReflectionBean(data);
        Set<Field> fields = getFields(type);
        fields.stream()
                .filter(r -> r.isAnnotationPresent(FromJson.class))
                .forEach(field -> {
                    FromJson fromJson = field.getAnnotation(FromJson.class);
                    Field strField;
                    try {
                        strField = CollectionUtils.getFirstOptional(fields, f -> fromJson.value()
                                .equals(f.getName()))
                                .orElseThrow(() -> new NoSuchFieldException(fromJson.value()));
                    } catch (NoSuchFieldException e) {
                        log.warn("field not found", e);
                        return;
                    }

                    if (!String.class.isAssignableFrom(strField.getType())) {
                        return;
                    }

                    String json = (String) ref.getFieldValue(strField.getName());
                    if (StringUtils.isEmpty(json)) {
                        return;
                    }
                    try {
                        ref.setFieldValue(field.getName(),
                                JacksonUtils.JSON.toObject(json, new TypeReference<Object>() {
                                    @Override
                                    public Type getType() {
                                        return field.getGenericType();
                                    }
                                }));
                    } catch (IllegalArgumentException e) {
                        log.warn("set field value fail.", e);
                        return;
                    }
                });

        return data;
    }

    /**
     * 
     * cast object field to json field
     * 
     * @param data
     * @param type
     * @return
     * @since JDK 1.8
     */
    public static <T> T serialize(T data, Class<T> type) {
        ReflectionBean ref = new PropertyDescriptorReflectionBean(data);
        Set<Field> fields = getFields(type);
        fields.stream()
                .filter(r -> r.isAnnotationPresent(FromObject.class) && r.getType() == String.class)
                .forEach(field -> {
                    FromObject fromObject = field.getAnnotation(FromObject.class);
                    Field objField;
                    try {
                        objField = CollectionUtils.getFirstOptional(fields, f -> fromObject.value()
                                .equals(f.getName()))
                                .orElseThrow(() -> new NoSuchFieldException(fromObject.value()));
                    } catch (NoSuchFieldException e) {
                        log.warn("field not found", e);
                        return;
                    }

                    Object obj = ref.getFieldValue(objField.getName());
                    if (obj == null) {
                        return;
                    }
                    try {
                        ref.setFieldValue(field.getName(), JacksonUtils.JSON.toStructuralString(obj));
                    } catch (IllegalArgumentException e) {
                        log.warn("set field value fail.", e);
                        return;
                    }
                });

        return data;
    }

    public static <T> T clone(T src, Class<T> type) {
        try {
            ReflectionBean srcReflection = new PropertyDescriptorReflectionBean(src);
            T result = type.getConstructor().newInstance();
            ReflectionBean targetReflection = new PropertyDescriptorReflectionBean(result);
            for (Field field : getFields(type)) {
                String fieldName = field.getName();
                targetReflection.setFieldValue(fieldName, srcReflection.getFieldValue(fieldName));
            }

            return result;
        } catch (InstantiationException | IllegalAccessException | SecurityException | IllegalArgumentException
                | InvocationTargetException | NoSuchMethodException e) {
            throw new ReflectException(e);
        }
    }

    /**
     * 
     * 判断类型是否有指定注解（会深入注解中的注解进行判断）
     * 
     * @param cls
     *                        类型
     * @param annotationClass
     *                        注解类型
     * @return true：类型有指定注解；false：类型没有指定注解
     * @since JDK 1.8
     */
    public static boolean isAnnotationedWith(Class<?> cls, Class<? extends Annotation> annotationClass) {
        if (cls.getName()
                .startsWith("java.lang.annotation")
                || cls.getName()
                        .startsWith("kotlin.annotation")
                || cls.getName()
                        .equals("kotlin.Metadata")) {
            return false;
        }

        if (cls.isAnnotationPresent(annotationClass)) {
            return true;
        }

        for (Annotation annotation : cls.getAnnotations()) {
            if (isAnnotationedWith(annotation.annotationType(), annotationClass)) {
                return true;
            }
        }

        return false;
    }

    /**
     * 
     * 获取类型上的指定注解（会深入注解中的注解进行判断）
     * 
     * @param cls
     *                        类型
     * @param annotationClass
     *                        注解类型
     * @param <T>
     *                        注解类型
     * @return 类型有指定注解：返回该注解；类型没有指定注解：返回null
     * @since JDK 1.8
     */
    public static <T extends Annotation> T getAnnotation(Class<?> cls, Class<T> annotationClass) {

        if (cls.isAnnotationPresent(annotationClass)) {
            return cls.getAnnotation(annotationClass);
        }

        for (Annotation annotation : cls.getAnnotations()) {
            Class<? extends Annotation> annotationType = annotation.annotationType();
            if (isAnnotationedWith(annotationType, annotationClass)) {
                return annotationType.getAnnotation(annotationClass);
            }
        }

        return null;

    }

    /**
     * 
     * 判断方法是否有指定注解（会深入注解中的注解进行判断，并且会判断定义类上的注解）
     * 
     * @param method
     *                        方法
     * @param annotationClass
     *                        注解类型
     * @return true：方法有指定注解；false：方法没有指定注解
     * @since JDK 1.8
     */
    public static boolean isAnnotationedWith(Method method, Class<? extends Annotation> annotationClass) {

        if (method.isAnnotationPresent(annotationClass)) {
            return true;
        }

        for (Annotation annotation : method.getAnnotations()) {
            if (isAnnotationedWith(annotation.annotationType(), annotationClass)) {
                return true;
            }
        }

        return isAnnotationedWith(method.getDeclaringClass(), annotationClass);
    }

    /**
     * 
     * 获取方法上的指定注解（会深入注解中的注解进行判断，并且会判断定义类上的注解）
     * 
     * @param method
     *                        方法
     * @param annotationClass
     *                        注解类型
     * @param <T>
     *                        注解类型
     * @return 方法有指定注解：返回该注解；方法没有指定注解：返回null
     * @since JDK 1.8
     */
    public static <T extends Annotation> T getAnnotation(Method method, Class<T> annotationClass) {

        if (method.isAnnotationPresent(annotationClass)) {
            return method.getAnnotation(annotationClass);
        }

        for (Annotation annotation : method.getAnnotations()) {
            Class<? extends Annotation> annotationType = annotation.annotationType();
            if (isAnnotationedWith(annotationType, annotationClass)) {
                return annotationType.getAnnotation(annotationClass);
            }
        }

        return getAnnotation(method.getDeclaringClass(), annotationClass);

    }

    public static Set<Field> getFields(Class<?> type) {
        return FIELD_CACHE.computeIfAbsent(type, t -> getFieldsNoCache(t));
    }

    private static Set<Field> getFieldsNoCache(Class<?> type) {
        if (type == Object.class) {
            return new HashSet<>();
        }

        Set<Field> result = new HashSet<>(Arrays.asList(type.getDeclaredFields()));

        result.addAll(getFieldsNoCache(type.getSuperclass()));

        return result;
    }

    /**
     * 
     * 获取泛型的Class对象
     * 
     * @author kehw_zwei
     * @param type
     *             泛型
     * @param i
     *             泛型类型的位置
     * @return 泛型的Class对象
     * @since JDK 1.8
     */
    public static Class<?> getClass(Type type, int i) {
        if (type instanceof ParameterizedType) { // 处理泛型类型
            return getGenericClass((ParameterizedType) type, i);
        } else if (type instanceof TypeVariable) {
            return getClass(((TypeVariable<?>) type).getBounds()[0], 0); // 处理泛型擦拭对象
        } else {// class本身也是type，强制转型
            return (Class<?>) type;
        }
    }

    /**
     * 
     * 获取泛型的Class对象
     * 
     * @author kehw_zwei
     * @param parameterizedType
     *                          参数类型
     * @param i
     *                          泛型类型位置
     * @return 泛型的Class对象
     * @since JDK 1.8
     */
    public static Class<?> getGenericClass(ParameterizedType parameterizedType, int i) {
        Object genericClass = parameterizedType.getActualTypeArguments()[i];
        if (genericClass instanceof ParameterizedType) { // 处理多级泛型
            return (Class<?>) ((ParameterizedType) genericClass).getRawType();
        } else if (genericClass instanceof GenericArrayType) { // 处理数组泛型
            return (Class<?>) ((GenericArrayType) genericClass).getGenericComponentType();
        } else if (genericClass instanceof TypeVariable) { // 处理泛型擦拭对象
            return (Class<?>) getClass(((TypeVariable<?>) genericClass).getBounds()[0], 0);
        } else {
            return (Class<?>) genericClass;
        }
    }

    public static boolean isClassExist(String className) {
        try {
            Class.forName(className);
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }
}
