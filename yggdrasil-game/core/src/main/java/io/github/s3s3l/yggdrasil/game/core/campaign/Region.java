package io.github.s3s3l.yggdrasil.game.core.campaign;

import io.github.s3s3l.yggdrasil.game.core.basic.ID;
import io.github.s3s3l.yggdrasil.game.core.grid.Coordinate;

import java.util.*;

/**
 * 区域
 * 战役地图上的一个可控制区域
 */
public class Region implements ID {

    private final long numericId;
    private final String name;
    private final String description;
    private final Coordinate position;
    private final int size;
    private final Map<String, Object> properties;
    private Faction controller;
    private final List<Region> adjacentRegions;
    private final RegionType type;

    private Region(Builder builder) {
        this.numericId = builder.id;
        this.name = builder.name;
        this.description = builder.description;
        this.position = builder.position;
        this.size = builder.size;
        this.properties = builder.properties;
        this.controller = builder.controller;
        this.adjacentRegions = builder.adjacentRegions;
        this.type = builder.type;
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

    public Coordinate getPosition() {
        return position;
    }

    public int getSize() {
        return size;
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
     * 设置属性
     *
     * @param key   属性键
     * @param value 属性值
     */
    public void setProperty(String key, Object value) {
        properties.put(key, value);
    }

    /**
     * 获取控制者
     *
     * @return 控制势力
     */
    public Faction getController() {
        return controller;
    }

    /**
     * 设置控制者
     *
     * @param controller 控制势力
     */
    public void setController(Faction controller) {
        this.controller = controller;
    }

    /**
     * 获取相邻区域
     *
     * @return 相邻区域列表
     */
    public List<Region> getAdjacentRegions() {
        return Collections.unmodifiableList(adjacentRegions);
    }

    /**
     * 添加相邻区域
     *
     * @param region 相邻区域
     */
    public void addAdjacentRegion(Region region) {
        if (!adjacentRegions.contains(region)) {
            adjacentRegions.add(region);
        }
    }

    /**
     * 移除相邻区域
     *
     * @param region 相邻区域
     */
    public void removeAdjacentRegion(Region region) {
        adjacentRegions.remove(region);
    }

    /**
     * 检查是否与另一个区域相邻
     *
     * @param region 区域
     * @return true 如果相邻
     */
    public boolean isAdjacentTo(Region region) {
        return adjacentRegions.contains(region);
    }

    /**
     * 获取区域类型
     *
     * @return 区域类型
     */
    public RegionType getType() {
        return type;
    }

    /**
     * 检查是否有资源产出
     *
     * @return true 如果有资源产出
     */
    public boolean hasResourceProduction() {
        return properties.containsKey("resourceProduction");
    }

    /**
     * 获取资源产出
     *
     * @param resourceType 资源类型
     * @return 产出数量
     */
    public int getResourceProduction(String resourceType) {
        Object production = properties.get("resourceProduction");
        if (production instanceof Map) {
            return ((Map<String, Integer>) production).getOrDefault(resourceType, 0);
        }
        return 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Region region = (Region) o;
        return Objects.equals(numericId, region.numericId);
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
        private Coordinate position;
        private int size = 1;
        private Map<String, Object> properties = new HashMap<>();
        private Faction controller;
        private List<Region> adjacentRegions = new ArrayList<>();
        private RegionType type = RegionType.NORMAL;

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

        public Builder position(Coordinate position) {
            this.position = position;
            return this;
        }

        public Builder size(int size) {
            this.size = size;
            return this;
        }

        public Builder properties(Map<String, Object> properties) {
            this.properties = properties;
            return this;
        }

        public Builder controller(Faction controller) {
            this.controller = controller;
            return this;
        }

        public Builder adjacentRegions(List<Region> adjacentRegions) {
            this.adjacentRegions = adjacentRegions;
            return this;
        }

        public Builder type(RegionType type) {
            this.type = type;
            return this;
        }

        public Region build() {
            return new Region(this);
        }
    }
}
