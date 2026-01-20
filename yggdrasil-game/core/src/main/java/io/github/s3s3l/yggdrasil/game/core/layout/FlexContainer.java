package io.github.s3s3l.yggdrasil.game.core.layout;

/**
 * 弹性盒子容器属性类
 * 包含所有Flexbox容器相关的布局属性
 */
public class FlexContainer {
    /** 主轴方向 */
    private FlexDirection flexDirection = FlexDirection.ROW;
    /** 换行方式 */
    private FlexWrap flexWrap = FlexWrap.NOWRAP;
    /** 主轴对齐方式 */
    private JustifyContent justifyContent = JustifyContent.FLEX_START;
    /** 交叉轴对齐方式 */
    private AlignItems alignItems = AlignItems.STRETCH;
    /** 多行对齐方式 */
    private AlignContent alignContent = AlignContent.STRETCH;
    /** 项目间隔（同时设置行和列间隔） */
    private LengthUnit gap;
    /** 行间隔 */
    private LengthUnit rowGap;
    /** 列间隔 */
    private LengthUnit columnGap;

    /**
     * 构造函数，初始化默认间隔为0
     */
    public FlexContainer() {
        this.gap = LengthUnit.px(0);
        this.rowGap = LengthUnit.px(0);
        this.columnGap = LengthUnit.px(0);
    }

    /**
     * 获取主轴方向
     * @return 主轴方向
     */
    public FlexDirection getFlexDirection() {
        return flexDirection;
    }

    /**
     * 设置主轴方向
     * @param flexDirection 主轴方向
     */
    public void setFlexDirection(FlexDirection flexDirection) {
        this.flexDirection = flexDirection;
    }

    /**
     * 获取换行方式
     * @return 换行方式
     */
    public FlexWrap getFlexWrap() {
        return flexWrap;
    }

    /**
     * 设置换行方式
     * @param flexWrap 换行方式
     */
    public void setFlexWrap(FlexWrap flexWrap) {
        this.flexWrap = flexWrap;
    }

    /**
     * 获取主轴对齐方式
     * @return 主轴对齐方式
     */
    public JustifyContent getJustifyContent() {
        return justifyContent;
    }

    /**
     * 设置主轴对齐方式
     * @param justifyContent 主轴对齐方式
     */
    public void setJustifyContent(JustifyContent justifyContent) {
        this.justifyContent = justifyContent;
    }

    /**
     * 获取交叉轴对齐方式
     * @return 交叉轴对齐方式
     */
    public AlignItems getAlignItems() {
        return alignItems;
    }

    /**
     * 设置交叉轴对齐方式
     * @param alignItems 交叉轴对齐方式
     */
    public void setAlignItems(AlignItems alignItems) {
        this.alignItems = alignItems;
    }

    /**
     * 获取多行对齐方式
     * @return 多行对齐方式
     */
    public AlignContent getAlignContent() {
        return alignContent;
    }

    /**
     * 设置多行对齐方式
     * @param alignContent 多行对齐方式
     */
    public void setAlignContent(AlignContent alignContent) {
        this.alignContent = alignContent;
    }

    /**
     * 获取项目间隔
     * @return 项目间隔
     */
    public LengthUnit getGap() {
        return gap;
    }

    /**
     * 设置项目间隔（同时设置行和列间隔）
     * @param gap 项目间隔
     */
    public void setGap(LengthUnit gap) {
        this.gap = gap;
        this.rowGap = gap;
        this.columnGap = gap;
    }

    /**
     * 获取行间隔
     * @return 行间隔
     */
    public LengthUnit getRowGap() {
        return rowGap;
    }

    /**
     * 设置行间隔
     * @param rowGap 行间隔
     */
    public void setRowGap(LengthUnit rowGap) {
        this.rowGap = rowGap;
    }

    /**
     * 获取列间隔
     * @return 列间隔
     */
    public LengthUnit getColumnGap() {
        return columnGap;
    }

    /**
     * 设置列间隔
     * @param columnGap 列间隔
     */
    public void setColumnGap(LengthUnit columnGap) {
        this.columnGap = columnGap;
    }
}
