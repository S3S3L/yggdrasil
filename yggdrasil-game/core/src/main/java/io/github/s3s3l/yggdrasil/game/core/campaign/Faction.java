package io.github.s3s3l.yggdrasil.game.core.campaign;

import io.github.s3s3l.yggdrasil.game.core.basic.ID;
import io.github.s3s3l.yggdrasil.game.core.strategy.Building;

import java.util.*;

/**
 * 势力
 * 代表战役中的一个派系或阵营
 */
public class Faction implements ID {

    private final long numericId;
    private final String name;
    private final String description;
    private final Map<String, Integer> resources;
    private final Set<Region> controlledRegions;
    private final FactionState state;
    private final Map<String, Boolean> unlockedTechnologies;
    private final List<Building> buildings;

    private Faction(Builder builder) {
        this.numericId = builder.id;
        this.name = builder.name;
        this.description = builder.description;
        this.resources = builder.resources;
        this.controlledRegions = builder.controlledRegions;
        this.state = builder.state;
        this.unlockedTechnologies = builder.unlockedTechnologies;
        this.buildings = builder.buildings;
    }

    @Override
    public long getId() {
        return numericId;
    }

    public void setId(long id) {
        // ID is immutable after construction
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    /**
     * 获取资源
     *
     * @param resourceType 资源类型
     * @return 资源数量
     */
    public int getResource(String resourceType) {
        return resources.getOrDefault(resourceType, 0);
    }

    /**
     * 添加资源
     *
     * @param resourceType 资源类型
     * @param amount       数量
     */
    public void addResource(String resourceType, int amount) {
        resources.merge(resourceType, amount, Integer::sum);
    }

    /**
     * 消耗资源
     *
     * @param resourceType 资源类型
     * @param amount       数量
     * @return true 如果资源足够并成功消耗
     */
    public boolean consumeResource(String resourceType, int amount) {
        int current = getResource(resourceType);
        if (current >= amount) {
            resources.put(resourceType, current - amount);
            return true;
        }
        return false;
    }

    /**
     * 获取所有资源
     *
     * @return 资源映射（不可修改）
     */
    public Map<String, Integer> getAllResources() {
        return Collections.unmodifiableMap(resources);
    }

    /**
     * 获取控制的区域
     *
     * @return 区域集合
     */
    public Set<Region> getControlledRegions() {
        return Collections.unmodifiableSet(controlledRegions);
    }

    /**
     * 添加控制的区域
     *
     * @param region 区域
     */
    public void addControlledRegion(Region region) {
        controlledRegions.add(region);
        region.setController(this);
    }

    /**
     * 移除控制的区域
     *
     * @param region 区域
     */
    public void removeControlledRegion(Region region) {
        controlledRegions.remove(region);
        if (region.getController() == this) {
            region.setController(null);
        }
    }

    /**
     * 获取势力状态
     *
     * @return 势力状态
     */
    public FactionState getState() {
        return state;
    }

    /**
     * 检查是否已解锁科技
     *
     * @param technologyId 科技 ID
     * @return true 如果已解锁
     */
    public boolean hasTechnology(String technologyId) {
        return unlockedTechnologies.getOrDefault(technologyId, false);
    }

    /**
     * 解锁科技
     *
     * @param technologyId 科技 ID
     */
    public void unlockTechnology(String technologyId) {
        unlockedTechnologies.put(technologyId, true);
    }

    /**
     * 获取所有已解锁科技
     *
     * @return 科技 ID 集合
     */
    public Set<String> getUnlockedTechnologies() {
        return Collections.unmodifiableSet(unlockedTechnologies.keySet());
    }

    /**
     * 获取建筑列表
     *
     * @return 建筑列表
     */
    public List<Building> getBuildings() {
        return Collections.unmodifiableList(buildings);
    }

    /**
     * 添加建筑
     *
     * @param building 建筑
     */
    public void addBuilding(Building building) {
        buildings.add(building);
    }

    /**
     * 移除建筑
     *
     * @param building 建筑
     */
    public void removeBuilding(Building building) {
        buildings.remove(building);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Faction faction = (Faction) o;
        return numericId == faction.numericId;
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
        private Map<String, Integer> resources = new HashMap<>();
        private Set<Region> controlledRegions = new HashSet<>();
        private FactionState state = FactionState.ACTIVE;
        private Map<String, Boolean> unlockedTechnologies = new HashMap<>();
        private List<Building> buildings = new ArrayList<>();

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

        public Builder resources(Map<String, Integer> resources) {
            this.resources = resources;
            return this;
        }

        public Builder controlledRegions(Set<Region> controlledRegions) {
            this.controlledRegions = controlledRegions;
            return this;
        }

        public Builder state(FactionState state) {
            this.state = state;
            return this;
        }

        public Builder unlockedTechnologies(Map<String, Boolean> unlockedTechnologies) {
            this.unlockedTechnologies = unlockedTechnologies;
            return this;
        }

        public Builder buildings(List<Building> buildings) {
            this.buildings = buildings;
            return this;
        }

        public Faction build() {
            return new Faction(this);
        }
    }
}
