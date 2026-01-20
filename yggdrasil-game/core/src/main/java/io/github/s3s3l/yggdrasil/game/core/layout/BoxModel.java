package io.github.s3s3l.yggdrasil.game.core.layout;

/**
 * 盒模型类
 * 包含CSS盒模型的所有属性：尺寸、外边距、内边距和盒模型类型
 */
public class BoxModel {
    /** 元素尺寸 */
    private Size size = new Size();
    /** 外边距 */
    private Spacing margin = new Spacing();
    /** 内边距 */
    private Spacing padding = new Spacing();
    /** 盒模型类型 */
    private BoxSizingType boxSizing = BoxSizingType.CONTENT_BOX;

    /**
     * 获取元素尺寸
     * @return 元素尺寸
     */
    public Size getSize() {
        return size;
    }

    /**
     * 设置元素尺寸
     * @param size 元素尺寸
     */
    public void setSize(Size size) {
        this.size = size;
    }

    /**
     * 获取外边距
     * @return 外边距
     */
    public Spacing getMargin() {
        return margin;
    }

    /**
     * 设置外边距
     * @param margin 外边距
     */
    public void setMargin(Spacing margin) {
        this.margin = margin;
    }

    /**
     * 获取内边距
     * @return 内边距
     */
    public Spacing getPadding() {
        return padding;
    }

    /**
     * 设置内边距
     * @param padding 内边距
     */
    public void setPadding(Spacing padding) {
        this.padding = padding;
    }

    /**
     * 获取盒模型类型
     * @return 盒模型类型
     */
    public BoxSizingType getBoxSizing() {
        return boxSizing;
    }

    /**
     * 设置盒模型类型
     * @param boxSizing 盒模型类型
     */
    public void setBoxSizing(BoxSizingType boxSizing) {
        this.boxSizing = boxSizing;
    }
}
