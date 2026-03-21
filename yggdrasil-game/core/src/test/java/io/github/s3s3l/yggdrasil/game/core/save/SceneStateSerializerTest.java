package io.github.s3s3l.yggdrasil.game.core.save;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.s3s3l.yggdrasil.game.core.ui.scene.SceneState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

/**
 * SceneStateSerializer 单元测试
 */
class SceneStateSerializerTest {

    @TempDir
    File tempDir;

    private SceneStateSerializer serializer;

    @BeforeEach
    void setUp() {
        serializer = new SceneStateSerializer();
    }

    @Test
    void testConstructor() {
        assertNotNull(serializer);
    }

    @Test
    void testConstructorWithCustomMapper() {
        ObjectMapper mapper = new ObjectMapper();
        SceneStateSerializer customSerializer = new SceneStateSerializer(mapper);
        assertNotNull(customSerializer);
    }

    @Test
    void testSerializeNull() {
        String result = serializer.serialize(null);
        assertNull(result);
    }

    @Test
    void testDeserializeNull() {
        SceneState result = serializer.deserialize(null);
        assertNull(result);
    }

    @Test
    void testDeserializeEmptyString() {
        SceneState result = serializer.deserialize("");
        assertNull(result);
    }

    @Test
    void testSerializeAndDeserialize() {
        SceneState original = new SceneState("TestScene");
        original.put("stringKey", "hello");
        original.put("intKey", 42);
        original.put("floatKey", 3.14f);
        original.put("booleanKey", true);
        original.put("longKey", 123456789L);

        String json = serializer.serialize(original);
        assertNotNull(json);

        SceneState deserialized = serializer.deserialize(json);
        assertNotNull(deserialized);
        assertEquals("TestScene", deserialized.getSceneName());
        assertEquals("hello", deserialized.getString("stringKey"));
        assertEquals(42, deserialized.getInt("intKey"));
        assertEquals(3.14f, deserialized.getFloat("floatKey"), 0.01f);
        assertTrue(deserialized.getBoolean("booleanKey"));
        assertEquals(123456789L, deserialized.getInt("longKey").longValue());
    }

    @Test
    void testSerializeWithTimestamp() {
        SceneState original = new SceneState("TestScene");
        long timestamp = 1234567890L;
        original.setTimestamp(timestamp);

        String json = serializer.serialize(original);
        SceneState deserialized = serializer.deserialize(json);

        assertEquals(timestamp, deserialized.getTimestamp());
    }

    @Test
    void testDeserializeWithNullValue() {
        SceneState original = new SceneState("TestScene");
        original.put("nullKey", null);

        String json = serializer.serialize(original);
        SceneState deserialized = serializer.deserialize(json);

        // null 值在反序列化后可能不存在
        assertFalse(deserialized.containsKey("nullKey"));
    }

    @Test
    void testDeserializeWithDoubleValue() {
        SceneState original = new SceneState("TestScene");
        original.put("doubleKey", 2.71828);

        String json = serializer.serialize(original);
        SceneState deserialized = serializer.deserialize(json);

        assertEquals(2.71828, deserialized.getFloat("doubleKey"), 0.001f);
    }

    @Test
    void testSaveToFile() {
        SceneState original = new SceneState("TestScene");
        original.put("key", "value");

        File saveFile = new File(tempDir, "scene_state.json");
        boolean result = serializer.saveToFile(original, saveFile);

        assertTrue(result);
        assertTrue(saveFile.exists());
    }

    @Test
    void testLoadFromFile() {
        SceneState original = new SceneState("TestScene");
        original.put("stringKey", "hello world");
        original.put("intKey", 100);

        File saveFile = new File(tempDir, "scene_state.json");
        serializer.saveToFile(original, saveFile);

        SceneState loaded = serializer.loadFromFile(saveFile);

        assertNotNull(loaded);
        assertEquals("TestScene", loaded.getSceneName());
        assertEquals("hello world", loaded.getString("stringKey"));
        assertEquals(100, loaded.getInt("intKey"));
    }

    @Test
    void testLoadFromNonExistentFile() {
        File nonExistentFile = new File(tempDir, "non_existent.json");
        SceneState result = serializer.loadFromFile(nonExistentFile);
        assertNull(result);
    }

    @Test
    void testLoadFromFileByPath() {
        SceneState original = new SceneState("TestScene");
        original.put("key", "value");

        File saveFile = new File(tempDir, "scene_state.json");
        serializer.saveToFile(original, saveFile);

        SceneState loaded = serializer.loadFromFile(saveFile.getAbsolutePath());

        assertNotNull(loaded);
        assertEquals("TestScene", loaded.getSceneName());
    }

    @Test
    void testMergeState() {
        SceneState state1 = new SceneState("TestScene");
        state1.put("key1", "value1");

        SceneState state2 = new SceneState("TestScene");
        state2.put("key2", "value2");

        state1.merge(state2);

        assertEquals("value1", state1.getString("key1"));
        assertEquals("value2", state1.getString("key2"));
    }

    @Test
    void testCopyState() {
        SceneState original = new SceneState("TestScene");
        original.put("key", "value");
        original.setTimestamp(1234567890L);

        SceneState copy = original.copy();

        assertEquals("TestScene", copy.getSceneName());
        assertEquals("value", copy.getString("key"));
        assertEquals(1234567890L, copy.getTimestamp());

        // 修改副本不应影响原件
        copy.put("newKey", "newValue");
        assertFalse(original.containsKey("newKey"));
    }

    @Test
    void testClearState() {
        SceneState state = new SceneState("TestScene");
        state.put("key1", "value1");
        state.put("key2", "value2");

        state.clear();

        assertFalse(state.containsKey("key1"));
        assertFalse(state.containsKey("key2"));
    }

    @Test
    void testStateEqualsAndHashCode() {
        SceneState state1 = new SceneState("TestScene");
        SceneState state2 = new SceneState("TestScene");
        SceneState state3 = new SceneState("DifferentScene");

        assertEquals(state1, state2);
        assertNotEquals(state1, state3);
        assertEquals(state1.hashCode(), state2.hashCode());
    }

    @Test
    void testStateToString() {
        SceneState state = new SceneState("TestScene");
        state.put("key1", "value1");
        state.put("key2", "value2");

        String result = state.toString();
        assertTrue(result.contains("TestScene"));
        assertTrue(result.contains("2 entries"));
    }
}
