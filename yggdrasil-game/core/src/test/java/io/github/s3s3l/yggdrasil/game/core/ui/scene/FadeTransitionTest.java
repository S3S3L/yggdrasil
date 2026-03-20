package io.github.s3s3l.yggdrasil.game.core.ui.scene;

import io.github.s3s3l.yggdrasil.game.core.ui.UIColor;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 淡入淡出过渡单元测试
 */
class FadeTransitionTest {

    @Test
    void testConstructorWithDuration() {
        FadeTransition transition = new FadeTransition(1.0f);
        assertEquals(1.0f, transition.getDuration(), 0.01f);
        assertEquals(UIColor.BLACK, transition.getColor());
    }

    @Test
    void testConstructorWithDurationAndColor() {
        FadeTransition transition = new FadeTransition(2.0f, UIColor.WHITE);
        assertEquals(2.0f, transition.getDuration(), 0.01f);
        assertEquals(UIColor.WHITE, transition.getColor());
    }

    @Test
    void testSetColor() {
        FadeTransition transition = new FadeTransition(1.0f);
        transition.setColor(UIColor.RED);
        assertEquals(UIColor.RED, transition.getColor());
    }

    @Test
    void testGetAlpha() {
        FadeTransition transition = new FadeTransition(1.0f);
        transition.begin(null, null);
        assertEquals(0, transition.getAlpha(), 0.01f);
    }

    @Test
    void testRender() {
        FadeTransition transition = new FadeTransition(1.0f);
        transition.begin(null, null);

        transition.update(0.25f);
        transition.render();
        assertEquals(0.5f, transition.getAlpha(), 0.01f);
    }

    @Test
    void testUpdateHalfWay() {
        FadeTransition transition = new FadeTransition(1.0f);
        transition.begin(null, null);

        transition.update(0.5f);
        assertEquals(1.0f, transition.getAlpha(), 0.01f);
    }

    @Test
    void testUpdateThreeQuarters() {
        FadeTransition transition = new FadeTransition(1.0f);
        transition.begin(null, null);

        transition.update(0.75f);
        assertEquals(0.5f, transition.getAlpha(), 0.01f);
    }

    @Test
    void testUpdateFinished() {
        FadeTransition transition = new FadeTransition(1.0f);
        transition.begin(null, null);

        transition.update(1.0f);
        assertEquals(0, transition.getAlpha(), 0.01f);
    }

    @Test
    void testOnBegin() {
        FadeTransition transition = new FadeTransition(1.0f);
        transition.begin(null, null);
        assertEquals(0, transition.getAlpha(), 0.01f);
    }

    @Test
    void testOnFinish() {
        FadeTransition transition = new FadeTransition(1.0f);
        transition.begin(null, null);
        transition.update(1.0f);

        assertEquals(0, transition.getAlpha(), 0.01f);
    }
}
