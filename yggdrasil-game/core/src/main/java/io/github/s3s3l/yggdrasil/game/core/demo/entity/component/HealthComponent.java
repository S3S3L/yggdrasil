package io.github.s3s3l.yggdrasil.game.core.demo.entity.component;

/**
 * 生命值组件
 * 管理实体的生命值、最大生命值等
 */
public class HealthComponent {

    /**
     * 当前生命值
     */
    private float currentHealth;

    /**
     * 最大生命值
     */
    private float maxHealth;

    /**
     * 生命值变化监听器
     */
    private Runnable onHealthChanged;

    /**
     * 死亡监听器
     */
    private Runnable onDeath;

    /**
     * 创建生命值组件
     *
     * @param maxHealth 最大生命值
     */
    public HealthComponent(float maxHealth) {
        this.maxHealth = maxHealth;
        this.currentHealth = maxHealth;
    }

    /**
     * 获取当前生命值
     *
     * @return 当前生命值
     */
    public float getCurrentHealth() {
        return currentHealth;
    }

    /**
     * 获取最大生命值
     *
     * @return 最大生命值
     */
    public float getMaxHealth() {
        return maxHealth;
    }

    /**
     * 设置最大生命值
     *
     * @param maxHealth 最大生命值
     */
    public void setMaxHealth(float maxHealth) {
        this.maxHealth = maxHealth;
        if (currentHealth > maxHealth) {
            currentHealth = maxHealth;
        }
    }

    /**
     * 获取生命值百分比（0-1）
     *
     * @return 生命值百分比
     */
    public float getHealthPercent() {
        if (maxHealth <= 0) {
            return 0;
        }
        return currentHealth / maxHealth;
    }

    /**
     * 设置当前生命值
     *
     * @param health 生命值
     */
    public void setCurrentHealth(float health) {
        this.currentHealth = Math.max(0, Math.min(health, maxHealth));

        if (onHealthChanged != null) {
            onHealthChanged.run();
        }

        if (currentHealth <= 0 && onDeath != null) {
            onDeath.run();
        }
    }

    /**
     * 恢复生命值
     *
     * @param amount 恢复量
     */
    public void heal(float amount) {
        if (amount <= 0 || currentHealth >= maxHealth) {
            return;
        }
        setCurrentHealth(currentHealth + amount);
    }

    /**
     * 受到伤害
     *
     * @param damage 伤害量
     */
    public void damage(float damage) {
        if (damage <= 0 || currentHealth <= 0) {
            return;
        }
        setCurrentHealth(currentHealth - damage);
    }

    /**
     * 设置生命值变化监听器
     *
     * @param listener 监听器
     */
    public void setOnHealthChanged(Runnable listener) {
        this.onHealthChanged = listener;
    }

    /**
     * 设置死亡监听器
     *
     * @param listener 监听器
     */
    public void setOnDeath(Runnable listener) {
        this.onDeath = listener;
    }

    /**
     * 检查是否死亡
     *
     * @return true 如果已死亡
     */
    public boolean isDead() {
        return currentHealth <= 0;
    }

    /**
     * 重置生命值到最大值
     */
    public void reset() {
        currentHealth = maxHealth;
        if (onHealthChanged != null) {
            onHealthChanged.run();
        }
    }
}
