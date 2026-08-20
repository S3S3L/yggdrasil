# yggdrasil-game 优化方案

基于对 `figures/` 流程图与实际代码的对比分析，整理出以下问题与优化点。按优先级排列，P0 为需立即修复的 Bug，P1 为重要优化，P2 为改善性优化。

---

## 实施状态（2026-08，UI/绘制/输入迭代）

`core/layout` 已从独立草稿接入后端无关的 `core/ui`：`UiScene` 在既有
`calculate → preRender → render` 生命周期中计算布局并生成不可变 draw-command
快照。`UiNode` 继承 `LayoutNode`，因此布局结果同时服务于绘制与命中测试；同 z
节点遵循插入顺序。`gdx/ui` 只负责将 LibGDX 输入和绘制资源适配到该核心协议。

本迭代还修正了 `display: none` 子节点仍可能获得尺寸的问题，并为 layout、事件传播、
modal/focus、裁剪、边界命中和 HiDPI 坐标映射补上无窗口测试。设计边界及前端接入方式
见 [UI 架构](ui-architecture.md)。这不会改变 `GameObjectManager` 现有的渲染排序语义；
后者的 P2 #7 仍应独立处理。

---

## P0：必须修复的 Bug

### 1. `doPreRender()` 未接入标准帧循环（严重）

**问题**

`AbstractScene.doRender()` 调用了 `gameObjectManager.doRender()`，但遗漏了 `gameObjectManager.doPreRender()`。`doPreRender()` 只在 `GameObjectManager.doRender(float delta)` 这个重载方法内被调用，而该重载方法在标准帧循环中从未使用。

结果：整个 preRender 优化机制（并行预计算 dirty 根对象）完全失效。

**涉及文件**

- `core/src/main/java/io/github/s3s3l/yggdrasil/game/core/scene/AbstractScene.java`
- `core/src/main/java/io/github/s3s3l/yggdrasil/game/core/object/GameObjectManager.java`

**修复**

```java
// AbstractScene.java
@Override
protected void doRender() {
    gameObjectManager.events().flush();
    gameObjectManager.doPreRender();  // 补上这一行
    gameObjectManager.doRender();
}
```

---

### 2. `dirtyRoots` 每帧增长，永不清空（内存泄漏）

**问题**

`dirtyRoots.clear()` 只在 `GameObjectManager.doPreRender()` 末尾调用。由于问题 1 导致 `doPreRender()` 从未执行，`dirtyRoots` 持有 `GameObject` 强引用并随每帧累积，对象无法被 GC 回收。

修复问题 1 后此问题自动消除。

---

### 3. Scene 对象 ID 被 `IdGenerator` 生成两次

**问题**

`AbstractGameAtom` 构造器已经调用 `IdGenerator.nextId()` 生成 ID，但 `AbstractSceneManager.createScene()` 随后又调用一次覆盖掉：

```java
// AbstractGameAtom 构造器
this.id = IdGenerator.nextId();      // 第一次（被覆盖，ID 浪费）

// AbstractSceneManager.createScene()
scene.setId(IdGenerator.nextId());   // 第二次（实际生效）
```

每次创建 Scene 浪费一个 ID，若 `IdGenerator` 有单调递增语义则会产生空洞。

**涉及文件**

- `core/src/main/java/io/github/s3s3l/yggdrasil/game/core/scene/AbstractSceneManager.java`

**修复**

删除 `createScene()` 中的 `scene.setId(IdGenerator.nextId())` 这一行，直接使用构造器里已生成的 ID：

```java
@Override
public synchronized T createScene(Class<? extends T> sceneClass, SceneConfig config) {
    try {
        T scene = sceneClass.getDeclaredConstructor(SceneConfig.class).newInstance(config);
        // 删除: scene.setId(IdGenerator.nextId());
        scenes.put(scene.getId(), scene);
        return scene;
    } catch (Exception e) {
        throw new RuntimeException("Failed to create scene instance", e);
    }
}
```

---

## P1：重要优化

### 4. `doCalculate()` 的潜在死锁风险

**问题**

`doCalculate()` 是 `synchronized` 方法，内部通过 `calculateExecutor.invokeAll()` 提交并行任务。若任何 `GameObject.calculate()` 实现（或其递归子对象的 `calculate()`）中调用了 `GameObjectManager` 的 `synchronized` 方法（`create`、`release`、`active`、`getActive`），将立即死锁——外层 `doCalculate()` 持有锁，内层无法获得锁。

**建议**

方案 A：文档约束——在 `doCalculate()` 的 Javadoc 中明确禁止 calculate 回调里调用 `GameObjectManager` 的任何方法，由调用方保证。

方案 B（彻底解决）：用读写分离替代单一 `synchronized`，calculate 阶段对 `activeObjs` 做快照副本，任务在副本上运行，完成后再合并结果。

---

### 5. `releasedObjs` 中失效的 `SoftReference` 不清理

**问题**

`releasedObjs` 是 `Map<Long, SoftReference<GameObject>>`。GC 回收对象后 `SoftReference.get()` 返回 null，但对应 Map Entry 永远不会被移除，导致 Map 随对象 release 操作持续增长。

**涉及文件**

- `core/src/main/java/io/github/s3s3l/yggdrasil/game/core/object/GameObjectManager.java`

**修复**

在 `release()` 方法或合适的周期性时机加入清理：

```java
public synchronized void release(GameObject obj) {
    if (obj == null) return;
    obj.setEnable(false);
    activeObjs.remove(obj.getId());
    releasedObjs.put(obj.getId(), new SoftReference<>(obj));
    // 顺手清理已被 GC 的条目
    releasedObjs.entrySet().removeIf(e -> e.getValue().get() == null);
}
```

---

### 6. `doCalculate()` 每帧产生多余的中间集合

**问题**

当前实现每帧创建两个中间集合：

```java
calculateExecutor.invokeAll(
    activeObjs.values().stream()
        .filter(...)
        .map(...)
        .toList()          // 第一次收集 → List
).stream()
 .map(...)
 .filter(...)
 .collect(Collectors.toSet())  // 第二次收集 → Set
```

对于对象数量多的场景，每帧的临时对象分配压力不可忽视。

**建议**

预分配复用集合，或将两个 stream 合并为一次遍历：

```java
List<Callable<GameObject>> tasks = new ArrayList<>(activeObjs.size());
for (GameObject obj : activeObjs.values()) {
    if (obj.isEnable()) {
        tasks.add(() -> { obj.calculate(delta); return obj; });
    }
}
List<Future<GameObject>> futures = calculateExecutor.invokeAll(tasks);
for (Future<GameObject> f : futures) {
    GameObject obj = f.get();
    if (obj != null && obj.isDirty()) {
        // dirty root 逻辑
    }
}
```

---

## P2：改善性优化

### 7. `activeObjs` 使用 `HashMap`，渲染顺序不确定

**问题**

`doRender()` 遍历 `activeObjs.values()`，`HashMap` 无顺序保证，渲染顺序依赖 JVM hash 实现，不同 JVM 版本或不同 Map 容量下可能不同，会导致对象遮挡关系无法预测。

**建议**

改用 `LinkedHashMap` 保证插入顺序，或为 `GameObject` 增加 `zIndex` 字段，在 `doRender()` 中按 `zIndex` 排序后渲染。

---

### 8. 静态线程池在多 `GameObjectManager` 实例间共享

**问题**

`calculateExecutor` 和 `preRenderExecutor` 是 `static` 字段，所有 `GameObjectManager` 实例（对应所有 Scene）共享同一线程池。虽然实际上同一时刻只有一个活跃 Scene，但如果未来支持同时存在多个活跃 Scene（如分屏），各 Scene 的计算任务会相互竞争。

**建议**

目前影响可控，可维持现状并加注释说明设计意图。若未来需要多 Scene 并行，改为实例字段。

---

## 修复优先级汇总

| # | 问题 | 优先级 | 影响 | 改动量 |
|---|---|---|---|---|
| 1 | `doPreRender()` 未接入帧循环 | P0 | preRender 完全失效 | 1 行 |
| 2 | `dirtyRoots` 内存泄漏 | P0 | 随帧线性增长 | 随修复 #1 消除 |
| 3 | Scene 双次 ID 生成 | P0 | ID 空洞 | 1 行删除 |
| 4 | `doCalculate()` 死锁风险 | P1 | 潜在挂死 | 文档或重构 |
| 5 | `releasedObjs` 不清理 | P1 | 长期内存泄漏 | 3 行 |
| 6 | `doCalculate()` 中间集合 | P1 | 每帧 GC 压力 | 中等 |
| 7 | 渲染顺序不确定 | P2 | 遮挡行为不可预测 | 小 |
| 8 | 静态线程池共享 | P2 | 未来扩展风险 | 低优先 |
