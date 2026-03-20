package io.github.s3s3l.yggdrasil.game.core.ui;

import java.util.ArrayList;
import java.util.List;

/**
 * UI 表格组件
 * 用于显示行列结构的数据表格
 */
public class UITable extends UIComponent {

    private List<UITableColumn> columns;              // 列列表
    private List<List<UITableCell>> rows;             // 行数据
    private int selectedRow = -1;                     // 选中的行
    private int selectedColumn = -1;                  // 选中的列
    private boolean showHeader;                       // 是否显示表头
    private boolean showGridLines;                    // 是否显示网格线
    private float rowHeight;                          // 行高
    private float headerHeight;                       // 表头高度
    private UIColor headerColor;                      // 表头背景颜色
    private UIColor rowColor;                         // 行背景颜色
    private UIColor selectedRowColor;                 // 选中行颜色
    private UIColor gridColor;                        // 网格线颜色
    private UIColor textColor;                        // 文本颜色
    private TableListener listener;                   // 监听器

    /**
     * 表格监听器接口
     */
    public interface TableListener {
        /**
         * 当行被选中时调用
         *
         * @param table 表格
         * @param rowIndex 行索引
         */
        void onRowSelected(UITable table, int rowIndex);

        /**
         * 当单元格被点击时调用
         *
         * @param table 表格
         * @param rowIndex 行索引
         * @param columnIndex 列索引
         */
        void onCellClicked(UITable table, int rowIndex, int columnIndex);

        /**
         * 当表头被点击时调用
         *
         * @param table 表格
         * @param columnIndex 列索引
         */
        void onHeaderClicked(UITable table, int columnIndex);
    }

    public UITable() {
        this.columns = new ArrayList<>();
        this.rows = new ArrayList<>();
        this.showHeader = true;
        this.showGridLines = true;
        this.rowHeight = 30;
        this.headerHeight = 30;
        this.headerColor = new UIColor(0.2f, 0.2f, 0.2f, 1f);
        this.rowColor = new UIColor(0.1f, 0.1f, 0.1f, 0.5f);
        this.selectedRowColor = new UIColor(0.2f, 0.4f, 0.6f, 0.8f);
        this.gridColor = new UIColor(0.3f, 0.3f, 0.3f, 1f);
        this.textColor = UIColor.WHITE;
    }

    public List<UITableColumn> getColumns() {
        return columns;
    }

    public void setColumns(List<UITableColumn> columns) {
        this.columns = columns != null ? new ArrayList<>(columns) : new ArrayList<>();
    }

    public List<List<UITableCell>> getRows() {
        return rows;
    }

    public void setRows(List<List<UITableCell>> rows) {
        this.rows = rows != null ? new ArrayList<>(rows) : new ArrayList<>();
    }

    public int getSelectedRow() {
        return selectedRow;
    }

    public void setSelectedRow(int selectedRow) {
        this.selectedRow = selectedRow;
        if (selectedRow >= 0 && listener != null) {
            listener.onRowSelected(this, selectedRow);
        }
    }

    public int getSelectedColumn() {
        return selectedColumn;
    }

    public void setSelectedColumn(int selectedColumn) {
        this.selectedColumn = selectedColumn;
    }

    public boolean isShowHeader() {
        return showHeader;
    }

    public void setShowHeader(boolean showHeader) {
        this.showHeader = showHeader;
    }

    public boolean isShowGridLines() {
        return showGridLines;
    }

    public void setShowGridLines(boolean showGridLines) {
        this.showGridLines = showGridLines;
    }

    public float getRowHeight() {
        return rowHeight;
    }

    public void setRowHeight(float rowHeight) {
        this.rowHeight = rowHeight;
    }

    public float getHeaderHeight() {
        return headerHeight;
    }

    public void setHeaderHeight(float headerHeight) {
        this.headerHeight = headerHeight;
    }

    public UIColor getHeaderColor() {
        return headerColor;
    }

    public void setHeaderColor(UIColor headerColor) {
        this.headerColor = headerColor;
    }

    public UIColor getRowColor() {
        return rowColor;
    }

    public void setRowColor(UIColor rowColor) {
        this.rowColor = rowColor;
    }

    public UIColor getSelectedRowColor() {
        return selectedRowColor;
    }

    public void setSelectedRowColor(UIColor selectedRowColor) {
        this.selectedRowColor = selectedRowColor;
    }

    public UIColor getGridColor() {
        return gridColor;
    }

    public void setGridColor(UIColor gridColor) {
        this.gridColor = gridColor;
    }

    public UIColor getTextColor() {
        return textColor;
    }

    public void setTextColor(UIColor textColor) {
        this.textColor = textColor;
    }

    public TableListener getListener() {
        return listener;
    }

    public void setListener(TableListener listener) {
        this.listener = listener;
    }

    /**
     * 添加列
     *
     * @param column 列
     */
    public void addColumn(UITableColumn column) {
        if (column != null) {
            columns.add(column);
        }
    }

    /**
     * 在指定位置添加列
     *
     * @param index 位置索引
     * @param column 列
     */
    public void addColumn(int index, UITableColumn column) {
        if (column != null) {
            index = Math.max(0, Math.min(index, columns.size()));
            columns.add(index, column);
        }
    }

    /**
     * 移除列
     *
     * @param column 列
     */
    public void removeColumn(UITableColumn column) {
        columns.remove(column);
    }

    /**
     * 移除指定位置的列
     *
     * @param index 位置索引
     */
    public void removeColumnAt(int index) {
        if (index >= 0 && index < columns.size()) {
            columns.remove(index);
        }
    }

    /**
     * 获取列数量
     *
     * @return 列数量
     */
    public int getColumnCount() {
        return columns.size();
    }

    /**
     * 获取指定位置的列
     *
     * @param index 位置索引
     * @return 列
     */
    public UITableColumn getColumnAt(int index) {
        if (index >= 0 && index < columns.size()) {
            return columns.get(index);
        }
        return null;
    }

    /**
     * 查找指定名称的列
     *
     * @param name 名称
     * @return 列索引，未找到返回 -1
     */
    public int findColumnByName(String name) {
        for (int i = 0; i < columns.size(); i++) {
            if (name != null && name.equals(columns.get(i).getName())) {
                return i;
            }
        }
        return -1;
    }

    /**
     * 添加行
     *
     * @param cells 单元格列表
     */
    public void addRow(List<UITableCell> cells) {
        if (cells != null) {
            rows.add(new ArrayList<>(cells));
        }
    }

    /**
     * 添加行（使用数据数组）
     *
     * @param data 数据数组
     */
    public void addRow(Object[] data) {
        List<UITableCell> cells = new ArrayList<>();
        for (Object value : data) {
            cells.add(new UITableCell(value));
        }
        rows.add(cells);
    }

    /**
     * 在指定位置添加行
     *
     * @param index 位置索引
     * @param cells 单元格列表
     */
    public void addRow(int index, List<UITableCell> cells) {
        if (cells != null) {
            index = Math.max(0, Math.min(index, rows.size()));
            rows.add(index, new ArrayList<>(cells));
        }
    }

    /**
     * 移除行
     *
     * @param cells 行
     */
    public void removeRow(List<UITableCell> cells) {
        rows.remove(cells);
    }

    /**
     * 移除指定位置的行
     *
     * @param index 位置索引
     */
    public void removeRowAt(int index) {
        if (index >= 0 && index < rows.size()) {
            rows.remove(index);
        }
    }

    /**
     * 清除所有行
     */
    public void clearRows() {
        rows.clear();
        selectedRow = -1;
    }

    /**
     * 获取行数
     *
     * @return 行数
     */
    public int getRowCount() {
        return rows.size();
    }

    /**
     * 获取指定位置的行
     *
     * @param rowIndex 行索引
     * @return 单元格列表
     */
    public List<UITableCell> getRowAt(int rowIndex) {
        if (rowIndex >= 0 && rowIndex < rows.size()) {
            return rows.get(rowIndex);
        }
        return null;
    }

    /**
     * 获取指定单元格
     *
     * @param rowIndex 行索引
     * @param columnIndex 列索引
     * @return 单元格
     */
    public UITableCell getCellAt(int rowIndex, int columnIndex) {
        if (rowIndex >= 0 && rowIndex < rows.size() && columnIndex >= 0 && columnIndex < columns.size()) {
            List<UITableCell> row = rows.get(rowIndex);
            if (columnIndex < row.size()) {
                return row.get(columnIndex);
            }
        }
        return null;
    }

    /**
     * 设置单元格
     *
     * @param rowIndex 行索引
     * @param columnIndex 列索引
     * @param cell 单元格
     */
    public void setCellAt(int rowIndex, int columnIndex, UITableCell cell) {
        if (rowIndex >= 0 && rowIndex < rows.size() && columnIndex >= 0 && columnIndex < columns.size()) {
            List<UITableCell> row = rows.get(rowIndex);
            if (columnIndex < row.size()) {
                row.set(columnIndex, cell);
            }
        }
    }

    /**
     * 获取列的宽度
     *
     * @param columnIndex 列索引
     * @return 列宽
     */
    public float getColumnWidth(int columnIndex) {
        if (columnIndex >= 0 && columnIndex < columns.size()) {
            UITableColumn column = columns.get(columnIndex);
            if (column.getWidth() > 0) {
                return column.getWidth();
            }
            // 如果没有指定宽度，根据内容计算
            float maxWidth = column.getName().length() * 10;
            for (List<UITableCell> row : rows) {
                if (columnIndex < row.size()) {
                    Object value = row.get(columnIndex).getValue();
                    if (value != null) {
                        float textWidth = value.toString().length() * 10;
                        maxWidth = Math.max(maxWidth, textWidth);
                    }
                }
            }
            return maxWidth + 20; // 加上边距
        }
        return 100;
    }

    /**
     * 获取列的 X 坐标
     *
     * @param columnIndex 列索引
     * @return X 坐标
     */
    public float getColumnX(int columnIndex) {
        float x = getX();
        for (int i = 0; i < columnIndex && i < columns.size(); i++) {
            x += getColumnWidth(i);
        }
        return x;
    }

    /**
     * 获取行的 Y 坐标
     *
     * @param rowIndex 行索引
     * @return Y 坐标
     */
    public float getRowY(int rowIndex) {
        float y = getY() + getHeight() - headerHeight;
        if (showHeader) {
            y -= rowIndex * rowHeight;
        } else {
            y -= (rowIndex + 1) * rowHeight;
        }
        return y;
    }

    /**
     * 获取点击位置的行索引
     *
     * @param y Y 坐标
     * @return 行索引，表头返回 -1，外部返回 -2
     */
    public int getRowIndexAtY(float y) {
        float contentStartY = getY() + getHeight() - (showHeader ? headerHeight : 0);
        float contentHeight = showHeader ? getHeight() - headerHeight : getHeight();

        if (y > contentStartY) {
            return -1; // 表头区域
        }

        int rowIndex = (int) ((contentStartY - y) / rowHeight);
        if (rowIndex >= 0 && rowIndex < rows.size()) {
            return rowIndex;
        }
        return -2; // 外部区域
    }

    /**
     * 获取点击位置的列索引
     *
     * @param x X 坐标
     * @return 列索引，外部返回 -1
     */
    public int getColumnIndexAtX(float x) {
        float currentX = getX();
        for (int i = 0; i < columns.size(); i++) {
            float columnWidth = getColumnWidth(i);
            if (x >= currentX && x < currentX + columnWidth) {
                return i;
            }
            currentX += columnWidth;
        }
        return -1;
    }

    /**
     * 处理点击事件
     *
     * @param x X 坐标
     * @param y Y 坐标
     */
    public void handleClick(float x, float y) {
        int columnIndex = getColumnIndexAtX(x);
        int rowIndex = getRowIndexAtY(y);

        if (columnIndex >= 0 && rowIndex >= 0) {
            // 点击单元格
            selectedRow = rowIndex;
            selectedColumn = columnIndex;
            if (listener != null) {
                listener.onCellClicked(this, rowIndex, columnIndex);
            }
        } else if (columnIndex >= 0 && rowIndex == -1) {
            // 点击表头
            if (listener != null) {
                listener.onHeaderClicked(this, columnIndex);
            }
        }
    }

    @Override
    public void render() {
        if (!isVisible()) {
            return;
        }

        // 渲染背景
        renderBackground();

        // 渲染表头
        if (showHeader) {
            renderHeader();
        }

        // 渲染行
        renderRows();

        // 渲染网格线
        if (showGridLines) {
            renderGridLines();
        }
    }

    /**
     * 渲染背景
     */
    protected void renderBackground() {
        // 实际渲染由游戏引擎实现
    }

    /**
     * 渲染表头
     */
    protected void renderHeader() {
        // 实际渲染由游戏引擎实现
    }

    /**
     * 渲染所有行
     */
    protected void renderRows() {
        // 实际渲染由游戏引擎实现
    }

    /**
     * 渲染网格线
     */
    protected void renderGridLines() {
        // 实际渲染由游戏引擎实现
    }

    /**
     * 获取推荐的宽度
     *
     * @return 推荐宽度
     */
    public float getPreferredWidth() {
        float totalWidth = 0;
        for (int i = 0; i < columns.size(); i++) {
            totalWidth += getColumnWidth(i);
        }
        return totalWidth;
    }

    /**
     * 获取推荐的高度
     *
     * @return 推荐高度
     */
    public float getPreferredHeight() {
        float totalHeight = rows.size() * rowHeight;
        if (showHeader) {
            totalHeight += headerHeight;
        }
        return totalHeight;
    }

    /**
     * 获取表格的总高度
     *
     * @return 总高度
     */
    public float getTotalHeight() {
        return getPreferredHeight();
    }
}
