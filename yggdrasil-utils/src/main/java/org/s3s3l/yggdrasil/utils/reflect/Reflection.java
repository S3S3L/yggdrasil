package org.s3s3l.yggdrasil.utils.reflect;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import org.apache.commons.lang3.ClassUtils;
import org.s3s3l.yggdrasil.utils.common.StringUtils;
import org.s3s3l.yggdrasil.utils.reflect.exception.ReflectException;
import org.s3s3l.yggdrasil.utils.verify.Verify;

/**
 * ClassName:ReflectUtils <br>
 * Date: 2016年2月25日 下午6:47:44 <br>
 * 
 * @author kehw_zwei
 * @version 1.0.0
 * @since JDK 1.8
 * @deprecated 各方面性能上
 *             {@code PropertyDescriptorReflectionBean}占优，请用{@code PropertyDescriptorReflectionBean}来替代使用。
 * @see PropertyDescriptorReflectionBean
 */
@Deprecated
public class Reflection<T> implements ReflectionBean {

    private T obj;

    public T getObj() {
        return obj;
    }

    private void setObj(T obj) {
        this.obj = obj;
    }

    private Reflection() {

    }

    public static <T> Reflection<T> create(T obj) {
        Reflection<T> tmp = new Reflection<>();
        tmp.setObj(obj);
        return tmp;
    }

    public boolean hasField(String fieldName) {

        if (StringUtils.isEmpty(fieldName)) {
            return false;
        }

        return Arrays.asList(obj.getClass()
                .getDeclaredFields())
                .stream()
                .anyMatch(r -> r.getName()
                        .equals(fieldName));
    }

    public Object getFieldValue(String fieldName) {
        Object result = null;
        Field field = getField(fieldName);
        if (field != null) {
            try {
                field.setAccessible(true);
                result = field.get(obj);
            } catch (IllegalArgumentException | IllegalAccessException e) {
                throw new ReflectException(e);
            }
        }
        return result;
    }

    public void setFieldValue(String fieldName, Object fieldValue) {
        Field field = getField(fieldName);
        if (field != null) {
            Object value;
            Class<?> type = field.getType();

            if (type.isPrimitive()) {
                type = ClassUtils.primitiveToWrapper(type);
            }
            if (fieldValue == null) {
                value = null;
            } else if (type == Boolean.class) {
                value = Boolean.valueOf(fieldValue.toString());
            } else if (type == Double.class) {
                value = Double.valueOf(fieldValue.toString());
            } else if (type == Float.class) {
                value = Float.valueOf(fieldValue.toString());
            } else if (type == Long.class) {
                value = Long.valueOf(fieldValue.toString());
            } else if (type == Integer.class) {
                value = Integer.valueOf(fieldValue.toString());
            } else if (type == Short.class) {
                value = Short.valueOf(fieldValue.toString());
            } else {
                value = type.cast(fieldValue);
            }
            field.setAccessible(true);

            try {
                field.set(obj, value);
            } catch (IllegalArgumentException | IllegalAccessException e) {
                throw new ReflectException(e);
            }
        }
    }

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

    public void fill(Map<String, Object> map) {
        Verify.notNull(map);
        for (Entry<String, Object> entry : map.entrySet()) {
            String key = entry.getKey();
            if (hasField(key)) {
                setFieldValue(key, entry.getValue());
            }
        }
    }

    /**
     * 利用反射获取指定对象里面的指定属性
     * 
     * @param fieldName
     *            目标属性
     * @return 目标字段值，字段不存在则返回null
     */
    private Field getField(String fieldName) {
        Field field = null;
        for (Class<?> clazz = obj.getClass(); clazz != Object.class; clazz = clazz.getSuperclass()) {
            try {
                field = clazz.getDeclaredField(fieldName);
                break;
            } catch (NoSuchFieldException e) {
                // 这里不用做处理，子类没有该字段可能对应的父类有，都没有就返回null。
            }
        }
        return field;
    }
}
