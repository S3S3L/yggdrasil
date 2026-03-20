package io.github.s3s3l.yggdrasil.game.core.ui;

/**
 * UI 面板
 * 用于组织和布局子组件的容器
 */
public class UIPanel extends UIComponent {

    private final UIDirection layoutDirection;
    private float spacing;
    private boolean autoSize;

    public UIPanel() {
        this(null);
    }

    public UIPanel(String name) {
        super(name);
        this.layoutDirection = UIDirection.VERTICAL;
        this.spacing = 0;
        this.autoSize = false;
    }

    public UIDirection getLayoutDirection() {
        return layoutDirection;
    }

    public float getSpacing() {
        return spacing;
    }

    public void setSpacing(float spacing) {
        this.spacing = spacing;
    }

    public boolean isAutoSize() {
        return autoSize;
    }

    public void setAutoSize(boolean autoSize) {
        this.autoSize = autoSize;
    }

    /**
     * 执行布局
     */
    public void layout() {
        if (getChildren().isEmpty()) {
            return;
        }

        float currentX = getBounds().getX() + getPadding().getX();
        float currentY = getBounds().getY() + getPadding().getY();

        float maxWidth = 0;
        float maxHeight = 0;

        for (UIComponent child : getChildren()) {
            if (child.getVisibility() == UIVisibility.COLLAPSED) {
                continue;
            }

            // 设置子组件位置
            child.setPosition(currentX, currentY);

            if (layoutDirection == UIDirection.HORIZONTAL) {
                currentX += child.getWidth() + spacing;
                maxWidth += child.getWidth() + spacing;
                maxHeight = Math.max(maxHeight, child.getHeight());
            } else {
                currentY += child.getHeight() + spacing;
                maxHeight += child.getHeight() + spacing;
                maxWidth = Math.max(maxWidth, child.getWidth());
            }
        }

        // 如果启用自动大小，调整面板大小
        if (autoSize) {
            float totalWidth = maxWidth + getPadding().getX() * 2;
            float totalHeight = maxHeight + getPadding().getY() * 2;
            setSize(totalWidth, totalHeight);
        }
    }

    @Override
    public void addChild(UIComponent child) {
        super.addChild(child);
        if (autoSize) {
            layout();
        }
    }

    @Override
    public void removeChild(UIComponent child) {
        super.removeChild(child);
        if (autoSize) {
            layout();
        }
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

        // 渲染子组件
        for (UIComponent child : getChildren()) {
            child.render();
        }
    }

    /**
     * 渲染背景
     */
    protected void renderBackground() {
        // 子类可以实现具体的背景渲染逻辑
        // 这里只定义接口，实际渲染由游戏引擎实现
    }
}
