package io.github.s3s3l.yggdrasil.game.core.scene;

import io.github.s3s3l.yggdrasil.game.core.basic.Renderable;
import io.github.s3s3l.yggdrasil.game.core.basic.Storable;

/**
 * 场景接口
 * 定义场景的基本行为，包括生命周期管理和渲染
 */
public interface Scene extends Renderable, Storable {

    /**
     * 场景被激活时调用（首次进入或从后台返回）
     */
    void show();

    /**
     * 场景被隐藏时调用（切换到其他场景或进入后台）
     */
    void hide();

    /**
     * 场景每帧更新时调用
     *
     * @param delta 距离上次更新的间隔时间，单位秒
     */
    void update(float delta);

    /**
     * 场景被暂停时调用
     * 场景暂停时仍会渲染，但不会更新逻辑
     */
    default void pause() {
    }

    /**
     * 场景从暂停恢复时调用
     */
    default void resume() {
    }

    /**
     * 检查场景是否处于暂停状态
     *
     * @return true 如果场景已暂停
     */
    default boolean isPaused() {
        return false;
    }
}
