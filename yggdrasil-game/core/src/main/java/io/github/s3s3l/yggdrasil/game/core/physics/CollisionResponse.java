package io.github.s3s3l.yggdrasil.game.core.physics;

import io.github.s3s3l.yggdrasil.game.core.basic.Vector2;
import io.github.s3s3l.yggdrasil.game.core.physics.collider.CollisionInfo;

/**
 * 碰撞响应工具类
 * <p>
 * 计算碰撞后的冲量、反弹和摩擦效果
 */
public class CollisionResponse {

    private CollisionResponse() {
        // 工具类不允许实例化
    }

    /**
     * 计算碰撞冲量
     * <p>
     * 使用一维碰撞公式，考虑质量和弹性系数
     *
     * @param info 碰撞信息
     * @param rb1  第一个刚体
     * @param rb2  第二个刚体
     * @return 冲量大小（标量）
     */
    public static float calculateImpulse(CollisionInfo info, RigidbodyComponent rb1, RigidbodyComponent rb2) {
        if (info == null || !info.collided || rb1 == null || rb2 == null) {
            return 0;
        }

        Vector2 normal = info.normal;
        if (normal == null) {
            return 0;
        }

        // 计算相对速度
        Vector2 relVel = rb2.getVelocity().sub(rb1.getVelocity());

        // 计算相对速度在法线方向的分量
        float velAlongNormal = relVel.dot(normal);

        // 如果速度方向与法线方向相同（分离中），不需要响应
        if (velAlongNormal > 0) {
            return 0;
        }

        // 计算合成弹性系数（取两者平均值）
        float restitution = (rb1.getRestitution() + rb2.getRestitution()) / 2.0f;

        // 计算冲量标量
        // j = -(1 + e) * v_rel · n / (1/m1 + 1/m2)
        float invMass1 = rb1.getInverseMass();
        float invMass2 = rb2.getInverseMass();

        // 处理质量为 0 的情况（静态刚体）
        if (invMass1 == 0 && invMass2 == 0) {
            return 0;
        }

        float impulseScalar = -(1 + restitution) * velAlongNormal;
        impulseScalar /= (invMass1 + invMass2);

        return impulseScalar;
    }

    /**
     * 应用碰撞冲量到两个刚体
     *
     * @param info 碰撞信息
     * @param rb1  第一个刚体
     * @param rb2  第二个刚体
     */
    public static void applyImpulse(CollisionInfo info, RigidbodyComponent rb1, RigidbodyComponent rb2) {
        float impulseScalar = calculateImpulse(info, rb1, rb2);

        if (impulseScalar <= 0) {
            return;
        }

        Vector2 impulse = info.normal.scale(impulseScalar);

        // 对 rb1 施加反向冲量
        rb1.applyImpulse(impulse.scale(-1));

        // 对 rb2 施加正向冲量
        rb2.applyImpulse(impulse);
    }

    /**
     * 计算并应用摩擦力冲量
     * <p>
     * 使用库仑摩擦模型
     *
     * @param info 碰撞信息
     * @param rb1  第一个刚体
     * @param rb2  第二个刚体
     */
    public static void applyFriction(CollisionInfo info, RigidbodyComponent rb1, RigidbodyComponent rb2) {
        if (info == null || !info.collided || rb1 == null || rb2 == null) {
            return;
        }

        // 计算相对速度
        Vector2 relVel = rb2.getVelocity().sub(rb1.getVelocity());

        // 计算切线方向（垂直于法线）
        Vector2 tangent = new Vector2(-info.normal.y, info.normal.x);

        // 计算相对速度在切线方向的分量
        float velAlongTangent = relVel.dot(tangent);

        // 计算质量和
        float invMass1 = rb1.getInverseMass();
        float invMass2 = rb2.getInverseMass();
        float invMassSum = invMass1 + invMass2;

        if (invMassSum == 0) {
            return; // 两个都是静态刚体
        }

        // 计算合成摩擦系数
        float friction = (rb1.getFriction() + rb2.getFriction()) / 2.0f;

        // 计算法向冲量（用于限制摩擦冲量）
        // 使用相对速度的法向分量来计算，即使它已经被应用了
        float velAlongNormal = relVel.dot(info.normal);
        float normalImpulse = Math.abs(velAlongNormal) / invMassSum;
        float maxFrictionImpulse = normalImpulse * friction;

        // 计算摩擦冲量（考虑质量）
        float impulseScalar = -velAlongTangent / invMassSum;

        // 库仑摩擦：限制摩擦冲量
        // 限制摩擦冲量大小
        impulseScalar = Math.max(-maxFrictionImpulse, Math.min(maxFrictionImpulse, impulseScalar));

        if (Math.abs(impulseScalar) < 0.001f) {
            return;
        }

        // 应用摩擦冲量
        Vector2 frictionImpulse = tangent.scale(impulseScalar);

        if (invMass1 > 0) {
            rb1.applyImpulse(frictionImpulse.scale(-1));
        }
        if (invMass2 > 0) {
            rb2.applyImpulse(frictionImpulse);
        }
    }

    /**
     * 位置修正（防止穿透）
     * <p>
     * 直接修正刚体位置，避免陷入
     *
     * @param info 碰撞信息
     * @param rb1  第一个刚体
     * @param rb2  第二个刚体
     * @param slop 穿透容差
     */
    public static void positionCorrection(CollisionInfo info, RigidbodyComponent rb1, RigidbodyComponent rb2, float slop) {
        if (info == null || !info.collided || rb1 == null || rb2 == null) {
            return;
        }

        float penetration = info.penetrationDepth;

        // 如果穿透小于容差，不修正
        if (penetration < slop) {
            return;
        }

        // 计算修正百分比（通常修正 20-40%）
        float correctionPercent = 0.4f;
        float correction = Math.max(penetration - slop, 0) * correctionPercent;

        // 计算修正方向
        Vector2 correctionVec = info.normal.scale(correction);

        float invMass1 = rb1.getInverseMass();
        float invMass2 = rb2.getInverseMass();

        // 根据质量比例分配位置修正
        float totalInvMass = invMass1 + invMass2;

        if (totalInvMass == 0) {
            return; // 两个都是静态刚体
        }

        // 质量越小，修正越多
        if (invMass1 > 0) {
            float ratio1 = invMass1 / totalInvMass;
            Vector2 offset1 = correctionVec.scale(ratio1);
            rb1.setPosition(rb1.getPosition().sub(offset1));
        }

        if (invMass2 > 0) {
            float ratio2 = invMass2 / totalInvMass;
            Vector2 offset2 = correctionVec.scale(ratio2);
            rb2.setPosition(rb2.getPosition().add(offset2));
        }
    }

    /**
     * 位置修正（防止穿透）- 使用默认容差
     *
     * @param info 碰撞信息
     * @param rb1  第一个刚体
     * @param rb2  第二个刚体
     */
    public static void positionCorrection(CollisionInfo info, RigidbodyComponent rb1, RigidbodyComponent rb2) {
        positionCorrection(info, rb1, rb2, 0.01f);
    }

    /**
     * 处理完全弹性碰撞（一个刚体）
     * <p>
     * 用于处理刚体与静态墙壁的碰撞
     *
     * @param normal 碰撞法线
     * @param rb     刚体
     */
    public static void resolveBounce(Vector2 normal, RigidbodyComponent rb) {
        if (normal == null || rb == null) {
            return;
        }

        Vector2 velocity = rb.getVelocity();

        // 计算法线方向的速度分量
        float velAlongNormal = velocity.dot(normal);

        // 如果速度方向与法线相同（分离中），不需要响应
        if (velAlongNormal > 0) {
            return;
        }

        // 反弹速度 = -velocity · normal * restitution
        float restitution = rb.getRestitution();
        float bounceVel = -velAlongNormal * restitution;

        // 更新速度
        Vector2 newVel = normal.scale(bounceVel);
        velocity.set(newVel);
        rb.setVelocity(velocity);
    }
}
