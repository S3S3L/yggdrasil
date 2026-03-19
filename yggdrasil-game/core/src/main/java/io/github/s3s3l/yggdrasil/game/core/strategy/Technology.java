package io.github.s3s3l.yggdrasil.game.core.strategy;

import io.github.s3s3l.yggdrasil.game.core.basic.ID;
import io.github.s3s3l.yggdrasil.game.core.campaign.Faction;

import java.util.*;

/**
 * 科技
 */
public class Technology implements ID {

    private final long numericId;
    private final String name;
    private final String description;
    private final TechnologyType type;
    private final TechnologyTier tier;
    private final Map<String, Integer> cost;
    private final int researchTime;
    private final List<String> effects;
    private final List<String> prerequisites;
    private final List<String> unlocks;

    private Technology(Builder builder) {
        this.numericId = builder.id;
        this.name = builder.name;
        this.description = builder.description;
        this.type = builder.type;
        this.tier = builder.tier;
        this.cost = builder.cost;
        this.researchTime = builder.researchTime;
        this.effects = builder.effects;
        this.prerequisites = builder.prerequisites;
        this.unlocks = builder.unlocks;
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

    public TechnologyType getType() {
        return type;
    }

    public TechnologyTier getTier() {
        return tier;
    }

    /**
     * 获取研究消耗
     *
     * @param resourceType 资源类型
     * @return 消耗数量
     */
    public int getCost(String resourceType) {
        return cost.getOrDefault(resourceType, 0);
    }

    /**
     * 获取所有研究消耗
     *
     * @return 消耗映射
     */
    public Map<String, Integer> getAllCosts() {
        return Collections.unmodifiableMap(cost);
    }

    public int getResearchTime() {
        return researchTime;
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
     * 获取前置科技
     *
     * @return 前置科技列表
     */
    public List<String> getPrerequisites() {
        return Collections.unmodifiableList(prerequisites);
    }

    /**
     * 获取解锁内容
     *
     * @return 解锁内容列表
     */
    public List<String> getUnlocks() {
        return Collections.unmodifiableList(unlocks);
    }

    /**
     * 检查是否可以研究
     *
     * @param faction 势力
     * @return true 如果满足条件
     */
    public boolean canResearch(Faction faction) {
        if (faction == null) {
            return false;
        }

        // 检查资源
        for (Map.Entry<String, Integer> entry : cost.entrySet()) {
            if (faction.getResource(entry.getKey()) < entry.getValue()) {
                return false;
            }
        }

        // 检查前置科技
        for (String prerequisite : prerequisites) {
            if (!faction.hasTechnology(prerequisite)) {
                return false;
            }
        }

        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Technology that = (Technology) o;
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
        private TechnologyType type = TechnologyType.GENERAL;
        private TechnologyTier tier = TechnologyTier.TIER_1;
        private Map<String, Integer> cost = new HashMap<>();
        private int researchTime = 1;
        private List<String> effects = new ArrayList<>();
        private List<String> prerequisites = new ArrayList<>();
        private List<String> unlocks = new ArrayList<>();

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

        public Builder type(TechnologyType type) {
            this.type = type;
            return this;
        }

        public Builder tier(TechnologyTier tier) {
            this.tier = tier;
            return this;
        }

        public Builder cost(Map<String, Integer> cost) {
            this.cost = cost;
            return this;
        }

        public Builder researchTime(int researchTime) {
            this.researchTime = researchTime;
            return this;
        }

        public Builder effects(List<String> effects) {
            this.effects = effects;
            return this;
        }

        public Builder prerequisites(List<String> prerequisites) {
            this.prerequisites = prerequisites;
            return this;
        }

        public Builder unlocks(List<String> unlocks) {
            this.unlocks = unlocks;
            return this;
        }

        public Technology build() {
            return new Technology(this);
        }
    }
}
