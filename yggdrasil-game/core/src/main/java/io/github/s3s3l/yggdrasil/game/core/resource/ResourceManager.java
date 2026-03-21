package io.github.s3s3l.yggdrasil.game.core.resource;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 资源管理器
 * 统一管理游戏资源的加载、缓存和卸载
 */
public class ResourceManager {

    private static final ResourceManager INSTANCE = new ResourceManager();

    /**
     * 资源加载器映射
     */
    private final Map<Class<?>, ResourceLoader<?>> loaders;

    /**
     * 资源缓存（路径 -> 资源）
     */
    private final Map<String, Object> resourceCache;

    /**
     * 资源引用计数
     */
    private final Map<String, Integer> referenceCounts;

    /**
     * 缓存大小限制
     */
    private int maxCacheSize;

    /**
     * 当前缓存大小
     */
    private AtomicLong currentCacheSize;

    /**
     * 私有构造函数（单例）
     */
    private ResourceManager() {
        this.loaders = new HashMap<>();
        this.resourceCache = new HashMap<>();
        this.referenceCounts = new HashMap<>();
        this.maxCacheSize = 1000; // 默认最多缓存 1000 个资源
        this.currentCacheSize = new AtomicLong(0);
    }

    /**
     * 获取资源管理器实例
     *
     * @return 资源管理器实例
     */
    public static ResourceManager get() {
        return INSTANCE;
    }

    /**
     * 注册资源加载器
     *
     * @param resourceType 资源类型
     * @param loader       资源加载器
     */
    public <T> void registerLoader(Class<T> resourceType, ResourceLoader<T> loader) {
        loaders.put(resourceType, loader);
    }

    /**
     * 注销资源加载器
     *
     * @param resourceType 资源类型
     */
    public void unregisterLoader(Class<?> resourceType) {
        loaders.remove(resourceType);
    }

    /**
     * 获取资源加载器
     *
     * @param resourceType 资源类型
     * @return 资源加载器，不存在返回 null
     */
    @SuppressWarnings("unchecked")
    public <T> ResourceLoader<T> getLoader(Class<T> resourceType) {
        return (ResourceLoader<T>) loaders.get(resourceType);
    }

    /**
     * 加载资源
     *
     * @param path         资源路径
     * @param resourceType 资源类型
     * @return 加载的资源对象
     * @throws IOException      加载失败时抛出
     * @throws IllegalStateException 未找到对应的加载器
     */
    @SuppressWarnings("unchecked")
    public <T> T load(String path, Class<T> resourceType) throws IOException {
        return load(path, resourceType, null);
    }

    /**
     * 加载资源
     *
     * @param path         资源路径
     * @param resourceType 资源类型
     * @param params       加载参数
     * @return 加载的资源对象
     * @throws IOException      加载失败时抛出
     * @throws IllegalStateException 未找到对应的加载器
     */
    @SuppressWarnings("unchecked")
    public <T> T load(String path, Class<T> resourceType, LoadParams params) throws IOException {
        // 检查缓存
        T cached = getCached(path, resourceType);
        if (cached != null) {
            incrementReference(path);
            return cached;
        }

        // 获取加载器
        ResourceLoader<T> loader = getLoader(resourceType);
        if (loader == null) {
            throw new IllegalStateException("No loader registered for: " + resourceType.getName());
        }

        // 加载资源
        T resource = loader.load(path, params != null ? params : new LoadParams());

        // 缓存资源
        cache(path, resource);

        return resource;
    }

    /**
     * 卸载资源
     *
     * @param path 资源路径
     */
    public void unload(String path) {
        // 减少引用计数
        int count = decrementReference(path);

        // 引用计数为 0 时卸载资源
        if (count <= 0) {
            Object resource = resourceCache.remove(path);
            referenceCounts.remove(path);

            if (resource != null) {
                // 查找对应的加载器并卸载
                for (ResourceLoader<?> loader : loaders.values()) {
                    if (loader.getResourceType().isInstance(resource)) {
                        unloadResource(loader, resource);
                        break;
                    }
                }
            }
        }
    }

    /**
     * 强制卸载资源（不考虑引用计数）
     *
     * @param path 资源路径
     */
    public void unloadForce(String path) {
        Object resource = resourceCache.remove(path);
        referenceCounts.remove(path);

        if (resource != null) {
            for (ResourceLoader<?> loader : loaders.values()) {
                if (loader.getResourceType().isInstance(resource)) {
                    unloadResource(loader, resource);
                    break;
                }
            }
        }
    }

    /**
     * 清空所有资源
     */
    public void clear() {
        for (Object resource : resourceCache.values()) {
            for (ResourceLoader<?> loader : loaders.values()) {
                if (loader.getResourceType().isInstance(resource)) {
                    unloadResource(loader, resource);
                    break;
                }
            }
        }
        resourceCache.clear();
        referenceCounts.clear();
    }

    /**
     * 获取缓存的资源
     *
     * @param path         资源路径
     * @param resourceType 资源类型
     * @return 缓存的资源，不存在返回 null
     */
    @SuppressWarnings("unchecked")
    public <T> T getCached(String path, Class<T> resourceType) {
        Object resource = resourceCache.get(path);
        if (resource != null && resourceType.isInstance(resource)) {
            return (T) resource;
        }
        return null;
    }

    /**
     * 检查资源是否已加载
     *
     * @param path 资源路径
     * @return true 如果资源已加载
     */
    public boolean isLoaded(String path) {
        return resourceCache.containsKey(path);
    }

    /**
     * 获取缓存大小
     *
     * @return 缓存的资源数量
     */
    public int getCacheSize() {
        return resourceCache.size();
    }

    /**
     * 获取最大缓存大小
     *
     * @return 最大缓存大小
     */
    public int getMaxCacheSize() {
        return maxCacheSize;
    }

    /**
     * 设置最大缓存大小
     *
     * @param maxCacheSize 最大缓存大小
     */
    public void setMaxCacheSize(int maxCacheSize) {
        this.maxCacheSize = Math.max(0, maxCacheSize);
        trimCache();
    }

    /**
     * 获取资源引用计数
     *
     * @param path 资源路径
     * @return 引用计数
     */
    public int getReferenceCount(String path) {
        Integer count = referenceCounts.get(path);
        return count != null ? count : 0;
    }

    /**
     * 增加资源引用计数
     *
     * @param path 资源路径
     */
    private void incrementReference(String path) {
        referenceCounts.merge(path, 1, Integer::sum);
    }

    /**
     * 减少资源引用计数
     *
     * @param path 资源路径
     * @return 减少后的引用计数
     */
    private int decrementReference(String path) {
        Integer count = referenceCounts.get(path);
        if (count != null && count > 0) {
            count--;
            referenceCounts.put(path, count);
            return count;
        }
        return 0;
    }

    /**
     * 缓存资源
     *
     * @param path     资源路径
     * @param resource 资源对象
     */
    private void cache(String path, Object resource) {
        // 检查缓存大小限制（在添加之前检查，所以使用 >=）
        while (resourceCache.size() >= maxCacheSize && !resourceCache.isEmpty()) {
            trimCacheOnce();
        }

        resourceCache.put(path, resource);
        referenceCounts.put(path, 1);
    }

    /**
     * 裁剪缓存中的一个资源
     */
    private void trimCacheOnce() {
        // 移除引用计数最小的资源
        String pathToRemove = null;
        int minCount = Integer.MAX_VALUE;

        for (Map.Entry<String, Integer> entry : referenceCounts.entrySet()) {
            if (entry.getValue() < minCount) {
                minCount = entry.getValue();
                pathToRemove = entry.getKey();
            }
        }

        if (pathToRemove != null) {
            unloadForce(pathToRemove);
        }
    }

    /**
     * 裁剪缓存到最大大小（用于设置 maxCacheSize 时）
     */
    private void trimCache() {
        while (resourceCache.size() > maxCacheSize && !resourceCache.isEmpty()) {
            trimCacheOnce();
        }
    }

    /**
     * 卸载资源（通用实现）
     */
    @SuppressWarnings("unchecked")
    private <T> void unloadResource(ResourceLoader<?> loader, Object resource) {
        try {
            ((ResourceLoader<T>) loader).unload((T) resource);
        } catch (Exception e) {
            // 忽略卸载错误
        }
    }
}
