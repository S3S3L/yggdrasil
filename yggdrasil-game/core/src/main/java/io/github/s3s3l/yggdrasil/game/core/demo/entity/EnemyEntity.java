package io.github.s3s3l.yggdrasil.game.core.demo.entity;

import io.github.s3s3l.yggdrasil.game.core.demo.entity.component.AttackComponent;
import io.github.s3s3l.yggdrasil.game.core.demo.entity.component.HealthComponent;
import io.github.s3s3l.yggdrasil.game.core.demo.entity.component.MovementComponent;

/**
 * 敌人实体
 */
public class EnemyEntity extends GameEntity {

    /**
     * 敌人类型
     */
    public enum EnemyType {
        /**
         * 普通敌人
         */
        NORMAL,
        /**
         * 精英敌人
         */
        ELITE,
        /**
         * Boss
         */
        BOSS
    }

    private final EnemyType enemyType;

    /**
     * 创建敌人实体
     *
     * @param enemyType 敌人类型
     */
    public EnemyEntity(EnemyType enemyType) {
        this("Enemy", enemyType);
    }

    /**
     * 创建敌人实体
     *
     * @param name      敌人名称
     * @param enemyType 敌人类型
     */
    public EnemyEntity(String name, EnemyType enemyType) {
        super(EntityType.ENEMY, name);
        this.enemyType = enemyType;

        // 根据敌人类型初始化属性
        switch (enemyType) {
            case BOSS:
                this.healthComponent = new HealthComponent(500);
                this.movementComponent = new MovementComponent(0, 0, 50);
                this.attackComponent = new AttackComponent(30, 80, 2.0f);
                break;
            case ELITE:
                this.healthComponent = new HealthComponent(150);
                this.movementComponent = new MovementComponent(0, 0, 80);
                this.attackComponent = new AttackComponent(20, 60, 1.0f);
                break;
            case NORMAL:
            default:
                this.healthComponent = new HealthComponent(50);
                this.movementComponent = new MovementComponent(0, 0, 100);
                this.attackComponent = new AttackComponent(10, 40, 1.0f);
                break;
        }
    }

    /**
     * 获取敌人类型
     *
     * @return 敌人类型
     */
    public EnemyType getEnemyType() {
        return enemyType;
    }

    /**
     * 获取击败后获得的分数
     *
     * @return 分数
     */
    public int getBounty() {
        switch (enemyType) {
            case BOSS:
                return 1000;
            case ELITE:
                return 200;
            case NORMAL:
            default:
                return 50;
        }
    }
}
