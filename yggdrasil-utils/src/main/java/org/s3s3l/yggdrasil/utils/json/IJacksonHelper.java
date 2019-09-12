package org.s3s3l.yggdrasil.utils.json;

import java.io.File;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.Map.Entry;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.base.CaseFormat;

/**
 * <p>
 * </p>
 * ClassName:IJacksonHelper <br>
 * Date: Dec 8, 2016 5:43:07 PM <br>
 * 
 * @author kehw_zwei
 * @version 1.0.0
 * @since JDK 1.8
 */
public interface IJacksonHelper extends IJsonHelper {
    
    IJacksonHelper include(Include include);

    /**
     * 
     * configure object mapper
     * 
     * @param feature
     *            feature
     * @param state
     *            true or false
     * @return this instance
     * @since JDK 1.8
     */
    IJacksonHelper configure(DeserializationFeature feature, boolean state);

    /**
     * 
     * configure object mapper
     * 
     * @param feature
     *            feature
     * @param state
     *            true or false
     * @return this instance
     * @since JDK 1.8
     */
    IJacksonHelper configure(SerializationFeature feature, boolean state);

    /**
     * 
     * configure object mapper
     * 
     * @param feature
     *            feature
     * @param state
     *            true or false
     * @return this instance
     * @since JDK 1.8
     */
    IJacksonHelper configure(MapperFeature feature, boolean state);
    
    IJacksonHelper setPropertyNamingStrategy(PropertyNamingStrategy pns);

    JavaType getJavaType(Type type);

    /**
     * 
     * 创建对象节点
     * 
     * @author kehw_zwei
     * @return 空的TreeNode对象
     * @since JDK 1.8
     */
    ObjectNode createObjectNode();

    /**
     * 
     * 创建数组节点
     * 
     * @author kehw_zwei
     * @return 空的ArrayNode对象
     * @since JDK 1.8
     */
    ArrayNode createArrayNode();

    /**
     * 
     * 将json字符串转化为JsonNode对象
     * 
     * @author kehw_zwei
     * @param str
     *            源字符串
     * @return 输入json对应的根节点TreeNode对象
     * @since JDK 1.8
     */
    JsonNode toTreeNode(String str);

    /**
     * 
     * 将json字符串流转化为JsonNode对象
     * 
     * @author kehw_zwei
     * @param stream
     *            源字符串流
     * @return 输入json对应的根节点TreeNode对象
     * @since JDK 1.8
     */
    JsonNode toTreeNode(InputStream stream);

    /**
     * 
     * 将json字符数组转化为JsonNode对象
     * 
     * @author kehw_zwei
     * @param bytes
     *            源字符数组
     * @return 输入json对应的根节点TreeNode对象
     * @since JDK 1.8
     */
    JsonNode toTreeNode(byte[] bytes);

    /**
     * 
     * 将对象转化为指定对象
     * 
     * @author kehw_zwei
     * @param from
     *            源对象
     * @param toCls
     *            目标类型
     * @param <T>
     *            目标类型
     * @return 转化后的指定对象实例
     * @since JDK 1.8
     */
    <T> T convert(Object from, Class<T> toCls);

    /**
     * 
     * 将对象转化为指定对象
     * 
     * @author kehw_zwei
     * @param from
     *            源对象
     * @param type
     *            目标类型
     * @param <T>
     *            目标类型
     * @return 转化后的指定对象实例
     * @since JDK 1.8
     */
    <T> T convert(Object from, TypeReference<T> type);

    /**
     * 
     * 设定输出格式化之后的json字符串
     * 
     * @author kehw_zwei
     * @return 设定为输出格式化json的JacksonUtils对象
     * @since JDK 1.8
     */
    IJacksonHelper prettyPrinter();

    /**
     * 
     * update target fields
     * 
     * @param origin
     *            origin object
     * @param overrideSource
     *            resource for update
     * @return updated object; the fields in overrideSource will override origin
     *         object`s field
     * @since JDK 1.8
     */
    <T> T update(T origin, String overrideSource);

    /**
     * 
     * update target fields
     * 
     * @param origin
     *            origin object
     * @param overrideSource
     *            resource for update
     * @return updated object; the fields in overrideSource will override origin
     *         object`s field
     * @since JDK 1.8
     */
    <T> T update(T origin, File overrideSource);

    /**
     * 
     * update target fields
     * 
     * @param origin
     *            origin object
     * @param overrideSource
     *            resource for update
     * @return updated object; the fields in overrideSource will override origin
     *         object`s field
     * @since JDK 1.8
     */
    <T> T update(T origin, URL overrideSource);

    /**
     * 
     * update target fields
     * 
     * @param origin
     *            origin object
     * @param overrideSource
     *            resource for update
     * @return updated object; the fields in overrideSource will override origin
     *         object`s field
     * @since JDK 1.8
     */
    <T> T update(T origin, InputStream overrideSource);

    /**
     * 
     * update target fields
     * 
     * @param origin
     *            origin object
     * @param overrideSource
     *            resource for update
     * @return updated object; the fields in overrideSource will override origin
     *         object`s field
     * @since JDK 1.8
     */
    <T> T update(T origin, JsonNode overrideSource);

    /**
     * 
     * update target fields
     * 
     * @param origin
     *            origin object
     * @param overrideSource
     *            resource for update
     * @param type
     *            object type
     * @return combined object; the non-empty fields in overrideSource will
     *         override origin object`s field
     * @since JDK 1.8
     */
    <T> T combine(T origin, T overrideSource, Class<T> type);

    /**
     * 
     * Convert {@link TreeNode} to Object.
     * 
     * @param <T>
     *            Customer type.
     * @param node
     *            TreeNode.
     * @param type
     *            Type class.
     * @return Instance of customer type.
     * @since JDK 1.8
     */
    <T> T treeToValue(TreeNode node, Class<T> type);

    /**
     * 
     * Convert {@link JsonNode} to Object.
     * 
     * @param <T>
     *            Customer type.
     * @param node
     *            JsonNode.
     * @param type
     *            Type class.
     * @return Instance of customer type.
     * @since JDK 1.8
     */
    <T> T nodeToValue(JsonNode node, TypeReference<T> type);

    /**
     * 
     * Convert Object to {@link TreeNode}.
     * 
     * @param origin
     *            Origin object.
     * @return {@link TreeNode} for origin object.
     * @since JDK 1.8
     */
    JsonNode valueToTree(Object origin);

    /**
     * 
     * 转化字段名称格式
     * 
     * @author kehw_zwei
     * @param node
     *            json对象节点
     * @param srcFormat
     *            源格式
     * @param targetFormat
     *            目标格式
     * @return json对象根节点
     * @since JDK 1.8
     */
    JsonNode convertFieldNameCase(JsonNode node, CaseFormat srcFormat, CaseFormat targetFormat);

    /**
     * 
     * 转化字段名称格式
     * 
     * @author kehw_zwei
     * @param field
     *            json对象字段
     * @param srcFormat
     *            源格式
     * @param targetFormat
     *            目标格式
     * @return json对象根节点
     * @since JDK 1.8
     */
    JsonNode convertFieldNameCase(Entry<String, JsonNode> field, CaseFormat srcFormat, CaseFormat targetFormat);

}
