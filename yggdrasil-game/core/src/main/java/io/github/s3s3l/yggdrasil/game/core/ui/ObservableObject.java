package io.github.s3s3l.yggdrasil.game.core.ui;

import java.util.HashMap;
import java.util.Map;

/**
 * 可观察对象
 * 支持属性变更监听的数据对象
 */
public class ObservableObject {

    private final Map<String, ObservableProperty<?>> properties;

    public ObservableObject() {
        this.properties = new HashMap<>();
    }

    /**
     * 注册属性
     *
     * @param name 属性名称
     * @param value 初始值
     * @param <T> 属性类型
     */
    public <T> void registerProperty(String name, T value) {
        properties.put(name, new ObservableProperty<>(value));
    }

    /**
     * 获取属性值
     *
     * @param name 属性名称
     * @param <T> 属性类型
     * @return 属性值
     */
    @SuppressWarnings("unchecked")
    public <T> T getProperty(String name) {
        ObservableProperty<?> prop = properties.get(name);
        if (prop != null) {
            return (T) prop.getValue();
        }
        return null;
    }

    /**
     * 设置属性值
     *
     * @param name 属性名称
     * @param value 新值
     * @param <T> 属性类型
     */
    @SuppressWarnings("unchecked")
    public <T> void setProperty(String name, T value) {
        ObservableProperty<?> prop = properties.get(name);
        if (prop != null) {
            ((ObservableProperty<T>) prop).setValue(value);
        }
    }

    /**
     * 添加属性变更监听器
     *
     * @param name 属性名称
     * @param listener 监听器
     * @param <T> 属性类型
     */
    @SuppressWarnings("unchecked")
    public <T> void addPropertyChangeListener(String name, DataChangeListener<T> listener) {
        ObservableProperty<?> prop = properties.get(name);
        if (prop != null) {
            ((ObservableProperty<T>) prop).addChangeListener(listener);
        }
    }

    /**
     * 移除属性变更监听器
     *
     * @param name 属性名称
     * @param listener 监听器
     * @param <T> 属性类型
     */
    @SuppressWarnings("unchecked")
    public <T> void removePropertyChangeListener(String name, DataChangeListener<T> listener) {
        ObservableProperty<?> prop = properties.get(name);
        if (prop != null) {
            ((ObservableProperty<T>) prop).removeChangeListener(listener);
        }
    }

    /**
     * 检查是否包含指定属性
     *
     * @param name 属性名称
     * @return 是否包含
     */
    public boolean hasProperty(String name) {
        return properties.containsKey(name);
    }

    /**
     * 获取所有属性名称
     *
     * @return 属性名称集合
     */
    public java.util.Set<String> getPropertyNames() {
        return properties.keySet();
    }

    /**
     * 清除所有属性
     */
    public void clear() {
        for (ObservableProperty<?> prop : properties.values()) {
            prop.clearListeners();
        }
        properties.clear();
    }
}
