package io.github.s3s3l.yggdrasil.game.core.ui;

import java.util.Objects;

/**
 * UI 颜色
 */
public class UIColor {

    private float r;
    private float g;
    private float b;
    private float a;

    public UIColor() {
        this(1, 1, 1, 1);
    }

    public UIColor(float r, float g, float b, float a) {
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;
    }

    public UIColor(float r, float g, float b) {
        this(r, g, b, 1);
    }

    public UIColor(UIColor other) {
        this.r = other.r;
        this.g = other.g;
        this.b = other.b;
        this.a = other.a;
    }

    /**
     * 从 0-255 RGB 值创建颜色
     */
    public static UIColor fromRGB(int r, int g, int b) {
        return new UIColor(r / 255f, g / 255f, b / 255f, 1);
    }

    /**
     * 从 0-255 RGBA 值创建颜色
     */
    public static UIColor fromRGBA(int r, int g, int b, int a) {
        return new UIColor(r / 255f, g / 255f, b / 255f, a / 255f);
    }

    /**
     * 从十六进制颜色创建（如 #FF0000）
     */
    public static UIColor fromHex(String hex) {
        if (hex.startsWith("#")) {
            hex = hex.substring(1);
        }
        if (hex.length() == 6) {
            return fromRGB(
                Integer.parseInt(hex.substring(0, 2), 16),
                Integer.parseInt(hex.substring(2, 4), 16),
                Integer.parseInt(hex.substring(4, 6), 16)
            );
        } else if (hex.length() == 8) {
            return fromRGBA(
                Integer.parseInt(hex.substring(0, 2), 16),
                Integer.parseInt(hex.substring(2, 4), 16),
                Integer.parseInt(hex.substring(4, 6), 16),
                Integer.parseInt(hex.substring(6, 8), 16)
            );
        }
        throw new IllegalArgumentException("Invalid hex color: " + hex);
    }

    public float getR() {
        return r;
    }

    public void setR(float r) {
        this.r = r;
    }

    public float getG() {
        return g;
    }

    public void setG(float g) {
        this.g = g;
    }

    public float getB() {
        return b;
    }

    public void setB(float b) {
        this.b = b;
    }

    public float getA() {
        return a;
    }

    public void setA(float a) {
        this.a = a;
    }

    /**
     * 设置颜色
     *
     * @param r 红色分量 (0-1)
     * @param g 绿色分量 (0-1)
     * @param b 蓝色分量 (0-1)
     * @param a 透明度分量 (0-1)
     */
    public void set(float r, float g, float b, float a) {
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;
    }

    /**
     * 从另一个颜色复制
     */
    public void set(UIColor other) {
        this.r = other.r;
        this.g = other.g;
        this.b = other.b;
        this.a = other.a;
    }

    /**
     * 转换为十六进制字符串
     */
    public String toHex() {
        return String.format("#%02X%02X%02X%02X",
            (int) (r * 255),
            (int) (g * 255),
            (int) (b * 255),
            (int) (a * 255)
        );
    }

    /**
     * 转换为 RGB 字符串
     */
    public String toRGBString() {
        return String.format("rgb(%d, %d, %d)",
            (int) (r * 255),
            (int) (g * 255),
            (int) (b * 255)
        );
    }

    /**
     * 转换为 RGBA 字符串
     */
    public String toRGBAString() {
        return String.format("rgba(%d, %d, %d, %.2f)",
            (int) (r * 255),
            (int) (g * 255),
            (int) (b * 255),
            a
        );
    }

    /**
     * 混合两个颜色
     *
     * @param other 另一个颜色
     * @param factor 混合因子 (0-1)，0 表示完全使用当前颜色，1 表示完全使用另一个颜色
     * @return 混合后的颜色
     */
    public UIColor lerp(UIColor other, float factor) {
        return new UIColor(
            r + (other.r - r) * factor,
            g + (other.g - g) * factor,
            b + (other.b - b) * factor,
            a + (other.a - a) * factor
        );
    }

    /**
     * 使颜色变亮
     *
     * @param factor 变亮因子 (0-1)
     * @return 变亮后的颜色
     */
    public UIColor lighten(float factor) {
        return new UIColor(
            Math.min(1, r + factor),
            Math.min(1, g + factor),
            Math.min(1, b + factor),
            a
        );
    }

    /**
     * 使颜色变暗
     *
     * @param factor 变暗因子 (0-1)
     * @return 变暗后的颜色
     */
    public UIColor darken(float factor) {
        return new UIColor(
            Math.max(0, r - factor),
            Math.max(0, g - factor),
            Math.max(0, b - factor),
            a
        );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UIColor uiColor = (UIColor) o;
        return Float.compare(uiColor.r, r) == 0 &&
               Float.compare(uiColor.g, g) == 0 &&
               Float.compare(uiColor.b, b) == 0 &&
               Float.compare(uiColor.a, a) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(r, g, b, a);
    }

    @Override
    public String toString() {
        return "UIColor{" +
                "r=" + r +
                ", g=" + g +
                ", b=" + b +
                ", a=" + a +
                '}';
    }

    // 预定义颜色
    public static final UIColor WHITE = new UIColor(1, 1, 1, 1);
    public static final UIColor BLACK = new UIColor(0, 0, 0, 1);
    public static final UIColor RED = new UIColor(1, 0, 0, 1);
    public static final UIColor GREEN = new UIColor(0, 1, 0, 1);
    public static final UIColor BLUE = new UIColor(0, 0, 1, 1);
    public static final UIColor YELLOW = new UIColor(1, 1, 0, 1);
    public static final UIColor CYAN = new UIColor(0, 1, 1, 1);
    public static final UIColor MAGENTA = new UIColor(1, 0, 1, 1);
    public static final UIColor GRAY = new UIColor(0.5f, 0.5f, 0.5f, 1);
    public static final UIColor TRANSPARENT = new UIColor(0, 0, 0, 0);
}
