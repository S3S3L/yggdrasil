package io.github.s3s3l.yggdrasil.game.core.physics;

import io.github.s3s3l.yggdrasil.game.core.basic.Vector2;
import io.github.s3s3l.yggdrasil.game.core.physics.collider.BoxCollider;
import io.github.s3s3l.yggdrasil.game.core.physics.collider.CollisionInfo;
import io.github.s3s3l.yggdrasil.game.core.physics.collider.PolygonCollider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 触发器碰撞单元测试
 */
class TriggerColliderTest {

    private PhysicsWorldWithResponse world;
    private List<String> events;

    @BeforeEach
    void setUp() {
        world = new PhysicsWorldWithResponse();
        events = new ArrayList<>();
    }

    @Test
    void testTriggerDetection() {
        // 创建触发器
        BoxCollider trigger = new BoxCollider(50, 50, 20, 20);
        trigger.setTrigger(true);
        assertTrue(trigger.isTrigger());

        // 创建普通碰撞体
        BoxCollider box = new BoxCollider(50, 50, 10, 10);
        assertFalse(box.isTrigger());

        world.addCollider(trigger);
        world.addCollider(box);

        List<CollisionInfo> collisions = world.checkCollisions();

        assertEquals(1, collisions.size());
        assertTrue(collisions.get(0).isTrigger);
    }

    @Test
    void testTriggerNoPhysicsResponse() {
        // 创建触发器
        RigidbodyComponent rb1 = new RigidbodyComponent(10.0f, new Vector2(0, 0), 0);
        rb1.setCollider(new BoxCollider(0, 0, 10, 10));
        rb1.setVelocity(new Vector2(10, 0));

        // 创建触发器碰撞体（无刚体）
        BoxCollider trigger = new BoxCollider(20, 0, 10, 10);
        trigger.setTrigger(true);
        world.addCollider(trigger);

        world.addRigidbody(rb1);

        Vector2 initialVel = new Vector2(rb1.getVelocity());

        world.checkCollisions();

        // 触发器不应改变刚体速度
        assertEquals(initialVel.x, rb1.getVelocity().x, 0.001f);
    }

    @Test
    void testTriggerEvents() {
        BoxCollider trigger = new BoxCollider(50, 50, 20, 20);
        trigger.setTrigger(true);

        BoxCollider box = new BoxCollider(50, 50, 10, 10);

        world.addCollider(trigger);
        world.addCollider(box);

        world.addCollisionListener(new CollisionListener() {
            @Override
            public void onCollisionStart(CollisionEvent event) {
                events.add("START:" + event.isTrigger());
            }

            @Override
            public void onCollisionStay(CollisionEvent event) {
                events.add("STAY:" + event.isTrigger());
            }

            @Override
            public void onCollisionEnd(CollisionEvent event) {
                events.add("END:" + event.isTrigger());
            }
        });

        // 第一次检测 - START
        world.checkCollisions();
        assertEquals(1, events.size());
        assertEquals("START:true", events.get(0));

        // 第二次检测 - STAY
        events.clear();
        world.checkCollisions();
        assertEquals(1, events.size());
        assertEquals("STAY:true", events.get(0));
    }

    @Test
    void testTriggerAndNonTriggerCollision() {
        // 两个普通碰撞体碰撞
        BoxCollider box1 = new BoxCollider(0, 0, 10, 10);
        BoxCollider box2 = new BoxCollider(9, 0, 10, 10);

        world.addCollider(box1);
        world.addCollider(box2);

        List<CollisionInfo> collisions = world.checkCollisions();

        assertEquals(1, collisions.size());
        assertFalse(collisions.get(0).isTrigger);
    }

    @Test
    void testTwoTriggersCollision() {
        // 两个触发器碰撞
        BoxCollider trigger1 = new BoxCollider(0, 0, 10, 10);
        trigger1.setTrigger(true);

        BoxCollider trigger2 = new BoxCollider(9, 0, 10, 10);
        trigger2.setTrigger(true);

        world.addCollider(trigger1);
        world.addCollider(trigger2);

        List<CollisionInfo> collisions = world.checkCollisions();

        assertEquals(1, collisions.size());
        assertTrue(collisions.get(0).isTrigger);
    }

    @Test
    void testTriggerPositionCorrection() {
        // 触发器不应应用位置修正
        RigidbodyComponent rb1 = new RigidbodyComponent(10.0f, new Vector2(0, 0), 0);
        rb1.setCollider(new BoxCollider(0, 0, 10, 10));

        BoxCollider trigger = new BoxCollider(9, 0, 10, 10);
        trigger.setTrigger(true);
        world.addCollider(trigger);

        world.addRigidbody(rb1);

        Vector2 initialPos = new Vector2(rb1.getPosition());

        world.checkCollisions();

        // 位置不应改变（触发器无物理响应）
        assertEquals(initialPos.x, rb1.getPosition().x, 0.001f);
    }

    @Test
    void testCircleTrigger() {
        // 圆形触发器
        RigidbodyComponent rb1 = new RigidbodyComponent(10.0f, new Vector2(0, 0), 0);
        rb1.setCollider(new io.github.s3s3l.yggdrasil.game.core.physics.collider.CircleCollider(0, 0, 5));

        io.github.s3s3l.yggdrasil.game.core.physics.collider.CircleCollider trigger =
                new io.github.s3s3l.yggdrasil.game.core.physics.collider.CircleCollider(10, 0, 5);
        trigger.setTrigger(true);
        world.addCollider(trigger);

        world.addRigidbody(rb1);

        List<CollisionInfo> collisions = world.checkCollisions();

        assertEquals(1, collisions.size());
        assertTrue(collisions.get(0).isTrigger);
    }

    @Test
    void testPolygonTrigger() {
        // 多边形触发器（使用矩形多边形）
        List<Vector2> vertices = new ArrayList<>();
        vertices.add(new Vector2(-5, -5));
        vertices.add(new Vector2(5, -5));
        vertices.add(new Vector2(5, 5));
        vertices.add(new Vector2(-5, 5));

        // BoxCollider 在 (0,0)，大小 8x8
        RigidbodyComponent rb1 = new RigidbodyComponent(10.0f, new Vector2(0, 0), 0);
        rb1.setCollider(new BoxCollider(0, 0, 8, 8));

        // 多边形中心在 (8,0)，顶点范围 [3,-5] 到 [13,5]，与 rb1 右侧重叠
        PolygonCollider trigger = new PolygonCollider(8, 0, vertices);
        trigger.setTrigger(true);
        world.addCollider(trigger);

        world.addRigidbody(rb1);

        List<CollisionInfo> collisions = world.checkCollisions();

        // 多边形和 Box 应该有碰撞
        assertEquals(1, collisions.size());
        assertTrue(collisions.get(0).isTrigger);
    }
}
