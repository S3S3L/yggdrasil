# UI 架构与前端适配

## 边界

`core/ui` 是纯 Java 层，不依赖 LibGDX、窗口或终端 cell 模型。它由以下协议组成：

- `UiScene`：可作为 `GameRuntime` 的 `Renderable` 运行。`calculate()` 计算
  `LayoutNode` 树，`preRender()` 生成不可变 draw-command 快照，`render()` 不做后端
  调用。
- `UiNode`：保序的布局树节点；`zIndex` 从低到高绘制，插入顺序打破同 z 的平局。
  `LayoutResult` 同时决定绘制、clip 与命中测试的矩形。
- `DrawCommand`：当前包含填充矩形、文本、`PushClipCommand` 和
  `PopClipCommand`。`TextCommand` 只携带 `FontHandle`（逻辑 source、style、size），
  字体、纹理和实际 GPU 调用属于 renderer。
- `UiInputEvent`：capture → target → bubble。`consume()` 会停止传播；监听器异常
  由 scene 的失败处理器隔离。

modal 仅命中最上层 modal，且键盘/文本/滚动优先路由到其中的焦点节点。由 scene 临时
附加的 modal 在关闭时会自动脱离树并恢复先前焦点；已由应用添加到树中的 modal 仍由
应用负责其可见性与生命周期。

## 坐标与裁剪契约

UI 使用左上为原点、Y 向下的浮点坐标。`UiRect` 和 `UiViewport` 的右、下边均为开
区间。`UiViewport` 把输入表面中的 viewport 矩形线性映射到 UI 尺寸；HiDPI 前端必须
确保输入坐标与该屏幕矩形来自同一种空间（都使用逻辑像素或都使用物理像素）。

节点设置 `clipChildren`，或布局样式的 `overflow` 不是 `VISIBLE` 时，都会围绕子节点
输出 clip 命令；命中测试使用同一内容矩形，因此不可见的溢出部分不会接收输入。

## LibGDX 接入

在 render thread 中：

1. resize 后，以 `GdxUiCoordinateMapper` 把 LibGDX viewport bounds 转成
   `UiViewport`，并同时设置到 `UiScene` 和 `GdxUiInputProcessor`。
2. 将 `GdxUiInputProcessor` 注册为 `InputProcessor`。它只进行坐标和回调转换，
   目标选择与传播仍在 `UiScene`。
3. 在 `GameRuntime.runFrame(scene, delta, ...)` 后，把 `scene.getDrawCommands()` 交给
   `GdxUiRenderer`。该 renderer 只能在拥有 GL context 的 render thread 使用；它负责
   左上 UI Y 轴到 LibGDX camera、以及嵌套 scissor clip 的转换。

未来的 My-Term 或其他 frontend 应实现相同的 draw-command consumer 和输入 adapter，
而不是将终端屏幕 cell/PTY 概念加入 `core/ui`。

## Retained-scene performance contract

`UiScene` is deliberately a thread-confined update/render-thread object; PTY
or worker threads must publish state to that owner rather than call
`calculate()`, `preRender()`, `metrics()`, or mutate the tree concurrently.
Its draw-command list is an immutable snapshot, but the scene itself is not a
locking or cross-thread hand-off abstraction.

`calculate()` uses an allocation-free fingerprint over the whole mutable layout
tree (including nested box, flex, grid, visibility, and child changes). It
runs `LayoutEngine` only after that fingerprint, the viewport, or an explicit
`markDirty()` changes. `preRender()` similarly retains commands until a
layout/bounds/UI-property change is observed. A draw handler whose captured
content changes in place, such as live text, must call
`invalidateDrawCommands()`; it refreshes commands without causing a layout
pass. Children cache their z-sorted order, invalidating that cache only for
add/remove/reparent or z-index changes.

`UiScene.metrics()` exposes cumulative deterministic counters, not timings:
layout passes, command-snapshot builds, drawn-node and hit-test visits, and
child-order cache builds. They are suitable for headless CI regression tests;
wall-clock and allocation-profiler numbers are intentionally not used as
acceptance criteria.

The `UiScenePerformanceBaselineTest` fixture drives a root and one drawing
child for 120 unchanged frames. Before retained invalidation it performed 480
`LayoutResult` writes, 120 handler calls, and 120 immutable command snapshots.
With this contract it performs 4 writes, 1 handler call, and 1 snapshot
(99.2%, 99.2%, and 99.2% fewer deterministic operations). The initial build
visits two nodes and builds two child-order caches; unchanged frames rebuild
neither. `UiSceneDirtyStateTest` additionally covers style, direct bounds,
children, visibility, dynamic text, resize, and cache-reusing hit tests.

This makes small, retained, mostly-idle overlays, menus, modal chrome, and
HUD controls good migration candidates. It is not a terminal-grid renderer:
cell buffers, PTY output, cursor/selection updates, high-frequency scrolling,
and other continuously changing dense surfaces should keep their specialized
frontends rather than forcing per-cell draw commands through `core/ui`.

## 字体与图集

`core/font` 定义了不依赖图形库的 `FontSource`、`FontRequest`、`FontStyle`、
`FontFallbackPolicy` 和 `FontSystem`。fallback policy 按 code point 产生有序 source
候选项；`unicodeLayers` 保持 ASCII 主字体，并提供 CJK、符号、PUA 的分层规则。它缓存
coverage 与 metrics 的键为 **具体 FontSource + request + code point**，并返回 advance、
ink bounds、ascent/descent、line height 和 baseline，供任何 UI 对齐策略使用。找不到字形
时返回明确的 `MissingGlyph`，不隐式替换为终端模型。

需要平台动态 fallback（例如 macOS CoreText）时，应用实现可选的
`FontFallbackResolver` SPI 并交给 policy；core 不链接 CoreText 或任何 native 库。未提供
resolver 时 policy 只使用静态层，因而在所有平台具有确定的回退行为。`ImageGlyphSource`
是 emoji/内联图片的扩展点；图片 id 和 metrics 在 core 中保持纯数据，纹理由 frontend
所有。

LibGDX 使用 `GdxResources.createFontSystem(...)` 在 render thread 建立
`GdxFontSystem`。它延迟创建 FreeType face 和增量 atlas，按 DPI 用物理字号生成纹理再缩回
逻辑尺寸。所有 face、atlas 上传、image glyph texture 和 dispose 都由同一个
`ResourceScope` 在 render thread 关闭；没有静态 `BitmapFont` 或静态 GL 创建。`FontHandle`
改变字号、或 framebuffer DPI 改变时，调用 `FontSystem.invalidate()` 或
`invalidateForFontSizeChange()` 或 `updateDpiScale(...)`，两者都会清除 coverage/metrics
缓存并释放旧 atlas。实现
`GdxImageGlyphRenderer` 时，其 loader 交出的 texture 所有权转移给
`GdxImageGlyphSource`，不得再由调用方释放。
