package io.github.s3s3l.yggdrasil.game.core.physics.collider;

import io.github.s3s3l.yggdrasil.game.core.basic.Vector2;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * PolygonCollider 单元测试
 */
class PolygonColliderTest {

    @Test
    void testConstructor() {
        List<Vector2> vertices = Arrays.asList(
                new Vector2(-10, -10),
                new Vector2(10, -10),
                new Vector2(10, 10),
                new Vector2(-10, 10)
        );
        PolygonCollider polygon = new PolygonCollider(50, 50, vertices);
        assertEquals(50, polygon.getPosition().x, 0.001f);
        assertEquals(50, polygon.getPosition().y, 0.001f);
        assertEquals(4, polygon.getVertexCount());
    }

    @Test
    void testGetType() {
        PolygonCollider polygon = PolygonCollider.createRectangle(0, 0, 10, 10);
        assertEquals(Collider.ColliderType.POLYGON, polygon.getType());
    }

    @Test
    void testCreateRectangle() {
        PolygonCollider rect = PolygonCollider.createRectangle(50, 50, 40, 30);
        assertEquals(4, rect.getVertexCount());
        Rectangle bounds = rect.getBounds();
        assertEquals(30, bounds.minX, 0.001f);
        assertEquals(35, bounds.minY, 0.001f);
        assertEquals(70, bounds.maxX, 0.001f);
        assertEquals(65, bounds.maxY, 0.001f);
    }

    @Test
    void testCreateRegularPolygon() {
        PolygonCollider hexagon = PolygonCollider.createRegularPolygon(0, 0, 10, 6, 0);
        assertEquals(6, hexagon.getVertexCount());
    }

    @Test
    void testCreateRegularPolygonTriangle() {
        PolygonCollider triangle = PolygonCollider.createRegularPolygon(0, 0, 10, 3, 0);
        assertEquals(3, triangle.getVertexCount());
    }

    @Test
    void testSetPosition() {
        PolygonCollider polygon = PolygonCollider.createRectangle(0, 0, 20, 20);
        polygon.setPosition(50, 50);
        assertEquals(50, polygon.getPosition().x, 0.001f);
        assertEquals(50, polygon.getPosition().y, 0.001f);
    }

    @Test
    void testSetRotation() {
        PolygonCollider polygon = PolygonCollider.createRectangle(0, 0, 20, 20);
        polygon.setRotation((float) Math.PI / 4);
        assertEquals(Math.PI / 4, polygon.getRotation(), 0.001f);
    }

    @Test
    void testContains() {
        PolygonCollider rect = PolygonCollider.createRectangle(0, 0, 20, 20);
        assertTrue(rect.contains(0, 0)); // 中心
        assertTrue(rect.contains(5, 5)); // 内部
        assertTrue(rect.contains(9, 9)); // 接近边界但在内部
        assertFalse(rect.contains(11, 0)); // 外部
        assertFalse(rect.contains(10, 10)); // 边界（射线法可能不确定）
    }

    @Test
    void testContainsTriangle() {
        PolygonCollider triangle = PolygonCollider.createRegularPolygon(0, 0, 10, 3, 0);
        assertTrue(triangle.contains(0, 0)); // 中心应该在内部
    }

    @Test
    void testIntersectsPolygon() {
        PolygonCollider a = PolygonCollider.createRectangle(0, 0, 20, 20);
        PolygonCollider b = PolygonCollider.createRectangle(15, 0, 20, 20); // 重叠
        PolygonCollider c = PolygonCollider.createRectangle(50, 0, 20, 20); // 不重叠

        assertTrue(a.intersects(b));
        assertFalse(a.intersects(c));
    }

    @Test
    void testIntersectsBox() {
        PolygonCollider polygon = PolygonCollider.createRectangle(0, 0, 20, 20);
        BoxCollider box1 = new BoxCollider(15, 0, 20, 20); // 重叠
        BoxCollider box2 = new BoxCollider(50, 0, 20, 20); // 不重叠

        assertTrue(polygon.intersects(box1));
        assertFalse(polygon.intersects(box2));
    }

    @Test
    void testIntersectsCircle() {
        PolygonCollider polygon = PolygonCollider.createRectangle(0, 0, 20, 20);
        CircleCollider circle1 = new CircleCollider(0, 0, 5); // 内部
        CircleCollider circle2 = new CircleCollider(50, 0, 5); // 外部

        assertTrue(polygon.intersects(circle1));
        assertFalse(polygon.intersects(circle2));
    }

    @Test
    void testIntersectsCircleEdge() {
        PolygonCollider polygon = PolygonCollider.createRectangle(0, 0, 20, 20);
        CircleCollider circle = new CircleCollider(12, 0, 5); // 有重叠

        assertTrue(polygon.intersects(circle));
    }

    @Test
    void testCopy() {
        PolygonCollider original = PolygonCollider.createRectangle(50, 50, 40, 30);
        original.setRotation((float) Math.PI / 6);
        PolygonCollider copy = original.copy();

        assertEquals(original.getPosition().x, copy.getPosition().x, 0.001f);
        assertEquals(original.getPosition().y, copy.getPosition().y, 0.001f);
        assertEquals(original.getRotation(), copy.getRotation(), 0.001f);
        assertEquals(original.getVertexCount(), copy.getVertexCount());

        // 修改副本不应影响原件
        copy.setPosition(100, 100);
        assertEquals(50, original.getPosition().x, 0.001f);
    }

    @Test
    void testGetTransformedVertex() {
        PolygonCollider polygon = PolygonCollider.createRectangle(0, 0, 20, 20);
        polygon.setRotation(0);

        // 旋转 0 度时，顶点应该与原始顶点一致（加上中心偏移）
        Vector2 v = polygon.getTransformedVertex(0);
        assertNotNull(v);
    }
}
