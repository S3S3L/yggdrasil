package io.github.s3s3l.yggdrasil.game.core.physics;

import io.github.s3s3l.yggdrasil.game.core.physics.collider.Collider;
import io.github.s3s3l.yggdrasil.game.core.physics.collider.CollisionInfo;

import java.util.List;

/**
 * 物理世界接口
 * 提供碰撞体管理和碰撞检测功能
 */
public interface PhysicsWorld {

    /**
     * 添加碰撞体到物理世界
     *
     * @param collider 碰撞体
     */
    void addCollider(Collider collider);

    /**
     * 从物理世界移除碰撞体
     *
     * @param collider 碰撞体
     */
    void removeCollider(Collider collider);

    /**
     * 更新物理世界状态
     *
     * @param deltaTime 时间增量（秒）
     */
    void update(float deltaTime);

    /**
     * 检查所有碰撞
     *
     * @return 碰撞信息列表
     */
    List<CollisionInfo> checkCollisions();

    /**
     * 添加碰撞监听器
     *
     * @param listener 碰撞监听器
     */
    void addCollisionListener(CollisionListener listener);

    /**
     * 移除碰撞监听器
     *
     * @param listener 碰撞监听器
     */
    void removeCollisionListener(CollisionListener listener);

    /**
     * 清除所有碰撞体
     */
    void clear();

    /**
     * 获取碰撞体数量
     *
     * @return 碰撞体数量
     */
    int getColliderCount();
}
