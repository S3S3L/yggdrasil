package io.github.s3s3l.yggdrasil.game.core.layout;

/**
 * 弹性盒子换行枚举
 * 对应CSS的flex-wrap属性，控制弹性盒子是否换行
 */
public enum FlexWrap {
    /** 不换行 */
    NOWRAP,
    /** 换行，第一行在上方 */
    WRAP,
    /** 换行，第一行在下方 */
    WRAP_REVERSE
}
