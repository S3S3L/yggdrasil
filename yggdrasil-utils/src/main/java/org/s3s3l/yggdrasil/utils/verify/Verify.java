package org.s3s3l.yggdrasil.utils.verify;

import java.util.Collection;

import org.s3s3l.yggdrasil.bean.exception.VerifyException;
import org.s3s3l.yggdrasil.utils.common.StringUtils;
import org.s3s3l.yggdrasil.utils.log.base.LogHelper;
import org.slf4j.Logger;

/**
 * ClassName:Verify <br>
 * Date: 2016年5月6日 下午3:41:25 <br>
 * 
 * @author kehw_zwei
 * @version 1.0.0
 * @since JDK 1.8
 */
public abstract class Verify {
    private static final Logger logger = LogHelper.getLogger(Verify.class);

    public static void typeCheck(Object value, Class<?> cls, String message) {
        if (value == null) {
            throw new VerifyException(message);
        }
        if (StringUtils.isEmpty(message)) {
            typeCheck(value, cls);
            return;
        }
        Class<?> type = value.getClass();
        if (!cls.isAssignableFrom(type)) {
            throw new VerifyException(message);
        }
    }

    public static void typeCheck(Object value, Class<?> cls) {
        typeCheck(value, cls,
                String.format("[Verify failed] - the argument must be assignable from [%s]", cls.getName()));
    }

    /**
     * 
     * 校验{@link Class}必须是{@code interface}
     * 
     * @param cls
     *            类
     * @param message
     *            错误信息
     * @since JDK 1.8
     */
    public static void isInterface(Class<?> cls, String message) {
        if (StringUtils.isEmpty(message)) {
            isInterface(cls);
            return;
        }
        if (!cls.isInterface()) {
            throw new VerifyException(message);
        }
    }

    /**
     * 
     * 校验{@link Class}必须是{@code interface}
     * 
     * @param cls
     *            类
     * @since JDK 1.8
     */
    public static void isInterface(Class<?> cls) {
        isInterface(cls, "[Verify failed] - the class argument must be a interface");
    }

    /**
     * 
     * 校验对象必须为空
     * 
     * @param object
     *            对象
     * @param message
     *            错误信息
     * @since JDK 1.8
     */
    public static void isNull(Object object, String message) {
        if (StringUtils.isEmpty(message)) {
            isNull(object);
            return;
        }
        if (object != null) {
            throw new VerifyException(message);
        }
    }

    /**
     * 
     * 校验对象必须为空
     * 
     * @param object
     *            对象
     * @since JDK 1.8
     */
    public static void isNull(Object object) {
        isNull(object, "[Verify failed] - the object argument must be null");
    }

    /**
     * 
     * 校验对象必须不为空
     * 
     * @param object
     *            对象
     * @param message
     *            错误信息
     * @since JDK 1.8
     */
    public static void notNull(Object object, String message) {
        if (StringUtils.isEmpty(message)) {
            notNull(object);
            return;
        }
        if (object == null) {
            throw new VerifyException(message);
        }
    }

    /**
     * 
     * 校验对象必须不为空
     * 
     * @param object
     *            对象
     * @since JDK 1.8
     */
    public static void notNull(Object object) {
        notNull(object, "[Verify failed] - the object argument must not be null");
    }

    public static void hasLength(Object value, String message) {
        if (value == null) {
            throw new VerifyException(message);
        }
        if (StringUtils.isEmpty(message)) {
            hasLength(value);
            return;
        }
        Class<?> type = value.getClass();
        if (String.class.isAssignableFrom(type)) {
            hasLength((String) value, message);
        } else {
            logger.warn("[hasLength] verify only effect on [String] type. Value type [{}]", type.getName());
        }
    }

    public static void hasLength(Object value) {
        hasLength(value, "[Verify failed] - the object argument must has length.");
    }

    /**
     * 
     * 校验字符串必须不为空
     * 
     * @param string
     *            字符串
     * @param message
     *            错误信息
     * @since JDK 1.8
     */
    public static void hasLength(String string, String message) {
        if (StringUtils.isEmpty(message)) {
            hasLength(string);
            return;
        }
        if (string == null || string.length() <= 0) {
            throw new VerifyException(message);
        }
    }

    /**
     * 
     * 校验字符串必须不为空
     * 
     * @param string
     *            字符串
     * @since JDK 1.8
     */
    public static void hasLength(String string) {
        hasLength(string, "[Verify failed] - the string argument must has length.");
    }

    /**
     * 
     * 校验字符串必须有除空格外的其他字符
     * 
     * @param string
     *            字符串
     * @param message
     *            错误信息
     * @since JDK 1.8
     */
    public static void hasText(String string, String message) {
        if (StringUtils.isEmpty(message)) {
            hasText(string);
            return;
        }
        hasLength(string, message);
        if (string.trim()
                .length() <= 0) {
            throw new VerifyException(message);
        }
    }

    /**
     * 
     * 校验字符串必须有除空格外的其他字符
     * 
     * @param string
     *            字符串
     * @since JDK 1.8
     */
    public static void hasText(String string) {
        hasText(string, "[Verify failed] - the string argument must has text.");
    }

    /**
     * 
     * 校验对象非空
     * 
     * @param value
     *            对象
     * @param message
     *            错误信息
     * @since JDK 1.8
     */
    public static void notEmpty(Object value, String message) {
        if (value == null) {
            throw new VerifyException(message);
        }
        if (StringUtils.isEmpty(message)) {
            notEmpty(value);
            return;
        }
        Class<?> type = value.getClass();
        if (Collection.class.isAssignableFrom(type)) {
            notEmpty(Collection.class.cast(value), message);
        } else if (type.isArray()) {
            notEmpty((Object[]) value, message);
        } else {
            logger.warn("[notEmpty] verify only effect on [Collection|Array] type. Value type [{}]", type.getName());
        }
    }

    /**
     * 
     * 校验对象非空
     * 
     * @param value
     *            对象
     * @since JDK 1.8
     */
    public static void notEmpty(Object value) {
        notEmpty(value, "[Verify failed] - the object argument must not be empty.");
    }

    /**
     * 
     * 校验集合非空
     * 
     * @param collection
     *            集合
     * @param message
     *            错误信息
     * @since JDK 1.8
     */
    public static void notEmpty(Collection<?> collection, String message) {
        if (StringUtils.isEmpty(message)) {
            notEmpty(collection);
            return;
        }
        if (collection == null || collection.isEmpty()) {
            throw new VerifyException(message);
        }
    }

    /**
     * 
     * 校验集合非空
     * 
     * @param collection
     *            集合
     * @since JDK 1.8
     */
    public static void notEmpty(Collection<?> collection) {
        notEmpty(collection, "[Verify failed] - the collection argument must not be empty.");
    }

    /**
     * 
     * 校验数组非空
     * 
     * @param array
     *            数组
     * @param message
     *            错误信息
     * @since JDK 1.8
     */
    public static void notEmpty(Object[] array, String message) {
        if (StringUtils.isEmpty(message)) {
            notEmpty(array);
            return;
        }
        if (array == null || array.length <= 0) {
            throw new VerifyException(message);
        }
    }

    /**
     * 
     * 校验数组非空
     * 
     * @param array
     *            数组
     * @since JDK 1.8
     */
    public static void notEmpty(Object[] array) {
        notEmpty(array, "[Verify failed] - the array argument must not be empty.");
    }

    /**
     * 
     * 校验对象为空
     * 
     * @param value
     *            对象
     * @param message
     *            错误信息
     * @since JDK 1.8
     */
    public static void empty(Object value, String message) {
        if (value == null) {
            throw new VerifyException(message);
        }
        if (StringUtils.isEmpty(message)) {
            empty(value);
            return;
        }
        Class<?> type = value.getClass();
        if (Collection.class.isAssignableFrom(type)) {
            empty(Collection.class.cast(value), message);
        } else if (type.isArray()) {
            empty((Object[]) value, message);
        } else {
            logger.warn("[empty] verify only effect on [Collection|Array] type. Value type [{}]", type.getName());
        }
    }

    /**
     * 
     * 校验对象为空
     * 
     * @param value
     *            对象
     * @since JDK 1.8
     */
    public static void empty(Object value) {
        empty(value, "[Verify failed] - the object argument must be empty.");
    }

    /**
     * 
     * 校验集合为空
     * 
     * @param collection
     *            集合
     * @param message
     *            错误信息
     * @since JDK 1.8
     */
    public static void empty(Collection<?> collection, String message) {
        if (StringUtils.isEmpty(message)) {
            empty(collection);
            return;
        }
        if (collection == null || !collection.isEmpty()) {
            throw new VerifyException(message);
        }
    }

    /**
     * 
     * 校验集合为空
     * 
     * @param collection
     *            集合
     * @since JDK 1.8
     */
    public static void empty(Collection<?> collection) {
        empty(collection, "[Verify failed] - the collection argument must be empty.");
    }

    /**
     * 
     * 校验数组为空
     * 
     * @param array
     *            数组
     * @param message
     *            错误信息
     * @since JDK 1.8
     */
    public static void empty(Object[] array, String message) {
        if (StringUtils.isEmpty(message)) {
            empty(array);
            return;
        }
        if (array == null || array.length > 0) {
            throw new VerifyException(message);
        }
    }

    /**
     * 
     * 校验数组为空
     * 
     * @param array
     *            数组
     * @since JDK 1.8
     */
    public static void empty(Object[] array) {
        empty(array, "[Verify failed] - the array argument must be empty.");
    }

    /**
     * 
     * 校验对象大小不超过限制
     * 
     * @param value
     *            对象
     * @param length
     *            大小限制
     * @param message
     *            错误信息
     * @since JDK 1.8
     */
    public static void lenthLimit(Object value, long length, String message) {
        if (value == null) {
            throw new VerifyException(message);
        }
        if (StringUtils.isEmpty(message)) {
            lenthLimit(value, length);
            return;
        }
        Class<?> type = value.getClass();
        if (Collection.class.isAssignableFrom(type)) {
            lenthLimit(Collection.class.cast(value), length, message);
        } else if (type.isArray()) {
            lenthLimit((Object[]) value, length, message);
        } else if (String.class.isAssignableFrom(type)) {
            lenthLimit((String) value, length, message);
        } else {
            logger.warn("[lenthLimit] verify only effect on [Collection|Array|String] type. Value type [{}]",
                    type.getName());
        }
    }

    /**
     * 
     * 校验对象大小不超过限制
     * 
     * @param value
     *            对象
     * @param length
     *            大小限制
     * @since JDK 1.8
     */
    public static void lenthLimit(Object value, long length) {
        lenthLimit(value, length, "[Verify failed] - the object size is out of limit.");
    }

    /**
     * 
     * 校验集合大小不超过限制
     * 
     * @param collection
     *            集合
     * @param limit
     *            集合大小限制
     * @param message
     *            错误信息
     * @since JDK 1.8
     */
    public static void lenthLimit(Collection<?> collection, long limit, String message) {
        if (StringUtils.isEmpty(message)) {
            lenthLimit(collection, limit);
            return;
        }
        if (collection == null || collection.size() > limit) {
            throw new VerifyException(message);
        }
    }

    /**
     * 
     * 校验集合大小不超过限制
     * 
     * @param collection
     *            集合
     * @param limit
     *            集合大小限制
     * @since JDK 1.8
     */
    public static void lenthLimit(Collection<?> collection, long limit) {
        lenthLimit(collection, limit, "[Verify failed] - the collection size is out of limit.");
    }

    /**
     * 
     * 校验数组大小不超过限制
     * 
     * @param array
     *            数组
     * @param limit
     *            数组大小限制
     * @param message
     *            错误信息
     * @since JDK 1.8
     */
    public static void lenthLimit(Object[] array, long limit, String message) {
        if (StringUtils.isEmpty(message)) {
            lenthLimit(array, limit);
            return;
        }
        if (array == null || array.length > limit) {
            throw new VerifyException(message);
        }
    }

    /**
     * 
     * 校验数组大小不超过限制
     * 
     * @param array
     *            数组
     * @param limit
     *            数组大小限制
     * @since JDK 1.8
     */
    public static void lenthLimit(Object[] array, long limit) {
        lenthLimit(array, limit, "[Verify failed] - the array size is out of limit.");
    }

    /**
     * 
     * 校验字符串长度不超过限制
     * 
     * @param string
     *            字符串
     * @param limit
     *            字符串长度限制
     * @param message
     *            错误信息
     * @since JDK 1.8
     */
    public static void lenthLimit(String string, long limit, String message) {
        if (StringUtils.isEmpty(message)) {
            lenthLimit(string, limit);
            return;
        }
        if (string == null || string.length() > limit) {
            throw new VerifyException(message);
        }
    }

    /**
     * 
     * 校验字符串长度不超过限制
     * 
     * @param string
     *            字符串
     * @param limit
     *            字符串长度限制
     * @since JDK 1.8
     */
    public static void lenthLimit(String string, long limit) {
        lenthLimit(string, limit, "[Verify failed] - the string length is out of limit.");
    }

    /**
     * 
     * 校验对象为固定大小
     * 
     * @param value
     *            对象
     * @param length
     *            大小限制
     * @param message
     *            错误信息
     * @since JDK 1.8
     */
    public static void fixedLength(Object value, long length, String message) {
        if (value == null) {
            throw new VerifyException(message);
        }
        if (StringUtils.isEmpty(message)) {
            fixedLength(value, length);
            return;
        }
        Class<?> type = value.getClass();
        if (Collection.class.isAssignableFrom(type)) {
            fixedLength(Collection.class.cast(value), length, message);
        } else if (type.isArray()) {
            fixedLength((Object[]) value, length, message);
        } else if (String.class.isAssignableFrom(type)) {
            fixedLength((String) value, length, message);
        } else {
            logger.warn("[fixedLength] verify only effect on [Collection|Array|String] type. Value type [{}]",
                    type.getName());
        }
    }

    /**
     * 
     * 校验对象为固定大小
     * 
     * @param value
     *            对象
     * @param length
     *            大小限制
     * @since JDK 1.8
     */
    public static void fixedLength(Object value, long length) {
        fixedLength(value, length, String.format("[Verify failed] - the object size must be %d.", length));
    }

    /**
     * 
     * 校验集合为固定大小
     * 
     * @param collection
     *            集合
     * @param length
     *            大小限制
     * @param message
     *            错误信息
     * @since JDK 1.8
     */
    public static void fixedLength(Collection<?> collection, long length, String message) {
        if (StringUtils.isEmpty(message)) {
            fixedLength(collection, length);
            return;
        }
        if (collection == null || collection.size() != length) {
            throw new VerifyException(message);
        }
    }

    /**
     * 
     * 校验集合为固定大小
     * 
     * @param collection
     *            集合
     * @param length
     *            大小限制
     * @since JDK 1.8
     */
    public static void fixedLength(Collection<?> collection, long length) {
        fixedLength(collection, length, String.format("[Verify failed] - the collection size must be %d.", length));
    }

    /**
     * 
     * 校验数组为固定大小
     * 
     * @param array
     *            数组
     * @param length
     *            大小限制
     * @param message
     *            错误信息
     * @since JDK 1.8
     */
    public static void fixedLength(Object[] array, long length, String message) {
        if (StringUtils.isEmpty(message)) {
            fixedLength(array, length);
            return;
        }
        if (array == null || array.length != length) {
            throw new VerifyException(message);
        }
    }

    /**
     * 
     * 校验数组为固定大小
     * 
     * @param array
     *            数组
     * @param length
     *            大小限制
     * @since JDK 1.8
     */
    public static void fixedLength(Object[] array, long length) {
        fixedLength(array, length, String.format("[Verify failed] - the array size must be %d.", length));
    }

    /**
     * 
     * 校验字符串为固定大小
     * 
     * @param array
     *            字符串
     * @param length
     *            大小限制
     * @param message
     *            错误信息
     * @since JDK 1.8
     */
    public static void fixedLength(String string, long length, String message) {
        if (StringUtils.isEmpty(message)) {
            fixedLength(string, length);
            return;
        }
        if (string == null || string.length() != length) {
            throw new VerifyException(message);
        }
    }

    /**
     * 
     * 校验字符串为固定大小
     * 
     * @param array
     *            字符串
     * @param length
     *            大小限制
     * @since JDK 1.8
     */
    public static void fixedLength(String string, long length) {
        fixedLength(string, length, String.format("[Verify failed] - the string length must be %d.", length));
    }

    public static void largerThan(Number src, Number compare, String message) {
        if (src.doubleValue() <= compare.doubleValue()) {
            throw new VerifyException(message);
        }
    }

    public static void largerThan(Number src, Number compare) {
        largerThan(src, compare,
                String.format("[Verify failed] - provided number must be larger than %s, but it is %s", compare, src));
    }

    public static void notLargerThan(Number src, Number compare, String message) {
        if (src.doubleValue() > compare.doubleValue()) {
            throw new VerifyException(message);
        }
    }

    public static void notLargerThan(Number src, Number compare) {
        notLargerThan(src, compare, String
                .format("[Verify failed] - provided number must not be larger than %s, but it is %s", compare, src));
    }

    public static void lessThan(Number src, Number compare, String message) {
        if (src.doubleValue() >= compare.doubleValue()) {
            throw new VerifyException(message);
        }
    }

    public static void lessThan(Number src, Number compare) {
        lessThan(src.doubleValue(), compare.doubleValue(),
                String.format("[Verify failed] - provided number must be less than %s, but it is %s", compare, src));
    }

    public static void notLessThan(Number src, Number compare, String message) {
        if (src.doubleValue() < compare.doubleValue()) {
            throw new VerifyException(message);
        }
    }

    public static void notLessThan(Number src, Number compare) {
        notLessThan(src, compare, String
                .format("[Verify failed] - provided number must not be less than %s, but it is %s", compare, src));
    }

    public static void assertEquals(Object src, Object compare) {
        assertEquals(src, compare,
                String.format("[Verify failed] - provided object must be equated with %s, but it is %s", compare, src));
    }

    public static void assertEquals(Object src, Object compare, String message) {
        if (src == null || !src.equals(compare)) {
            throw new VerifyException(message);
        }
    }

    public static void assertNotEquals(Object src, Object compare) {
        assertNotEquals(src, compare, String
                .format("[Verify failed] - provided object must not be equated with %s, but it is %s", compare, src));
    }

    public static void assertNotEquals(Object src, Object compare, String message) {
        if ((src == null && compare != null) || (src != null && src.equals(compare))) {
            throw new VerifyException(message);
        }
    }
}
