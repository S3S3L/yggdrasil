package io.github.s3s3l.yggdrasil.game.core.strategy;

import io.github.s3s3l.yggdrasil.game.core.basic.ID;
import io.github.s3s3l.yggdrasil.game.core.campaign.Faction;
import io.github.s3s3l.yggdrasil.game.core.campaign.Region;

import java.util.*;

/**
 * 建筑
 */
public class Building implements ID {

    private final long numericId;
    private final String name;
    private final String description;
    private final BuildingType type;
    private final Map<String, Integer> cost;
    private final Map<String, Integer> upkeep;
    private final int health;
    private final int defense;
    private final Region location;
    private final BuildingState state;
    private final Map<String, Object> properties;
    private final List<String> effects;
    private final List<String> requirements;

    private Building(Builder builder) {
        this.numericId = builder.id;
        this.name = builder.name;
        this.description = builder.description;
        this.type = builder.type;
        this.cost = builder.cost;
        this.upkeep = builder.upkeep;
        this.health = builder.health;
        this.defense = builder.defense;
        this.location = builder.location;
        this.state = builder.state;
        this.properties = builder.properties;
        this.effects = builder.effects;
        this.requirements = builder.requirements;
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

    public BuildingType getType() {
        return type;
    }

    /**
     * 获取建造消耗
     *
     * @param resourceType 资源类型
     * @return 消耗数量
     */
    public int getCost(String resourceType) {
        return cost.getOrDefault(resourceType, 0);
    }

    /**
     * 获取所有建造消耗
     *
     * @return 消耗映射
     */
    public Map<String, Integer> getAllCosts() {
        return Collections.unmodifiableMap(cost);
    }

    /**
     * 获取维护消耗
     *
     * @param resourceType 资源类型
     * @return 消耗数量
     */
    public int getUpkeep(String resourceType) {
        return upkeep.getOrDefault(resourceType, 0);
    }

    /**
     * 获取所有维护消耗
     *
     * @return 消耗映射
     */
    public Map<String, Integer> getAllUpkeeps() {
        return Collections.unmodifiableMap(upkeep);
    }

    public int getHealth() {
        return health;
    }

    public int getDefense() {
        return defense;
    }

    public Region getLocation() {
        return location;
    }

    public BuildingState getState() {
        return state;
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

    /**
     * 获取效果列表
     *
     * @return 效果列表
     */
    public List<String> getEffects() {
        return Collections.unmodifiableList(effects);
    }

    /**
     * 获取前置要求
     *
     * @return 前置要求列表
     */
    public List<String> getRequirements() {
        return Collections.unmodifiableList(requirements);
    }

    /**
     * 检查是否满足建造条件
     *
     * @param faction 势力
     * @return true 如果满足条件
     */
    public boolean canBuild(Faction faction) {
        if (faction == null) {
            return false;
        }

        // 检查资源
        for (Map.Entry<String, Integer> entry : cost.entrySet()) {
            if (faction.getResource(entry.getKey()) < entry.getValue()) {
                return false;
            }
        }

        // 检查科技要求
        for (String requirement : requirements) {
            if (!faction.hasTechnology(requirement)) {
                return false;
            }
        }

        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Building building = (Building) o;
        return numericId == building.numericId;
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
        private BuildingType type = BuildingType.NORMAL;
        private Map<String, Integer> cost = new HashMap<>();
        private Map<String, Integer> upkeep = new HashMap<>();
        private int health = 100;
        private int defense = 0;
        private Region location;
        private BuildingState state = BuildingState.CONSTRUCTED;
        private Map<String, Object> properties = new HashMap<>();
        private List<String> effects = new ArrayList<>();
        private List<String> requirements = new ArrayList<>();

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

        public Builder type(BuildingType type) {
            this.type = type;
            return this;
        }

        public Builder cost(Map<String, Integer> cost) {
            this.cost = cost;
            return this;
        }

        public Builder upkeep(Map<String, Integer> upkeep) {
            this.upkeep = upkeep;
            return this;
        }

        public Builder health(int health) {
            this.health = health;
            return this;
        }

        public Builder defense(int defense) {
            this.defense = defense;
            return this;
        }

        public Builder location(Region location) {
            this.location = location;
            return this;
        }

        public Builder state(BuildingState state) {
            this.state = state;
            return this;
        }

        public Builder properties(Map<String, Object> properties) {
            this.properties = properties;
            return this;
        }

        public Builder effects(List<String> effects) {
            this.effects = effects;
            return this;
        }

        public Builder requirements(List<String> requirements) {
            this.requirements = requirements;
            return this;
        }

        public Building build() {
            return new Building(this);
        }
    }
}
