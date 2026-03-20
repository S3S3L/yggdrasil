package io.github.s3s3l.yggdrasil.game.core.ui.animation;

import io.github.s3s3l.yggdrasil.game.core.ui.UIComponent;

import java.util.ArrayList;
import java.util.List;

/**
 * 并行动画
 * 同时执行多个动画
 */
public class ParallelAction implements UIAction {

    private final List<UIAction> actions;
    private boolean finished;
    private Runnable onFinishListener;
    private UIComponent target;

    public ParallelAction() {
        this.actions = new ArrayList<>();
        this.finished = false;
    }

    public ParallelAction(List<UIAction> actions) {
        this.actions = new ArrayList<>(actions);
        this.finished = false;
    }

    /**
     * 创建并行动画
     *
     * @param actions 动画列表
     * @return ParallelAction
     */
    public static ParallelAction parallel(UIAction... actions) {
        return new ParallelAction(List.of(actions));
    }

    /**
     * 创建并行动画
     *
     * @param actions 动画列表
     * @return ParallelAction
     */
    public static ParallelAction parallel(List<UIAction> actions) {
        return new ParallelAction(actions);
    }

    /**
     * 添加动画到并行组
     *
     * @param action 动画
     * @return this
     */
    public ParallelAction append(UIAction action) {
        actions.add(action);
        return this;
    }

    @Override
    public void start(UIComponent target) {
        this.target = target;
        this.finished = false;
        for (UIAction action : actions) {
            action.start(target);
        }
    }

    @Override
    public boolean update(float delta) {
        if (finished) {
            return false;
        }

        boolean anyRunning = false;
        boolean allFinished = true;

        for (UIAction action : actions) {
            if (!action.isFinished()) {
                action.update(delta);
                anyRunning = true;
            }
            if (!action.isFinished()) {
                allFinished = false;
            }
        }

        if (allFinished) {
            finished = true;
            if (onFinishListener != null) {
                onFinishListener.run();
            }
            return false;
        }

        return anyRunning;
    }

    @Override
    public void end() {
        for (UIAction action : actions) {
            action.end();
        }
        finished = true;
        if (onFinishListener != null) {
            onFinishListener.run();
        }
    }

    @Override
    public void reset() {
        finished = false;
        for (UIAction action : actions) {
            action.reset();
        }
    }

    @Override
    public float getDuration() {
        float max = 0;
        for (UIAction action : actions) {
            max = Math.max(max, action.getDuration());
        }
        return max;
    }

    @Override
    public void setDuration(float duration) {
        // 并行动画的持续时间由最长的动画决定
    }

    @Override
    public boolean isFinished() {
        return finished;
    }

    @Override
    public void setOnFinishListener(Runnable listener) {
        this.onFinishListener = listener;
    }

    public List<UIAction> getActions() {
        return actions;
    }
}
