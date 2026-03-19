package io.github.s3s3l.yggdrasil.game.core.strategy;

import io.github.s3s3l.yggdrasil.game.core.basic.ID;

import java.util.*;

/**
 * 资源
 */
public class Resource implements ID {

    private final long numericId;
    private final String name;
    private final String description;
    private final ResourceType type;
    private int amount;
    private int maxCapacity;
    private int incomeRate;

    private Resource(Builder builder) {
        this.numericId = builder.id;
        this.name = builder.name;
        this.description = builder.description;
        this.type = builder.type;
        this.amount = builder.amount;
        this.maxCapacity = builder.maxCapacity;
        this.incomeRate = builder.incomeRate;
    }

    @Override
    public long getId() {
        return numericId;
    }

    @Override
    public void setId(long id) {
        // ID is immutable after construction
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public ResourceType getType() {
        return type;
    }

    public int getAmount() {
        return amount;
    }

    public int getMaxCapacity() {
        return maxCapacity;
    }

    public int getIncomeRate() {
        return incomeRate;
    }

    /**
     * 检查是否已满容量
     *
     * @return true 如果已达到最大容量
     */
    public boolean isAtCapacity() {
        return amount >= maxCapacity;
    }

    /**
     * 检查是否有盈余
     *
     * @return true 如果收入率为正
     */
    public boolean hasIncome() {
        return incomeRate > 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Resource resource = (Resource) o;
        return numericId == resource.numericId;
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
        private String name;
        private String description = "";
        private ResourceType type = ResourceType.MATERIAL;
        private int amount;
        private int maxCapacity = Integer.MAX_VALUE;
        private int incomeRate;

        public Builder id(long id) {
            this.id = id;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder type(ResourceType type) {
            this.type = type;
            return this;
        }

        public Builder amount(int amount) {
            this.amount = amount;
            return this;
        }

        public Builder maxCapacity(int maxCapacity) {
            this.maxCapacity = maxCapacity;
            return this;
        }

        public Builder incomeRate(int incomeRate) {
            this.incomeRate = incomeRate;
            return this;
        }

        public Resource build() {
            return new Resource(this);
        }
    }
}
