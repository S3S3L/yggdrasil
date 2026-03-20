package io.github.s3s3l.yggdrasil.game.core.ai.combat;

import io.github.s3s3l.yggdrasil.game.core.basic.ID;

import java.util.Objects;

/**
 * 战斗目标
 * 表示一个可被选择的目标
 */
public class CombatTarget implements ID {

    private long numericId;
    private final String name;
    private final TargetType type;
    private final float health;
    private final float maxHealth;
    private final float damage;
    private final float defense;
    private final float range;
    private final float threatLevel;
    private final float distance;

    private CombatTarget(Builder builder) {
        this.numericId = builder.id;
        this.name = builder.name;
        this.type = builder.type;
        this.health = builder.health;
        this.maxHealth = builder.maxHealth;
        this.damage = builder.damage;
        this.defense = builder.defense;
        this.range = builder.range;
        this.threatLevel = builder.threatLevel;
        this.distance = builder.distance;
    }

    @Override
    public long getId() {
        return numericId;
    }

    @Override
    public void setId(long id) {
        // ID is immutable
    }

    public String getName() {
        return name;
    }

    public TargetType getType() {
        return type;
    }

    public float getHealth() {
        return health;
    }

    public float getMaxHealth() {
        return maxHealth;
    }

    public float getHealthPercent() {
        if (maxHealth == 0) {
            return 0f;
        }
        return health / maxHealth;
    }

    public float getDamage() {
        return damage;
    }

    public float getDefense() {
        return defense;
    }

    public float getRange() {
        return range;
    }

    public float getThreatLevel() {
        return threatLevel;
    }

    public float getDistance() {
        return distance;
    }

    public boolean isLowHealth(float threshold) {
        return getHealthPercent() <= threshold;
    }

    public boolean isInRange(float attackRange) {
        return distance <= attackRange + range;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CombatTarget that = (CombatTarget) o;
        return Objects.equals(numericId, that.numericId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(numericId);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private long id;
        private String name = "";
        private TargetType type = TargetType.ENEMY;
        private float health = 100f;
        private float maxHealth = 100f;
        private float damage = 10f;
        private float defense = 0f;
        private float range = 1f;
        private float threatLevel = 0f;
        private float distance = 0f;

        public Builder id(long id) {
            this.id = id;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder type(TargetType type) {
            this.type = type;
            return this;
        }

        public Builder health(float health) {
            this.health = health;
            return this;
        }

        public Builder maxHealth(float maxHealth) {
            this.maxHealth = maxHealth;
            return this;
        }

        public Builder damage(float damage) {
            this.damage = damage;
            return this;
        }

        public Builder defense(float defense) {
            this.defense = defense;
            return this;
        }

        public Builder range(float range) {
            this.range = range;
            return this;
        }

        public Builder threatLevel(float threatLevel) {
            this.threatLevel = threatLevel;
            return this;
        }

        public Builder distance(float distance) {
            this.distance = distance;
            return this;
        }

        public CombatTarget build() {
            return new CombatTarget(this);
        }
    }
}
