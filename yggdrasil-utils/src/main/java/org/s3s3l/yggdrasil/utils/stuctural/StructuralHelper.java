package org.s3s3l.yggdrasil.utils.stuctural;

import java.io.File;
import java.io.InputStream;
import java.net.URL;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;

/**
 * <p>
 * </p>
 * ClassName: StructuralHelper <br>
 * Date: Dec 8, 2016 3:23:36 PM <br>
 * 
 * @author kehw_zwei
 * @version 1.0.0
 * @since JDK 1.8
 */
public interface StructuralHelper {

    /**
     * 
     * 返回对象的结构化字节数组
     * 
     * @param value
     * @return
     * @since JDK 1.8
     */
    byte[] toStructuralBytes(Object value);

    /**
     * 
     * 返回对象的结构化字符串
     * 
     * @author kehw_zwei
     * @param obj
     *            源对象
     * @return json字符串
     * @since JDK 1.8
     */
    String toStructuralString(Object obj);

    /**
     * 
     * 将对象的结构化字符串写入指定文件
     * 
     * @author kehw_zwei
     * @param file
     *            json字符串输出文件
     * @param obj
     *            源对象
     * @since JDK 1.8
     */
    void toStructuralString(File file, Object obj);

    /**
     * 
     * 将json字节数组化为指定对象
     * 
     * @author kehw_zwei
     * @param bytes
     *            源字符数组
     * @param cls
     *            目标类型
     * @return json字符串转化后的指定对象实例
     * @since JDK 1.8
     */
    <T> T toObject(byte[] bytes, Class<T> cls);

    /**
     * 
     * 将json字节数组化为指定对象
     * 
     * @author kehw_zwei
     * @param bytes
     *            源字符数组
     * @param type
     *            目标类型
     * @return json字符串转化后的指定对象实例
     * @since JDK 1.8
     */
    <T> T toObject(byte[] bytes, TypeReference<T> type);

    /**
     * 
     * 将json字符串转化为指定对象
     * 
     * @author kehw_zwei
     * @param str
     *            源字符串
     * @param cls
     *            目标类型
     * @return json字符串转化后的指定对象实例
     * @since JDK 1.8
     */
    <T> T toObject(String str, Class<T> cls);

    /**
     * 
     * 将json字符串转化为指定对象
     * 
     * @author kehw_zwei
     * @param str
     *            源字符串
     * @param type
     *            目标类型
     * @return json字符串转化后的指定对象实例
     * @since JDK 1.8
     */
    <T> T toObject(String str, JavaType type);

    /**
     * 
     * 将json字符串转化为指定对象
     * 
     * @author kehw_zwei
     * @param is
     *            源字符串
     * @param cls
     *            目标类型
     * @return json字符串转化后的指定对象实例
     * @since JDK 1.8
     */
    <T> T toObject(InputStream is, Class<T> cls);

    /**
     * 
     * 将json字符串转化为指定对象
     * 
     * @author kehw_zwei
     * @param is
     *            源字符串
     * @param type
     *            目标类型
     * @return json字符串转化后的指定对象实例
     * @since JDK 1.8
     */
    <T> T toObject(InputStream is, JavaType type);

    /**
     * 
     * 将json字符串转化为指定对象
     * 
     * @author kehw_zwei
     * @param url
     *            源字符串所在url
     * @param cls
     *            目标类型
     * @param <T>
     *            目标类型
     * @return json字符串转化后的指定对象实例
     * @since JDK 1.8
     */
    <T> T toObject(URL url, Class<T> cls);

    /**
     * 
     * 将json字符串转化为指定对象
     * 
     * @author kehw_zwei
     * @param file
     *            源字符串所在文件
     * @param cls
     *            目标类型
     * @param <T>
     *            目标类型
     * @return json字符串转化后的指定对象实例
     * @since JDK 1.8
     */
    <T> T toObject(File file, Class<T> cls);

    /**
     * 
     * 将json字符串转化为指定对象
     * 
     * @author kehw_zwei
     * @param str
     *            源字符串
     * @param type
     *            目标类型
     * @param <T>
     *            目标类型
     * @return json字符串转化后的指定对象实例
     * @since JDK 1.8
     */
    <T> T toObject(String str, TypeReference<T> type);

    /**
     * 
     * 将json字符串转化为指定对象
     * 
     * @author kehw_zwei
     * @param url
     *            源字符串所在url
     * @param type
     *            目标类型
     * @param <T>
     *            目标类型
     * @return json字符串转化后的指定对象实例
     * @since JDK 1.8
     */
    <T> T toObject(URL url, TypeReference<T> type);

    /**
     * 
     * 将json字符串转化为指定对象
     * 
     * @author kehw_zwei
     * @param file
     *            源字符串所在文件
     * @param type
     *            目标类型
     * @param <T>
     *            目标类型
     * @return json字符串转化后的指定对象实例
     * @since JDK 1.8
     */
    <T> T toObject(File file, TypeReference<T> type);

    /**
     * 
     * 将json字符串转化为指定对象
     * 
     * @author kehw_zwei
     * @param is
     *            源字符串
     * @param type
     *            目标类型
     * @param <T>
     *            目标类型
     * @return json字符串转化后的指定对象实例
     * @since JDK 1.8
     */
    <T> T toObject(InputStream is, TypeReference<T> type);

}
