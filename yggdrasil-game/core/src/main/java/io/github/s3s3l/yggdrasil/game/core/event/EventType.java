package io.github.s3s3l.yggdrasil.game.core.event;

public enum EventType {
    // Basic Events
    /**
     * 按键按下
     */
    KEY_DOWN,
    /**
     * 按键抬起
     */
    KEY_UP,
    /**
     * 鼠标、摇杆或者触摸点移动
     */
    MOVE,

    // Extended Events
    /**
     * 按键事件
     */
    CLICK,
    /**
     * 拖拽事件
     */
    DRAG,
    /**
     * 释放事件
     */
    DROP,
    HOVER,
    SCROLL,
    HOLD;

}
