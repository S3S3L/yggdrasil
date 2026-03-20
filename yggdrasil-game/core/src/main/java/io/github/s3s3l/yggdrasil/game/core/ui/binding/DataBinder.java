package io.github.s3s3l.yggdrasil.game.core.ui.binding;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * 数据绑定管理器
 * 用于管理对象之间的数据绑定
 */
public class DataBinder {

    private final Map<Object, Map<String, Property<?>>> boundProperties;
    private boolean enabled;

    public DataBinder() {
        this.boundProperties = new HashMap<>();
        this.enabled = true;
    }

    /**
     * 绑定属性
     *
     * @param source    源对象
     * @param sourceProp 源属性名
     * @param target    目标对象
     * @param targetProp 目标属性名
     */
    public <T> void bind(Object source, String sourceProp, Object target, String targetProp) {
        Property<T> sourceProperty = getPropertyViaReflection(source, sourceProp);
        Property<T> targetProperty = getPropertyViaReflection(target, targetProp);

        if (sourceProperty != null && targetProperty != null) {
            targetProperty.bind(sourceProperty);
            storeBinding(source, sourceProp, target, targetProp, targetProperty);
        }
    }

    /**
     * 双向绑定
     */
    public <T> void bindBidirectional(Object source, String sourceProp, Object target, String targetProp) {
        Property<T> sourceProperty = getPropertyViaReflection(source, sourceProp);
        Property<T> targetProperty = getPropertyViaReflection(target, targetProp);

        if (sourceProperty != null && targetProperty != null) {
            sourceProperty.bindBidirectional(targetProperty);
            storeBinding(source, sourceProp, target, targetProp, targetProperty);
        }
    }

    /**
     * 解除绑定
     */
    public <T> void unbind(Object target, String targetProp) {
        Property<T> targetProperty = getProperty(target, targetProp);
        if (targetProperty != null) {
            targetProperty.unbind();
        }
        removeBinding(target, targetProp);
    }

    /**
     * 解除所有绑定
     */
    public void unbindAll() {
        for (Map<String, Property<?>> props : boundProperties.values()) {
            for (Property<?> prop : props.values()) {
                if (prop != null) {
                    prop.unbind();
                }
            }
        }
        boundProperties.clear();
    }

    /**
     * 启用绑定
     */
    public void enable() {
        this.enabled = true;
    }

    /**
     * 禁用绑定
     */
    public void disable() {
        this.enabled = false;
    }

    /**
     * 检查是否启用
     */
    public boolean isEnabled() {
        return enabled;
    }

    /**
     * 注册属性
     */
    public <T> void registerProperty(Object bean, String name, Property<T> property) {
        boundProperties.computeIfAbsent(bean, k -> new HashMap<>()).put(name, property);
    }

    /**
     * 获取属性（仅从已注册的属性中查找）
     */
    @SuppressWarnings("unchecked")
    public <T> Property<T> getProperty(Object bean, String name) {
        Map<String, Property<?>> props = boundProperties.get(bean);
        if (props != null) {
            return (Property<T>) props.get(name);
        }
        return null;
    }

    /**
     * 通过反射获取属性
     */
    @SuppressWarnings("unchecked")
    private <T> Property<T> getPropertyViaReflection(Object bean, String name) {
        if (bean != null && name != null) {
            try {
                String methodName = name + "Property";
                return (Property<T>) bean.getClass().getMethod(methodName).invoke(bean);
            } catch (Exception e) {
                // 反射失败，返回 null
            }
        }
        return null;
    }

    /**
     * 存储绑定信息
     */
    private void storeBinding(Object source, String sourceProp, Object target, String targetProp, Property<?> binding) {
        if (enabled) {
            String key = target.getClass().getName() + "#" + targetProp;
            boundProperties.computeIfAbsent(target, k -> new HashMap<>()).put(targetProp, binding);
        }
    }

    /**
     * 移除绑定信息
     */
    private void removeBinding(Object target, String prop) {
        Map<String, Property<?>> props = boundProperties.get(target);
        if (props != null) {
            props.remove(prop);
            if (props.isEmpty()) {
                boundProperties.remove(target);
            }
        }
    }

    /**
     * 创建绑定构建器
     */
    public BindingBuilder bind() {
        return new BindingBuilder(this);
    }

    /**
     * 绑定构建器
     */
    public static class BindingBuilder {

        private final DataBinder binder;
        private Object source;
        private String sourceProp;
        private boolean bidirectional;

        public BindingBuilder(DataBinder binder) {
            this.binder = binder;
        }

        public BindingBuilder from(Object source, String prop) {
            this.source = source;
            this.sourceProp = prop;
            return this;
        }

        public BindingBuilder to(Object target, String prop) {
            if (source != null && sourceProp != null) {
                if (bidirectional) {
                    binder.bindBidirectional(source, sourceProp, target, prop);
                } else {
                    binder.bind(source, sourceProp, target, prop);
                }
            }
            return this;
        }

        public BindingBuilder bidirectional() {
            this.bidirectional = true;
            return this;
        }

        public void apply() {
            // 完成绑定
        }
    }
}
