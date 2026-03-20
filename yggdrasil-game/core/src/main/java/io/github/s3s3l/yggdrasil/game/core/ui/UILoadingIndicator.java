package io.github.s3s3l.yggdrasil.game.core.ui;

/**
 * UI 加载指示器组件
 * 加载进度动画，支持旋转和脉冲效果
 */
public class UILoadingIndicator extends UIComponent {

    private LoadingStyle style;               // 加载样式
    private UIColor color;                    // 颜色
    private float animationSpeed;             // 动画速度
    private float animationTimer;             // 动画计时器
    private boolean autoStart;                // 自动开始动画
    private boolean animating;                // 是否正在动画
    private int segmentCount;                 // 分段数量（用于环形进度）
    private float strokeWidth;                // 线条宽度
    private String loadingText;               // 加载文本
    private UIColor textColor;                // 文本颜色
    private float fontSize;                   // 字体大小

    /**
     * 加载样式枚举
     */
    public enum LoadingStyle {
        /** 旋转圆环 */
        SPINNER,
        /** 脉冲圆点 */
        PULSE,
        /** 进度条 */
        PROGRESS_BAR,
        /** 分段圆环 */
        SEGMENTED_RING
    }

    public UILoadingIndicator() {
        this(LoadingStyle.SPINNER);
    }

    public UILoadingIndicator(LoadingStyle style) {
        this.style = style;
        this.color = UIColor.BLUE;
        this.animationSpeed = 1.0f;
        this.animationTimer = 0;
        this.autoStart = true;
        this.animating = autoStart;
        this.segmentCount = 12;
        this.strokeWidth = 4;
        this.loadingText = null;
        this.textColor = UIColor.WHITE;
        this.fontSize = 14;
    }

    public LoadingStyle getLoadingStyle() {
        return style;
    }

    public void setLoadingStyle(LoadingStyle style) {
        this.style = style;
    }

    public UIColor getColor() {
        return color;
    }

    public void setColor(UIColor color) {
        this.color = color;
    }

    public float getAnimationSpeed() {
        return animationSpeed;
    }

    public void setAnimationSpeed(float animationSpeed) {
        this.animationSpeed = animationSpeed;
    }

    public boolean isAutoStart() {
        return autoStart;
    }

    public void setAutoStart(boolean autoStart) {
        this.autoStart = autoStart;
        this.animating = autoStart;
    }

    public boolean isAnimating() {
        return animating;
    }

    public float getAnimationTimer() {
        return animationTimer;
    }

    public int getSegmentCount() {
        return segmentCount;
    }

    public void setSegmentCount(int segmentCount) {
        this.segmentCount = segmentCount;
    }

    public float getStrokeWidth() {
        return strokeWidth;
    }

    public void setStrokeWidth(float strokeWidth) {
        this.strokeWidth = strokeWidth;
    }

    public String getLoadingText() {
        return loadingText;
    }

    public void setLoadingText(String loadingText) {
        this.loadingText = loadingText;
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

    /**
     * 开始动画
     */
    public void start() {
        animating = true;
    }

    /**
     * 停止动画
     */
    public void stop() {
        animating = false;
        animationTimer = 0;
    }

    /**
     * 切换动画状态
     */
    public void toggle() {
        animating = !animating;
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        if (animating) {
            animationTimer += deltaTime * animationSpeed;
            // 重置计时器，保持在 0-1 范围内
            if (animationTimer > 1) {
                animationTimer -= 1;
            }
        }
    }

    @Override
    public void render() {
        if (!isVisible()) {
            return;
        }

        switch (style) {
            case SPINNER:
                renderSpinner();
                break;
            case PULSE:
                renderPulse();
                break;
            case PROGRESS_BAR:
                renderProgressBar();
                break;
            case SEGMENTED_RING:
                renderSegmentedRing();
                break;
        }

        // 渲染文本
        if (loadingText != null) {
            renderText();
        }
    }

    /**
     * 渲染旋转圆环
     */
    protected void renderSpinner() {
        // 实际渲染由游戏引擎实现
        // 使用 animationTimer 控制旋转角度
    }

    /**
     * 渲染脉冲圆点
     */
    protected void renderPulse() {
        // 实际渲染由游戏引擎实现
        // 使用 animationTimer 控制缩放和透明度
    }

    /**
     * 渲染进度条
     */
    protected void renderProgressBar() {
        // 实际渲染由游戏引擎实现
        // 使用 animationTimer 控制进度
    }

    /**
     * 渲染分段圆环
     */
    protected void renderSegmentedRing() {
        // 实际渲染由游戏引擎实现
        // 根据 segmentCount 和 animationTimer 渲染分段
    }

    /**
     * 渲染加载文本
     */
    protected void renderText() {
        // 实际渲染由游戏引擎实现
    }

    /**
     * 获取当前旋转角度（0-360）
     *
     * @return 旋转角度
     */
    public float getRotationAngle() {
        return animationTimer * 360;
    }

    /**
     * 获取当前脉冲缩放比例 (0.5-1.5)
     *
     * @return 缩放比例
     */
    public float getPulseScale() {
        // 使用正弦波实现脉冲效果
        return 1.0f + 0.5f * (float) Math.sin(animationTimer * Math.PI * 2);
    }

    /**
     * 获取当前脉冲透明度 (0.3-1.0)
     *
     * @return 透明度
     */
    public float getPulseAlpha() {
        // 使用正弦波实现透明度脉冲
        return 0.65f + 0.35f * (float) Math.sin(animationTimer * Math.PI * 2);
    }

    /**
     * 获取当前进度条进度 (0-1)
     *
     * @return 进度
     */
    public float getProgress() {
        return animationTimer;
    }
}
