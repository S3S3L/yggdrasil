package io.github.s3s3l.yggdrasil.game.core.ui;

/**
 * UI 数字选择器组件
 * 步进式数字选择（+/-按钮），支持直接输入数字，支持范围限制和步长
 */
public class UINumberPicker extends UIComponent {

    private double value;                          // 当前值
    private double minValue;                       // 最小值
    private double maxValue;                       // 最大值
    private double stepSize;                       // 步长
    private int decimalPlaces;                     // 小数位数
    private String suffix;                         // 后缀（如 "%", "kg"）
    private String prefix;                         // 前缀（如 "$", "€"）
    private UIColor textColor;                     // 文本颜色
    private UIColor buttonColor;                   // 按钮颜色
    private UIColor backgroundColor;               // 背景颜色
    private float fontSize;                        // 字体大小
    private boolean showButtons;                   // 显示按钮
    private boolean allowDirectInput;              // 允许直接输入
    private NumberChangeListener listener;         // 数值变化监听器
    private boolean dragging;                      // 是否正在拖拽

    /**
     * 数值变化监听器接口
     */
    public interface NumberChangeListener {
        /**
         * 当数值改变时调用
         *
         * @param picker 数字选择器
         * @param oldValue 旧值
         * @param newValue 新值
         */
        void onValueChanged(UINumberPicker picker, double oldValue, double newValue);

        /**
         * 当拖拽开始时调用
         *
         * @param picker 数字选择器
         */
        void onDragStart(UINumberPicker picker);

        /**
         * 当拖拽结束时调用
         *
         * @param picker 数字选择器
         * @param value 最终值
         */
        void onDragEnd(UINumberPicker picker, double value);
    }

    public UINumberPicker() {
        this(0, 0, 100, 1);
    }

    public UINumberPicker(double value, double minValue, double maxValue, double stepSize) {
        this.value = clampValue(value, minValue, maxValue, stepSize);
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.stepSize = stepSize;
        this.decimalPlaces = 0;
        this.suffix = "";
        this.prefix = "";
        this.textColor = UIColor.WHITE;
        this.buttonColor = UIColor.GRAY;
        this.backgroundColor = new UIColor(0.2f, 0.2f, 0.2f, 1f);
        this.fontSize = 16;
        this.showButtons = true;
        this.allowDirectInput = true;
        this.dragging = false;
        // 根据步长自动计算小数位数
        if (stepSize > 0 && stepSize < 1) {
            this.decimalPlaces = (int) Math.ceil(-Math.log10(stepSize));
        }
    }

    /**
     * 钳制值到有效范围内
     */
    private double clampValue(double value, double min, double max, double step) {
        double clamped = Math.max(min, Math.min(value, max));
        if (step > 0) {
            clamped = Math.round(clamped / step) * step;
        }
        return clamped;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        double oldValue = this.value;
        double newValue = clampValue(value, minValue, maxValue, stepSize);
        this.value = newValue;
        if (listener != null && oldValue != this.value) {
            listener.onValueChanged(this, oldValue, this.value);
        }
    }

    public double getMinValue() {
        return minValue;
    }

    public void setMinValue(double minValue) {
        this.minValue = minValue;
        if (value < minValue) {
            setValue(minValue);
        }
    }

    public double getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(double maxValue) {
        this.maxValue = maxValue;
        if (value > maxValue) {
            setValue(maxValue);
        }
    }

    public double getStepSize() {
        return stepSize;
    }

    public void setStepSize(double stepSize) {
        this.stepSize = stepSize;
        if (stepSize > 0 && stepSize < 1) {
            this.decimalPlaces = (int) Math.ceil(-Math.log10(stepSize));
        }
    }

    public int getDecimalPlaces() {
        return decimalPlaces;
    }

    public void setDecimalPlaces(int decimalPlaces) {
        this.decimalPlaces = Math.max(0, decimalPlaces);
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix != null ? suffix : "";
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix != null ? prefix : "";
    }

    public UIColor getTextColor() {
        return textColor;
    }

    public void setTextColor(UIColor textColor) {
        this.textColor = textColor;
    }

    public UIColor getButtonColor() {
        return buttonColor;
    }

    public void setButtonColor(UIColor buttonColor) {
        this.buttonColor = buttonColor;
    }

    public UIColor getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(UIColor backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public float getFontSize() {
        return fontSize;
    }

    public void setFontSize(float fontSize) {
        this.fontSize = fontSize;
    }

    public boolean isShowButtons() {
        return showButtons;
    }

    public void setShowButtons(boolean showButtons) {
        this.showButtons = showButtons;
    }

    public boolean isAllowDirectInput() {
        return allowDirectInput;
    }

    public void setAllowDirectInput(boolean allowDirectInput) {
        this.allowDirectInput = allowDirectInput;
    }

    public NumberChangeListener getListener() {
        return listener;
    }

    public void setListener(NumberChangeListener listener) {
        this.listener = listener;
    }

    public boolean isDragging() {
        return dragging;
    }

    /**
     * 获取格式化后的显示文本
     *
     * @return 格式化文本
     */
    public String getDisplayText() {
        StringBuilder sb = new StringBuilder();
        if (prefix != null && !prefix.isEmpty()) {
            sb.append(prefix);
        }
        sb.append(formatValue(value));
        if (suffix != null && !suffix.isEmpty()) {
            sb.append(" ").append(suffix);
        }
        return sb.toString();
    }

    /**
     * 格式化数值
     */
    private String formatValue(double value) {
        if (decimalPlaces <= 0) {
            return String.valueOf((long) value);
        }
        return String.format("%." + decimalPlaces + "f", value);
    }

    /**
     * 增加一个步长
     */
    public void stepUp() {
        setValue(value + stepSize);
    }

    /**
     * 减少一个步长
     */
    public void stepDown() {
        setValue(value - stepSize);
    }

    /**
     * 处理拖拽开始
     *
     * @param x 鼠标 X 坐标
     * @param y 鼠标 Y 坐标
     */
    public void startDrag(float x, float y) {
        dragging = true;
        if (listener != null) {
            listener.onDragStart(this);
        }
    }

    /**
     * 处理拖拽中（上下拖动改变数值）
     *
     * @param x 鼠标 X 坐标
     * @param y 鼠标 Y 坐标
     * @param deltaY Y 方向变化量
     */
    public void drag(float x, float y, float deltaY) {
        if (dragging) {
            // 向上拖动增加，向下拖动减少
            double change = -deltaY * stepSize;
            setValue(value + change);
        }
    }

    /**
     * 处理拖拽结束
     *
     * @param x 鼠标 X 坐标
     * @param y 鼠标 Y 坐标
     */
    public void endDrag(float x, float y) {
        if (dragging) {
            dragging = false;
            if (listener != null) {
                listener.onDragEnd(this, value);
            }
        }
    }

    /**
     * 处理输入
     *
     * @param input 输入的文本
     */
    public void handleInput(String input) {
        if (!allowDirectInput || input == null || !isInteractable()) {
            return;
        }
        // 简单实现：直接尝试解析并设置值
        // 实际使用时可能需要更复杂的输入处理
        try {
            double newValue = Double.parseDouble(input);
            setValue(newValue);
        } catch (NumberFormatException e) {
            // 忽略无效输入
        }
    }

    /**
     * 处理 + 按钮点击
     */
    public void onPlusClick() {
        stepUp();
    }

    /**
     * 处理 - 按钮点击
     */
    public void onMinusClick() {
        stepDown();
    }

    @Override
    public void render() {
        if (!isVisible()) {
            return;
        }

        // 渲染背景
        renderBackground();

        // 渲染数值文本
        renderValueText();

        // 渲染按钮
        if (showButtons) {
            renderButtons();
        }
    }

    /**
     * 渲染背景
     */
    protected void renderBackground() {
        // 实际渲染由游戏引擎实现
    }

    /**
     * 渲染数值文本
     */
    protected void renderValueText() {
        // 实际渲染由游戏引擎实现
    }

    /**
     * 渲染按钮
     */
    protected void renderButtons() {
        // 实际渲染由游戏引擎实现
    }

    /**
     * 获取进度百分比 (0-1)
     *
     * @return 进度百分比
     */
    public float getProgressPercent() {
        if (maxValue == minValue) {
            return 0;
        }
        return (float) ((value - minValue) / (maxValue - minValue));
    }
}
