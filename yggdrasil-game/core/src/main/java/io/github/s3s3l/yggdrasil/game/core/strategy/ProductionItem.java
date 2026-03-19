package io.github.s3s3l.yggdrasil.game.core.strategy;

import io.github.s3s3l.yggdrasil.game.core.basic.ID;

import java.util.*;

/**
 * 生产项目
 */
public class ProductionItem implements ID {

    private final long numericId;
    private final String name;
    private final String description;
    private final ItemType type;
    private final Map<String, Integer> cost;
    private final int productionTime;
    private final int priority;
    private final Map<String, Object> properties;

    private ProductionItem(Builder builder) {
        this.numericId = builder.id;
        this.name = builder.name;
        this.description = builder.description;
        this.type = builder.type;
        this.cost = builder.cost;
        this.productionTime = builder.productionTime;
        this.priority = builder.priority;
        this.properties = builder.properties;
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

    public String getDescription() {
        return description;
    }

    public ItemType getType() {
        return type;
    }

    /**
     * 获取消耗
     *
     * @param resourceType 资源类型
     * @return 消耗数量
     */
    public int getCost(String resourceType) {
        return cost.getOrDefault(resourceType, 0);
    }

    /**
     * 获取所有消耗
     *
     * @return 消耗映射
     */
    public Map<String, Integer> getAllCosts() {
        return Collections.unmodifiableMap(cost);
    }

    public int getProductionTime() {
        return productionTime;
    }

    public int getPriority() {
        return priority;
    }

    /**
     * 获取属性
     *
     * @param key 属性键
     * @return 属性值
     */
    public Object getProperty(String key) {
        return properties.get(key);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductionItem that = (ProductionItem) o;
        return numericId == that.numericId;
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
        private ItemType type = ItemType.UNIT;
        private Map<String, Integer> cost = new HashMap<>();
        private int productionTime = 1;
        private int priority = 0;
        private Map<String, Object> properties = new HashMap<>();

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

        public Builder type(ItemType type) {
            this.type = type;
            return this;
        }

        public Builder cost(Map<String, Integer> cost) {
            this.cost = cost;
            return this;
        }

        public Builder productionTime(int productionTime) {
            this.productionTime = productionTime;
            return this;
        }

        public Builder priority(int priority) {
            this.priority = priority;
            return this;
        }

        public Builder properties(Map<String, Object> properties) {
            this.properties = properties;
            return this;
        }

        public ProductionItem build() {
            return new ProductionItem(this);
        }
    }
}
