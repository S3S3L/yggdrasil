package io.github.s3s3l.yggdrasil.utils.common;

import java.lang.reflect.Field;

import io.github.s3s3l.yggdrasil.bean.condition.Choosable;
import io.github.s3s3l.yggdrasil.utils.reflect.PropertyDescriptorReflectionBean;
import io.github.s3s3l.yggdrasil.utils.reflect.ReflectionBean;
import io.github.s3s3l.yggdrasil.utils.reflect.ReflectionUtils;

/**
 * <p>
 * </p>
 * ClassName:ChoosableHelper <br>
 * Date: Oct 30, 2017 5:12:48 PM <br>
 * 
 * @author kehw_zwei
 * @version 1.0.0
 * @since JDK 1.8
 */
public class ChoosableHelper {

    public static void applyChoosableCondition(Object condition, Object source) throws IllegalArgumentException,
            IllegalAccessException {
        ReflectionBean ref = new PropertyDescriptorReflectionBean(condition);
        ReflectionBean srcRef = new PropertyDescriptorReflectionBean(source);
        for (Field field : ReflectionUtils.getFields(condition.getClass())) {
            if (!Choosable.class.isAssignableFrom(field.getType())) {
                continue;
            }
            Choosable<?> c = (Choosable<?>) ref.getFieldValue(field.getName());
            if (null == c || !c.isEnable()) {
                continue;
            }
            srcRef.setFieldValue(field.getName(), c.getData());
        }

    }
}
