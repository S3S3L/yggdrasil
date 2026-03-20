package io.github.s3s3l.yggdrasil.game.core.ui;

import java.util.ArrayList;
import java.util.List;

/**
 * UI 网格布局组件
 * 用于显示行列结构的网格布局
 */
public class UIGrid extends UIComponent {

    private int columns;                            // 列数
    private int rows;                               // 行数
    private float cellWidth;                        // 单元格宽度
    private float cellHeight;                       // 单元格高度
    private float spacing;                          // 单元格间距
    private UIColor cellColor;                      // 单元格背景颜色
    private UIColor selectedColor;                  // 选中单元格颜色
    private UIColor hoverColor;                     // 悬停单元格颜色
    private UIColor gridColor;                      // 网格线颜色
    private boolean showGridLines;                  // 是否显示网格线
    private SelectionMode selectionMode;            // 选择模式
    private List<UIGridCell> cells;                 // 单元格列表
    private UIGridCell selectedCell;                // 选中的单元格
    private UIGridCell hoveredCell;                 // 悬停的单元格
    private GridListener listener;                  // 监听器

    /**
     * 选择模式枚举
     */
    public enum SelectionMode {
        NONE,           // 不可选择
        SINGLE,         // 单选
        MULTI_CELL,     // 多选单元格
        MULTI_ROW,      // 多选行
        MULTI_COLUMN    // 多选列
    }

    /**
     * 网格监听器接口
     */
    public interface GridListener {
        /**
         * 当单元格被选中时调用
         *
         * @param grid 网格
         * @param cell 单元格
         */
        void onCellSelected(UIGrid grid, UIGridCell cell);

        /**
         * 当单元格被点击时调用
         *
         * @param grid 网格
         * @param row 行索引
         * @param column 列索引
         */
        void onCellClicked(UIGrid grid, int row, int column);

        /**
         * 当单元格被双击时调用
         *
         * @param grid 网格
         * @param row 行索引
         * @param column 列索引
         */
        void onCellDoubleClicked(UIGrid grid, int row, int column);
    }

    public UIGrid() {
        this(3, 3);
    }

    public UIGrid(int columns, int rows) {
        this.columns = columns;
        this.rows = rows;
        this.cellWidth = 100;
        this.cellHeight = 100;
        this.spacing = 2;
        this.cellColor = new UIColor(0.1f, 0.1f, 0.1f, 0.5f);
        this.selectedColor = new UIColor(0.2f, 0.4f, 0.6f, 0.8f);
        this.hoverColor = new UIColor(0.3f, 0.3f, 0.3f, 0.5f);
        this.gridColor = new UIColor(0.3f, 0.3f, 0.3f, 1f);
        this.showGridLines = true;
        this.selectionMode = SelectionMode.SINGLE;
        this.cells = new ArrayList<>();
        initializeCells();
    }

    public int getColumns() {
        return columns;
    }

    public void setColumns(int columns) {
        this.columns = columns;
        initializeCells();
    }

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        this.rows = rows;
        initializeCells();
    }

    public float getCellWidth() {
        return cellWidth;
    }

    public void setCellWidth(float cellWidth) {
        this.cellWidth = cellWidth;
    }

    public float getCellHeight() {
        return cellHeight;
    }

    public void setCellHeight(float cellHeight) {
        this.cellHeight = cellHeight;
    }

    public float getSpacing() {
        return spacing;
    }

    public void setSpacing(float spacing) {
        this.spacing = spacing;
    }

    public UIColor getCellColor() {
        return cellColor;
    }

    public void setCellColor(UIColor cellColor) {
        this.cellColor = cellColor;
    }

    public UIColor getSelectedColor() {
        return selectedColor;
    }

    public void setSelectedColor(UIColor selectedColor) {
        this.selectedColor = selectedColor;
    }

    public UIColor getHoverColor() {
        return hoverColor;
    }

    public void setHoverColor(UIColor hoverColor) {
        this.hoverColor = hoverColor;
    }

    public UIColor getGridColor() {
        return gridColor;
    }

    public void setGridColor(UIColor gridColor) {
        this.gridColor = gridColor;
    }

    public boolean isShowGridLines() {
        return showGridLines;
    }

    public void setShowGridLines(boolean showGridLines) {
        this.showGridLines = showGridLines;
    }

    public SelectionMode getSelectionMode() {
        return selectionMode;
    }

    public void setSelectionMode(SelectionMode selectionMode) {
        this.selectionMode = selectionMode;
    }

    public List<UIGridCell> getCells() {
        return cells;
    }

    public void setCells(List<UIGridCell> cells) {
        this.cells = cells != null ? new ArrayList<>(cells) : new ArrayList<>();
    }

    public GridListener getListener() {
        return listener;
    }

    public void setListener(GridListener listener) {
        this.listener = listener;
    }

    /**
     * 初始化单元格
     */
    private void initializeCells() {
        cells.clear();
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < columns; col++) {
                UIGridCell cell = new UIGridCell(row, col);
                cells.add(cell);
            }
        }
    }

    /**
     * 获取指定位置的单元格
     *
     * @param row 行索引
     * @param column 列索引
     * @return 单元格
     */
    public UIGridCell getCellAt(int row, int column) {
        if (row >= 0 && row < rows && column >= 0 && column < columns) {
            int index = row * columns + column;
            if (index < cells.size()) {
                return cells.get(index);
            }
        }
        return null;
    }

    /**
     * 设置单元格
     *
     * @param row 行索引
     * @param column 列索引
     * @param cell 单元格
     */
    public void setCellAt(int row, int column, UIGridCell cell) {
        if (row >= 0 && row < rows && column >= 0 && column < columns) {
            int index = row * columns + column;
            if (index < cells.size()) {
                cells.set(index, cell);
                cell.setRow(row);
                cell.setColumn(column);
            }
        }
    }

    /**
     * 获取单元格的内容
     *
     * @param row 行索引
     * @param column 列索引
     * @return 内容
     */
    public Object getCellContent(int row, int column) {
        UIGridCell cell = getCellAt(row, column);
        if (cell != null) {
            return cell.getContent();
        }
        return null;
    }

    /**
     * 设置单元格内容
     *
     * @param row 行索引
     * @param column 列索引
     * @param content 内容
     */
    public void setCellContent(int row, int column, Object content) {
        UIGridCell cell = getCellAt(row, column);
        if (cell != null) {
            cell.setContent(content);
        }
    }

    /**
     * 选中指定单元格
     *
     * @param cell 单元格
     */
    public void selectCell(UIGridCell cell) {
        if (cell == null) {
            selectedCell = null;
            return;
        }

        if (selectionMode == SelectionMode.NONE) {
            return;
        }

        if (selectionMode == SelectionMode.SINGLE) {
            // 取消其他单元格的选择
            for (UIGridCell c : cells) {
                c.setSelected(false);
            }
        }

        cell.setSelected(true);
        selectedCell = cell;

        if (listener != null) {
            listener.onCellSelected(this, cell);
        }
    }

    /**
     * 选中指定位置的单元格
     *
     * @param row 行索引
     * @param column 列索引
     */
    public void selectCell(int row, int column) {
        UIGridCell cell = getCellAt(row, column);
        selectCell(cell);
    }

    /**
     * 取消所有单元格的选择
     */
    public void deselectAll() {
        for (UIGridCell cell : cells) {
            cell.setSelected(false);
        }
        selectedCell = null;
    }

    /**
     * 获取选中的单元格
     *
     * @return 选中的单元格
     */
    public UIGridCell getSelectedCell() {
        return selectedCell;
    }

    /**
     * 获取所有选中的单元格
     *
     * @return 选中的单元格列表
     */
    public List<UIGridCell> getSelectedCells() {
        List<UIGridCell> result = new ArrayList<>();
        for (UIGridCell cell : cells) {
            if (cell.isSelected()) {
                result.add(cell);
            }
        }
        return result;
    }

    /**
     * 获取单元格的边界
     *
     * @param row 行索引
     * @param column 列索引
     * @return 边界数组 [x, y, width, height]
     */
    public float[] getCellBounds(int row, int column) {
        float[] bounds = new float[4];

        float totalSpacing = spacing * (columns + 1);
        float availableWidth = getWidth() - totalSpacing;
        float actualCellWidth = availableWidth / columns;

        float totalSpacingY = spacing * (rows + 1);
        float availableHeight = getHeight() - totalSpacingY;
        float actualCellHeight = availableHeight / rows;

        bounds[0] = getX() + spacing + column * (actualCellWidth + spacing);
        bounds[1] = getY() + getHeight() - spacing - actualCellHeight - row * (actualCellHeight + spacing);
        bounds[2] = actualCellWidth;
        bounds[3] = actualCellHeight;

        return bounds;
    }

    /**
     * 获取点击位置的单元格
     *
     * @param x X 坐标
     * @param y Y 坐标
     * @return 单元格，如果点击在外部则返回 null
     */
    public UIGridCell getCellAtPosition(float x, float y) {
        if (x < getX() || x > getX() + getWidth() || y < getY() || y > getY() + getHeight()) {
            return null;
        }

        float totalSpacing = spacing * (columns + 1);
        float availableWidth = getWidth() - totalSpacing;
        float actualCellWidth = availableWidth / columns;

        float totalSpacingY = spacing * (rows + 1);
        float availableHeight = getHeight() - totalSpacingY;
        float actualCellHeight = availableHeight / rows;

        int column = (int) ((x - getX() - spacing) / (actualCellWidth + spacing));
        int row = (int) ((getY() + getHeight() - spacing - y) / (actualCellHeight + spacing));

        return getCellAt(row, column);
    }

    /**
     * 处理点击事件
     *
     * @param x X 坐标
     * @param y Y 坐标
     */
    public void handleClick(float x, float y) {
        UIGridCell cell = getCellAtPosition(x, y);
        if (cell != null) {
            selectCell(cell);
            if (listener != null) {
                listener.onCellClicked(this, cell.getRow(), cell.getColumn());
            }
        }
    }

    /**
     * 处理双击事件
     *
     * @param x X 坐标
     * @param y Y 坐标
     */
    public void handleDoubleClick(float x, float y) {
        UIGridCell cell = getCellAtPosition(x, y);
        if (cell != null && listener != null) {
            listener.onCellDoubleClicked(this, cell.getRow(), cell.getColumn());
        }
    }

    /**
     * 处理鼠标移动事件
     *
     * @param x X 坐标
     * @param y Y 坐标
     */
    public void handleMouseMove(float x, float y) {
        UIGridCell cell = getCellAtPosition(x, y);
        if (cell != hoveredCell) {
            hoveredCell = cell;
            if (cell != null) {
                cell.setHover(true);
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

        // 渲染所有单元格
        renderCells();

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
     * 渲染所有单元格
     */
    protected void renderCells() {
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
        return columns * cellWidth + (columns + 1) * spacing;
    }

    /**
     * 获取推荐的高度
     *
     * @return 推荐高度
     */
    public float getPreferredHeight() {
        return rows * cellHeight + (rows + 1) * spacing;
    }

    /**
     * 获取单元格数量
     *
     * @return 单元格数量
     */
    public int getCellCount() {
        return cells.size();
    }

    /**
     * 清除所有单元格内容
     */
    public void clearCells() {
        for (UIGridCell cell : cells) {
            cell.setContent(null);
            cell.setSelected(false);
        }
        selectedCell = null;
        hoveredCell = null;
    }
}
