package io.github.s3s3l.yggdrasil.utils.reflect;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ReflectionTest {

    private Reflection<TestClass> reflection;
    private TestClass testObj;

    @BeforeEach
    public void setUp() {
        testObj = new TestClass();
        reflection = Reflection.create(testObj);
    }

    @Test
    public void testCreate() {
        assertNotNull(reflection);
        assertEquals(testObj, reflection.getObj());
    }

    @Test
    public void testGetFields() {
        Collection<String> fields = reflection.getFields();
        assertTrue(fields.contains("field1"));
        assertTrue(fields.contains("field2"));
    }

    @Test
    public void testHasField() {
        assertTrue(reflection.hasField("field1"));
        assertFalse(reflection.hasField("nonExistentField"));
    }

    @Test
    public void testGetFieldValue() {
        testObj.field1 = "testValue";
        assertEquals("testValue", reflection.getFieldValue("field1"));
    }

    @Test
    public void testSetFieldValue() {
        reflection.setFieldValue("field1", "newValue");
        assertEquals("newValue", testObj.field1);
    }

    @Test
    public void testFillWithProperties() {
        Properties props = new Properties();
        props.setProperty("field1", "propValue");
        reflection.fill(props);
        assertEquals("propValue", testObj.field1);
    }

    @Test
    public void testFillWithMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("field1", "mapValue");
        reflection.fill(map);
        assertEquals("mapValue", testObj.field1);
    }

    private static class TestClass {
        private String field1;
        private int field2;
    }
}