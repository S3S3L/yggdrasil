package io.github.s3s3l.yggdrasil.game.core.ui.scene;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 场景过渡动画单元测试
 */
class SceneTransitionTest {

    @Test
    void testGetDuration() {
        TestTransition transition = new TestTransition(1.0f);
        assertEquals(1.0f, transition.getDuration(), 0.01f);
    }

    @Test
    void testSetDuration() {
        TestTransition transition = new TestTransition(1.0f);
        transition.setDuration(2.0f);
        assertEquals(2.0f, transition.getDuration(), 0.01f);
    }

    @Test
    void testGetProgress() {
        TestTransition transition = new TestTransition(1.0f);
        transition.begin(null, null);

        assertEquals(0, transition.getProgress(), 0.01f);

        transition.update(0.5f);
        assertEquals(0.5, transition.getProgress(), 0.01f);

        transition.update(0.5f);
        assertEquals(1.0, transition.getProgress(), 0.01f);
    }

    @Test
    void testIsFinished() {
        TestTransition transition = new TestTransition(0.5f);
        transition.begin(null, null);

        assertFalse(transition.isFinished());

        transition.update(0.5f);
        assertTrue(transition.isFinished());
    }

    @Test
    void testGetFromAndToScene() {
        TestTransition transition = new TestTransition(1.0f);
        TestScene from = new TestScene("From");
        TestScene to = new TestScene("To");

        transition.begin(from, to);

        assertEquals(from, transition.getFromScene());
        assertEquals(to, transition.getToScene());
    }

    @Test
    void testBegin() {
        TestTransition transition = new TestTransition(1.0f);
        TestScene from = new TestScene("From");
        TestScene to = new TestScene("To");

        transition.begin(from, to);

        assertTrue(transition.wasBeginCalled());
    }

    @Test
    void testUpdate() {
        TestTransition transition = new TestTransition(1.0f);
        transition.begin(null, null);

        transition.update(0.3f);
        assertEquals(1, transition.getUpdateCount());
    }

    @Test
    void testUpdateFinishes() {
        TestTransition transition = new TestTransition(0.5f);
        transition.begin(null, null);

        transition.update(0.5f);
        assertTrue(transition.isFinished());
        assertTrue(transition.wasFinishCalled());
    }

    @Test
    void testCancel() {
        TestTransition transition = new TestTransition(1.0f);
        transition.begin(null, null);

        transition.cancel();
        assertTrue(transition.isFinished());
        assertTrue(transition.wasCancelCalled());
    }

    @Test
    void testUpdateAfterFinished() {
        TestTransition transition = new TestTransition(0.3f);
        transition.begin(null, null);

        // First update completes the transition (no onUpdate called)
        transition.update(0.3f);
        // Second update should not call onUpdate since already finished
        transition.update(0.1f);

        assertTrue(transition.isFinished());
        // onUpdate is only called when elapsed < duration, so 0 times here
        assertEquals(0, transition.getUpdateCount());
    }

    @Test
    void testGetProgressWithZeroDuration() {
        TestTransition transition = new TestTransition(0);
        transition.begin(null, null);
        assertEquals(1, transition.getProgress(), 0.01f);
    }

    private static class TestTransition extends SceneTransition {
        private boolean beginCalled;
        private boolean finishCalled;
        private boolean cancelCalled;
        private int updateCount;

        public TestTransition(float duration) {
            super(duration);
        }

        @Override
        public void render() {
            // No-op for testing
        }

        @Override
        protected void onBegin(UIScene fromScene, UIScene toScene) {
            beginCalled = true;
        }

        @Override
        protected void onUpdate(float progress) {
            updateCount++;
        }

        @Override
        protected void onFinish() {
            finishCalled = true;
        }

        @Override
        protected void onCancel() {
            cancelCalled = true;
        }

        public boolean wasBeginCalled() {
            return beginCalled;
        }

        public boolean wasFinishCalled() {
            return finishCalled;
        }

        public boolean wasCancelCalled() {
            return cancelCalled;
        }

        public int getUpdateCount() {
            return updateCount;
        }
    }

    private static class TestScene extends UIScene {
        public TestScene(String name) {
            super(name);
        }
    }
}
