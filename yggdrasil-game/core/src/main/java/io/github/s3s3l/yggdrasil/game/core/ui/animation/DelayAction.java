package io.github.s3s3l.yggdrasil.game.core.ui.animation;

import io.github.s3s3l.yggdrasil.game.core.ui.UIComponent;

/**
 * 延迟动画
 * 在执行动画前延迟指定时间
 */
public class DelayAction implements UIAction {

    private float delay;
    private float elapsed;
    private boolean finished;
    private Runnable onFinishListener;

    public DelayAction() {
        this(0f);
    }

    public DelayAction(float delay) {
        this.delay = delay;
        this.elapsed = 0f;
        this.finished = false;
    }

    /**
     * 创建延迟动画
     *
     * @param delay 延迟时间（秒）
     * @return DelayAction
     */
    public static DelayAction delay(float delay) {
        return new DelayAction(delay);
    }

    @Override
    public void start(UIComponent target) {
        elapsed = 0f;
        finished = false;
    }

    @Override
    public boolean update(float delta) {
        if (finished) {
            return false;
        }

        elapsed += delta;

        if (elapsed >= delay) {
            finished = true;
            if (onFinishListener != null) {
                onFinishListener.run();
            }
            return false;
        }

        return true;
    }

    @Override
    public void end() {
        finished = true;
        if (onFinishListener != null) {
            onFinishListener.run();
        }
    }

    @Override
    public void reset() {
        elapsed = 0f;
        finished = false;
    }

    @Override
    public float getDuration() {
        return delay;
    }

    @Override
    public void setDuration(float duration) {
        this.delay = duration;
    }

    @Override
    public boolean isFinished() {
        return finished;
    }

    @Override
    public void setOnFinishListener(Runnable listener) {
        this.onFinishListener = listener;
    }

    public float getDelay() {
        return delay;
    }

    public void setDelay(float delay) {
        this.delay = delay;
    }
}
