package io.github.s3s3l.yggdrasil.game.core.layout;

/**
 * 弹性盒子项目属性类
 * 包含Flexbox项目的所有属性
 */
public class FlexItem {
    /** 放大比例，默认为0（不放大） */
    private float flexGrow = 0;
    /** 缩小比例，默认为1（空间不足时缩小） */
    private float flexShrink = 1;
    /** 项目基础大小，默认为auto */
    private LengthUnit flexBasis = LengthUnit.auto();
    /** 项目自身对齐方式 */
    private AlignSelf alignSelf = AlignSelf.AUTO;
    /** 项目排列顺序，数值越小越靠前 */
    private int order = 0;

    /**
     * 获取放大比例
     * @return 放大比例
     */
    public float getFlexGrow() {
        return flexGrow;
    }

    /**
     * 设置放大比例
     * @param flexGrow 放大比例
     */
    public void setFlexGrow(float flexGrow) {
        this.flexGrow = flexGrow;
    }

    /**
     * 获取缩小比例
     * @return 缩小比例
     */
    public float getFlexShrink() {
        return flexShrink;
    }

    /**
     * 设置缩小比例
     * @param flexShrink 缩小比例
     */
    public void setFlexShrink(float flexShrink) {
        this.flexShrink = flexShrink;
    }

    /**
     * 获取项目基础大小
     * @return 项目基础大小
     */
    public LengthUnit getFlexBasis() {
        return flexBasis;
    }

    /**
     * 设置项目基础大小
     * @param flexBasis 项目基础大小
     */
    public void setFlexBasis(LengthUnit flexBasis) {
        this.flexBasis = flexBasis;
    }

    /**
     * 获取项目自身对齐方式
     * @return 项目自身对齐方式
     */
    public AlignSelf getAlignSelf() {
        return alignSelf;
    }

    /**
     * 设置项目自身对齐方式
     * @param alignSelf 项目自身对齐方式
     */
    public void setAlignSelf(AlignSelf alignSelf) {
        this.alignSelf = alignSelf;
    }

    /**
     * 获取项目排列顺序
     * @return 项目排列顺序
     */
    public int getOrder() {
        return order;
    }

    /**
     * 设置项目排列顺序
     * @param order 项目排列顺序
     */
    public void setOrder(int order) {
        this.order = order;
    }

    /**
     * 简写方式设置flex-grow、flex-shrink和flex-basis
     * @param grow 放大比例
     * @param shrink 缩小比例
     * @param basis 项目基础大小
     */
    public void setFlex(float grow, float shrink, LengthUnit basis) {
        this.flexGrow = grow;
        this.flexShrink = shrink;
        this.flexBasis = basis;
    }
}
