package io.github.s3s3l.yggdrasil.game.core.ui.animation;

import io.github.s3s3l.yggdrasil.game.core.ui.UIComponent;

/**
 * UI 动画接口
 * 定义随时间变化的动画效果
 */
public interface UIAnimation extends UIAction {

    /**
     * 获取动画进度（0.0 - 1.0）
     *
     * @return 动画进度
     */
    float getProgress();

    /**
     * 设置缓动函数
     *
     * @param easing 缓动函数
     */
    void setEasing(UIEasing easing);

    /**
     * 获取缓动函数
     *
     * @return 缓动函数
     */
    UIEasing getEasing();

    /**
     * 设置是否循环
     *
     * @param loop true 为循环
     */
    void setLoop(boolean loop);

    /**
     * 检查是否循环
     *
     * @return true 如果动画循环
     */
    boolean isLoop();

    /**
     * 设置是否自动反转（往返动画）
     *
     * @param autoReverse true 为往返
     */
    void setAutoReverse(boolean autoReverse);

    /**
     * 检查是否自动反转
     *
     * @return true 如果动画自动反转
     */
    boolean isAutoReverse();
}
