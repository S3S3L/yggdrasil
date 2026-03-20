package io.github.s3s3l.yggdrasil.game.core.ui.input;

import java.util.ArrayList;
import java.util.List;

/**
 * 手势检测器
 * 识别各种触摸手势
 */
public class GestureDetector {

    // 配置参数
    private float swipeThreshold;
    private float doubleTapTime;
    private float longPressTime;
    private float pinchThreshold;

    // 状态变量
    private float lastTouchX;
    private float lastTouchY;
    private float firstTouchX;
    private float firstTouchY;
    private float lastTapTime;
    private float pressStartTime;
    private boolean isPressing;
    private boolean isDragging;
    private boolean hasDoubleTap;

    // 双指捏合状态
    private float lastPointerDistance;

    // 监听器
    private final List<GestureListener> listeners;

    /**
     * 创建手势检测器
     */
    public GestureDetector() {
        this.swipeThreshold = 50f;
        this.doubleTapTime = 0.3f;
        this.longPressTime = 0.5f;
        this.pinchThreshold = 20f;
        this.listeners = new ArrayList<>();
        reset();
    }

    /**
     * 创建手势检测器
     *
     * @param swipeThreshold  滑动阈值（像素）
     * @param doubleTapTime   双击时间间隔（秒）
     * @param longPressTime   长按时间（秒）
     */
    public GestureDetector(float swipeThreshold, float doubleTapTime, float longPressTime) {
        this.swipeThreshold = swipeThreshold;
        this.doubleTapTime = doubleTapTime;
        this.longPressTime = longPressTime;
        this.pinchThreshold = 20f;
        this.listeners = new ArrayList<>();
        reset();
    }

    /**
     * 添加手势监听器
     *
     * @param listener 监听器
     */
    public void addListener(GestureListener listener) {
        if (listener != null && !listeners.contains(listener)) {
            listeners.add(listener);
        }
    }

    /**
     * 移除手势监听器
     *
     * @param listener 监听器
     */
    public void removeListener(GestureListener listener) {
        listeners.remove(listener);
    }

    /**
     * 清除所有监听器
     */
    public void clearListeners() {
        listeners.clear();
    }

    /**
     * 重置状态
     */
    public void reset() {
        lastTouchX = 0;
        lastTouchY = 0;
        firstTouchX = 0;
        firstTouchY = 0;
        lastTapTime = 0;
        pressStartTime = 0;
        isPressing = false;
        isDragging = false;
        hasDoubleTap = false;
        lastPointerDistance = 0;
        accumulatedTime = 0;
    }

    // ==================== 触摸事件处理 ====================

    /**
     * 处理触摸按下事件
     *
     * @param x         触摸 X 坐标
     * @param y         触摸 Y 坐标
     * @param pointerId 指针 ID
     */
    public void touchDown(float x, float y, int pointerId) {
        if (pointerId == 0) {
            firstTouchX = x;
            firstTouchY = y;
            lastTouchX = x;
            lastTouchY = y;
            pressStartTime = getTime();
            isPressing = true;
            isDragging = false;
            hasDoubleTap = false;
        }
    }

    /**
     * 处理触摸释放事件
     *
     * @param x         触摸 X 坐标
     * @param y         触摸 Y 坐标
     * @param pointerId 指针 ID
     */
    public void touchUp(float x, float y, int pointerId) {
        if (pointerId == 0 && isPressing) {
            float duration = getTime() - pressStartTime;
            float deltaX = x - firstTouchX;
            float deltaY = y - firstTouchY;
            float distance = (float) Math.sqrt(deltaX * deltaX + deltaY * deltaY);

            // 检查是否为双击
            float tapTime = getTime() - lastTapTime;
            if (tapTime < doubleTapTime && distance < swipeThreshold) {
                fireDoubleTap(x, y);
                hasDoubleTap = true;
            } else if (distance < swipeThreshold) {
                // 检查是否为点击
                fireTap(x, y);
            } else {
                // 检查是否为滑动
                if (distance >= swipeThreshold) {
                    Gesture swipeGesture = detectSwipeGesture(deltaX, deltaY);
                    fireSwipe(swipeGesture, firstTouchX, firstTouchY, x, y, duration);
                }
            }

            // 检查是否为拖动结束
            if (isDragging) {
                fireDragEnd(x, y);
            }

            lastTapTime = getTime();
            isPressing = false;
            isDragging = false;
        }
    }

    /**
     * 处理触摸拖动事件
     *
     * @param x         触摸 X 坐标
     * @param y         触摸 Y 坐标
     * @param pointerId 指针 ID
     */
    public void touchDragged(float x, float y, int pointerId) {
        if (pointerId == 0 && isPressing) {
            float deltaX = x - lastTouchX;
            float deltaY = y - lastTouchY;
            float totalDeltaX = x - firstTouchX;
            float totalDeltaY = y - firstTouchY;
            float totalDistance = (float) Math.sqrt(totalDeltaX * totalDeltaX + totalDeltaY * totalDeltaY);

            // 检查是否开始拖动
            if (!isDragging && totalDistance >= swipeThreshold) {
                isDragging = true;
                fireDragStart(firstTouchX, firstTouchY);
            }

            // 拖动中
            if (isDragging) {
                fireDragging(x, y, deltaX, deltaY);
            }

            lastTouchX = x;
            lastTouchY = y;
        }
    }

    // 累积时间（用于支持模拟时间的测试）
    private float accumulatedTime;

    /**
     * 处理长按检测（需要在 update 中调用）
     *
     * @param deltaTime 时间增量
     */
    public void update(float deltaTime) {
        if (isPressing && !isDragging) {
            // 使用累积时间（支持测试模拟）或真实时间
            accumulatedTime += deltaTime;
            float duration = accumulatedTime;

            // 如果累积时间超过阈值，触发长按
            if (duration >= longPressTime) {
                fireLongPress(firstTouchX, firstTouchY, duration);
                isPressing = false;
                accumulatedTime = 0;
            }
        }
    }

    // ==================== 双指捏合处理 ====================

    /**
     * 处理双指捏合
     *
     * @param x1, y1    第一指坐标
     * @param x2, y2    第二指坐标
     */
    public void pinch(float x1, float y1, float x2, float y2) {
        float distance = calculateDistance(x1, y1, x2, y2);

        if (lastPointerDistance > 0) {
            float delta = distance - lastPointerDistance;

            if (Math.abs(delta) >= pinchThreshold) {
                float factor = distance / lastPointerDistance;
                if (delta > 0) {
                    firePinch(Gesture.PINCH_OUT, factor);
                } else {
                    firePinch(Gesture.PINCH_IN, factor);
                }
            }
        }

        lastPointerDistance = distance;
    }

    // ==================== 辅助方法 ====================

    /**
     * 检测滑动手势类型
     */
    private Gesture detectSwipeGesture(float deltaX, float deltaY) {
        if (Math.abs(deltaX) > Math.abs(deltaY)) {
            return deltaX > 0 ? Gesture.SWIPE_RIGHT : Gesture.SWIPE_LEFT;
        } else {
            return deltaY > 0 ? Gesture.SWIPE_DOWN : Gesture.SWIPE_UP;
        }
    }

    /**
     * 计算两点距离
     */
    private float calculateDistance(float x1, float y1, float x2, float y2) {
        float dx = x2 - x1;
        float dy = y2 - y1;
        return (float) Math.sqrt(dx * dx + dy * dy);
    }

    /**
     * 获取当前时间（秒）
     */
    protected float getTime() {
        return System.currentTimeMillis() / 1000f;
    }

    // ==================== 事件触发方法 ====================

    private void fireSwipe(Gesture gesture, float startX, float startY, float endX, float endY, float duration) {
        for (GestureListener listener : listeners) {
            listener.onSwipe(gesture, startX, startY, endX, endY, duration);
        }
    }

    private void fireDoubleTap(float x, float y) {
        for (GestureListener listener : listeners) {
            listener.onDoubleTap(x, y);
        }
    }

    private void fireLongPress(float x, float y, float duration) {
        for (GestureListener listener : listeners) {
            listener.onLongPress(x, y, duration);
        }
    }

    private void firePinch(Gesture gesture, float factor) {
        for (GestureListener listener : listeners) {
            listener.onPinch(gesture, factor);
        }
    }

    private void fireTap(float x, float y) {
        for (GestureListener listener : listeners) {
            listener.onTap(x, y);
        }
    }

    private void fireDragStart(float x, float y) {
        for (GestureListener listener : listeners) {
            listener.onDragStart(x, y);
        }
    }

    private void fireDragging(float x, float y, float deltaX, float deltaY) {
        for (GestureListener listener : listeners) {
            listener.onDragging(x, y, deltaX, deltaY);
        }
    }

    private void fireDragEnd(float x, float y) {
        for (GestureListener listener : listeners) {
            listener.onDragEnd(x, y);
        }
    }

    // ==================== Getter/Setter ====================

    public float getSwipeThreshold() {
        return swipeThreshold;
    }

    public void setSwipeThreshold(float swipeThreshold) {
        this.swipeThreshold = swipeThreshold;
    }

    public float getDoubleTapTime() {
        return doubleTapTime;
    }

    public void setDoubleTapTime(float doubleTapTime) {
        this.doubleTapTime = doubleTapTime;
    }

    public float getLongPressTime() {
        return longPressTime;
    }

    public void setLongPressTime(float longPressTime) {
        this.longPressTime = longPressTime;
    }

    public float getPinchThreshold() {
        return pinchThreshold;
    }

    public void setPinchThreshold(float pinchThreshold) {
        this.pinchThreshold = pinchThreshold;
    }
}
