package io.github.s3s3l.yggdrasil.game.core.ui.animation;

import io.github.s3s3l.yggdrasil.game.core.ui.UIComponent;

/**
 * 基础动画实现类
 * 提供动画的通用功能和状态管理
 */
public abstract class BaseAnimation implements UIAnimation {

    protected UIComponent target;
    protected float duration;
    protected float elapsed;
    protected boolean loop;
    protected boolean autoReverse;
    protected boolean reversing;
    protected boolean finished;
    protected UIEasing easing;
    protected Runnable onFinishListener;

    protected BaseAnimation() {
        this.duration = 1f;
        this.elapsed = 0f;
        this.loop = false;
        this.autoReverse = false;
        this.reversing = false;
        this.finished = false;
        this.easing = UIEasing.LINEAR;
    }

    @Override
    public void start(UIComponent target) {
        this.target = target;
        this.elapsed = 0f;
        this.finished = false;
        this.reversing = false;
    }

    @Override
    public boolean update(float delta) {
        if (finished) {
            return false;
        }

        elapsed += delta;

        float progress;
        if (elapsed >= duration) {
            if (autoReverse && !reversing) {
                reversing = true;
                elapsed = 0f;
                progress = 1f;
                apply(easing.apply(progress));
                return true;
            } else if (loop) {
                elapsed = elapsed % duration;
                progress = elapsed / duration;
                apply(easing.apply(progress));
                return true;
            } else {
                progress = 1f;
                apply(easing.apply(progress));
                finished = true;
                if (onFinishListener != null) {
                    onFinishListener.run();
                }
                return false;
            }
        } else {
            progress = elapsed / duration;
            if (reversing) {
                progress = 1f - progress;
            }
            apply(easing.apply(progress));
            return true;
        }
    }

    @Override
    public void end() {
        finished = true;
        if (target != null && easing != null) {
            apply(reversing ? 0f : 1f);
        }
        if (onFinishListener != null) {
            onFinishListener.run();
        }
    }

    @Override
    public void reset() {
        elapsed = 0f;
        finished = false;
        reversing = false;
        if (target != null) {
            apply(easing.apply(0f));
        }
    }

    @Override
    public float getDuration() {
        return duration;
    }

    @Override
    public void setDuration(float duration) {
        this.duration = duration;
    }

    @Override
    public boolean isFinished() {
        return finished;
    }

    @Override
    public void setOnFinishListener(Runnable listener) {
        this.onFinishListener = listener;
    }

    @Override
    public float getProgress() {
        return Math.min(elapsed / duration, 1f);
    }

    @Override
    public UIEasing getEasing() {
        return easing;
    }

    @Override
    public void setEasing(UIEasing easing) {
        this.easing = easing;
    }

    @Override
    public void setLoop(boolean loop) {
        this.loop = loop;
    }

    @Override
    public boolean isLoop() {
        return loop;
    }

    @Override
    public void setAutoReverse(boolean autoReverse) {
        this.autoReverse = autoReverse;
    }

    @Override
    public boolean isAutoReverse() {
        return autoReverse;
    }

    /**
     * 检查动画是否正在反向播放
     *
     * @return true 如果动画正在反向播放
     */
    public boolean isReversing() {
        return reversing;
    }

    /**
     * 应用动画效果
     * 由子类实现具体的动画逻辑
     *
     * @param alpha 动画进度 (0.0 - 1.0)
     */
    protected abstract void apply(float alpha);

    /**
     * 获取目标组件
     *
     * @return 目标组件
     */
    protected UIComponent getTarget() {
        return target;
    }
}
