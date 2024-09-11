package io.github.s3s3l.yggdrasil.utils.structural;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Map;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.NullNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.ValueNode;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.google.common.base.CaseFormat;

import io.github.s3s3l.yggdrasil.utils.stuctural.jackson.JacksonHelper;
import io.github.s3s3l.yggdrasil.utils.stuctural.jackson.JacksonUtils;

public class JacksonUtilsTest {

    // Existing tests...

    @Test
    public void testGetValue() {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode node = mapper.createObjectNode();
        node.put("key1", "value");

        Object value1 = JacksonUtils.getValue((ValueNode) node.get("key1"));
        Assertions.assertEquals("value", value1);

        node.put("key2", true);
        Object value2 = JacksonUtils.getValue((ValueNode) node.get("key2"));
        Assertions.assertEquals(true, value2);;

        node.put("key3", 123);
        Object value3 = JacksonUtils.getValue((ValueNode) node.get("key3"));
        Assertions.assertEquals(123, value3);

        node.set("key4", NullNode.getInstance());
        Object value4 = JacksonUtils.getValue((ValueNode) node.get("key4"));
        Assertions.assertNull(value4);;
    }

    @Test
    public void testFlatValues() {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode node = mapper.createObjectNode();
        node.put("key1", "value1");
        node.put("key2", "value2");

        Map<String, TreeNode> flatMap = JacksonUtils.flatValues(node);
        Assertions.assertEquals(2, flatMap.size());
        Assertions.assertTrue(flatMap.containsKey("key1"));
        Assertions.assertTrue(flatMap.containsKey("key2"));
    }

    @Test
    public void testFlatValuesWithPrefix() {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode node = mapper.createObjectNode();
        node.put("key1", "value1");
        node.put("key2", "value2");

        Map<String, TreeNode> flatMap = JacksonUtils.flatValues("prefix", node);
        Assertions.assertEquals(3, flatMap.size());
        Assertions.assertTrue(flatMap.containsKey("prefix.key1"));
        Assertions.assertTrue(flatMap.containsKey("prefix.key2"));
    }

    @Test
    public void testCreate() {
        JacksonHelper jacksonHelper = JacksonUtils.create();
        Assertions.assertNotNull(jacksonHelper);
    }

    @Test
    public void testCreateWithFactory() {
        JacksonHelper jacksonHelper = JacksonUtils.create(new YAMLFactory());
        Assertions.assertNotNull(jacksonHelper);
    }

    @Test
    public void testInclude() {
        JacksonHelper jacksonHelper = JacksonUtils.create();
        jacksonHelper.include(Include.NON_NULL);
        // No exception should be thrown
    }

    @Test
    public void testConfigureDeserializationFeature() {
        JacksonHelper jacksonHelper = JacksonUtils.create();
        jacksonHelper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        // No exception should be thrown
    }

    @Test
    public void testSetPropertyNamingStrategy() {
        JacksonHelper jacksonHelper = JacksonUtils.create();
        jacksonHelper.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);
        // No exception should be thrown
    }

    @Test
    public void testGetJavaType() {
        JacksonHelper jacksonHelper = JacksonUtils.create();
        JavaType javaType = jacksonHelper.getJavaType(String.class);
        Assertions.assertNotNull(javaType);
    }

    @Test
    public void testCreateObjectNode() {
        JacksonHelper jacksonHelper = JacksonUtils.create();
        ObjectNode objectNode = jacksonHelper.createObjectNode();
        Assertions.assertNotNull(objectNode);
    }

    @Test
    public void testCreateArrayNode() {
        JacksonHelper jacksonHelper = JacksonUtils.create();
        ArrayNode arrayNode = jacksonHelper.createArrayNode();
        Assertions.assertNotNull(arrayNode);
    }

    @Test
    public void testToStructuralString() {
        JacksonHelper jacksonHelper = JacksonUtils.create();
        String json = jacksonHelper.toStructuralString(new TestObject("test", 123));
        Assertions.assertNotNull(json);
        Assertions.assertTrue(json.contains("test"));
    }

    @Test
    public void testToStructuralStringToFile() throws IOException {
        JacksonHelper jacksonHelper = JacksonUtils.create();
        File file = File.createTempFile("test", ".json");
        jacksonHelper.toStructuralString(file, new TestObject("test", 123));
        TestObject obj = jacksonHelper.toObject(file, TestObject.class);
        Assertions.assertNotNull(obj);
        Assertions.assertEquals("test", obj.getName());
        Assertions.assertEquals(123, obj.getValue());
    }

    @Test
    public void testToTreeNodeFromString() {
        JacksonHelper jacksonHelper = JacksonUtils.create();
        String json = "{\"name\":\"test\",\"value\":123}";
        JsonNode node = jacksonHelper.toTreeNode(json);
        Assertions.assertNotNull(node);
        Assertions.assertEquals("test", node.get("name").asText());
        Assertions.assertEquals(123, node.get("value").asInt());
    }

    @Test
    public void testToTreeNodeFromInputStream() throws IOException {
        JacksonHelper jacksonHelper = JacksonUtils.create();
        String json = "{\"name\":\"test\",\"value\":123}";
        InputStream is = new ByteArrayInputStream(json.getBytes());
        JsonNode node = jacksonHelper.toTreeNode(is);
        Assertions.assertNotNull(node);
        Assertions.assertEquals("test", node.get("name").asText());
        Assertions.assertEquals(123, node.get("value").asInt());
    }

    @Test
    public void testToTreeNodeFromBytes() {
        JacksonHelper jacksonHelper = JacksonUtils.create();
        String json = "{\"name\":\"test\",\"value\":123}";
        JsonNode node = jacksonHelper.toTreeNode(json.getBytes());
        Assertions.assertNotNull(node);
        Assertions.assertEquals("test", node.get("name").asText());
        Assertions.assertEquals(123, node.get("value").asInt());
    }

    @Test
    public void testToObjectFromString() {
        JacksonHelper jacksonHelper = JacksonUtils.create();
        String json = "{\"name\":\"test\",\"value\":123}";
        TestObject obj = jacksonHelper.toObject(json, TestObject.class);
        Assertions.assertNotNull(obj);
        Assertions.assertEquals("test", obj.getName());
        Assertions.assertEquals(123, obj.getValue());
    }

    @Test
    public void testToObjectFromInputStream() throws IOException {
        JacksonHelper jacksonHelper = JacksonUtils.create();
        String json = "{\"name\":\"test\",\"value\":123}";
        InputStream is = new ByteArrayInputStream(json.getBytes());
        TestObject obj = jacksonHelper.toObject(is, TestObject.class);
        Assertions.assertNotNull(obj);
        Assertions.assertEquals("test", obj.getName());
        Assertions.assertEquals(123, obj.getValue());
    }

    @Test
    public void testToObjectFromURL() throws IOException {
        JacksonHelper jacksonHelper = JacksonUtils.create();
        URL url = new URL("https://jsonplaceholder.typicode.com/todos/1");
        Map<String, Object> obj = jacksonHelper.toObject(url, new TypeReference<Map<String, Object>>() {
        });
        Assertions.assertNotNull(obj);
        Assertions.assertEquals(1, obj.get("id"));
    }

    @Test
    public void testToObjectFromFile() throws IOException {
        JacksonHelper jacksonHelper = JacksonUtils.create();
        File file = File.createTempFile("test", ".json");
        jacksonHelper.toStructuralString(file, new TestObject("test", 123));
        TestObject obj = jacksonHelper.toObject(file, TestObject.class);
        Assertions.assertNotNull(obj);
        Assertions.assertEquals("test", obj.getName());
        Assertions.assertEquals(123, obj.getValue());
    }

    @Test
    public void testToObjectFromTypeReference() {
        JacksonHelper jacksonHelper = JacksonUtils.create();
        String json = "{\"name\":\"test\",\"value\":123}";
        Map<String, Object> obj = jacksonHelper.toObject(json, new TypeReference<Map<String, Object>>() {
        });
        Assertions.assertNotNull(obj);
        Assertions.assertEquals("test", obj.get("name"));
        Assertions.assertEquals(123, obj.get("value"));
    }

    @Test
    public void testConvert() {
        JacksonHelper jacksonHelper = JacksonUtils.create();
        TestObject obj = new TestObject("test", 123);
        Map<String, Object> map = jacksonHelper.convert(obj, new TypeReference<Map<String, Object>>() {
        });
        Assertions.assertNotNull(map);
        Assertions.assertEquals("test", map.get("name"));
        Assertions.assertEquals(123, map.get("value"));
    }

    @Test
    public void testPrettyPrinter() {
        JacksonHelper jacksonHelper = JacksonUtils.create().prettyPrinter();
        String json = jacksonHelper.toStructuralString(new TestObject("test", 123));
        Assertions.assertNotNull(json);
        Assertions.assertTrue(json.contains("test"));
    }

    @Test
    public void testUpdateFromString() {
        JacksonHelper jacksonHelper = JacksonUtils.create();
        TestObject obj = new TestObject("test", 123);
        TestObject updated = jacksonHelper.update(obj, "{\"name\":\"updated\"}");
        Assertions.assertNotNull(updated);
        Assertions.assertEquals("updated", updated.getName());
        Assertions.assertEquals(123, updated.getValue());
    }

    @Test
    public void testCombine() {
        JacksonHelper jacksonHelper = JacksonUtils.create();
        TestObject obj1 = new TestObject("test1", 123);
        TestObject obj2 = new TestObject("test2", 456);
        TestObject combined = jacksonHelper.combine(obj1, obj2, TestObject.class);
        Assertions.assertNotNull(combined);
        Assertions.assertEquals("test2", combined.getName());
        Assertions.assertEquals(456, combined.getValue());
    }

    @Test
    public void testTreeToValue() {
        JacksonHelper jacksonHelper = JacksonUtils.create();
        String json = "{\"name\":\"test\",\"value\":123}";
        JsonNode node = jacksonHelper.toTreeNode(json);
        TestObject obj = jacksonHelper.treeToValue(node, TestObject.class);
        Assertions.assertNotNull(obj);
        Assertions.assertEquals("test", obj.getName());
        Assertions.assertEquals(123, obj.getValue());
    }

    @Test
    public void testNodeToValue() {
        JacksonHelper jacksonHelper = JacksonUtils.create();
        String json = "{\"name\":\"test\",\"value\":123}";
        JsonNode node = jacksonHelper.toTreeNode(json);
        Map<String, Object> map = jacksonHelper.nodeToValue(node, new TypeReference<Map<String, Object>>() {
        });
        Assertions.assertNotNull(map);
        Assertions.assertEquals("test", map.get("name"));
        Assertions.assertEquals(123, map.get("value"));
    }

    @Test
    public void testValueToTree() {
        JacksonHelper jacksonHelper = JacksonUtils.create();
        TestObject obj = new TestObject("test", 123);
        JsonNode node = jacksonHelper.valueToTree(obj);
        Assertions.assertNotNull(node);
        Assertions.assertEquals("test", node.get("name").asText());
        Assertions.assertEquals(123, node.get("value").asInt());
    }

    @Test
    public void testConvertFieldNameCase() {
        JacksonHelper jacksonHelper = JacksonUtils.create();
        String json = "{\"testName\":\"test\",\"testValue\":123}";
        JsonNode node = jacksonHelper.toTreeNode(json);
        JsonNode convertedNode = jacksonHelper.convertFieldNameCase(node, CaseFormat.LOWER_CAMEL, CaseFormat.UPPER_UNDERSCORE);
        Assertions.assertNotNull(convertedNode);
        Assertions.assertEquals("test", convertedNode.get("TEST_NAME").asText());
        Assertions.assertEquals(123, convertedNode.get("TEST_VALUE").asInt());
    }

    @Test
    public void testToStructuralBytes() {
        JacksonHelper jacksonHelper = JacksonUtils.create();
        byte[] bytes = jacksonHelper.toStructuralBytes(new TestObject("test", 123));
        Assertions.assertNotNull(bytes);
        Assertions.assertTrue(bytes.length > 0);
    }

    @Test
    public void testToObjectFromBytes() {
        JacksonHelper jacksonHelper = JacksonUtils.create();
        byte[] bytes = jacksonHelper.toStructuralBytes(new TestObject("test", 123));
        TestObject obj = jacksonHelper.toObject(bytes, TestObject.class);
        Assertions.assertNotNull(obj);
        Assertions.assertEquals("test", obj.getName());
        Assertions.assertEquals(123, obj.getValue());
    }

    @Test
    public void testToObjectFromInputStreamWithTypeReference() throws IOException {
        JacksonHelper jacksonHelper = JacksonUtils.create();
        String json = "{\"name\":\"test\",\"value\":123}";
        InputStream is = new ByteArrayInputStream(json.getBytes());
        Map<String, Object> map = jacksonHelper.toObject(is, new TypeReference<Map<String, Object>>() {
        });
        Assertions.assertNotNull(map);
        Assertions.assertEquals("test", map.get("name"));
        Assertions.assertEquals(123, map.get("value"));
    }

    public static class TestObject {
        private String name;
        private int value;

        public TestObject() {
        }

        public TestObject(String name, int value) {
            this.name = name;
            this.value = value;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getValue() {
            return value;
        }

        public void setValue(int value) {
            this.value = value;
        }
    }
}