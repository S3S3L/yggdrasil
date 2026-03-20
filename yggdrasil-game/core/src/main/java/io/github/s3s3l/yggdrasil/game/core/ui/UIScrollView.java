package io.github.s3s3l.yggdrasil.game.core.ui;

/**
 * UI 滚动视图
 * 支持滚动显示超出边界的内容
 */
public class UIScrollView extends UIPanel {

    private float scrollX;
    private float scrollY;
    private float maxScrollX;
    private float maxScrollY;
    private boolean horizontalScrollEnabled;
    private boolean verticalScrollEnabled;
    private float scrollSensitivity;

    public UIScrollView() {
        this(null);
    }

    public UIScrollView(String name) {
        super(name);
        this.scrollX = 0;
        this.scrollY = 0;
        this.maxScrollX = 0;
        this.maxScrollY = 0;
        this.horizontalScrollEnabled = false;
        this.verticalScrollEnabled = true;
        this.scrollSensitivity = 20f;
    }

    public float getScrollX() {
        return scrollX;
    }

    public void setScrollX(float scrollX) {
        this.scrollX = Math.max(0, Math.min(scrollX, maxScrollX));
    }

    public float getScrollY() {
        return scrollY;
    }

    public void setScrollY(float scrollY) {
        this.scrollY = Math.max(0, Math.min(scrollY, maxScrollY));
    }

    public float getMaxScrollX() {
        return maxScrollX;
    }

    public void setMaxScrollX(float maxScrollX) {
        this.maxScrollX = maxScrollX;
    }

    public float getMaxScrollY() {
        return maxScrollY;
    }

    public void setMaxScrollY(float maxScrollY) {
        this.maxScrollY = maxScrollY;
    }

    public boolean isHorizontalScrollEnabled() {
        return horizontalScrollEnabled;
    }

    public void setHorizontalScrollEnabled(boolean horizontalScrollEnabled) {
        this.horizontalScrollEnabled = horizontalScrollEnabled;
    }

    public boolean isVerticalScrollEnabled() {
        return verticalScrollEnabled;
    }

    public void setVerticalScrollEnabled(boolean verticalScrollEnabled) {
        this.verticalScrollEnabled = verticalScrollEnabled;
    }

    public float getScrollSensitivity() {
        return scrollSensitivity;
    }

    public void setScrollSensitivity(float scrollSensitivity) {
        this.scrollSensitivity = scrollSensitivity;
    }

    /**
     * 处理滚动
     *
     * @param deltaX 水平滚动量
     * @param deltaY 垂直滚动量
     */
    public void handleScroll(float deltaX, float deltaY) {
        if (horizontalScrollEnabled) {
            setScrollX(scrollX + deltaX * scrollSensitivity);
        }
        if (verticalScrollEnabled) {
            setScrollY(scrollY + deltaY * scrollSensitivity);
        }
    }

    /**
     * 更新滚动范围
     */
    public void updateScrollRange() {
        float contentWidth = 0;
        float contentHeight = 0;

        for (UIComponent child : getChildren()) {
            contentWidth = Math.max(contentWidth, child.getX() + child.getWidth());
            contentHeight = Math.max(contentHeight, child.getY() + child.getHeight());
        }

        maxScrollX = Math.max(0, contentWidth - getBounds().getWidth());
        maxScrollY = Math.max(0, contentHeight - getBounds().getHeight());
    }

    @Override
    public void addChild(UIComponent child) {
        super.addChild(child);
        updateScrollRange();
    }

    @Override
    public void render() {
        if (!isVisible()) {
            return;
        }

        // 渲染背景
        if (getBackgroundColor() != null && getBackgroundColor().getA() > 0) {
            renderBackground();
        }

        // 保存当前变换状态
        float offsetX = getBounds().getX() - scrollX;
        float offsetY = getBounds().getY() - scrollY;

        // 渲染子组件（应用滚动偏移）
        for (UIComponent child : getChildren()) {
            float originalX = child.getX();
            float originalY = child.getY();

            // 检查子组件是否在可视区域内
            float childRight = originalX + child.getWidth();
            float childBottom = originalY + child.getHeight();
            float viewRight = getBounds().getX() + getBounds().getWidth();
            float viewBottom = getBounds().getY() + getBounds().getHeight();

            if (childRight > offsetX && originalX < viewRight &&
                childBottom > offsetY && originalY < viewBottom) {
                child.render();
            }
        }
    }
}
