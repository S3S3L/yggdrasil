package io.github.s3s3l.yggdrasil.game.core.layout;

import java.util.ArrayList;
import java.util.List;

/**
 * 网格容器属性类
 * 包含所有Grid布局容器相关的属性
 */
public class GridContainer {
    /** 列轨道模板 */
    private GridTrack templateColumns = new GridTrack();
    /** 行轨道模板 */
    private GridTrack templateRows = new GridTrack();
    /** 区域模板，用于命名网格区域 */
    private List<List<GridArea>> templateAreas = new ArrayList<>();
    /** 自动生成的列宽度 */
    private LengthUnit autoColumns = LengthUnit.auto();
    /** 自动生成的行高度 */
    private LengthUnit autoRows = LengthUnit.auto();
    /** 自动放置算法的流动方向 */
    private GridAutoFlow autoFlow = GridAutoFlow.ROW;
    /** 网格间隔（同时设置行和列间隔） */
    private LengthUnit gap;
    /** 行间隔 */
    private LengthUnit rowGap;
    /** 列间隔 */
    private LengthUnit columnGap;
    /** 网格在容器中的水平对齐方式 */
    private JustifyContent justifyContent = JustifyContent.FLEX_START;
    /** 网格在容器中的垂直对齐方式 */
    private AlignContent alignContent = AlignContent.FLEX_START;
    /** 网格项目在单元格中的水平对齐方式 */
    private JustifyItems justifyItems = JustifyItems.STRETCH;
    /** 网格项目在单元格中的垂直对齐方式 */
    private AlignItems alignItems = AlignItems.STRETCH;

    /**
     * 构造函数，初始化默认间隔为0
     */
    public GridContainer() {
        this.gap = LengthUnit.px(0);
        this.rowGap = LengthUnit.px(0);
        this.columnGap = LengthUnit.px(0);
    }

    /**
     * 获取列轨道模板
     * @return 列轨道模板
     */
    public GridTrack getTemplateColumns() {
        return templateColumns;
    }

    /**
     * 设置列轨道模板
     * @param templateColumns 列轨道模板
     */
    public void setTemplateColumns(GridTrack templateColumns) {
        this.templateColumns = templateColumns;
    }

    /**
     * 获取行轨道模板
     * @return 行轨道模板
     */
    public GridTrack getTemplateRows() {
        return templateRows;
    }

    /**
     * 设置行轨道模板
     * @param templateRows 行轨道模板
     */
    public void setTemplateRows(GridTrack templateRows) {
        this.templateRows = templateRows;
    }

    /**
     * 获取区域模板
     * @return 区域模板
     */
    public List<List<GridArea>> getTemplateAreas() {
        return templateAreas;
    }

    /**
     * 设置区域模板
     * @param templateAreas 区域模板
     */
    public void setTemplateAreas(List<List<GridArea>> templateAreas) {
        this.templateAreas = templateAreas;
    }

    /**
     * 获取自动生成的列宽度
     * @return 自动生成的列宽度
     */
    public LengthUnit getAutoColumns() {
        return autoColumns;
    }

    /**
     * 设置自动生成的列宽度
     * @param autoColumns 自动生成的列宽度
     */
    public void setAutoColumns(LengthUnit autoColumns) {
        this.autoColumns = autoColumns;
    }

    /**
     * 获取自动生成的行高度
     * @return 自动生成的行高度
     */
    public LengthUnit getAutoRows() {
        return autoRows;
    }

    /**
     * 设置自动生成的行高度
     * @param autoRows 自动生成的行高度
     */
    public void setAutoRows(LengthUnit autoRows) {
        this.autoRows = autoRows;
    }

    /**
     * 获取自动放置算法的流动方向
     * @return 自动放置算法的流动方向
     */
    public GridAutoFlow getAutoFlow() {
        return autoFlow;
    }

    /**
     * 设置自动放置算法的流动方向
     * @param autoFlow 自动放置算法的流动方向
     */
    public void setAutoFlow(GridAutoFlow autoFlow) {
        this.autoFlow = autoFlow;
    }

    /**
     * 获取网格间隔
     * @return 网格间隔
     */
    public LengthUnit getGap() {
        return gap;
    }

    /**
     * 设置网格间隔（同时设置行和列间隔）
     * @param gap 网格间隔
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

    /**
     * 获取网格在容器中的水平对齐方式
     * @return 水平对齐方式
     */
    public JustifyContent getJustifyContent() {
        return justifyContent;
    }

    /**
     * 设置网格在容器中的水平对齐方式
     * @param justifyContent 水平对齐方式
     */
    public void setJustifyContent(JustifyContent justifyContent) {
        this.justifyContent = justifyContent;
    }

    /**
     * 获取网格在容器中的垂直对齐方式
     * @return 垂直对齐方式
     */
    public AlignContent getAlignContent() {
        return alignContent;
    }

    /**
     * 设置网格在容器中的垂直对齐方式
     * @param alignContent 垂直对齐方式
     */
    public void setAlignContent(AlignContent alignContent) {
        this.alignContent = alignContent;
    }

    /**
     * 获取网格项目在单元格中的水平对齐方式
     * @return 水平对齐方式
     */
    public JustifyItems getJustifyItems() {
        return justifyItems;
    }

    /**
     * 设置网格项目在单元格中的水平对齐方式
     * @param justifyItems 水平对齐方式
     */
    public void setJustifyItems(JustifyItems justifyItems) {
        this.justifyItems = justifyItems;
    }

    /**
     * 获取网格项目在单元格中的垂直对齐方式
     * @return 垂直对齐方式
     */
    public AlignItems getAlignItems() {
        return alignItems;
    }

    /**
     * 设置网格项目在单元格中的垂直对齐方式
     * @param alignItems 垂直对齐方式
     */
    public void setAlignItems(AlignItems alignItems) {
        this.alignItems = alignItems;
    }
}
