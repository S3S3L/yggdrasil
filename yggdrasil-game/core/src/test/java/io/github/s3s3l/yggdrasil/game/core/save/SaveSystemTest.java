package io.github.s3s3l.yggdrasil.game.core.save;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * SaveSystem 单元测试
 */
class SaveSystemTest {

    @TempDir
    File tempDir;

    private SaveSystem saveSystem;

    @BeforeEach
    void setUp() {
        saveSystem = new SaveSystem(3);
        saveSystem.setSaveDirectory(tempDir);
    }

    @Test
    void testConstructor() {
        SaveSystem system = new SaveSystem(5);
        assertEquals(5, system.getMaxSlots());
    }

    @Test
    void testDefaultConstructor() {
        SaveSystem system = new SaveSystem();
        assertEquals(3, system.getMaxSlots());
    }

    @Test
    void testGetSaveSlot() {
        SaveSlot slot = saveSystem.getSaveSlot(1);
        assertNotNull(slot);
        assertEquals(1, slot.getSlotId());
    }

    @Test
    void testGetInvalidSaveSlot() {
        SaveSlot slot = saveSystem.getSaveSlot(0);
        assertNull(slot);

        slot = saveSystem.getSaveSlot(4);
        assertNull(slot);
    }

    @Test
    void testGetAllSaveSlots() {
        var allSlots = saveSystem.getAllSaveSlots();
        assertEquals(3, allSlots.size());
    }

    @Test
    void testSaveScene() {
        MockScene scene = new MockScene("TestScene");
        boolean result = saveSystem.saveScene(1, scene);

        assertTrue(result);
        assertTrue(saveSystem.hasSave(1));
    }

    @Test
    void testSaveSceneWithInvalidSlot() {
        MockScene scene = new MockScene("TestScene");
        boolean result = saveSystem.saveScene(0, scene);

        assertFalse(result);
    }

    @Test
    void testSaveSceneWithNullScene() {
        boolean result = saveSystem.saveScene(1, null);
        assertFalse(result);
    }

    @Test
    void testSaveSceneWithMetadata() {
        MockScene scene = new MockScene("TestScene");
        Map<String, String> metadata = new HashMap<>();
        metadata.put("version", "1.0");
        metadata.put("author", "Test");

        boolean result = saveSystem.saveScene(1, "My Save", scene, 123.45f, metadata);

        assertTrue(result);

        SaveSlot slot = saveSystem.getSaveSlot(1);
        assertEquals("My Save", slot.getSlotName());
        assertEquals(123.45f, slot.getGameTime(), 0.01f);
        assertEquals("1.0", slot.getMetadata("version"));
        assertEquals("Test", slot.getMetadata("author"));
    }

    @Test
    void testLoadScene() {
        MockScene scene = new MockScene("TestScene");
        saveSystem.saveScene(1, scene);

        MockScene loadedScene = new MockScene("LoadedScene");
        boolean result = saveSystem.loadScene(1, loadedScene);

        assertTrue(result);
    }

    @Test
    void testLoadEmptyScene() {
        MockScene scene = new MockScene("TestScene");
        boolean result = saveSystem.loadScene(1, scene);

        assertFalse(result);
    }

    @Test
    void testDeleteSave() {
        MockScene scene = new MockScene("TestScene");
        saveSystem.saveScene(1, scene);
        assertTrue(saveSystem.hasSave(1));

        boolean result = saveSystem.deleteSave(1);
        assertTrue(result);
        assertFalse(saveSystem.hasSave(1));
    }

    @Test
    void testGetSaveSlotInfo() {
        MockScene scene = new MockScene("TestScene");
        saveSystem.saveScene(1, "My Save", scene, 100.0f, null);

        SaveSystem.SaveSlotInfo info = saveSystem.getSaveSlotInfo(1);
        assertNotNull(info);
        assertEquals(1, info.getSlotId());
        assertEquals("My Save", info.getSlotName());
        assertEquals("MockScene", info.getSceneName());
    }

    @Test
    void testGetEmptySaveSlotInfo() {
        SaveSystem.SaveSlotInfo info = saveSystem.getSaveSlotInfo(1);
        assertNull(info);
    }

    @Test
    void testGetSaveCount() {
        assertEquals(0, saveSystem.getSaveCount());

        MockScene scene = new MockScene("TestScene");
        saveSystem.saveScene(1, scene);
        assertEquals(1, saveSystem.getSaveCount());

        saveSystem.saveScene(2, scene);
        assertEquals(2, saveSystem.getSaveCount());
    }

    @Test
    void testGetAllSaveInfo() {
        MockScene scene = new MockScene("TestScene");
        saveSystem.saveScene(1, scene);
        saveSystem.saveScene(2, scene);

        List<SaveSystem.SaveSlotInfo> allInfo = saveSystem.getAllSaveInfo();
        assertEquals(2, allInfo.size());
    }

    @Test
    void testSaveToFileAndLoad() {
        MockScene scene = new MockScene("TestScene");
        Map<String, String> metadata = new HashMap<>();
        metadata.put("difficulty", "hard");

        saveSystem.saveScene(1, "Persistent Save", scene, 250.0f, metadata);

        // 创建新的 SaveSystem 实例模拟重新加载
        SaveSystem newSystem = new SaveSystem(3);
        newSystem.setSaveDirectory(tempDir);

        assertTrue(newSystem.hasSave(1));

        SaveSystem.SaveSlotInfo info = newSystem.getSaveSlotInfo(1);
        assertNotNull(info);
        assertEquals("Persistent Save", info.getSlotName());
        assertEquals(250.0f, info.getGameTime(), 0.01f);
    }

    @Test
    void testSaveSlotInfoToString() {
        SaveSystem.SaveSlotInfo info = new SaveSystem.SaveSlotInfo(
                1, "Test Save", "GameScene", 1234567890L, 100.5f
        );

        String result = info.toString();
        assertTrue(result.contains("slotId=1"));
        assertTrue(result.contains("slotName='Test Save'"));
        assertTrue(result.contains("sceneName='GameScene'"));
    }

    /**
     * 模拟场景用于测试
     */
    private static class MockScene implements io.github.s3s3l.yggdrasil.game.core.scene.Scene {

        private final String className;
        private long id = 1L;

        MockScene(String className) {
            this.className = className;
        }

        @Override
        public void show() {
        }

        @Override
        public void hide() {
        }

        @Override
        public void update(float delta) {
        }

        @Override
        public void onWindowResize(int width, int height) {
        }

        @Override
        public void calculate(float delta) {
        }

        @Override
        public void preRender() {
        }

        @Override
        public void render() {
        }

        @Override
        public boolean isDirty() {
            return false;
        }

        @Override
        public void markDirty() {
        }

        @Override
        public void clearDirty() {
        }

        @Override
        public long getId() {
            return id;
        }

        @Override
        public void setId(long id) {
            this.id = id;
        }

        @Override
        public io.github.s3s3l.yggdrasil.game.core.snapshot.ObjectSnapshot snapshot() {
            return null;
        }

        @Override
        public void restore(io.github.s3s3l.yggdrasil.game.core.snapshot.ObjectSnapshot snapshot) {
        }
    }
}
