package io.github.s3s3l.yggdrasil.game.core.ui;

/**
 * UI 多选框组件
 * 用于选中/取消选中的组件，支持单选按钮模式
 */
public class UICheckbox extends UIComponent {

    private String text;
    private UIColor textColor;
    private float fontSize;
    private boolean checked;
    private boolean isRadio;              // 单选模式
    private CheckState state;             // 检查状态
    private ChangeListener listener;      // 状态变化监听器

    /**
     * 检查状态枚举
     */
    public enum CheckState {
        /**
         * 未选中
         */
        UNCHECKED,
        /**
         * 选中
         */
        CHECKED,
        /**
         * 部分选中（用于多选框组）
         */
        INDETERMINATE
    }

    /**
     * 状态变化监听器接口
     */
    public interface ChangeListener {
        /**
         * 当状态改变时调用
         *
         * @param checkbox 多选框组件
         * @param isChecked 是否选中
         */
        void onChanged(UICheckbox checkbox, boolean isChecked);
    }

    public UICheckbox() {
        this(null);
    }

    public UICheckbox(String text) {
        this.text = text;
        this.textColor = UIColor.WHITE;
        this.fontSize = 16;
        this.checked = false;
        this.isRadio = false;
        this.state = CheckState.UNCHECKED;
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

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
        this.state = checked ? CheckState.CHECKED : CheckState.UNCHECKED;
        if (listener != null) {
            listener.onChanged(this, checked);
        }
    }

    public boolean isRadio() {
        return isRadio;
    }

    public void setRadio(boolean radio) {
        isRadio = radio;
    }

    public CheckState getState() {
        return state;
    }

    public void setState(CheckState state) {
        this.state = state;
        this.checked = state == CheckState.CHECKED;
        if (listener != null) {
            listener.onChanged(this, checked);
        }
    }

    public ChangeListener getListener() {
        return listener;
    }

    public void setListener(ChangeListener listener) {
        this.listener = listener;
    }

    /**
     * 切换选中状态
     */
    public void toggle() {
        setChecked(!checked);
    }

    /**
     * 设置为不确定状态（仅用于非单选模式）
     */
    public void setIndeterminate(boolean indeterminate) {
        if (indeterminate && !isRadio) {
            this.state = CheckState.INDETERMINATE;
            this.checked = false;
        } else {
            this.state = this.checked ? CheckState.CHECKED : CheckState.UNCHECKED;
        }
    }

    @Override
    public void onClick() {
        toggle();
    }

    @Override
    public void render() {
        if (!isVisible()) {
            return;
        }

        // 渲染复选框/单选按钮
        renderCheckbox();

        // 渲染文本
        renderText();
    }

    /**
     * 渲染复选框/单选按钮
     */
    protected void renderCheckbox() {
        // 实际渲染由游戏引擎实现
        // 根据 isRadio 决定渲染为复选框还是单选按钮
        // 根据 checked/state 决定渲染状态
    }

    /**
     * 渲染文本
     */
    protected void renderText() {
        // 实际渲染由游戏引擎实现
    }
}
