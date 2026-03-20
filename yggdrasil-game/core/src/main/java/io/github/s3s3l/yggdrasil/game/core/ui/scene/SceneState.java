package io.github.s3s3l.yggdrasil.game.core.ui.scene;

import java.util.HashMap;
import java.util.Map;

/**
 * 场景状态
 * 用于保存和恢复场景状态
 */
public class SceneState {

    private final String sceneName;
    private final Map<String, Object> data;
    private long timestamp;

    /**
     * 创建场景状态
     *
     * @param sceneName 场景名称
     */
    public SceneState(String sceneName) {
        this.sceneName = sceneName;
        this.data = new HashMap<>();
        this.timestamp = System.currentTimeMillis();
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
     * 保存数据
     *
     * @param key   键
     * @param value 值
     */
    public void put(String key, Object value) {
        data.put(key, value);
    }

    /**
     * 获取数据
     *
     * @param key 键
     * @return 值
     */
    public Object get(String key) {
        return data.get(key);
    }

    /**
     * 获取字符串数据
     *
     * @param key 键
     * @return 字符串值
     */
    public String getString(String key) {
        Object value = data.get(key);
        return value != null ? value.toString() : null;
    }

    /**
     * 获取整数数据
     *
     * @param key 键
     * @return 整数值
     */
    public Integer getInt(String key) {
        Object value = data.get(key);
        if (value instanceof Number) {
            return ((Number) value).intValue();
        }
        return null;
    }

    /**
     * 获取浮点数据
     *
     * @param key 键
     * @return 浮点值
     */
    public Float getFloat(String key) {
        Object value = data.get(key);
        if (value instanceof Number) {
            return ((Number) value).floatValue();
        }
        return null;
    }

    /**
     * 获取布尔数据
     *
     * @param key 键
     * @return 布尔值
     */
    public Boolean getBoolean(String key) {
        Object value = data.get(key);
        if (value instanceof Boolean) {
            return (Boolean) value;
        }
        return null;
    }

    /**
     * 检查是否包含键
     *
     * @param key 键
     * @return true 如果包含
     */
    public boolean containsKey(String key) {
        return data.containsKey(key);
    }

    /**
     * 移除数据
     *
     * @param key 键
     */
    public void remove(String key) {
        data.remove(key);
    }

    /**
     * 清空所有数据
     */
    public void clear() {
        data.clear();
    }

    /**
     * 获取所有数据
     *
     * @return 数据映射
     */
    public Map<String, Object> getAll() {
        return new HashMap<>(data);
    }

    /**
     * 合并另一个状态
     *
     * @param other 要合并的状态
     */
    public void merge(SceneState other) {
        if (other != null) {
            data.putAll(other.data);
        }
    }

    /**
     * 复制状态
     *
     * @return 新的状态副本
     */
    public SceneState copy() {
        SceneState copy = new SceneState(sceneName);
        copy.data.putAll(data);
        copy.timestamp = timestamp;
        return copy;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SceneState that = (SceneState) o;
        if (sceneName == null) return false;
        return sceneName.equals(that.sceneName);
    }

    @Override
    public int hashCode() {
        return sceneName != null ? sceneName.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "SceneState{" +
                "sceneName='" + sceneName + '\'' +
                ", timestamp=" + timestamp +
                ", data=" + data.size() + " entries" +
                '}';
    }
}
