package io.github.s3s3l.yggdrasil.game.core.terrain;

import io.github.s3s3l.yggdrasil.game.core.map.Terrain;
import io.github.s3s3l.yggdrasil.game.core.map.TerrainType;

import java.util.HashMap;
import java.util.Map;

/**
 * 地形工厂
 * 用于创建和获取地形实例
 */
public class TerrainFactory {

    /**
     * 地形注册表
     */
    private static final Map<String, Terrain> terrainRegistry = new HashMap<>();

    static {
        // 注册预定义地形
        for (TerrainType type : TerrainType.values()) {
            terrainRegistry.put(type.name().toLowerCase(), type);
        }
    }

    private TerrainFactory() {
        // 私有构造，防止实例化
    }

    /**
     * 获取预定义地形
     *
     * @param name 地形名称（不区分大小写）
     * @return 地形实例，如果不存在则返回 null
     */
    public static Terrain getTerrain(String name) {
        return terrainRegistry.get(name.toLowerCase());
    }

    /**
     * 获取预定义地形，如果不存在则返回默认地形
     *
     * @param name 地形名称
     * @param defaultTerrain 默认地形
     * @return 地形实例
     */
    public static Terrain getTerrain(String name, Terrain defaultTerrain) {
        Terrain terrain = terrainRegistry.get(name.toLowerCase());
        return terrain != null ? terrain : defaultTerrain;
    }

    /**
     * 注册自定义地形
     *
     * @param name 地形名称
     * @param terrain 地形实例
     */
    public static void registerTerrain(String name, Terrain terrain) {
        terrainRegistry.put(name.toLowerCase(), terrain);
    }

    /**
     * 注销地形
     *
     * @param name 地形名称
     * @return 被注销的地形，如果不存在则返回 null
     */
    public static Terrain unregisterTerrain(String name) {
        return terrainRegistry.remove(name.toLowerCase());
    }

    /**
     * 清除所有自定义地形（保留预定义地形）
     */
    public static void clearCustomTerrains() {
        for (TerrainType type : TerrainType.values()) {
            // 保留预定义地形
        }
        // 简单实现：重建注册表
        terrainRegistry.clear();
        for (TerrainType type : TerrainType.values()) {
            terrainRegistry.put(type.name().toLowerCase(), type);
        }
    }

    /**
     * 获取所有已注册的地形名称
     *
     * @return 地形名称数组
     */
    public static String[] getRegisteredTerrainNames() {
        return terrainRegistry.keySet().toArray(new String[0]);
    }

    /**
     * 创建自定义地形
     *
     * @param name 名称
     * @param description 描述
     * @param movementCost 移动消耗
     * @param defenseBonus 防御加成
     * @param coverValue 掩体值
     * @param blocksVision 是否阻挡视野
     * @param isPassable 是否可通过
     * @return 自定义地形
     */
    public static Terrain createCustomTerrain(String name, String description, float movementCost,
                                               int defenseBonus, int coverValue, boolean blocksVision,
                                               boolean isPassable) {
        return new CustomTerrain(name, description, movementCost, defenseBonus, coverValue,
                blocksVision, isPassable);
    }
}
