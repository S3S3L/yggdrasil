package io.github.s3s3l.yggdrasil.game.core.ui.scene;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 场景管理器单元测试
 */
class SceneManagerTest {

    @BeforeEach
    void setUp() {
        SceneManager.resetInstance();
    }

    @AfterEach
    void tearDown() {
        SceneManager.resetInstance();
    }

    @Test
    void testGetInstance() {
        SceneManager manager1 = SceneManager.getInstance();
        SceneManager manager2 = SceneManager.getInstance();
        assertSame(manager1, manager2);
    }

    @Test
    void testRegisterScene() {
        SceneManager manager = SceneManager.getInstance();
        TestScene scene = new TestScene("Test");
        manager.registerScene(scene);

        assertTrue(manager.hasScene("Test"));
        assertEquals(scene, manager.getScene("Test"));
    }

    @Test
    void testUnregisterScene() {
        SceneManager manager = SceneManager.getInstance();
        TestScene scene = new TestScene("Test");
        manager.registerScene(scene);

        manager.unregisterScene("Test");
        assertFalse(manager.hasScene("Test"));
    }

    @Test
    void testChangeScene() {
        SceneManager manager = SceneManager.getInstance();
        TestScene scene1 = new TestScene("Scene1");
        TestScene scene2 = new TestScene("Scene2");
        manager.registerScene(scene1);
        manager.registerScene(scene2);

        manager.changeScene("Scene1");
        assertEquals("Scene1", manager.getCurrentSceneName());
        assertTrue(scene1.wasEntered());

        manager.changeScene("Scene2");
        assertEquals("Scene2", manager.getCurrentSceneName());
        assertTrue(scene1.wasExited());
        assertTrue(scene2.wasEntered());
    }

    @Test
    void testChangeSceneWithSameScene() {
        SceneManager manager = SceneManager.getInstance();
        TestScene scene = new TestScene("Test");
        manager.registerScene(scene);

        manager.changeScene("Test");
        manager.changeScene("Test");

        assertEquals(1, scene.getEnterCount());
    }

    @Test
    void testPushScene() {
        SceneManager manager = SceneManager.getInstance();
        TestScene scene1 = new TestScene("Scene1");
        TestScene scene2 = new TestScene("Scene2");
        manager.registerScene(scene1);
        manager.registerScene(scene2);

        manager.changeScene("Scene1");
        manager.pushScene("Scene2");

        assertEquals("Scene2", manager.getCurrentSceneName());
        assertEquals(1, manager.getStackSize());
        assertTrue(scene1.wasPaused());
    }

    @Test
    void testPopScene() {
        SceneManager manager = SceneManager.getInstance();
        TestScene scene1 = new TestScene("Scene1");
        TestScene scene2 = new TestScene("Scene2");
        manager.registerScene(scene1);
        manager.registerScene(scene2);

        manager.changeScene("Scene1");
        manager.pushScene("Scene2");
        manager.popScene();

        assertEquals("Scene1", manager.getCurrentSceneName());
        assertTrue(scene2.wasExited());
        assertTrue(scene1.wasResumed());
    }

    @Test
    void testPopSceneEmptyStack() {
        SceneManager manager = SceneManager.getInstance();
        UIScene popped = manager.popScene();
        assertNull(popped);
    }

    @Test
    void testPeekScene() {
        SceneManager manager = SceneManager.getInstance();
        TestScene scene1 = new TestScene("Scene1");
        TestScene scene2 = new TestScene("Scene2");
        manager.registerScene(scene1);
        manager.registerScene(scene2);

        manager.changeScene("Scene1");
        manager.pushScene("Scene2");

        UIScene peeked = manager.peekScene();
        assertEquals("Scene1", peeked.getName());
    }

    @Test
    void testGetStackSize() {
        SceneManager manager = SceneManager.getInstance();
        TestScene scene1 = new TestScene("Scene1");
        TestScene scene2 = new TestScene("Scene2");
        manager.registerScene(scene1);
        manager.registerScene(scene2);

        manager.changeScene("Scene1");
        assertEquals(0, manager.getStackSize());

        manager.pushScene("Scene2");
        assertEquals(1, manager.getStackSize());
    }

    @Test
    void testClearStack() {
        SceneManager manager = SceneManager.getInstance();
        TestScene scene1 = new TestScene("Scene1");
        TestScene scene2 = new TestScene("Scene2");
        manager.registerScene(scene1);
        manager.registerScene(scene2);

        manager.changeScene("Scene1");
        manager.pushScene("Scene2");
        manager.pushScene("Scene1");

        manager.clearStack();
        assertEquals(0, manager.getStackSize());
    }

    @Test
    void testUpdate() {
        SceneManager manager = SceneManager.getInstance();
        TestScene scene = new TestScene("Test");
        manager.registerScene(scene);
        manager.changeScene("Test");

        manager.update(0.016f);
        assertTrue(scene.wasUpdated());
    }

    @Test
    void testUpdatePausedScene() {
        SceneManager manager = SceneManager.getInstance();
        TestScene scene1 = new TestScene("Scene1");
        TestScene scene2 = new TestScene("Scene2");
        manager.registerScene(scene1);
        manager.registerScene(scene2);

        manager.changeScene("Scene1");
        manager.pushScene("Scene2");

        manager.update(0.016f);
        assertFalse(scene1.wasUpdated());
    }

    @Test
    void testAddSceneChangeListener() {
        SceneManager manager = SceneManager.getInstance();
        TestListener listener = new TestListener();
        manager.addSceneChangeListener(listener);

        TestScene scene = new TestScene("Test");
        manager.registerScene(scene);
        manager.changeScene("Test");

        assertTrue(listener.changed);
        assertEquals("Test", listener.newScene.getName());
    }

    @Test
    void testRemoveSceneChangeListener() {
        SceneManager manager = SceneManager.getInstance();
        TestListener listener = new TestListener();
        manager.addSceneChangeListener(listener);
        manager.removeSceneChangeListener(listener);

        TestScene scene = new TestScene("Test");
        manager.registerScene(scene);
        manager.changeScene("Test");

        assertFalse(listener.changed);
    }

    @Test
    void testClearAll() {
        SceneManager manager = SceneManager.getInstance();
        TestScene scene = new TestScene("Test");
        manager.registerScene(scene);
        manager.changeScene("Test");

        manager.clearAll();

        assertFalse(manager.hasScene("Test"));
        assertNull(manager.getCurrentScene());
    }

    @Test
    void testGetPreviousScene() {
        SceneManager manager = SceneManager.getInstance();
        TestScene scene1 = new TestScene("Scene1");
        TestScene scene2 = new TestScene("Scene2");
        manager.registerScene(scene1);
        manager.registerScene(scene2);

        manager.changeScene("Scene1");
        manager.changeScene("Scene2");

        assertEquals("Scene1", manager.getPreviousScene().getName());
    }

    private static class TestScene extends UIScene {
        private boolean entered;
        private boolean exited;
        private boolean paused;
        private boolean resumed;
        private boolean updated;
        private int enterCount;

        public TestScene(String name) {
            super(name);
        }

        @Override
        protected void onEnter() {
            super.onEnter();
            entered = true;
            enterCount++;
        }

        @Override
        protected void onExit() {
            super.onExit();
            exited = true;
        }

        @Override
        protected void onPause() {
            super.onPause();
            paused = true;
        }

        @Override
        protected void onResume() {
            super.onResume();
            resumed = true;
        }

        @Override
        public void update(float deltaTime) {
            if (isActive() && !isPaused()) {
                updated = true;
            }
        }

        public boolean wasEntered() {
            return entered;
        }

        public boolean wasExited() {
            return exited;
        }

        public boolean wasPaused() {
            return paused;
        }

        public boolean wasResumed() {
            return resumed;
        }

        public boolean wasUpdated() {
            return updated;
        }

        public int getEnterCount() {
            return enterCount;
        }
    }

    private static class TestListener implements SceneManager.SceneChangeListener {
        boolean changed = false;
        UIScene newScene = null;

        @Override
        public void onSceneChanged(UIScene oldScene, UIScene newScene) {
            this.changed = true;
            this.newScene = newScene;
        }
    }
}
