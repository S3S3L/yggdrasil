package io.github.s3s3l.yggdrasil.game.core.ui.scene;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 缩放过渡单元测试
 */
class ZoomTransitionTest {

    @Test
    void testConstructorWithDefault() {
        ZoomTransition transition = new ZoomTransition(1.0f);
        assertEquals(1.0f, transition.getDuration(), 0.01f);
        assertEquals(0.5f, transition.getStartScale(), 0.01f);
        assertEquals(1.0f, transition.getEndScale(), 0.01f);
    }

    @Test
    void testConstructorWithScales() {
        ZoomTransition transition = new ZoomTransition(1.0f, 0.0f, 2.0f);
        assertEquals(0.0f, transition.getStartScale(), 0.01f);
        assertEquals(2.0f, transition.getEndScale(), 0.01f);
    }

    @Test
    void testSetStartScale() {
        ZoomTransition transition = new ZoomTransition(1.0f);
        transition.setStartScale(0.0f);
        assertEquals(0.0f, transition.getStartScale(), 0.01f);
    }

    @Test
    void testSetEndScale() {
        ZoomTransition transition = new ZoomTransition(1.0f);
        transition.setEndScale(1.5f);
        assertEquals(1.5f, transition.getEndScale(), 0.01f);
    }

    @Test
    void testGetCurrentScale() {
        ZoomTransition transition = new ZoomTransition(1.0f, 0.5f, 1.5f);
        transition.begin(null, null);

        assertEquals(0.5f, transition.getCurrentScale(), 0.01f);

        transition.update(0.5f);
        assertEquals(1.0f, transition.getCurrentScale(), 0.01f);

        transition.update(0.5f);
        assertEquals(1.5f, transition.getCurrentScale(), 0.01f);
    }

    @Test
    void testUpdate() {
        ZoomTransition transition = new ZoomTransition(1.0f, 0.0f, 1.0f);
        transition.begin(null, null);

        transition.update(0.5f);
        assertEquals(0.5f, transition.getCurrentScale(), 0.01f);
    }

    @Test
    void testUpdateFinished() {
        ZoomTransition transition = new ZoomTransition(1.0f, 0.5f, 1.5f);
        transition.begin(null, null);

        transition.update(1.0f);
        assertEquals(1.5f, transition.getCurrentScale(), 0.01f);
    }

    @Test
    void testRender() {
        ZoomTransition transition = new ZoomTransition(1.0f);
        transition.begin(null, null);

        // Should not throw
        transition.render();
    }
}
