package io.github.s3s3l.yggdrasil.game.core.layout;

/**
 * 盒模型类型枚举
 * 对应CSS的box-sizing属性，控制盒模型的计算方式
 */
public enum BoxSizingType {
    /** 内容盒模型，width/height只包含内容区域 */
    CONTENT_BOX,
    /** 边框盒模型，width/height包含padding和border */
    BORDER_BOX
}
