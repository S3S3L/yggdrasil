package io.github.s3s3l.yggdrasil.game._2d.event.mouse;

import io.github.s3s3l.yggdrasil.game._2d.event.InputEvent;
import io.github.s3s3l.yggdrasil.game.core.event.EventPriority;
import lombok.Getter;

/**
 * 鼠标滚轮事件
 */
@Getter
public class MouseScrollEvent extends InputEvent {

    /**
     * 滚动量（正数=向上滚动，负数=向下滚动）
     */
    private final float amount;

    /**
     * 鼠标 X 坐标
     */
    private final float x;

    /**
     * 鼠标 Y 坐标
     */
    private final float y;

    private MouseScrollEvent(Object source, float amount, float x, float y, EventPriority priority) {
        super(source, priority);
        this.amount = amount;
        this.x = x;
        this.y = y;
    }

    @Override
    public InputDeviceType getDeviceType() {
        return InputDeviceType.MOUSE;
    }

    /**
     * 创建鼠标滚轮事件
     *
     * @param source 事件源
     * @param amount 滚动量
     * @param x      X 坐标
     * @param y      Y 坐标
     * @return 鼠标滚轮事件
     */
    public static MouseScrollEvent create(Object source, float amount, float x, float y) {
        return new MouseScrollEvent(source, amount, x, y, EventPriority.NORMAL);
    }

    /**
     * 判断是否向上滚动
     *
     * @return true 如果向上滚动
     */
    public boolean isScrollUp() {
        return amount > 0;
    }

    /**
     * 判断是否向下滚动
     *
     * @return true 如果向下滚动
     */
    public boolean isScrollDown() {
        return amount < 0;
    }
}
