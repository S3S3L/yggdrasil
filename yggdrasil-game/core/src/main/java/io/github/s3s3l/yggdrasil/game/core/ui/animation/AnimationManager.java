package io.github.s3s3l.yggdrasil.game.core.ui.animation;

import io.github.s3s3l.yggdrasil.game.core.ui.UIComponent;

/**
 * 动画管理器
 * 管理和更新动画
 */
public class AnimationManager {

    private final UIComponent target;
    private final AnimationList animations;

    public AnimationManager(UIComponent target) {
        this.target = target;
        this.animations = new AnimationList();
    }

    /**
     * 添加动画
     *
     * @param action 动画
     */
    public void addAction(UIAction action) {
        animations.addAction(action);
        if (!animations.isStarted()) {
            action.start(target);
        }
    }

    /**
     * 移除动画
     *
     * @param action 动画
     */
    public void removeAction(UIAction action) {
        animations.removeAction(action);
    }

    /**
     * 清除所有动画
     */
    public void clearActions() {
        animations.clearActions();
    }

    /**
     * 更新所有动画
     *
     * @param delta 时间增量
     */
    public void update(float delta) {
        animations.update(delta);
    }

    /**
     * 检查是否有任何动画在运行
     *
     * @return true 如果有动画在运行
     */
    public boolean hasRunningActions() {
        return animations.hasRunningActions();
    }

    /**
     * 获取目标组件
     *
     * @return 目标组件
     */
    public UIComponent getTarget() {
        return target;
    }

    /**
     * 动画列表管理类
     */
    private static class AnimationList {

        private final java.util.List<UIAction> actions;
        private boolean started;

        AnimationList() {
            this.actions = new java.util.ArrayList<>();
            this.started = false;
        }

        void addAction(UIAction action) {
            actions.add(action);
        }

        void removeAction(UIAction action) {
            actions.remove(action);
        }

        void clearActions() {
            actions.clear();
            started = false;
        }

        void update(float delta) {
            if (!started && !actions.isEmpty()) {
                for (UIAction action : actions) {
                    action.start(null);
                }
                started = true;
            }

            // 移除已完成的动画
            actions.removeIf(UIAction::isFinished);

            // 更新所有动画
            for (UIAction action : actions) {
                action.update(delta);
            }
        }

        boolean isStarted() {
            return started;
        }

        boolean hasRunningActions() {
            return !actions.isEmpty() && actions.stream().anyMatch(a -> !a.isFinished());
        }
    }
}
