package io.github.s3s3l.yggdrasil.game.core.layout;

/**
 * 网格项目自身主轴对齐枚举
 * 对应CSS的justify-self属性，控制单个网格项目在行内轴上的对齐方式
 */
public enum JustifySelf {
    /** 继承父容器的justify-items值 */
    AUTO,
    /** 起始端对齐 */
    START,
    /** 末尾端对齐 */
    END,
    /** 居中对齐 */
    CENTER,
    /** 拉伸以填充网格区域 */
    STRETCH
}
