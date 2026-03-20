package io.github.s3s3l.yggdrasil.game.core.ui.layout;

import io.github.s3s3l.yggdrasil.game.core.ui.UIComponent;

/**
 * 线性布局
 * 按水平或垂直方向依次排列子组件
 */
public class UILayoutBoxLayout extends UILayoutBase {

    private Direction direction;
    private HorizontalAlign horizontalAlign;
    private VerticalAlign verticalAlign;
    private float spacing;
    private boolean reverse;

    public UILayoutBoxLayout() {
        this(Direction.VERTICAL, 0);
    }

    public UILayoutBoxLayout(Direction direction) {
        this(direction, 0);
    }

    public UILayoutBoxLayout(Direction direction, float spacing) {
        this.direction = direction;
        this.spacing = spacing;
        this.horizontalAlign = HorizontalAlign.START;
        this.verticalAlign = VerticalAlign.START;
        this.reverse = false;
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
        markDirty();
    }

    public float getSpacing() {
        return spacing;
    }

    public void setSpacing(float spacing) {
        this.spacing = spacing;
        markDirty();
    }

    public HorizontalAlign getHorizontalAlign() {
        return horizontalAlign;
    }

    public void setHorizontalAlign(HorizontalAlign align) {
        this.horizontalAlign = align;
        markDirty();
    }

    public VerticalAlign getVerticalAlign() {
        return verticalAlign;
    }

    public void setVerticalAlign(VerticalAlign align) {
        this.verticalAlign = align;
        markDirty();
    }

    public boolean isReverse() {
        return reverse;
    }

    public void setReverse(boolean reverse) {
        this.reverse = reverse;
        markDirty();
    }

    @Override
    protected void onLayout(float width, float height) {
        if (children.isEmpty()) {
            return;
        }

        float contentWidth = width - paddingLeft - paddingRight;
        float contentHeight = height - paddingTop - paddingBottom;

        if (direction == Direction.VERTICAL) {
            layoutVertical(contentWidth, contentHeight);
        } else {
            layoutHorizontal(contentWidth, contentHeight);
        }
    }

    private void layoutVertical(float contentWidth, float contentHeight) {
        float availableHeight = contentHeight;
        float totalSpacing = spacing * (children.size() - 1);
        float totalChildHeight = 0;

        // 计算所有子组件的总高度
        for (UIComponent child : children) {
            LayoutParams lp = getChildLayoutParams(child);
            float childHeight = lp.getHeight() == LayoutParams.WRAP_CONTENT ?
                child.getHeight() : lp.getHeight();
            if (childHeight == LayoutParams.MATCH_PARENT) {
                childHeight = contentHeight / children.size();
            }
            totalChildHeight += childHeight;
        }

        float startY = paddingTop;
        float extraSpace = availableHeight - totalChildHeight - totalSpacing;

        // 根据垂直对齐方式调整起始位置
        if (verticalAlign == VerticalAlign.CENTER) {
            startY += extraSpace / 2;
        } else if (verticalAlign == VerticalAlign.END || verticalAlign == VerticalAlign.BOTTOM) {
            startY += extraSpace;
        }

        float currentY = startY;

        for (int i = 0; i < children.size(); i++) {
            UIComponent child = children.get(reverse ? children.size() - 1 - i : i);
            LayoutParams lp = getChildLayoutParams(child);

            float childWidth = lp.getWidth() == LayoutParams.MATCH_PARENT ?
                contentWidth - lp.getMarginLeft() - lp.getMarginRight() :
                (lp.getWidth() == LayoutParams.WRAP_CONTENT ? child.getWidth() : lp.getWidth());

            float childHeight = lp.getHeight() == LayoutParams.MATCH_PARENT ?
                (contentHeight - totalSpacing) / children.size() - lp.getMarginTop() - lp.getMarginBottom() :
                (lp.getHeight() == LayoutParams.WRAP_CONTENT ? child.getHeight() : lp.getHeight());

            // 水平对齐
            float childX = paddingLeft + lp.getMarginLeft();
            if (horizontalAlign == HorizontalAlign.CENTER) {
                childX = paddingLeft + (contentWidth - childWidth - lp.getMarginLeft() - lp.getMarginRight()) / 2;
            } else if (horizontalAlign == HorizontalAlign.RIGHT || horizontalAlign == HorizontalAlign.END) {
                childX = paddingLeft + contentWidth - childWidth - lp.getMarginRight();
            }

            child.setX(childX);
            child.setY(currentY + lp.getMarginTop());
            if (childWidth > 0) child.setWidth(childWidth);
            if (childHeight > 0) child.setHeight(childHeight);

            currentY += childHeight + lp.getMarginTop() + lp.getMarginBottom() + spacing;
        }
    }

    private void layoutHorizontal(float contentWidth, float contentHeight) {
        float availableWidth = contentWidth;
        float totalSpacing = spacing * (children.size() - 1);
        float totalChildWidth = 0;

        // 计算所有子组件的总宽度
        for (UIComponent child : children) {
            LayoutParams lp = getChildLayoutParams(child);
            float childWidth = lp.getWidth() == LayoutParams.WRAP_CONTENT ?
                child.getWidth() : lp.getWidth();
            if (childWidth == LayoutParams.MATCH_PARENT) {
                childWidth = contentWidth / children.size();
            }
            totalChildWidth += Math.max(0, childWidth);
        }

        float startX = paddingLeft;
        float extraSpace = availableWidth - totalChildWidth - totalSpacing;

        // 根据水平对齐方式调整起始位置
        if (horizontalAlign == HorizontalAlign.CENTER) {
            startX += extraSpace / 2;
        } else if (horizontalAlign == HorizontalAlign.END || horizontalAlign == HorizontalAlign.RIGHT) {
            startX += extraSpace;
        }

        float currentX = startX;

        for (int i = 0; i < children.size(); i++) {
            UIComponent child = children.get(reverse ? children.size() - 1 - i : i);
            LayoutParams lp = getChildLayoutParams(child);

            float childWidth = lp.getWidth() == LayoutParams.MATCH_PARENT ?
                (contentWidth - totalSpacing) / children.size() - lp.getMarginLeft() - lp.getMarginRight() :
                (lp.getWidth() == LayoutParams.WRAP_CONTENT ? child.getWidth() : lp.getWidth());

            float childHeight = lp.getHeight() == LayoutParams.MATCH_PARENT ?
                contentHeight - lp.getMarginTop() - lp.getMarginBottom() :
                (lp.getHeight() == LayoutParams.WRAP_CONTENT ? child.getHeight() : lp.getHeight());

            // 垂直对齐
            float childY = paddingTop + lp.getMarginTop();
            if (verticalAlign == VerticalAlign.CENTER) {
                childY = paddingTop + (contentHeight - childHeight - lp.getMarginTop() - lp.getMarginBottom()) / 2;
            } else if (verticalAlign == VerticalAlign.END || verticalAlign == VerticalAlign.BOTTOM) {
                childY = paddingTop + contentHeight - childHeight - lp.getMarginBottom();
            }

            child.setX(currentX + lp.getMarginLeft());
            child.setY(childY);
            if (childWidth > 0) child.setWidth(childWidth);
            if (childHeight > 0) child.setHeight(childHeight);

            currentX += childWidth + lp.getMarginLeft() + lp.getMarginRight() + spacing;
        }
    }

    @Override
    protected float[] onMeasure() {
        float totalWidth = paddingLeft + paddingRight;
        float totalHeight = paddingTop + paddingBottom;
        float maxChildDimension = 0;

        if (direction == Direction.VERTICAL) {
            for (UIComponent child : children) {
                LayoutParams lp = getChildLayoutParams(child);
                float childHeight = lp.getHeight() == LayoutParams.WRAP_CONTENT ?
                    child.getHeight() : (lp.getHeight() > 0 ? lp.getHeight() : 0);
                float childWidth = lp.getWidth() == LayoutParams.WRAP_CONTENT ?
                    child.getWidth() : (lp.getWidth() > 0 ? lp.getWidth() : 0);

                totalHeight += childHeight + lp.getMarginTop() + lp.getMarginBottom();
                maxChildDimension = Math.max(maxChildDimension, childWidth + lp.getMarginLeft() + lp.getMarginRight());
            }
            if (!children.isEmpty()) {
                totalHeight += spacing * (children.size() - 1);
            }
            totalWidth += maxChildDimension;
        } else {
            for (UIComponent child : children) {
                LayoutParams lp = getChildLayoutParams(child);
                float childWidth = lp.getWidth() == LayoutParams.WRAP_CONTENT ?
                    child.getWidth() : (lp.getWidth() > 0 ? lp.getWidth() : 0);
                float childHeight = lp.getHeight() == LayoutParams.WRAP_CONTENT ?
                    child.getHeight() : (lp.getHeight() > 0 ? lp.getHeight() : 0);

                totalWidth += childWidth + lp.getMarginLeft() + lp.getMarginRight();
                maxChildDimension = Math.max(maxChildDimension, childHeight + lp.getMarginTop() + lp.getMarginBottom());
            }
            if (!children.isEmpty()) {
                totalWidth += spacing * (children.size() - 1);
            }
            totalHeight += maxChildDimension;
        }

        return new float[]{totalWidth, totalHeight};
    }
}
