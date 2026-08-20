package io.github.s3s3l.yggdrasil.game.core.layout;

/**
 * 显示类型枚举
 * 对应CSS的display属性，控制元素如何显示及其布局行为
 */
public enum DisplayType {
    /** 块级元素，独占一行 */
    BLOCK,
    /** 行内元素，不换行 */
    INLINE,
    /** 行内块元素，不换行但可设置宽高 */
    INLINE_BLOCK,
    /** 弹性盒子容器（块级） */
    FLEX,
    /** 弹性盒子容器（行内） */
    INLINE_FLEX,
    /** 网格容器（块级） */
    GRID,
    /** 网格容器（行内） */
    INLINE_GRID,
    /** 不显示元素 */
    NONE,
    /** 元素本身不生成盒子，但其子元素正常渲染 */
    CONTENTS
}
