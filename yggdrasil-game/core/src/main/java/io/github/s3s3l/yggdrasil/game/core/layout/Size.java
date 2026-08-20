package io.github.s3s3l.yggdrasil.game.core.layout;

/**
 * 尺寸类
 * 表示元素的宽度和高度，包括最小值和最大值约束
 */
public class Size {
    /** 宽度 */
    private LengthUnit width;
    /** 高度 */
    private LengthUnit height;
    /** 最小宽度 */
    private LengthUnit minWidth;
    /** 最小高度 */
    private LengthUnit minHeight;
    /** 最大宽度 */
    private LengthUnit maxWidth;
    /** 最大高度 */
    private LengthUnit maxHeight;

    /**
     * 构造函数，初始化尺寸为auto，最小值为0
     */
    public Size() {
        this.width = LengthUnit.auto();
        this.height = LengthUnit.auto();
        this.minWidth = LengthUnit.px(0);
        this.minHeight = LengthUnit.px(0);
        this.maxWidth = LengthUnit.auto();
        this.maxHeight = LengthUnit.auto();
    }

    /**
     * 获取宽度
     * @return 宽度
     */
    public LengthUnit getWidth() {
        return width;
    }

    /**
     * 设置宽度
     * @param width 宽度
     */
    public void setWidth(LengthUnit width) {
        this.width = width;
    }

    /**
     * 获取高度
     * @return 高度
     */
    public LengthUnit getHeight() {
        return height;
    }

    /**
     * 设置高度
     * @param height 高度
     */
    public void setHeight(LengthUnit height) {
        this.height = height;
    }

    /**
     * 获取最小宽度
     * @return 最小宽度
     */
    public LengthUnit getMinWidth() {
        return minWidth;
    }

    /**
     * 设置最小宽度
     * @param minWidth 最小宽度
     */
    public void setMinWidth(LengthUnit minWidth) {
        this.minWidth = minWidth;
    }

    /**
     * 获取最小高度
     * @return 最小高度
     */
    public LengthUnit getMinHeight() {
        return minHeight;
    }

    /**
     * 设置最小高度
     * @param minHeight 最小高度
     */
    public void setMinHeight(LengthUnit minHeight) {
        this.minHeight = minHeight;
    }

    /**
     * 获取最大宽度
     * @return 最大宽度
     */
    public LengthUnit getMaxWidth() {
        return maxWidth;
    }

    /**
     * 设置最大宽度
     * @param maxWidth 最大宽度
     */
    public void setMaxWidth(LengthUnit maxWidth) {
        this.maxWidth = maxWidth;
    }

    /**
     * 获取最大高度
     * @return 最大高度
     */
    public LengthUnit getMaxHeight() {
        return maxHeight;
    }

    /**
     * 设置最大高度
     * @param maxHeight 最大高度
     */
    public void setMaxHeight(LengthUnit maxHeight) {
        this.maxHeight = maxHeight;
    }
}
