package io.github.s3s3l.yggdrasil.game.core.ui.layout;

import io.github.s3s3l.yggdrasil.game.core.ui.UIComponent;

import java.util.HashMap;
import java.util.Map;

/**
 * 锚点布局
 * 通过锚点定位子组件
 */
public class UILayoutAnchorLayout extends UILayoutBase {

    private Map<UIComponent, AnchorParams> anchorParams;

    public UILayoutAnchorLayout() {
        this.anchorParams = new HashMap<>();
    }

    /**
     * 设置组件的锚点参数
     */
    public void setAnchor(UIComponent child, Anchor anchor, float offsetX, float offsetY) {
        if (child != null) {
            AnchorParams params = anchorParams.computeIfAbsent(child, k -> new AnchorParams());
            params.anchor = anchor;
            params.offsetX = offsetX;
            params.offsetY = offsetY;
            markDirty();
        }
    }

    /**
     * 设置组件的锚点参数（使用 AnchorParams 对象）
     */
    public void setAnchorParams(UIComponent child, AnchorParams params) {
        if (child != null && params != null) {
            anchorParams.put(child, params);
            markDirty();
        }
    }

    /**
     * 获取组件的锚点参数
     */
    public AnchorParams getAnchorParams(UIComponent child) {
        return anchorParams.get(child);
    }

    /**
     * 移除组件的锚点
     */
    public void removeAnchor(UIComponent child) {
        anchorParams.remove(child);
        markDirty();
    }

    /**
     * 清除所有锚点
     */
    public void clearAnchors() {
        anchorParams.clear();
        markDirty();
    }

    @Override
    public void addChild(UIComponent child) {
        super.addChild(child);
        if (!anchorParams.containsKey(child)) {
            anchorParams.put(child, new AnchorParams());
        }
    }

    @Override
    public void removeChild(UIComponent child) {
        super.removeChild(child);
        anchorParams.remove(child);
    }

    @Override
    public void clearChildren() {
        super.clearChildren();
        anchorParams.clear();
    }

    @Override
    protected void onLayout(float width, float height) {
        if (children.isEmpty()) {
            return;
        }

        float contentWidth = width - paddingLeft - paddingRight;
        float contentHeight = height - paddingTop - paddingBottom;

        for (UIComponent child : children) {
            AnchorParams params = anchorParams.get(child);
            if (params == null) {
                params = new AnchorParams();
            }

            LayoutParams lp = getChildLayoutParams(child);

            float childWidth = getChildWidth(child, lp, contentWidth);
            float childHeight = getChildHeight(child, lp, contentHeight);

            float x = calculateX(params, childWidth, contentWidth);
            float y = calculateY(params, childHeight, contentHeight);

            child.setX(x + paddingLeft + lp.getMarginLeft());
            child.setY(y + paddingTop + lp.getMarginTop());
            if (childWidth > 0) child.setWidth(childWidth);
            if (childHeight > 0) child.setHeight(childHeight);
        }
    }

    private float calculateX(AnchorParams params, float childWidth, float contentWidth) {
        float x = 0;

        switch (params.anchor) {
            case TOP_LEFT:
            case MIDDLE_LEFT:
            case BOTTOM_LEFT:
                x = params.offsetX;
                break;
            case TOP_CENTER:
            case MIDDLE_CENTER:
            case BOTTOM_CENTER:
                x = (contentWidth - childWidth) / 2 + params.offsetX;
                break;
            case TOP_RIGHT:
            case MIDDLE_RIGHT:
            case BOTTOM_RIGHT:
                x = contentWidth - childWidth + params.offsetX;
                break;
            case CUSTOM:
                x = params.customX * contentWidth + params.offsetX;
                break;
        }

        return x;
    }

    private float calculateY(AnchorParams params, float childHeight, float contentHeight) {
        float y = 0;

        switch (params.anchor) {
            case TOP_LEFT:
            case TOP_CENTER:
            case TOP_RIGHT:
                y = params.offsetY;
                break;
            case MIDDLE_LEFT:
            case MIDDLE_CENTER:
            case MIDDLE_RIGHT:
                y = (contentHeight - childHeight) / 2 + params.offsetY;
                break;
            case BOTTOM_LEFT:
            case BOTTOM_CENTER:
            case BOTTOM_RIGHT:
                y = contentHeight - childHeight + params.offsetY;
                break;
            case CUSTOM:
                y = params.customY * contentHeight + params.offsetY;
                break;
        }

        return y;
    }

    private float getChildWidth(UIComponent child, LayoutParams lp, float availableWidth) {
        if (lp.getWidth() == LayoutParams.MATCH_PARENT) {
            return availableWidth - lp.getMarginLeft() - lp.getMarginRight();
        } else if (lp.getWidth() == LayoutParams.WRAP_CONTENT) {
            return child.getWidth();
        } else if (lp.getWidth() > 0) {
            return lp.getWidth();
        }
        return child.getWidth();
    }

    private float getChildHeight(UIComponent child, LayoutParams lp, float availableHeight) {
        if (lp.getHeight() == LayoutParams.MATCH_PARENT) {
            return availableHeight - lp.getMarginTop() - lp.getMarginBottom();
        } else if (lp.getHeight() == LayoutParams.WRAP_CONTENT) {
            return child.getHeight();
        } else if (lp.getHeight() > 0) {
            return lp.getHeight();
        }
        return child.getHeight();
    }

    @Override
    protected float[] onMeasure() {
        float totalWidth = paddingLeft + paddingRight;
        float totalHeight = paddingTop + paddingBottom;

        for (UIComponent child : children) {
            AnchorParams params = anchorParams.get(child);
            LayoutParams lp = getChildLayoutParams(child);

            float childWidth = lp.getWidth() == LayoutParams.WRAP_CONTENT ?
                child.getWidth() : (lp.getWidth() > 0 ? lp.getWidth() : 0);
            float childHeight = lp.getHeight() == LayoutParams.WRAP_CONTENT ?
                child.getHeight() : (lp.getHeight() > 0 ? lp.getHeight() : 0);

            totalWidth = Math.max(totalWidth, childWidth + lp.getMarginLeft() + lp.getMarginRight());
            totalHeight = Math.max(totalHeight, childHeight + lp.getMarginTop() + lp.getMarginBottom());
        }

        return new float[]{totalWidth, totalHeight};
    }

    /**
     * 锚点枚举
     */
    public enum Anchor {
        TOP_LEFT,
        TOP_CENTER,
        TOP_RIGHT,
        MIDDLE_LEFT,
        MIDDLE_CENTER,
        MIDDLE_RIGHT,
        BOTTOM_LEFT,
        BOTTOM_CENTER,
        BOTTOM_RIGHT,
        CUSTOM
    }

    /**
     * 锚点参数
     */
    public static class AnchorParams {
        public Anchor anchor;
        public float offsetX;
        public float offsetY;
        public float customX;
        public float customY;

        public AnchorParams() {
            this.anchor = Anchor.TOP_LEFT;
            this.offsetX = 0;
            this.offsetY = 0;
            this.customX = 0;
            this.customY = 0;
        }

        public AnchorParams(Anchor anchor) {
            this(anchor, 0, 0);
        }

        public AnchorParams(Anchor anchor, float offsetX, float offsetY) {
            this.anchor = anchor;
            this.offsetX = offsetX;
            this.offsetY = offsetY;
            this.customX = 0;
            this.customY = 0;
        }

        public Anchor getAnchor() {
            return anchor;
        }

        public void setAnchor(Anchor anchor) {
            this.anchor = anchor;
        }

        public float getOffsetX() {
            return offsetX;
        }

        public void setOffsetX(float offsetX) {
            this.offsetX = offsetX;
        }

        public float getOffsetY() {
            return offsetY;
        }

        public void setOffsetY(float offsetY) {
            this.offsetY = offsetY;
        }

        public float getCustomX() {
            return customX;
        }

        public void setCustomX(float customX) {
            this.customX = customX;
            this.anchor = Anchor.CUSTOM;
        }

        public float getCustomY() {
            return customY;
        }

        public void setCustomY(float customY) {
            this.customY = customY;
            this.anchor = Anchor.CUSTOM;
        }

        /**
         * 设置自定义锚点位置（0-1 之间的归一化坐标）
         */
        public void setCustomPosition(float x, float y) {
            this.customX = x;
            this.customY = y;
            this.anchor = Anchor.CUSTOM;
        }
    }
}
