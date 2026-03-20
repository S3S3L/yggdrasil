package io.github.s3s3l.yggdrasil.game.core.physics;

import io.github.s3s3l.yggdrasil.game.core.basic.Vector2;
import io.github.s3s3l.yggdrasil.game.core.physics.collider.BoxCollider;
import io.github.s3s3l.yggdrasil.game.core.physics.collider.Collider;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 碰撞事件单元测试
 */
class CollisionEventTest {

    @Test
    void testConstructor() {
        Collider a = new BoxCollider(0, 0, 10, 10);
        Collider b = new BoxCollider(5, 5, 10, 10);

        CollisionEvent event = new CollisionEvent(a, b, CollisionEvent.CollisionEventType.START);

        assertEquals(a, event.getColliderA());
        assertEquals(b, event.getColliderB());
        assertEquals(CollisionEvent.CollisionEventType.START, event.getType());
    }

    @Test
    void testFullConstructor() {
        Collider a = new BoxCollider(0, 0, 10, 10);
        Collider b = new BoxCollider(5, 5, 10, 10);
        Vector2 contactPoint = new Vector2(5, 5);
        Vector2 normal = new Vector2(1, 0);

        CollisionEvent event = new CollisionEvent(a, b, contactPoint, normal, 2.5f,
                CollisionEvent.CollisionEventType.START);

        assertEquals(a, event.getColliderA());
        assertEquals(b, event.getColliderB());
        assertEquals(contactPoint, event.getContactPoint());
        assertEquals(normal, event.getNormal());
        assertEquals(2.5f, event.getPenetrationDepth(), 0.001f);
        assertEquals(CollisionEvent.CollisionEventType.START, event.getType());
    }

    @Test
    void testSet() {
        Collider a = new BoxCollider(0, 0, 10, 10);
        Collider b = new BoxCollider(5, 5, 10, 10);
        Vector2 contactPoint = new Vector2(5, 5);
        Vector2 normal = new Vector2(1, 0);

        CollisionEvent event = new CollisionEvent();
        event.set(a, b, contactPoint, normal, 2.5f, CollisionEvent.CollisionEventType.START);

        assertEquals(a, event.getColliderA());
        assertEquals(b, event.getColliderB());
        assertEquals(contactPoint, event.getContactPoint());
        assertEquals(normal, event.getNormal());
        assertEquals(2.5f, event.getPenetrationDepth(), 0.001f);
        assertEquals(CollisionEvent.CollisionEventType.START, event.getType());
    }

    @Test
    void testSetters() {
        CollisionEvent event = new CollisionEvent();
        Collider a = new BoxCollider(0, 0, 10, 10);
        Collider b = new BoxCollider(5, 5, 10, 10);
        Vector2 contactPoint = new Vector2(5, 5);
        Vector2 normal = new Vector2(1, 0);

        event.setColliderA(a);
        event.setColliderB(b);
        event.setContactPoint(contactPoint);
        event.setNormal(normal);
        event.setPenetrationDepth(2.5f);
        event.setType(CollisionEvent.CollisionEventType.STAY);

        assertEquals(a, event.getColliderA());
        assertEquals(b, event.getColliderB());
        assertEquals(contactPoint, event.getContactPoint());
        assertEquals(normal, event.getNormal());
        assertEquals(2.5f, event.getPenetrationDepth(), 0.001f);
        assertEquals(CollisionEvent.CollisionEventType.STAY, event.getType());
    }

    @Test
    void testEquals() {
        Collider a1 = new BoxCollider(0, 0, 10, 10);
        Collider a2 = new BoxCollider(0, 0, 10, 10);
        Collider b = new BoxCollider(5, 5, 10, 10);

        CollisionEvent event1 = new CollisionEvent(a1, b, CollisionEvent.CollisionEventType.START);
        CollisionEvent event2 = new CollisionEvent(a1, b, CollisionEvent.CollisionEventType.START);
        CollisionEvent event3 = new CollisionEvent(a1, b, CollisionEvent.CollisionEventType.END);

        assertEquals(event1, event1);
        assertEquals(event1, event2);
        assertNotEquals(event1, event3);
        assertNotEquals(event1, null);
        assertNotEquals(event1, new Object());
    }

    @Test
    void testHashCode() {
        Collider a = new BoxCollider(0, 0, 10, 10);
        Collider b = new BoxCollider(5, 5, 10, 10);

        CollisionEvent event1 = new CollisionEvent(a, b, CollisionEvent.CollisionEventType.START);
        CollisionEvent event2 = new CollisionEvent(a, b, CollisionEvent.CollisionEventType.START);

        assertEquals(event1.hashCode(), event2.hashCode());
    }

    @Test
    void testToString() {
        Collider a = new BoxCollider(0, 0, 10, 10);
        Collider b = new BoxCollider(5, 5, 10, 10);

        CollisionEvent event = new CollisionEvent(a, b, CollisionEvent.CollisionEventType.START);
        String str = event.toString();

        assertNotNull(str);
        assertTrue(str.contains("START"));
    }

    @Test
    void testCollisionEventTypes() {
        // 确保所有枚举值都存在
        assertNotNull(CollisionEvent.CollisionEventType.START);
        assertNotNull(CollisionEvent.CollisionEventType.STAY);
        assertNotNull(CollisionEvent.CollisionEventType.END);
    }
}
