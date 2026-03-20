package io.github.s3s3l.yggdrasil.game.core.ui;

/**
 * UI 标签按钮组件
 * 用于 UITabContainer 中的标签按钮
 */
public class TabButton extends UIComponent {

    private String text;
    private UIColor textColor;
    private UIColor backgroundColor;
    private UIColor selectedColor;
    private UIColor hoverColor;
    private boolean selected;
    private boolean hovered;
    private float fontSize;
    private Runnable onClickListener;

    public TabButton() {
        this("");
    }

    public TabButton(String text) {
        this.text = text;
        this.textColor = UIColor.WHITE;
        this.backgroundColor = new UIColor(0.2f, 0.2f, 0.2f, 1f);
        this.selectedColor = UIColor.BLUE;
        this.hoverColor = new UIColor(0.3f, 0.3f, 0.3f, 1f);
        this.selected = false;
        this.hovered = false;
        this.fontSize = 14;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public UIColor getTextColor() {
        return textColor;
    }

    public void setTextColor(UIColor textColor) {
        this.textColor = textColor;
    }

    public UIColor getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(UIColor backgroundColor) {
        this.backgroundColor = backgroundColor;
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

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public boolean isHovered() {
        return hovered;
    }

    public void setHovered(boolean hovered) {
        this.hovered = hovered;
    }

    public float getFontSize() {
        return fontSize;
    }

    public void setFontSize(float fontSize) {
        this.fontSize = fontSize;
    }

    public Runnable getOnClickListener() {
        return onClickListener;
    }

    public void setOnClickListener(Runnable onClickListener) {
        this.onClickListener = onClickListener;
    }

    @Override
    public void onClick() {
        if (onClickListener != null) {
            onClickListener.run();
        }
    }

    @Override
    public void render() {
        if (!isVisible()) {
            return;
        }

        // 渲染背景
        renderBackground();

        // 渲染文本
        renderText();
    }

    /**
     * 渲染背景
     */
    protected void renderBackground() {
        // 根据状态选择背景颜色
        // 实际渲染由游戏引擎实现
    }

    /**
     * 渲染文本
     */
    protected void renderText() {
        // 实际渲染由游戏引擎实现
    }

    /**
     * 获取当前背景颜色（根据状态）
     *
     * @return 当前背景颜色
     */
    public UIColor getCurrentBackgroundColor() {
        if (selected) {
            return selectedColor;
        } else if (hovered) {
            return hoverColor;
        }
        return backgroundColor;
    }
}
