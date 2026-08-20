package io.github.s3s3l.yggdrasil.game.core.layout;

/**
 * 交叉轴对齐枚举
 * 对应CSS的align-items属性，控制项目在交叉轴上的对齐方式
 */
public enum AlignItems {
    /** 交叉轴起始端对齐 */
    FLEX_START,
    /** 交叉轴末尾端对齐 */
    FLEX_END,
    /** 交叉轴居中对齐 */
    CENTER,
    /** 基线对齐 */
    BASELINE,
    /** 拉伸以填充容器 */
    STRETCH
}
