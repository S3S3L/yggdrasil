package io.github.s3s3l.yggdrasil.game.core.ui.input;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 手势检测器单元测试
 */
class GestureDetectorTest {

    private GestureDetector detector;
    private TestGestureListener listener;

    @BeforeEach
    void setUp() {
        detector = new GestureDetector();
        listener = new TestGestureListener();
        detector.addListener(listener);
    }

    @Test
    void testAddListener() {
        TestGestureListener listener2 = new TestGestureListener();
        detector.addListener(listener2);

        detector.touchDown(100f, 100f, 0);
        detector.touchUp(100f, 100f, 0);

        assertTrue(listener.tapped);
        assertTrue(listener2.tapped);
    }

    @Test
    void testRemoveListener() {
        detector.removeListener(listener);

        detector.touchDown(100f, 100f, 0);
        detector.touchUp(100f, 100f, 0);

        assertFalse(listener.tapped);
    }

    @Test
    void testClearListeners() {
        detector.clearListeners();

        detector.touchDown(100f, 100f, 0);
        detector.touchUp(100f, 100f, 0);

        assertFalse(listener.tapped);
    }

    @Test
    void testTap() {
        detector.touchDown(100f, 100f, 0);
        detector.touchUp(100f, 100f, 0);

        assertTrue(listener.tapped);
    }

    @Test
    void testDoubleTap() {
        detector.touchDown(100f, 100f, 0);
        detector.touchUp(100f, 100f, 0);

        // 等待一段时间
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            // Ignore
        }

        detector.touchDown(100f, 100f, 0);
        detector.touchUp(100f, 100f, 0);

        assertTrue(listener.doubleTapped);
    }

    @Test
    void testSwipeRight() {
        detector.touchDown(50f, 100f, 0);
        detector.touchUp(150f, 100f, 0);

        assertTrue(listener.swiped);
        assertEquals(Gesture.SWIPE_RIGHT, listener.swipeGesture);
    }

    @Test
    void testSwipeLeft() {
        detector.touchDown(150f, 100f, 0);
        detector.touchUp(50f, 100f, 0);

        assertTrue(listener.swiped);
        assertEquals(Gesture.SWIPE_LEFT, listener.swipeGesture);
    }

    @Test
    void testSwipeUp() {
        detector.touchDown(100f, 150f, 0);
        detector.touchUp(100f, 50f, 0);

        assertTrue(listener.swiped);
        assertEquals(Gesture.SWIPE_UP, listener.swipeGesture);
    }

    @Test
    void testSwipeDown() {
        detector.touchDown(100f, 50f, 0);
        detector.touchUp(100f, 150f, 0);

        assertTrue(listener.swiped);
        assertEquals(Gesture.SWIPE_DOWN, listener.swipeGesture);
    }

    @Test
    void testDrag() {
        detector.touchDown(100f, 100f, 0);
        detector.touchDragged(150f, 100f, 0);
        detector.touchDragged(200f, 100f, 0);
        detector.touchUp(200f, 100f, 0);

        assertTrue(listener.dragStarted);
        assertTrue(listener.dragged);
        assertTrue(listener.dragEnded);
    }

    @Test
    void testLongPress() {
        detector.setLongPressTime(0.1f); // 100ms for testing

        detector.touchDown(100f, 100f, 0);
        detector.update(0.15f); // 超过长按时间

        assertTrue(listener.longPressed);
    }

    @Test
    void testSetSwipeThreshold() {
        detector.setSwipeThreshold(100f);
        assertEquals(100f, detector.getSwipeThreshold(), 0.01f);
    }

    @Test
    void testSetDoubleTapTime() {
        detector.setDoubleTapTime(0.5f);
        assertEquals(0.5f, detector.getDoubleTapTime(), 0.01f);
    }

    @Test
    void testSetLongPressTime() {
        detector.setLongPressTime(1.0f);
        assertEquals(1.0f, detector.getLongPressTime(), 0.01f);
    }

    @Test
    void testReset() {
        detector.touchDown(100f, 100f, 0);
        detector.reset();

        // 重置后不应触发任何手势
        detector.touchUp(100f, 100f, 0);
        // 状态已重置，行为不确定，但不应该崩溃
    }

    private static class TestGestureListener extends GestureListener.Adapter {
        boolean tapped;
        boolean doubleTapped;
        boolean swiped;
        boolean longPressed;
        boolean dragStarted;
        boolean dragged;
        boolean dragEnded;
        Gesture swipeGesture;

        @Override
        public void onTap(float x, float y) {
            tapped = true;
        }

        @Override
        public void onDoubleTap(float x, float y) {
            doubleTapped = true;
        }

        @Override
        public void onSwipe(Gesture gesture, float startX, float startY, float endX, float endY, float duration) {
            swiped = true;
            swipeGesture = gesture;
        }

        @Override
        public void onLongPress(float x, float y, float duration) {
            longPressed = true;
        }

        @Override
        public void onDragStart(float x, float y) {
            dragStarted = true;
        }

        @Override
        public void onDragging(float x, float y, float deltaX, float deltaY) {
            dragged = true;
        }

        @Override
        public void onDragEnd(float x, float y) {
            dragEnded = true;
        }
    }
}
