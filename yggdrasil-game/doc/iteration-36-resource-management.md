# Iteration 36 - 资源管理系统 (2026-03-21)

## 完成状态
✅ 已完成 - 所有目标达成，26 个新增测试全部通过

## 实现内容

### 1. 资源加载器接口
**ResourceLoader<T>** - 通用资源加载器接口
- `load(String path)` - 加载资源
- `load(String path, LoadParams params)` - 带参数加载
- `unload(T resource)` - 卸载资源
- `isLoaded(String path)` - 检查是否已加载
- `getResourceType()` - 获取支持的资源类型

**LoadParams** - 资源加载参数
- 支持字符串、整数、浮点数、布尔类型参数
- 链式调用 API
- 类型安全的参数获取

### 2. 纹理资源加载器
**TextureResource** - 纹理资源类
- 路径、宽度、高度属性
- 底层纹理对象封装
- 加载状态管理

**TextureLoader** - 纹理加载器
- 支持从类路径或文件系统加载
- 纹理参数配置（过滤、环绕、预乘 alpha）
- 使用 Java AWT BufferedImage 作为底层实现

**TextureParams**:
- `filter`: nearest, linear, mipmap
- `wrapS/wrapT`: clamp, repeat, mirroredRepeat
- `premultiplyAlpha`: 是否预乘 alpha

### 3. 音频资源加载器
**AudioResource** - 音频资源类
- 路径、类型、音频数据
- 音效 (SOUND) 和音乐 (MUSIC) 两种类型
- 加载状态管理

**AudioLoader** - 音频加载器
- 支持从类路径或文件系统加载
- 自动识别音频类型（通过路径或参数）
- 音频参数配置（音量、循环、优先级）

**AudioParams**:
- `volume`: 0.0 - 1.0
- `looping`: 是否循环播放
- `priority`: 播放优先级

### 4. 资源缓存管理
**ResourceManager** - 资源管理器（单例）
- 统一的资源加载接口
- 资源缓存和引用计数
- 自动内存管理（LRU 裁剪）
- 最大缓存大小限制

**核心功能**:
- `load()`: 加载资源（自动缓存）
- `unload()`: 卸载资源（引用计数为 0 时）
- `unloadForce()`: 强制卸载
- `clear()`: 清空所有资源
- `getCached()`: 获取缓存资源
- `getReferenceCount()`: 获取引用计数

**缓存策略**:
- 基于引用计数的自动管理
- 超过最大缓存时 LRU 裁剪
- 默认最大缓存 1000 个资源

## 新增文件

### 核心类
- `core/resource/ResourceLoader.java` - 资源加载器接口
- `core/resource/LoadParams.java` - 加载参数
- `core/resource/ResourceManager.java` - 资源管理器
- `core/resource/TextureResource.java` - 纹理资源
- `core/resource/TextureLoader.java` - 纹理加载器
- `core/resource/AudioResource.java` - 音频资源
- `core/resource/AudioLoader.java` - 音频加载器

### 测试类
- `core/test/resource/LoadParamsTest.java` - 14 测试
- `core/test/resource/ResourceManagerTest.java` - 14 测试
- `core/test/resource/TextureLoaderTest.java` - 6 测试
- `core/test/resource/AudioLoaderTest.java` - 7 测试

## 测试结果

| 测试类 | 测试数 | 状态 |
|--------|--------|------|
| LoadParamsTest | 14 | ✅ |
| ResourceManagerTest | 14 | ✅ |
| TextureLoaderTest | 6 | ✅ |
| AudioLoaderTest | 7 | ✅ |
| **总计** | **41** | **✅** |

## 测试覆盖

### LoadParamsTest (14 测试)
- testSetAndGet - 基本设置获取
- testGetString - 字符串参数
- testGetInt - 整数参数
- testGetFloat - 浮点数参数
- testGetBoolean - 布尔参数
- testHas - 检查参数存在
- testGetAll - 获取所有参数
- testClear - 清空参数
- testNullHandling - 空值处理
- testInvalidIntFormat - 无效整数格式
- testInvalidFloatFormat - 无效浮点格式

### ResourceManagerTest (14 测试)
- testRegisterLoader - 注册加载器
- testUnregisterLoader - 注销加载器
- testLoad - 加载资源
- testLoadWithParams - 带参数加载
- testLoadNotFound - 加载不存在的资源
- testUnload - 卸载资源
- testUnloadWithReference - 引用计数卸载
- testUnloadForce - 强制卸载
- testClear - 清空所有资源
- testIsLoaded - 检查是否已加载
- testGetCacheSize - 获取缓存大小
- testMaxCacheSize - 最大缓存大小
- testReferenceCount - 引用计数
- testGetCached - 获取缓存资源

### TextureLoaderTest (6 测试)
- testConstructor - 构造函数
- testTextureParams - 纹理参数默认值
- testSetTextureParams - 设置纹理参数
- testIsLoaded - 检查加载状态
- testLoadParamsHandling - 加载参数处理

### AudioLoaderTest (7 测试)
- testConstructor - 构造函数
- testAudioParams - 音频参数默认值
- testSetAudioParams - 设置音频参数
- testIsLoaded - 检查加载状态
- testGetAudioTypeFromParams - 从参数获取类型
- testGetAudioTypeFromPath - 从路径获取类型
- testAudioType - 音频类型枚举

## 设计决策

### 1. 单例模式
**决策**: ResourceManager 使用单例模式

**原因**:
- 全局统一的资源管理
- 避免重复加载同一资源
- 集中管理资源缓存和内存

### 2. 引用计数
**决策**: 使用引用计数管理资源生命周期

**原因**:
- 支持多个模块共享同一资源
- 自动管理资源卸载时机
- 避免过早卸载仍在使用的资源

### 3. LRU 缓存裁剪
**决策**: 使用引用计数最小的优先移除（类似 LRU）

**原因**:
- 简单高效的实现
- 优先保留常用资源
- 自动释放不常用资源

### 4. 泛型加载器
**决策**: 使用泛型接口 `ResourceLoader<T>`

**原因**:
- 类型安全的资源加载
- 支持扩展新的资源类型
- 统一的加载器注册机制

## 使用示例

```java
// 获取资源管理器
ResourceManager rm = ResourceManager.get();

// 注册加载器
rm.registerLoader(TextureResource.class, new TextureLoader());
rm.registerLoader(AudioResource.class, new AudioLoader());

// 加载纹理
TextureResource texture = rm.load("textures/player.png", TextureResource.class);

// 加载音频（带参数）
LoadParams params = new LoadParams();
params.set("type", "music");
AudioResource music = rm.load("audio/bgm.ogg", AudioResource.class, params);

// 使用资源（多次加载返回同一缓存）
TextureResource cached = rm.getCached("textures/player.png", TextureResource.class);

// 卸载资源（引用计数为 0 时自动释放）
rm.unload("textures/player.png");
```

## 下一步：Iteration 37 - 异步资源加载
- 异步加载接口
- 加载进度回调
- 加载队列管理
- 资源预加载

## Git 提交
待提交
