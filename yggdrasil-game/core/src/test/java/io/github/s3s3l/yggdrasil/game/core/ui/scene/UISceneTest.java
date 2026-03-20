package io.github.s3s3l.yggdrasil.game.core.ui.scene;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * UI 场景单元测试
 */
class UISceneTest {

    private TestScene scene;

    @BeforeEach
    void setUp() {
        scene = new TestScene("TestScene");
    }

    @Test
    void testGetName() {
        assertEquals("TestScene", scene.getName());
    }

    @Test
    void testSetName() {
        scene.setName("NewName");
        assertEquals("NewName", scene.getName());
    }

    @Test
    void testInitialState() {
        assertFalse(scene.isActive());
        assertFalse(scene.isPaused());
    }

    @Test
    void testOnEnter() {
        scene.onEnter();
        assertTrue(scene.isActive());
        assertFalse(scene.isPaused());
    }

    @Test
    void testOnExit() {
        scene.onEnter();
        scene.onExit();
        assertFalse(scene.isActive());
        assertFalse(scene.isPaused());
    }

    @Test
    void testOnPause() {
        scene.onEnter();
        scene.onPause();
        assertTrue(scene.isActive());
        assertTrue(scene.isPaused());
    }

    @Test
    void testOnResume() {
        scene.onEnter();
        scene.onPause();
        scene.onResume();
        assertTrue(scene.isActive());
        assertFalse(scene.isPaused());
    }

    @Test
    void testUpdate() {
        scene.onEnter();
        scene.update(0.016f);
        assertTrue(scene.isUpdated());
    }

    @Test
    void testUpdateWhenPaused() {
        scene.onEnter();
        scene.onPause();
        scene.update(0.016f);
        assertFalse(scene.isUpdated());
    }

    @Test
    void testUpdateWhenInactive() {
        scene.update(0.016f);
        assertFalse(scene.isUpdated());
    }

    @Test
    void testRender() {
        scene.onEnter();
        scene.render();
        assertTrue(scene.isRendered());
    }

    @Test
    void testRenderWhenInactive() {
        scene.render();
        assertFalse(scene.isRendered());
    }

    @Test
    void testEquals() {
        UIScene a = new TestScene("Scene");
        UIScene b = new TestScene("Scene");
        assertEquals(a, b);
    }

    @Test
    void testHashCode() {
        UIScene a = new TestScene("Scene");
        UIScene b = new TestScene("Scene");
        assertEquals(a.hashCode(), b.hashCode());
    }

    private static class TestScene extends UIScene {
        private boolean updated;
        private boolean rendered;

        public TestScene(String name) {
            super(name);
        }

        @Override
        public void update(float deltaTime) {
            if (isActive() && !isPaused()) {
                updated = true;
            }
        }

        @Override
        public void render() {
            if (isActive()) {
                rendered = true;
            }
        }

        public boolean isUpdated() {
            return updated;
        }

        public boolean isRendered() {
            return rendered;
        }
    }
}
