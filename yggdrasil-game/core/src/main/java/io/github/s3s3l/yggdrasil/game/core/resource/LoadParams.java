package io.github.s3s3l.yggdrasil.game.core.resource;

import java.util.HashMap;
import java.util.Map;

/**
 * 资源加载参数
 * 用于传递资源加载时的配置参数
 */
public class LoadParams {

    /**
     * 参数键值对
     */
    private final Map<String, Object> params;

    /**
     * 创建加载参数
     */
    public LoadParams() {
        this.params = new HashMap<>();
    }

    /**
     * 设置参数
     *
     * @param key   参数键
     * @param value 参数值
     * @return this
     */
    public LoadParams set(String key, Object value) {
        params.put(key, value);
        return this;
    }

    /**
     * 获取参数
     *
     * @param key 参数键
     * @return 参数值，不存在返回 null
     */
    public Object get(String key) {
        return params.get(key);
    }

    /**
     * 获取字符串参数
     *
     * @param key 参数键
     * @return 字符串值
     */
    public String getString(String key) {
        Object value = params.get(key);
        return value != null ? value.toString() : null;
    }

    /**
     * 获取整数参数
     *
     * @param key 参数键
     * @return 整数值
     */
    public Integer getInt(String key) {
        Object value = params.get(key);
        if (value instanceof Number) {
            return ((Number) value).intValue();
        }
        try {
            return Integer.parseInt(value.toString());
        } catch (NumberFormatException e) {
            return null;
        }
    }

    /**
     * 获取浮点数参数
     *
     * @param key 参数键
     * @return 浮点数值
     */
    public Float getFloat(String key) {
        Object value = params.get(key);
        if (value instanceof Number) {
            return ((Number) value).floatValue();
        }
        try {
            return Float.parseFloat(value.toString());
        } catch (NumberFormatException e) {
            return null;
        }
    }

    /**
     * 获取布尔参数
     *
     * @param key 参数键
     * @return 布尔值
     */
    public Boolean getBoolean(String key) {
        Object value = params.get(key);
        if (value instanceof Boolean) {
            return (Boolean) value;
        }
        return Boolean.parseBoolean(value != null ? value.toString() : "false");
    }

    /**
     * 检查是否包含参数
     *
     * @param key 参数键
     * @return true 如果包含该参数
     */
    public boolean has(String key) {
        return params.containsKey(key);
    }

    /**
     * 获取所有参数
     *
     * @return 参数映射
     */
    public Map<String, Object> getAll() {
        return new HashMap<>(params);
    }

    /**
     * 清除所有参数
     */
    public void clear() {
        params.clear();
    }
}
