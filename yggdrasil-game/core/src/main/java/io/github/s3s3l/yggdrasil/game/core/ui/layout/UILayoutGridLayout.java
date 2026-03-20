package io.github.s3s3l.yggdrasil.game.core.ui.layout;

import io.github.s3s3l.yggdrasil.game.core.ui.UIComponent;

/**
 * 网格布局
 * 按行列排列子组件，支持自动换行
 */
public class UILayoutGridLayout extends UILayoutBase {

    private int columns;
    private int rows;
    private float columnWidth;
    private float rowHeight;
    private float horizontalSpacing;
    private float verticalSpacing;
    private boolean autoFit;
    private Align align;

    public enum Align {
        TOP_LEFT,
        TOP_CENTER,
        TOP_RIGHT,
        MIDDLE_LEFT,
        MIDDLE_CENTER,
        MIDDLE_RIGHT,
        BOTTOM_LEFT,
        BOTTOM_CENTER,
        BOTTOM_RIGHT
    }

    public UILayoutGridLayout() {
        this(3, 0, 10, 10);
    }

    public UILayoutGridLayout(int columns, int rows) {
        this(columns, rows, 0, 0);
    }

    public UILayoutGridLayout(int columns, int rows, float horizontalSpacing, float verticalSpacing) {
        this.columns = Math.max(1, columns);
        this.rows = Math.max(0, rows);
        this.columnWidth = 0;
        this.rowHeight = 0;
        this.horizontalSpacing = horizontalSpacing;
        this.verticalSpacing = verticalSpacing;
        this.autoFit = false;
        this.align = Align.TOP_LEFT;
    }

    public int getColumns() {
        return columns;
    }

    public void setColumns(int columns) {
        this.columns = Math.max(1, columns);
        markDirty();
    }

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        this.rows = Math.max(0, rows);
        markDirty();
    }

    public float getColumnWidth() {
        return columnWidth;
    }

    public void setColumnWidth(float columnWidth) {
        this.columnWidth = columnWidth;
        markDirty();
    }

    public float getRowHeight() {
        return rowHeight;
    }

    public void setRowHeight(float rowHeight) {
        this.rowHeight = rowHeight;
        markDirty();
    }

    public float getHorizontalSpacing() {
        return horizontalSpacing;
    }

    public void setHorizontalSpacing(float horizontalSpacing) {
        this.horizontalSpacing = horizontalSpacing;
        markDirty();
    }

    public float getVerticalSpacing() {
        return verticalSpacing;
    }

    public void setVerticalSpacing(float verticalSpacing) {
        this.verticalSpacing = verticalSpacing;
        markDirty();
    }

    public boolean isAutoFit() {
        return autoFit;
    }

    public void setAutoFit(boolean autoFit) {
        this.autoFit = autoFit;
        markDirty();
    }

    public Align getAlign() {
        return align;
    }

    public void setAlign(Align align) {
        this.align = align;
        markDirty();
    }

    @Override
    protected void onLayout(float width, float height) {
        if (children.isEmpty()) {
            return;
        }

        float contentWidth = width - paddingLeft - paddingRight;
        float contentHeight = height - paddingTop - paddingBottom;

        int actualColumns = columns;
        int actualRows = rows;

        // 自动计算行列数
        if (autoFit) {
            if (columnWidth > 0) {
                actualColumns = Math.max(1, (int) ((contentWidth + horizontalSpacing) / (columnWidth + horizontalSpacing)));
            }
            if (rowHeight > 0 && actualRows == 0) {
                actualRows = (int) Math.ceil((float) children.size() / actualColumns);
            }
        }

        // 计算实际行数
        if (actualRows == 0) {
            actualRows = (int) Math.ceil((float) children.size() / actualColumns);
        }

        // 计算单元格大小
        float cellWidth = columnWidth;
        float cellHeight = rowHeight;

        if (cellWidth <= 0) {
            cellWidth = (contentWidth - (actualColumns - 1) * horizontalSpacing) / actualColumns;
        }
        if (cellHeight <= 0) {
            cellHeight = (contentHeight - (actualRows - 1) * verticalSpacing) / actualRows;
        }

        // 计算起始偏移（根据对齐方式）
        float totalGridWidth = actualColumns * cellWidth + (actualColumns - 1) * horizontalSpacing;
        float totalGridHeight = actualRows * cellHeight + (actualRows - 1) * verticalSpacing;

        float startX = paddingLeft;
        float startY = paddingTop;

        switch (align) {
            case TOP_CENTER:
            case MIDDLE_CENTER:
            case BOTTOM_CENTER:
                startX += (contentWidth - totalGridWidth) / 2;
                break;
            case TOP_RIGHT:
            case MIDDLE_RIGHT:
            case BOTTOM_RIGHT:
                startX += contentWidth - totalGridWidth;
                break;
        }

        switch (align) {
            case MIDDLE_LEFT:
            case MIDDLE_CENTER:
            case MIDDLE_RIGHT:
                startY += (contentHeight - totalGridHeight) / 2;
                break;
            case BOTTOM_LEFT:
            case BOTTOM_CENTER:
            case BOTTOM_RIGHT:
                startY += contentHeight - totalGridHeight;
                break;
        }

        // 布局子组件
        for (int i = 0; i < children.size(); i++) {
            int row = i / actualColumns;
            int col = i % actualColumns;

            UIComponent child = children.get(i);
            LayoutParams lp = getChildLayoutParams(child);

            float x = startX + col * (cellWidth + horizontalSpacing) + lp.getMarginLeft();
            float y = startY + row * (cellHeight + verticalSpacing) + lp.getMarginTop();

            float childWidth = lp.getWidth() == LayoutParams.MATCH_PARENT ?
                cellWidth - lp.getMarginLeft() - lp.getMarginRight() :
                (lp.getWidth() == LayoutParams.WRAP_CONTENT ? child.getWidth() : lp.getWidth());

            float childHeight = lp.getHeight() == LayoutParams.MATCH_PARENT ?
                cellHeight - lp.getMarginTop() - lp.getMarginBottom() :
                (lp.getHeight() == LayoutParams.WRAP_CONTENT ? child.getHeight() : lp.getHeight());

            child.setX(x);
            child.setY(y);
            if (childWidth > 0) child.setWidth(childWidth);
            if (childHeight > 0) child.setHeight(childHeight);
        }
    }

    @Override
    protected float[] onMeasure() {
        if (children.isEmpty()) {
            return new float[]{paddingLeft + paddingRight, paddingTop + paddingBottom};
        }

        int actualColumns = columns > 0 ? columns : children.size();
        int actualRows = rows > 0 ? rows : (int) Math.ceil((float) children.size() / actualColumns);

        float totalWidth = paddingLeft + paddingRight + actualColumns * Math.max(0, columnWidth) +
                (actualColumns - 1) * horizontalSpacing;
        float totalHeight = paddingTop + paddingBottom + actualRows * Math.max(0, rowHeight) +
                (actualRows - 1) * verticalSpacing;

        // 如果没有指定单元格大小，根据子组件计算
        if (columnWidth <= 0 || rowHeight <= 0) {
            for (UIComponent child : children) {
                LayoutParams lp = getChildLayoutParams(child);
                if (columnWidth <= 0) {
                    totalWidth = Math.max(totalWidth, lp.getWidth() + lp.getMarginLeft() + lp.getMarginRight());
                }
                if (rowHeight <= 0) {
                    totalHeight = Math.max(totalHeight, lp.getHeight() + lp.getMarginTop() + lp.getMarginBottom());
                }
            }
        }

        return new float[]{totalWidth, totalHeight};
    }
}
