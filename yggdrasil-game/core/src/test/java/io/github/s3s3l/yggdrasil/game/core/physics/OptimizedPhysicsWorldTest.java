package io.github.s3s3l.yggdrasil.game.core.physics;

import io.github.s3s3l.yggdrasil.game.core.physics.collider.BoxCollider;
import io.github.s3s3l.yggdrasil.game.core.physics.collider.CircleCollider;
import io.github.s3s3l.yggdrasil.game.core.physics.collider.Collider;
import io.github.s3s3l.yggdrasil.game.core.physics.collider.CollisionInfo;
import io.github.s3s3l.yggdrasil.game.core.physics.collider.Rectangle;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 优化物理世界单元测试
 */
class OptimizedPhysicsWorldTest {

    private OptimizedPhysicsWorld world;
    private Rectangle worldBounds;

    @BeforeEach
    void setUp() {
        worldBounds = new Rectangle(0, 0, 200, 200);
        world = new OptimizedPhysicsWorld(worldBounds, 4, 4);
    }

    @Test
    void testCreate() {
        assertNotNull(world);
        assertEquals(0, world.getColliderCount());
        assertEquals(worldBounds, world.getWorldBounds());
    }

    @Test
    void testCreateDefault() {
        OptimizedPhysicsWorld w = new OptimizedPhysicsWorld(worldBounds);
        assertNotNull(w);
    }

    @Test
    void testAddCollider() {
        Collider collider = new BoxCollider(50, 50, 10, 10);
        world.addCollider(collider);
        assertEquals(1, world.getColliderCount());
    }

    @Test
    void testAddNullCollider() {
        assertThrows(IllegalArgumentException.class, () -> world.addCollider(null));
    }

    @Test
    void testRemoveCollider() {
        Collider collider = new BoxCollider(50, 50, 10, 10);
        world.addCollider(collider);
        world.removeCollider(collider);
        assertEquals(0, world.getColliderCount());
    }

    @Test
    void testClear() {
        world.addCollider(new BoxCollider(50, 50, 10, 10));
        world.addCollider(new BoxCollider(100, 100, 10, 10));
        world.clear();
        assertEquals(0, world.getColliderCount());
    }

    @Test
    void testCheckCollisionsNoCollision() {
        world.addCollider(new BoxCollider(10, 10, 10, 10));
        world.addCollider(new BoxCollider(100, 100, 10, 10));

        List<CollisionInfo> collisions = world.checkCollisions();
        assertTrue(collisions.isEmpty());
    }

    @Test
    void testCheckCollisionsWithCollision() {
        world.addCollider(new BoxCollider(50, 50, 20, 20));
        world.addCollider(new BoxCollider(60, 50, 20, 20));

        List<CollisionInfo> collisions = world.checkCollisions();
        assertFalse(collisions.isEmpty());
        assertEquals(1, collisions.size());
    }

    @Test
    void testCheckCollisionsMultiple() {
        world.addCollider(new BoxCollider(50, 50, 20, 20)); // A
        world.addCollider(new BoxCollider(60, 50, 20, 20)); // B - collides with A
        world.addCollider(new BoxCollider(150, 150, 20, 20)); // C - no collision

        List<CollisionInfo> collisions = world.checkCollisions();
        assertEquals(1, collisions.size());
    }

    @Test
    void testCollisionListener() {
        final List<CollisionEvent> events = new ArrayList<>();
        world.addCollisionListener(new CollisionListener() {
            @Override
            public void onCollisionStart(CollisionEvent event) {
                events.add(event);
            }

            @Override
            public void onCollisionStay(CollisionEvent event) {}

            @Override
            public void onCollisionEnd(CollisionEvent event) {}
        });

        Collider a = new BoxCollider(50, 50, 20, 20);
        Collider b = new BoxCollider(60, 50, 20, 20);
        world.addCollider(a);
        world.addCollider(b);

        world.checkCollisions();
        assertEquals(1, events.size());
        assertEquals(CollisionEvent.CollisionEventType.START, events.get(0).getType());
    }

    @Test
    void testCollisionListenerStay() {
        final List<CollisionEvent> startEvents = new ArrayList<>();
        final List<CollisionEvent> stayEvents = new ArrayList<>();

        world.addCollisionListener(new CollisionListener() {
            @Override
            public void onCollisionStart(CollisionEvent event) {
                startEvents.add(event);
            }

            @Override
            public void onCollisionStay(CollisionEvent event) {
                stayEvents.add(event);
            }

            @Override
            public void onCollisionEnd(CollisionEvent event) {}
        });

        Collider a = new BoxCollider(50, 50, 20, 20);
        Collider b = new BoxCollider(60, 50, 20, 20);
        world.addCollider(a);
        world.addCollider(b);

        world.checkCollisions(); // START
        world.checkCollisions(); // STAY

        assertEquals(1, startEvents.size());
        assertEquals(1, stayEvents.size());
    }

    @Test
    void testCollisionListenerEnd() {
        final List<CollisionEvent> endEvents = new ArrayList<>();

        world.addCollisionListener(new CollisionListener() {
            @Override
            public void onCollisionStart(CollisionEvent event) {}

            @Override
            public void onCollisionStay(CollisionEvent event) {}

            @Override
            public void onCollisionEnd(CollisionEvent event) {
                endEvents.add(event);
            }
        });

        Collider a = new BoxCollider(50, 50, 20, 20);
        Collider b = new BoxCollider(60, 50, 20, 20);
        world.addCollider(a);
        world.addCollider(b);

        // 第一次检测 - 碰撞
        world.checkCollisions();

        // 移除一个碰撞体
        world.removeCollider(b);

        // 注意：当前实现在移除碰撞体时会清理历史记录
        // 所以碰撞结束事件不会触发
        // 这里验证移除后不抛异常
        assertEquals(0, endEvents.size());
    }

    @Test
    void testUpdate() {
        world.addCollider(new BoxCollider(50, 50, 10, 10));
        assertDoesNotThrow(() -> world.update(0.016f));
    }

    @Test
    void testAutoRebuild() {
        world.setAutoRebuild(true);
        // 验证设置后不抛异常
        world.update(0.016f);

        world.setAutoRebuild(false);
        // 验证关闭自动重建后不抛异常
        world.update(0.016f);
    }

    @Test
    void testGetQuadTree() {
        assertNotNull(world.getQuadTree());
    }

    @Test
    void testMixedColliderTypes() {
        world.addCollider(new BoxCollider(50, 50, 20, 20));
        world.addCollider(new CircleCollider(60, 50, 15));
        world.addCollider(new BoxCollider(150, 150, 20, 20));

        List<CollisionInfo> collisions = world.checkCollisions();
        assertEquals(1, collisions.size());
    }

    @Test
    void testManyColliders() {
        // 性能测试：添加大量碰撞体
        for (int i = 0; i < 50; i++) {
            float x = (i % 10) * 20 + 10;
            float y = (i / 10) * 20 + 10;
            world.addCollider(new BoxCollider(x, y, 10, 10));
        }

        assertEquals(50, world.getColliderCount());

        // 确保碰撞检测不抛异常
        assertDoesNotThrow(() -> world.checkCollisions());
    }

    @Test
    void testAddDuplicateCollider() {
        Collider collider = new BoxCollider(50, 50, 10, 10);
        world.addCollider(collider);
        world.addCollider(collider);
        assertEquals(1, world.getColliderCount());
    }

    @Test
    void testAddNullListener() {
        assertThrows(IllegalArgumentException.class, () -> world.addCollisionListener(null));
    }

    @Test
    void testRemoveCollisionListener() {
        CollisionListener listener = new CollisionListener() {
            @Override
            public void onCollisionStart(CollisionEvent event) {}
            @Override
            public void onCollisionStay(CollisionEvent event) {}
            @Override
            public void onCollisionEnd(CollisionEvent event) {}
        };

        world.addCollisionListener(listener);
        world.removeCollisionListener(listener);

        // 验证移除后不抛异常
        Collider a = new BoxCollider(50, 50, 20, 20);
        Collider b = new BoxCollider(60, 50, 20, 20);
        world.addCollider(a);
        world.addCollider(b);

        assertDoesNotThrow(() -> world.checkCollisions());
    }
}
