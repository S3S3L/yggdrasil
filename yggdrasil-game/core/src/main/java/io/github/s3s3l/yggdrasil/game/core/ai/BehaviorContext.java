package io.github.s3s3l.yggdrasil.game.core.ai;

import java.util.HashMap;
import java.util.Map;

/**
 * 行为上下文
 * 存储行为树执行过程中的共享数据
 */
public class BehaviorContext {

    private final Map<String, Object> data;

    public BehaviorContext() {
        this.data = new HashMap<>();
    }

    /**
     * 获取数据
     *
     * @param key 键
     * @param <T> 数据类型
     * @return 数据值
     */
    @SuppressWarnings("unchecked")
    public <T> T getData(String key) {
        return (T) data.get(key);
    }

    /**
     * 设置数据
     *
     * @param key   键
     * @param value 值
     */
    public void setData(String key, Object value) {
        data.put(key, value);
    }

    /**
     * 检查是否包含数据
     *
     * @param key 键
     * @return 是否包含
     */
    public boolean hasData(String key) {
        return data.containsKey(key);
    }

    /**
     * 移除数据
     *
     * @param key 键
     */
    public void removeData(String key) {
        data.remove(key);
    }

    /**
     * 清空所有数据
     */
    public void clear() {
        data.clear();
    }
}
