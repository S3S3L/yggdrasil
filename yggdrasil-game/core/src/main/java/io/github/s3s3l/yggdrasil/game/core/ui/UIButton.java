package io.github.s3s3l.yggdrasil.game.core.ui;

/**
 * UI 按钮
 * 可点击的按钮组件
 */
public class UIButton extends UIComponent {

    private String text;
    private UIColor textColor;
    private float fontSize;
    private Runnable onClickListener;
    private boolean hovered;
    private boolean pressed;

    public UIButton() {
        this(null);
    }

    public UIButton(String text) {
        this.text = text;
        this.textColor = UIColor.WHITE;
        this.fontSize = 16;
        this.hovered = false;
        this.pressed = false;
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

    public Runnable getOnClickListener() {
        return onClickListener;
    }

    public void setOnClickListener(Runnable onClickListener) {
        this.onClickListener = onClickListener;
    }

    public boolean isHovered() {
        return hovered;
    }

    public void setHovered(boolean hovered) {
        this.hovered = hovered;
    }

    public boolean isPressed() {
        return pressed;
    }

    public void setPressed(boolean pressed) {
        this.pressed = pressed;
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
     * 渲染按钮背景
     */
    protected void renderBackground() {
        // 根据状态选择背景颜色
        UIColor bgColor = getBackgroundColor();
        if (pressed) {
            bgColor = bgColor.darken(0.2f);
        } else if (hovered) {
            bgColor = bgColor.lighten(0.1f);
        }

        // 实际渲染由游戏引擎实现
    }

    /**
     * 渲染按钮文本
     */
    protected void renderText() {
        // 实际渲染由游戏引擎实现
    }
}
