package io.github.s3s3l.yggdrasil.game.core.ui.animation;

import io.github.s3s3l.yggdrasil.game.core.ui.UIComponent;

/**
 * 移动动画
 * 改变组件的位置
 */
public class MoveToAction extends BaseAnimation {

    private float fromX;
    private float fromY;
    private float toX;
    private float toY;

    public MoveToAction() {
        this(0f, 0f, 0f, 0f);
    }

    public MoveToAction(float toX, float toY) {
        this.toX = toX;
        this.toY = toY;
    }

    public MoveToAction(float fromX, float fromY, float toX, float toY) {
        this.fromX = fromX;
        this.fromY = fromY;
        this.toX = toX;
        this.toY = toY;
    }

    /**
     * 创建移动动画
     *
     * @param duration 持续时间
     * @param toX      目标 X 坐标
     * @param toY      目标 Y 坐标
     * @return MoveToAction
     */
    public static MoveToAction moveTo(float duration, float toX, float toY) {
        MoveToAction action = new MoveToAction(toX, toY);
        action.setDuration(duration);
        return action;
    }

    /**
     * 创建相对移动动画
     *
     * @param duration 持续时间
     * @param byX      X 轴移动距离
     * @param byY      Y 轴移动距离
     * @return MoveToAction
     */
    public static MoveToAction moveBy(float duration, float byX, float byY) {
        MoveToAction action = new MoveToAction();
        action.setDuration(duration);
        action.setRelative(true);
        action.setToX(byX);
        action.setToY(byY);
        return action;
    }

    private boolean relative = false;

    @Override
    public void start(UIComponent target) {
        super.start(target);
        this.fromX = target.getX();
        this.fromY = target.getY();

        if (relative) {
            this.toX = fromX + toX;
            this.toY = fromY + toY;
        }
    }

    @Override
    protected void apply(float alpha) {
        if (target == null) {
            return;
        }

        float currentX = fromX + (toX - fromX) * alpha;
        float currentY = fromY + (toY - fromY) * alpha;
        target.setPosition(currentX, currentY);
    }

    public float getFromX() {
        return fromX;
    }

    public void setFromX(float fromX) {
        this.fromX = fromX;
    }

    public float getFromY() {
        return fromY;
    }

    public void setFromY(float fromY) {
        this.fromY = fromY;
    }

    public float getToX() {
        return toX;
    }

    public void setToX(float toX) {
        this.toX = toX;
    }

    public float getToY() {
        return toY;
    }

    public void setToY(float toY) {
        this.toY = toY;
    }

    public boolean isRelative() {
        return relative;
    }

    public void setRelative(boolean relative) {
        this.relative = relative;
    }
}
