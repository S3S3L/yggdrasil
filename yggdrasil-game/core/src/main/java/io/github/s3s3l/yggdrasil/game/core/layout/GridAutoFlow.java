package io.github.s3s3l.yggdrasil.game.core.layout;

/**
 * 网格自动流枚举
 * 对应CSS的grid-auto-flow属性，控制网格自动放置算法的工作方式
 */
public enum GridAutoFlow {
    /** 按行填充，先填满一行再到下一行 */
    ROW,
    /** 按列填充，先填满一列再到下一列 */
    COLUMN,
    /** 按行填充，并尝试填充空白区域 */
    ROW_DENSE,
    /** 按列填充，并尝试填充空白区域 */
    COLUMN_DENSE
}
