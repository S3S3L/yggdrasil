package io.github.s3s3l.yggdrasil.game.core.save;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * SaveSlot 单元测试
 */
class SaveSlotTest {

    private SaveSlot saveSlot;

    @BeforeEach
    void setUp() {
        saveSlot = new SaveSlot(1);
    }

    @Test
    void testConstructor() {
        assertEquals(1, saveSlot.getSlotId());
        assertEquals("Slot 1", saveSlot.getSlotName());
        assertNull(saveSlot.getSceneName());
        assertEquals(0, saveSlot.getGameTime());
        assertFalse(saveSlot.hasData());
    }

    @Test
    void testSetSlotName() {
        saveSlot.setSlotName("My Save");
        assertEquals("My Save", saveSlot.getSlotName());
    }

    @Test
    void testSetSceneName() {
        saveSlot.setSceneName("GameScene");
        assertEquals("GameScene", saveSlot.getSceneName());
    }

    @Test
    void testSetTimestamp() {
        long timestamp = 1234567890L;
        saveSlot.setTimestamp(timestamp);
        assertEquals(timestamp, saveSlot.getTimestamp());
    }

    @Test
    void testUpdateTimestamp() {
        long before = System.currentTimeMillis();
        saveSlot.updateTimestamp();
        long after = System.currentTimeMillis();

        long timestamp = saveSlot.getTimestamp();
        assertTrue(timestamp >= before && timestamp <= after);
    }

    @Test
    void testSetGameTime() {
        saveSlot.setGameTime(123.45f);
        assertEquals(123.45f, saveSlot.getGameTime(), 0.01f);
    }

    @Test
    void testPutSceneData() {
        saveSlot.putSceneData("key1", "value1");
        assertEquals("value1", saveSlot.getStringSceneData("key1"));
    }

    @Test
    void testGetStringSceneData() {
        saveSlot.putSceneData("string", "hello");
        assertEquals("hello", saveSlot.getStringSceneData("string"));
        assertNull(saveSlot.getStringSceneData("nonexistent"));
    }

    @Test
    void testGetIntSceneData() {
        saveSlot.putSceneData("number", 42);
        assertEquals(42, saveSlot.getIntSceneData("number"));
        assertNull(saveSlot.getIntSceneData("nonexistent"));
    }

    @Test
    void testGetFloatSceneData() {
        saveSlot.putSceneData("float", 3.14f);
        assertEquals(3.14f, saveSlot.getFloatSceneData("float"), 0.01f);
        assertNull(saveSlot.getFloatSceneData("nonexistent"));
    }

    @Test
    void testGetBooleanSceneData() {
        saveSlot.putSceneData("boolean", true);
        assertTrue(saveSlot.getBooleanSceneData("boolean"));

        saveSlot.putSceneData("falseBoolean", false);
        assertFalse(saveSlot.getBooleanSceneData("falseBoolean"));

        assertNull(saveSlot.getBooleanSceneData("nonexistent"));
    }

    @Test
    void testGetAllSceneData() {
        saveSlot.putSceneData("key1", "value1");
        saveSlot.putSceneData("key2", 42);

        Map<String, Object> allData = saveSlot.getAllSceneData();
        assertEquals(2, allData.size());
        assertEquals("value1", allData.get("key1"));
        assertEquals(42, allData.get("key2"));
    }

    @Test
    void testPutMetadata() {
        saveSlot.putMetadata("version", "1.0");
        assertEquals("1.0", saveSlot.getMetadata("version"));
    }

    @Test
    void testGetMetadata() {
        saveSlot.putMetadata("author", "Test");
        assertEquals("Test", saveSlot.getMetadata("author"));
        assertNull(saveSlot.getMetadata("nonexistent"));
    }

    @Test
    void testGetAllMetadata() {
        saveSlot.putMetadata("key1", "value1");
        saveSlot.putMetadata("key2", "value2");

        Map<String, String> allMetadata = saveSlot.getAllMetadata();
        assertEquals(2, allMetadata.size());
        assertEquals("value1", allMetadata.get("key1"));
        assertEquals("value2", allMetadata.get("key2"));
    }

    @Test
    void testHasSceneData() {
        saveSlot.putSceneData("key", "value");
        assertTrue(saveSlot.hasSceneData("key"));
        assertFalse(saveSlot.hasSceneData("nonexistent"));
    }

    @Test
    void testClearSceneData() {
        saveSlot.putSceneData("key", "value");
        assertTrue(saveSlot.hasSceneData("key"));

        saveSlot.clearSceneData();
        assertFalse(saveSlot.hasSceneData("key"));
    }

    @Test
    void testHasData() {
        assertFalse(saveSlot.hasData());

        saveSlot.setSceneName("GameScene");
        assertFalse(saveSlot.hasData());

        saveSlot.putSceneData("key", "value");
        assertTrue(saveSlot.hasData());
    }

    @Test
    void testClear() {
        saveSlot.setSlotName("My Save");
        saveSlot.setSceneName("GameScene");
        saveSlot.setGameTime(100.5f);
        saveSlot.putSceneData("key", "value");
        saveSlot.putMetadata("meta", "data");

        saveSlot.clear();

        assertEquals("Slot 1", saveSlot.getSlotName());
        assertNull(saveSlot.getSceneName());
        assertEquals(0, saveSlot.getGameTime(), 0.01f);
        assertFalse(saveSlot.hasData());
    }

    @Test
    void testEqualsAndHashCode() {
        SaveSlot slot1 = new SaveSlot(1);
        SaveSlot slot2 = new SaveSlot(1);
        SaveSlot slot3 = new SaveSlot(2);

        assertEquals(slot1, slot2);
        assertNotEquals(slot1, slot3);
        assertEquals(slot1.hashCode(), slot2.hashCode());
    }

    @Test
    void testToString() {
        saveSlot.setSceneName("GameScene");
        saveSlot.putSceneData("key", "value");

        String result = saveSlot.toString();
        assertTrue(result.contains("slotId=1"));
        assertTrue(result.contains("sceneName='GameScene'"));
        assertTrue(result.contains("data=1 entries"));
    }
}
