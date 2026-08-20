package io.github.s3s3l.yggdrasil.game.core.layout;

/**
 * 清除浮动类型枚举
 * 对应CSS的clear属性，控制元素是否清除浮动
 */
public enum ClearType {
    /** 清除左侧浮动 */
    LEFT,
    /** 清除右侧浮动 */
    RIGHT,
    /** 清除两侧浮动 */
    BOTH,
    /** 不清除浮动 */
    NONE
}
