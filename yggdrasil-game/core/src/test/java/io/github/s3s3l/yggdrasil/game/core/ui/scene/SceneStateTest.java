package io.github.s3s3l.yggdrasil.game.core.ui.scene;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 场景状态单元测试
 */
class SceneStateTest {

    private SceneState state;

    @BeforeEach
    void setUp() {
        state = new SceneState("TestScene");
    }

    @Test
    void testGetSceneName() {
        assertEquals("TestScene", state.getSceneName());
    }

    @Test
    void testGetTimestamp() {
        long before = System.currentTimeMillis();
        SceneState newState = new SceneState("Test");
        long after = System.currentTimeMillis();

        assertTrue(newState.getTimestamp() >= before);
        assertTrue(newState.getTimestamp() <= after);
    }

    @Test
    void testUpdateTimestamp() {
        long before = state.getTimestamp();

        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            // Ignore
        }

        state.updateTimestamp();
        assertTrue(state.getTimestamp() > before);
    }

    @Test
    void testPutAndGet() {
        state.put("key", "value");
        assertEquals("value", state.get("key"));
    }

    @Test
    void testGetString() {
        state.put("string", "hello");
        assertEquals("hello", state.getString("string"));
    }

    @Test
    void testGetInt() {
        state.put("int", 42);
        assertEquals(42, state.getInt("int"));
    }

    @Test
    void testGetFloat() {
        state.put("float", 3.14f);
        assertEquals(3.14f, state.getFloat("float"), 0.01f);
    }

    @Test
    void testGetBoolean() {
        state.put("boolean", true);
        assertTrue(state.getBoolean("boolean"));

        state.put("boolean", false);
        assertFalse(state.getBoolean("boolean"));
    }

    @Test
    void testContainsKey() {
        assertFalse(state.containsKey("key"));
        state.put("key", "value");
        assertTrue(state.containsKey("key"));
    }

    @Test
    void testRemove() {
        state.put("key", "value");
        state.remove("key");
        assertNull(state.get("key"));
    }

    @Test
    void testClear() {
        state.put("key1", "value1");
        state.put("key2", "value2");

        state.clear();

        assertFalse(state.containsKey("key1"));
        assertFalse(state.containsKey("key2"));
    }

    @Test
    void testGetAll() {
        state.put("key1", "value1");
        state.put("key2", "value2");

        var all = state.getAll();

        assertEquals(2, all.size());
        assertEquals("value1", all.get("key1"));
        assertEquals("value2", all.get("key2"));
    }

    @Test
    void testMerge() {
        SceneState other = new SceneState("Other");
        other.put("otherKey", "otherValue");

        state.put("key", "value");
        state.merge(other);

        assertEquals("value", state.get("key"));
        assertEquals("otherValue", state.get("otherKey"));
    }

    @Test
    void testCopy() {
        state.put("key", "value");
        state.updateTimestamp();

        SceneState copy = state.copy();

        assertEquals(state.getSceneName(), copy.getSceneName());
        assertEquals(state.get("key"), copy.get("key"));
        assertEquals(state.getTimestamp(), copy.getTimestamp());
        assertNotSame(state, copy);
    }

    @Test
    void testEquals() {
        SceneState a = new SceneState("Test");
        SceneState b = new SceneState("Test");
        assertEquals(a, b);
    }

    @Test
    void testHashCode() {
        SceneState a = new SceneState("Test");
        SceneState b = new SceneState("Test");
        assertEquals(a.hashCode(), b.hashCode());
    }

    @Test
    void testToString() {
        String str = state.toString();
        assertTrue(str.contains("TestScene"));
    }

    @Test
    void testGetNull() {
        assertNull(state.get("nonexistent"));
    }

    @Test
    void testGetStringNull() {
        assertNull(state.getString("nonexistent"));
    }

    @Test
    void testGetIntNull() {
        assertNull(state.getInt("nonexistent"));
    }

    @Test
    void testGetFloatNull() {
        assertNull(state.getFloat("nonexistent"));
    }

    @Test
    void testGetBooleanNull() {
        assertNull(state.getBoolean("nonexistent"));
    }
}
