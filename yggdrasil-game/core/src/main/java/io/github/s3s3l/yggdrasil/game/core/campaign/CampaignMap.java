package io.github.s3s3l.yggdrasil.game.core.campaign;

import io.github.s3s3l.yggdrasil.game.core.basic.ID;

import java.util.*;

/**
 * 战役地图
 * 大地图表示，包含多个区域和势力
 */
public class CampaignMap implements ID {

    private final long numericId;
    private final String name;
    private final String description;
    private final Map<Long, Region> regions;
    private final Map<Long, Faction> factions;
    private final CampaignMapConfig config;

    private CampaignMap(Builder builder) {
        this.numericId = builder.id;
        this.name = builder.name;
        this.description = builder.description;
        this.regions = builder.regions;
        this.factions = builder.factions;
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

    public String getDescription() {
        return description;
    }

    /**
     * 获取所有区域
     *
     * @return 区域集合
     */
    public Collection<Region> getAllRegions() {
        return Collections.unmodifiableCollection(regions.values());
    }

    /**
     * 根据 ID 获取区域
     *
     * @param regionId 区域 ID
     * @return 区域，不存在返回 null
     */
    public Region getRegion(long regionId) {
        return regions.get(regionId);
    }

    /**
     * 添加区域
     *
     * @param region 区域
     */
    public void addRegion(Region region) {
        regions.put(region.getId(), region);
    }

    /**
     * 移除区域
     *
     * @param regionId 区域 ID
     * @return 被移除的区域，不存在返回 null
     */
    public Region removeRegion(long regionId) {
        return regions.remove(regionId);
    }

    /**
     * 获取所有势力
     *
     * @return 势力集合
     */
    public Collection<Faction> getAllFactions() {
        return Collections.unmodifiableCollection(factions.values());
    }

    /**
     * 根据 ID 获取势力
     *
     * @param factionId 势力 ID
     * @return 势力，不存在返回 null
     */
    public Faction getFaction(long factionId) {
        return factions.get(factionId);
    }

    /**
     * 添加势力
     *
     * @param faction 势力
     */
    public void addFaction(Faction faction) {
        factions.put(faction.getId(), faction);
    }

    /**
     * 移除势力
     *
     * @param factionId 势力 ID
     * @return 被移除的势力，不存在返回 null
     */
    public Faction removeFaction(long factionId) {
        return factions.remove(factionId);
    }

    /**
     * 获取地图配置
     *
     * @return 地图配置
     */
    public CampaignMapConfig getConfig() {
        return config;
    }

    /**
     * 根据名称获取势力
     *
     * @param name 势力名称
     * @return 势力，不存在返回 null
     */
    public Optional<Faction> getFactionByName(String name) {
        return factions.values().stream()
                .filter(f -> f.getName().equals(name))
                .findFirst();
    }

    /**
     * 获取指定势力的所有区域
     *
     * @param factionId 势力 ID
     * @return 区域列表
     */
    public List<Region> getRegionsByFaction(long factionId) {
        List<Region> result = new ArrayList<>();
        for (Region region : regions.values()) {
            if (region.getController() != null &&
                    region.getController().getId() == factionId) {
                result.add(region);
            }
        }
        return result;
    }

    /**
     * 获取所有首都区域
     *
     * @return 首都区域列表
     */
    public List<Region> getCapitalRegions() {
        List<Region> result = new ArrayList<>();
        for (Region region : regions.values()) {
            if (region.getType() == RegionType.CAPITAL) {
                result.add(region);
            }
        }
        return result;
    }

    /**
     * 检查两个区域是否相邻
     *
     * @param regionId1 区域 1 ID
     * @param regionId2 区域 2 ID
     * @return true 如果相邻
     */
    public boolean areRegionsAdjacent(long regionId1, long regionId2) {
        Region region1 = regions.get(regionId1);
        Region region2 = regions.get(regionId2);
        return region1 != null && region2 != null && region1.isAdjacentTo(region2);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private long id;
        private String name;
        private String description = "";
        private Map<Long, Region> regions = new HashMap<>();
        private Map<Long, Faction> factions = new HashMap<>();
        private CampaignMapConfig config = new CampaignMapConfig();

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

        public Builder regions(Map<Long, Region> regions) {
            this.regions = regions;
            return this;
        }

        public Builder factions(Map<Long, Faction> factions) {
            this.factions = factions;
            return this;
        }

        public Builder config(CampaignMapConfig config) {
            this.config = config;
            return this;
        }

        public CampaignMap build() {
            return new CampaignMap(this);
        }
    }
}
