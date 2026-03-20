package io.github.s3s3l.yggdrasil.game.core.ui;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * UI 状态条组件
 * 多层进度显示（如 HP/MP/XP），支持文本叠加显示，支持动画过渡，支持阈值警告
 */
public class UIStatusBar extends UIComponent {

    private List<UIStatusBarLayer> layers;           // 图层列表
    private String displayText;                      // 显示文本
    private boolean showText;                        // 是否显示文本
    private UIColor backgroundColor;                 // 背景颜色
    private UIColor borderColor;                     // 边框颜色
    private UIColor textColor;                       // 文本颜色
    private float borderWidth;                       // 边框宽度
    private float cornerRadius;                      // 圆角半径
    private TextPosition textPosition;               // 文本位置
    private boolean showBorder;                      // 显示边框
    private StatusBarListener listener;              // 状态条监听器
    private List<ThresholdWarning> thresholdWarnings; // 阈值警告列表
    private boolean warningsEnabled;                 // 启用警告

    /**
     * 文本位置枚举
     */
    public enum TextPosition {
        CENTER,      // 居中
        LEFT,        // 左对齐
        RIGHT,       // 右对齐
        OUTSIDE      // 外部（右侧）
    }

    /**
     * 阈值警告
     */
    public static class ThresholdWarning {
        public String layerId;
        public float threshold;
        public UIColor warningColor;
        public Runnable onThresholdReached;

        public ThresholdWarning(String layerId, float threshold, UIColor warningColor) {
            this.layerId = layerId;
            this.threshold = threshold;
            this.warningColor = warningColor;
        }
    }

    /**
     * 状态条监听器接口
     */
    public interface StatusBarListener {
        /**
         * 当状态条数值改变时调用
         *
         * @param statusBar 状态条
         * @param layer 改变的层
         * @param oldValue 旧值
         * @param newValue 新值
         */
        void onLayerValueChanged(UIStatusBar statusBar, UIStatusBarLayer layer, float oldValue, float newValue);

        /**
         * 当达到阈值警告时调用
         *
         * @param statusBar 状态条
         * @param layer 触发的层
         * @param threshold 阈值
         */
        void onThresholdWarning(UIStatusBar statusBar, UIStatusBarLayer layer, float threshold);
    }

    public UIStatusBar() {
        this(new ArrayList<>());
    }

    public UIStatusBar(List<UIStatusBarLayer> layers) {
        this.layers = new ArrayList<>();
        this.displayText = "";
        this.showText = true;
        this.backgroundColor = new UIColor(0.2f, 0.2f, 0.2f, 1f);
        this.borderColor = UIColor.WHITE;
        this.textColor = UIColor.WHITE;
        this.borderWidth = 1;
        this.cornerRadius = 3;
        this.textPosition = TextPosition.CENTER;
        this.showBorder = true;
        this.thresholdWarnings = new ArrayList<>();
        this.warningsEnabled = true;
        if (layers != null) {
            this.layers.addAll(layers);
        }
        sortLayersByRenderOrder();
    }

    public List<UIStatusBarLayer> getLayers() {
        return new ArrayList<>(layers);
    }

    public void setLayers(List<UIStatusBarLayer> layers) {
        this.layers = layers != null ? new ArrayList<>(layers) : new ArrayList<>();
        sortLayersByRenderOrder();
    }

    public void addLayer(UIStatusBarLayer layer) {
        if (layer != null) {
            layers.add(layer);
            sortLayersByRenderOrder();
        }
    }

    public void addLayer(int index, UIStatusBarLayer layer) {
        if (layer != null) {
            layers.add(index, layer);
        }
    }

    public void removeLayer(UIStatusBarLayer layer) {
        layers.remove(layer);
    }

    public void removeLayer(String id) {
        UIStatusBarLayer layer = getLayer(id);
        if (layer != null) {
            layers.remove(layer);
        }
    }

    public void clearLayers() {
        layers.clear();
    }

    public int getLayerCount() {
        return layers.size();
    }

    public UIStatusBarLayer getLayer(String id) {
        if (id == null) {
            return null;
        }
        for (UIStatusBarLayer layer : layers) {
            if (id.equals(layer.getId())) {
                return layer;
            }
        }
        return null;
    }

    public UIStatusBarLayer getLayer(int index) {
        if (index >= 0 && index < layers.size()) {
            return layers.get(index);
        }
        return null;
    }

    /**
     * 创建并添加层
     *
     * @param id 层标识
     * @param color 颜色
     * @param value 初始值
     * @param maxValue 最大值
     * @return 创建的层
     */
    public UIStatusBarLayer addLayer(String id, UIColor color, float value, float maxValue) {
        UIStatusBarLayer layer = new UIStatusBarLayer(id, color, value, maxValue);
        addLayer(layer);
        return layer;
    }

    /**
     * 设置层的值
     *
     * @param layerId 层标识
     * @param value 值
     */
    public void setLayerValue(String layerId, float value) {
        UIStatusBarLayer layer = getLayer(layerId);
        if (layer != null) {
            float oldValue = layer.getValue();
            layer.setValue(value);
            if (listener != null && oldValue != layer.getValue()) {
                listener.onLayerValueChanged(this, layer, oldValue, layer.getValue());
            }
            // 检查阈值警告
            checkThresholdWarnings(layer);
        }
    }

    /**
     * 设置层的百分比
     *
     * @param layerId 层标识
     * @param percent 百分比 (0-1)
     */
    public void setLayerPercent(String layerId, float percent) {
        UIStatusBarLayer layer = getLayer(layerId);
        if (layer != null) {
            layer.setProgressPercent(percent);
            checkThresholdWarnings(layer);
        }
    }

    /**
     * 增加层的值
     *
     * @param layerId 层标识
     * @param delta 变化量
     */
    public void addLayerValue(String layerId, float delta) {
        UIStatusBarLayer layer = getLayer(layerId);
        if (layer != null) {
            float oldValue = layer.getValue();
            layer.addValue(delta);
            if (listener != null && oldValue != layer.getValue()) {
                listener.onLayerValueChanged(this, layer, oldValue, layer.getValue());
            }
            checkThresholdWarnings(layer);
        }
    }

    public String getDisplayText() {
        return displayText;
    }

    public void setDisplayText(String displayText) {
        this.displayText = displayText != null ? displayText : "";
    }

    public boolean isShowText() {
        return showText;
    }

    public void setShowText(boolean showText) {
        this.showText = showText;
    }

    public UIColor getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(UIColor backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public UIColor getBorderColor() {
        return borderColor;
    }

    public void setBorderColor(UIColor borderColor) {
        this.borderColor = borderColor;
    }

    public UIColor getTextColor() {
        return textColor;
    }

    public void setTextColor(UIColor textColor) {
        this.textColor = textColor;
    }

    public float getBorderWidth() {
        return borderWidth;
    }

    public void setBorderWidth(float borderWidth) {
        this.borderWidth = Math.max(0, borderWidth);
    }

    public float getCornerRadius() {
        return cornerRadius;
    }

    public void setCornerRadius(float cornerRadius) {
        this.cornerRadius = Math.max(0, cornerRadius);
    }

    public TextPosition getTextPosition() {
        return textPosition;
    }

    public void setTextPosition(TextPosition textPosition) {
        this.textPosition = textPosition;
    }

    public boolean isShowBorder() {
        return showBorder;
    }

    public void setShowBorder(boolean showBorder) {
        this.showBorder = showBorder;
    }

    public StatusBarListener getListener() {
        return listener;
    }

    public void setListener(StatusBarListener listener) {
        this.listener = listener;
    }

    public List<ThresholdWarning> getThresholdWarnings() {
        return new ArrayList<>(thresholdWarnings);
    }

    public void setThresholdWarnings(List<ThresholdWarning> thresholdWarnings) {
        this.thresholdWarnings = thresholdWarnings != null ? new ArrayList<>(thresholdWarnings) : new ArrayList<>();
    }

    public void addThresholdWarning(String layerId, float threshold, UIColor warningColor) {
        ThresholdWarning warning = new ThresholdWarning(layerId, threshold, warningColor);
        thresholdWarnings.add(warning);
    }

    public void removeThresholdWarning(String layerId, float threshold) {
        thresholdWarnings.removeIf(w -> layerId.equals(w.layerId) && w.threshold == threshold);
    }

    public boolean isWarningsEnabled() {
        return warningsEnabled;
    }

    public void setWarningsEnabled(boolean warningsEnabled) {
        this.warningsEnabled = warningsEnabled;
    }

    /**
     * 对层按渲染顺序排序
     */
    private void sortLayersByRenderOrder() {
        layers.sort(Comparator.comparingInt(UIStatusBarLayer::getRenderOrder));
    }

    /**
     * 检查阈值警告
     */
    private void checkThresholdWarnings(UIStatusBarLayer layer) {
        if (!warningsEnabled || listener == null) {
            return;
        }

        float percent = layer.getProgressPercent();
        for (ThresholdWarning warning : thresholdWarnings) {
            if (layer.getId().equals(warning.layerId)) {
                // 检查是否刚好达到或低于阈值
                if (percent <= warning.threshold &&
                    (percent + 0.01f) > warning.threshold) { // 简单的「刚好达到」判断
                    listener.onThresholdWarning(this, layer, warning.threshold);
                    if (warning.onThresholdReached != null) {
                        warning.onThresholdReached.run();
                    }
                }
            }
        }
    }

    /**
     * 更新状态条（每帧调用）
     *
     * @param deltaTime 时间增量
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        // 更新所有层的动画
        for (UIStatusBarLayer layer : layers) {
            layer.update(deltaTime);
        }
    }

    /**
     * 获取组合显示文本
     *
     * @return 显示文本
     */
    public String getComputedDisplayText() {
        if (displayText != null && !displayText.isEmpty()) {
            return displayText;
        }

        // 默认显示第一层的值
        if (!layers.isEmpty()) {
            UIStatusBarLayer layer = layers.get(0);
            return String.format("%.0f / %.0f", layer.getValue(), layer.getMaxValue());
        }

        return "";
    }

    @Override
    public void render() {
        if (!isVisible()) {
            return;
        }

        // 渲染背景
        renderBackground();

        // 渲染边框
        if (showBorder) {
            renderBorder();
        }

        // 渲染各层进度条
        renderLayers();

        // 渲染文本
        if (showText) {
            renderText();
        }
    }

    /**
     * 渲染背景
     */
    protected void renderBackground() {
        // 实际渲染由游戏引擎实现
    }

    /**
     * 渲染边框
     */
    protected void renderBorder() {
        // 实际渲染由游戏引擎实现
    }

    /**
     * 渲染各层进度条
     */
    protected void renderLayers() {
        // 实际渲染由游戏引擎实现
    }

    /**
     * 渲染文本
     */
    protected void renderText() {
        // 实际渲染由游戏引擎实现
    }

    /**
     * 获取状态条的总进度（所有层的平均值）
     *
     * @return 总进度百分比
     */
    public float getTotalProgress() {
        if (layers.isEmpty()) {
            return 0;
        }
        float total = 0;
        for (UIStatusBarLayer layer : layers) {
            total += layer.getProgressPercent();
        }
        return total / layers.size();
    }

    /**
     * 检查是否有任何层处于警告状态
     *
     * @return true 如果有层处于警告状态
     */
    public boolean hasWarning() {
        if (!warningsEnabled) {
            return false;
        }
        for (UIStatusBarLayer layer : layers) {
            float percent = layer.getProgressPercent();
            for (ThresholdWarning warning : thresholdWarnings) {
                if (layer.getId().equals(warning.layerId) && percent <= warning.threshold) {
                    return true;
                }
            }
        }
        return false;
    }
}
