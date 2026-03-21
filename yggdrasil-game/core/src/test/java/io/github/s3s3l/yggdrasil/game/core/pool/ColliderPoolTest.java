package io.github.s3s3l.yggdrasil.game.core.pool;

import io.github.s3s3l.yggdrasil.game.core.basic.Vector2;
import io.github.s3s3l.yggdrasil.game.core.physics.collider.BoxCollider;
import io.github.s3s3l.yggdrasil.game.core.physics.collider.CircleCollider;
import io.github.s3s3l.yggdrasil.game.core.physics.collider.Collider;
import io.github.s3s3l.yggdrasil.game.core.physics.collider.PolygonCollider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 碰撞体对象池测试
 */
@DisplayName("碰撞体对象池测试")
class ColliderPoolTest {

    private ColliderPool pool;

    @BeforeEach
    void setUp() {
        pool = ColliderPool.get();
        pool.clear();  // 清空之前的状态
    }

    @Test
    @DisplayName("借用和归还矩形碰撞体")
    void testBorrowAndReturnBox() {
        // 借用矩形碰撞体
        BoxCollider box = pool.borrowBox(100, 100, 50, 30);
        assertNotNull(box);
        assertEquals(100, box.getPosition().x);
        assertEquals(100, box.getPosition().y);
        assertEquals(50, box.getWidth());
        assertEquals(30, box.getHeight());

        // 归还
        pool.returnCollider(box);
    }

    @Test
    @DisplayName("借用矩形碰撞体（默认原点）")
    void testBorrowBoxAtOrigin() {
        BoxCollider box = pool.borrowBox(40, 20);
        assertNotNull(box);
        assertEquals(0, box.getPosition().x);
        assertEquals(0, box.getPosition().y);
        assertEquals(40, box.getWidth());
        assertEquals(20, box.getHeight());

        pool.returnCollider(box);
    }

    @Test
    @DisplayName("借用和归还圆形碰撞体")
    void testBorrowAndReturnCircle() {
        CircleCollider circle = pool.borrowCircle(50, 50, 10);
        assertNotNull(circle);
        assertEquals(50, circle.getCenterX());
        assertEquals(50, circle.getCenterY());
        assertEquals(10, circle.getRadius());

        pool.returnCollider(circle);
    }

    @Test
    @DisplayName("借用圆形碰撞体（默认原点）")
    void testBorrowCircleAtOrigin() {
        CircleCollider circle = pool.borrowCircle(15);
        assertNotNull(circle);
        assertEquals(0, circle.getCenterX());
        assertEquals(0, circle.getCenterY());
        assertEquals(15, circle.getRadius());

        pool.returnCollider(circle);
    }

    @Test
    @DisplayName("借用和归还多边形碰撞体")
    void testBorrowAndReturnPolygon() {
        var vertices = Arrays.asList(
            new Vector2(-10, -10),
            new Vector2(10, -10),
            new Vector2(10, 10),
            new Vector2(-10, 10)
        );

        PolygonCollider polygon = pool.borrowPolygon(0, 0, vertices);
        assertNotNull(polygon);
        assertEquals(0, polygon.getPosition().x);
        assertEquals(0, polygon.getPosition().y);
        assertEquals(4, polygon.getVertexCount());

        pool.returnCollider(polygon);
    }

    @Test
    @DisplayName("碰撞体池统计信息")
    void testStats() {
        BoxCollider box1 = pool.borrowBox(0, 0, 10, 10);
        BoxCollider box2 = pool.borrowBox(0, 0, 20, 20);

        ObjectPool.PoolStats stats = pool.getStats(Collider.ColliderType.BOX);
        assertNotNull(stats);
        assertTrue(stats.getBorrowCount() >= 2);

        pool.returnCollider(box1);
        pool.returnCollider(box2);
    }

    @Test
    @DisplayName("碰撞体归还后重用")
    void testColliderReuse() {
        // 借用并设置数据
        BoxCollider box1 = pool.borrowBox(100, 100, 50, 50);

        // 归还
        pool.returnCollider(box1);

        // 再次借用，应该是同一个对象但数据已重置
        BoxCollider box2 = pool.borrowBox(200, 200, 30, 30);

        // 验证是同一个对象（从池中取回）
        assertSame(box1, box2);
        assertEquals(200, box2.getPosition().x);
        assertEquals(200, box2.getPosition().y);
    }

    @Test
    @DisplayName("清空碰撞体池")
    void testClear() {
        BoxCollider box = pool.borrowBox(0, 0, 10, 10);
        pool.returnCollider(box);

        pool.clear();

        ObjectPool<BoxCollider> boxPool = (ObjectPool<BoxCollider>) getPoolInternal(pool, Collider.ColliderType.BOX);
        assertNotNull(boxPool);
        assertEquals(0, boxPool.getAvailableCount());
    }

    @Test
    @DisplayName("归还 null 碰撞体")
    void testReturnNull() {
        // 不应该抛出异常
        assertDoesNotThrow(() -> pool.returnCollider((BoxCollider) null));
    }

    // Helper method to access private map via reflection for testing
    private static Object getPoolInternal(ColliderPool pool, Collider.ColliderType type) {
        try {
            java.lang.reflect.Field field = ColliderPool.class.getDeclaredField("colliderPools");
            field.setAccessible(true);
            @SuppressWarnings("unchecked")
            java.util.Map<Collider.ColliderType, ObjectPool<? extends PoolableObject<?>>> map =
                (java.util.Map<Collider.ColliderType, ObjectPool<? extends PoolableObject<?>>>) field.get(pool);
            return map.get(type);
        } catch (Exception e) {
            return null;
        }
    }
}
