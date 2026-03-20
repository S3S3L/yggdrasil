package io.github.s3s3l.yggdrasil.game.core.ui.scene;

import io.github.s3s3l.yggdrasil.game.core.ui.UIColor;

/**
 * 淡入淡出过渡
 */
public class FadeTransition extends SceneTransition {

    private UIColor color;
    private float alpha;

    /**
     * 创建淡入淡出过渡
     *
     * @param duration 过渡时长
     */
    public FadeTransition(float duration) {
        this(duration, UIColor.BLACK);
    }

    /**
     * 创建淡入淡出过渡
     *
     * @param duration 过渡时长
     * @param color    过渡颜色
     */
    public FadeTransition(float duration, UIColor color) {
        super(duration);
        this.color = color;
        this.alpha = 0;
    }

    /**
     * 获取过渡颜色
     *
     * @return 颜色
     */
    public UIColor getColor() {
        return color;
    }

    /**
     * 设置过渡颜色
     *
     * @param color 颜色
     */
    public void setColor(UIColor color) {
        this.color = color;
    }

    /**
     * 获取当前透明度
     *
     * @return 透明度（0-1）
     */
    public float getAlpha() {
        return alpha;
    }

    @Override
    public void render() {
        // 根据透明度渲染颜色覆盖层
        // 实际渲染由 UIRenderer 处理
        float progress = getProgress();
        // 前半段淡出（不透明度增加），后半段淡入（不透明度减少）
        if (progress < 0.5f) {
            alpha = progress * 2; // 0 -> 1
        } else {
            alpha = (1 - progress) * 2; // 1 -> 0
        }
    }

    @Override
    protected void onBegin(UIScene fromScene, UIScene toScene) {
        alpha = 0;
    }

    @Override
    protected void onUpdate(float progress) {
        if (progress < 0.5f) {
            alpha = progress * 2;
        } else {
            alpha = (1 - progress) * 2;
        }
    }

    @Override
    protected void onFinish() {
        alpha = 0;
    }
}
