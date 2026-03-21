package io.github.s3s3l.yggdrasil.game.core.save;

import java.util.HashMap;
import java.util.Map;

/**
 * 存档槽
 * 用于存储单个存档位的数据
 */
public class SaveSlot {

    /**
     * 存档槽 ID（从 1 开始）
     */
    private final int slotId;

    /**
     * 存档名称（用户自定义）
     */
    private String slotName;

    /**
     * 场景名称
     */
    private String sceneName;

    /**
     * 存档时间戳
     */
    private long timestamp;

    /**
     * 游戏时间（秒）
     */
    private float gameTime;

    /**
     * 场景状态数据
     */
    private final Map<String, Object> sceneData;

    /**
     * 元数据
     */
    private final Map<String, String> metadata;

    /**
     * 创建存档槽
     *
     * @param slotId 存档槽 ID
     */
    public SaveSlot(int slotId) {
        this.slotId = slotId;
        this.sceneData = new HashMap<>();
        this.metadata = new HashMap<>();
        this.timestamp = 0;
        this.gameTime = 0;
    }

    /**
     * 获取存档槽 ID
     *
     * @return 存档槽 ID
     */
    public int getSlotId() {
        return slotId;
    }

    /**
     * 获取存档名称
     *
     * @return 存档名称
     */
    public String getSlotName() {
        return slotName != null ? slotName : "Slot " + slotId;
    }

    /**
     * 设置存档名称
     *
     * @param slotName 存档名称
     */
    public void setSlotName(String slotName) {
        this.slotName = slotName;
    }

    /**
     * 获取场景名称
     *
     * @return 场景名称
     */
    public String getSceneName() {
        return sceneName;
    }

    /**
     * 设置场景名称
     *
     * @param sceneName 场景名称
     */
    public void setSceneName(String sceneName) {
        this.sceneName = sceneName;
    }

    /**
     * 获取时间戳
     *
     * @return 时间戳
     */
    public long getTimestamp() {
        return timestamp;
    }

    /**
     * 更新时间戳
     */
    public void updateTimestamp() {
        this.timestamp = System.currentTimeMillis();
    }

    /**
     * 设置时间戳
     *
     * @param timestamp 时间戳
     */
    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    /**
     * 获取游戏时间
     *
     * @return 游戏时间（秒）
     */
    public float getGameTime() {
        return gameTime;
    }

    /**
     * 设置游戏时间
     *
     * @param gameTime 游戏时间（秒）
     */
    public void setGameTime(float gameTime) {
        this.gameTime = gameTime;
    }

    /**
     * 保存场景数据
     *
     * @param key   键
     * @param value 值
     */
    public void putSceneData(String key, Object value) {
        sceneData.put(key, value);
    }

    /**
     * 获取场景数据
     *
     * @param key 键
     * @return 值
     */
    public Object getSceneData(String key) {
        return sceneData.get(key);
    }

    /**
     * 获取字符串场景数据
     *
     * @param key 键
     * @return 字符串值
     */
    public String getStringSceneData(String key) {
        Object value = sceneData.get(key);
        return value != null ? value.toString() : null;
    }

    /**
     * 获取整数场景数据
     *
     * @param key 键
     * @return 整数值
     */
    public Integer getIntSceneData(String key) {
        Object value = sceneData.get(key);
        if (value instanceof Number) {
            return ((Number) value).intValue();
        }
        return null;
    }

    /**
     * 获取浮点场景数据
     *
     * @param key 键
     * @return 浮点值
     */
    public Float getFloatSceneData(String key) {
        Object value = sceneData.get(key);
        if (value instanceof Number) {
            return ((Number) value).floatValue();
        }
        return null;
    }

    /**
     * 获取布尔场景数据
     *
     * @param key 键
     * @return 布尔值
     */
    public Boolean getBooleanSceneData(String key) {
        Object value = sceneData.get(key);
        if (value instanceof Boolean) {
            return (Boolean) value;
        }
        return null;
    }

    /**
     * 获取所有场景数据
     *
     * @return 场景数据映射
     */
    public Map<String, Object> getAllSceneData() {
        return new HashMap<>(sceneData);
    }

    /**
     * 设置元数据
     *
     * @param key   键
     * @param value 值
     */
    public void putMetadata(String key, String value) {
        metadata.put(key, value);
    }

    /**
     * 获取元数据
     *
     * @param key 键
     * @return 值
     */
    public String getMetadata(String key) {
        return metadata.get(key);
    }

    /**
     * 获取所有元数据
     *
     * @return 元数据映射
     */
    public Map<String, String> getAllMetadata() {
        return new HashMap<>(metadata);
    }

    /**
     * 检查是否包含场景数据
     *
     * @param key 键
     * @return true 如果包含
     */
    public boolean hasSceneData(String key) {
        return sceneData.containsKey(key);
    }

    /**
     * 清除场景数据
     */
    public void clearSceneData() {
        sceneData.clear();
    }

    /**
     * 检查是否有存档数据
     *
     * @return true 如果有存档数据
     */
    public boolean hasData() {
        return sceneName != null && !sceneData.isEmpty();
    }

    /**
     * 清空存档
     */
    public void clear() {
        slotName = null;
        sceneName = null;
        timestamp = 0;
        gameTime = 0;
        sceneData.clear();
        metadata.clear();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SaveSlot saveSlot = (SaveSlot) o;
        return slotId == saveSlot.slotId;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(slotId);
    }

    @Override
    public String toString() {
        return "SaveSlot{" +
                "slotId=" + slotId +
                ", slotName='" + slotName + '\'' +
                ", sceneName='" + sceneName + '\'' +
                ", timestamp=" + timestamp +
                ", gameTime=" + gameTime +
                ", data=" + sceneData.size() + " entries" +
                '}';
    }
}
