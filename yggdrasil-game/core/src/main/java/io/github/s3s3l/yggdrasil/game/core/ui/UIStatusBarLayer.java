package io.github.s3s3l.yggdrasil.game.core.ui;

import java.util.ArrayList;
import java.util.List;

/**
 * UI 状态条层
 * 用于多层进度显示（如 HP/MP/XP）
 */
public class UIStatusBarLayer {

    private String id;                               // 层标识
    private UIColor color;                           // 颜色
    private UIColor highlightColor;                  // 高亮颜色
    private float value;                             // 当前值
    private float maxValue;                          // 最大值
    private boolean animated;                        // 是否启用动画
    private float animationSpeed;                    // 动画速度
    private float smoothedValue;                     // 平滑后的值（用于动画）
    private boolean visible;                         // 是否可见
    private int renderOrder;                         // 渲染顺序

    public UIStatusBarLayer() {
        this("");
    }

    public UIStatusBarLayer(String id) {
        this(id, UIColor.GREEN, 100, 100);
    }

    public UIStatusBarLayer(String id, UIColor color, float value, float maxValue) {
        this.id = id != null ? id : "";
        this.color = color != null ? color : UIColor.GREEN;
        this.highlightColor = color != null ? color.lighten(0.1f) : UIColor.GREEN;
        this.value = Math.max(0, value);
        this.maxValue = Math.max(1, maxValue);
        this.animated = true;
        this.animationSpeed = 5f;
        this.smoothedValue = this.value;
        this.visible = true;
        this.renderOrder = 0;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id != null ? id : "";
    }

    public UIColor getColor() {
        return color;
    }

    public void setColor(UIColor color) {
        this.color = color != null ? color : UIColor.GREEN;
        this.highlightColor = this.color.lighten(0.1f);
    }

    public UIColor getHighlightColor() {
        return highlightColor;
    }

    public void setHighlightColor(UIColor highlightColor) {
        this.highlightColor = highlightColor != null ? highlightColor : this.color;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        float oldValue = this.value;
        this.value = Math.max(0, Math.min(maxValue, value));
        if (animated && oldValue != this.value) {
            // 动画过渡在 update 中处理
        } else {
            smoothedValue = this.value;
        }
    }

    public float getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(float maxValue) {
        this.maxValue = Math.max(1, maxValue);
        if (value > this.maxValue) {
            setValue(this.maxValue);
        }
    }

    public boolean isAnimated() {
        return animated;
    }

    public void setAnimated(boolean animated) {
        this.animated = animated;
        if (!animated) {
            smoothedValue = value;
        }
    }

    public float getAnimationSpeed() {
        return animationSpeed;
    }

    public void setAnimationSpeed(float animationSpeed) {
        this.animationSpeed = Math.max(0.1f, animationSpeed);
    }

    public float getSmoothedValue() {
        return smoothedValue;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public int getRenderOrder() {
        return renderOrder;
    }

    public void setRenderOrder(int renderOrder) {
        this.renderOrder = renderOrder;
    }

    /**
     * 获取进度百分比 (0-1)
     *
     * @return 进度百分比
     */
    public float getProgressPercent() {
        if (maxValue <= 0) {
            return 0;
        }
        return Math.max(0, Math.min(1, value / maxValue));
    }

    /**
     * 获取平滑后的进度百分比 (0-1)
     *
     * @return 平滑后的进度百分比
     */
    public float getSmoothedProgressPercent() {
        if (maxValue <= 0) {
            return 0;
        }
        return Math.max(0, Math.min(1, smoothedValue / maxValue));
    }

    /**
     * 更新动画（每帧调用）
     *
     * @param deltaTime 时间增量
     */
    public void update(float deltaTime) {
        if (animated && smoothedValue != value) {
            // 平滑过渡到目标值
            float diff = value - smoothedValue;
            float change = diff * animationSpeed * deltaTime;

            // 防止过冲
            if (Math.abs(change) > Math.abs(diff)) {
                smoothedValue = value;
            } else {
                smoothedValue += change;
            }
        }
    }

    /**
     * 增加数值
     *
     * @param delta 变化量
     */
    public void addValue(float delta) {
        setValue(value + delta);
    }

    /**
     * 减少数值
     *
     * @param delta 变化量
     */
    public void subtractValue(float delta) {
        setValue(value - delta);
    }

    /**
     * 设置百分比
     *
     * @param percent 百分比 (0-1)
     */
    public void setProgressPercent(float percent) {
        setValue(maxValue * Math.max(0, Math.min(1, percent)));
    }

    /**
     * 检查是否为空值
     *
     * @return true 如果值为 0
     */
    public boolean isEmpty() {
        return value <= 0;
    }

    /**
     * 检查是否已满
     *
     * @return true 如果值等于最大值
     */
    public boolean isFull() {
        return value >= maxValue;
    }

    /**
     * 检查是否低于阈值
     *
     * @param threshold 阈值百分比 (0-1)
     * @return true 如果低于阈值
     */
    public boolean isBelowThreshold(float threshold) {
        return getProgressPercent() < threshold;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UIStatusBarLayer that = (UIStatusBarLayer) o;
        return id != null && id.equals(that.id);
    }
}
