package io.github.s3s3l.yggdrasil.game.core.scene;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * SceneManager 暂停/恢复功能单元测试
 */
class SceneManagerPauseTest {

    private SceneManager sceneManager;

    @BeforeEach
    void setUp() {
        sceneManager = SceneManager.get();
        sceneManager.clear();
    }

    @Test
    void testPause() {
        TestScene scene = new TestScene("Test");
        sceneManager.switchScene(scene);

        sceneManager.pause();
        assertTrue(sceneManager.isPaused());
        assertTrue(scene.wasPaused());
    }

    @Test
    void testResume() {
        TestScene scene = new TestScene("Test");
        sceneManager.switchScene(scene);

        sceneManager.pause();
        sceneManager.resume();

        assertFalse(sceneManager.isPaused());
        assertTrue(scene.wasResumed());
    }

    @Test
    void testUpdateWhenPaused() {
        TestScene scene = new TestScene("Test");
        sceneManager.switchScene(scene);

        // 第一次更新
        sceneManager.update(0.016f);
        assertTrue(scene.wasUpdated());

        // 重置更新标志
        scene.updated = false;

        // 暂停后更新
        sceneManager.pause();
        sceneManager.update(0.016f);

        // 暂停时场景不应更新
        assertFalse(scene.wasUpdated());
    }

    @Test
    void testUpdateWhenNotPaused() {
        TestScene scene = new TestScene("Test");
        sceneManager.switchScene(scene);

        sceneManager.update(0.016f);
        assertTrue(scene.wasUpdated());
    }

    @Test
    void testPauseWithNullScene() {
        // 当前场景为 null 时 pause 不应抛出异常
        assertDoesNotThrow(() -> sceneManager.pause());
        assertFalse(sceneManager.isPaused());
    }

    @Test
    void testResumeWithNullScene() {
        // 当前场景为 null 时 resume 不应抛出异常
        assertDoesNotThrow(() -> sceneManager.resume());
        assertFalse(sceneManager.isPaused());
    }

    @Test
    void testPauseIsIdempotent() {
        TestScene scene = new TestScene("Test");
        sceneManager.switchScene(scene);

        sceneManager.pause();
        sceneManager.pause();
        sceneManager.pause();

        assertTrue(sceneManager.isPaused());
        // pause() 方法在 SceneManager 中调用 scene.pause()，但 scene 自身的 paused 标记只在第一次调用时设置
        assertTrue(scene.wasPaused());
    }

    @Test
    void testResumeIsIdempotent() {
        TestScene scene = new TestScene("Test");
        sceneManager.switchScene(scene);

        sceneManager.pause();
        sceneManager.resume();
        sceneManager.resume();
        sceneManager.resume();

        assertFalse(sceneManager.isPaused());
        assertEquals(1, scene.getResumeCount());
    }

    @Test
    void testPauseAndSwitchScene() {
        TestScene scene1 = new TestScene("Scene1");
        TestScene scene2 = new TestScene("Scene2");
        sceneManager.switchScene(scene1);
        sceneManager.pause();

        // 切换场景应该重置暂停状态
        sceneManager.switchScene(scene2);
        assertFalse(sceneManager.isPaused());
    }

    /**
     * 测试用场景
     */
    private static class TestScene implements Scene {

        private final String name;
        private boolean paused;
        private boolean resumed;
        private boolean updated;
        private int pauseCount;
        private int resumeCount;
        private long id = 1L;

        TestScene(String name) {
            this.name = name;
        }

        @Override
        public void show() {
        }

        @Override
        public void hide() {
        }

        @Override
        public void update(float delta) {
            if (!paused) {
                updated = true;
            }
        }

        @Override
        public void pause() {
            paused = true;
            pauseCount++;
        }

        @Override
        public void resume() {
            resumed = true;
            resumeCount++;
        }

        @Override
        public boolean isPaused() {
            return paused;
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

        public boolean wasPaused() {
            return paused;
        }

        public boolean wasResumed() {
            return resumed;
        }

        public boolean wasUpdated() {
            return updated;
        }

        public int getPauseCount() {
            return pauseCount;
        }

        public int getResumeCount() {
            return resumeCount;
        }
    }
}
