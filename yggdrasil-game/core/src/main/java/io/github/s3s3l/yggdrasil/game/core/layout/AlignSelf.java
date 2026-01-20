package io.github.s3s3l.yggdrasil.game.core.layout;

/**
 * 项目自身对齐枚举
 * 对应CSS的align-self属性，控制单个项目的对齐方式，会覆盖align-items
 */
public enum AlignSelf {
    /** 继承父容器的align-items值 */
    AUTO,
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
