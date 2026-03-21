package io.github.s3s3l.yggdrasil.game.core.physics;

import io.github.s3s3l.yggdrasil.game.core.basic.Vector2;
import io.github.s3s3l.yggdrasil.game.core.physics.collider.Collider;

/**
 * 刚体组件
 * <p>
 * 为游戏对象提供物理属性，包括质量、速度、角速度等
 * 支持与碰撞体集成，实现物理模拟
 */
public class RigidbodyComponent {

    // ==================== 质量属性 ====================

    /**
     * 质量（kg）
     * 0 表示静态刚体（不受力影响）
     */
    private float mass;

    /**
     * 质量的倒数，用于物理计算优化
     */
    private float inverseMass;

    /**
     * 转动惯量
     */
    private float momentOfInertia;

    /**
     * 转动惯量的倒数
     */
    private float inverseMomentOfInertia;

    // ==================== 运动属性 ====================

    /**
     * 线速度
     */
    private Vector2 velocity;

    /**
     * 角速度（弧度/秒）
     */
    private float angularVelocity;

    // ==================== 力 accumulators ====================

    /**
     * 累积的外力
     */
    private Vector2 forceAccumulator;

    /**
     * 累积的外力矩
     */
    private float torqueAccumulator;

    // ==================== 物理材质属性 ====================

    /**
     * 弹性系数（0-1）
     * 0 = 完全非弹性，1 = 完全弹性
     */
    private float restitution = 0.5f;

    /**
     * 摩擦系数
     */
    private float friction = 0.5f;

    // ==================== 状态 ====================

    /**
     * 是否激活（受物理模拟）
     */
    private boolean awake = true;

    /**
     * 是否为静态刚体（不受力影响）
     */
    private boolean kinematic = false;

    /**
     * 是否允许旋转
     */
    private boolean fixedRotation = false;

    // ==================== 关联的碰撞体 ====================

    /**
     * 关联的碰撞体
     */
    private Collider collider;

    // ==================== 位置（用于与 GameObject 同步） ====================

    /**
     * 位置
     */
    private Vector2 position;

    /**
     * 旋转角度（弧度）
     */
    private float rotation;

    // ==================== 构造函数 ====================

    /**
     * 创建刚体组件
     *
     * @param mass 质量（0 表示静态）
     */
    public RigidbodyComponent(float mass) {
        this(mass, new Vector2(), 0);
    }

    /**
     * 创建刚体组件
     *
     * @param mass     质量
     * @param position 初始位置
     * @param rotation 初始旋转角度（弧度）
     */
    public RigidbodyComponent(float mass, Vector2 position, float rotation) {
        this.mass = mass;
        this.position = position != null ? position : new Vector2();
        this.rotation = rotation;
        this.velocity = new Vector2();
        this.forceAccumulator = new Vector2();

        updateInverseMass();
    }

    // ==================== 质量属性方法 ====================

    /**
     * 获取质量
     *
     * @return 质量
     */
    public float getMass() {
        return mass;
    }

    /**
     * 设置质量
     *
     * @param mass 质量值
     */
    public void setMass(float mass) {
        this.mass = Math.max(0, mass);
        updateInverseMass();
    }

    /**
     * 获取质量的倒数
     *
     * @return 质量的倒数
     */
    public float getInverseMass() {
        return inverseMass;
    }

    /**
     * 获取转动惯量
     *
     * @return 转动惯量
     */
    public float getMomentOfInertia() {
        return momentOfInertia;
    }

    /**
     * 设置转动惯量
     *
     * @param momentOfInertia 转动惯量
     */
    public void setMomentOfInertia(float momentOfInertia) {
        this.momentOfInertia = Math.max(0, momentOfInertia);
        updateInverseMomentOfInertia();
    }

    /**
     * 获取转动惯量的倒数
     *
     * @return 转动惯量的倒数
     */
    public float getInverseMomentOfInertia() {
        return inverseMomentOfInertia;
    }

    private void updateInverseMass() {
        this.inverseMass = (mass > 0) ? (1.0f / mass) : 0.0f;
    }

    private void updateInverseMomentOfInertia() {
        this.inverseMomentOfInertia = (momentOfInertia > 0) ? (1.0f / momentOfInertia) : 0.0f;
    }

    // ==================== 运动属性方法 ====================

    /**
     * 获取线速度
     *
     * @return 线速度向量
     */
    public Vector2 getVelocity() {
        return velocity;
    }

    /**
     * 设置线速度
     *
     * @param velocity 线速度向量
     */
    public void setVelocity(Vector2 velocity) {
        if (velocity != null) {
            this.velocity.set(velocity);
        } else {
            this.velocity.set(0, 0);
        }
    }

    /**
     * 设置线速度
     *
     * @param x X 方向速度
     * @param y Y 方向速度
     */
    public void setVelocity(float x, float y) {
        this.velocity.set(x, y);
    }

    /**
     * 获取角速度
     *
     * @return 角速度（弧度/秒）
     */
    public float getAngularVelocity() {
        return angularVelocity;
    }

    /**
     * 设置角速度
     *
     * @param angularVelocity 角速度
     */
    public void setAngularVelocity(float angularVelocity) {
        this.angularVelocity = angularVelocity;
    }

    // ==================== 位置方法 ====================

    /**
     * 获取位置
     *
     * @return 位置向量
     */
    public Vector2 getPosition() {
        return position;
    }

    /**
     * 设置位置
     *
     * @param position 位置向量
     */
    public void setPosition(Vector2 position) {
        if (position != null) {
            this.position.set(position);
            syncColliderPosition();
        }
    }

    /**
     * 设置位置
     *
     * @param x X 坐标
     * @param y Y 坐标
     */
    public void setPosition(float x, float y) {
        this.position.set(x, y);
        syncColliderPosition();
    }

    /**
     * 获取旋转角度
     *
     * @return 旋转角度（弧度）
     */
    public float getRotation() {
        return rotation;
    }

    /**
     * 设置旋转角度
     *
     * @param rotation 旋转角度（弧度）
     */
    public void setRotation(float rotation) {
        this.rotation = rotation;
    }

    private void syncColliderPosition() {
        if (collider != null) {
            collider.setPosition(position);
        }
    }

    // ==================== 外力施加方法 ====================

    /**
     * 施加外力
     *
     * @param force 力向量
     */
    public void addForce(Vector2 force) {
        if (force != null && !kinematic && mass > 0) {
            forceAccumulator.addLocal(force);
            awake = true;
        }
    }

    /**
     * 施加外力
     *
     * @param x X 方向力
     * @param y Y 方向力
     */
    public void addForce(float x, float y) {
        if (!kinematic && mass > 0) {
            forceAccumulator.x += x;
            forceAccumulator.y += y;
            awake = true;
        }
    }

    /**
     * 施加冲量（瞬时改变速度）
     *
     * @param impulse 冲量向量
     */
    public void applyImpulse(Vector2 impulse) {
        if (impulse != null && !kinematic && mass > 0) {
            velocity.x += impulse.x * inverseMass;
            velocity.y += impulse.y * inverseMass;
            awake = true;
        }
    }

    /**
     * 施加力矩
     *
     * @param torque 力矩值
     */
    public void addTorque(float torque) {
        if (!kinematic && momentOfInertia > 0) {
            torqueAccumulator += torque;
            awake = true;
        }
    }

    /**
     * 施加冲量矩（瞬时改变角速度）
     *
     * @param angularImpulse 冲量矩值
     */
    public void applyAngularImpulse(float angularImpulse) {
        if (!kinematic && momentOfInertia > 0) {
            angularVelocity += angularImpulse * inverseMomentOfInertia;
            awake = true;
        }
    }

    // ==================== 物理更新方法 ====================

    /**
     * 更新物理状态
     *
     * @param deltaTime 时间增量（秒）
     */
    public void update(float deltaTime) {
        if (kinematic) {
            // 运动学刚体：根据速度更新位置，但不受力影响
            position.x += velocity.x * deltaTime;
            position.y += velocity.y * deltaTime;
            if (!fixedRotation) {
                rotation += angularVelocity * deltaTime;
            }
            syncColliderPosition();
            return;
        }

        if (!awake) {
            return;
        }

        // 应用外力计算加速度：F = ma => a = F/m
        Vector2 acceleration = new Vector2(
                forceAccumulator.x * inverseMass,
                forceAccumulator.y * inverseMass
        );

        // 更新速度：v = v0 + a*t
        velocity.x += acceleration.x * deltaTime;
        velocity.y += acceleration.y * deltaTime;

        // 应用角加速度：τ = Iα => α = τ/I
        float angularAcceleration = torqueAccumulator * inverseMomentOfInertia;

        // 更新角速度：ω = ω0 + α*t
        angularVelocity += angularAcceleration * deltaTime;

        // 应用阻尼（简化空气阻力）
        applyDamping(deltaTime);

        // 更新位置：p = p0 + v*t
        position.x += velocity.x * deltaTime;
        position.y += velocity.y * deltaTime;

        // 更新旋转
        if (!fixedRotation) {
            rotation += angularVelocity * deltaTime;
        }

        // 同步碰撞体位置
        syncColliderPosition();

        // 清除力积累器
        clearAccumulators();

        // 检查是否进入睡眠
        checkSleep();
    }

    /**
     * 应用阻尼
     *
     * @param deltaTime 时间增量
     */
    private void applyDamping(float deltaTime) {
        // 线性阻尼（简化为 0.95）
        float linearDamping = 0.95f;
        float angularDamping = 0.9f;

        // 指数衰减
        float linDamp = (float) Math.pow(linearDamping, deltaTime);
        float angDamp = (float) Math.pow(angularDamping, deltaTime);

        velocity.x *= linDamp;
        velocity.y *= linDamp;
        angularVelocity *= angDamp;
    }

    /**
     * 清除力积累器
     */
    public void clearAccumulators() {
        forceAccumulator.set(0, 0);
        torqueAccumulator = 0;
    }

    /**
     * 检查是否进入睡眠状态
     */
    private void checkSleep() {
        float speedSquared = velocity.lengthSquared();
        float minSpeed = 0.01f;

        if (speedSquared < minSpeed * minSpeed && Math.abs(angularVelocity) < 0.01f) {
            // 速度很低，可能进入睡眠
            awake = false;
        }
    }

    // ==================== 状态方法 ====================

    /**
     * 检查是否激活
     *
     * @return 是否激活
     */
    public boolean isAwake() {
        return awake;
    }

    /**
     * 唤醒刚体
     */
    public void wakeUp() {
        awake = true;
    }

    /**
     * 使刚体进入睡眠
     */
    public void putToSleep() {
        awake = false;
        velocity.set(0, 0);
        angularVelocity = 0;
        clearAccumulators();
    }

    /**
     * 检查是否为运动学刚体
     *
     * @return 是否为运动学刚体
     */
    public boolean isKinematic() {
        return kinematic;
    }

    /**
     * 设置是否为运动学刚体
     *
     * @param kinematic 是否为运动学刚体
     */
    public void setKinematic(boolean kinematic) {
        this.kinematic = kinematic;
        if (kinematic) {
            putToSleep();
        }
    }

    /**
     * 检查是否固定旋转
     *
     * @return 是否固定旋转
     */
    public boolean isFixedRotation() {
        return fixedRotation;
    }

    /**
     * 设置是否固定旋转
     *
     * @param fixedRotation 是否固定旋转
     */
    public void setFixedRotation(boolean fixedRotation) {
        this.fixedRotation = fixedRotation;
    }

    // ==================== 材质属性方法 ====================

    /**
     * 获取弹性系数
     *
     * @return 弹性系数
     */
    public float getRestitution() {
        return restitution;
    }

    /**
     * 设置弹性系数
     *
     * @param restitution 弹性系数（0-1）
     */
    public void setRestitution(float restitution) {
        this.restitution = Math.max(0, Math.min(1, restitution));
    }

    /**
     * 获取摩擦系数
     *
     * @return 摩擦系数
     */
    public float getFriction() {
        return friction;
    }

    /**
     * 设置摩擦系数
     *
     * @param friction 摩擦系数
     */
    public void setFriction(float friction) {
        this.friction = Math.max(0, friction);
    }

    // ==================== 碰撞体关联方法 ====================

    /**
     * 获取关联的碰撞体
     *
     * @return 碰撞体
     */
    public Collider getCollider() {
        return collider;
    }

    /**
     * 设置关联的碰撞体
     *
     * @param collider 碰撞体
     */
    public void setCollider(Collider collider) {
        this.collider = collider;
        if (collider != null) {
            syncColliderPosition();
        }
    }

    // ==================== 工具方法 ====================

    /**
     * 获取动能
     *
     * @return 动能值
     */
    public float getKineticEnergy() {
        float linearEnergy = 0.5f * mass * velocity.lengthSquared();
        float angularEnergy = 0.5f * momentOfInertia * angularVelocity * angularVelocity;
        return linearEnergy + angularEnergy;
    }

    /**
     * 获取动量
     *
     * @return 动量向量
     */
    public Vector2 getMomentum() {
        return new Vector2(velocity.x * mass, velocity.y * mass);
    }

    /**
     * 获取角动量
     *
     * @return 角动量值
     */
    public float getAngularMomentum() {
        return momentOfInertia * angularVelocity;
    }
}
