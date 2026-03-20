package io.github.s3s3l.yggdrasil.game.core.ui;

/**
 * UI 切换开关组件
 * 开/关切换按钮，带动画过渡
 */
public class UIToggle extends UIComponent {

    private boolean on;
    private String text;
    private UIColor textColor;
    private UIColor onColor;                  // 开启状态颜色
    private UIColor offColor;                 // 关闭状态颜色
    private UIColor knobColor;                // 滑块颜色
    private float animationDuration;          // 动画时长（秒）
    private float animationProgress;          // 动画进度 (0-1)
    private ToggleListener listener;          // 状态变化监听器

    /**
     * 状态变化监听器接口
     */
    public interface ToggleListener {
        /**
         * 当开关状态改变时调用
         *
         * @param toggle 切换开关组件
         * @param isOn 是否开启
         */
        void onToggled(UIToggle toggle, boolean isOn);
    }

    public UIToggle() {
        this(null, false);
    }

    public UIToggle(String text) {
        this(text, false);
    }

    public UIToggle(String text, boolean on) {
        this.text = text;
        this.on = on;
        this.textColor = UIColor.WHITE;
        this.onColor = UIColor.GREEN;
        this.offColor = new UIColor(0.3f, 0.3f, 0.3f, 1f);
        this.knobColor = UIColor.WHITE;
        this.animationDuration = 0.2f;
        this.animationProgress = on ? 1.0f : 0.0f;
    }

    public boolean isOn() {
        return on;
    }

    public void setOn(boolean on) {
        boolean changed = this.on != on;
        this.on = on;
        if (changed && listener != null) {
            listener.onToggled(this, on);
        }
    }

    public void toggle() {
        setOn(!on);
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

    public UIColor getOnColor() {
        return onColor;
    }

    public void setOnColor(UIColor onColor) {
        this.onColor = onColor;
    }

    public UIColor getOffColor() {
        return offColor;
    }

    public void setOffColor(UIColor offColor) {
        this.offColor = offColor;
    }

    public UIColor getKnobColor() {
        return knobColor;
    }

    public void setKnobColor(UIColor knobColor) {
        this.knobColor = knobColor;
    }

    public float getAnimationDuration() {
        return animationDuration;
    }

    public void setAnimationDuration(float animationDuration) {
        this.animationDuration = animationDuration;
    }

    public float getAnimationProgress() {
        return animationProgress;
    }

    public void setAnimationProgress(float animationProgress) {
        this.animationProgress = animationProgress;
    }

    public ToggleListener getListener() {
        return listener;
    }

    public void setListener(ToggleListener listener) {
        this.listener = listener;
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        // 更新动画进度
        float targetProgress = on ? 1.0f : 0.0f;
        if (animationProgress != targetProgress) {
            float delta = targetProgress - animationProgress;
            float step = deltaTime / animationDuration;
            if (Math.abs(delta) <= step) {
                animationProgress = targetProgress;
            } else {
                animationProgress += Math.signum(delta) * step;
            }
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

        // 渲染背景（轨道）
        renderTrack();

        // 渲染滑块（带动画）
        renderKnob();

        // 渲染文本
        renderText();
    }

    /**
     * 渲染背景轨道
     */
    protected void renderTrack() {
        // 实际渲染由游戏引擎实现
        // 根据 animationProgress 混合 onColor 和 offColor
    }

    /**
     * 渲染滑块
     */
    protected void renderKnob() {
        // 实际渲染由游戏引擎实现
        // 滑块位置根据 animationProgress 在轨道左右两侧之间移动
    }

    /**
     * 渲染文本
     */
    protected void renderText() {
        // 实际渲染由游戏引擎实现
    }

    /**
     * 获取滑块的位置（X 坐标）
     *
     * @return 滑块 X 坐标
     */
    public float getKnobX() {
        float trackWidth = getWidth() - getHeight(); // 减去滑块高度
        float startX = getX();
        return startX + animationProgress * trackWidth;
    }

    /**
     * 获取背景的当前颜色（混合后的颜色）
     *
     * @return 混合后的颜色
     */
    public UIColor getCurrentColor() {
        return offColor.lerp(onColor, animationProgress);
    }
}
