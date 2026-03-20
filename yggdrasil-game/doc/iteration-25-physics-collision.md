# Iteration 25 - 物理碰撞系统（一）

## 迭代目标

实现基础碰撞检测系统，为游戏对象提供碰撞体定义和碰撞检测能力。

## 变更内容

### 新增核心类

#### 1. 碰撞体接口和实现

**Collider** (`core/physics/Collider.java`)
- 碰撞体基础接口
- 定义 `bounds()`, `contains()`, `intersects()` 等核心方法
- 支持位置偏移和旋转

**BoxCollider** (`core/physics/BoxCollider.java`)
- 矩形碰撞体实现
- AABB（轴对齐包围盒）检测
- 支持大小、中心点设定

**CircleCollider** (`core/physics/CircleCollider.java`)
- 圆形碰撞体实现
- 圆 - 圆、圆 - 矩形检测
- 支持半径、中心点设定

**PolygonCollider** (`core/physics/PolygonCollider.java`)
- 多边形碰撞体实现
- SAT（分离轴定理）检测
- 支持顶点列表、旋转

#### 2. 碰撞检测工具

**CollisionDetector** (`core/physics/CollisionDetector.java`)
- 静态工具类
- 提供所有碰撞体类型的检测组合
- `checkCollision(Collider, Collider)` 统一入口

**CollisionInfo** (`core/physics/CollisionInfo.java`)
- 碰撞信息数据类
- 包含碰撞点、法线、穿透深度

#### 3. 射线检测

**Ray** (`core/physics/Ray.java`)
- 射线定义（原点 + 方向）
- 2D 射线表示

**RayCastHit** (`core/physics/RayCastHit.java`)
- 射线命中信息
- 包含命中点、法线、距离、碰撞体引用

**RayCaster** (`core/physics/RayCaster.java`)
- 射线投射工具
- `cast(Ray, Collider)` 检测射线与碰撞体相交
- `castAll(Ray, List<Collider>)` 检测多个碰撞体

### 新增测试类

- `ColliderTest.java` - 碰撞体基础测试
- `BoxColliderTest.java` - 矩形碰撞体测试
- `CircleColliderTest.java` - 圆形碰撞体测试
- `PolygonColliderTest.java` - 多边形碰撞体测试
- `CollisionDetectorTest.java` - 碰撞检测测试
- `RayCasterTest.java` - 射线投射测试

## 技术细节

### AABB 检测（Box vs Box）

```java
boolean intersects(BoxCollider a, BoxCollider b) {
    return Math.abs(a.centerX - b.centerX) < (a.halfWidth + b.halfWidth) &&
           Math.abs(a.centerY - b.centerY) < (a.halfHeight + b.halfHeight);
}
```

### 圆 - 圆检测

```java
boolean intersects(CircleCollider a, CircleCollider b) {
    float dx = a.centerX - b.centerX;
    float dy = a.centerY - b.centerY;
    float radiiSum = a.radius + b.radius;
    return dx * dx + dy * dy < radiiSum * radiiSum;
}
```

### 圆 - 矩形检测

```java
boolean intersects(CircleCollider circle, BoxCollider box) {
    float closestX = clamp(circle.centerX, box.left, box.right);
    float closestY = clamp(circle.centerY, box.top, box.bottom);
    float dx = circle.centerX - closestX;
    float dy = circle.centerY - closestY;
    return dx * dx + dy * dy < circle.radius * circle.radius;
}
```

### SAT（分离轴定理）- 多边形检测

```java
boolean intersects(PolygonCollider a, PolygonCollider b) {
    // 检查所有边的法线作为分离轴
    for (Vector2 axis : getAxes(a, b)) {
        if (!overlapOnAxis(a, b, axis)) {
            return false; // 找到分离轴，无碰撞
        }
    }
    return true; // 所有轴都有重叠，发生碰撞
}
```

### 射线 - 碰撞体检测

```java
RayCastHit cast(Ray ray, Collider collider) {
    // 将射线表示为参数方程 P(t) = origin + direction * t
    // 求解与碰撞体边界的交点
    // 返回最近的交点信息
}
```

## 影响范围

### 依赖模块
- 无外部依赖，纯核心模块实现

### 受影响的类
- `GameObject` - 未来将集成碰撞体组件

## 使用示例

```java
// 创建碰撞体
BoxCollider box = new BoxCollider(100f, 50f, 20f, 10f);
CircleCollider circle = new CircleCollider(0f, 0f, 15f);

// 检测碰撞
boolean collides = CollisionDetector.checkCollision(box, circle);
if (collides) {
    CollisionInfo info = CollisionDetector.getCollisionInfo(box, circle);
    System.out.println("碰撞点：" + info.contactPoint);
    System.out.println("法线：" + info.normal);
}

// 射线检测
Ray ray = new Ray(new Vector2(0, 0), new Vector2(1, 0).normalized());
RayCastHit hit = RayCaster.cast(ray, box);
if (hit != null) {
    System.out.println("命中距离：" + hit.distance);
    System.out.println("命中点：" + hit.point);
}
```

## 测试结果

- 总测试数：1827
- 新增测试：103 (全部通过)
- 失败：0
- 跳过：1

## Git 提交

- Commit: `bd62c13`
- Message: `feat(core): Iteration 25 - 实现物理碰撞系统（一）`

## 后续工作

- [ ] 物理世界管理（Iteration 26）
- [ ] 刚体物理和碰撞响应
- [ ] 空间划分优化（四叉树）
- [ ] 碰撞体组件集成到 GameObject
