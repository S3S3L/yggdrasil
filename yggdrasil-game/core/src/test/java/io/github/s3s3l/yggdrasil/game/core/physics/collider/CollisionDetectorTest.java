package io.github.s3s3l.yggdrasil.game.core.physics.collider;

import io.github.s3s3l.yggdrasil.game.core.basic.Vector2;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * CollisionDetector 单元测试
 */
class CollisionDetectorTest {

    @Test
    void testCheckCollisionBoxBox() {
        BoxCollider a = new BoxCollider(0, 0, 50, 50);
        BoxCollider b = new BoxCollider(40, 0, 50, 50);
        BoxCollider c = new BoxCollider(100, 0, 50, 50);

        assertTrue(CollisionDetector.checkCollision(a, b));
        assertFalse(CollisionDetector.checkCollision(a, c));
    }

    @Test
    void testCheckCollisionCircleCircle() {
        CircleCollider a = new CircleCollider(0, 0, 10);
        CircleCollider b = new CircleCollider(15, 0, 10);
        CircleCollider c = new CircleCollider(30, 0, 5);

        assertTrue(CollisionDetector.checkCollision(a, b));
        assertFalse(CollisionDetector.checkCollision(a, c));
    }

    @Test
    void testCheckCollisionBoxCircle() {
        BoxCollider box = new BoxCollider(50, 50, 40, 40);
        CircleCollider circle1 = new CircleCollider(50, 50, 10);
        CircleCollider circle2 = new CircleCollider(100, 50, 10);

        assertTrue(CollisionDetector.checkCollision(box, circle1));
        assertFalse(CollisionDetector.checkCollision(box, circle2));
    }

    @Test
    void testGetCollisionInfoNoCollision() {
        BoxCollider a = new BoxCollider(0, 0, 50, 50);
        BoxCollider b = new BoxCollider(200, 0, 50, 50);

        CollisionInfo info = CollisionDetector.getCollisionInfo(a, b);
        assertFalse(info.collided);
        assertEquals(0, info.penetrationDepth, 0.001f);
    }

    @Test
    void testGetCollisionInfoBoxBox() {
        BoxCollider a = new BoxCollider(0, 0, 50, 50);
        BoxCollider b = new BoxCollider(40, 0, 50, 50);

        CollisionInfo info = CollisionDetector.getCollisionInfo(a, b);
        assertTrue(info.collided);
        assertNotNull(info.contactPoint);
        assertNotNull(info.normal);
        assertTrue(info.penetrationDepth > 0);
    }

    @Test
    void testGetCollisionInfoCircleCircle() {
        CircleCollider a = new CircleCollider(0, 0, 10);
        CircleCollider b = new CircleCollider(15, 0, 10);

        CollisionInfo info = CollisionDetector.getCollisionInfo(a, b);
        assertTrue(info.collided);
        assertNotNull(info.contactPoint);
        assertNotNull(info.normal);
        assertTrue(info.penetrationDepth > 0);
    }

    @Test
    void testGetCollisionInfoCircleBox() {
        CircleCollider circle = new CircleCollider(50, 50, 15);
        BoxCollider box = new BoxCollider(50, 50, 20, 20);

        CollisionInfo info = CollisionDetector.getCollisionInfo(circle, box);
        assertTrue(info.collided);
        assertNotNull(info.contactPoint);
        assertNotNull(info.normal);
    }

    @Test
    void testGetCollisionInfoPolygonBox() {
        PolygonCollider polygon = PolygonCollider.createRectangle(0, 0, 20, 20);
        BoxCollider box = new BoxCollider(15, 0, 20, 20);

        CollisionInfo info = CollisionDetector.getCollisionInfo(polygon, box);
        assertTrue(info.collided);
        assertNotNull(info.contactPoint);
    }

    @Test
    void testCollisionInfoCopy() {
        CollisionInfo original = new CollisionInfo();
        original.set(true, new Vector2(1, 2), new Vector2(0, 1), 5.0f,
                new BoxCollider(0, 0, 10, 10), new BoxCollider(5, 0, 10, 10));

        CollisionInfo copy = original.copy();

        assertEquals(original.collided, copy.collided);
        assertEquals(original.contactPoint.x, copy.contactPoint.x, 0.001f);
        assertEquals(original.contactPoint.y, copy.contactPoint.y, 0.001f);
        assertEquals(original.penetrationDepth, copy.penetrationDepth, 0.001f);
    }

    @Test
    void testCollisionInfoClear() {
        CollisionInfo info = new CollisionInfo();
        info.set(true, new Vector2(1, 2), new Vector2(0, 1), 5.0f,
                new BoxCollider(0, 0, 10, 10), new BoxCollider(5, 0, 10, 10));

        info.clear();

        assertFalse(info.collided);
        assertEquals(0, info.penetrationDepth, 0.001f);
        assertNull(info.colliderA);
        assertNull(info.colliderB);
    }
}
