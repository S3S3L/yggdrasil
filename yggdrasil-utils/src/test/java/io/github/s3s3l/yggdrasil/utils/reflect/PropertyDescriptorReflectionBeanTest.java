package io.github.s3s3l.yggdrasil.utils.reflect;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.github.s3s3l.yggdrasil.utils.reflect.exception.ReflectException;

public class PropertyDescriptorReflectionBeanTest {

    private TestClass testObj;
    private PropertyDescriptorReflectionBean reflectionBean;

    @BeforeEach
    public void setUp() {
        testObj = new TestClass();
        reflectionBean = new PropertyDescriptorReflectionBean(testObj);
    }

    @Test
    public void testGetFields() {
        assertNotNull(reflectionBean.getFields());
        assertTrue(reflectionBean.getFields().contains("field1"));
        assertTrue(reflectionBean.getFields().contains("field2"));
    }

    @Test
    public void testHasField() {
        assertTrue(reflectionBean.hasField("field1"));
        assertTrue(reflectionBean.hasField("field2"));
        assertTrue(!reflectionBean.hasField("nonExistentField"));
    }

    @Test
    public void testGetFieldValue() {
        testObj.setField1("value1");
        assertEquals("value1", reflectionBean.getFieldValue("field1"));
    }

    @Test
    public void testSetFieldValue() {
        reflectionBean.setFieldValue("field1", "newValue1");
        assertEquals("newValue1", testObj.getField1());
    }

    @Test
    public void testFillWithProperties() {
        Properties props = new Properties();
        props.setProperty("field1", "propValue1");
        props.setProperty("field2", "propValue2");

        reflectionBean.fill(props);

        assertEquals("propValue1", testObj.getField1());
        assertEquals("propValue2", testObj.getField2());
    }

    @Test
    public void testFillWithMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("field1", "mapValue1");
        map.put("field2", "mapValue2");

        reflectionBean.fill(map);

        assertEquals("mapValue1", testObj.getField1());
        assertEquals("mapValue2", testObj.getField2());
    }

    @Test
    public void testGetFieldValueNonExistentField() {
        assertThrows(ReflectException.class, () -> {
            reflectionBean.getFieldValue("nonExistentField");
        });
    }

    @Test
    public void testSetFieldValueNonExistentField() {
        assertThrows(ReflectException.class, () -> {
            reflectionBean.setFieldValue("nonExistentField", "value");
        });
    }

    public static class TestClass {
        private String field1;
        private String field2;

        public String getField1() {
            return field1;
        }

        public void setField1(String field1) {
            this.field1 = field1;
        }

        public String getField2() {
            return field2;
        }

        public void setField2(String field2) {
            this.field2 = field2;
        }
    }
}