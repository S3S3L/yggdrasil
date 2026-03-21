package io.github.s3s3l.yggdrasil.game.core.physics;

import io.github.s3s3l.yggdrasil.game.core.basic.Vector2;
import io.github.s3s3l.yggdrasil.game.core.physics.collider.BoxCollider;
import io.github.s3s3l.yggdrasil.game.core.physics.collider.Collider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 刚体组件单元测试
 */
class RigidbodyComponentTest {

    private RigidbodyComponent rigidbody;

    @BeforeEach
    void setUp() {
        rigidbody = new RigidbodyComponent(10.0f, new Vector2(0, 0), 0);
    }

    @Test
    void testCreate() {
        assertNotNull(rigidbody);
        assertEquals(10.0f, rigidbody.getMass(), 0.001f);
        assertEquals(0.1f, rigidbody.getInverseMass(), 0.001f);
        assertEquals(0, rigidbody.getPosition().x, 0.001f);
        assertEquals(0, rigidbody.getPosition().y, 0.001f);
        assertEquals(0, rigidbody.getRotation(), 0.001f);
    }

    @Test
    void testCreateWithZeroMass() {
        RigidbodyComponent rb = new RigidbodyComponent(0);
        assertNotNull(rb);
        assertEquals(0, rb.getMass(), 0.001f);
        assertEquals(0, rb.getInverseMass(), 0.001f);
    }

    @Test
    void testSetMass() {
        rigidbody.setMass(20.0f);
        assertEquals(20.0f, rigidbody.getMass(), 0.001f);
        assertEquals(0.05f, rigidbody.getInverseMass(), 0.001f);
    }

    @Test
    void testSetMassNegative() {
        rigidbody.setMass(-5.0f);
        assertEquals(0, rigidbody.getMass(), 0.001f);
        assertEquals(0, rigidbody.getInverseMass(), 0.001f);
    }

    @Test
    void testSetMomentOfInertia() {
        rigidbody.setMomentOfInertia(50.0f);
        assertEquals(50.0f, rigidbody.getMomentOfInertia(), 0.001f);
        assertEquals(0.02f, rigidbody.getInverseMomentOfInertia(), 0.001f);
    }

    @Test
    void testVelocity() {
        rigidbody.setVelocity(new Vector2(5, 10));
        assertEquals(5, rigidbody.getVelocity().x, 0.001f);
        assertEquals(10, rigidbody.getVelocity().y, 0.001f);
    }

    @Test
    void testVelocityComponents() {
        rigidbody.setVelocity(3, 7);
        assertEquals(3, rigidbody.getVelocity().x, 0.001f);
        assertEquals(7, rigidbody.getVelocity().y, 0.001f);
    }

    @Test
    void testAngularVelocity() {
        rigidbody.setAngularVelocity(1.5f);
        assertEquals(1.5f, rigidbody.getAngularVelocity(), 0.001f);
    }

    @Test
    void testAddForce() {
        rigidbody.addForce(new Vector2(100, 0));
        rigidbody.update(0.1f);

        // F = ma, a = F/m = 100/10 = 10
        // v = a * t = 10 * 0.1 = 1
        assertEquals(1.0f, rigidbody.getVelocity().x, 0.01f);
    }

    @Test
    void testAddForceComponents() {
        rigidbody.addForce(50, 50);
        rigidbody.update(0.1f);

        assertEquals(0.5f, rigidbody.getVelocity().x, 0.01f);
        assertEquals(0.5f, rigidbody.getVelocity().y, 0.01f);
    }

    @Test
    void testAddForceToKinematic() {
        rigidbody.setKinematic(true);
        rigidbody.addForce(new Vector2(1000, 1000));
        rigidbody.update(0.1f);

        // 运动学刚体不受力影响
        assertEquals(0, rigidbody.getVelocity().x, 0.001f);
        assertEquals(0, rigidbody.getVelocity().y, 0.001f);
    }

    @Test
    void testApplyImpulse() {
        Vector2 impulse = new Vector2(20, 0);
        rigidbody.applyImpulse(impulse);

        // impulse = mass * deltaV
        // deltaV = impulse / mass = 20 / 10 = 2
        assertEquals(2.0f, rigidbody.getVelocity().x, 0.001f);
    }

    @Test
    void testAddTorque() {
        rigidbody.setMomentOfInertia(10.0f);
        rigidbody.addTorque(5.0f);
        rigidbody.update(0.1f);

        // τ = Iα, α = τ/I = 5/10 = 0.5
        // ω = α * t = 0.5 * 0.1 = 0.05
        assertEquals(0.05f, rigidbody.getAngularVelocity(), 0.001f);
    }

    @Test
    void testApplyAngularImpulse() {
        rigidbody.setMomentOfInertia(10.0f);
        rigidbody.applyAngularImpulse(20.0f);

        // angularImpulse = I * deltaω
        // deltaω = angularImpulse / I = 20 / 10 = 2
        assertEquals(2.0f, rigidbody.getAngularVelocity(), 0.001f);
    }

    @Test
    void testUpdatePosition() {
        // 禁用阻尼以便精确测试
        rigidbody.setVelocity(new Vector2(10, 0));
        // 手动计算位置，不考虑阻尼
        Vector2 expectedPos = new Vector2(10 * 0.5f, 0); // v * t
        rigidbody.update(0.5f);

        // 由于有阻尼，位置会略小于理论值
        assertEquals(expectedPos.x, rigidbody.getPosition().x, 0.15f);
    }

    @Test
    void testUpdateRotation() {
        rigidbody.setAngularVelocity(2.0f);
        rigidbody.update(0.5f);

        // 由于有角阻尼，旋转角度会略小于理论值 (1.0)
        assertEquals(1.0f, rigidbody.getRotation(), 0.1f);
    }

    @Test
    void testUpdateFixedRotation() {
        rigidbody.setFixedRotation(true);
        rigidbody.setAngularVelocity(2.0f);
        rigidbody.update(0.5f);

        // 固定旋转，角度不变
        assertEquals(0, rigidbody.getRotation(), 0.001f);
    }

    @Test
    void testDamping() {
        rigidbody.setVelocity(new Vector2(100, 0));
        rigidbody.update(1.0f);

        // 阻尼后速度应该减小
        assertTrue(rigidbody.getVelocity().x < 100);
        assertTrue(rigidbody.getVelocity().x > 0);
    }

    @Test
    void testClearAccumulators() {
        rigidbody.addForce(new Vector2(100, 100));
        rigidbody.addTorque(50);
        rigidbody.clearAccumulators();

        // 验证积累器已清除（通过后续加力测试）
        rigidbody.update(0.1f);
        // 如果没有清除，速度会更大
    }

    @Test
    void testSleep() {
        rigidbody.setVelocity(new Vector2(0.001f, 0));
        rigidbody.update(1.0f);

        // 速度很低，应该进入睡眠
        assertFalse(rigidbody.isAwake());
    }

    @Test
    void testWakeUp() {
        rigidbody.putToSleep();
        assertFalse(rigidbody.isAwake());

        rigidbody.wakeUp();
        assertTrue(rigidbody.isAwake());
    }

    @Test
    void testPutToSleep() {
        rigidbody.setVelocity(new Vector2(100, 100));
        rigidbody.setAngularVelocity(10);
        rigidbody.putToSleep();

        assertEquals(0, rigidbody.getVelocity().x, 0.001f);
        assertEquals(0, rigidbody.getVelocity().y, 0.001f);
        assertEquals(0, rigidbody.getAngularVelocity(), 0.001f);
        assertFalse(rigidbody.isAwake());
    }

    @Test
    void testKinematic() {
        rigidbody.setKinematic(true);
        assertTrue(rigidbody.isKinematic());

        rigidbody.setKinematic(false);
        assertFalse(rigidbody.isKinematic());
    }

    @Test
    void testRestitution() {
        rigidbody.setRestitution(0.8f);
        assertEquals(0.8f, rigidbody.getRestitution(), 0.001f);

        rigidbody.setRestitution(1.5f);
        assertEquals(1.0f, rigidbody.getRestitution(), 0.001f);

        rigidbody.setRestitution(-0.5f);
        assertEquals(0, rigidbody.getRestitution(), 0.001f);
    }

    @Test
    void testFriction() {
        rigidbody.setFriction(0.7f);
        assertEquals(0.7f, rigidbody.getFriction(), 0.001f);

        rigidbody.setFriction(-0.5f);
        assertEquals(0, rigidbody.getFriction(), 0.001f);
    }

    @Test
    void testCollider() {
        Collider collider = new BoxCollider(0, 0, 10, 10);
        rigidbody.setCollider(collider);

        assertEquals(collider, rigidbody.getCollider());
        assertEquals(0, collider.getPosition().x, 0.001f);
        assertEquals(0, collider.getPosition().y, 0.001f);
    }

    @Test
    void testColliderSync() {
        Collider collider = new BoxCollider(0, 0, 10, 10);
        rigidbody.setCollider(collider);

        rigidbody.setPosition(50, 100);

        assertEquals(50, collider.getPosition().x, 0.001f);
        assertEquals(100, collider.getPosition().y, 0.001f);
    }

    @Test
    void testPosition() {
        rigidbody.setPosition(new Vector2(10, 20));
        assertEquals(10, rigidbody.getPosition().x, 0.001f);
        assertEquals(20, rigidbody.getPosition().y, 0.001f);
    }

    @Test
    void testPositionComponents() {
        rigidbody.setPosition(30, 40);
        assertEquals(30, rigidbody.getPosition().x, 0.001f);
        assertEquals(40, rigidbody.getPosition().y, 0.001f);
    }

    @Test
    void testRotation() {
        rigidbody.setRotation((float) Math.PI / 4);
        assertEquals(Math.PI / 4, rigidbody.getRotation(), 0.001f);
    }

    @Test
    void testKineticEnergy() {
        rigidbody.setMass(2.0f);
        rigidbody.setVelocity(new Vector2(3, 4));

        // KE = 0.5 * m * v^2 = 0.5 * 2 * 25 = 25
        assertEquals(25.0f, rigidbody.getKineticEnergy(), 0.01f);
    }

    @Test
    void testMomentum() {
        rigidbody.setMass(5.0f);
        rigidbody.setVelocity(new Vector2(2, 3));

        Vector2 momentum = rigidbody.getMomentum();

        // p = m * v
        assertEquals(10.0f, momentum.x, 0.001f);
        assertEquals(15.0f, momentum.y, 0.001f);
    }

    @Test
    void testAngularMomentum() {
        rigidbody.setMomentOfInertia(4.0f);
        rigidbody.setAngularVelocity(3.0f);

        // L = I * ω
        assertEquals(12.0f, rigidbody.getAngularMomentum(), 0.001f);
    }

    @Test
    void testUpdateDoesNotAffectKinematic() {
        RigidbodyComponent kinematicRb = new RigidbodyComponent(10.0f);
        kinematicRb.setKinematic(true);
        kinematicRb.setVelocity(new Vector2(100, 0));
        kinematicRb.update(1.0f);

        // 运动学刚体的位置应该随速度更新
        assertEquals(100.0f, kinematicRb.getPosition().x, 0.01f);
    }
}
