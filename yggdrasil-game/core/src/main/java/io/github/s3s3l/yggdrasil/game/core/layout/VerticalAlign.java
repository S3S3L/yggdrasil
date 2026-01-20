package io.github.s3s3l.yggdrasil.game.core.layout;

/**
 * 垂直对齐枚举
 * 对应CSS的vertical-align属性，控制行内元素的垂直对齐方式
 */
public enum VerticalAlign {
    /** 基线对齐 */
    BASELINE,
    /** 顶部对齐 */
    TOP,
    /** 居中对齐 */
    MIDDLE,
    /** 底部对齐 */
    BOTTOM,
    /** 与父元素文本顶部对齐 */
    TEXT_TOP,
    /** 与父元素文本底部对齐 */
    TEXT_BOTTOM,
    /** 下标 */
    SUB,
    /** 上标 */
    SUPER
}
