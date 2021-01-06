package org.s3s3l.yggdrasil.utils.reflect;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

import org.s3s3l.yggdrasil.utils.reflect.exception.ReflectException;
import org.s3s3l.yggdrasil.utils.verify.Verify;

public class PropertyDescriptorReflectionBean implements ReflectionBean {

    private static final Map<Class<?>, Map<String, PropertyDescriptor>> TYPE_PROP_META = new ConcurrentHashMap<>();

    private final Object target;
    private final Class<?> type;
    private Map<String, PropertyDescriptor> propMap = new ConcurrentHashMap<>();

    public PropertyDescriptorReflectionBean(Object target) {
        Verify.notNull(target);
        this.target = target;
        this.type = target.getClass();
        refresh();
    }

    private void refresh() {
        Map<String, PropertyDescriptor> propMeta = TYPE_PROP_META.get(type);
        if (propMeta == null) {
            try {
                propMeta = new ConcurrentHashMap<>();
                for (Field field : ReflectionUtils.getFields(type)) {
                    propMeta.put(field.getName(), new PropertyDescriptor(field.getName(), type));
                }
                TYPE_PROP_META.put(type, propMeta);
            } catch (IntrospectionException e) {
                throw new ReflectException(e);
            }
        }
        propMap = propMeta;
    }

    @Override
    public boolean hasField(String fieldName) {
        return propMap.containsKey(fieldName);
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
