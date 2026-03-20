package io.github.s3s3l.yggdrasil.game.core.ui.input;

/**
 * 手势枚举
 * 定义所有支持的手势类型
 */
public enum Gesture {
    /**
     * 向上滑动
     */
    SWIPE_UP,

    /**
     * 向下滑动
     */
    SWIPE_DOWN,

    /**
     * 向左滑动
     */
    SWIPE_LEFT,

    /**
     * 向右滑动
     */
    SWIPE_RIGHT,

    /**
     * 双击
     */
    DOUBLE_TAP,

    /**
     * 长按
     */
    LONG_PRESS,

    /**
     * 捏合（双指靠近）
     */
    PINCH_IN,

    /**
     * 张开（双指远离）
     */
    PINCH_OUT,

    /**
     * 点击
     */
    TAP,

    /**
     * 拖动开始
     */
    DRAG_START,

    /**
     * 拖动中
     */
    DRAGGING,

    /**
     * 拖动结束
     */
    DRAG_END;

    /**
     * 检查是否为滑动手势
     *
     * @return true 如果是滑动手势
     */
    public boolean isSwipe() {
        return this == SWIPE_UP || this == SWIPE_DOWN || this == SWIPE_LEFT || this == SWIPE_RIGHT;
    }

    /**
     * 检查是否为捏合手势
     *
     * @return true 如果是捏合手势
     */
    public boolean isPinch() {
        return this == PINCH_IN || this == PINCH_OUT;
    }

    /**
     * 检查是否为拖动手势
     *
     * @return true 如果是拖动手势
     */
    public boolean isDrag() {
        return this == DRAG_START || this == DRAGGING || this == DRAG_END;
    }

    /**
     * 获取滑动手势的方向
     *
     * @return 方向枚举，如果不是滑动手势则返回 null
     */
    public SwipeDirection getSwipeDirection() {
        switch (this) {
            case SWIPE_UP:
                return SwipeDirection.UP;
            case SWIPE_DOWN:
                return SwipeDirection.DOWN;
            case SWIPE_LEFT:
                return SwipeDirection.LEFT;
            case SWIPE_RIGHT:
                return SwipeDirection.RIGHT;
            default:
                return null;
        }
    }

    /**
     * 滑动方向枚举
     */
    public enum SwipeDirection {
        UP, DOWN, LEFT, RIGHT
    }
}
