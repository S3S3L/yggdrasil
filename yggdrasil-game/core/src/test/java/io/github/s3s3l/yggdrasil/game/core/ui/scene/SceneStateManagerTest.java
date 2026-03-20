package io.github.s3s3l.yggdrasil.game.core.ui.scene;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 场景状态管理器单元测试
 */
class SceneStateManagerTest {

    @BeforeEach
    void setUp() {
        SceneStateManager.resetInstance();
    }

    @AfterEach
    void tearDown() {
        SceneStateManager.resetInstance();
    }

    private static class TestScene extends UIScene {
        public TestScene(String name) {
            super(name);
        }
    }

    @Test
    void testGetInstance() {
        SceneStateManager manager1 = SceneStateManager.getInstance();
        SceneStateManager manager2 = SceneStateManager.getInstance();
        assertSame(manager1, manager2);
    }

    @Test
    void testSaveWithScene() {
        SceneStateManager manager = SceneStateManager.getInstance();
        TestScene scene = new TestScene("TestScene");

        SceneState state = manager.save(scene);

        assertNotNull(state);
        assertEquals("TestScene", state.getSceneName());
        assertTrue(manager.hasState("TestScene"));
    }

    @Test
    void testSaveWithNullScene() {
        SceneStateManager manager = SceneStateManager.getInstance();
        SceneState state = manager.save((UIScene) null);
        assertNull(state);
    }

    @Test
    void testSaveWithName() {
        SceneStateManager manager = SceneStateManager.getInstance();
        SceneState state = manager.save("TestScene");

        assertNotNull(state);
        assertEquals("TestScene", state.getSceneName());
        assertEquals("TestScene", manager.getLastSavedStateName());
    }

    @Test
    void testSaveWithNullName() {
        SceneStateManager manager = SceneStateManager.getInstance();
        SceneState state = manager.save((String) null);
        assertNull(state);
    }

    @Test
    void testLoad() {
        SceneStateManager manager = SceneStateManager.getInstance();
        manager.save("TestScene");

        SceneState state = manager.load("TestScene");

        assertNotNull(state);
        assertEquals("TestScene", state.getSceneName());
    }

    @Test
    void testLoadNotFound() {
        SceneStateManager manager = SceneStateManager.getInstance();
        SceneState state = manager.load("NonExistent");

        assertNull(state);
    }

    @Test
    void testLoadToScene() {
        SceneStateManager manager = SceneStateManager.getInstance();

        // First save creates the state
        manager.save("TestScene");
        SceneState savedState = manager.load("TestScene");
        savedState.put("key", "value");

        // Load state and verify it has the data
        SceneState loadedState = manager.load("TestScene");
        assertEquals("value", loadedState.get("key"));
    }

    @Test
    void testGetLastSavedStateName() {
        SceneStateManager manager = SceneStateManager.getInstance();
        manager.save("Scene1");
        manager.save("Scene2");

        assertEquals("Scene2", manager.getLastSavedStateName());
    }

    @Test
    void testHasState() {
        SceneStateManager manager = SceneStateManager.getInstance();
        assertFalse(manager.hasState("TestScene"));

        manager.save("TestScene");
        assertTrue(manager.hasState("TestScene"));
    }

    @Test
    void testRemoveState() {
        SceneStateManager manager = SceneStateManager.getInstance();
        manager.save("TestScene");
        assertTrue(manager.hasState("TestScene"));

        manager.removeState("TestScene");
        assertFalse(manager.hasState("TestScene"));
    }

    @Test
    void testClearAll() {
        SceneStateManager manager = SceneStateManager.getInstance();
        manager.save("Scene1");
        manager.save("Scene2");

        manager.clearAll();

        assertFalse(manager.hasState("Scene1"));
        assertFalse(manager.hasState("Scene2"));
    }

    @Test
    void testGetAllStates() {
        SceneStateManager manager = SceneStateManager.getInstance();
        manager.save("Scene1");
        manager.save("Scene2");

        var states = manager.getAllStates();

        assertEquals(2, states.size());
        assertTrue(states.containsKey("Scene1"));
        assertTrue(states.containsKey("Scene2"));
    }
}
