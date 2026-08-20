# yggdrasil-game 详细优化文档

> 本文档是 `optimization-plan.md` 的深化版本。在原有 P0–P2 清单基础上，补充了**代码级验证**（精确到文件/行号）、**根因分析**、**可直接套用的修复**、**验证方法**，并新增了 **layout 布局引擎专项** 与 **测试/质量缺口** 两大章节。
>
> 分析基准：commit `f9bcdce`（feat(game): layout type define），2026-06。
> 模块结构：`core`（抽象层）/ `2d`（2D 几何·碰撞·事件实现）/ `gdx`（LibGDX 具体实现）。

---

## 0. 问题总览

| #  | 问题 | 优先级 | 影响 | 改动量 | 验证状态 |
|----|------|:------:|------|:------:|:--------:|
| 1  | `doPreRender()` 未接入标准帧循环 | **P0** | preRender 优化完全失效 | 1 行 | ✅ 已验证 |
| 2  | `dirtyRoots` 逐帧累积，永不清空 | **P0** | 内存泄漏（线性增长） | 随 #1 消除 | ✅ 已验证 |
| 3  | Scene 对象 ID 被生成两次 | **P0** | ID 浪费 / 空洞 | 1 行删除 | ✅ 已验证 |
| 4  | `doCalculate()` 并行 + synchronized 死锁风险 | P1 | 潜在挂死 | 文档约束 / 重构 | ✅ 已验证 |
| 5  | `releasedObjs` 失效 SoftReference 不清理 | P1 | 长期内存泄漏 | 3 行 | ✅ 已验证 |
| 6  | `doCalculate()` 每帧多余中间集合 | P1 | 每帧 GC 压力 | 中等 | ✅ 已验证 |
| 7  | `activeObjs` 用 `HashMap`，渲染顺序不确定 | P2 | 遮挡关系不可预测 | 小 | ✅ 已验证 |
| 8  | 静态线程池在多实例间共享 | P2 | 未来分屏扩展风险 | 低 | ✅ 已验证 |
| 9  | layout 引擎未集成、无测试 | P1 | UI 路线被阻塞 | 大（设计） | ✅ 已验证 |
| 10 | layout 引擎「简化版」特性缺口 | P2 | 复杂布局不可用 | 中–大 | ✅ 已验证 |
| 11 | 整体单元测试覆盖严重不足 | P1 | 回归风险高 | 持续 | ✅ 已验证 |

---

## 1. P0 — 必须修复的 Bug

### 1.1 `doPreRender()` 未接入标准帧循环（严重）

**现象 / 根因**

标准每帧流程为 `GdxScene.render(delta)` → `AbstractScene.doCalculate(delta)` + `AbstractScene.doRender()`。但 `AbstractScene.doRender()` 只调用了 `doRender()`，**遗漏了 `doPreRender()`**：

```java
// core/.../scene/AbstractScene.java:19-24  （现状）
@Override
protected void doRender() {
    gameObjectManager.events().flush();
    gameObjectManager.doRender();        // ← 只有 render，没有 preRender
}
```

而 `doPreRender()` 唯一的调用点是 `GameObjectManager.doRender(float delta)` 这个**重载方法**：

```java
// core/.../object/GameObjectManager.java:225-229  （从未被标准循环调用）
public synchronized void doRender(float delta) {
    doCalculate(delta);
    doPreRender();
    doRender();
}
```

由于标准循环走的是 `AbstractScene` 的 `doCalculate` + 无参 `doRender`，上面这个三合一重载在运行时**从不执行**。

**后果**：整个「脏对象并行预渲染」机制（`doCalculate` 计算出的 `dirtyRoots` → `doPreRender` 并行预渲染）完全失效。`doCalculate` 仍在累积 `dirtyRoots`，但没有任何环节消费它。

**修复**

```java
// core/.../scene/AbstractScene.java
@Override
protected void doRender() {
    gameObjectManager.events().flush();
    gameObjectManager.doPreRender();   // ← 补上
    gameObjectManager.doRender();
}
```

> 说明：`doCalculate` 已由 `AbstractScene.doCalculate(delta)` 在帧循环前半段独立调用，因此这里**不应**再用 `doRender(float delta)` 重载（会导致 `doCalculate` 一帧跑两次）。正确做法是单独补 `doPreRender()`。
>
> 建议顺带处理 `GameObjectManager.doRender(float delta)` 这个易误用的重载：要么删除，要么加 `@Deprecated` 与注释，避免后续有人错误接入导致 `doCalculate` 重复执行。

**验证方法**

- 单测：构造一个会在 `calculate` 中 `markDirty()` 的 GameObject，跑一帧后断言其 `preRender()` 被调用、且 `dirtyRoots` 在帧末为空。
- 运行：在 `preRender()` 打点，确认每帧脏对象数量与预期一致。

---

### 1.2 `dirtyRoots` 逐帧累积，永不清空（内存泄漏）

**根因**

`dirtyRoots` 是 `Set<GameObject>`（持**强引用**）。它仅在 `doPreRender()` 末尾被清空：

```java
// core/.../object/GameObjectManager.java:200
dirtyRoots.clear();
```

因 #1 导致 `doPreRender()` 从不执行，`doCalculate()` 每帧把新的脏根对象 `add` 进去（`GameObjectManager.java:163-165`），却永远不清。即使对象逻辑上已 `release`，`dirtyRoots` 仍持有其强引用，使其无法被 GC 回收。

**后果**：内存随帧数 / 脏对象数线性增长；同时 `release` 后的对象因被强引用而泄漏。

**修复**：修复 #1 后本问题自动消除（`doPreRender()` 恢复执行 → 每帧末 `clear()`）。

**加固建议**：在 `release()` / `releaseAll()` 中同步从 `dirtyRoots` 移除对象，避免「已释放对象在本帧仍残留于 dirtyRoots」的窗口期。

**验证方法**：连续跑 N 帧后断言 `dirtyRoots.size()` 不随 N 单调增长（修复后应在每帧末归零）。

---

### 1.3 Scene 对象 ID 被 `IdGenerator` 生成两次

**根因**

`AbstractGameAtom` 构造器已生成 ID：

```java
// core/.../basic/AbstractGameAtom.java:38
this.id = IdGenerator.nextId();
```

而 `AbstractSceneManager.createScene()` 紧接着又覆盖一次：

```java
// core/.../scene/AbstractSceneManager.java:20-29
@Override
public synchronized T createScene(Class<? extends T> sceneClass, SceneConfig config) {
    try {
        T scene = sceneClass.getDeclaredConstructor(SceneConfig.class).newInstance(config);
        scene.setId(IdGenerator.nextId());   // ← 第二次，覆盖构造器生成的 ID
        scenes.put(scene.getId(), scene);
        return scene;
    } catch (Exception e) {
        throw new RuntimeException("Failed to create scene instance", e);
    }
}
```

**后果**：每创建一个 Scene 浪费一个 ID；若 `IdGenerator` 为单调递增语义，会在 ID 序列中产生空洞，影响任何依赖 ID 连续性的诊断/快照逻辑。

**修复**：删除 `createScene()` 中的 `scene.setId(IdGenerator.nextId());`，直接使用构造器已生成的 ID。

```java
@Override
public synchronized T createScene(Class<? extends T> sceneClass, SceneConfig config) {
    try {
        T scene = sceneClass.getDeclaredConstructor(SceneConfig.class).newInstance(config);
        scenes.put(scene.getId(), scene);   // 直接用构造器里的 id
        return scene;
    } catch (Exception e) {
        throw new RuntimeException("Failed to create scene instance", e);
    }
}
```

**验证方法**：创建两个 Scene，断言其 ID 与 `IdGenerator` 连续输出一致（中间无跳号）。

---

## 2. P1 — 重要优化

### 2.1 `doCalculate()` 并行 + `synchronized` 死锁风险

**根因**

`doCalculate()` 是 `synchronized` 实例方法（`GameObjectManager.java:131`），内部用 `calculateExecutor.invokeAll(...)`（`:133`）把每个对象的 `calculate(delta)` 派发到工作线程，并阻塞等待全部完成。

若任何 `GameObject.calculate()`（或其递归子对象的 `calculate()`）中调用了 `GameObjectManager` 的其它 `synchronized` 方法——`create` / `release` / `active` / `getActive` / `activeObjects`——将立即死锁：

- 帧线程持有 `GameObjectManager` 的监视器锁，阻塞在 `invokeAll` 等待 worker；
- worker 线程在 `calculate` 中试图进入 `synchronized` 方法，但锁被帧线程持有，永远拿不到；
- 帧线程因 worker 不结束而永不释放锁 → 互相等待，挂死。

**建议**

- **方案 A（低成本，先行）**：在 `doCalculate()` 与 `calculate()` 的 Javadoc 中明确约束「`calculate` 回调内禁止调用 `GameObjectManager` 的任何方法」，由调用方保证。提供「延迟操作队列」让用户把 create/release 意图入队，帧末统一执行。
- **方案 B（彻底）**：读写分离。calculate 阶段先对 `activeObjs` 做快照副本，并行任务在副本上运行；create/release 在并行期间写入待合并缓冲，`invokeAll` 返回后再单线程合并。如此 `calculate` 期间无需持有全局锁。

**验证方法**：写一个在 `calculate` 中调用 `getActive()` 的对象，加超时断言（修复前会超时/死锁，修复后正常返回）。

---

### 2.2 `releasedObjs` 中失效的 `SoftReference` 不清理

**根因**

`releasedObjs` 是 `Map<Long, SoftReference<GameObject>>`（`GameObjectManager.java:41`）。`release()`（`:73-80`）只往里 `put`，从不清理。GC 回收对象后 `SoftReference.get()` 返回 `null`，但对应的 Map Entry 永久驻留，使 Map 随 release 次数持续增长（key 与空壳 SoftReference 泄漏）。

**修复**：在 `release()` 顺手清理已失效条目（或引入 `ReferenceQueue` 做精确回收）。

```java
public synchronized void release(GameObject obj) {
    if (obj == null) {
        return;
    }
    obj.setEnable(false);
    activeObjs.remove(obj.getId());
    dirtyRoots.remove(obj);                                   // 顺带处理 #1.2
    releasedObjs.put(obj.getId(), new SoftReference<>(obj));
    releasedObjs.entrySet().removeIf(e -> e.getValue().get() == null);  // 清理已被 GC 的条目
}
```

> 更稳妥的做法是用 `ReferenceQueue<GameObject>` 注册每个 SoftReference，在合适时机 `poll` 出已回收引用并按记录的 id 精确删除，避免每次 `release` 全表扫描。

**验证方法**：release 大量对象 → 触发 GC → 断言 `releasedObjs` 中无 `get()==null` 的条目。

---

### 2.3 `doCalculate()` 每帧产生多余中间集合

**根因**

当前实现每帧创建两个临时集合（`GameObjectManager.java:133-148`）：先 `.toList()` 收集 `Callable`，再对 Future 流 `.collect(Collectors.toSet())` 收集脏对象。对象多时，每帧临时分配压力不可忽视。

**修复**：合并遍历 + 预分配复用。

```java
List<Callable<GameObject>> tasks = new ArrayList<>(activeObjs.size());
for (GameObject obj : activeObjs.values()) {
    if (obj.isEnable()) {
        tasks.add(() -> { obj.calculate(delta); return obj; });
    }
}
List<Future<GameObject>> futures = calculateExecutor.invokeAll(tasks);
for (Future<GameObject> f : futures) {
    GameObject obj = f.get();   // 注意 InterruptedException/ExecutionException 处理
    if (obj != null && obj.isDirty()) {
        // dirty root 归并逻辑
    }
}
```

**验证方法**：用 JMH 或简单基准对比修复前后每帧分配量 / 帧耗时（在 1k+ 对象场景下差异明显）。

---

## 3. P2 — 改善性优化

### 3.1 `activeObjs` 使用 `HashMap`，渲染顺序不确定

**根因**：`doRender()` 遍历 `activeObjs.values()`（`GameObjectManager.java:211`），`HashMap` 无顺序保证，渲染顺序依赖 JVM hash 实现与容量，跨版本/容量可能变化 → 对象遮挡关系（绘制先后）不可预测。

**建议**：

- 短期：改 `activeObjs` 为 `LinkedHashMap`，保证按插入顺序渲染（注意 `synchronized` 已保护，无并发结构修改问题）。
- 长期：为 `GameObject` 增加 `zIndex` 字段，`doRender()` 按 `zIndex` 稳定排序后渲染，提供显式层级控制（UI 组件章节会依赖它）。

---

### 3.2 静态线程池在多 `GameObjectManager` 实例间共享

**根因**：`calculateExecutor` / `preRenderExecutor` 为 `static`（`GameObjectManager.java:22,31`），所有 `GameObjectManager`（即所有 Scene）共享同一对线程池。当前同一时刻只有一个活跃 Scene，影响可控；但若未来支持多活跃 Scene（分屏 / 后台场景预热），各 Scene 计算任务会争用线程。

**建议**：当前可维持现状并补注释说明设计意图（单活跃 Scene 假设）。规划多 Scene 并行时，改为实例字段并在 Scene 销毁时 `shutdown`，避免线程泄漏。

---

## 4. layout 布局引擎专项

> `core/layout` 是 commit `f9bcdce` 新增的「CSS 风格布局引擎」（约 40 文件），定位为后续 UI 组件系统（按钮/输入框/列表…）的底层。入口 `LayoutEngine.layout(LayoutNode root, float vpW, float vpH)`，纯静态、无状态，对 `LayoutNode` 树单趟递归。

### 4.1 原始基线：引擎未集成、无测试（P1）

**现状**

- `LayoutNode` / `LayoutEngine` / `LayoutStyle` 在 **layout 包外零引用**——尚未与 `GameObject` / `Scene` / 渲染管线建立任何桥接。
- 无任何 layout 单元测试。
- 在该基准提交中，`core/ui`、`core/input` 为空占位目录。
- `WorkPlan.md` 中 UI 组件（文本框/输入框/下拉框/多选框/模态框/按钮/列表/图片/视频）**全部未勾选**——它们都依赖此引擎，因此引擎是 UI 路线的关键路径。

**建议（落地顺序）**

1. **先补测试**：引擎是纯函数式、无 LibGDX 依赖，极易测。建立基准用例覆盖 block / flex（含 grow/shrink/justify/align）/ grid / absolute 定位，固化当前行为，再做集成与扩展。
2. **设计桥接**：定义 `GameObject` ↔ `LayoutNode` 的映射（谁持有谁、何时触发 `layout()`、`LayoutResult` 如何回写到对象的渲染坐标/尺寸）。建议：
   - 布局计算只在「尺寸/样式 dirty」或「视口 resize」时触发，复用现有 dirty 机制，避免每帧全树重算。
   - `LayoutResult` 写回到 GameObject 的位置/包围盒，供渲染与命中测试（`HitTester`）共用。
3. **接入帧循环**：布局应在 `calculate` 之后、`render` 之前完成（类似 preRender 的位置）。

### 4.2 2026-08 更新：后端无关 UI 协议已接入

本轮没有把布局引擎绑定到 `GameObject`、`Scene` 或 LibGDX。取而代之的是
`core/ui` 的纯 Java `UiScene`/`UiNode`：

- `UiNode` 复用 `LayoutNode`/`LayoutResult`，布局结果是绘制和命中测试的唯一几何输入。
- `UiScene.calculate()` 调用 `LayoutEngine`；`preRender()` 产生不可变
  `DrawCommand` 快照；`render()` 不执行后端操作，因此可直接由
  `GameRuntime` 驱动并保持迭代 1 的线程边界。
- 绘制按 `zIndex` 升序、同 z 按插入序稳定遍历；`PushClipCommand` /
  `PopClipCommand` 表达嵌套裁剪。
- `UiInputEvent` 以 capture → target → bubble 路由；单个监听器异常交给失败处理器，
  不阻止同阶段后续监听器。modal 屏蔽背景命中，键盘优先路由到其内部焦点。
- `UiViewport` 明确 screen → UI 的左上原点、右/下开区间契约。HiDPI 由前端在同一
  输入坐标空间中提供 viewport 矩形，而非泄漏到 core。

`gdx/ui` 提供 `GdxUiInputProcessor`、`GdxUiCoordinateMapper` 和
`GdxUiRenderer`；它们是 LibGDX 的唯一依赖点。详细接入约定见
[UI 架构](ui-architecture.md)。本轮新增的无窗口单测覆盖 layout 边缘、事件消费、
modal/focus、稳定绘制顺序、clip、命中边界和坐标映射。

### 4.3 引擎「简化版」特性缺口（P2）

`LayoutEngine` 注释自称「简化版」。以下特性**已在样式类型中定义，但引擎未实现/未消费**，使用前需知悉，避免误以为生效：

| 类别 | 缺口 | 位置 / 说明 |
|------|------|------|
| Flex 换行 | `FlexWrap` 枚举存在，但 `layoutFlex` 把所有项排在**单行** | `LayoutEngine.layoutFlex` 未读 wrap |
| Grid 定位 | 仅按 child 索引顺序填入 `columnCount` 个单元格；**忽略** `GridItem` 的 gridColumn/gridRow/area，也不按 order 排序 | `LayoutEngine.layoutGrid` |
| Box-sizing | `BoxSizingType.BORDER_BOX` 为 **no-op**（尚无 border 支持） | `computeWidth/computeHeight` 注释已注明 |
| 其它样式 | `aspectRatio` / `VerticalAlign` / `Visibility` / `ObjectFit` / `FloatType`（浮动）/ `ClearType` / `OverflowType` 均定义但**引擎不消费** | `LayoutStyle` 有字段，`layout*` 无对应逻辑 |
| 死代码 | `LengthResolver` 构造器接收 `availableWidth/Height` 却未保存使用（仅用 `resolve` 的 `fallbackAvailable` 参数） | `ComputedStyle.java` 内 `LengthResolver` |

**建议**：

- 在 `LayoutEngine` / `LayoutStyle` 的 Javadoc 中**明确列出「当前未生效的属性」**，防止误用。
- 按 UI 组件实际需求**增量补齐**，优先级建议：`overflow`（滚动/裁剪，列表必需）> `BORDER_BOX` + border > flex-wrap > grid 显式定位 > float/clear。
- 清理 `LengthResolver` 的死参数。
- 每补一项特性，先加对应单测再实现（见 4.1）。

---

## 5. 测试与质量缺口（P1）

**现状**：`2d` 模块的 shape 有较完整测试（`*Test.java`）；但 `core`（渲染循环、对象管理、事件分发、快照、layout）几乎无测试。鉴于 #1–#3 这类「机制级失效」Bug 仅靠人工很难发现，测试缺口是高优先级风险。

**建议（优先级从高到低）**

1. **帧循环回归测试**：用一个内存版 `GameObjectManager`，断言 calculate → preRender → render 的调用次序与脏对象传播（可直接覆盖 #1/#2）。
2. **GameObjectManager 生命周期测试**：create/release/active/releaseAll 的状态迁移 + `releasedObjs`/`dirtyRoots` 不泄漏（覆盖 #2/#5）。
3. **并发安全测试**：`calculate` 回调内调用管理器方法的死锁/约束测试（覆盖 #4，加超时）。
4. **layout 引擎测试**：见 4.1。
5. **事件分发测试**：EventDispatcher 三阶段（capture→target→bubble）与队列延迟投递、EventBus 发布订阅。

---

## 6. 建议实施路线

分三批推进，保证每批可独立合入、可验证：

**第 1 批 · 止血（P0，半天内可完成）**
- 修 #1（补 `doPreRender()`）→ 连带消除 #2。
- 修 #3（删重复 `setId`）。
- 处理 `doRender(float delta)` 重载（删除或 `@Deprecated`）。
- 配套补「帧循环回归测试」固化行为。

**第 2 批 · 健壮性（P1）**
- #5（`releasedObjs` 清理 + `dirtyRoots` 在 release 时移除）。
- #4（先文档约束，规划读写分离）。
- #6（合并遍历、减少中间集合）。
- 补 GameObjectManager 生命周期 / 并发测试。

**第 3 批 · 演进（P1/P2）**
- #9 layout 引擎测试 + 与 GameObject 的集成设计与实现。
- #7（渲染顺序：`LinkedHashMap` 或 `zIndex`）。
- #10 按 UI 组件需求增量补齐 layout 特性。
- #8（线程池随多 Scene 规划调整）。

---

## 附录 A：标准帧循环时序（修复 #1 后）

```
GdxScene.render(delta)
  └─ AbstractScene.doCalculate(delta)
        └─ GameObjectManager.doCalculate(delta)      // 并行 calculate，累积 dirtyRoots
  └─ AbstractScene.doRender()
        ├─ gameObjectManager.events().flush()        // 投递延迟事件
        ├─ gameObjectManager.doPreRender()           // ← 修复点：并行预渲染 dirtyRoots，帧末 clear()
        └─ gameObjectManager.doRender()              // 单线程按序渲染根对象
```

## 附录 B：关键文件索引

| 关注点 | 文件 |
|--------|------|
| 帧循环装配 | `core/.../scene/AbstractScene.java` |
| 对象管理 / 并行计算 / 预渲染 | `core/.../object/GameObjectManager.java` |
| Scene 创建 / 切换 | `core/.../scene/AbstractSceneManager.java` |
| ID / 脏标记 / 快照基类 | `core/.../basic/AbstractGameAtom.java` |
| 布局引擎入口 | `core/.../layout/LayoutEngine.java` |
| 布局样式 / 像素化 | `core/.../layout/LayoutStyle.java`、`ComputedStyle.java` |
| 路线图 | `doc/WorkPlan.md` |
