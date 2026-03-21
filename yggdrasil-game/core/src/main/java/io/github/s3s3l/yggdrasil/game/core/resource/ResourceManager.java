package io.github.s3s3l.yggdrasil.game.core.resource;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
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
     * 异步加载线程池
     */
    private ExecutorService executorService;

    /**
     * 加载队列
     */
    private final Queue<AsyncLoadTask<?>> loadQueue;

    /**
     * 是否正在处理加载队列
     */
    private volatile boolean processingQueue;

    /**
     * 私有构造函数（单例）
     */
    private ResourceManager() {
        this.loaders = new HashMap<>();
        this.resourceCache = new HashMap<>();
        this.referenceCounts = new HashMap<>();
        this.maxCacheSize = 1000; // 默认最多缓存 1000 个资源
        this.currentCacheSize = new AtomicLong(0);
        this.executorService = Executors.newFixedThreadPool(4); // 默认 4 个加载线程
        this.loadQueue = new ConcurrentLinkedQueue<>();
        this.processingQueue = false;
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

    // ==================== 异步加载方法实现 ====================

    /**
     * 异步加载资源
     *
     * @param path         资源路径
     * @param resourceType 资源类型
     * @param callback     加载完成回调
     * @return 加载任务
     */
    @SuppressWarnings("unchecked")
    public <T> AsyncLoadTask<T> loadAsync(String path, Class<T> resourceType, AsyncResourceLoader.LoadCallback<T> callback) {
        return loadAsync(path, resourceType, null, callback);
    }

    /**
     * 异步加载资源
     *
     * @param path         资源路径
     * @param resourceType 资源类型
     * @param params       加载参数
     * @param callback     加载完成回调
     * @return 加载任务
     */
    @SuppressWarnings("unchecked")
    public <T> AsyncLoadTask<T> loadAsync(String path, Class<T> resourceType, LoadParams params, AsyncResourceLoader.LoadCallback<T> callback) {
        // 创建异步加载任务
        AsyncLoadTask<T> task = new AsyncLoadTask<>(path, resourceType);

        // 添加到加载队列
        loadQueue.offer(task);

        // 启动队列处理
        processLoadQueue();

        // 异步执行加载
        executeAsyncLoad(task, params, callback);

        return task;
    }

    /**
     * 异步加载资源（不指定资源类型，根据路径推断）
     *
     * @param path     资源路径
     * @param callback 加载完成回调
     * @return 加载任务
     */
    @SuppressWarnings("unchecked")
    public <T> AsyncLoadTask<T> loadAsync(String path, AsyncResourceLoader.LoadCallback<T> callback) {
        return loadAsync(path, (Class<T>) Object.class, null, callback);
    }

    /**
     * 异步加载资源（不指定资源类型，根据路径推断）
     *
     * @param path     资源路径
     * @param params   加载参数
     * @param callback 加载完成回调
     * @return 加载任务
     */
    @SuppressWarnings("unchecked")
    public <T> AsyncLoadTask<T> loadAsync(String path, LoadParams params, AsyncResourceLoader.LoadCallback<T> callback) {
        return loadAsync(path, (Class<T>) Object.class, params, callback);
    }

    /**
     * 执行异步加载
     */
    @SuppressWarnings("unchecked")
    private <T> void executeAsyncLoad(AsyncLoadTask<T> task, LoadParams params, AsyncResourceLoader.LoadCallback<T> callback) {
        executorService.submit(() -> {
            try {
                // 检查是否取消
                if (task.isCancelled()) {
                    return;
                }

                // 更新状态为加载中
                task.setStatus(AsyncLoadTask.TaskStatus.LOADING);
                task.setProgress(0.1f);

                // 通知开始
                if (callback != null) {
                    callback.onStart(task.getPath());
                }

                // 检查缓存
                T cached = getCached(task.getPath(), task.getResourceType());
                if (cached != null) {
                    incrementReference(task.getPath());
                    task.setResult(cached);
                    task.setProgress(1.0f);
                    task.setStatus(AsyncLoadTask.TaskStatus.COMPLETED);

                    if (callback != null) {
                        callback.onComplete(task.getPath(), cached);
                    }
                    return;
                }

                // 获取加载器
                ResourceLoader<T> loader = getLoader(task.getResourceType());
                if (loader == null) {
                    Exception error = new IllegalStateException("No loader registered for: " + task.getResourceType().getName());
                    task.setError(error);
                    task.setStatus(AsyncLoadTask.TaskStatus.FAILED);

                    if (callback != null) {
                        callback.onError(task.getPath(), error);
                    }
                    return;
                }

                // 模拟加载进度（对于大文件）
                task.setProgress(0.3f);
                if (callback != null) {
                    callback.onProgress(task.getPath(), 0.3f, "Loading resource...");
                }

                // 加载资源
                T resource = loader.load(task.getPath(), params != null ? params : new LoadParams());

                // 检查是否取消
                if (task.isCancelled()) {
                    loader.unload(resource);
                    return;
                }

                task.setProgress(0.8f);
                if (callback != null) {
                    callback.onProgress(task.getPath(), 0.8f, "Caching resource...");
                }

                // 缓存资源
                cache(task.getPath(), resource);
                task.setResult(resource);

                task.setProgress(1.0f);
                task.setStatus(AsyncLoadTask.TaskStatus.COMPLETED);

                // 通知完成
                if (callback != null) {
                    callback.onComplete(task.getPath(), resource);
                }
            } catch (Exception e) {
                task.setError(e);
                task.setStatus(AsyncLoadTask.TaskStatus.FAILED);

                if (callback != null) {
                    callback.onError(task.getPath(), e);
                }
            }
        });
    }

    /**
     * 处理加载队列
     */
    private void processLoadQueue() {
        if (processingQueue || loadQueue.isEmpty()) {
            return;
        }

        synchronized (this) {
            if (processingQueue) {
                return;
            }
            processingQueue = true;
        }

        executorService.submit(() -> {
            while (!loadQueue.isEmpty()) {
                AsyncLoadTask<?> task = loadQueue.poll();
                if (task != null && !task.isDone()) {
                    // 任务已在 executeAsyncLoad 中处理
                }
            }
            processingQueue = false;
        });
    }

    /**
     * 获取资源类型（根据路径推断）
     */
    private Class<?> getResourceType(String path) {
        if (path.endsWith(".png") || path.endsWith(".jpg") || path.endsWith(".jpeg")) {
            return Object.class; // 纹理资源
        } else if (path.endsWith(".wav") || path.endsWith(".mp3") || path.endsWith(".ogg")) {
            return Object.class; // 音频资源
        }
        return Object.class; // 默认
    }

    /**
     * 设置异步加载线程池大小
     *
     * @param threadCount 线程数量
     */
    public void setThreadPoolSize(int threadCount) {
        if (threadCount <= 0) {
            throw new IllegalArgumentException("Thread count must be positive");
        }

        if (executorService != null) {
            executorService.shutdown();
        }
        this.executorService = Executors.newFixedThreadPool(threadCount);
    }

    /**
     * 预加载资源
     *
     * @param paths      资源路径列表
     * @param resourceType 资源类型
     * @param callback   加载完成回调（所有资源加载完成后调用）
     */
    @SuppressWarnings("unchecked")
    public void preloadResources(String[] paths, Class<?> resourceType, PreloadCallback callback) {
        if (paths == null || paths.length == 0) {
            if (callback != null) {
                callback.onPreloadComplete(0, 0);
            }
            return;
        }

        final int total = paths.length;
        final int[] completed = {0};
        final int[] failed = {0};

        for (String path : paths) {
            loadAsync(path, (Class<Object>) resourceType, new AsyncResourceLoader.LoadCallback<Object>() {
                @Override
                public void onComplete(String path, Object resource) {
                    synchronized (completed) {
                        completed[0]++;
                        int progress = (int) ((completed[0] * 100L) / total);
                        if (callback != null) {
                            callback.onPreloadProgress(completed[0], total);
                        }
                        if (completed[0] + failed[0] >= total) {
                            if (callback != null) {
                                callback.onPreloadComplete(completed[0], failed[0]);
                            }
                        }
                    }
                }

                @Override
                public void onError(String path, Exception exception) {
                    synchronized (failed) {
                        failed[0]++;
                        if (completed[0] + failed[0] >= total) {
                            if (callback != null) {
                                callback.onPreloadComplete(completed[0], failed[0]);
                            }
                        }
                    }
                }
            });
        }
    }

    /**
     * 预加载资源（不指定资源类型）
     *
     * @param paths    资源路径列表
     * @param callback 加载完成回调
     */
    public void preloadResources(String[] paths, PreloadCallback callback) {
        preloadResources(paths, Object.class, callback);
    }

    /**
     * 批量加载资源
     *
     * @param paths        资源路径列表
     * @param resourceType 资源类型
     * @param priority     优先级（HIGH, NORMAL, LOW）
     */
    public void bulkLoad(String[] paths, Class<?> resourceType, LoadPriority priority) {
        if (paths == null || paths.length == 0) {
            return;
        }

        // 根据优先级添加到队列
        for (String path : paths) {
            loadAsync(path, resourceType, (LoadParams) null, null);
        }
    }

    /**
     * 批量加载资源（不指定资源类型）
     *
     * @param paths    资源路径列表
     * @param priority 优先级（HIGH, NORMAL, LOW）
     */
    public void bulkLoad(String[] paths, LoadPriority priority) {
        bulkLoad(paths, Object.class, priority);
    }

    /**
     * 关闭资源管理器
     * 释放所有资源并关闭线程池
     */
    public void shutdown() {
        clear();
        if (executorService != null) {
            executorService.shutdown();
            executorService = null;
        }
    }

    /**
     * 预加载回调接口
     */
    public interface PreloadCallback {
        /**
         * 预加载进度更新
         *
         * @param loaded 已加载数量
         * @param total  总数量
         */
        void onPreloadProgress(int loaded, int total);

        /**
         * 预加载完成
         *
         * @param successCount 成功加载数量
         * @param failCount    失败数量
         */
        void onPreloadComplete(int successCount, int failCount);
    }

    /**
     * 加载优先级枚举
     */
    public enum LoadPriority {
        /**
         * 高优先级
         */
        HIGH,
        /**
         * 普通优先级
         */
        NORMAL,
        /**
         * 低优先级
         */
        LOW
    }
}
