package io.github.s3s3l.yggdrasil.game.core.ui.animation;

import io.github.s3s3l.yggdrasil.game.core.ui.UIComponent;

/**
 * 尺寸动画
 * 改变组件的大小
 */
public class SizeToAction extends BaseAnimation {

    private float fromWidth;
    private float fromHeight;
    private float toWidth;
    private float toHeight;

    public SizeToAction() {
        this(0f, 0f, 0f, 0f);
    }

    public SizeToAction(float toWidth, float toHeight) {
        this.toWidth = toWidth;
        this.toHeight = toHeight;
    }

    public SizeToAction(float fromWidth, float fromHeight, float toWidth, float toHeight) {
        this.fromWidth = fromWidth;
        this.fromHeight = fromHeight;
        this.toWidth = toWidth;
        this.toHeight = toHeight;
    }

    /**
     * 创建尺寸动画
     *
     * @param duration  持续时间
     * @param toWidth   目标宽度
     * @param toHeight  目标高度
     * @return SizeToAction
     */
    public static SizeToAction sizeTo(float duration, float toWidth, float toHeight) {
        SizeToAction action = new SizeToAction(toWidth, toHeight);
        action.setDuration(duration);
        return action;
    }

    /**
     * 创建相对尺寸动画
     *
     * @param duration  持续时间
     * @param byWidth   宽度变化量
     * @param byHeight  高度变化量
     * @return SizeToAction
     */
    public static SizeToAction sizeBy(float duration, float byWidth, float byHeight) {
        SizeToAction action = new SizeToAction();
        action.setDuration(duration);
        action.setRelative(true);
        action.setToWidth(byWidth);
        action.setToHeight(byHeight);
        return action;
    }

    private boolean relative = false;

    @Override
    public void start(UIComponent target) {
        super.start(target);
        this.fromWidth = target.getWidth();
        this.fromHeight = target.getHeight();

        if (relative) {
            this.toWidth = fromWidth + toWidth;
            this.toHeight = fromHeight + toHeight;
        }
    }

    @Override
    protected void apply(float alpha) {
        if (target == null) {
            return;
        }

        float currentWidth = fromWidth + (toWidth - fromWidth) * alpha;
        float currentHeight = fromHeight + (toHeight - fromHeight) * alpha;
        target.setSize(currentWidth, currentHeight);
    }

    public float getFromWidth() {
        return fromWidth;
    }

    public void setFromWidth(float fromWidth) {
        this.fromWidth = fromWidth;
    }

    public float getFromHeight() {
        return fromHeight;
    }

    public void setFromHeight(float fromHeight) {
        this.fromHeight = fromHeight;
    }

    public float getToWidth() {
        return toWidth;
    }

    public void setToWidth(float toWidth) {
        this.toWidth = toWidth;
    }

    public float getToHeight() {
        return toHeight;
    }

    public void setToHeight(float toHeight) {
        this.toHeight = toHeight;
    }

    public boolean isRelative() {
        return relative;
    }

    public void setRelative(boolean relative) {
        this.relative = relative;
    }
}
