package io.github.s3s3l.yggdrasil.game.core.ui;

/**
 * UI 标签
 * 用于显示文本的组件
 */
public class UILabel extends UIComponent {

    private String text;
    private UIColor textColor;
    private float fontSize;
    private UIAlign textAlign;
    private boolean wrap;
    private int maxLines;

    public UILabel() {
        this("");
    }

    public UILabel(String text) {
        this.text = text;
        this.textColor = UIColor.WHITE;
        this.fontSize = 16;
        this.textAlign = UIAlign.TOP_LEFT;
        this.wrap = false;
        this.maxLines = 0;
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

    public float getFontSize() {
        return fontSize;
    }

    public void setFontSize(float fontSize) {
        this.fontSize = fontSize;
    }

    public UIAlign getTextAlign() {
        return textAlign;
    }

    public void setTextAlign(UIAlign textAlign) {
        this.textAlign = textAlign;
    }

    public boolean isWrap() {
        return wrap;
    }

    public void setWrap(boolean wrap) {
        this.wrap = wrap;
    }

    public int getMaxLines() {
        return maxLines;
    }

    public void setMaxLines(int maxLines) {
        this.maxLines = maxLines;
    }

    @Override
    public void render() {
        if (!isVisible()) {
            return;
        }

        // 渲染背景（如果有）
        if (getBackgroundColor() != null && getBackgroundColor().getA() > 0) {
            renderBackground();
        }

        // 渲染文本
        renderText();
    }

    /**
     * 渲染背景
     */
    protected void renderBackground() {
        // 实际渲染由游戏引擎实现
    }

    /**
     * 渲染文本
     */
    protected void renderText() {
        // 实际渲染由游戏引擎实现
    }
}
