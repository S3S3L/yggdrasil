package io.github.s3s3l.yggdrasil.game.core.ui.animation;

import io.github.s3s3l.yggdrasil.game.core.ui.UIComponent;

import java.util.ArrayList;
import java.util.List;

/**
 * 动画序列
 * 按顺序执行多个动画
 */
public class SequenceAction implements UIAction {

    private final List<UIAction> actions;
    private int currentIndex;
    private UIAction currentAction;
    private UIComponent target;
    private float duration;
    private boolean finished;
    private Runnable onFinishListener;

    public SequenceAction() {
        this.actions = new ArrayList<>();
        this.currentIndex = 0;
        this.currentAction = null;
        this.finished = false;
    }

    public SequenceAction(List<UIAction> actions) {
        this.actions = new ArrayList<>(actions);
        this.currentIndex = 0;
        this.currentAction = null;
        this.finished = false;
    }

    /**
     * 创建动画序列
     *
     * @param actions 动画列表
     * @return SequenceAction
     */
    public static SequenceAction sequence(UIAction... actions) {
        return new SequenceAction(List.of(actions));
    }

    /**
     * 创建动画序列
     *
     * @param actions 动画列表
     * @return SequenceAction
     */
    public static SequenceAction sequence(List<UIAction> actions) {
        return new SequenceAction(actions);
    }

    /**
     * 添加动画到序列
     *
     * @param action 动画
     * @return this
     */
    public SequenceAction append(UIAction action) {
        actions.add(action);
        return this;
    }

    @Override
    public void start(UIComponent target) {
        this.target = target;
        currentIndex = 0;
        finished = false;
        if (!actions.isEmpty()) {
            currentAction = actions.get(0);
            currentAction.start(target);
        }
    }

    @Override
    public boolean update(float delta) {
        if (finished || actions.isEmpty()) {
            finished = true;
            return false;
        }

        float timeUsed = 0;

        while (timeUsed < delta && !finished && currentIndex < actions.size()) {
            if (currentAction == null) {
                currentAction = actions.get(currentIndex);
                currentAction.start(target);
            }

            float timeForThisAction = delta - timeUsed;
            boolean stillRunning = currentAction.update(timeForThisAction);

            if (stillRunning) {
                // 动画仍在运行，说明时间已全部消耗
                return !finished;
            } else {
                // 动画完成，计算实际消耗的时间
                float actualTimeUsed;
                if (currentAction instanceof BaseAnimation) {
                    BaseAnimation anim = (BaseAnimation) currentAction;
                    actualTimeUsed = anim.getDuration();
                } else {
                    actualTimeUsed = currentAction.getDuration();
                }
                timeUsed += actualTimeUsed;

                // 切换到下一个动画
                currentIndex++;
                currentAction = null;
            }
        }

        if (currentIndex >= actions.size()) {
            finished = true;
            if (onFinishListener != null) {
                onFinishListener.run();
            }
        }

        return !finished;
    }

    @Override
    public void end() {
        if (currentAction != null) {
            currentAction.end();
        }
        finished = true;
        if (onFinishListener != null) {
            onFinishListener.run();
        }
    }

    @Override
    public void reset() {
        currentIndex = 0;
        finished = false;
        if (!actions.isEmpty()) {
            currentAction = actions.get(0);
            currentAction.reset();
        }
    }

    @Override
    public float getDuration() {
        float total = 0;
        for (UIAction action : actions) {
            total += action.getDuration();
        }
        return total;
    }

    @Override
    public void setDuration(float duration) {
        // 序列的持续时间由各个动画决定
    }

    @Override
    public boolean isFinished() {
        return finished;
    }

    @Override
    public void setOnFinishListener(Runnable listener) {
        this.onFinishListener = listener;
    }

    private UIComponent getTarget(UIAction action) {
        if (action instanceof BaseAnimation) {
            return ((BaseAnimation) action).getTarget();
        }
        return null;
    }

    public List<UIAction> getActions() {
        return actions;
    }

    public int getCurrentIndex() {
        return currentIndex;
    }
}
