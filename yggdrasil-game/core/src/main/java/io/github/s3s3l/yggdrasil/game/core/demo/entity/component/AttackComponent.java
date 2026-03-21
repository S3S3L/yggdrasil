package io.github.s3s3l.yggdrasil.game.core.demo.entity.component;

/**
 * 攻击组件
 * 管理实体的攻击能力
 */
public class AttackComponent {

    /**
     * 攻击力
     */
    private float attackDamage;

    /**
     * 攻击范围
     */
    private float attackRange;

    /**
     * 攻击冷却时间（秒）
     */
    private float attackCooldown;

    /**
     * 当前冷却时间（秒）
     */
    private float currentCooldown;

    /**
     * 攻击速度加成（0 为无加成）
     */
    private float attackSpeedBonus;

    /**
     * 创建攻击组件
     */
    public AttackComponent() {
        this(10, 50, 1.0f);
    }

    /**
     * 创建攻击组件
     *
     * @param attackDamage  攻击力
     * @param attackRange   攻击范围
     * @param attackCooldown 攻击冷却时间（秒）
     */
    public AttackComponent(float attackDamage, float attackRange, float attackCooldown) {
        this.attackDamage = attackDamage;
        this.attackRange = attackRange;
        this.attackCooldown = attackCooldown;
        this.currentCooldown = 0;
        this.attackSpeedBonus = 0;
    }

    /**
     * 获取攻击力
     *
     * @return 攻击力
     */
    public float getAttackDamage() {
        return attackDamage;
    }

    /**
     * 设置攻击力
     *
     * @param damage 攻击力
     */
    public void setAttackDamage(float damage) {
        this.attackDamage = damage;
    }

    /**
     * 获取攻击范围
     *
     * @return 攻击范围
     */
    public float getAttackRange() {
        return attackRange;
    }

    /**
     * 设置攻击范围
     *
     * @param range 攻击范围
     */
    public void setAttackRange(float range) {
        this.attackRange = range;
    }

    /**
     * 获取基础攻击冷却时间
     *
     * @return 攻击冷却时间（秒）
     */
    public float getBaseAttackCooldown() {
        return attackCooldown;
    }

    /**
     * 获取实际攻击冷却时间（考虑攻速加成）
     *
     * @return 实际冷却时间（秒）
     */
    public float getEffectiveCooldown() {
        return attackCooldown / (1 + attackSpeedBonus);
    }

    /**
     * 设置攻击冷却时间
     *
     * @param cooldown 冷却时间（秒）
     */
    public void setAttackCooldown(float cooldown) {
        this.attackCooldown = cooldown;
    }

    /**
     * 获取攻击速度加成
     *
     * @return 攻速加成
     */
    public float getAttackSpeedBonus() {
        return attackSpeedBonus;
    }

    /**
     * 设置攻击速度加成
     *
     * @param bonus 攻速加成
     */
    public void setAttackSpeedBonus(float bonus) {
        this.attackSpeedBonus = Math.max(0, bonus);
    }

    /**
     * 检查是否可以攻击
     *
     * @return true 如果可以攻击
     */
    public boolean canAttack() {
        return currentCooldown <= 0;
    }

    /**
     * 尝试攻击
     *
     * @return true 如果攻击成功
     */
    public boolean tryAttack() {
        if (canAttack()) {
            currentCooldown = getEffectiveCooldown();
            return true;
        }
        return false;
    }

    /**
     * 更新冷却（每帧调用）
     *
     * @param delta 时间增量（秒）
     */
    public void update(float delta) {
        if (currentCooldown > 0) {
            currentCooldown = Math.max(0, currentCooldown - delta);
        }
    }

    /**
     * 获取当前冷却时间
     *
     * @return 当前冷却时间
     */
    public float getCurrentCooldown() {
        return currentCooldown;
    }

    /**
     * 获取冷却进度（0-1，1 表示冷却完成）
     *
     * @return 冷却进度
     */
    public float getCooldownProgress() {
        if (currentCooldown <= 0) {
            return 1;
        }
        return 1 - (currentCooldown / getEffectiveCooldown());
    }

    /**
     * 重置冷却
     */
    public void resetCooldown() {
        currentCooldown = 0;
    }
}
