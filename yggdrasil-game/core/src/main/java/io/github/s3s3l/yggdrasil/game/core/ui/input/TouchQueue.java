package io.github.s3s3l.yggdrasil.game.core.ui.input;

/**
 * 触摸事件队列
 * 使用环形缓冲区存储触摸事件
 */
public class TouchQueue {

    private final TouchEvent[] buffer;
    private final int capacity;
    private int head;
    private int tail;
    private int size;

    /**
     * 创建触摸事件队列
     */
    public TouchQueue() {
        this(64);
    }

    /**
     * 创建触摸事件队列
     *
     * @param capacity 队列容量
     */
    public TouchQueue(int capacity) {
        this.capacity = capacity;
        this.buffer = new TouchEvent[capacity];
        for (int i = 0; i < capacity; i++) {
            buffer[i] = new TouchEvent();
        }
        this.head = 0;
        this.tail = 0;
        this.size = 0;
    }

    /**
     * 入队事件
     *
     * @param event 事件
     * @return true 如果成功，false 如果队列已满
     */
    public boolean enqueue(TouchEvent event) {
        if (isFull()) {
            // 队列满时丢弃最旧的事件
            head = (head + 1) % capacity;
            size--;
        }

        buffer[tail].copyFrom(event);
        tail = (tail + 1) % capacity;
        size++;
        return true;
    }

    /**
     * 入队事件
     *
     * @param x         X 坐标
     * @param y         Y 坐标
     * @param action    动作类型
     * @param pointerId 指针 ID
     * @return true 如果成功
     */
    public boolean enqueue(float x, float y, TouchEvent.Action action, int pointerId) {
        TouchEvent event = buffer[tail];
        event.set(x, y, action, pointerId);
        tail = (tail + 1) % capacity;
        if (size < capacity) {
            size++;
        } else {
            head = (head + 1) % capacity;
        }
        return true;
    }

    /**
     * 出队事件
     *
     * @return 事件，如果队列为空则返回 null
     */
    public TouchEvent dequeue() {
        if (isEmpty()) {
            return null;
        }

        TouchEvent event = buffer[head];
        head = (head + 1) % capacity;
        size--;
        return event;
    }

    /**
     * 查看队首事件（不出队）
     *
     * @return 队首事件，如果队列为空则返回 null
     */
    public TouchEvent peek() {
        if (isEmpty()) {
            return null;
        }
        return buffer[head];
    }

    /**
     * 清空队列
     */
    public void clear() {
        head = 0;
        tail = 0;
        size = 0;
    }

    /**
     * 检查队列是否为空
     *
     * @return true 如果为空
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * 检查队列是否已满
     *
     * @return true 如果已满
     */
    public boolean isFull() {
        return size == capacity;
    }

    /**
     * 获取队列大小
     *
     * @return 队列中的事件数量
     */
    public int size() {
        return size;
    }

    /**
     * 获取队列容量
     *
     * @return 队列容量
     */
    public int getCapacity() {
        return capacity;
    }

    /**
     * 获取剩余可用空间
     *
     * @return 可用空间数量
     */
    public int remainingCapacity() {
        return capacity - size;
    }
}
