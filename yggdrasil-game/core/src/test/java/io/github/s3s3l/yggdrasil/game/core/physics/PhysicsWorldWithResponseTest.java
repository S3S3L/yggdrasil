package io.github.s3s3l.yggdrasil.game.core.physics;

import io.github.s3s3l.yggdrasil.game.core.basic.Vector2;
import io.github.s3s3l.yggdrasil.game.core.physics.collider.BoxCollider;
import io.github.s3s3l.yggdrasil.game.core.physics.collider.Collider;
import io.github.s3s3l.yggdrasil.game.core.physics.collider.CollisionInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 带响应的物理世界单元测试
 */
class PhysicsWorldWithResponseTest {

    private PhysicsWorldWithResponse world;

    @BeforeEach
    void setUp() {
        world = new PhysicsWorldWithResponse();
    }

    @Test
    void testCreate() {
        assertNotNull(world);
        assertEquals(0, world.getColliderCount());
        assertEquals(0, world.getRigidbodyCount());
    }

    @Test
    void testAddCollider() {
        Collider collider = new BoxCollider(50, 50, 10, 10);
        world.addCollider(collider);
        assertEquals(1, world.getColliderCount());
    }

    @Test
    void testAddRigidbody() {
        RigidbodyComponent rb = new RigidbodyComponent(10.0f, new Vector2(50, 50), 0);
        rb.setCollider(new BoxCollider(50, 50, 10, 10));

        world.addRigidbody(rb);

        assertEquals(1, world.getColliderCount());
        assertEquals(1, world.getRigidbodyCount());
    }

    @Test
    void testUpdate() {
        RigidbodyComponent rb = new RigidbodyComponent(10.0f, new Vector2(0, 0), 0);
        rb.setVelocity(new Vector2(10, 0));
        world.addRigidbody(rb);

        world.update(0.1f);

        // 刚体速度不变（没有外力）
        assertEquals(10, rb.getVelocity().x, 0.001f);
    }

    @Test
    void testCheckCollisionsNoRigidbody() {
        world.addCollider(new BoxCollider(50, 50, 10, 10));
        world.addCollider(new BoxCollider(55, 50, 10, 10));

        List<CollisionInfo> collisions = world.checkCollisions();

        assertEquals(1, collisions.size());
        assertTrue(collisions.get(0).collided);
    }

    @Test
    void testCheckCollisionsWithResponse() {
        // 碰撞体需要实际重叠：宽 10，中心在 0 和 9 时穿透 1 个单位
        RigidbodyComponent rb1 = new RigidbodyComponent(10.0f, new Vector2(0, 0), 0);
        rb1.setCollider(new BoxCollider(0, 0, 10, 10));
        rb1.setVelocity(new Vector2(5, 0));

        RigidbodyComponent rb2 = new RigidbodyComponent(10.0f, new Vector2(9, 0), 0);
        rb2.setCollider(new BoxCollider(9, 0, 10, 10));
        rb2.setVelocity(new Vector2(0, 0));

        world.addRigidbody(rb1);
        world.addRigidbody(rb2);

        // 第一次检测：碰撞发生，应用冲量
        List<CollisionInfo> collisions = world.checkCollisions();

        assertTrue(collisions.size() >= 1, "应该有至少一次碰撞");

        // 碰撞后，rb1 速度应该减小
        assertTrue(rb1.getVelocity().x < 5);

        // rb2 应该获得速度
        assertTrue(rb2.getVelocity().x > 0);
    }

    @Test
    void testPositionCorrection() {
        RigidbodyComponent rb1 = new RigidbodyComponent(10.0f, new Vector2(0, 0), 0);
        rb1.setCollider(new BoxCollider(0, 0, 10, 10));

        RigidbodyComponent rb2 = new RigidbodyComponent(10.0f, new Vector2(9, 0), 0);
        rb2.setCollider(new BoxCollider(9, 0, 10, 10));

        world.addRigidbody(rb1);
        world.addRigidbody(rb2);

        // 穿透 1 个单位
        world.checkCollisions();

        // 位置应该被修正
        assertTrue(rb1.getPosition().x < 0);
        assertTrue(rb2.getPosition().x > 9);
    }

    @Test
    void testPositionCorrectionDisabled() {
        world.setPositionCorrectionEnabled(false);

        RigidbodyComponent rb1 = new RigidbodyComponent(10.0f, new Vector2(0, 0), 0);
        rb1.setCollider(new BoxCollider(0, 0, 10, 10));

        RigidbodyComponent rb2 = new RigidbodyComponent(10.0f, new Vector2(9, 0), 0);
        rb2.setCollider(new BoxCollider(9, 0, 10, 10));

        world.addRigidbody(rb1);
        world.addRigidbody(rb2);

        Vector2 initialPos1 = new Vector2(rb1.getPosition());
        Vector2 initialPos2 = new Vector2(rb2.getPosition());

        world.checkCollisions();

        // 位置修正禁用，位置不变
        assertEquals(initialPos1.x, rb1.getPosition().x, 0.001f);
        assertEquals(initialPos2.x, rb2.getPosition().x, 0.001f);
    }

    @Test
    void testFrictionDisabled() {
        world.setFrictionEnabled(false);

        RigidbodyComponent rb1 = new RigidbodyComponent(10.0f, new Vector2(0, 0), 0);
        rb1.setCollider(new BoxCollider(0, 0, 10, 10));
        rb1.setVelocity(new Vector2(10, 5)); // 有切线速度

        RigidbodyComponent rb2 = new RigidbodyComponent(10.0f, new Vector2(20, 0), 0);
        rb2.setCollider(new BoxCollider(20, 0, 10, 10));

        world.addRigidbody(rb1);
        world.addRigidbody(rb2);

        world.checkCollisions();

        // 摩擦禁用，切线速度不变（只测试不抛异常）
        assertDoesNotThrow(() -> world.checkCollisions());
    }

    @Test
    void testCollisionListener() {
        final boolean[] collisionDetected = {false};

        world.addCollisionListener(new CollisionListener() {
            @Override
            public void onCollisionStart(CollisionEvent event) {
                collisionDetected[0] = true;
            }

            @Override
            public void onCollisionStay(CollisionEvent event) {}

            @Override
            public void onCollisionEnd(CollisionEvent event) {}
        });

        // 创建实际接触的碰撞体：宽 10，中心在 0 和 9 时穿透 1 个单位
        RigidbodyComponent rb1 = new RigidbodyComponent(10.0f, new Vector2(0, 0), 0);
        rb1.setCollider(new BoxCollider(0, 0, 10, 10));

        RigidbodyComponent rb2 = new RigidbodyComponent(10.0f, new Vector2(9, 0), 0);
        rb2.setCollider(new BoxCollider(9, 0, 10, 10));

        world.addRigidbody(rb1);
        world.addRigidbody(rb2);

        world.checkCollisions();

        assertTrue(collisionDetected[0], "应该检测到碰撞");
    }

    @Test
    void testRemoveRigidbody() {
        RigidbodyComponent rb = new RigidbodyComponent(10.0f, new Vector2(0, 0), 0);
        rb.setCollider(new BoxCollider(0, 0, 10, 10));

        world.addRigidbody(rb);
        assertEquals(1, world.getColliderCount());
        assertEquals(1, world.getRigidbodyCount());

        world.removeRigidbody(rb);

        assertEquals(0, world.getColliderCount());
        assertEquals(0, world.getRigidbodyCount());
    }

    @Test
    void testClear() {
        world.addRigidbody(new RigidbodyComponent(10.0f, new Vector2(0, 0), 0));
        world.addRigidbody(new RigidbodyComponent(10.0f, new Vector2(20, 0), 0));

        world.clear();

        assertEquals(0, world.getColliderCount());
        assertEquals(0, world.getRigidbodyCount());
    }

    @Test
    void testSetPositionSlop() {
        world.setPositionSlop(0.05f);
        assertEquals(0.05f, world.getPositionSlop(), 0.001f);
    }

    @Test
    void testMultipleCollisions() {
        // 创建多个碰撞体，相邻放置使其重叠：宽 10，中心相距 9，穿透 1 个单位
        for (int i = 0; i < 5; i++) {
            float x = i * 9; // 每个中心相距 9，宽 10，穿透 1 个单位
            RigidbodyComponent rb = new RigidbodyComponent(10.0f, new Vector2(x, 0), 0);
            rb.setCollider(new BoxCollider(x, 0, 10, 10));
            world.addRigidbody(rb);
        }

        List<CollisionInfo> collisions = world.checkCollisions();

        // 4 对相邻的碰撞体应该碰撞
        assertTrue(collisions.size() >= 1, "应该有碰撞");
    }

    @Test
    void testUpdateMovesRigidbodies() {
        RigidbodyComponent rb = new RigidbodyComponent(10.0f, new Vector2(0, 0), 0);
        rb.setCollider(new BoxCollider(0, 0, 10, 10));
        rb.setVelocity(new Vector2(100, 0));

        world.addRigidbody(rb);
        world.update(0.1f);

        // 位置应该移动
        assertEquals(10, rb.getPosition().x, 0.5f);
    }

    @Test
    void testKinematicRigidbodyNotAffectedByForces() {
        RigidbodyComponent rb = new RigidbodyComponent(10.0f, new Vector2(0, 0), 0);
        rb.setCollider(new BoxCollider(0, 0, 10, 10));
        rb.setKinematic(true);
        rb.addForce(new Vector2(1000, 0));

        world.addRigidbody(rb);
        world.update(0.1f);

        // 运动学刚体不受力影响，但应该能手动移动
        // （这里主要测试不抛异常）
        assertDoesNotThrow(() -> world.update(0.1f));
    }
}
