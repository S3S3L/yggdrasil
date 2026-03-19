package io.github.s3s3l.yggdrasil.game._2d.event;

import io.github.s3s3l.yggdrasil.game.core.event.Event;
import io.github.s3s3l.yggdrasil.game.core.event.EventContext;
import io.github.s3s3l.yggdrasil.game.core.event.EventPriority;
import lombok.Getter;

/**
 * 输入事件抽象基类
 * 所有用户输入相关事件的父类
 */
@Getter
public abstract class InputEvent extends EventContext implements Event {

    /**
     * 事件优先级
     */
    private final EventPriority priority;

    public InputEvent(Object source) {
        this(source, EventPriority.NORMAL);
    }

    public InputEvent(Object source, EventPriority priority) {
        super(source);
        this.priority = priority;
    }

    /**
     * 获取输入设备类型
     *
     * @return 输入设备类型
     */
    public abstract InputDeviceType getDeviceType();

    /**
     * 输入设备类型枚举
     */
    public enum InputDeviceType {
        /**
         * 鼠标
         */
        MOUSE,

        /**
         * 键盘
         */
        KEYBOARD,

        /**
         * 触摸屏
         */
        TOUCH,

        /**
         * 游戏手柄
         */
        GAMEPAD
    }
}
