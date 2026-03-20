package io.github.s3s3l.yggdrasil.game.core.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * UI 颜色选择器组件
 * 支持 RGB/HSV 模式切换，支持色相、饱和度、亮度调节，支持 Alpha 通道，支持预设颜色
 */
public class UIColorPicker extends UIComponent {

    private UIColor selectedColor;                   // 选中的颜色
    private ColorMode mode;                          // RGB/HSV 模式
    private float hue;                               // 色相 (0-360)
    private float saturation;                        // 饱和度 (0-1)
    private float value;                             // 亮度 (0-1)
    private float alpha;                             // 透明度 (0-1)
    private List<UIColor> presetColors;              // 预设颜色
    private ColorChangeListener listener;            // 颜色变化监听器
    private boolean showAlpha;                       // 显示 Alpha 通道
    private boolean showColorPreview;                // 显示颜色预览
    private UIColor sliderColor;                     // 滑块颜色

    /**
     * 颜色模式枚举
     */
    public enum ColorMode {
        RGB,
        HSV
    }

    /**
     * 颜色变化监听器接口
     */
    public interface ColorChangeListener {
        /**
         * 当颜色改变时调用
         *
         * @param picker 颜色选择器
         * @param oldColor 旧颜色
         * @param newColor 新颜色
         */
        void onColorChanged(UIColorPicker picker, UIColor oldColor, UIColor newColor);
    }

    public UIColorPicker() {
        this(UIColor.WHITE);
    }

    public UIColorPicker(UIColor initialColor) {
        this.selectedColor = initialColor != null ? initialColor : UIColor.WHITE;
        this.mode = ColorMode.RGB;
        this.presetColors = new ArrayList<>();
        this.showAlpha = true;
        this.showColorPreview = true;
        this.sliderColor = UIColor.GRAY;
        this.alpha = initialColor != null ? initialColor.getA() : 1f;
        // 从初始颜色初始化 HSV 值
        updateHSVFromColor();
        // 添加默认预设颜色
        addDefaultPresetColors();
    }

    /**
     * 添加默认预设颜色
     */
    private void addDefaultPresetColors() {
        presetColors.add(UIColor.RED);
        presetColors.add(UIColor.GREEN);
        presetColors.add(UIColor.BLUE);
        presetColors.add(UIColor.YELLOW);
        presetColors.add(UIColor.CYAN);
        presetColors.add(UIColor.MAGENTA);
        presetColors.add(UIColor.WHITE);
        presetColors.add(UIColor.GRAY);
        presetColors.add(UIColor.BLACK);
    }

    public UIColor getSelectedColor() {
        return selectedColor;
    }

    public void setSelectedColor(UIColor selectedColor) {
        if (selectedColor == null) {
            return;
        }
        UIColor oldColor = this.selectedColor;
        this.selectedColor = selectedColor;
        this.alpha = selectedColor.getA();
        if (mode == ColorMode.HSV) {
            updateHSVFromColor();
        } else {
            updateHSVFromColor();
        }
        notifyListener(oldColor, selectedColor);
    }

    /**
     * 从当前颜色更新 HSV 值
     */
    private void updateHSVFromColor() {
        float[] hsv = rgbToHsv(selectedColor.getR(), selectedColor.getG(), selectedColor.getB());
        this.hue = hsv[0];
        this.saturation = hsv[1];
        this.value = hsv[2];
    }

    /**
     * 从 HSV 值更新颜色
     */
    private void updateColorFromHSV() {
        float[] rgb = hsvToRgb(hue, saturation, value);
        this.selectedColor = new UIColor(rgb[0], rgb[1], rgb[2], alpha);
    }

    public ColorMode getMode() {
        return mode;
    }

    public void setMode(ColorMode mode) {
        this.mode = mode;
    }

    /**
     * 切换到 RGB 模式
     */
    public void setRGBMode() {
        this.mode = ColorMode.RGB;
    }

    /**
     * 切换到 HSV 模式
     */
    public void setHSVMode() {
        this.mode = ColorMode.HSV;
    }

    public float getHue() {
        return hue;
    }

    public void setHue(float hue) {
        float oldHue = this.hue;
        this.hue = (hue % 360 + 360) % 360; // 确保在 0-360 范围内
        if (mode == ColorMode.HSV || oldHue != this.hue) {
            updateColorFromHSV();
            notifyListener(selectedColor, selectedColor);
        }
    }

    public float getSaturation() {
        return saturation;
    }

    public void setSaturation(float saturation) {
        float oldSaturation = this.saturation;
        this.saturation = Math.max(0, Math.min(1, saturation));
        if (mode == ColorMode.HSV || oldSaturation != this.saturation) {
            updateColorFromHSV();
            notifyListener(selectedColor, selectedColor);
        }
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        float oldValue = this.value;
        this.value = Math.max(0, Math.min(1, value));
        if (mode == ColorMode.HSV || oldValue != this.value) {
            updateColorFromHSV();
            notifyListener(selectedColor, selectedColor);
        }
    }

    public float getAlpha() {
        return alpha;
    }

    public void setAlpha(float alpha) {
        float oldAlpha = this.alpha;
        this.alpha = Math.max(0, Math.min(1, alpha));
        if (showAlpha && oldAlpha != this.alpha) {
            selectedColor = new UIColor(selectedColor.getR(), selectedColor.getG(),
                    selectedColor.getB(), this.alpha);
            notifyListener(selectedColor, selectedColor);
        }
    }

    public List<UIColor> getPresetColors() {
        return new ArrayList<>(presetColors);
    }

    public void setPresetColors(List<UIColor> presetColors) {
        this.presetColors = presetColors != null ? new ArrayList<>(presetColors) : new ArrayList<>();
    }

    public void addPresetColor(UIColor color) {
        if (color != null && !presetColors.contains(color)) {
            presetColors.add(color);
        }
    }

    public void removePresetColor(UIColor color) {
        presetColors.remove(color);
    }

    public void clearPresetColors() {
        presetColors.clear();
    }

    public boolean isShowAlpha() {
        return showAlpha;
    }

    public void setShowAlpha(boolean showAlpha) {
        this.showAlpha = showAlpha;
    }

    public boolean isShowColorPreview() {
        return showColorPreview;
    }

    public void setShowColorPreview(boolean showColorPreview) {
        this.showColorPreview = showColorPreview;
    }

    public UIColor getSliderColor() {
        return sliderColor;
    }

    public void setSliderColor(UIColor sliderColor) {
        this.sliderColor = sliderColor;
    }

    public ColorChangeListener getListener() {
        return listener;
    }

    public void setListener(ColorChangeListener listener) {
        this.listener = listener;
    }

    /**
     * 设置颜色（RGB 值）
     *
     * @param r 红色 (0-1)
     * @param g 绿色 (0-1)
     * @param b 蓝色 (0-1)
     */
    public void setColor(float r, float g, float b) {
        setColor(r, g, b, alpha);
    }

    /**
     * 设置颜色（RGB 值）
     *
     * @param r 红色 (0-1)
     * @param g 绿色 (0-1)
     * @param b 蓝色 (0-1)
     * @param a 透明度 (0-1)
     */
    public void setColor(float r, float g, float b, float a) {
        UIColor oldColor = selectedColor;
        r = Math.max(0, Math.min(1, r));
        g = Math.max(0, Math.min(1, g));
        b = Math.max(0, Math.min(1, b));
        a = Math.max(0, Math.min(1, a));
        this.selectedColor = new UIColor(r, g, b, a);
        this.alpha = a;
        updateHSVFromColor();
        notifyListener(oldColor, selectedColor);
    }

    /**
     * 设置颜色（UIColor）
     *
     * @param color 颜色
     */
    public void setColor(UIColor color) {
        setSelectedColor(color);
    }

    /**
     * 设置颜色（十六进制）
     *
     * @param hexColor 十六进制颜色（如 "#FF0000" 或 "FF0000FF"）
     */
    public void setColor(String hexColor) {
        if (hexColor == null) {
            return;
        }
        String hex = hexColor.startsWith("#") ? hexColor.substring(1) : hexColor;
        if (hex.length() == 6) {
            float r = Integer.parseInt(hex.substring(0, 2), 16) / 255f;
            float g = Integer.parseInt(hex.substring(2, 4), 16) / 255f;
            float b = Integer.parseInt(hex.substring(4, 6), 16) / 255f;
            setColor(r, g, b, alpha);
        } else if (hex.length() == 8) {
            float r = Integer.parseInt(hex.substring(0, 2), 16) / 255f;
            float g = Integer.parseInt(hex.substring(2, 4), 16) / 255f;
            float b = Integer.parseInt(hex.substring(4, 6), 16) / 255f;
            float a = Integer.parseInt(hex.substring(6, 8), 16) / 255f;
            setColor(r, g, b, a);
        }
    }

    /**
     * 选择预设颜色
     *
     * @param index 预设颜色索引
     */
    public void selectPresetColor(int index) {
        if (index >= 0 && index < presetColors.size()) {
            setSelectedColor(presetColors.get(index));
        }
    }

    /**
     * RGB 转 HSV
     */
    private float[] rgbToHsv(float r, float g, float b) {
        float max = Math.max(r, Math.max(g, b));
        float min = Math.min(r, Math.min(g, b));
        float delta = max - min;

        float h = 0;
        float s = max == 0 ? 0 : delta / max;
        float v = max;

        if (delta > 0) {
            if (max == r) {
                h = 60 * (((g - b) / delta) % 6);
            } else if (max == g) {
                h = 60 * (((b - r) / delta) + 2);
            } else {
                h = 60 * (((r - g) / delta) + 4);
            }
            if (h < 0) {
                h += 360;
            }
        }

        return new float[]{h, s, v};
    }

    /**
     * HSV 转 RGB
     */
    private float[] hsvToRgb(float h, float s, float v) {
        h = (h % 360 + 360) % 360;
        s = Math.max(0, Math.min(1, s));
        v = Math.max(0, Math.min(1, v));

        float c = v * s;
        float x = c * (1 - Math.abs((h / 60) % 2 - 1));
        float m = v - c;

        float r, g, b;
        if (h < 60) {
            r = c; g = x; b = 0;
        } else if (h < 120) {
            r = x; g = c; b = 0;
        } else if (h < 180) {
            r = 0; g = c; b = x;
        } else if (h < 240) {
            r = 0; g = x; b = c;
        } else if (h < 300) {
            r = x; g = 0; b = c;
        } else {
            r = c; g = 0; b = x;
        }

        return new float[]{r + m, g + m, b + m};
    }

    /**
     * 通知监听器
     */
    private void notifyListener(UIColor oldColor, UIColor newColor) {
        if (listener != null && !Objects.equals(oldColor, newColor)) {
            listener.onColorChanged(this, oldColor, newColor);
        }
    }

    @Override
    public void render() {
        if (!isVisible()) {
            return;
        }

        // 渲染颜色预览
        if (showColorPreview) {
            renderColorPreview();
        }

        // 渲染模式切换
        renderModeSelector();

        // 渲染滑块
        if (mode == ColorMode.RGB) {
            renderRGBSliders();
        } else {
            renderHSVSliders();
        }

        // 渲染预设颜色
        renderPresetColors();

        // 渲染 Alpha 滑块
        if (showAlpha) {
            renderAlphaSlider();
        }
    }

    /**
     * 渲染颜色预览
     */
    protected void renderColorPreview() {
        // 实际渲染由游戏引擎实现
    }

    /**
     * 渲染模式选择器
     */
    protected void renderModeSelector() {
        // 实际渲染由游戏引擎实现
    }

    /**
     * 渲染 RGB 滑块
     */
    protected void renderRGBSliders() {
        // 实际渲染由游戏引擎实现
    }

    /**
     * 渲染 HSV 滑块
     */
    protected void renderHSVSliders() {
        // 实际渲染由游戏引擎实现
    }

    /**
     * 渲染预设颜色
     */
    protected void renderPresetColors() {
        // 实际渲染由游戏引擎实现
    }

    /**
     * 渲染 Alpha 滑块
     */
    protected void renderAlphaSlider() {
        // 实际渲染由游戏引擎实现
    }

    /**
     * 处理色相滑块拖动
     *
     * @param value 滑块值 (0-1)
     */
    public void onHueSliderChanged(float value) {
        setHue(value * 360);
    }

    /**
     * 处理饱和度滑块拖动
     *
     * @param value 滑块值 (0-1)
     */
    public void onSaturationSliderChanged(float value) {
        setSaturation(value);
    }

    /**
     * 处理亮度滑块拖动
     *
     * @param value 滑块值 (0-1)
     */
    public void onValueSliderChanged(float value) {
        setValue(value);
    }

    /**
     * 处理红色滑块拖动
     *
     * @param value 滑块值 (0-1)
     */
    public void onRedSliderChanged(float value) {
        setColor(value, selectedColor.getG(), selectedColor.getB(), alpha);
    }

    /**
     * 处理绿色滑块拖动
     *
     * @param value 滑块值 (0-1)
     */
    public void onGreenSliderChanged(float value) {
        setColor(selectedColor.getR(), value, selectedColor.getB(), alpha);
    }

    /**
     * 处理蓝色滑块拖动
     *
     * @param value 滑块值 (0-1)
     */
    public void onBlueSliderChanged(float value) {
        setColor(selectedColor.getR(), selectedColor.getG(), value, alpha);
    }

    /**
     * 处理 Alpha 滑块拖动
     *
     * @param value 滑块值 (0-1)
     */
    public void onAlphaSliderChanged(float value) {
        setAlpha(value);
    }

    /**
     * 获取颜色的十六进制表示
     *
     * @return 十六进制颜色字符串（如 "#RRGGBB"）
     */
    public String getHexColor() {
        return String.format("#%02X%02X%02X",
                (int) (selectedColor.getR() * 255),
                (int) (selectedColor.getG() * 255),
                (int) (selectedColor.getB() * 255));
    }

    /**
     * 获取颜色的十六进制表示（包含 Alpha）
     *
     * @return 十六进制颜色字符串（如 "#RRGGBBAA"）
     */
    public String getHexColorWithAlpha() {
        return String.format("#%02X%02X%02X%02X",
                (int) (selectedColor.getR() * 255),
                (int) (selectedColor.getG() * 255),
                (int) (selectedColor.getB() * 255),
                (int) (alpha * 255));
    }
}
