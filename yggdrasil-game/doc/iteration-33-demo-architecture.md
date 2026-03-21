# Iteration 33 - Demo 基础架构

**日期**: 2026-03-21
**状态**: ✅ 已完成
**测试数量**: 82 个测试用例

---

## 迭代目标

整合之前实现的各种系统（实体组件、输入系统、UI 系统），创建一个完整的游戏演示 Demo 基础架构，为后续的游戏逻辑实现提供基础。

---

## 变更内容

### 1. Demo 游戏对象定义

#### 新增枚举

**`EntityType.java`** - 实体类型定义
```java
public enum EntityType {
    PLAYER, ENEMY, NPC, ITEM, OBSTACLE, PROJECTILE, ENVIRONMENT;
}
```

**`GameAction.java`** - 游戏动作定义
```java
public enum GameAction {
    MOVE_UP, MOVE_DOWN, MOVE_LEFT, MOVE_RIGHT,
    ATTACK, SKILL_1, SKILL_2, SKILL_3,
    INTERACT, JUMP, CROUCH, DODGE,
    PAUSE, CONFIRM, CANCEL;
}
```

### 2. 实体组件系统

#### 核心组件

**`HealthComponent.java`** - 生命值组件
- 功能：血量管理、伤害计算、治疗、死亡回调
- 特性：生命值变化监听器、死亡监听器、血量百分比计算
- 测试：15 个测试用例

**`MovementComponent.java`** - 移动组件
- 功能：位置管理、速度管理、方向移动、归一化处理
- 特性：支持对角线移动归一化、距离计算
- 测试：13 个测试用例

**`AttackComponent.java`** - 攻击组件
- 功能：攻击管理、冷却系统、攻击伤害计算
- 特性：攻击速度加成、有效冷却计算
- 测试：14 个测试用例

#### 实体类

**`GameEntity.java`** - 游戏实体基类
- 抽象基类，包含所有组件
- 功能：组件更新委托、统一伤害处理、攻击接口
- 特性：唯一 ID 生成、名称管理、激活/销毁生命周期

**`PlayerEntity.java`** - 玩家实体
- 继承自 GameEntity
- 功能：分数管理、等级系统、升级机制
- 升级效果：最大生命值 +20，攻击力 +2，生命重置

**`EnemyEntity.java`** - 敌人实体
- 继承自 GameEntity
- 三种类型：
  - NORMAL: 50 HP, 100 速度，10 伤害，50 赏金
  - ELITE: 150 HP, 80 速度，20 伤害，200 赏金
  - BOSS: 500 HP, 50 速度，30 伤害，1000 赏金

### 3. 输入系统

**`InputActionMap.java`** - 输入动作映射
- 功能：键码到游戏动作的绑定
- 方法：bind, unbind, getAction, isBound, getAllBindings
- 测试：10 个测试用例

**`GameInputProcessor.java`** - 游戏输入处理器
- 功能：键盘事件处理、动作状态管理、动作监听器
- 方法：keyDown, keyUp, isActionActive, getMoveDirection, getActiveActions
- 特性：支持动作监听器回调、移动方向归一化
- 测试：12 个测试用例

**`DefaultInputConfig.java`** - 默认输入配置
- 提供默认键盘绑定（WASD 移动，JKL 技能，F 交互）
- 键码常量（LibGDX 兼容值）
- 方法：applyDefaultBindings, applyArrowKeys

### 4. Demo 配置

**`DemoConfig.java`** - Demo 游戏配置常量
```java
SCREEN_WIDTH = 1280
SCREEN_HEIGHT = 720
TARGET_FPS = 60
PLAYER_INITIAL_HEALTH = 100
PLAYER_MOVE_SPEED = 150
ENEMY_SPAWN_INTERVAL = 3.0f
// ... 更多配置
```

### 5. Demo 游戏场景

**`DemoGameScene.java`** - Demo 游戏场景
- 继承自 GameScene
- 功能：
  - 玩家实体管理
  - 敌人生成和管理
  - 输入处理集成
  - 碰撞检测
  - 游戏结束条件判断
- 游戏循环：敌人生成 → 输入处理 → 实体更新 → 碰撞检测 → 游戏结束检查

### 6. UI 组件

**`HealthBar.java`** - 血条组件
- 功能：显示当前/最大生命值
- 特性：动态颜色变化（绿>50%, 黄>25%, 红≤25%）
- 测试：通过组件测试覆盖

**`EnemyHealthBar.java`** - 敌人血条
- 功能：显示敌人生命值
- 特性：敌人名称显示、简化设计

**`ScoreDisplay.java`** - 分数显示
- 功能：显示分数和等级
- 方法：addScore, setLevel, setColor

**`GameOverDialog.java`** - 游戏结束对话框
- 功能：显示胜利/失败信息
- 按钮：重新开始、返回主菜单
- 回调：setOnRestartClickListener, setOnMenuClickListener

### 7. UI 组件修复

修复了 Demo UI 类的编译错误：
- 添加了 `render()` 方法实现（空实现，由引擎完成渲染）
- 使用正确的 API 方法：
  - `setBackgroundColor()` 替代 `UIRect.setColor()`
  - `setFillColor()` 替代 `UIProgressBar.setColor()`
  - `setTextColor()` 替代 `UILabel.setColor()`
  - `setOnClickListener()` 替代 `UIButton.setOnClickCallback()`
  - `setVisibility()` 替代 `setVisible()`

---

## 测试覆盖

### 组件测试（42 个测试）
- `HealthComponentTest`: 15 个测试
  - 构造函数、伤害、治疗、死亡、监听器、重置、快照等

- `MovementComponentTest`: 13 个测试
  - 移动、速度、位置、距离、归一化、快照等

- `AttackComponentTest`: 14 个测试
  - 攻击、冷却、攻击速度、有效冷却、快照等

### 实体测试（23 个测试）
- `PlayerEntityTest`: 15 个测试
  - 构造函数、分数、等级、升级、生命、攻击、移动等

- `EnemyEntityTest`: 8 个测试
  - 构造函数、类型、生命值、赏金、初始化、销毁等

### 输入系统测试（22 个测试）
- `InputActionMapTest`: 10 个测试
  - 绑定、解绑、查询、清空、重新绑定等

- `GameInputProcessorTest`: 12 个测试
  - 按键处理、动作监听器、移动方向、active actions 等

---

## 关键设计决策

### 1. 组件组合模式
使用组合而非继承来构建实体功能：
- GameEntity 组合 HealthComponent、MovementComponent、AttackComponent
- 每个组件负责单一职责
- 便于测试和复用

### 2. 输入抽象层
通过 InputActionMap 抽象输入：
- 解耦键码和游戏动作
- 支持自定义键位绑定
- 便于实现手柄等多输入设备支持

### 3. 归一化移动
对角线移动时自动归一化：
- 防止对角线移动过快
- getMoveDirection() 返回单位向量
- 测试验证：(上 + 右) → (0.707, 0.707)

### 4. 敌人难度分级
通过 EnemyType 枚举实现难度分级：
- 不同类型有不同属性（生命、速度、伤害、赏金）
- 便于扩展新类型
- 便于平衡调整

---

## 文件清单

### 源码文件（14 个）
```
src/main/java/io/github/s3s3l/yggdrasil/game/core/demo/
├── entity/
│   ├── EntityType.java
│   ├── GameAction.java
│   ├── GameEntity.java
│   ├── PlayerEntity.java
│   ├── EnemyEntity.java
│   └── component/
│       ├── HealthComponent.java
│       ├── MovementComponent.java
│       └── AttackComponent.java
├── input/
│   ├── InputActionMap.java
│   ├── GameInputProcessor.java
│   └── DefaultInputConfig.java
├── config/
│   └── DemoConfig.java
├── scene/
│   └── DemoGameScene.java
└── ui/
    ├── HealthBar.java
    ├── EnemyHealthBar.java
    ├── ScoreDisplay.java
    └── GameOverDialog.java
```

### 测试文件（7 个）
```
src/test/java/io/github/s3s3l/yggdrasil/game/core/demo/
├── entity/
│   ├── PlayerEntityTest.java
│   ├── EnemyEntityTest.java
│   └── component/
│       ├── HealthComponentTest.java
│       ├── MovementComponentTest.java
│       └── AttackComponentTest.java
└── input/
    ├── InputActionMapTest.java
    └── GameInputProcessorTest.java
```

---

## 依赖关系

```
DemoGameScene 依赖:
├── GameScene (框架层)
├── PlayerEntity (Demo 层)
├── EnemyEntity (Demo 层)
├── GameInputProcessor (Demo 层)
├── HealthBar (Demo UI 层)
├── ScoreDisplay (Demo UI 层)
└── GameOverDialog (Demo UI 层)

GameInputProcessor 依赖:
├── InputActionMap (Demo 层)
└── GameAction (Demo 层)

实体类依赖:
├── GameEntity → HealthComponent, MovementComponent, AttackComponent
└── PlayerEntity/EnemyEntity → GameEntity
```

---

## Git 提交

提交信息：
```
feat(core): Iteration 33 - Demo 基础架构

实现游戏演示 Demo 的基础架构，包括：

实体组件系统:
- HealthComponent, MovementComponent, AttackComponent
- GameEntity 基类，PlayerEntity, EnemyEntity

输入系统:
- InputActionMap 键位绑定
- GameInputProcessor 输入处理
- DefaultInputConfig 默认配置

UI 组件:
- HealthBar, EnemyHealthBar
- ScoreDisplay, GameOverDialog

Demo 场景:
- DemoGameScene 游戏循环
- DemoConfig 配置常量

单元测试:
- 7 个测试类，82 个测试用例
- 覆盖所有组件、实体和输入系统

Fixes: 编译错误修复（UI 组件 API 适配）
```

---

## 待办事项

### Iteration 34: 游戏逻辑实现
- [ ] 玩家控制逻辑完善
- [ ] 战斗系统（攻击判定、伤害计算）
- [ ] 敌人生成逻辑
- [ ] 胜负条件实现

### Iteration 35: Demo 完善和测试
- [ ] 完整游戏循环
- [ ] UI 反馈完善
- [ ] 场景切换
- [ ] 性能测试

---

## 技术备注

1. **无 GUI 测试**: 由于运行环境无桌面环境，所有测试都是纯 JUnit 单元测试，不依赖 LibGDX 或任何图形框架。

2. **LibGDX 兼容性**: DefaultInputConfig 中的键码常量与 LibGDX 的 Input.Keys 兼容，但不依赖 LibGDX 库。

3. **渲染抽象**: UI 组件的 `render()` 方法为空实现，实际渲染由游戏引擎（如 LibGDX）完成。

4. **扩展性**: 组件设计支持灵活扩展，可轻松添加新组件类型（如 BuffComponent、InventoryComponent 等）。
