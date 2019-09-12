package org.s3s3l.yggdrasil.utils.json;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.s3s3l.yggdrasil.bean.exception.JsonException;
import org.s3s3l.yggdrasil.utils.common.StringUtils;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.NumericNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.ValueNode;
import com.google.common.base.CaseFormat;

/**
 * ClassName:JacksonUtils <br>
 * Date: 2016年1月8日 下午2:21:46 <br>
 * 
 * @author kehw_zwei
 * @version 1.0
 * @since JDK 1.8
 */
public class JacksonUtils implements IJacksonHelper {
    private JsonFactory factory;
    private ObjectMapper mapper;
    private ObjectWriter writer;
    private static final String FIELD_NAME_SPLITOR = ".";

    public static final IJacksonHelper defaultHelper = JacksonUtils.create()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    public static final IJacksonHelper nonNull = JacksonUtils.create()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .include(Include.NON_NULL);

    public static Object getValue(ValueNode vn) {
        if (vn.isNull()) {
            return null;
        } else if (vn.isBoolean()) {
            return vn.asBoolean();
        } else if (vn.isNumber()) {
            return ((NumericNode) vn).numberValue();
        } else {
            return vn.asText();
        }
    }

    public static Map<String, TreeNode> flatValues(TreeNode node) {
        return flatValues(StringUtils.EMPTY_STRING, node);
    }

    public static Map<String, TreeNode> flatValues(String prefix, TreeNode node) {
        Map<String, TreeNode> map = new HashMap<>();
        if (node == null) {
            return map;
        }

        if (node.isArray() || node.isValueNode()) {
            map.put(prefix, node);
            return map;
        } else if (!node.isObject()) {
            return map;
        }

        ObjectNode on = (ObjectNode) node;
        if (!StringUtils.isEmpty(prefix)) {
            map.put(prefix, on);
        }
        Iterator<Entry<String, JsonNode>> iterator = on.fields();
        while (iterator.hasNext()) {
            Entry<String, JsonNode> entry = iterator.next();
            map.putAll(flatValues(StringUtils.isEmpty(prefix) ? entry.getKey()
                    : String.join(FIELD_NAME_SPLITOR, prefix, entry.getKey()), entry.getValue()));
        }

        return map;
    }

    public JacksonUtils() {
        this.mapper = new ObjectMapper().findAndRegisterModules();
        this.factory = new JsonFactory();
        this.writer = mapper.writer();
    }

    /**
     * 
     * Creates a new instance of JacksonUtil.
     * 
     * @param factory
     *            a factory instance
     */
    private JacksonUtils(JsonFactory factory) {
        this.factory = factory;
        this.mapper = new ObjectMapper(this.factory);
        this.writer = mapper.writer();
    }

    /**
     * 
     * 创建新实例
     * 
     * @author kehw_zwei
     * @return JacksonUtils实例
     * @since JDK 1.8
     */
    public static JacksonUtils create() {
        return new JacksonUtils();
    }

    /**
     * 
     * 创建新实例
     * 
     * @param factory
     *            a factory instance
     * @return JacksonUtil实例
     * @since JDK 1.8
     */
    public static IJacksonHelper create(JsonFactory factory) {
        return new JacksonUtils(factory);
    }

    @Override
    public IJacksonHelper include(Include include) {
        this.mapper.setSerializationInclusion(include);
        this.writer = this.mapper.writer();
        return this;
    }

    /**
     * @see org.s3s3l.yggdrasil.utils.json.IJacksonHelper#configure(com.fasterxml.jackson.databind.DeserializationFeature,
     *      boolean)
     */
    @Override
    public IJacksonHelper configure(DeserializationFeature feature, boolean state) {
        this.mapper.configure(feature, state);
        this.writer = this.mapper.writer();
        return this;
    }

    /**
     * @see org.s3s3l.yggdrasil.utils.json.IJacksonHelper#configure(com.fasterxml.jackson.databind.SerializationFeature,
     *      boolean)
     */
    @Override
    public IJacksonHelper configure(SerializationFeature feature, boolean state) {
        this.mapper.configure(feature, state);
        this.writer = this.mapper.writer();
        return this;
    }

    @Override
    public IJacksonHelper configure(MapperFeature feature, boolean state) {
        this.mapper.configure(feature, state);
        this.writer = this.mapper.writer();
        return this;
    }

    @Override
    public IJacksonHelper setPropertyNamingStrategy(PropertyNamingStrategy pns) {
        this.mapper.setPropertyNamingStrategy(pns);
        this.writer = this.mapper.writer();
        return this;
    }

    /**
     * @see org.s3s3l.yggdrasil.utils.json.IJacksonHelper#getJavaType(java.lang.reflect.Type)
     */
    @Override
    public JavaType getJavaType(Type type) {
        return this.mapper.getTypeFactory()
                .constructType(type);
    }

    /**
     * @see org.s3s3l.yggdrasil.utils.json.IJacksonHelper#createObjectNode()
     */
    @Override
    public ObjectNode createObjectNode() {
        return mapper.createObjectNode();
    }

    /**
     * @see org.s3s3l.yggdrasil.utils.json.IJacksonHelper#createArrayNode()
     */
    @Override
    public ArrayNode createArrayNode() {
        return mapper.createArrayNode();
    }

    /**
     * @see org.s3s3l.yggdrasil.utils.json.IJsonHelper#toJsonString(java.lang.Object)
     */
    @Override
    public String toJsonString(Object obj) {
        try {
            return this.writer.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            return StringUtils.EMPTY_STRING;
        }
    }

    /**
     * @see org.s3s3l.yggdrasil.utils.json.IJsonHelper#toJsonString(java.io.File,
     *      java.lang.Object)
     */
    @Override
    public void toJsonString(File file, Object obj) {
        try {
            if (!file.exists() && !file.createNewFile()) {
                throw new IOException("Fail to create file.");
            }
            this.writer.writeValue(file, obj);
        } catch (IOException e) {
            throw new JsonException(e);
        }
    }

    /**
     * @see org.s3s3l.yggdrasil.utils.json.IJacksonHelper#toTreeNode(java.lang.String)
     */
    @Override
    public JsonNode toTreeNode(String str) {
        try {
            JsonParser parser = factory.createParser(str);
            return mapper.readTree(parser);
        } catch (IOException e) {
            throw new JsonException(e);
        }
    }

    /**
     * @see org.s3s3l.yggdrasil.utils.json.IJacksonHelper#toTreeNode(java.io.InputStream)
     */
    @Override
    public JsonNode toTreeNode(InputStream stream) {
        try {
            JsonParser parser = factory.createParser(stream);
            return mapper.readTree(parser);
        } catch (IOException e) {
            throw new JsonException(e);
        }
    }

    @Override
    public JsonNode toTreeNode(byte[] bytes) {
        try {
            JsonParser parser = factory.createParser(bytes);
            return mapper.readTree(parser);
        } catch (IOException e) {
            throw new JsonException(e);
        }
    }

    /**
     * @see org.s3s3l.yggdrasil.utils.json.IJsonHelper#toObject(java.lang.String,
     *      java.lang.Class)
     */
    @Override
    public <T> T toObject(String str, Class<T> cls) {
        try {
            JsonParser parser = factory.createParser(str);
            return mapper.readValue(parser, cls);
        } catch (IOException e) {
            throw new JsonException(e);
        }
    }

    /**
     * @see org.s3s3l.yggdrasil.utils.json.IJsonHelper#toObject(java.lang.String,
     *      com.fasterxml.jackson.databind.JavaType)
     */
    @Override
    public <T> T toObject(String str, JavaType type) {
        try {
            JsonParser parser = factory.createParser(str);
            return mapper.readValue(parser, type);
        } catch (IOException e) {
            throw new JsonException(e);
        }
    }

    /**
     * @see org.s3s3l.yggdrasil.utils.json.IJsonHelper#toObject(java.io.InputStream,
     *      java.lang.Class)
     */
    @Override
    public <T> T toObject(InputStream is, Class<T> cls) {
        try {
            JsonParser parser = factory.createParser(is);
            return mapper.readValue(parser, cls);
        } catch (IOException e) {
            throw new JsonException(e);
        }
    }

    /**
     * @see org.s3s3l.yggdrasil.utils.json.IJsonHelper#toObject(java.io.InputStream,
     *      com.fasterxml.jackson.databind.JavaType)
     */
    @Override
    public <T> T toObject(InputStream is, JavaType type) {
        try {
            JsonParser parser = factory.createParser(is);
            return mapper.readValue(parser, type);
        } catch (IOException e) {
            throw new JsonException(e);
        }
    }

    /**
     * @see org.s3s3l.yggdrasil.utils.json.IJsonHelper#toObject(java.net.URL,
     *      java.lang.Class)
     */
    @Override
    public <T> T toObject(URL url, Class<T> cls) {
        try {
            JsonParser parser = factory.createParser(url);
            return mapper.readValue(parser, cls);
        } catch (IOException e) {
            throw new JsonException(e);
        }
    }

    /**
     * @see org.s3s3l.yggdrasil.utils.json.IJsonHelper#toObject(java.io.File,
     *      java.lang.Class)
     */
    @Override
    public <T> T toObject(File file, Class<T> cls) {
        try {
            JsonParser parser = factory.createParser(file);
            return mapper.readValue(parser, cls);
        } catch (IOException e) {
            throw new JsonException(e);
        }
    }

    /**
     * @see org.s3s3l.yggdrasil.utils.json.IJsonHelper#toObject(java.lang.String,
     *      com.fasterxml.jackson.core.type.TypeReference)
     */
    @Override
    public <T> T toObject(String str, TypeReference<T> type) {
        try {
            JsonParser parser = factory.createParser(str);
            return mapper.readValue(parser, type);
        } catch (IOException e) {
            throw new JsonException(e);
        }
    }

    /**
     * @see org.s3s3l.yggdrasil.utils.json.IJsonHelper#toObject(java.net.URL,
     *      com.fasterxml.jackson.core.type.TypeReference)
     */
    @Override
    public <T> T toObject(URL url, TypeReference<T> type) {
        try {
            JsonParser parser = factory.createParser(url);
            return mapper.readValue(parser, type);
        } catch (IOException e) {
            throw new JsonException(e);
        }
    }

    /**
     * @see org.s3s3l.yggdrasil.utils.json.IJsonHelper#toObject(java.io.File,
     *      com.fasterxml.jackson.core.type.TypeReference)
     */
    @Override
    public <T> T toObject(File file, TypeReference<T> type) {
        try {
            JsonParser parser = factory.createParser(file);
            return mapper.readValue(parser, type);
        } catch (IOException e) {
            throw new JsonException(e);
        }
    }

    /**
     * @see org.s3s3l.yggdrasil.utils.json.IJacksonHelper#convert(java.lang.Object,
     *      java.lang.Class)
     */
    @Override
    public <T> T convert(Object from, Class<T> toCls) {
        if (from == null) {
            return null;
        }
        return mapper.convertValue(from, toCls);
    }

    /**
     * @see org.s3s3l.yggdrasil.utils.json.IJacksonHelper#convert(java.lang.Object,
     *      com.fasterxml.jackson.core.type.TypeReference)
     */
    @Override
    public <T> T convert(Object from, TypeReference<T> type) {
        if (from == null) {
            return null;
        }
        return mapper.convertValue(from, type);
    }

    /**
     * @see org.s3s3l.yggdrasil.utils.json.IJacksonHelper#prettyPrinter()
     */
    @Override
    public JacksonUtils prettyPrinter() {
        this.writer = this.mapper.writerWithDefaultPrettyPrinter();
        return this;
    }

    /**
     * @see org.s3s3l.yggdrasil.utils.json.IJacksonHelper#update(T,
     *      java.lang.String)
     */
    @Override
    public <T> T update(T origin, String overrideSource) {
        ObjectReader updater = this.mapper.readerForUpdating(origin);
        try {
            return updater.readValue(overrideSource);
        } catch (IOException e) {
            throw new JsonException(e);
        }
    }

    /**
     * @see org.s3s3l.yggdrasil.utils.json.IJacksonHelper#update(T,
     *      java.io.File)
     */
    @Override
    public <T> T update(T origin, File overrideSource) {
        ObjectReader updater = this.mapper.readerForUpdating(origin);
        try {
            return updater.readValue(overrideSource);
        } catch (IOException e) {
            throw new JsonException(e);
        }
    }

    /**
     * @see org.s3s3l.yggdrasil.utils.json.IJacksonHelper#update(T,
     *      java.net.URL)
     */
    @Override
    public <T> T update(T origin, URL overrideSource) {
        ObjectReader updater = this.mapper.readerForUpdating(origin);
        try {
            return updater.readValue(overrideSource);
        } catch (IOException e) {
            throw new JsonException(e);
        }
    }

    /**
     * @see org.s3s3l.yggdrasil.utils.json.IJacksonHelper#update(T,
     *      java.io.InputStream)
     */
    @Override
    public <T> T update(T origin, InputStream overrideSource) {
        ObjectReader updater = this.mapper.readerForUpdating(origin);
        try {
            return updater.readValue(overrideSource);
        } catch (IOException e) {
            throw new JsonException(e);
        }
    }

    /**
     * @see org.s3s3l.yggdrasil.utils.json.IJacksonHelper#update(T,
     *      com.fasterxml.jackson.databind.JsonNode)
     */
    @Override
    public <T> T update(T origin, JsonNode overrideSource) {
        ObjectReader updater = this.mapper.readerForUpdating(origin);
        try {
            return updater.readValue(overrideSource);
        } catch (IOException e) {
            throw new JsonException(e);
        }
    }

    /**
     * @see org.s3s3l.yggdrasil.utils.json.IJacksonHelper#combine(T, T,
     *      java.lang.Class)
     */
    @Override
    public <T> T combine(T origin, T overrideSource, Class<T> type) {
        ObjectMapper combineMapper = new ObjectMapper(this.factory);
        combineMapper.setSerializationInclusion(Include.NON_EMPTY);
        combineMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        ObjectReader updater = combineMapper.readerForUpdating(origin);
        try {
            return updater.readValue(valueToTree(overrideSource));
        } catch (IOException e) {
            throw new JsonException(e);
        }
    }

    /**
     * @see org.s3s3l.yggdrasil.utils.json.IJacksonHelper#treeToValue(com.fasterxml.jackson.core.TreeNode,
     *      java.lang.Class)
     */
    @Override
    public <T> T treeToValue(TreeNode node, Class<T> type) {
        try {
            return this.mapper.treeToValue(node, type);
        } catch (IOException e) {
            throw new JsonException(e);
        }
    }

    /**
     * @see org.s3s3l.yggdrasil.utils.json.IJacksonHelper#nodeToValue(com.fasterxml.jackson.databind.JsonNode,
     *      com.fasterxml.jackson.core.type.TypeReference)
     */
    public <T> T nodeToValue(JsonNode node, TypeReference<T> type) {
        try {
            return this.mapper.readerFor(type)
                    .readValue(node);
        } catch (IOException e) {
            throw new JsonException(e);
        }
    }

    /**
     * @see org.s3s3l.yggdrasil.utils.json.IJacksonHelper#valueToTree(java.lang.Object)
     */
    @Override
    public JsonNode valueToTree(Object origin) {
        return this.mapper.valueToTree(origin);
    }

    /**
     * @see org.s3s3l.yggdrasil.utils.json.IJacksonHelper#convertFieldNameCase(com.fasterxml.jackson.databind.JsonNode,
     *      com.google.common.base.CaseFormat,
     *      com.google.common.base.CaseFormat)
     */
    @Override
    public JsonNode convertFieldNameCase(JsonNode node, CaseFormat srcFormat, CaseFormat targetFormat) {
        if (node == null) {
            return null;
        }
        if (node.isArray()) {
            ArrayNode newNode = createArrayNode();
            Iterator<JsonNode> nodes = node.iterator();
            while (nodes.hasNext()) {
                newNode.add(convertFieldNameCase(nodes.next(), srcFormat, targetFormat));
            }
            return newNode;
        } else if (node.isObject()) {
            Iterator<Entry<String, JsonNode>> fields = node.fields();
            ObjectNode newNode = createObjectNode();
            while (fields.hasNext()) {
                Entry<String, JsonNode> subField = fields.next();
                newNode.set(StringUtils.toFormatedString(subField.getKey(), srcFormat, targetFormat),
                        convertFieldNameCase(subField, srcFormat, targetFormat));
            }
            return newNode;
        } else if (node.isValueNode()) {
            return node;
        } else {
            return null;
        }
    }

    /**
     * @see org.s3s3l.yggdrasil.utils.json.IJacksonHelper#convertFieldNameCase(java.util.Map.Entry,
     *      com.google.common.base.CaseFormat,
     *      com.google.common.base.CaseFormat)
     */
    @Override
    public JsonNode convertFieldNameCase(Entry<String, JsonNode> field, CaseFormat srcFormat, CaseFormat targetFormat) {
        if (field == null) {
            return null;
        }
        JsonNode node = field.getValue();
        if (node == null) {
            return null;
        }
        if (node.isArray()) {
            ArrayNode newNode = createArrayNode();
            Iterator<JsonNode> nodes = node.iterator();
            while (nodes.hasNext()) {
                newNode.add(convertFieldNameCase(nodes.next(), srcFormat, targetFormat));
            }
            return newNode;
        } else if (node.isObject()) {
            Iterator<Entry<String, JsonNode>> fields = node.fields();
            ObjectNode newNode = createObjectNode();
            while (fields.hasNext()) {
                Entry<String, JsonNode> subField = fields.next();
                newNode.set(StringUtils.toFormatedString(subField.getKey(), srcFormat, targetFormat),
                        convertFieldNameCase(subField, srcFormat, targetFormat));
            }
            return newNode;
        } else if (node.isValueNode()) {
            return node;
        } else {
            return null;
        }
    }

    @Override
    public byte[] toJsonBytes(Object value) {
        try {
            return this.mapper.writeValueAsBytes(value);
        } catch (JsonProcessingException e) {
            throw new JsonException(e);
        }
    }

    @Override
    public <T> T toObject(byte[] bytes, Class<T> cls) {
        try {
            return this.mapper.readValue(bytes, cls);
        } catch (IOException e) {
            throw new JsonException(e);
        }
    }

    @Override
    public <T> T toObject(byte[] bytes, TypeReference<T> type) {
        try {
            return this.mapper.readValue(bytes, type);
        } catch (IOException e) {
            throw new JsonException(e);
        }
    }

    @Override
    public <T> T toObject(InputStream is, TypeReference<T> type) {
        try {
            return this.mapper.readValue(is, type);
        } catch (IOException e) {
            throw new JsonException(e);
        }
    }

}
