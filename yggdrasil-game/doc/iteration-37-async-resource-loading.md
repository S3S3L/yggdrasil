# Iteration 37 - 异步资源加载 (2026-03-21)

## 完成状态
✅ 已完成 - 12 个测试全部通过

## 实现内容

### 核心架构

#### AsyncLoadTask<T> - 异步加载任务
- 任务 ID 自动生成（基于时间戳和线程 ID）
- 进度跟踪（0.0 - 1.0）
- 任务状态管理（PENDING, LOADING, COMPLETED, FAILED, CANCELLED）
- 取消支持（AtomicBoolean + Future.cancel）
- 错误信息存储

#### AsyncResourceLoader.LoadCallback<T> - 加载回调接口
- `onStart(String path)` - 加载开始时调用
- `onProgress(String path, float progress, String message)` - 进度更新回调
- `onComplete(String path, T resource)` - 加载完成回调
- `onError(String path, Exception exception)` - 加载失败回调

### ResourceManager 异步功能扩展

#### 异步加载方法
```java
// 基础异步加载（自动推断资源类型）
<T> AsyncLoadTask<T> loadAsync(String path, AsyncResourceLoader.LoadCallback<T> callback)

// 带参数异步加载
<T> AsyncLoadTask<T> loadAsync(String path, LoadParams params, AsyncResourceLoader.LoadCallback<T> callback)

// 指定资源类型异步加载
<T> AsyncLoadTask<T> loadAsync(String path, Class<T> resourceType, AsyncResourceLoader.LoadCallback<T> callback)

// 完整异步加载（指定类型 + 参数）
<T> AsyncLoadTask<T> loadAsync(String path, Class<T> resourceType, LoadParams params, AsyncResourceLoader.LoadCallback<T> callback)
```

#### 加载队列管理
- 使用 `ConcurrentLinkedQueue` 实现无阻塞队列
- 自动队列处理（防重入设计）
- 支持批量加载

#### 资源预加载
```java
// 预加载资源（支持进度回调）
void preloadResources(String[] paths, Class<?> resourceType, PreloadCallback callback)
void preloadResources(String[] paths, PreloadCallback callback)

// 批量加载（按优先级）
void bulkLoad(String[] paths, Class<?> resourceType, LoadPriority priority)
void bulkLoad(String[] paths, LoadPriority priority)
```

#### 线程池管理
- 默认 4 个加载线程
- 可配置线程池大小
- 支持优雅关闭（shutdown 方法）

### 特性

1. **进度回调** - 模拟加载进度（30% → 80% → 100%）
2. **缓存优化** - 异步加载已缓存资源时直接返回，不重新加载
3. **取消支持** - 可在任何时候取消加载任务
4. **错误处理** - 失败时通过回调通知
5. **优先级加载** - 支持 HIGH/NORMAL/LOW三种优先级

## 测试结果

| 测试类 | 测试数 | 状态 |
|--------|--------|------|
| AsyncResourceLoaderTest | 12 | ✅ |

### 测试覆盖

1. **testLoadAsyncSuccess** - 异步加载成功场景
2. **testLoadAsyncFailure** - 异步加载失败场景
3. **testLoadAsyncCancel** - 任务取消功能
4. **testLoadAsyncProgress** - 进度回调测试
5. **testLoadAsyncStart** - 开始回调测试
6. **testPreloadResources** - 预加载功能测试
7. **testPreloadResourcesPartialFailure** - 预加载部分失败测试
8. **testBulkLoad** - 批量加载测试
9. **testSetThreadPoolSize** - 线程池配置测试
10. **testLoadAsyncCached** - 缓存资源加载测试
11. **testShutdown** - 资源管理器关闭测试
12. **testLoadAsyncWithParams** - 带参数异步加载测试

## 代码变更

### 新增文件
- `src/main/java/io/github/s3s3l/yggdrasil/game/core/resource/AsyncLoadTask.java`
- `src/main/java/io/github/s3s3l/yggdrasil/game/core/resource/AsyncResourceLoader.java`
- `src/test/java/io/github/s3s3l/yggdrasil/game/core/resource/AsyncResourceLoaderTest.java`

### 修改文件
- `src/main/java/io/github/s3s3l/yggdrasil/game/core/resource/ResourceManager.java`
  - 添加 ExecutorService 线程池
  - 添加加载队列管理
  - 实现异步加载方法
  - 添加预加载功能
  - 添加线程池配置方法

## 使用示例

### 基础异步加载
```java
ResourceManager rm = ResourceManager.get();

rm.loadAsync("textures/player.png", Texture.class, new AsyncResourceLoader.LoadCallback<Texture>() {
    @Override
    public void onComplete(String path, Texture resource) {
        // 资源加载完成，可以使用 resource
    }

    @Override
    public void onError(String path, Exception exception) {
        // 处理加载错误
    }

    @Override
    public void onProgress(String path, float progress, String message) {
        // 更新进度条：progress (0.0 - 1.0)
    }
});
```

### 预加载资源（如游戏关卡资源）
```java
String[] levelResources = {
    "textures/bg.png",
    "textures/player.png",
    "audio/bgm.wav",
    "audio/sfx_jump.wav"
};

rm.preloadResources(levelResources, Object.class, new ResourceManager.PreloadCallback() {
    @Override
    public void onPreloadProgress(int loaded, int total) {
        // 更新总体进度：loaded / total
    }

    @Override
    public void onPreloadComplete(int successCount, int failCount) {
        // 所有资源加载完成
        System.out.println("预加载完成：成功" + successCount + ", 失败" + failCount);
    }
});
```

### 配置线程池
```java
// 根据设备性能调整线程数
ResourceManager.get().setThreadPoolSize(8);

// 游戏退出时关闭
ResourceManager.get().shutdown();
```

## Git 提交
待提交

## 下一步：Iteration 38 - 本地化系统
- 多语言文本资源管理
- 运行时语言切换
- 本地化 UI 适配
- 默认语言配置
