package io.github.s3s3l.yggdrasil.utils.reflect;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

import io.github.s3s3l.yggdrasil.utils.reflect.exception.ReflectException;
import io.github.s3s3l.yggdrasil.utils.verify.Verify;

public class PropertyDescriptorReflectionBean implements ReflectionBean {

    private static final Map<Class<?>, Map<String, PropertyDescriptor>> TYPE_PROP_META = new ConcurrentHashMap<>();

    private final Object target;
    private final Class<?> type;
    private Map<String, PropertyDescriptor> propMap = new ConcurrentHashMap<>();

    public static void warmup(Class<?>... types) {
        for (Class<?> type : types) {
            resolveType(type);
        }
    }

    public PropertyDescriptorReflectionBean(Object target) {
        Verify.notNull(target);
        this.target = target;
        this.type = target.getClass();
        propMap = resolveType(type);
    }

    private static Map<String, PropertyDescriptor> resolveType(Class<?> type) {
        return TYPE_PROP_META.computeIfAbsent(type, t -> {
            Map<String, PropertyDescriptor> propMeta = new ConcurrentHashMap<>();
            for (Field field : ReflectionUtils.getFields(type)) {
                try {
                    propMeta.put(field.getName(), new PropertyDescriptor(field.getName(), type));
                } catch (IntrospectionException e) {
                    continue;
                } catch (Exception e) {
                    throw new ReflectException(e);
                }
            }

            return propMeta;
        });
    }

    @Override
    public Collection<String> getFields() {
        return propMap.keySet();
    }

    @Override
    public boolean hasField(String fieldName) {
        return propMap.containsKey(fieldName);
    }

    @Override
    public Class<?> getFieldType(String fieldName) {
        return findField(fieldName).getPropertyType();
    }

    @Override
    public Object getFieldValue(String fieldName) {
        PropertyDescriptor pd = findField(fieldName);

        try {
            return pd.getReadMethod()
                    .invoke(target);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            throw new ReflectException(e);
        }
    }

    @Override
    public void setFieldValue(String fieldName, Object fieldValue) {
        PropertyDescriptor pd = findField(fieldName);

        try {
            pd.getWriteMethod()
                    .invoke(target, fieldValue);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            throw new ReflectException(e);
        }
    }

    @Override
    public void fill(Properties prop) {
        Verify.notNull(prop);
        for (Entry<Object, Object> entry : prop.entrySet()) {
            String key = entry.getKey()
                    .toString();
            if (hasField(key)) {
                setFieldValue(key, entry.getValue());
            }
        }
    }

    @Override
    public void fill(Map<String, Object> map) {
        Verify.notNull(map);
        for (Entry<String, Object> entry : map.entrySet()) {
            String key = entry.getKey();
            if (hasField(key)) {
                setFieldValue(key, entry.getValue());
            }
        }
    }

    private PropertyDescriptor findField(String fieldName) {
        PropertyDescriptor pd = propMap.get(fieldName);
        if (pd == null) {
            throw new ReflectException("field '" + fieldName + "'not found.");
        }

        return pd;
    }

}
