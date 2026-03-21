package io.github.s3s3l.yggdrasil.game.core.resource;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

/**
 * LoadParams 单元测试
 */
public class LoadParamsTest {

    private LoadParams params;

    @BeforeEach
    void setUp() {
        params = new LoadParams();
    }

    @Test
    void testSetAndGet() {
        params.set("key1", "value1");
        assertEquals("value1", params.get("key1"));
    }

    @Test
    void testGetString() {
        params.set("stringKey", "testValue");
        assertEquals("testValue", params.getString("stringKey"));
    }

    @Test
    void testGetInt() {
        params.set("intKey", 42);
        assertEquals(42, params.getInt("intKey"));

        params.set("stringInt", "100");
        assertEquals(100, params.getInt("stringInt"));
    }

    @Test
    void testGetFloat() {
        params.set("floatKey", 3.14f);
        assertEquals(3.14f, params.getFloat("floatKey"), 0.01f);

        params.set("stringFloat", "2.5");
        assertEquals(2.5f, params.getFloat("stringFloat"), 0.01f);
    }

    @Test
    void testGetBoolean() {
        params.set("boolKey", true);
        assertTrue(params.getBoolean("boolKey"));

        params.set("stringBool", "false");
        assertFalse(params.getBoolean("stringBool"));
    }

    @Test
    void testHas() {
        params.set("exists", "value");
        assertTrue(params.has("exists"));
        assertFalse(params.has("notExists"));
    }

    @Test
    void testGetAll() {
        params.set("key1", "value1");
        params.set("key2", "value2");

        assertEquals(2, params.getAll().size());
    }

    @Test
    void testClear() {
        params.set("key1", "value1");
        params.set("key2", "value2");

        params.clear();

        assertTrue(params.getAll().isEmpty());
    }

    @Test
    void testNullHandling() {
        params.set("nullKey", null);
        assertNull(params.get("nullKey"));
    }

    @Test
    void testInvalidIntFormat() {
        params.set("invalidInt", "not-a-number");
        assertNull(params.getInt("invalidInt"));
    }

    @Test
    void testInvalidFloatFormat() {
        params.set("invalidFloat", "not-a-float");
        assertNull(params.getFloat("invalidFloat"));
    }
}
