package io.github.s3s3l.yggdrasil.game.core.physics;

/**
 * 碰撞事件监听器接口
 * 用于接收物理世界的碰撞事件通知
 */
public interface CollisionListener {

    /**
     * 当两个碰撞体开始碰撞时调用
     *
     * @param event 碰撞事件
     */
    void onCollisionStart(CollisionEvent event);

    /**
     * 当两个碰撞体持续碰撞时调用（每帧）
     *
     * @param event 碰撞事件
     */
    void onCollisionStay(CollisionEvent event);

    /**
     * 当两个碰撞体结束碰撞时调用
     *
     * @param event 碰撞事件
     */
    void onCollisionEnd(CollisionEvent event);
}
