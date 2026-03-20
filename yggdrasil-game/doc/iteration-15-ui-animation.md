# Iteration 15 - UI 动画系统

## 迭代目标

实现完整的 UI 动画系统，为 UI 组件提供补间动画、缓动函数和动画组合能力。

## 变更内容

### 新增核心类

#### 1. 基础接口和抽象类

**UIAction** (`core/ui/animation/UIAction.java`)
- 动画动作基础接口
- 定义 start(), update(), end(), reset() 等核心方法
- 支持完成监听器回调

**UIAnimation** (`core/ui/animation/UIAnimation.java`)
- 扩展 UIAction，添加动画特性
- 支持缓动函数、循环播放、自动反向

**BaseAnimation** (`core/ui/animation/BaseAnimation.java`)
- 动画抽象基类
- 实现通用状态管理（elapsed, finished, loop, autoReverse）
- 提供 easing 应用和进度计算

**UIEasing** (`core/ui/animation/UIEasing.java`)
- 18+ 预定义缓动函数
- 类型：Linear, Quad, Cubic, Quint, Sine, Expo, Circ, Bounce, Elastic, Back
- 每种类型包含 IN/OUT/IN_OUT 变体

**UIInterpolation** (`core/ui/animation/UIInterpolation.java`)
- 插值方法工具类
- 支持 Linear, SmoothStep, SmootherStep

#### 2. 基础动画动作

**MoveToAction** (`core/ui/animation/MoveToAction.java`)
- 位置移动动画
- 支持绝对移动 (moveTo) 和相对移动 (moveBy)

**SizeToAction** (`core/ui/animation/SizeToAction.java`)
- 大小变化动画
- 支持绝对大小和相对大小变化

**AlphaAction** (`core/ui/animation/AlphaAction.java`)
- 透明度渐变动画
- 支持 fadeIn, fadeOut, fadeInOut

**DelayAction** (`core/ui/animation/DelayAction.java`)
- 延迟动作
- 用于动画序列中的等待

#### 3. 动画组合

**SequenceAction** (`core/ui/animation/SequenceAction.java`)
- 顺序执行多个动画
- 支持动态 append 添加动画
- 正确处理时间分配和剩余时间传递

**ParallelAction** (`core/ui/animation/ParallelAction.java`)
- 并行执行多个动画
- 所有动画完成后标记为完成

**AnimationManager** (`core/ui/animation/AnimationManager.java`)
- 管理 UI 组件的动画列表
- 每帧更新所有运行中的动画

#### 4. UIComponent 集成

更新 `UIComponent.java`:
- 添加 `AnimationManager` 字段
- `update(deltaTime)` 中更新动画
- 新增方法：
  - `addAction(UIAction)` - 添加动画
  - `removeAction(UIAction)` - 移除动画
  - `runAction(UIAction)` - 运行动画
  - `stopAllActions()` - 停止所有动画

### 新增测试类

- `UIEasingTest.java` - 15 个缓动函数测试
- `MoveToActionTest.java` - 6 个移动动画测试
- `AlphaActionTest.java` - 7 个透明度动画测试
- `SequenceActionTest.java` - 5 个序列动画测试
- `ParallelActionTest.java` - 6 个并行动画测试

## 技术细节

### 缓动函数

```java
// 二次方缓动
IN_QUAD    = t -> t * t
OUT_QUAD   = t -> t * (2 - t)
IN_OUT_QUAD = t -> t < 0.5f ? 2 * t * t : -1 + (4 - 2 * t) * t

// 三次方缓动
IN_CUBIC    = t -> t * t * t
OUT_CUBIC   = t -> (t-1)^3 + 1
IN_OUT_CUBIC = t < 0.5f ? 4t^3 : (t-1)(2t-1)(2t-1)+1

// 弹性缓动 (Bounce)
IN_BOUNCE  = 1 - OUT_BOUNCE(1-t)
OUT_BOUNCE = 分段函数 (1/27, 4/27, 16/27 系数)

// 弹性缓动 (Elastic)
IN_ELASTIC  = 1 - OUT_ELASTIC(1-t)
OUT_ELASTIC = 2^(-10t) * sin((t-0.1)*5π) + 1
```

### 动画状态机

```
BaseAnimation 状态流转:
- start() -> elapsed=0, finished=false, reversing=false
- update(delta):
  - elapsed < duration -> 正常更新进度
  - elapsed >= duration && autoReverse && !reversing -> reversing=true, elapsed 重置
  - elapsed >= duration && loop -> elapsed % duration, 继续循环
  - elapsed >= duration && 其他情况 -> finished=true, 触发回调
```

### SequenceAction 时间分配

```java
// 关键逻辑：在单个 update 调用中处理多个动画
float timeUsed = 0;
while (timeUsed < delta && !finished && currentIndex < actions.size()) {
    float timeForThisAction = delta - timeUsed;
    boolean stillRunning = currentAction.update(timeForThisAction);

    if (!stillRunning) {
        float actualTimeUsed = currentAction.getDuration();
        timeUsed += actualTimeUsed;
        currentIndex++;  // 切换到下一个动画
    }
}
```

## 影响范围

### 依赖模块
- 无外部依赖，纯核心模块实现

### 受影响的类
- `UIComponent` - 集成动画管理器

### 需要回归测试
- `UIComponentTest` - 验证 update 方法正常工作
- `UIManagerTest` - 验证 UI 系统整体功能

## 使用示例

```java
// 创建淡入动画
AlphaAction fadeIn = AlphaAction.fadeIn(0.5f);

// 创建移动动画
MoveToAction moveTo = MoveToAction.moveTo(1f, 100f, 50f);
moveTo.setEasing(UIEasing.OUT_QUAD);

// 序列：先淡入再移动
SequenceAction sequence = SequenceAction.sequence(fadeIn, moveTo);

// 并行：同时移动和淡入
ParallelAction parallel = ParallelAction.parallel(fadeIn, moveTo);

// 添加到组件
button.runAction(sequence);

// 设置循环和反向
moveTo.setLoop(true);
moveTo.setAutoReverse(true);

// 添加完成监听
sequence.setOnFinishListener(() -> {
    System.out.println("动画完成!");
});
```

## Git 提交

- Commit: `db70864`
- Message: `feat(core): Iteration 15 - 实现 UI 动画系统`

## 测试结果

- 总测试数：437
- 新增测试：39 (全部通过)
- 失败：0
- 跳过：1

## 后续工作

- [ ] 颜色渐变动画 (ColorAction)
- [ ] 旋转动画 (RotateAction)
- [ ] 缩放动画 (ScaleAction)
- [ ] 路径动画 (PathAction)
- [ ] 动画预设/模板系统
