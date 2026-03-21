package io.github.s3s3l.yggdrasil.game.core.physics;

import io.github.s3s3l.yggdrasil.game.core.basic.Vector2;
import io.github.s3s3l.yggdrasil.game.core.physics.collider.BoxCollider;
import io.github.s3s3l.yggdrasil.game.core.physics.collider.Collider;
import io.github.s3s3l.yggdrasil.game.core.physics.collider.CollisionInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 碰撞响应单元测试
 */
class CollisionResponseTest {

    private RigidbodyComponent rb1;
    private RigidbodyComponent rb2;

    @BeforeEach
    void setUp() {
        rb1 = new RigidbodyComponent(10.0f, new Vector2(0, 0), 0);
        rb2 = new RigidbodyComponent(10.0f, new Vector2(20, 0), 0);
    }

    @Test
    void testCalculateImpulse() {
        // 创建碰撞信息：rb1 向右移动，撞击静止的 rb2
        CollisionInfo info = createCollisionInfo(new Vector2(1, 0));

        rb1.setVelocity(new Vector2(10, 0));
        rb2.setVelocity(new Vector2(0, 0));

        float impulse = CollisionResponse.calculateImpulse(info, rb1, rb2);

        // 应该有正冲量
        assertTrue(impulse > 0);
    }

    @Test
    void testCalculateImpulseWithRestitution() {
        CollisionInfo info = createCollisionInfo(new Vector2(1, 0));

        rb1.setVelocity(new Vector2(10, 0));
        rb1.setRestitution(1.0f); // 完全弹性
        rb2.setVelocity(new Vector2(0, 0));

        float impulse = CollisionResponse.calculateImpulse(info, rb1, rb2);

        // 完全弹性碰撞冲量更大
        assertTrue(impulse > 0);
    }

    @Test
    void testCalculateImpulseNoCollision() {
        CollisionInfo info = new CollisionInfo();
        info.collided = false;

        float impulse = CollisionResponse.calculateImpulse(info, rb1, rb2);

        assertEquals(0, impulse, 0.001f);
    }

    @Test
    void testCalculateImpulseSeparating() {
        // 法线方向与速度方向相同（分离中）
        CollisionInfo info = createCollisionInfo(new Vector2(-1, 0));

        rb1.setVelocity(new Vector2(10, 0));
        rb2.setVelocity(new Vector2(0, 0));

        float impulse = CollisionResponse.calculateImpulse(info, rb1, rb2);

        // 分离中，冲量为 0
        assertEquals(0, impulse, 0.001f);
    }

    @Test
    void testApplyImpulse() {
        CollisionInfo info = createCollisionInfo(new Vector2(1, 0));

        rb1.setVelocity(new Vector2(10, 0));
        rb2.setVelocity(new Vector2(0, 0));

        CollisionResponse.applyImpulse(info, rb1, rb2);

        // rb1 速度应该减小（反向冲量）
        assertTrue(rb1.getVelocity().x < 10);

        // rb2 速度应该增加（正向冲量）
        assertTrue(rb2.getVelocity().x > 0);
    }

    @Test
    void testApplyImpulseConservation() {
        CollisionInfo info = createCollisionInfo(new Vector2(1, 0));

        rb1.setMass(5.0f);
        rb2.setMass(5.0f);
        rb1.setVelocity(new Vector2(10, 0));
        rb2.setVelocity(new Vector2(0, 0));

        float initialMomentum = rb1.getMomentum().x + rb2.getMomentum().x;

        CollisionResponse.applyImpulse(info, rb1, rb2);

        float finalMomentum = rb1.getMomentum().x + rb2.getMomentum().x;

        // 动量守恒（允许小误差）
        assertEquals(initialMomentum, finalMomentum, 0.1f);
    }

    @Test
    void testApplyFriction() {
        // 法线向右，切线方向为 Y 轴
        // rb1 从左边撞向 rb2，有向上的切线速度
        CollisionInfo info = createCollisionInfo(new Vector2(1, 0));
        info.normal = new Vector2(1, 0); // 确保法线向右

        // 设置切线方向速度（Y 方向）和法向速度
        rb1.setVelocity(new Vector2(10, 5)); // 向右撞，有向上切线速度
        rb2.setVelocity(new Vector2(0, 0));

        // 先施加法向冲量，再测试摩擦
        CollisionResponse.applyImpulse(info, rb1, rb2);
        CollisionResponse.applyFriction(info, rb1, rb2);

        // 摩擦力应该减少切线方向速度（Y 方向）
        // 使用更大的容差，因为摩擦效果可能较小
        assertNotEquals(5.0f, rb1.getVelocity().y, 0.5f);
    }

    @Test
    void testPositionCorrection() {
        CollisionInfo info = createCollisionInfo(new Vector2(1, 0));
        info.penetrationDepth = 0.5f;

        Vector2 initialPos1 = new Vector2(rb1.getPosition());
        Vector2 initialPos2 = new Vector2(rb2.getPosition());

        CollisionResponse.positionCorrection(info, rb1, rb2, 0.01f);

        // 位置应该被修正
        assertNotEquals(initialPos1.x, rb1.getPosition().x, 0.001f);
        assertNotEquals(initialPos2.x, rb2.getPosition().x, 0.001f);
    }

    @Test
    void testPositionCorrectionSmallPenetration() {
        CollisionInfo info = createCollisionInfo(new Vector2(1, 0));
        info.penetrationDepth = 0.005f; // 小于容差

        Vector2 initialPos1 = new Vector2(rb1.getPosition());
        Vector2 initialPos2 = new Vector2(rb2.getPosition());

        CollisionResponse.positionCorrection(info, rb1, rb2, 0.01f);

        // 穿透小于容差，不修正
        assertEquals(initialPos1.x, rb1.getPosition().x, 0.001f);
        assertEquals(initialPos2.x, rb2.getPosition().x, 0.001f);
    }

    @Test
    void testPositionCorrectionStaticRigidbody() {
        CollisionInfo info = createCollisionInfo(new Vector2(1, 0));
        info.penetrationDepth = 0.5f;

        rb1.setMass(0); // 静态

        Vector2 initialPos2 = new Vector2(rb2.getPosition());

        CollisionResponse.positionCorrection(info, rb1, rb2, 0.01f);

        // 静态刚体不动，只有动态刚体移动
        assertEquals(0, rb1.getPosition().x, 0.001f);
        assertNotEquals(initialPos2.x, rb2.getPosition().x, 0.001f);
    }

    @Test
    void testResolveBounce() {
        Vector2 normal = new Vector2(0, 1); // 向上的法线
        rb1.setVelocity(new Vector2(0, -10)); // 向下速度

        CollisionResponse.resolveBounce(normal, rb1);

        // 速度应该反弹向上
        assertTrue(rb1.getVelocity().y > 0);
    }

    @Test
    void testResolveBounceWithRestitution() {
        Vector2 normal = new Vector2(0, 1);
        rb1.setVelocity(new Vector2(0, -10));
        rb1.setRestitution(0.5f);

        CollisionResponse.resolveBounce(normal, rb1);

        // 反弹速度应该是原来的一半
        assertEquals(5, rb1.getVelocity().y, 0.5f);
    }

    @Test
    void testResolveBounceNoOppositeVelocity() {
        Vector2 normal = new Vector2(0, 1);
        rb1.setVelocity(new Vector2(0, 10)); // 已经分离

        CollisionResponse.resolveBounce(normal, rb1);

        // 速度不变
        assertEquals(10, rb1.getVelocity().y, 0.001f);
    }

    @Test
    void testNullSafety() {
        // 所有方法都应该能处理 null 输入
        assertDoesNotThrow(() -> CollisionResponse.applyImpulse(null, rb1, rb2));
        assertDoesNotThrow(() -> CollisionResponse.applyFriction(null, rb1, rb2));
        assertDoesNotThrow(() -> CollisionResponse.positionCorrection(null, rb1, rb2));
        assertDoesNotThrow(() -> CollisionResponse.resolveBounce(null, rb1));
    }

    @Test
    void testUnequalMassCollision() {
        CollisionInfo info = createCollisionInfo(new Vector2(1, 0));

        rb1.setMass(1.0f); // 轻
        rb2.setMass(100.0f); // 重
        rb1.setVelocity(new Vector2(10, 0));
        rb2.setVelocity(new Vector2(0, 0));

        CollisionResponse.applyImpulse(info, rb1, rb2);

        // 轻的物体应该反弹更多
        assertTrue(rb1.getVelocity().x < 0); // 反弹
        assertTrue(rb2.getVelocity().x > 0); // 轻微移动
        assertTrue(rb2.getVelocity().x < 1); // 移动很小
    }

    private CollisionInfo createCollisionInfo(Vector2 normal) {
        CollisionInfo info = new CollisionInfo();
        info.collided = true;
        info.normal = normal;
        info.contactPoint = new Vector2(10, 0);
        info.penetrationDepth = 0.1f;
        info.colliderA = new BoxCollider(0, 0, 10, 10);
        info.colliderB = new BoxCollider(20, 0, 10, 10);
        return info;
    }
}
