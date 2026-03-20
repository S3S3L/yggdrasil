package io.github.s3s3l.yggdrasil.game.core.ui.scene;

import io.github.s3s3l.yggdrasil.game.core.ui.scene.SlideTransition.SlideDirection;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 滑动过渡单元测试
 */
class SlideTransitionTest {

    @Test
    void testConstructor() {
        SlideTransition transition = new SlideTransition(1.0f, SlideDirection.LEFT);
        assertEquals(1.0f, transition.getDuration(), 0.01f);
        assertEquals(SlideDirection.LEFT, transition.getDirection());
    }

    @Test
    void testSetDirection() {
        SlideTransition transition = new SlideTransition(1.0f, SlideDirection.LEFT);
        transition.setDirection(SlideDirection.RIGHT);
        assertEquals(SlideDirection.RIGHT, transition.getDirection());
    }

    @Test
    void testUpdateWithLeftDirection() {
        SlideTransition transition = new SlideTransition(1.0f, SlideDirection.LEFT);
        transition.begin(null, null);

        transition.update(0.5f);
        assertEquals(0, transition.getOffsetX(), 0.01f);
        assertEquals(0, transition.getOffsetY(), 0.01f);
    }

    @Test
    void testUpdateWithRightDirection() {
        SlideTransition transition = new SlideTransition(1.0f, SlideDirection.RIGHT);
        transition.begin(null, null);

        transition.update(0.25f);
        assertEquals(0.5f, transition.getOffsetX(), 0.01f);
        assertEquals(0, transition.getOffsetY(), 0.01f);
    }

    @Test
    void testUpdateWithUpDirection() {
        SlideTransition transition = new SlideTransition(1.0f, SlideDirection.UP);
        transition.begin(null, null);

        transition.update(0.5f);
        assertEquals(0, transition.getOffsetX(), 0.01f);
        assertEquals(0, transition.getOffsetY(), 0.01f);
    }

    @Test
    void testUpdateWithDownDirection() {
        SlideTransition transition = new SlideTransition(1.0f, SlideDirection.DOWN);
        transition.begin(null, null);

        transition.update(0.25f);
        assertEquals(0, transition.getOffsetX(), 0.01f);
        assertEquals(0.5f, transition.getOffsetY(), 0.01f);
    }

    @Test
    void testUpdateFinished() {
        SlideTransition transition = new SlideTransition(1.0f, SlideDirection.LEFT);
        transition.begin(null, null);

        transition.update(1.0f);
        assertEquals(0, transition.getOffsetX(), 0.01f);
        assertEquals(0, transition.getOffsetY(), 0.01f);
    }

    @Test
    void testRender() {
        SlideTransition transition = new SlideTransition(1.0f, SlideDirection.LEFT);
        transition.begin(null, null);

        // Should not throw
        transition.render();
    }
}
