package io.github.s3s3l.yggdrasil.game.core.ui.input;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 触摸事件单元测试
 */
class TouchEventTest {

    private TouchEvent event;

    @BeforeEach
    void setUp() {
        event = new TouchEvent();
    }

    @Test
    void testDefaultConstructor() {
        assertNotNull(event);
        assertEquals(TouchEvent.Action.DOWN, event.getAction());
        assertEquals(0, event.getPointerId());
        assertEquals(1.0f, event.getPressure(), 0.01f);
    }

    @Test
    void testConstructorWithParams() {
        TouchEvent e = new TouchEvent(100f, 200f, TouchEvent.Action.UP, 1);
        assertEquals(100f, e.getX(), 0.01f);
        assertEquals(200f, e.getY(), 0.01f);
        assertEquals(TouchEvent.Action.UP, e.getAction());
        assertEquals(1, e.getPointerId());
    }

    @Test
    void testConstructorWithPressure() {
        TouchEvent e = new TouchEvent(100f, 200f, TouchEvent.Action.MOVE, 0, 0.5f);
        assertEquals(0.5f, e.getPressure(), 0.01f);
    }

    @Test
    void testSet() {
        event.set(50f, 60f, TouchEvent.Action.CANCEL, 2);
        assertEquals(50f, event.getX(), 0.01f);
        assertEquals(60f, event.getY(), 0.01f);
        assertEquals(TouchEvent.Action.CANCEL, event.getAction());
        assertEquals(2, event.getPointerId());
    }

    @Test
    void testSetWithPressure() {
        event.set(50f, 60f, TouchEvent.Action.MOVE, 1, 0.8f);
        assertEquals(0.8f, event.getPressure(), 0.01f);
    }

    @Test
    void testCopyFrom() {
        TouchEvent source = new TouchEvent(100f, 200f, TouchEvent.Action.UP, 1, 0.5f);
        event.copyFrom(source);

        assertEquals(source.getX(), event.getX(), 0.01f);
        assertEquals(source.getY(), event.getY(), 0.01f);
        assertEquals(source.getAction(), event.getAction());
        assertEquals(source.getPointerId(), event.getPointerId());
        assertEquals(source.getPressure(), event.getPressure(), 0.01f);
    }

    @Test
    void testCopy() {
        TouchEvent source = new TouchEvent(100f, 200f, TouchEvent.Action.UP, 1, 0.5f);
        TouchEvent copy = source.copy();

        assertEquals(source.getX(), copy.getX(), 0.01f);
        assertEquals(source.getY(), copy.getY(), 0.01f);
        assertEquals(source.getAction(), copy.getAction());
        assertEquals(source.getPointerId(), copy.getPointerId());
        assertNotSame(source, copy);
    }

    @Test
    void testSetters() {
        event.setX(100f);
        event.setY(200f);
        event.setAction(TouchEvent.Action.UP);
        event.setPointerId(2);
        event.setPressure(0.5f);

        assertEquals(100f, event.getX(), 0.01f);
        assertEquals(200f, event.getY(), 0.01f);
        assertEquals(TouchEvent.Action.UP, event.getAction());
        assertEquals(2, event.getPointerId());
        assertEquals(0.5f, event.getPressure(), 0.01f);
    }

    @Test
    void testTimestamp() {
        long before = System.currentTimeMillis();
        TouchEvent e = new TouchEvent();
        long after = System.currentTimeMillis();

        assertTrue(e.getTimestamp() >= before);
        assertTrue(e.getTimestamp() <= after);
    }

    @Test
    void testSetTimestamp() {
        long customTimestamp = 1234567890L;
        event.setTimestamp(customTimestamp);
        assertEquals(customTimestamp, event.getTimestamp());
    }

    @Test
    void testToString() {
        TouchEvent e = new TouchEvent(100f, 200f, TouchEvent.Action.UP, 1);
        String str = e.toString();
        assertTrue(str.contains("TouchEvent"));
        assertTrue(str.contains("100"));
        assertTrue(str.contains("200"));
        assertTrue(str.contains("UP"));
    }
}
