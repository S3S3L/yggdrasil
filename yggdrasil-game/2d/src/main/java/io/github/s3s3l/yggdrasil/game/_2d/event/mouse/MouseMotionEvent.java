package io.github.s3s3l.yggdrasil.game._2d.event.mouse;

import io.github.s3s3l.yggdrasil.game._2d.event.InputEvent;
import io.github.s3s3l.yggdrasil.game.core.event.EventPriority;
import lombok.Getter;

/**
 * 鼠标移动事件
 */
@Getter
public class MouseMotionEvent extends InputEvent {

    /**
     * 当前 X 坐标
     */
    private final float x;

    /**
     * 当前 Y 坐标
     */
    private final float y;

    /**
     * X 方向移动量
     */
    private final float deltaX;

    /**
     * Y 方向移动量
     */
    private final float deltaY;

    private MouseMotionEvent(Object source, float x, float y, float deltaX, float deltaY, EventPriority priority) {
        super(source, priority);
        this.x = x;
        this.y = y;
        this.deltaX = deltaX;
        this.deltaY = deltaY;
    }

    @Override
    public InputDeviceType getDeviceType() {
        return InputDeviceType.MOUSE;
    }

    /**
     * 创建鼠标移动事件
     *
     * @param source 事件源
     * @param x      X 坐标
     * @param y      Y 坐标
     * @param deltaX X 方向移动量
     * @param deltaY Y 方向移动量
     * @return 鼠标移动事件
     */
    public static MouseMotionEvent create(Object source, float x, float y, float deltaX, float deltaY) {
        return new MouseMotionEvent(source, x, y, deltaX, deltaY, EventPriority.NORMAL);
    }

    /**
     * 判断是否为快速移动
     *
     * @param threshold 阈值
     * @return true 如果移动距离超过阈值
     */
    public boolean isQuickMove(float threshold) {
        return Math.abs(deltaX) > threshold || Math.abs(deltaY) > threshold;
    }

    /**
     * 获取移动距离
     *
     * @return 移动距离
     */
    public float getDistance() {
        return (float) Math.sqrt(deltaX * deltaX + deltaY * deltaY);
    }
}
