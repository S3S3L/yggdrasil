package io.github.s3s3l.yggdrasil.game.core.layout;

/**
 * 溢出处理类型枚举
 * 对应CSS的overflow属性，控制内容溢出时的处理方式
 */
public enum OverflowType {
    /** 可见，溢出内容不被裁剪 */
    VISIBLE,
    /** 隐藏，溢出内容被裁剪 */
    HIDDEN,
    /** 滚动，总是显示滚动条 */
    SCROLL,
    /** 自动，需要时显示滚动条 */
    AUTO
}
