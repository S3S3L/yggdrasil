# Yggdrasil 项目工作计划

> 最后更新：2026-03-21
> 当前迭代：Iteration 34 - 游戏逻辑实现（已完成）
> 下次里程碑回顾：Iteration 35 完成后

---

## 已完成功能

### 核心系统
- [x] 属性初始化机制
- [x] 状态更新机制
- [x] 暂存和恢复机制（Snapshot 系统）
- [x] 事件绑定机制（EventBus）
- [x] 形状设定和碰撞判断（Iteration 25 进行中）

### 渲染逻辑
- [x] 并发计算
- [x] 脏对象并发预渲染
- [x] 同步渲染
- [x] UI 渲染器适配层

### 事件系统
- 鼠标
  - [x] 点击（GestureDetector）
  - [x] 长按（GestureDetector）
  - [x] 移动（InputManager）
  - [x] 拖拽（GestureDetector）
- 键盘
  - [x] 下压（InputManager）
  - [x] 弹起（InputManager）
  - [ ] 长按
  - [ ] 组合键
- 手柄
  - [ ] 基础支持

### 碰撞系统（Iteration 25-26 已完成）
- [x] 刚体碰撞（Iteration 25）
- [x] 射线检测（Iteration 25）
- [x] 物理世界管理（Iteration 26）
- [x] 碰撞事件回调（Iteration 26）
- [x] 四叉树优化（Iteration 26）
- [ ] 碰撞响应
- [ ] 触发器系统

### UI 组件（完整）
- [x] 文本框（UILabel）
- [x] 输入框（UIInputField）
- [x] 下拉框（UIDropdown, UIComboBox）
- [x] 多选框（UICheckbox, UIToggle）
- [x] 模态对话框（UIModalDialog）
- [x] 按钮（UIButton）
- [x] 列表（UIList, UITreeList）
- [x] 图片（UIImage）
- [ ] 视频

### 场景系统
- [x] 切换（SceneManager + 过渡效果）
- [ ] 暂停/恢复
- [ ] 保存/加载

### 迭代历史

| 迭代 | 功能模块 | 测试数 | 状态 |
|------|----------|--------|------|
| 3 | 场景管理和快照系统 | 24 | ✅ |
| 4 | 网格和地图系统 | - | ✅ |
| 5 | 地形效果和地图生成器 | - | ✅ |
| 6 | 单位系统和移动路径 | - | ✅ |
| 7 | 动作和技能系统 | - | ✅ |
| 8 | 回合和游戏流程系统 | - | ✅ |
| 9 | 视野和掩体系统 | - | ✅ |
| 10 | 战役和大地图系统 | - | ✅ |
| 11 | AI 框架系统 | - | ✅ |
| 12 | 数据配置系统 | - | ✅ |
| 13 | UI 框架系统 | - | ✅ |
| 14 | UI 渲染器适配层 | - | ✅ |
| 15 | UI 动画系统 | 43 | ✅ |
| 18-19 | UI 高级组件 | - | ✅ |
| 20 | UI 布局系统 | - | ✅ |
| 21 | UI 数据绑定系统 | - | ✅ |
| 22 | UI 主题系统 | 96 | ✅ |
| 23 | UI 场景和导航管理 | 112 | ✅ |
| 24 | 增强输入系统 | 89 | ✅ |
| 25 | 物理碰撞系统（一） | 103 | ✅ |
| 26 | 物理世界管理 | 65 | ✅ |
| 27 | 刚体物理基础 | - | ✅ |
| 28 | 碰撞响应系统 | 49 | ✅ |
| 29 | 触发器系统 | 50 | ✅ |
| 30 | 输入系统完善 | 36 | ✅ |
| 31 | 场景系统完善 | 28 | ✅ |
| 32 | 游戏流程管理 | 20 | ✅ |
| 33 | Demo 基础架构 | 82 | ✅ |
| 34 | 游戏逻辑实现 | 17 | ✅ |
| 33 | Demo 基础架构 | 82 | ✅ |

---

## 里程碑规划

### 里程碑 1：物理系统完善（Iteration 27-29）

**目标**: 完善物理碰撞系统，实现碰撞响应和触发器，将物理系统集成到 GameObject。

#### Iteration 27: 刚体物理基础
- [ ] 刚体组件（RigidbodyComponent）
- [ ] 质量和力矩
- [ ] 速度/角速度
- [ ] 外力施加接口

#### Iteration 28: 碰撞响应
- [ ] 碰撞冲量计算
- [ ] 反弹和摩擦
- [ ] 碰撞体质量感知
- [ ] 响应事件回调

#### Iteration 29: 触发器和集成
- [ ] 触发器碰撞体（不产生物理响应）
- [ ] 进入/离开触发器事件
- [ ] GameObject 物理组件集成
- [ ] 物理世界与游戏对象同步

**里程碑回顾任务**:
- [ ] 更新记忆文件（ITERATIONS/iteration-27/28/29.md）
- [ ] 更新 PROJECT_OVERVIEW.md 物理系统章节
- [ ] 更新 DECISION_LOG.md 记录刚体物理设计决策
- [ ] 更新 TODO_BACKLOG.md 标记完成项

---

### 里程碑 2：输入和场景系统完善（Iteration 30-32）

**目标**: 完善输入系统和场景管理系统，为游戏 Demo 提供完整的用户交互和场景管理能力。

#### Iteration 30: 输入系统完善
- [ ] 键盘长按检测（KeyHoldDetector）
- [ ] 组合键系统（ComboKey）
- [ ] 按键绑定配置
- [ ] 全局快捷键管理

#### Iteration 31: 场景系统完善
- [ ] 场景暂停/恢复机制
- [ ] 场景状态序列化
- [ ] 场景保存/加载
- [ ] 多存档位支持

#### Iteration 32: 游戏流程管理
- [ ] 游戏状态机（GameStateMachine）
- [ ] 主菜单场景
- [ ] 游戏内场景
- [ ] 暂停菜单场景

**里程碑回顾任务**:
- [ ] 更新记忆文件（ITERATIONS/iteration-30/31/32.md）
- [ ] 更新 PROJECT_OVERVIEW.md 输入和场景章节
- [ ] 更新 DECISION_LOG.md 记录场景状态管理设计
- [ ] 更新 TODO_BACKLOG.md 标记完成项

---

### 里程碑 3：游戏演示 Demo（Iteration 33-35）

**目标**: 整合所有系统，实现一个完整的游戏演示 Demo，验证框架功能。

#### Iteration 33: Demo 基础架构
- [x] Demo 游戏对象定义（PlayerEntity, EnemyEntity, GameAction, EntityType）
- [x] 基础 UI 界面组件（HealthBar, ScoreDisplay, GameOverDialog, EnemyHealthBar）
- [x] Demo 场景配置（DemoGameScene, DemoConfig）
- [x] 输入到游戏动作映射（InputActionMap, GameInputProcessor, DefaultInputConfig）
- [x] 单元测试（82 个测试用例，覆盖组件、实体、输入系统）

#### Iteration 34: 游戏逻辑实现
- [x] 玩家控制（攻击、技能）
- [x] 基础战斗逻辑（玩家攻击、敌人 AI 攻击）
- [x] 胜负条件判断（生存时间、玩家死亡）
- [x] 游戏流程控制（游戏状态管理）
- [x] 单元测试（17 个测试用例）

#### Iteration 35: Demo 完善和测试
- [ ] 完整游戏循环（开始→游戏→结束）
- [ ] UI 反馈（血条、分数等）
- [ ] 场景切换演示
- [ ] 性能测试和优化

**里程碑回顾任务**:
- [ ] 更新记忆文件（ITERATIONS/iteration-33/34/35.md）
- [ ] 更新 PROJECT_OVERVIEW.md 添加 Demo 说明
- [ ] 更新 DECISION_LOG.md 记录 Demo 架构设计
- [ ] 更新 TODO_BACKLOG.md 标记完成项
- [ ] 编写 Demo 演示文档

---

### 里程碑 4：资源和本地化（Iteration 36-38）

**目标**: 实现资源管理和本地化系统，提升框架的实用性和可扩展性。

#### Iteration 36: 资源管理系统
- [ ] 资源加载器接口
- [ ] 纹理资源加载
- [ ] 音频资源加载
- [ ] 资源缓存管理

#### Iteration 37: 异步资源加载
- [ ] 异步加载接口
- [ ] 加载进度回调
- [ ] 加载队列管理
- [ ] 资源预加载

#### Iteration 38: 本地化系统
- [ ] 多语言文本资源
- [ ] 运行时语言切换
- [ ] 本地化 UI 适配
- [ ] 默认语言配置

**里程碑回顾任务**:
- [ ] 更新记忆文件（ITERATIONS/iteration-36/37/38.md）
- [ ] 更新 PROJECT_OVERVIEW.md 资源和本地化章节
- [ ] 更新 DECISION_LOG.md 记录资源管理设计
- [ ] 更新 TODO_BACKLOG.md 标记完成项

---

### 里程碑 5：性能优化和技术债务（Iteration 39-41）

**目标**: 优化框架性能，清理技术债务，提升代码质量。

#### Iteration 39: 对象池和缓存优化
- [ ] 通用对象池实现
- [ ] 碰撞体对象池
- [ ] 事件对象池
- [ ] 缓存清理策略

#### Iteration 40: 渲染优化
- [ ] 批次渲染实现
- [ ] 脏标记优化
- [ ] 视锥剔除
- [ ] 渲染性能分析工具

#### Iteration 41: 代码质量和文档
- [ ] 技术债务清理
- [ ] JavaDoc 完善
- [ ] 统一日志规范
- [ ] API 文档生成

**里程碑回顾任务**:
- [ ] 更新记忆文件（ITERATIONS/iteration-39/40/41.md）
- [ ] 更新 PROJECT_OVERVIEW.md 性能优化章节
- [ ] 更新 DECISION_LOG.md 记录优化决策
- [ ] 更新 TODO_BACKLOG.md 标记完成项
- [ ] 生成性能基准报告

---

## 技术债务

### 高优先级
- [ ] FileSnapshotStore 完整实现
- [ ] core/gdx 模块循环依赖清理

### 中优先级
- [ ] 代码重构
- [ ] JavaDoc 完善
- [ ] 统一日志规范

### 低优先级
- [ ] 性能分析工具
- [ ] 自动化测试覆盖率提升
