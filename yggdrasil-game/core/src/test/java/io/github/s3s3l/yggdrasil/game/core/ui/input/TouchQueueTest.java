package io.github.s3s3l.yggdrasil.game.core.ui.input;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 触摸事件队列单元测试
 */
class TouchQueueTest {

    private TouchQueue queue;

    @BeforeEach
    void setUp() {
        queue = new TouchQueue();
    }

    @Test
    void testDefaultCapacity() {
        assertEquals(64, queue.getCapacity());
    }

    @Test
    void testCustomCapacity() {
        TouchQueue q = new TouchQueue(32);
        assertEquals(32, q.getCapacity());
    }

    @Test
    void testEnqueueDequeue() {
        queue.enqueue(100f, 200f, TouchEvent.Action.DOWN, 0);
        TouchEvent event = queue.dequeue();

        assertNotNull(event);
        assertEquals(100f, event.getX(), 0.01f);
        assertEquals(200f, event.getY(), 0.01f);
        assertEquals(TouchEvent.Action.DOWN, event.getAction());
    }

    @Test
    void testEnqueueWithObject() {
        TouchEvent event = new TouchEvent(100f, 200f, TouchEvent.Action.UP, 1);
        queue.enqueue(event);
        TouchEvent dequeued = queue.dequeue();

        assertEquals(100f, dequeued.getX(), 0.01f);
        assertEquals(200f, dequeued.getY(), 0.01f);
    }

    @Test
    void testPeek() {
        queue.enqueue(100f, 200f, TouchEvent.Action.DOWN, 0);
        TouchEvent peeked = queue.peek();

        assertNotNull(peeked);
        assertEquals(100f, peeked.getX(), 0.01f);
        assertEquals(1, queue.size()); // peek 不减少大小
    }

    @Test
    void testIsEmpty() {
        assertTrue(queue.isEmpty());
        queue.enqueue(100f, 200f, TouchEvent.Action.DOWN, 0);
        assertFalse(queue.isEmpty());
    }

    @Test
    void testDequeueEmpty() {
        TouchEvent event = queue.dequeue();
        assertNull(event);
    }

    @Test
    void testClear() {
        queue.enqueue(100f, 200f, TouchEvent.Action.DOWN, 0);
        queue.enqueue(100f, 200f, TouchEvent.Action.UP, 0);
        queue.clear();

        assertTrue(queue.isEmpty());
        assertEquals(0, queue.size());
    }

    @Test
    void testSize() {
        assertEquals(0, queue.size());
        queue.enqueue(100f, 200f, TouchEvent.Action.DOWN, 0);
        assertEquals(1, queue.size());
        queue.enqueue(100f, 200f, TouchEvent.Action.UP, 0);
        assertEquals(2, queue.size());
    }

    @Test
    void testRemainingCapacity() {
        TouchQueue q = new TouchQueue(5);
        assertEquals(5, q.remainingCapacity());

        q.enqueue(100f, 200f, TouchEvent.Action.DOWN, 0);
        assertEquals(4, q.remainingCapacity());
    }

    @Test
    void testOverflowDiscardsOldest() {
        TouchQueue q = new TouchQueue(3);

        q.enqueue(10f, 10f, TouchEvent.Action.DOWN, 0);
        q.enqueue(20f, 20f, TouchEvent.Action.DOWN, 0);
        q.enqueue(30f, 30f, TouchEvent.Action.DOWN, 0);

        // 队列已满，新事件会丢弃最旧的
        q.enqueue(40f, 40f, TouchEvent.Action.DOWN, 0);

        assertEquals(3, q.size());
        TouchEvent e1 = q.dequeue();
        assertEquals(20f, e1.getX(), 0.01f); // 第一个事件被丢弃
    }

    @Test
    void testFIFOOrder() {
        queue.enqueue(10f, 10f, TouchEvent.Action.DOWN, 0);
        queue.enqueue(20f, 20f, TouchEvent.Action.DOWN, 0);
        queue.enqueue(30f, 30f, TouchEvent.Action.DOWN, 0);

        TouchEvent e1 = queue.dequeue();
        TouchEvent e2 = queue.dequeue();
        TouchEvent e3 = queue.dequeue();

        assertEquals(10f, e1.getX(), 0.01f);
        assertEquals(20f, e2.getX(), 0.01f);
        assertEquals(30f, e3.getX(), 0.01f);
    }

    @Test
    void testIsFull() {
        TouchQueue q = new TouchQueue(2);
        assertFalse(q.isFull());

        q.enqueue(10f, 10f, TouchEvent.Action.DOWN, 0);
        assertFalse(q.isFull());

        q.enqueue(20f, 20f, TouchEvent.Action.DOWN, 0);
        assertTrue(q.isFull());
    }
}
