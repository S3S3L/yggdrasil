package io.github.s3s3l.yggdrasil.game.core.layout;

/**
 * 多行对齐枚举
 * 对应CSS的align-content属性，控制多行项目在交叉轴上的对齐方式
 */
public enum AlignContent {
    /** 交叉轴起始端对齐 */
    FLEX_START,
    /** 交叉轴末尾端对齐 */
    FLEX_END,
    /** 交叉轴居中对齐 */
    CENTER,
    /** 两端对齐，行与行之间间隔相等 */
    SPACE_BETWEEN,
    /** 每行两侧间隔相等 */
    SPACE_AROUND,
    /** 拉伸以填充容器 */
    STRETCH
}
