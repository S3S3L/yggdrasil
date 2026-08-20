package io.github.s3s3l.yggdrasil.game.core.layout;

/**
 * 主轴对齐枚举
 * 对应CSS的justify-content属性，控制主轴方向的对齐方式
 */
public enum JustifyContent {
    /** 起始端对齐 */
    FLEX_START,
    /** 末尾端对齐 */
    FLEX_END,
    /** 居中对齐 */
    CENTER,
    /** 两端对齐，项目之间间隔相等 */
    SPACE_BETWEEN,
    /** 每个项目两侧间隔相等 */
    SPACE_AROUND,
    /** 所有间隔完全相等 */
    SPACE_EVENLY
}
