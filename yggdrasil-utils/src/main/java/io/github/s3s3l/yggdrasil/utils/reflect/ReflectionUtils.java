package io.github.s3s3l.yggdrasil.utils.reflect;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.fasterxml.jackson.core.type.TypeReference;

import io.github.s3s3l.yggdrasil.annotation.FromJson;
import io.github.s3s3l.yggdrasil.annotation.FromObject;
import io.github.s3s3l.yggdrasil.utils.collection.CollectionUtils;
import io.github.s3s3l.yggdrasil.utils.common.StringUtils;
import io.github.s3s3l.yggdrasil.utils.reflect.exception.ReflectException;
import io.github.s3s3l.yggdrasil.utils.stuctural.jackson.JacksonUtils;
import io.github.s3s3l.yggdrasil.utils.verify.Verify;
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
    private static final char FIELD_SPLIT_TOKEN = '.';
    private static final char ARRAY_START_TOKEN = '[';
    private static final char ARRAY_END_TOKEN = ']';

    public static final Map<Class<?>, Set<Field>> FIELD_CACHE = new ConcurrentHashMap<>();

    /**
     * 从{@code src}<b>浅拷贝</b>属性到{@code target} <br>
     * {@code src}中需要有对应的{@code getter}方法，{@code target}中需要有对应的{@code setter}方法
     *
     * @param src
     *               原对象
     * @param target
     *               目标对象
     */
    public static void copyProperties(Object src, Object target) {
        try {
            ReflectionBean srcReflection = new PropertyDescriptorReflectionBean(src);
            ReflectionBean targetReflection = new PropertyDescriptorReflectionBean(target);
            for (String fieldName : srcReflection.getFields()) {
                if (!targetReflection.hasField(fieldName)) {
                    continue;
                }
                targetReflection.setFieldValue(fieldName, srcReflection.getFieldValue(fieldName));
            }
        } catch (SecurityException | IllegalArgumentException e) {
            throw new ReflectException(e);
        }
    }

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
            T result = type.getConstructor()
                    .newInstance();
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

    public static <T> T deepClone(Object src, Class<T> type) {
        if (isSampleType(type)) {
            return type.cast(src);
        }

        try {
            ReflectionBean srcReflection = new PropertyDescriptorReflectionBean(src);
            T result = type.getConstructor()
                    .newInstance();
            ReflectionBean targetReflection = new PropertyDescriptorReflectionBean(result);
            for (String field : srcReflection.getFields()) {
                var fieldType = srcReflection.getFieldType(field);
                Object fieldValue = srcReflection.getFieldValue(field);
                if (fieldValue == null) {
                    continue;
                }

                if (isSampleType(fieldType)) {
                    targetReflection.setFieldValue(field, fieldValue);
                } else {
                    targetReflection.setFieldValue(field, deepClone(fieldValue, fieldType));
                }
            }

            return result;
        } catch (InstantiationException | IllegalAccessException | SecurityException | IllegalArgumentException
                | InvocationTargetException | NoSuchMethodException e) {
            throw new ReflectException(e);
        }
    }

    private static boolean isSampleType(Class<?> cls) {
        return cls.isPrimitive() || cls == String.class || Number.class.isAssignableFrom(cls)
                || Boolean.class.isAssignableFrom(cls) || Character.class.isAssignableFrom(cls)
                || Enum.class.isAssignableFrom(cls);
    }

    public static boolean isAnnotationedWith(Class<?> cls, Class<? extends Annotation> annotationClass) {
        return isAnnotationedWiths(cls, annotationClass);
    }

    /**
     * 
     * 判断类型是否有指定注解（会深入注解中的注解进行判断）
     * 
     * @param cls
     *                          类型
     * @param annotationClasses
     *                          注解类型
     * @return true：类型有指定注解；false：类型没有指定注解
     * @since JDK 1.8
     */
    @SuppressWarnings({ "unchecked" })
    public static boolean isAnnotationedWiths(Class<?> cls, Class<?>... annotationClasses) {
        if (cls.getPackageName()
                .startsWith("java.lang.annotation")
                || cls.getName()
                        .startsWith("kotlin.annotation")
                || cls.getName()
                        .equals("kotlin.Metadata")) {
            return false;
        }

        for (Class<?> annotationClass : annotationClasses) {
            if (Annotation.class.isAssignableFrom(annotationClass)
                    && cls.isAnnotationPresent((Class<? extends Annotation>) annotationClass)) {
                return true;
            }
        }

        for (Annotation annotation : cls.getAnnotations()) {
            if (isAnnotationedWiths(annotation.annotationType(), annotationClasses)) {
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

    public static Class<?>[] resolveTypeArguments(Class<?> type, Class<?> iface) {
        Class<?> currentType = type;
        Type genericType = type;
        while (currentType != Object.class) {
            if (currentType == iface) {
                return getTypeArguments(genericType);
            }
            Type[] genericInterfaces = currentType.getGenericInterfaces();
            Class<?>[] interfaces = currentType.getInterfaces();
            for (int i = 0; i < interfaces.length; i++) {
                if (interfaces[i] == iface) {
                    return getTypeArguments(genericInterfaces[i]);
                }
            }

            genericType = currentType.getGenericSuperclass();
            currentType = currentType.getSuperclass();
        }

        return new Class<?>[] {};
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
    public static Class<?>[] getTypeArguments(Type type) {
        if (type instanceof ParameterizedType) { // 处理泛型类型
            return getGenericClasses((ParameterizedType) type);
        } else if (type instanceof TypeVariable) {
            return getTypeArguments(((TypeVariable<?>) type).getBounds()[0]); // 处理泛型擦拭对象
        } else {
            return new Class<?>[] {};
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
    public static Class<?>[] getGenericClasses(ParameterizedType parameterizedType) {
        Type[] genericClasses = parameterizedType.getActualTypeArguments();
        return Arrays.stream(genericClasses)
                .flatMap(genericClass -> {
                    if (genericClass instanceof ParameterizedType) { // 处理多级泛型
                        return Stream.of((Class<?>) ((ParameterizedType) genericClass).getRawType());
                    } else if (genericClass instanceof GenericArrayType) { // 处理数组泛型
                        return Stream.of((Class<?>) ((GenericArrayType) genericClass).getGenericComponentType());
                    } else if (genericClass instanceof TypeVariable) { // 处理泛型擦拭对象
                        return Arrays.stream(getTypeArguments(((TypeVariable<?>) genericClass).getBounds()[0]));
                    } else {
                        return Stream.of((Class<?>) genericClass);
                    }
                })
                .toArray(size -> new Class<?>[size]);
    }

    public static boolean isClassExist(String className) {
        try {
            Class.forName(className);
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    public static List<PlaceHolderMeta> resolvePlaceHolder(String placeHolder) {
        List<PlaceHolderMeta> metas = new LinkedList<>();
        PlaceHolderMeta meta = new PlaceHolderMeta();
        StringBuilder field = new StringBuilder();
        StringBuilder arrIndex = new StringBuilder();
        boolean isArray = false;
        for (int i = 0; i < placeHolder.length(); i++) {
            char token = placeHolder.charAt(i);
            switch (token) {
                case FIELD_SPLIT_TOKEN:
                    meta.setFieldName(field.toString());
                    metas.add(meta);
                    meta = new PlaceHolderMeta();
                    field = new StringBuilder();
                    break;
                case ARRAY_START_TOKEN:
                    isArray = true;
                    break;
                case ARRAY_END_TOKEN:
                    meta.setIndex(Integer.parseInt(arrIndex.toString()));
                    meta.setArray(isArray);
                    arrIndex = new StringBuilder();
                    isArray = false;
                    break;
                default:
                    if (isArray) {
                        arrIndex.append(token);
                    } else {
                        field.append(token);
                    }
                    break;
            }
        }
        meta.setFieldName(field.toString());
        metas.add(meta);

        return metas;
    }

    @SuppressWarnings("rawtypes")
    private static Object checkAndGetObject(PlaceHolderMeta meta, Object value) {
        if (!meta.isArray()) {
            return value;
        }

        Class<?> type = value.getClass();

        if (type.isArray()) {
            return ((Object[]) value)[meta.getIndex()];
        } else if (value instanceof Collection) {
            return ((Collection) value).toArray()[meta.getIndex()];
        }

        throw new ReflectException("relace holder not found. " + meta);
    }

    public static Object getObject(PlaceHolderMeta meta, Map<String, Object> src) {
        Object value = src.get(meta.getFieldName());
        return checkAndGetObject(meta, value);
    }

    public static Object getObject(PlaceHolderMeta meta, Object src) {
        ReflectionBean rf = new PropertyDescriptorReflectionBean(src);

        Object value = rf.getFieldValue(meta.getFieldName());
        return checkAndGetObject(meta, value);
    }

    public static List<Field> getFieldsAnnotatedWith(Class<?> type, Class<? extends Annotation> annotationType) {
        return getFields(type).stream()
                .filter(field -> field.isAnnotationPresent(annotationType))
                .collect(Collectors.toList());
    }

    /**
     * 
     * 获取实现了指定接口的集合
     * 
     * @param interfaceClass
     *                       接口类型
     * @param classes
     *                       类型列表
     * @return 实现了指定接口的类型列表
     * @since JDK 1.8
     */
    public static List<Class<?>> getClassesImplement(Class<?> interfaceClass,
            Collection<Class<? extends Object>> classes) {
        Verify.notNull(interfaceClass, "interfaceClass can not be NULL");
        Verify.notNull(classes, "classes can not be NULL");
        Verify.isInterface(interfaceClass, "interfaceClass must be a interface");
        return classes.stream()
                .filter(r -> interfaceClass.isAssignableFrom(r))
                .distinct()
                .collect(Collectors.toList());
    }

    public static Set<Class<?>> getAllParentTypes(Class<?> type) {
        Set<Class<?>> subTypes = new HashSet<>();
        if (type == Object.class) {
            return subTypes;
        }
        subTypes.add(type);
        Class<?> superclass = type.getSuperclass();
        if (superclass != null) {
            subTypes.addAll(getAllParentTypes(superclass));
        }
        for (Class<?> iface : type.getInterfaces()) {
            subTypes.addAll(getAllParentTypes(iface));
        }
        return subTypes;
    }
}
