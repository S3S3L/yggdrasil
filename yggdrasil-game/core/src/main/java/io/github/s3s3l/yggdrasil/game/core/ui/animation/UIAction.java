package io.github.s3s3l.yggdrasil.game.core.ui.animation;

import io.github.s3s3l.yggdrasil.game.core.ui.UIComponent;

/**
 * UI 动画动作接口
 * 定义动画的基本行为
 */
public interface UIAction {

    /**
     * 开始执行动画
     *
     * @param target 目标组件
     */
    void start(UIComponent target);

    /**
     * 更新动画状态
     *
     * @param delta 时间增量
     * @return 动画是否仍在进行中
     */
    boolean update(float delta);

    /**
     * 结束动画
     */
    void end();

    /**
     * 重置动画到初始状态
     */
    void reset();

    /**
     * 获取动画持续时间
     *
     * @return 持续时间（秒）
     */
    float getDuration();

    /**
     * 设置动画持续时间
     *
     * @param duration 持续时间（秒）
     */
    void setDuration(float duration);

    /**
     * 检查动画是否完成
     *
     * @return true 如果动画已完成
     */
    boolean isFinished();

    /**
     * 设置动画完成监听器
     *
     * @param listener 监听器
     */
    void setOnFinishListener(Runnable listener);
}
