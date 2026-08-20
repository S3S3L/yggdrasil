# UI 架构与前端适配

## 边界

`core/ui` 是纯 Java 层，不依赖 LibGDX、窗口或终端 cell 模型。它由以下协议组成：

- `UiScene`：可作为 `GameRuntime` 的 `Renderable` 运行。`calculate()` 计算
  `LayoutNode` 树，`preRender()` 生成不可变 draw-command 快照，`render()` 不做后端
  调用。
- `UiNode`：保序的布局树节点；`zIndex` 从低到高绘制，插入顺序打破同 z 的平局。
  `LayoutResult` 同时决定绘制、clip 与命中测试的矩形。
- `DrawCommand`：当前包含填充矩形、文本、`PushClipCommand` 和
  `PopClipCommand`。字体、纹理和实际 GPU 调用属于 renderer。
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
