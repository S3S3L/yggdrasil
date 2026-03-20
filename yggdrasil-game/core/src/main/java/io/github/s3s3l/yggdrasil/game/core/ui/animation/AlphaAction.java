package io.github.s3s3l.yggdrasil.game.core.ui.animation;

import io.github.s3s3l.yggdrasil.game.core.ui.UIComponent;

/**
 * 透明度动画
 * 改变组件的透明度
 */
public class AlphaAction extends BaseAnimation {

    private float fromAlpha;
    private float toAlpha;

    public AlphaAction() {
        this(1f, 0f);
    }

    public AlphaAction(float fromAlpha, float toAlpha) {
        this.fromAlpha = fromAlpha;
        this.toAlpha = toAlpha;
    }

    /**
     * 创建淡出动画
     *
     * @param duration 持续时间
     * @return AlphaAction
     */
    public static AlphaAction fadeOut(float duration) {
        AlphaAction action = new AlphaAction(1f, 0f);
        action.setDuration(duration);
        return action;
    }

    /**
     * 创建淡入动画
     *
     * @param duration 持续时间
     * @return AlphaAction
     */
    public static AlphaAction fadeIn(float duration) {
        AlphaAction action = new AlphaAction(0f, 1f);
        action.setDuration(duration);
        return action;
    }

    /**
     * 创建淡入淡出动画
     *
     * @param duration 持续时间
     * @return AlphaAction
     */
    public static AlphaAction fadeInOut(float duration) {
        AlphaAction action = new AlphaAction(0f, 1f);
        action.setDuration(duration);
        action.setAutoReverse(true);
        return action;
    }

    @Override
    public void start(UIComponent target) {
        super.start(target);
        if (target.getBackgroundColor() != null) {
            this.fromAlpha = target.getBackgroundColor().getA();
        }
    }

    @Override
    protected void apply(float alpha) {
        if (target == null || target.getBackgroundColor() == null) {
            return;
        }

        float currentAlpha = fromAlpha + (toAlpha - fromAlpha) * alpha;
        target.getBackgroundColor().setA(currentAlpha);
    }

    public float getFromAlpha() {
        return fromAlpha;
    }

    public void setFromAlpha(float fromAlpha) {
        this.fromAlpha = fromAlpha;
    }

    public float getToAlpha() {
        return toAlpha;
    }

    public void setToAlpha(float toAlpha) {
        this.toAlpha = toAlpha;
    }
}
