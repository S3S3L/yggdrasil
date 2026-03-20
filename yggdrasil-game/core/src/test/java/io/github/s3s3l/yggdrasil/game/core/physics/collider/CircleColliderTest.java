package io.github.s3s3l.yggdrasil.game.core.physics.collider;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * CircleCollider 单元测试
 */
class CircleColliderTest {

    @Test
    void testConstructor() {
        CircleCollider circle = new CircleCollider(50, 50, 25);
        assertEquals(50, circle.getPosition().x, 0.001f);
        assertEquals(50, circle.getPosition().y, 0.001f);
        assertEquals(25, circle.getRadius(), 0.001f);
    }

    @Test
    void testGetType() {
        CircleCollider circle = new CircleCollider(0, 0, 10);
        assertEquals(Collider.ColliderType.CIRCLE, circle.getType());
    }

    @Test
    void testGetPosition() {
        CircleCollider circle = new CircleCollider(100, 200, 25);
        assertEquals(100, circle.getPosition().x, 0.001f);
        assertEquals(200, circle.getPosition().y, 0.001f);
    }

    @Test
    void testSetPosition() {
        CircleCollider circle = new CircleCollider(0, 0, 25);
        circle.setPosition(100, 200);
        assertEquals(100, circle.getPosition().x, 0.001f);
        assertEquals(200, circle.getPosition().y, 0.001f);
    }

    @Test
    void testGetBounds() {
        CircleCollider circle = new CircleCollider(50, 50, 25);
        Rectangle bounds = circle.getBounds();
        assertEquals(25, bounds.minX, 0.001f);
        assertEquals(25, bounds.minY, 0.001f);
        assertEquals(75, bounds.maxX, 0.001f);
        assertEquals(75, bounds.maxY, 0.001f);
    }

    @Test
    void testContains() {
        CircleCollider circle = new CircleCollider(0, 0, 10);
        assertTrue(circle.contains(0, 0)); // 中心
        assertTrue(circle.contains(5, 5)); // 内部
        assertTrue(circle.contains(10, 0)); // 边界
        assertFalse(circle.contains(11, 0)); // 外部
        assertFalse(circle.contains(8, 8)); // 外部 (sqrt(128) > 10)
    }

    @Test
    void testIntersectsCircle() {
        CircleCollider a = new CircleCollider(0, 0, 10);
        CircleCollider b = new CircleCollider(15, 0, 10); // 重叠
        CircleCollider c = new CircleCollider(30, 0, 5); // 不重叠

        assertTrue(a.intersects(b));
        assertFalse(a.intersects(c));
    }

    @Test
    void testIntersectsCircleTouching() {
        CircleCollider a = new CircleCollider(0, 0, 10);
        CircleCollider b = new CircleCollider(20, 0, 10); // 刚好接触

        assertTrue(a.intersects(b));
    }

    @Test
    void testIntersectsBox() {
        CircleCollider circle = new CircleCollider(50, 50, 15);
        BoxCollider box1 = new BoxCollider(50, 50, 20, 20); // 内部
        BoxCollider box2 = new BoxCollider(100, 50, 20, 20); // 外部

        assertTrue(circle.intersects(box1));
        assertFalse(circle.intersects(box2));
    }

    @Test
    void testIntersectsBoxEdge() {
        CircleCollider circle = new CircleCollider(0, 0, 10);
        BoxCollider box = new BoxCollider(15, 0, 10, 10); // 边缘附近

        assertTrue(circle.intersects(box));
    }

    @Test
    void testSetRadius() {
        CircleCollider circle = new CircleCollider(0, 0, 10);
        circle.setRadius(20);
        assertEquals(20, circle.getRadius(), 0.001f);
    }

    @Test
    void testCopy() {
        CircleCollider original = new CircleCollider(50, 50, 25);
        CircleCollider copy = original.copy();

        assertEquals(original.getPosition().x, copy.getPosition().x, 0.001f);
        assertEquals(original.getPosition().y, copy.getPosition().y, 0.001f);
        assertEquals(original.getRadius(), copy.getRadius(), 0.001f);

        // 修改副本不应影响原件
        copy.setPosition(100, 100);
        assertEquals(50, original.getPosition().x, 0.001f);
    }

    @Test
    void testEquals() {
        CircleCollider a = new CircleCollider(50, 50, 25);
        CircleCollider b = new CircleCollider(50, 50, 25);
        CircleCollider c = new CircleCollider(60, 60, 25);

        assertEquals(a, b);
        assertNotEquals(a, c);
    }

    @Test
    void testNegativeRadius() {
        CircleCollider circle = new CircleCollider(0, 0, -10);
        assertEquals(10, circle.getRadius(), 0.001f);
    }
}
