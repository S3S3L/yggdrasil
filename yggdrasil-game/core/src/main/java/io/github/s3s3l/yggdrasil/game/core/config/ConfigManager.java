package io.github.s3s3l.yggdrasil.game.core.config;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 配置管理器
 * 管理游戏配置的加载、保存和热重载
 */
public class ConfigManager {

    private static ConfigManager instance;

    private final Map<String, ConfigLoader> loaders;
    private final Map<String, Object> configs;
    private final Map<String, ConfigChangeListener> changeListeners;
    private ConfigLoader defaultLoader;
    private boolean hotReloadEnabled;

    private ConfigManager() {
        this.loaders = new HashMap<>();
        this.configs = new ConcurrentHashMap<>();
        this.changeListeners = new ConcurrentHashMap<>();
        this.hotReloadEnabled = false;
    }

    /**
     * 获取单例实例
     *
     * @return 配置管理器实例
     */
    public static synchronized ConfigManager getInstance() {
        if (instance == null) {
            instance = new ConfigManager();
        }
        return instance;
    }

    /**
     * 注册配置加载器
     *
     * @param name   加载器名称
     * @param loader 加载器实例
     */
    public void registerLoader(String name, ConfigLoader loader) {
        loaders.put(name.toLowerCase(), loader);
    }

    /**
     * 设置默认加载器
     *
     * @param loader 加载器实例
     */
    public void setDefaultLoader(ConfigLoader loader) {
        this.defaultLoader = loader;
    }

    /**
     * 获取默认加载器
     *
     * @return 默认加载器
     */
    public ConfigLoader getDefaultLoader() {
        return defaultLoader;
    }

    /**
     * 加载配置
     *
     * @param <T>    配置类型
     * @param clazz  配置类
     * @param path   配置路径
     * @return 配置对象
     * @throws Exception 加载失败时抛出异常
     */
    public <T> T load(Class<T> clazz, String path) throws Exception {
        return load(clazz, path, defaultLoader);
    }

    /**
     * 加载配置（指定加载器）
     *
     * @param <T>      配置类型
     * @param clazz    配置类
     * @param path     配置路径
     * @param loaderId 加载器 ID
     * @return 配置对象
     * @throws Exception 加载失败时抛出异常
     */
    @SuppressWarnings("unchecked")
    public <T> T load(Class<T> clazz, String path, String loaderId) throws Exception {
        ConfigLoader loader = loaders.get(loaderId.toLowerCase());
        if (loader == null) {
            throw new IllegalArgumentException("Unknown loader: " + loaderId);
        }
        return load(clazz, path, loader);
    }

    /**
     * 加载配置
     *
     * @param <T>    配置类型
     * @param clazz  配置类
     * @param path   配置路径
     * @param loader 加载器
     * @return 配置对象
     * @throws Exception 加载失败时抛出异常
     */
    @SuppressWarnings("unchecked")
    public <T> T load(Class<T> clazz, String path, ConfigLoader loader) throws Exception {
        T config = loader.load(clazz, path);
        configs.put(path, config);
        return config;
    }

    /**
     * 保存配置
     *
     * @param config 配置对象
     * @param path   保存路径
     * @throws Exception 保存失败时抛出异常
     */
    public void save(Object config, String path) throws Exception {
        save(config, path, defaultLoader);
    }

    /**
     * 保存配置（指定加载器）
     *
     * @param config   配置对象
     * @param path     保存路径
     * @param loaderId 加载器 ID
     * @throws Exception 保存失败时抛出异常
     */
    public void save(Object config, String path, String loaderId) throws Exception {
        ConfigLoader loader = loaders.get(loaderId.toLowerCase());
        if (loader == null) {
            throw new IllegalArgumentException("Unknown loader: " + loaderId);
        }
        save(config, path, loader);
    }

    /**
     * 保存配置
     *
     * @param config 配置对象
     * @param path   保存路径
     * @param loader 加载器
     * @throws Exception 保存失败时抛出异常
     */
    public void save(Object config, String path, ConfigLoader loader) throws Exception {
        loader.save(config, path);
        configs.put(path, config);
    }

    /**
     * 获取已加载的配置
     *
     * @param <T>  配置类型
     * @param path 配置路径
     * @return 配置对象
     */
    @SuppressWarnings("unchecked")
    public <T> T getConfig(String path) {
        return (T) configs.get(path);
    }

    /**
     * 检查配置是否已加载
     *
     * @param path 配置路径
     * @return 是否已加载
     */
    public boolean hasConfig(String path) {
        return configs.containsKey(path);
    }

    /**
     * 卸载配置
     *
     * @param path 配置路径
     */
    public void unload(String path) {
        configs.remove(path);
    }

    /**
     * 卸载所有配置
     */
    public void unloadAll() {
        configs.clear();
    }

    /**
     * 启用热重载
     */
    public void enableHotReload() {
        this.hotReloadEnabled = true;
    }

    /**
     * 禁用热重载
     */
    public void disableHotReload() {
        this.hotReloadEnabled = false;
    }

    /**
     * 检查是否启用了热重载
     *
     * @return 是否启用
     */
    public boolean isHotReloadEnabled() {
        return hotReloadEnabled;
    }

    /**
     * 重新加载配置
     *
     * @param path 配置路径
     * @param clazz 配置类
     * @return 是否成功重新加载
     */
    public boolean reload(String path, Class<?> clazz) {
        if (!hotReloadEnabled) {
            return false;
        }

        Object config = configs.get(path);
        if (config == null) {
            return false;
        }

        try {
            // 使用 JSON 加载器重新加载（如果支持）
            if (defaultLoader instanceof JsonConfigLoader) {
                JsonConfigLoader jsonLoader = (JsonConfigLoader) defaultLoader;
                Object newConfig = jsonLoader.reloadIfModified(clazz, path);
                if (newConfig != null) {
                    configs.put(path, newConfig);
                    notifyChangeListeners(path, newConfig);
                    return true;
                }
            }
        } catch (Exception e) {
            // 记录错误但不抛出
            e.printStackTrace();
        }

        return false;
    }

    /**
     * 重新加载所有配置
     *
     * @return 成功重新加载的配置数量
     */
    public int reloadAll() {
        if (!hotReloadEnabled) {
            return 0;
        }

        int count = 0;
        for (Map.Entry<String, Object> entry : configs.entrySet()) {
            String path = entry.getKey();
            Object config = entry.getValue();
            try {
                if (defaultLoader instanceof JsonConfigLoader) {
                    JsonConfigLoader jsonLoader = (JsonConfigLoader) defaultLoader;
                    Object newConfig = jsonLoader.reloadIfModified(config.getClass(), path);
                    if (newConfig != null) {
                        configs.put(path, newConfig);
                        notifyChangeListeners(path, newConfig);
                        count++;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return count;
    }

    /**
     * 注册配置变更监听器
     *
     * @param path     配置路径
     * @param listener 监听器
     */
    public void addChangeListener(String path, ConfigChangeListener listener) {
        changeListeners.put(path, listener);
    }

    /**
     * 移除配置变更监听器
     *
     * @param path 配置路径
     */
    public void removeChangeListener(String path) {
        changeListeners.remove(path);
    }

    /**
     * 通知监听器配置已变更
     *
     * @param path   配置路径
     * @param config 新配置
     */
    private void notifyChangeListeners(String path, Object config) {
        ConfigChangeListener listener = changeListeners.get(path);
        if (listener != null) {
            listener.onConfigChanged(path, config);
        }
    }

    /**
     * 配置变更监听器接口
     */
    public interface ConfigChangeListener {
        /**
         * 配置变更回调
         *
         * @param path   配置路径
         * @param config 新配置
         */
        void onConfigChanged(String path, Object config);
    }
}
