package io.github.s3s3l.yggdrasil.game.core.layout;

/**
 * 对象适配枚举
 * 对应CSS的object-fit属性，控制替换元素（如图片）如何适配容器
 */
public enum ObjectFit {
    /** 填充，拉伸以填满容器 */
    FILL,
    /** 包含，保持比例并完整显示 */
    CONTAIN,
    /** 覆盖，保持比例并填满容器 */
    COVER,
    /** 不缩放 */
    NONE,
    /** 缩小，在NONE和CONTAIN中选择较小的 */
    SCALE_DOWN
}
