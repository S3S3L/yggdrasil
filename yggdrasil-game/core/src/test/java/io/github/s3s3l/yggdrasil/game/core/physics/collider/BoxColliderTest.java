package io.github.s3s3l.yggdrasil.game.core.physics.collider;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * BoxCollider 单元测试
 */
class BoxColliderTest {

    @Test
    void testConstructor() {
        BoxCollider box = new BoxCollider(50, 50, 40, 30);
        assertEquals(50, box.getPosition().x, 0.001f);
        assertEquals(50, box.getPosition().y, 0.001f);
        assertEquals(40, box.getWidth(), 0.001f);
        assertEquals(30, box.getHeight(), 0.001f);
    }

    @Test
    void testGetType() {
        BoxCollider box = new BoxCollider(0, 0, 10, 10);
        assertEquals(Collider.ColliderType.BOX, box.getType());
    }

    @Test
    void testGetPosition() {
        BoxCollider box = new BoxCollider(100, 200, 50, 40);
        assertEquals(100, box.getPosition().x, 0.001f);
        assertEquals(200, box.getPosition().y, 0.001f);
    }

    @Test
    void testSetPosition() {
        BoxCollider box = new BoxCollider(0, 0, 50, 40);
        box.setPosition(100, 200);
        assertEquals(100, box.getPosition().x, 0.001f);
        assertEquals(200, box.getPosition().y, 0.001f);
    }

    @Test
    void testGetBounds() {
        BoxCollider box = new BoxCollider(50, 50, 40, 30);
        Rectangle bounds = box.getBounds();
        assertEquals(30, bounds.minX, 0.001f);
        assertEquals(35, bounds.minY, 0.001f);
        assertEquals(70, bounds.maxX, 0.001f);
        assertEquals(65, bounds.maxY, 0.001f);
    }

    @Test
    void testContains() {
        BoxCollider box = new BoxCollider(50, 50, 40, 30);
        assertTrue(box.contains(50, 50)); // 中心
        assertTrue(box.contains(30, 35)); // 左下角
        assertTrue(box.contains(70, 65)); // 右上角
        assertFalse(box.contains(10, 50)); // 左侧外
        assertFalse(box.contains(90, 50)); // 右侧外
    }

    @Test
    void testIntersectsBox() {
        BoxCollider a = new BoxCollider(0, 0, 50, 50);
        BoxCollider b = new BoxCollider(40, 0, 50, 50); // 重叠
        BoxCollider c = new BoxCollider(100, 0, 50, 50); // 不重叠

        assertTrue(a.intersects(b));
        assertFalse(a.intersects(c));
    }

    @Test
    void testIntersectsCircle() {
        BoxCollider box = new BoxCollider(50, 50, 40, 40);
        CircleCollider circle1 = new CircleCollider(50, 50, 10); // 内部
        CircleCollider circle2 = new CircleCollider(100, 50, 10); // 外部

        assertTrue(box.intersects(circle1));
        assertFalse(box.intersects(circle2));
    }

    @Test
    void testIntersectsCircleEdge() {
        BoxCollider box = new BoxCollider(50, 50, 40, 40);
        CircleCollider circle = new CircleCollider(75, 50, 10); // 边缘附近

        assertTrue(box.intersects(circle));
    }

    @Test
    void testCopy() {
        BoxCollider original = new BoxCollider(50, 50, 40, 30);
        BoxCollider copy = original.copy();

        assertEquals(original.getPosition().x, copy.getPosition().x, 0.001f);
        assertEquals(original.getPosition().y, copy.getPosition().y, 0.001f);
        assertEquals(original.getWidth(), copy.getWidth(), 0.001f);
        assertEquals(original.getHeight(), copy.getHeight(), 0.001f);

        // 修改副本不应影响原件
        copy.setPosition(100, 100);
        assertEquals(50, original.getPosition().x, 0.001f);
    }

    @Test
    void testEquals() {
        BoxCollider a = new BoxCollider(50, 50, 40, 30);
        BoxCollider b = new BoxCollider(50, 50, 40, 30);
        BoxCollider c = new BoxCollider(60, 60, 40, 30);

        assertEquals(a, b);
        assertNotEquals(a, c);
    }

    @Test
    void testGetEdges() {
        BoxCollider box = new BoxCollider(50, 50, 40, 30);
        assertEquals(30, box.getLeft(), 0.001f);
        assertEquals(70, box.getRight(), 0.001f);
        assertEquals(35, box.getBottom(), 0.001f);
        assertEquals(65, box.getTop(), 0.001f);
    }

    @Test
    void testNegativeSize() {
        BoxCollider box = new BoxCollider(0, 0, -40, -30);
        assertEquals(40, box.getWidth(), 0.001f);
        assertEquals(30, box.getHeight(), 0.001f);
    }
}
