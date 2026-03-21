package io.github.s3s3l.yggdrasil.game.core.demo.entity;

import io.github.s3s3l.yggdrasil.game.core.demo.entity.component.AttackComponent;
import io.github.s3s3l.yggdrasil.game.core.demo.entity.component.HealthComponent;
import io.github.s3s3l.yggdrasil.game.core.demo.entity.component.MovementComponent;

/**
 * 玩家实体
 */
public class PlayerEntity extends GameEntity {

    /**
     * 玩家分数
     */
    private int score;

    /**
     * 玩家等级
     */
    private int level;

    /**
     * 创建玩家实体
     */
    public PlayerEntity() {
        this("Player");
    }

    /**
     * 创建玩家实体
     *
     * @param name 玩家名称
     */
    public PlayerEntity(String name) {
        super(EntityType.PLAYER, name);
        this.score = 0;
        this.level = 1;

        // 初始化组件
        this.healthComponent = new HealthComponent(100);
        this.movementComponent = new MovementComponent(0, 0, 150);
        this.attackComponent = new AttackComponent(10, 50, 0.5f);
    }

    /**
     * 获取分数
     *
     * @return 分数
     */
    public int getScore() {
        return score;
    }

    /**
     * 添加分数
     *
     * @param points 分数
     */
    public void addScore(int points) {
        this.score += points;
    }

    /**
     * 获取等级
     *
     * @return 等级
     */
    public int getLevel() {
        return level;
    }

    /**
     * 设置等级
     *
     * @param level 等级
     */
    public void setLevel(int level) {
        this.level = level;
    }

    /**
     * 升级
     */
    public void levelUp() {
        level++;
        // 升级时恢复生命值并提升属性
        healthComponent.setMaxHealth(healthComponent.getMaxHealth() + 20);
        healthComponent.reset();
        attackComponent.setAttackDamage(attackComponent.getAttackDamage() + 2);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // 重置分数
        score = 0;
    }
}
