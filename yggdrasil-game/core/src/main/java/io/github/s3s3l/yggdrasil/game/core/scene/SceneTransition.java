package io.github.s3s3l.yggdrasil.game.core.scene;

/**
 * 场景过渡效果接口
 * 用于在场景切换时执行过渡动画或效果
 */
public interface SceneTransition {

    /**
     * 过渡效果开始
     *
     * @param fromScene 源场景（可能为 null）
     * @param toScene   目标场景
     */
    void onTransitionStart(Scene fromScene, Scene toScene);

    /**
     * 过渡效果结束
     *
     * @param fromScene 源场景（可能为 null）
     * @param toScene   目标场景
     */
    void onTransitionEnd(Scene fromScene, Scene toScene);
}
