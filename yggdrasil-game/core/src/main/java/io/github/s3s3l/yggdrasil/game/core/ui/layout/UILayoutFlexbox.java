package io.github.s3s3l.yggdrasil.game.core.ui.layout;

import io.github.s3s3l.yggdrasil.game.core.ui.UIComponent;

import java.util.ArrayList;
import java.util.List;

/**
 * 弹性布局
 * 支持灵活的子组件大小分配和换行
 */
public class UILayoutFlexbox extends UILayoutBase {

    private FlexDirection flexDirection;
    private FlexWrap flexWrap;
    private JustifyContent justifyContent;
    private AlignItems alignItems;
    private AlignContent alignContent;
    private float gap;

    public UILayoutFlexbox() {
        this.flexDirection = FlexDirection.ROW;
        this.flexWrap = FlexWrap.WRAP;
        this.justifyContent = JustifyContent.FLEX_START;
        this.alignItems = AlignItems.STRETCH;
        this.alignContent = AlignContent.STRETCH;
        this.gap = 0;
    }

    public FlexDirection getFlexDirection() {
        return flexDirection;
    }

    public void setFlexDirection(FlexDirection flexDirection) {
        this.flexDirection = flexDirection;
        markDirty();
    }

    public FlexWrap getFlexWrap() {
        return flexWrap;
    }

    public void setFlexWrap(FlexWrap flexWrap) {
        this.flexWrap = flexWrap;
        markDirty();
    }

    public JustifyContent getJustifyContent() {
        return justifyContent;
    }

    public void setJustifyContent(JustifyContent justifyContent) {
        this.justifyContent = justifyContent;
        markDirty();
    }

    public AlignItems getAlignItems() {
        return alignItems;
    }

    public void setAlignItems(AlignItems alignItems) {
        this.alignItems = alignItems;
        markDirty();
    }

    public AlignContent getAlignContent() {
        return alignContent;
    }

    public void setAlignContent(AlignContent alignContent) {
        this.alignContent = alignContent;
        markDirty();
    }

    public float getGap() {
        return gap;
    }

    public void setGap(float gap) {
        this.gap = gap;
        markDirty();
    }

    @Override
    protected void onLayout(float width, float height) {
        if (children.isEmpty()) {
            return;
        }

        float contentWidth = width - paddingLeft - paddingRight;
        float contentHeight = height - paddingTop - paddingBottom;

        // 根据主轴方向计算
        if (flexDirection == FlexDirection.ROW) {
            layoutRow(contentWidth, contentHeight);
        } else {
            layoutColumn(contentWidth, contentHeight);
        }
    }

    private void layoutRow(float contentWidth, float contentHeight) {
        // 收集所有行
        List<List<FlexLine>> lines = new ArrayList<>();
        List<FlexLine> currentLine = new ArrayList<>();
        float currentLineSize = 0;

        for (int i = 0; i < children.size(); i++) {
            UIComponent child = children.get(i);
            LayoutParams lp = getChildLayoutParams(child);
            FlexParams fp = getFlexParams(child);

            float childWidth = getChildWidth(child, lp, fp, contentWidth);
            float childHeight = getChildHeight(child, lp, fp, contentHeight);

            // 检查是否需要换行
            float itemSize = childWidth + (currentLine.isEmpty() ? 0 : gap);
            if (flexWrap == FlexWrap.WRAP &&
                !currentLine.isEmpty() &&
                currentLineSize + itemSize > contentWidth) {
                lines.add(currentLine);
                currentLine = new ArrayList<>();
                currentLineSize = 0;
            }

            currentLine.add(new FlexLine(i, child, lp, fp, childWidth, childHeight));
            currentLineSize += itemSize;
        }

        if (!currentLine.isEmpty()) {
            lines.add(currentLine);
        }

        // 计算行的位置和高度
        float mainOffset = flexDirection == FlexDirection.ROW ? paddingTop : paddingLeft;
        float crossOffset = flexDirection == FlexDirection.ROW ? paddingLeft : paddingTop;
        float contentSize = flexDirection == FlexDirection.ROW ? contentHeight : contentWidth;

        // 根据 justifyContent 计算主轴起始偏移
        float mainStartOffset = calculateMainStartOffset(lines, contentSize, mainOffset);

        // 布局每一行
        for (List<FlexLine> line : lines) {
            float lineMainSize = 0;
            float lineCrossSize = 0;

            // 计算行的尺寸
            for (FlexLine flexLine : line) {
                lineMainSize += flexLine.mainSize;
                lineCrossSize = Math.max(lineCrossSize, flexLine.crossSize);
            }

            // 计算行内间距
            float spaceBetweenItems = calculateSpaceBetweenItems(line, contentSize);

            // 布局行内子组件
            float currentMainPos = mainStartOffset;
            for (FlexLine flexLine : line) {
                float mainPos = currentMainPos;
                float crossPos = crossOffset;

                // 根据 alignItems 调整交叉轴位置
                if (flexDirection == FlexDirection.ROW) {
                    crossPos += calculateCrossAlignment(flexLine, lineCrossSize, alignItems);
                } else {
                    mainPos += calculateCrossAlignment(flexLine, lineCrossSize, alignItems);
                }

                // 设置子组件位置
                if (flexDirection == FlexDirection.ROW) {
                    flexLine.child.setX(mainPos + flexLine.lp.getMarginLeft());
                    flexLine.child.setY(crossPos + flexLine.lp.getMarginTop());
                } else {
                    flexLine.child.setX(crossPos + flexLine.lp.getMarginLeft());
                    flexLine.child.setY(mainPos + flexLine.lp.getMarginTop());
                }

                flexLine.child.setWidth(flexLine.mainSize);
                flexLine.child.setHeight(flexLine.crossSize);

                currentMainPos += flexLine.mainSize + gap + spaceBetweenItems;
            }

            // 更新下一行的起始位置
            if (flexDirection == FlexDirection.ROW) {
                mainStartOffset += lineCrossSize + gap;
            } else {
                mainStartOffset += lineCrossSize + gap;
            }
        }
    }

    private void layoutColumn(float contentWidth, float contentHeight) {
        // 垂直方向布局，与行布局类似
        List<List<FlexLine>> lines = new ArrayList<>();
        List<FlexLine> currentLine = new ArrayList<>();
        float currentLineSize = 0;

        for (int i = 0; i < children.size(); i++) {
            UIComponent child = children.get(i);
            LayoutParams lp = getChildLayoutParams(child);
            FlexParams fp = getFlexParams(child);

            float childWidth = getChildWidth(child, lp, fp, contentWidth);
            float childHeight = getChildHeight(child, lp, fp, contentHeight);

            float itemSize = childHeight + (currentLine.isEmpty() ? 0 : gap);
            if (flexWrap == FlexWrap.WRAP &&
                !currentLine.isEmpty() &&
                currentLineSize + itemSize > contentHeight) {
                lines.add(currentLine);
                currentLine = new ArrayList<>();
                currentLineSize = 0;
            }

            currentLine.add(new FlexLine(i, child, lp, fp, childWidth, childHeight));
            currentLineSize += itemSize;
        }

        if (!currentLine.isEmpty()) {
            lines.add(currentLine);
        }

        float mainOffset = paddingLeft;
        float crossOffset = paddingTop;
        float contentSize = contentWidth;

        float mainStartOffset = calculateMainStartOffset(lines, contentSize, mainOffset);

        for (List<FlexLine> line : lines) {
            float lineMainSize = 0;
            float lineCrossSize = 0;

            for (FlexLine flexLine : line) {
                lineMainSize += flexLine.crossSize;
                lineCrossSize = Math.max(lineCrossSize, flexLine.mainSize);
            }

            float spaceBetweenItems = calculateSpaceBetweenItems(line, contentSize);

            float currentMainPos = mainStartOffset;
            for (FlexLine flexLine : line) {
                float mainPos = currentMainPos;
                float crossPos = crossOffset;

                if (flexDirection == FlexDirection.COLUMN) {
                    crossPos += calculateCrossAlignment(flexLine, lineCrossSize, alignItems);
                } else {
                    mainPos += calculateCrossAlignment(flexLine, lineCrossSize, alignItems);
                }

                flexLine.child.setX(crossPos + flexLine.lp.getMarginLeft());
                flexLine.child.setY(mainPos + flexLine.lp.getMarginTop());

                flexLine.child.setWidth(flexLine.crossSize);
                flexLine.child.setHeight(flexLine.mainSize);

                currentMainPos += flexLine.crossSize + gap + spaceBetweenItems;
            }

            mainStartOffset += lineCrossSize + gap;
        }
    }

    private float calculateMainStartOffset(List<List<FlexLine>> lines, float contentSize, float offset) {
        float totalSize = 0;
        for (List<FlexLine> line : lines) {
            for (FlexLine flexLine : line) {
                totalSize += flexDirection == FlexDirection.ROW ? flexLine.mainSize : flexLine.crossSize;
            }
        }
        totalSize += gap * (lines.size() - 1);

        float extraSpace = contentSize - totalSize;

        switch (justifyContent) {
            case CENTER:
                return offset + extraSpace / 2;
            case FLEX_END:
                return offset + extraSpace;
            case SPACE_BETWEEN:
            case SPACE_AROUND:
            case SPACE_EVENLY:
                return offset;
            default:
                return offset;
        }
    }

    private float calculateSpaceBetweenItems(List<FlexLine> line, float contentSize) {
        if (line.isEmpty()) return 0;

        float totalItemSize = 0;
        for (FlexLine flexLine : line) {
            totalItemSize += flexLine.mainSize;
        }

        float extraSpace = contentSize - totalItemSize - gap * (line.size() - 1);

        switch (justifyContent) {
            case SPACE_BETWEEN:
                return line.size() > 1 ? extraSpace / (line.size() - 1) : 0;
            case SPACE_AROUND:
                return extraSpace / line.size();
            case SPACE_EVENLY:
                return extraSpace / (line.size() + 1);
            default:
                return 0;
        }
    }

    private float calculateCrossAlignment(FlexLine flexLine, float lineSize, AlignItems align) {
        float extraSpace = lineSize - flexLine.crossSize;

        switch (align) {
            case CENTER:
                return extraSpace / 2;
            case FLEX_END:
                return extraSpace;
            case STRETCH:
                return 0; // stretch handled in size calculation
            default:
                return 0;
        }
    }

    private float getChildWidth(UIComponent child, LayoutParams lp, FlexParams fp, float availableWidth) {
        if (lp.getWidth() == LayoutParams.MATCH_PARENT) {
            return availableWidth - lp.getMarginLeft() - lp.getMarginRight();
        } else if (lp.getWidth() == LayoutParams.WRAP_CONTENT) {
            return child.getWidth() + lp.getMarginLeft() + lp.getMarginRight();
        } else if (lp.getWidth() > 0) {
            return lp.getWidth();
        } else {
            // 根据 flex grow 分配空间
            return availableWidth / children.size();
        }
    }

    private float getChildHeight(UIComponent child, LayoutParams lp, FlexParams fp, float availableHeight) {
        if (lp.getHeight() == LayoutParams.MATCH_PARENT) {
            return availableHeight - lp.getMarginTop() - lp.getMarginBottom();
        } else if (lp.getHeight() == LayoutParams.WRAP_CONTENT) {
            return child.getHeight() + lp.getMarginTop() + lp.getMarginBottom();
        } else if (lp.getHeight() > 0) {
            return lp.getHeight();
        } else {
            return availableHeight / children.size();
        }
    }

    private FlexParams getFlexParams(UIComponent child) {
        if (child instanceof FlexParamsProvider) {
            return ((FlexParamsProvider) child).getFlexParams();
        }
        return new FlexParams();
    }

    @Override
    protected float[] onMeasure() {
        float totalWidth = paddingLeft + paddingRight;
        float totalHeight = paddingTop + paddingBottom;

        if (flexDirection == FlexDirection.ROW) {
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

                if (flexWrap == FlexWrap.WRAP) {
                    if (currentLineWidth + childWidth > totalWidth && currentLineWidth > 0) {
                        maxWidth = Math.max(maxWidth, currentLineWidth);
                        accumulatedHeight += currentLineHeight + gap;
                        currentLineWidth = 0;
                        currentLineHeight = 0;
                    }
                    currentLineWidth += childWidth + gap;
                    currentLineHeight = Math.max(currentLineHeight, childHeight);
                } else {
                    currentLineWidth += childWidth + gap;
                    currentLineHeight = Math.max(currentLineHeight, childHeight);
                }
            }

            if (flexWrap == FlexWrap.WRAP) {
                maxWidth = Math.max(maxWidth, currentLineWidth);
                accumulatedHeight += currentLineHeight;
                totalWidth += maxWidth;
                totalHeight += accumulatedHeight;
            } else {
                totalWidth += currentLineWidth;
                totalHeight += currentLineHeight;
            }
        } else {
            float maxHeight = 0;
            float accumulatedWidth = 0;

            for (UIComponent child : children) {
                LayoutParams lp = getChildLayoutParams(child);
                float childWidth = lp.getWidth() == LayoutParams.WRAP_CONTENT ?
                    child.getWidth() : (lp.getWidth() > 0 ? lp.getWidth() : 0);
                float childHeight = lp.getHeight() == LayoutParams.WRAP_CONTENT ?
                    child.getHeight() : (lp.getHeight() > 0 ? lp.getHeight() : 0);

                accumulatedWidth = Math.max(accumulatedWidth, childWidth);
                maxHeight += childHeight + gap;
            }

            totalWidth += accumulatedWidth;
            totalHeight += maxHeight;
        }

        return new float[]{totalWidth, totalHeight};
    }

    /**
     * 弹性参数提供者接口
     */
    public interface FlexParamsProvider {
        FlexParams getFlexParams();
        void setFlexParams(FlexParams params);
    }

    /**
     * 弹性布局参数
     */
    public static class FlexParams {
        private float flexGrow;
        private float flexShrink;
        private float flexBasis;

        public FlexParams() {
            this.flexGrow = 0;
            this.flexShrink = 1;
            this.flexBasis = -1;
        }

        public float getFlexGrow() {
            return flexGrow;
        }

        public void setFlexGrow(float flexGrow) {
            this.flexGrow = flexGrow;
        }

        public float getFlexShrink() {
            return flexShrink;
        }

        public void setFlexShrink(float flexShrink) {
            this.flexShrink = flexShrink;
        }

        public float getFlexBasis() {
            return flexBasis;
        }

        public void setFlexBasis(float flexBasis) {
            this.flexBasis = flexBasis;
        }
    }

    /**
     * 主轴方向
     */
    public enum FlexDirection {
        ROW,
        COLUMN
    }

    /**
     * 换行方式
     */
    public enum FlexWrap {
        WRAP,
        NOWRAP
    }

    /**
     * 主轴对齐方式
     */
    public enum JustifyContent {
        FLEX_START,
        FLEX_END,
        CENTER,
        SPACE_BETWEEN,
        SPACE_AROUND,
        SPACE_EVENLY
    }

    /**
     * 交叉轴对齐方式
     */
    public enum AlignItems {
        FLEX_START,
        FLEX_END,
        CENTER,
        BASELINE,
        STRETCH
    }

    /**
     * 多行交叉轴对齐方式
     */
    public enum AlignContent {
        FLEX_START,
        FLEX_END,
        CENTER,
        SPACE_BETWEEN,
        SPACE_AROUND,
        STRETCH
    }

    /**
     * 行内项目
     */
    private static class FlexLine {
        int index;
        UIComponent child;
        LayoutParams lp;
        FlexParams fp;
        float mainSize;
        float crossSize;

        FlexLine(int index, UIComponent child, LayoutParams lp, FlexParams fp, float mainSize, float crossSize) {
            this.index = index;
            this.child = child;
            this.lp = lp;
            this.fp = fp;
            this.mainSize = mainSize;
            this.crossSize = crossSize;
        }
    }
}
