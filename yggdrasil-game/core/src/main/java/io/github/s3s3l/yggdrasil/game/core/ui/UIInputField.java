package io.github.s3s3l.yggdrasil.game.core.ui;

/**
 * UI 输入框
 * 用于文本输入的组件
 */
public class UIInputField extends UIComponent {

    private String text;
    private String placeholder;
    private UIColor textColor;
    private UIColor placeholderColor;
    private float fontSize;
    private int maxLength;
    private boolean isPassword;
    private char passwordChar;
    private boolean focused;
    private InputListener listener;

    public UIInputField() {
        this("");
    }

    public UIInputField(String text) {
        this.text = text;
        this.placeholder = "";
        this.textColor = UIColor.WHITE;
        this.placeholderColor = UIColor.GRAY;
        this.fontSize = 16;
        this.maxLength = -1;
        this.isPassword = false;
        this.passwordChar = '*';
        this.focused = false;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        if (maxLength > 0 && text.length() > maxLength) {
            this.text = text.substring(0, maxLength);
        } else {
            this.text = text;
        }
    }

    public String getPlaceholder() {
        return placeholder;
    }

    public void setPlaceholder(String placeholder) {
        this.placeholder = placeholder;
    }

    public UIColor getTextColor() {
        return textColor;
    }

    public void setTextColor(UIColor textColor) {
        this.textColor = textColor;
    }

    public UIColor getPlaceholderColor() {
        return placeholderColor;
    }

    public void setPlaceholderColor(UIColor placeholderColor) {
        this.placeholderColor = placeholderColor;
    }

    public float getFontSize() {
        return fontSize;
    }

    public void setFontSize(float fontSize) {
        this.fontSize = fontSize;
    }

    public int getMaxLength() {
        return maxLength;
    }

    public void setMaxLength(int maxLength) {
        this.maxLength = maxLength;
    }

    public boolean isPassword() {
        return isPassword;
    }

    public void setPassword(boolean password) {
        isPassword = password;
    }

    public char getPasswordChar() {
        return passwordChar;
    }

    public void setPasswordChar(char passwordChar) {
        this.passwordChar = passwordChar;
    }

    public boolean isFocused() {
        return focused;
    }

    public void setFocused(boolean focused) {
        this.focused = focused;
        if (focused) {
            onFocus();
        } else {
            onBlur();
        }
    }

    public InputListener getListener() {
        return listener;
    }

    public void setListener(InputListener listener) {
        this.listener = listener;
    }

    /**
     * 处理文本输入
     *
     * @param input 输入的文本
     */
    public void handleInput(String input) {
        if (input == null || !isInteractable()) {
            return;
        }

        String newText = text + input;
        if (maxLength > 0 && newText.length() > maxLength) {
            newText = newText.substring(0, maxLength);
        }

        String oldText = text;
        text = newText;

        if (listener != null) {
            listener.onTextChanged(oldText, newText);
        }
    }

    /**
     * 处理删除
     */
    public void handleBackspace() {
        if (!isInteractable() || text.isEmpty()) {
            return;
        }

        String oldText = text;
        text = text.substring(0, text.length() - 1);

        if (listener != null) {
            listener.onTextChanged(oldText, text);
        }
    }

    /**
     * 获取显示文本（密码模式下返回掩码文本）
     *
     * @return 显示文本
     */
    public String getDisplayText() {
        if (isPassword) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < text.length(); i++) {
                sb.append(passwordChar);
            }
            return sb.toString();
        }
        return text;
    }

    @Override
    public void onFocus() {
        focused = true;
        // 实际使用时可以由子类重写添加焦点视觉效果
    }

    @Override
    public void onBlur() {
        focused = false;
        // 实际使用时可以由子类重写移除焦点视觉效果
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

        // 渲染光标
        if (focused) {
            renderCursor();
        }
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

    /**
     * 渲染光标
     */
    protected void renderCursor() {
        // 实际渲染由游戏引擎实现
    }

    /**
     * 输入监听器接口
     */
    public interface InputListener {
        /**
         * 文本变更时调用
         *
         * @param oldText 旧文本
         * @param newText 新文本
         */
        void onTextChanged(String oldText, String newText);
    }
}
