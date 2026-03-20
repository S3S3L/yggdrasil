package io.github.s3s3l.yggdrasil.game.core.ui.scene;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 交叉淡出过渡单元测试
 */
class CrossFadeTransitionTest {

    @Test
    void testConstructor() {
        CrossFadeTransition transition = new CrossFadeTransition(1.0f);
        assertEquals(1.0f, transition.getDuration(), 0.01f);
    }

    @Test
    void testGetFromAlpha() {
        CrossFadeTransition transition = new CrossFadeTransition(1.0f);
        transition.begin(null, null);
        assertEquals(1.0f, transition.getFromAlpha(), 0.01f);
    }

    @Test
    void testGetToAlpha() {
        CrossFadeTransition transition = new CrossFadeTransition(1.0f);
        transition.begin(null, null);
        assertEquals(0, transition.getToAlpha(), 0.01f);
    }

    @Test
    void testOnBegin() {
        CrossFadeTransition transition = new CrossFadeTransition(1.0f);
        transition.begin(null, null);

        assertEquals(1.0f, transition.getFromAlpha(), 0.01f);
        assertEquals(0, transition.getToAlpha(), 0.01f);
    }

    @Test
    void testUpdateHalfWay() {
        CrossFadeTransition transition = new CrossFadeTransition(1.0f);
        transition.begin(null, null);

        transition.update(0.5f);
        assertEquals(0.5f, transition.getFromAlpha(), 0.01f);
        assertEquals(0.5f, transition.getToAlpha(), 0.01f);
    }

    @Test
    void testUpdateQuarter() {
        CrossFadeTransition transition = new CrossFadeTransition(1.0f);
        transition.begin(null, null);

        transition.update(0.25f);
        assertEquals(0.75f, transition.getFromAlpha(), 0.01f);
        assertEquals(0.25f, transition.getToAlpha(), 0.01f);
    }

    @Test
    void testUpdateThreeQuarters() {
        CrossFadeTransition transition = new CrossFadeTransition(1.0f);
        transition.begin(null, null);

        transition.update(0.75f);
        assertEquals(0.25f, transition.getFromAlpha(), 0.01f);
        assertEquals(0.75f, transition.getToAlpha(), 0.01f);
    }

    @Test
    void testUpdateFinished() {
        CrossFadeTransition transition = new CrossFadeTransition(1.0f);
        transition.begin(null, null);

        transition.update(1.0f);
        assertEquals(0, transition.getFromAlpha(), 0.01f);
        assertEquals(1.0f, transition.getToAlpha(), 0.01f);
    }

    @Test
    void testRender() {
        CrossFadeTransition transition = new CrossFadeTransition(1.0f);
        transition.begin(null, null);

        // Should not throw
        transition.render();
    }
}
