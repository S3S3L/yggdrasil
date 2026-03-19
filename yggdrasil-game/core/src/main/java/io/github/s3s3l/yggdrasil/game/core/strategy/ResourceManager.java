package io.github.s3s3l.yggdrasil.game.core.strategy;

import io.github.s3s3l.yggdrasil.game.core.basic.ID;

import java.util.*;

/**
 * 资源管理器
 * 管理势力的资源生产、消耗和存储
 */
public class ResourceManager implements ID {

    private final long numericId;
    private final String name;
    private final Map<String, Resource> resources;
    private final Map<String, ResourceProduction> productions;
    private final Map<String, ResourceConsumption> consumptions;
    private final ResourceConfig config;

    private ResourceManager(Builder builder) {
        this.numericId = builder.id;
        this.name = builder.name;
        this.resources = builder.resources;
        this.productions = builder.productions;
        this.consumptions = builder.consumptions;
        this.config = builder.config;
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

    /**
     * 获取资源
     *
     * @param resourceType 资源类型
     * @return 资源对象，不存在返回 null
     */
    public Resource getResource(String resourceType) {
        return resources.get(resourceType);
    }

    /**
     * 获取资源数量
     *
     * @param resourceType 资源类型
     * @return 资源数量
     */
    public int getResourceAmount(String resourceType) {
        Resource resource = resources.get(resourceType);
        return resource != null ? resource.getAmount() : 0;
    }

    /**
     * 添加资源
     *
     * @param resourceType 资源类型
     * @param amount       数量
     */
    public void addResource(String resourceType, int amount) {
        resources.computeIfPresent(resourceType, (key, res) ->
                Resource.builder()
                        .id(res.getId())
                        .name(res.getName())
                        .type(res.getType())
                        .amount(Math.min(res.getAmount() + amount, res.getMaxCapacity()))
                        .maxCapacity(res.getMaxCapacity())
                        .incomeRate(res.getIncomeRate())
                        .build());
    }

    /**
     * 消耗资源
     *
     * @param resourceType 资源类型
     * @param amount       数量
     * @return true 如果资源足够并成功消耗
     */
    public boolean consumeResource(String resourceType, int amount) {
        Resource resource = resources.get(resourceType);
        if (resource != null && resource.getAmount() >= amount) {
            resources.put(resourceType,
                    Resource.builder()
                            .id(resource.getId())
                            .name(resource.getName())
                            .type(resource.getType())
                            .amount(resource.getAmount() - amount)
                            .maxCapacity(resource.getMaxCapacity())
                            .incomeRate(resource.getIncomeRate())
                            .build());
            return true;
        }
        return false;
    }

    /**
     * 检查是否有足够资源
     *
     * @param resourceType 资源类型
     * @param amount       数量
     * @return true 如果资源足够
     */
    public boolean hasEnoughResource(String resourceType, int amount) {
        return getResourceAmount(resourceType) >= amount;
    }

    /**
     * 获取所有资源
     *
     * @return 资源映射
     */
    public Map<String, Resource> getAllResources() {
        return Collections.unmodifiableMap(resources);
    }

    /**
     * 注册资源生产
     *
     * @param resourceType 资源类型
     * @param production   生产配置
     */
    public void registerProduction(String resourceType, ResourceProduction production) {
        productions.put(resourceType, production);
    }

    /**
     * 注册资源消耗
     *
     * @param resourceType 资源类型
     * @param consumption  消耗配置
     */
    public void registerConsumption(String resourceType, ResourceConsumption consumption) {
        consumptions.put(resourceType, consumption);
    }

    /**
     * 执行资源生产
     */
    public void executeProduction() {
        for (Map.Entry<String, ResourceProduction> entry : productions.entrySet()) {
            String resourceType = entry.getKey();
            ResourceProduction production = entry.getValue();
            int amount = production.calculateProduction(this);
            if (amount > 0) {
                addResource(resourceType, amount);
            }
        }
    }

    /**
     * 执行资源消耗
     *
     * @return 总消耗量映射
     */
    public Map<String, Integer> executeConsumption() {
        Map<String, Integer> totalConsumption = new HashMap<>();
        for (Map.Entry<String, ResourceConsumption> entry : consumptions.entrySet()) {
            String resourceType = entry.getKey();
            ResourceConsumption consumption = entry.getValue();
            int amount = consumption.calculateConsumption(this);
            if (amount > 0) {
                consumeResource(resourceType, amount);
                totalConsumption.put(resourceType, amount);
            }
        }
        return totalConsumption;
    }

    /**
     * 获取资源配置
     *
     * @return 资源配置
     */
    public ResourceConfig getConfig() {
        return config;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private long id;
        private String name;
        private Map<String, Resource> resources = new HashMap<>();
        private Map<String, ResourceProduction> productions = new HashMap<>();
        private Map<String, ResourceConsumption> consumptions = new HashMap<>();
        private ResourceConfig config = new ResourceConfig();

        public Builder id(long id) {
            this.id = id;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder resources(Map<String, Resource> resources) {
            this.resources = resources;
            return this;
        }

        public Builder productions(Map<String, ResourceProduction> productions) {
            this.productions = productions;
            return this;
        }

        public Builder consumptions(Map<String, ResourceConsumption> consumptions) {
            this.consumptions = consumptions;
            return this;
        }

        public Builder config(ResourceConfig config) {
            this.config = config;
            return this;
        }

        public ResourceManager build() {
            return new ResourceManager(this);
        }
    }
}
