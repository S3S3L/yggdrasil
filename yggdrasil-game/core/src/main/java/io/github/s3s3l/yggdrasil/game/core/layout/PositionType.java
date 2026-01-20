package io.github.s3s3l.yggdrasil.game.core.layout;

/**
 * 定位类型枚举
 * 对应CSS的position属性，控制元素的定位方式
 */
public enum PositionType {
    /** 静态定位（默认），按正常文档流排列 */
    STATIC,
    /** 相对定位，相对于元素原始位置偏移 */
    RELATIVE,
    /** 绝对定位，相对于最近的已定位祖先元素 */
    ABSOLUTE,
    /** 固定定位，相对于视口固定位置 */
    FIXED,
    /** 粘性定位，在滚动时在relative和fixed之间切换 */
    STICKY
}
