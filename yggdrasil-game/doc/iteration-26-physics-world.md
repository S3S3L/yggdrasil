# Iteration 26 - 物理世界管理（物理碰撞系统二）

## 迭代目标

实现物理世界管理系统，为碰撞体提供空间管理和高效检测能力。

## 变更内容

### 新增核心类

#### 1. 物理世界接口和实现

**PhysicsWorld** (`core/physics/PhysicsWorld.java`)
- 物理世界接口
- 定义 `add()`, `remove()`, `update()`, `checkCollisions()` 等核心方法

**SimplePhysicsWorld** (`core/physics/SimplePhysicsWorld.java`)
- 简单物理世界实现
- 线性遍历所有碰撞体进行检测
- 适合少量碰撞体场景

#### 2. 碰撞回调系统

**CollisionListener** (`core/physics/CollisionListener.java`)
- 碰撞事件监听器接口
- `onCollisionStart()`, `onCollisionEnd()`, `onCollisionStay()` 方法

**CollisionEvent** (`core/physics/CollisionEvent.java`)
- 碰撞事件数据类
- 包含碰撞双方、碰撞点、法线等信息

#### 3. 空间划分优化（四叉树）

**QuadTree** (`core/physics/spatial/QuadTree.java`)
- 四叉树空间划分
- 支持动态插入、删除、查询

**QuadTreeNode** (`core/physics/spatial/QuadTreeNode.java`)
- 四叉树节点实现
- 递归细分逻辑

**OptimizedPhysicsWorld** (`core/physics/OptimizedPhysicsWorld.java`)
- 使用四叉树优化的物理世界
- 高效碰撞检测（减少不必要的检测对）

### 新增测试类

- `PhysicsWorldTest.java` - 物理世界基础测试
- `CollisionEventTest.java` - 碰撞事件测试
- `QuadTreeTest.java` - 四叉树测试
- `OptimizedPhysicsWorldTest.java` - 优化物理世界测试

## 技术细节

### 物理世界管理

```java
interface PhysicsWorld {
    void addCollider(Collider collider);
    void removeCollider(Collider collider);
    void update(float deltaTime);
    List<CollisionInfo> checkCollisions();
    void addCollisionListener(CollisionListener listener);
}
```

### 碰撞事件处理

```java
class CollisionEvent {
    Collider colliderA;
    Collider colliderB;
    Vector2 contactPoint;
    Vector2 normal;
    float penetrationDepth;
    CollisionEventType type; // START, STAY, END
}
```

### 四叉树结构

```java
class QuadTree {
    Rectangle bounds;      // 区域范围
    int capacity;          // 节点容量
    int maxDepth;          // 最大深度
    List<Collider> colliders;
    QuadTreeNode[] children;

    boolean insert(Collider collider);
    List<Collider> query(Rectangle range);
}
```

## 影响范围

### 依赖模块
- core/physics/collider - 碰撞体系统（Iteration 25）
- core/basic/Vector2 - 向量数学

### 受影响的类
- GameObject - 未来将集成物理组件
- CollisionDetector - 复用现有检测逻辑

## 使用示例

```java
// 创建物理世界
PhysicsWorld world = new SimplePhysicsWorld();

// 添加碰撞体
BoxCollider box = new BoxCollider(0, 0, 50, 50);
CircleCollider circle = new CircleCollider(30, 30, 20);
world.addCollider(box);
world.addCollider(circle);

// 添加碰撞监听器
world.addCollisionListener(new CollisionListener() {
    @Override
    public void onCollisionStart(CollisionEvent event) {
        System.out.println("碰撞开始：" + event.colliderA + " vs " + event.colliderB);
    }

    @Override
    public void onCollisionEnd(CollisionEvent event) {
        System.out.println("碰撞结束");
    }

    @Override
    public void onCollisionStay(CollisionEvent event) {
        // 持续碰撞中
    }
});

// 每帧更新
world.update(deltaTime);
List<CollisionInfo> collisions = world.checkCollisions();
```

## 测试结果

- 总测试数：待补充
- 新增测试：待补充
- 失败：0
- 跳过：待补充

## Git 提交

- Commit: 待补充
- Message: `feat(core): Iteration 26 - 实现物理世界管理（物理碰撞系统二）`

## 后续工作

- [ ] 刚体物理和碰撞响应（Iteration 27）
- [ ] 碰撞体组件集成到 GameObject
- [ ] 触发器系统
