package io.github.s3s3l.yggdrasil.game.core.ui.input;

/**
 * 手势监听器接口
 * 用于接收手势识别事件
 */
public interface GestureListener {

    /**
     * 滑动手势触发时调用
     *
     * @param gesture   手势类型
     * @param startX    起始 X 坐标
     * @param startY    起始 Y 坐标
     * @param endX      结束 X 坐标
     * @param endY      结束 Y 坐标
     * @param duration  手势持续时间（秒）
     */
    void onSwipe(Gesture gesture, float startX, float startY, float endX, float endY, float duration);

    /**
     * 双击手势触发时调用
     *
     * @param x         点击 X 坐标
     * @param y         点击 Y 坐标
     */
    void onDoubleTap(float x, float y);

    /**
     * 长按手势触发时调用
     *
     * @param x         按压 X 坐标
     * @param y         按压 Y 坐标
     * @param duration  按压持续时间（秒）
     */
    void onLongPress(float x, float y, float duration);

    /**
     * 捏合手势触发时调用
     *
     * @param gesture   捏合类型（PINCH_IN 或 PINCH_OUT）
     * @param factor    缩放因子（>1 表示放大，<1 表示缩小）
     */
    void onPinch(Gesture gesture, float factor);

    /**
     * 点击手势触发时调用
     *
     * @param x         点击 X 坐标
     * @param y         点击 Y 坐标
     */
    void onTap(float x, float y);

    /**
     * 拖动开始时调用
     *
     * @param x         起始 X 坐标
     * @param y         起始 Y 坐标
     */
    void onDragStart(float x, float y);

    /**
     * 拖动中调用
     *
     * @param x         当前 X 坐标
     * @param y         当前 Y 坐标
     * @param deltaX    X 轴变化量
     * @param deltaY    Y 轴变化量
     */
    void onDragging(float x, float y, float deltaX, float deltaY);

    /**
     * 拖动结束时调用
     *
     * @param x         结束 X 坐标
     * @param y         结束 Y 坐标
     */
    void onDragEnd(float x, float y);

    /**
     * 手势监听器适配器类
     * 提供空实现，方便只重写需要的方法
     */
    abstract class Adapter implements GestureListener {
        @Override
        public void onSwipe(Gesture gesture, float startX, float startY, float endX, float endY, float duration) {
            // 默认空实现
        }

        @Override
        public void onDoubleTap(float x, float y) {
            // 默认空实现
        }

        @Override
        public void onLongPress(float x, float y, float duration) {
            // 默认空实现
        }

        @Override
        public void onPinch(Gesture gesture, float factor) {
            // 默认空实现
        }

        @Override
        public void onTap(float x, float y) {
            // 默认空实现
        }

        @Override
        public void onDragStart(float x, float y) {
            // 默认空实现
        }

        @Override
        public void onDragging(float x, float y, float deltaX, float deltaY) {
            // 默认空实现
        }

        @Override
        public void onDragEnd(float x, float y) {
            // 默认空实现
        }
    }
}
