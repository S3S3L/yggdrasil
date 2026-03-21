# Iteration 34 - 游戏逻辑实现

**日期**: 2026-03-21
**状态**: ✅ 已完成
**测试数量**: 17 个测试用例（DemoGameSceneTest）

---

## 迭代目标

在 Iteration 33 的基础上，实现完整的游戏逻辑，包括玩家攻击控制、敌人 AI、战斗系统和胜负条件判断。

---

## 变更内容

### 1. 玩家攻击控制

#### 修改文件：`DemoGameScene.java`

**新增攻击输入处理**：
```java
// 攻击
inputProcessor.setActionListener(GameAction.ATTACK, () -> {
    if (!gameOver && !isPaused() && player != null && player.isActive()) {
        playerAttack();
    }
});

// 技能 1-3
inputProcessor.setActionListener(GameAction.SKILL_1, () -> { ... });
inputProcessor.setActionListener(GameAction.SKILL_2, () -> { ... });
inputProcessor.setActionListener(GameAction.SKILL_3, () -> { ... });
```

**玩家攻击方法**：
```java
protected void playerAttack() {
    if (player == null || !player.isActive()) return;

    // 尝试攻击（检查冷却）
    if (!player.tryAttack()) return;

    // 获取攻击范围和伤害
    float attackRange = player.getAttackComponent().getAttackRange();
    float attackDamage = player.getAttackComponent().getAttackDamage();

    // 检查范围内的敌人
    for (EnemyEntity enemy : enemies) {
        if (!enemy.isActive() || enemy.isDead()) continue;

        float distance = player.getMovementComponent().distanceTo(enemy.getX(), enemy.getY());
        if (distance <= attackRange) {
            enemy.takeDamage(attackDamage);
            onPlayerAttackHit(player, enemy);
        }
    }

    onPlayerAttack(player);
}
```

### 2. 技能系统

**三个基础技能**：

| 技能 | 按键 | 效果 |
|------|------|------|
| 技能 1 | K | 快速治疗（恢复 20 点生命） |
| 技能 2 | L | 范围攻击（对所有敌人造成 50% 伤害） |
| 技能 3 | I | 临时增益（重置攻击冷却） |

```java
protected void playerUseSkill(int skillIndex) {
    switch (skillIndex) {
        case 1: // 快速治疗
            if (player != null) {
                player.heal(20);
                onPlayerUseSkill(player, 1);
            }
            break;
        case 2: // 范围攻击
            if (player != null) {
                float attackDamage = player.getAttackComponent().getAttackDamage() * 0.5f;
                for (EnemyEntity enemy : enemies) {
                    if (enemy.isActive() && !enemy.isDead()) {
                        enemy.takeDamage(attackDamage);
                    }
                }
                onPlayerUseSkill(player, 2);
            }
            break;
        case 3: // 临时增益
            if (player != null) {
                player.getAttackComponent().resetCooldown();
                onPlayerUseSkill(player, 3);
            }
            break;
    }
}
```

### 3. 敌人 AI 系统

#### 敌人追踪逻辑

```java
protected void updateEnemyAI(EnemyEntity enemy, float deltaTime) {
    if (enemy == null || !enemy.isActive() || enemy.isDead()) return;
    if (player == null || !player.isActive()) return;

    float distance = enemy.getMovementComponent().distanceTo(player.getX(), player.getY());

    // 如果在攻击范围内，攻击玩家
    if (distance <= enemy.getAttackComponent().getAttackRange()) {
        enemyAIAttack(enemy);
    } else {
        // 否则向玩家移动
        float dx = player.getX() - enemy.getX();
        float dy = player.getY() - enemy.getY();

        // 归一化
        float length = (float) Math.sqrt(dx * dx + dy * dy);
        if (length > 0) {
            dx /= length;
            dy /= length;
        }

        // 移动
        enemy.moveTo(
            enemy.getX() + dx * deltaTime,
            enemy.getY() + dy * deltaTime
        );
    }
}
```

#### 敌人攻击逻辑

```java
protected void enemyAIAttack(EnemyEntity enemy) {
    if (enemy == null || !enemy.isActive() || enemy.isDead()) return;
    if (player == null || !player.isActive()) return;

    // 尝试攻击
    if (enemy.tryAttack()) {
        float attackDamage = enemy.getAttackComponent().getAttackDamage();
        player.takeDamage(attackDamage);
        onEnemyAttackHit(enemy, player);
    }
}
```

### 4. 胜负条件系统

#### 胜利条件

- **生存模式**：存活到目标时间（默认 120 秒）
- 可扩展：击败特定 Boss、收集物品等

#### 失败条件

- 玩家生命值归零（死亡）

#### 游戏结束检查

```java
protected void checkGameOver() {
    if (player == null || player.isDead()) {
        gameOver = true;
        isVictory = false;
        onGameLost();
        return;
    }

    // 检查胜利条件：生存时间达到目标
    if (gameTimer >= survivalGoalTime) {
        gameOver = true;
        isVictory = true;
        onGameVictory();
    }
}
```

#### 胜利/失败回调

```java
protected void onGameVictory() {
    victory(); // 调用 GameManager.victory()
}

protected void onGameLost() {
    defeat(); // 调用 GameManager.defeat()
}
```

### 5. DemoGameScene 增强

#### 新增字段

```java
private boolean isVictory;          // 是否胜利
private final float survivalGoalTime; // 生存目标时间（秒）
```

#### 新增构造方法

```java
public DemoGameScene() {
    this(120); // 默认生存 120 秒
}

public DemoGameScene(float survivalTime) {
    super("DemoGame");
    // ...
    this.survivalGoalTime = survivalTime;
}
```

#### 新增 Getter 方法

```java
public boolean isVictory() { return isVictory; }
public float getGameTimer() { return gameTimer; }
public boolean isGameOver() { return gameOver; }
```

---

## 测试覆盖

### DemoGameSceneTest（17 个测试）

| 测试方法 | 测试内容 |
|---------|---------|
| `testConstructor` | 构造方法测试 |
| `testConstructorWithSurvivalTime` | 带生存时间的构造方法 |
| `testOnEnter` | 场景进入初始化 |
| `testPlayerAttack` | 玩家攻击功能 |
| `testPlayerAttackCooldown` | 攻击冷却机制 |
| `testPlayerUseSkill1_Heal` | 技能 1：治疗 |
| `testPlayerUseSkill2_AOE` | 技能 2：范围攻击 |
| `testPlayerUseSkill3_Buff` | 技能 3：增益 |
| `testEnemyAI_TrackPlayer` | 敌人 AI 追踪 |
| `testEnemyAI_AttackPlayer` | 敌人 AI 攻击 |
| `testCheckGameOver_PlayerDead` | 失败条件检测 |
| `testCheckGameOver_Victory` | 胜利条件检测 |
| `testCheckGameOver_NotOver` | 游戏进行中状态 |
| `testSpawnEnemy` | 敌人生成 |
| `testSpawnEnemyMaxLimit` | 敌人数量限制 |
| `testOnEnemyDefeated_AddScore` | 击败敌人加分 |
| `testGetters` | Getter 方法测试 |

---

## 关键设计决策

### 1. 技能系统设计

采用简单的索引式技能系统：
- 优点：易于扩展新技能
- 优点：技能效果直接编码，性能高
- 缺点：技能配置需要修改代码

未来可扩展为数据驱动的技能系统。

### 2. 敌人 AI 设计

使用简单的追踪 AI：
- 当玩家在攻击范围内时攻击
- 否则向玩家移动
- 优点：实现简单，适合 Demo
- 缺点：行为单一

未来可扩展：
- 状态机 AI（巡逻、追击、攻击）
- 群体行为（包围、协同攻击）
- 寻路系统（避开障碍物）

### 3. 胜利条件设计

采用生存模式：
- 简单直接，易于理解
- 时间可配置，便于调整难度
- 易于扩展到其它胜利条件

---

## 文件清单

### 修改的文件

- `src/main/java/io/github/s3s3l/yggdrasil/game/core/demo/scene/DemoGameScene.java`
  - 新增攻击处理方法
  - 新增技能处理方法
  - 新增敌人 AI 方法
  - 新增胜负条件检测
  - 新增 Getter 方法

### 新增的文件

- `src/test/java/io/github/s3s3l/yggdrasil/game/core/demo/scene/DemoGameSceneTest.java`
  - 17 个单元测试

---

## 游戏流程

### 完整游戏循环

```
1. 游戏开始
   ↓
2. 生成敌人（每 3 秒）
   ↓
3. 玩家输入处理
   - 移动（WASD）
   - 攻击（J）
   - 技能（K/L/I）
   ↓
4. 敌人 AI 更新
   - 追踪玩家
   - 攻击玩家
   ↓
5. 更新实体
   - 玩家更新
   - 敌人更新
   ↓
6. 碰撞检测
   ↓
7. 检查游戏结束
   - 玩家死亡 → 失败
   - 生存时间达到 → 胜利
   ↓
8. 重复步骤 2-7
```

---

## 性能考虑

### 当前实现

- 简单的距离检测进行碰撞判断
- 线性遍历敌人列表
- 适合少量敌人（< 100）

### 优化建议

1. **空间分割**：使用四叉树或网格进行空间分割，减少碰撞检测次数
2. **对象池**：敌人对象池，减少 GC 压力
3. **批量更新**：并行更新多个敌人

---

## 后续改进

### Iteration 35 待办

1. **UI 反馈完善**
   - 血条实时更新
   - 分数显示更新
   - 游戏结束对话框显示

2. **场景切换**
   - 主菜单 → 游戏场景
   - 游戏场景 → 暂停菜单
   - 游戏结束 → 主菜单

3. **性能测试**
   - 大量敌人压力测试
   - 帧率稳定性测试

4. **游戏平衡**
   - 敌人难度调整
   - 技能效果平衡
   - 胜利时间调整

---

## Git 提交

提交哈希：待创建

提交信息：
```
feat(core): Iteration 34 - 游戏逻辑实现

实现完整的游戏逻辑：

玩家控制:
- 攻击输入处理（J 键）
- 技能输入处理（K/L/I键）
- 攻击冷却机制

战斗系统:
- 玩家攻击检测（范围检测）
- 敌人 AI 追踪和攻击
- 伤害计算和生命恢复

胜负条件:
- 生存模式（120 秒）
- 失败条件（玩家死亡）
- 游戏结束回调

单元测试:
- DemoGameSceneTest: 17 个测试
- 覆盖攻击、技能、AI、胜负条件
```

---

## 技术备注

1. **无 GUI 测试**: 所有测试都是纯 JUnit 单元测试，不依赖图形界面。

2. **时间管理**: 游戏时间通过 `gameTimer` 累加 `deltaTime` 计算，确保跨平台一致性。

3. **扩展性**: 通过回调方法（如 `onPlayerAttack`、`onEnemyAttackHit`）提供扩展点，子类可自定义行为。

4. **敌人 AI 简化**: 当前 AI 仅支持直线追踪，适合开阔场景。复杂场景需要寻路系统。
