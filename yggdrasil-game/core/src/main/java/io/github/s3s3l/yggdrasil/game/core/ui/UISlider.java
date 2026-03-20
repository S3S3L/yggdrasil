package io.github.s3s3l.yggdrasil.game.core.ui;

/**
 * UI 滑动条组件
 * 可拖动的进度条，支持范围选择
 */
public class UISlider extends UIComponent {

    private float value;
    private float minValue;
    private float maxValue;
    private float stepSize;                   // 步长（0 表示无限制）
    private boolean horizontal;               // 水平/垂直方向
    private UIColor fillColor;                // 填充颜色
    private UIColor backgroundColor;          // 背景颜色
    private UIColor knobColor;                // 滑块颜色
    private float knobSize;                   // 滑块大小
    private boolean showLabels;               // 显示最小/最大值标签
    private SliderListener listener;          // 值变化监听器
    private boolean dragging;                 // 是否正在拖拽

    /**
     * 值变化监听器接口
     */
    public interface SliderListener {
        /**
         * 当滑块值改变时调用
         *
         * @param slider 滑动条组件
         * @param newValue 新的值
         */
        void onValueChanged(UISlider slider, float newValue);

        /**
         * 当拖拽开始时调用
         *
         * @param slider 滑动条组件
         */
        void onDragStart(UISlider slider);

        /**
         * 当拖拽结束时调用
         *
         * @param slider 滑动条组件
         * @param newValue 最终的值
         */
        void onDragEnd(UISlider slider, float newValue);
    }

    public UISlider() {
        this(0, 0, 100);
    }

    public UISlider(float value, float minValue, float maxValue) {
        this.value = Math.max(minValue, Math.min(value, maxValue));
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.stepSize = 0;
        this.horizontal = true;
        this.fillColor = UIColor.BLUE;
        this.backgroundColor = new UIColor(0.2f, 0.2f, 0.2f, 1f);
        this.knobColor = UIColor.WHITE;
        this.knobSize = 20;
        this.showLabels = false;
        this.dragging = false;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        float clampedValue = Math.max(minValue, Math.min(value, maxValue));
        if (stepSize > 0) {
            clampedValue = Math.round(clampedValue / stepSize) * stepSize;
        }
        float oldValue = this.value;
        this.value = clampedValue;
        if (listener != null && oldValue != this.value) {
            listener.onValueChanged(this, this.value);
        }
    }

    public float getMinValue() {
        return minValue;
    }

    public void setMinValue(float minValue) {
        this.minValue = minValue;
        if (value < minValue) {
            setValue(minValue);
        }
    }

    public float getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(float maxValue) {
        this.maxValue = maxValue;
        if (value > maxValue) {
            setValue(maxValue);
        }
    }

    public float getStepSize() {
        return stepSize;
    }

    public void setStepSize(float stepSize) {
        this.stepSize = stepSize;
    }

    public boolean isHorizontal() {
        return horizontal;
    }

    public void setHorizontal(boolean horizontal) {
        this.horizontal = horizontal;
    }

    public UIColor getFillColor() {
        return fillColor;
    }

    public void setFillColor(UIColor fillColor) {
        this.fillColor = fillColor;
    }

    public UIColor getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(UIColor backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public UIColor getKnobColor() {
        return knobColor;
    }

    public void setKnobColor(UIColor knobColor) {
        this.knobColor = knobColor;
    }

    public float getKnobSize() {
        return knobSize;
    }

    public void setKnobSize(float knobSize) {
        this.knobSize = knobSize;
    }

    public boolean isShowLabels() {
        return showLabels;
    }

    public void setShowLabels(boolean showLabels) {
        this.showLabels = showLabels;
    }

    public SliderListener getListener() {
        return listener;
    }

    public void setListener(SliderListener listener) {
        this.listener = listener;
    }

    public boolean isDragging() {
        return dragging;
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
        return (value - minValue) / (maxValue - minValue);
    }

    /**
     * 设置进度百分比
     *
     * @param percent 进度百分比 (0-1)
     */
    public void setProgressPercent(float percent) {
        setValue(minValue + (maxValue - minValue) * Math.max(0, Math.min(1, percent)));
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
        updateValueFromPosition(x, y);
    }

    /**
     * 处理拖拽中
     *
     * @param x 鼠标 X 坐标
     * @param y 鼠标 Y 坐标
     */
    public void drag(float x, float y) {
        if (dragging) {
            updateValueFromPosition(x, y);
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
            updateValueFromPosition(x, y);
            if (listener != null) {
                listener.onDragEnd(this, value);
            }
        }
    }

    /**
     * 从位置更新值
     *
     * @param x 鼠标 X 坐标
     * @param y 鼠标 Y 坐标
     */
    private void updateValueFromPosition(float x, float y) {
        float range = maxValue - minValue;
        float percent;

        if (horizontal) {
            percent = (x - getX()) / getWidth();
        } else {
            percent = 1 - (y - getY()) / getHeight();
        }

        percent = Math.max(0, Math.min(1, percent));
        setValue(minValue + range * percent);
    }

    @Override
    public void render() {
        if (!isVisible()) {
            return;
        }

        // 渲染背景
        renderBackground();

        // 渲染填充部分
        renderFill();

        // 渲染滑块
        renderKnob();

        // 渲染标签
        if (showLabels) {
            renderLabels();
        }
    }

    /**
     * 渲染背景
     */
    protected void renderBackground() {
        // 实际渲染由游戏引擎实现
    }

    /**
     * 渲染填充部分
     */
    protected void renderFill() {
        // 实际渲染由游戏引擎实现
    }

    /**
     * 渲染滑块
     */
    protected void renderKnob() {
        // 实际渲染由游戏引擎实现
    }

    /**
     * 渲染最小/最大值标签
     */
    protected void renderLabels() {
        // 实际渲染由游戏引擎实现
    }

    /**
     * 获取滑块的位置（X 坐标或 Y 坐标，取决于方向）
     *
     * @return 滑块位置
     */
    public float getKnobPosition() {
        float percent = getProgressPercent();
        if (horizontal) {
            return getX() + percent * (getWidth() - knobSize);
        } else {
            return getY() + (1 - percent) * (getHeight() - knobSize);
        }
    }
}
