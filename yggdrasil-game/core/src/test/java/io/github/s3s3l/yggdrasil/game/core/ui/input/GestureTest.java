package io.github.s3s3l.yggdrasil.game.core.ui.input;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 手势枚举单元测试
 */
class GestureTest {

    @Test
    void testIsSwipe() {
        assertTrue(Gesture.SWIPE_UP.isSwipe());
        assertTrue(Gesture.SWIPE_DOWN.isSwipe());
        assertTrue(Gesture.SWIPE_LEFT.isSwipe());
        assertTrue(Gesture.SWIPE_RIGHT.isSwipe());
        assertFalse(Gesture.DOUBLE_TAP.isSwipe());
        assertFalse(Gesture.TAP.isSwipe());
    }

    @Test
    void testIsPinch() {
        assertTrue(Gesture.PINCH_IN.isPinch());
        assertTrue(Gesture.PINCH_OUT.isPinch());
        assertFalse(Gesture.SWIPE_UP.isPinch());
    }

    @Test
    void testIsDrag() {
        assertTrue(Gesture.DRAG_START.isDrag());
        assertTrue(Gesture.DRAGGING.isDrag());
        assertTrue(Gesture.DRAG_END.isDrag());
        assertFalse(Gesture.SWIPE_UP.isDrag());
    }

    @Test
    void testGetSwipeDirection() {
        assertEquals(Gesture.SwipeDirection.UP, Gesture.SWIPE_UP.getSwipeDirection());
        assertEquals(Gesture.SwipeDirection.DOWN, Gesture.SWIPE_DOWN.getSwipeDirection());
        assertEquals(Gesture.SwipeDirection.LEFT, Gesture.SWIPE_LEFT.getSwipeDirection());
        assertEquals(Gesture.SwipeDirection.RIGHT, Gesture.SWIPE_RIGHT.getSwipeDirection());
        assertNull(Gesture.DOUBLE_TAP.getSwipeDirection());
    }

    @Test
    void testValueOf() {
        assertEquals(Gesture.SWIPE_UP, Gesture.valueOf("SWIPE_UP"));
        assertEquals(Gesture.DOUBLE_TAP, Gesture.valueOf("DOUBLE_TAP"));
        assertEquals(Gesture.LONG_PRESS, Gesture.valueOf("LONG_PRESS"));
    }

    @Test
    void testValues() {
        Gesture[] gestures = Gesture.values();
        assertTrue(gestures.length >= 10);
    }
}
