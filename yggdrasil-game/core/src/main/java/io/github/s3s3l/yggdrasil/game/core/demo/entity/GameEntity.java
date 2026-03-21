package io.github.s3s3l.yggdrasil.game.core.demo.entity;

import io.github.s3s3l.yggdrasil.game.core.demo.entity.component.AttackComponent;
import io.github.s3s3l.yggdrasil.game.core.demo.entity.component.HealthComponent;
import io.github.s3s3l.yggdrasil.game.core.demo.entity.component.MovementComponent;

/**
 * 游戏实体基类
 * 包含实体的基本组件和功能
 */
public abstract class GameEntity {

    /**
     * 实体类型
     */
    protected final EntityType type;

    /**
     * 实体名称
     */
    protected String name;

    /**
     * 实体 ID
     */
    protected final long id;

    /**
     * 生命值组件
     */
    protected HealthComponent healthComponent;

    /**
     * 移动组件
     */
    protected MovementComponent movementComponent;

    /**
     * 攻击组件
     */
    protected AttackComponent attackComponent;

    /**
     * 是否激活
     */
    protected boolean active;

    /**
     * 创建游戏实体
     *
     * @param type 实体类型
     * @param name 实体名称
     */
    protected GameEntity(EntityType type, String name) {
        this.type = type;
        this.name = name;
        this.id = generateId();
        this.active = false;
    }

    /**
     * 生成唯一 ID
     *
     * @return 唯一 ID
     */
    private static long generateId() {
        return System.nanoTime();
    }

    /**
     * 获取实体类型
     *
     * @return 实体类型
     */
    public EntityType getType() {
        return type;
    }

    /**
     * 获取实体名称
     *
     * @return 实体名称
     */
    public String getName() {
        return name;
    }

    /**
     * 设置实体名称
     *
     * @param name 实体名称
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 获取实体 ID
     *
     * @return 实体 ID
     */
    public long getId() {
        return id;
    }

    /**
     * 检查是否激活
     *
     * @return true 如果已激活
     */
    public boolean isActive() {
        return active;
    }

    /**
     * 设置激活状态
     *
     * @param active 激活状态
     */
    public void setActive(boolean active) {
        this.active = active;
    }

    /**
     * 获取生命值组件
     *
     * @return 生命值组件
     */
    public HealthComponent getHealthComponent() {
        return healthComponent;
    }

    /**
     * 设置生命值组件
     *
     * @param healthComponent 生命值组件
     */
    public void setHealthComponent(HealthComponent healthComponent) {
        this.healthComponent = healthComponent;
    }

    /**
     * 获取移动组件
     *
     * @return 移动组件
     */
    public MovementComponent getMovementComponent() {
        return movementComponent;
    }

    /**
     * 设置移动组件
     *
     * @param movementComponent 移动组件
     */
    public void setMovementComponent(MovementComponent movementComponent) {
        this.movementComponent = movementComponent;
    }

    /**
     * 获取攻击组件
     *
     * @return 攻击组件
     */
    public AttackComponent getAttackComponent() {
        return attackComponent;
    }

    /**
     * 设置攻击组件
     *
     * @param attackComponent 攻击组件
     */
    public void setAttackComponent(AttackComponent attackComponent) {
        this.attackComponent = attackComponent;
    }

    /**
     * 初始化实体
     */
    public void initialize() {
        active = true;
        onInitialize();
    }

    /**
     * 初始化回调
     * 子类可重写以添加自定义初始化逻辑
     */
    protected void onInitialize() {
    }

    /**
     * 更新实体（每帧调用）
     *
     * @param delta 时间增量（秒）
     */
    public void update(float delta) {
        if (!active) {
            return;
        }

        onUpdate(delta);

        // 更新组件
        if (movementComponent != null) {
            movementComponent.update(delta);
        }
        if (attackComponent != null) {
            attackComponent.update(delta);
        }
    }

    /**
     * 更新回调
     * 子类可重写以添加自定义更新逻辑
     *
     * @param delta 时间增量（秒）
     */
    protected void onUpdate(float delta) {
    }

    /**
     * 销毁实体
     */
    public void destroy() {
        active = false;
        onDestroy();
    }

    /**
     * 销毁回调
     * 子类可重写以添加自定义销毁逻辑
     */
    protected void onDestroy() {
    }

    /**
     * 检查实体是否死亡
     *
     * @return true 如果已死亡
     */
    public boolean isDead() {
        return healthComponent != null && healthComponent.isDead();
    }

    /**
     * 受到伤害
     *
     * @param damage 伤害量
     */
    public void takeDamage(float damage) {
        if (healthComponent != null) {
            healthComponent.damage(damage);
        }
    }

    /**
     * 恢复生命值
     *
     * @param amount 恢复量
     */
    public void heal(float amount) {
        if (healthComponent != null) {
            healthComponent.heal(amount);
        }
    }

    /**
     * 尝试攻击
     *
     * @return true 如果攻击成功
     */
    public boolean tryAttack() {
        if (attackComponent != null) {
            return attackComponent.tryAttack();
        }
        return false;
    }

    /**
     * 移动到指定位置
     *
     * @param x X 位置
     * @param y Y 位置
     */
    public void moveTo(float x, float y) {
        if (movementComponent != null) {
            float currentX = movementComponent.getX();
            float currentY = movementComponent.getY();
            float dx = x - currentX;
            float dy = y - currentY;
            movementComponent.move(dx, dy);
        }
    }

    /**
     * 停止移动
     */
    public void stopMoving() {
        if (movementComponent != null) {
            movementComponent.stop();
        }
    }

    /**
     * 获取当前位置 X
     *
     * @return X 位置
     */
    public float getX() {
        return movementComponent != null ? movementComponent.getX() : 0;
    }

    /**
     * 获取当前位置 Y
     *
     * @return Y 位置
     */
    public float getY() {
        return movementComponent != null ? movementComponent.getY() : 0;
    }

    /**
     * 设置位置
     *
     * @param x X 位置
     * @param y Y 位置
     */
    public void setPosition(float x, float y) {
        if (movementComponent != null) {
            movementComponent.setPosition(x, y);
        }
    }
}
