package io.github.s3s3l.yggdrasil.game.core.ui.layout;

import io.github.s3s3l.yggdrasil.game.core.ui.UIComponent;

import java.util.ArrayList;
import java.util.List;

/**
 * 流式布局
 * 自动排列子组件，超出宽度后换行
 */
public class UILayoutFlowLayout extends UILayoutBase {

    private FlowDirection flowDirection;
    private HorizontalAlign horizontalAlign;
    private VerticalAlign verticalAlign;
    private float horizontalSpacing;
    private float verticalSpacing;
    private boolean autoFit;

    public UILayoutFlowLayout() {
        this(FlowDirection.HORIZONTAL, 5, 5);
    }

    public UILayoutFlowLayout(FlowDirection flowDirection, float horizontalSpacing, float verticalSpacing) {
        this.flowDirection = flowDirection;
        this.horizontalSpacing = horizontalSpacing;
        this.verticalSpacing = verticalSpacing;
        this.horizontalAlign = HorizontalAlign.LEFT;
        this.verticalAlign = VerticalAlign.TOP;
        this.autoFit = false;
    }

    public FlowDirection getFlowDirection() {
        return flowDirection;
    }

    public void setFlowDirection(FlowDirection flowDirection) {
        this.flowDirection = flowDirection;
        markDirty();
    }

    public HorizontalAlign getHorizontalAlign() {
        return horizontalAlign;
    }

    public void setHorizontalAlign(HorizontalAlign horizontalAlign) {
        this.horizontalAlign = horizontalAlign;
        markDirty();
    }

    public VerticalAlign getVerticalAlign() {
        return verticalAlign;
    }

    public void setVerticalAlign(VerticalAlign verticalAlign) {
        this.verticalAlign = verticalAlign;
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

    @Override
    protected void onLayout(float width, float height) {
        if (children.isEmpty()) {
            return;
        }

        float contentWidth = width - paddingLeft - paddingRight;
        float contentHeight = height - paddingTop - paddingBottom;

        if (flowDirection == FlowDirection.HORIZONTAL) {
            layoutHorizontal(contentWidth, contentHeight);
        } else {
            layoutVertical(contentWidth, contentHeight);
        }
    }

    private void layoutHorizontal(float contentWidth, float contentHeight) {
        // 收集所有行
        List<List<FlowItem>> lines = new ArrayList<>();
        List<FlowItem> currentLine = new ArrayList<>();
        float currentLineWidth = 0;

        for (int i = 0; i < children.size(); i++) {
            UIComponent child = children.get(i);
            LayoutParams lp = getChildLayoutParams(child);

            float childWidth = getChildWidth(child, lp, contentWidth);
            float childHeight = getChildHeight(child, lp, contentHeight);

            float itemWidth = childWidth + lp.getMarginLeft() + lp.getMarginRight();
            float itemSpacing = currentLine.isEmpty() ? 0 : horizontalSpacing;

            // 检查是否需要换行
            if (!currentLine.isEmpty() && currentLineWidth + itemWidth + itemSpacing > contentWidth) {
                lines.add(currentLine);
                currentLine = new ArrayList<>();
                currentLineWidth = 0;
            }

            currentLine.add(new FlowItem(child, lp, childWidth, childHeight));
            currentLineWidth += itemWidth + itemSpacing;
        }

        if (!currentLine.isEmpty()) {
            lines.add(currentLine);
        }

        // 计算所有行的总高度
        float totalRowsHeight = 0;
        for (List<FlowItem> line : lines) {
            float lineHeight = 0;
            for (FlowItem item : line) {
                lineHeight = Math.max(lineHeight, item.height + item.lp.getMarginTop() + item.lp.getMarginBottom());
            }
            totalRowsHeight += lineHeight + verticalSpacing;
        }
        if (!lines.isEmpty()) {
            totalRowsHeight -= verticalSpacing;
        }

        // 根据垂直对齐方式计算起始 Y
        float startY = paddingTop;
        float extraVerticalSpace = contentHeight - totalRowsHeight;

        if (verticalAlign == VerticalAlign.CENTER) {
            startY += extraVerticalSpace / 2;
        } else if (verticalAlign == VerticalAlign.BOTTOM || verticalAlign == VerticalAlign.END) {
            startY += extraVerticalSpace;
        }

        // 布局每一行
        float currentY = startY;
        for (List<FlowItem> line : lines) {
            float lineHeight = 0;
            for (FlowItem item : line) {
                lineHeight = Math.max(lineHeight, item.height + item.lp.getMarginTop() + item.lp.getMarginBottom());
            }

            // 计算行内总宽度
            float lineWidth = 0;
            for (FlowItem item : line) {
                lineWidth += item.width + item.lp.getMarginLeft() + item.lp.getMarginRight() + horizontalSpacing;
            }
            if (!line.isEmpty()) {
                lineWidth -= horizontalSpacing;
            }

            // 根据水平对齐方式计算起始 X
            float startX = paddingLeft;
            float extraHorizontalSpace = contentWidth - lineWidth;

            if (horizontalAlign == HorizontalAlign.CENTER) {
                startX += extraHorizontalSpace / 2;
            } else if (horizontalAlign == HorizontalAlign.RIGHT || horizontalAlign == HorizontalAlign.END) {
                startX += extraHorizontalSpace;
            }

            float currentX = startX;
            for (FlowItem item : line) {
                float x = currentX + item.lp.getMarginLeft();
                float y = currentY + item.lp.getMarginTop();

                // 垂直对齐行内项目
                if (verticalAlign == VerticalAlign.CENTER) {
                    y += (lineHeight - item.height - item.lp.getMarginTop() - item.lp.getMarginBottom()) / 2;
                } else if (verticalAlign == VerticalAlign.BOTTOM || verticalAlign == VerticalAlign.END) {
                    y += lineHeight - item.height - item.lp.getMarginBottom() - item.lp.getMarginTop();
                }

                item.child.setX(x);
                item.child.setY(y);
                item.child.setWidth(item.width);
                item.child.setHeight(item.height);

                currentX += item.width + item.lp.getMarginLeft() + item.lp.getMarginRight() + horizontalSpacing;
            }

            currentY += lineHeight + verticalSpacing;
        }
    }

    private void layoutVertical(float contentWidth, float contentHeight) {
        // 垂直流向，按列排列
        List<List<FlowItem>> columns = new ArrayList<>();
        List<FlowItem> currentColumn = new ArrayList<>();
        float currentColumnHeight = 0;

        for (int i = 0; i < children.size(); i++) {
            UIComponent child = children.get(i);
            LayoutParams lp = getChildLayoutParams(child);

            float childWidth = getChildWidth(child, lp, contentWidth);
            float childHeight = getChildHeight(child, lp, contentHeight);

            float itemHeight = childHeight + lp.getMarginTop() + lp.getMarginBottom();
            float itemSpacing = currentColumn.isEmpty() ? 0 : verticalSpacing;

            // 检查是否需要换列
            if (!currentColumn.isEmpty() && currentColumnHeight + itemHeight + itemSpacing > contentHeight) {
                columns.add(currentColumn);
                currentColumn = new ArrayList<>();
                currentColumnHeight = 0;
            }

            currentColumn.add(new FlowItem(child, lp, childWidth, childHeight));
            currentColumnHeight += itemHeight + itemSpacing;
        }

        if (!currentColumn.isEmpty()) {
            columns.add(currentColumn);
        }

        // 计算所有列的总宽度
        float totalColumnsWidth = 0;
        for (List<FlowItem> column : columns) {
            float columnWidth = 0;
            for (FlowItem item : column) {
                columnWidth = Math.max(columnWidth, item.width + item.lp.getMarginLeft() + item.lp.getMarginRight());
            }
            totalColumnsWidth += columnWidth + horizontalSpacing;
        }
        if (!columns.isEmpty()) {
            totalColumnsWidth -= horizontalSpacing;
        }

        // 根据水平对齐方式计算起始 X
        float startX = paddingLeft;
        float extraHorizontalSpace = contentWidth - totalColumnsWidth;

        if (horizontalAlign == HorizontalAlign.CENTER) {
            startX += extraHorizontalSpace / 2;
        } else if (horizontalAlign == HorizontalAlign.RIGHT || horizontalAlign == HorizontalAlign.END) {
            startX += extraHorizontalSpace;
        }

        // 布局每一列
        float currentX = startX;
        for (List<FlowItem> column : columns) {
            float columnWidth = 0;
            for (FlowItem item : column) {
                columnWidth = Math.max(columnWidth, item.width + item.lp.getMarginLeft() + item.lp.getMarginRight());
            }

            // 计算列内总高度
            float columnHeight = 0;
            for (FlowItem item : column) {
                columnHeight += item.height + item.lp.getMarginTop() + item.lp.getMarginBottom() + verticalSpacing;
            }
            if (!column.isEmpty()) {
                columnHeight -= verticalSpacing;
            }

            // 根据垂直对齐方式计算起始 Y
            float startY = paddingTop;
            float extraVerticalSpace = contentHeight - columnHeight;

            if (verticalAlign == VerticalAlign.CENTER) {
                startY += extraVerticalSpace / 2;
            } else if (verticalAlign == VerticalAlign.BOTTOM || verticalAlign == VerticalAlign.END) {
                startY += extraVerticalSpace;
            }

            float currentY = startY;
            for (FlowItem item : column) {
                float x = currentX + item.lp.getMarginLeft();
                float y = currentY + item.lp.getMarginTop();

                // 水平对齐列内项目
                if (horizontalAlign == HorizontalAlign.CENTER) {
                    x += (columnWidth - item.width - item.lp.getMarginLeft() - item.lp.getMarginRight()) / 2;
                } else if (horizontalAlign == HorizontalAlign.RIGHT || horizontalAlign == HorizontalAlign.END) {
                    x += columnWidth - item.width - item.lp.getMarginRight() - item.lp.getMarginLeft();
                }

                item.child.setX(x);
                item.child.setY(y);
                item.child.setWidth(item.width);
                item.child.setHeight(item.height);

                currentY += item.height + item.lp.getMarginTop() + item.lp.getMarginBottom() + verticalSpacing;
            }

            currentX += columnWidth + horizontalSpacing;
        }
    }

    private float getChildWidth(UIComponent child, LayoutParams lp, float availableWidth) {
        if (lp.getWidth() == LayoutParams.MATCH_PARENT) {
            return availableWidth - lp.getMarginLeft() - lp.getMarginRight();
        } else if (lp.getWidth() == LayoutParams.WRAP_CONTENT) {
            return child.getWidth();
        } else if (lp.getWidth() > 0) {
            return lp.getWidth();
        } else {
            return child.getWidth();
        }
    }

    private float getChildHeight(UIComponent child, LayoutParams lp, float availableHeight) {
        if (lp.getHeight() == LayoutParams.MATCH_PARENT) {
            return availableHeight - lp.getMarginTop() - lp.getMarginBottom();
        } else if (lp.getHeight() == LayoutParams.WRAP_CONTENT) {
            return child.getHeight();
        } else if (lp.getHeight() > 0) {
            return lp.getHeight();
        } else {
            return child.getHeight();
        }
    }

    @Override
    protected float[] onMeasure() {
        float totalWidth = paddingLeft + paddingRight;
        float totalHeight = paddingTop + paddingBottom;

        if (flowDirection == FlowDirection.HORIZONTAL) {
            float maxWidth = 0;
            float accumulatedHeight = 0;
            float currentLineWidth = 0;
            float currentLineHeight = 0;

            for (UIComponent child : children) {
                LayoutParams lp = getChildLayoutParams(child);
                float childWidth = lp.getWidth() == LayoutParams.WRAP_CONTENT ?
                    child.getWidth() : (lp.getWidth() > 0 ? lp.getWidth() : 0);
                float childHeight = lp.getHeight() == LayoutParams.WRAP_CONTENT ?
                    child.getHeight() : (lp.getHeight() > 0 ? lp.getHeight() : 0);

                float itemWidth = childWidth + lp.getMarginLeft() + lp.getMarginRight();
                float itemHeight = childHeight + lp.getMarginTop() + lp.getMarginBottom();

                if (currentLineWidth + itemWidth + horizontalSpacing > totalWidth && currentLineWidth > 0) {
                    maxWidth = Math.max(maxWidth, currentLineWidth);
                    accumulatedHeight += currentLineHeight + verticalSpacing;
                    currentLineWidth = 0;
                    currentLineHeight = 0;
                }

                currentLineWidth += itemWidth + horizontalSpacing;
                currentLineHeight = Math.max(currentLineHeight, itemHeight);
            }

            maxWidth = Math.max(maxWidth, currentLineWidth);
            accumulatedHeight += currentLineHeight;

            totalWidth += maxWidth;
            totalHeight += accumulatedHeight;
        } else {
            float maxHeight = 0;
            float accumulatedWidth = 0;
            float currentColumnHeight = 0;
            float currentColumnWidth = 0;

            for (UIComponent child : children) {
                LayoutParams lp = getChildLayoutParams(child);
                float childWidth = lp.getWidth() == LayoutParams.WRAP_CONTENT ?
                    child.getWidth() : (lp.getWidth() > 0 ? lp.getWidth() : 0);
                float childHeight = lp.getHeight() == LayoutParams.WRAP_CONTENT ?
                    child.getHeight() : (lp.getHeight() > 0 ? lp.getHeight() : 0);

                float itemWidth = childWidth + lp.getMarginLeft() + lp.getMarginRight();
                float itemHeight = childHeight + lp.getMarginTop() + lp.getMarginBottom();

                if (currentColumnHeight + itemHeight + verticalSpacing > totalHeight && currentColumnHeight > 0) {
                    maxHeight = Math.max(maxHeight, currentColumnHeight);
                    accumulatedWidth += currentColumnWidth + horizontalSpacing;
                    currentColumnHeight = 0;
                    currentColumnWidth = 0;
                }

                currentColumnHeight += itemHeight + verticalSpacing;
                currentColumnWidth = Math.max(currentColumnWidth, itemWidth);
            }

            maxHeight = Math.max(maxHeight, currentColumnHeight);
            accumulatedWidth += currentColumnWidth;

            totalWidth += accumulatedWidth;
            totalHeight += maxHeight;
        }

        return new float[]{totalWidth, totalHeight};
    }

    /**
     * 流向
     */
    public enum FlowDirection {
        HORIZONTAL,
        VERTICAL
    }

    /**
     * 流式项目
     */
    private static class FlowItem {
        UIComponent child;
        LayoutParams lp;
        float width;
        float height;

        FlowItem(UIComponent child, LayoutParams lp, float width, float height) {
            this.child = child;
            this.lp = lp;
            this.width = width;
            this.height = height;
        }
    }
}
