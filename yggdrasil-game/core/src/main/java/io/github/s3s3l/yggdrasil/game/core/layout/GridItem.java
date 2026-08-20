package io.github.s3s3l.yggdrasil.game.core.layout;

/**
 * 网格项目属性类
 * 包含网格项目的位置和对齐属性
 */
public class GridItem {
    /** 列起始线 */
    private GridLine columnStart;
    /** 列结束线 */
    private GridLine columnEnd;
    /** 行起始线 */
    private GridLine rowStart;
    /** 行结束线 */
    private GridLine rowEnd;
    /** 所属网格区域 */
    private GridArea area;
    /** 项目在单元格中的水平对齐方式 */
    private JustifySelf justifySelf = JustifySelf.AUTO;
    /** 项目在单元格中的垂直对齐方式 */
    private AlignSelf alignSelf = AlignSelf.AUTO;

    /**
     * 构造函数，初始化默认网格线位置为0
     */
    public GridItem() {
        this.columnStart = new GridLine(0);
        this.columnEnd = new GridLine(0);
        this.rowStart = new GridLine(0);
        this.rowEnd = new GridLine(0);
    }

    /**
     * 获取列起始线
     * @return 列起始线
     */
    public GridLine getColumnStart() {
        return columnStart;
    }

    /**
     * 设置列起始线
     * @param columnStart 列起始线
     */
    public void setColumnStart(GridLine columnStart) {
        this.columnStart = columnStart;
    }

    /**
     * 获取列结束线
     * @return 列结束线
     */
    public GridLine getColumnEnd() {
        return columnEnd;
    }

    /**
     * 设置列结束线
     * @param columnEnd 列结束线
     */
    public void setColumnEnd(GridLine columnEnd) {
        this.columnEnd = columnEnd;
    }

    /**
     * 获取行起始线
     * @return 行起始线
     */
    public GridLine getRowStart() {
        return rowStart;
    }

    /**
     * 设置行起始线
     * @param rowStart 行起始线
     */
    public void setRowStart(GridLine rowStart) {
        this.rowStart = rowStart;
    }

    /**
     * 获取行结束线
     * @return 行结束线
     */
    public GridLine getRowEnd() {
        return rowEnd;
    }

    /**
     * 设置行结束线
     * @param rowEnd 行结束线
     */
    public void setRowEnd(GridLine rowEnd) {
        this.rowEnd = rowEnd;
    }

    /**
     * 获取所属网格区域
     * @return 所属网格区域
     */
    public GridArea getArea() {
        return area;
    }

    /**
     * 设置所属网格区域
     * @param area 所属网格区域
     */
    public void setArea(GridArea area) {
        this.area = area;
    }

    /**
     * 获取项目在单元格中的水平对齐方式
     * @return 水平对齐方式
     */
    public JustifySelf getJustifySelf() {
        return justifySelf;
    }

    /**
     * 设置项目在单元格中的水平对齐方式
     * @param justifySelf 水平对齐方式
     */
    public void setJustifySelf(JustifySelf justifySelf) {
        this.justifySelf = justifySelf;
    }

    /**
     * 获取项目在单元格中的垂直对齐方式
     * @return 垂直对齐方式
     */
    public AlignSelf getAlignSelf() {
        return alignSelf;
    }

    /**
     * 设置项目在单元格中的垂直对齐方式
     * @param alignSelf 垂直对齐方式
     */
    public void setAlignSelf(AlignSelf alignSelf) {
        this.alignSelf = alignSelf;
    }

    /**
     * 简写方式设置网格列位置
     * @param start 列起始线
     * @param end 列结束线
     */
    public void setGridColumn(GridLine start, GridLine end) {
        this.columnStart = start;
        this.columnEnd = end;
    }

    /**
     * 简写方式设置网格行位置
     * @param start 行起始线
     * @param end 行结束线
     */
    public void setGridRow(GridLine start, GridLine end) {
        this.rowStart = start;
        this.rowEnd = end;
    }
}
