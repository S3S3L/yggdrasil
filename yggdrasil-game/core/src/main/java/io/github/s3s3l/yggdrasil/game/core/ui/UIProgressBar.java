package io.github.s3s3l.yggdrasil.game.core.ui;

/**
 * UI 进度条
 * 用于显示进度的组件
 */
public class UIProgressBar extends UIComponent {

    private float value;
    private float minValue;
    private float maxValue;
    private boolean horizontal;
    private UIColor fillColor;

    public UIProgressBar() {
        this(0, 0, 100);
    }

    public UIProgressBar(float value, float minValue, float maxValue) {
        this.value = value;
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.horizontal = true;
        this.fillColor = UIColor.GREEN;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = Math.max(minValue, Math.min(value, maxValue));
    }

    public float getMinValue() {
        return minValue;
    }

    public void setMinValue(float minValue) {
        this.minValue = minValue;
    }

    public float getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(float maxValue) {
        this.maxValue = maxValue;
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

    /**
     * 获取进度百分比
     *
     * @return 进度百分比 (0-1)
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
        setValue(minValue + (maxValue - minValue) * percent);
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
}
