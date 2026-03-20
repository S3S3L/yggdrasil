package io.github.s3s3l.yggdrasil.game.core.physics;

import io.github.s3s3l.yggdrasil.game.core.basic.Vector2;
import io.github.s3s3l.yggdrasil.game.core.physics.collider.BoxCollider;
import io.github.s3s3l.yggdrasil.game.core.physics.collider.CircleCollider;
import io.github.s3s3l.yggdrasil.game.core.physics.collider.Collider;
import io.github.s3s3l.yggdrasil.game.core.physics.collider.PolygonCollider;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * RayCaster 单元测试
 */
class RayCasterTest {

    @Test
    void testCastBoxHit() {
        Ray ray = new Ray(new Vector2(0, 0), new Vector2(1, 0));
        BoxCollider box = new BoxCollider(50, 0, 20, 20);

        RayCastHit hit = RayCaster.cast(ray, box);

        assertTrue(hit.hit);
        assertNotNull(hit.point);
        assertTrue(hit.distance > 0);
        assertEquals(box, hit.collider);
    }

    @Test
    void testCastBoxMiss() {
        Ray ray = new Ray(new Vector2(0, 0), new Vector2(1, 0));
        BoxCollider box = new BoxCollider(0, 50, 20, 20);

        RayCastHit hit = RayCaster.cast(ray, box);

        assertFalse(hit.hit);
    }

    @Test
    void testCastBoxMissBehind() {
        Ray ray = new Ray(new Vector2(0, 0), new Vector2(1, 0));
        BoxCollider box = new BoxCollider(-50, 0, 20, 20);

        RayCastHit hit = RayCaster.cast(ray, box);

        assertFalse(hit.hit);
    }

    @Test
    void testCastCircleHit() {
        Ray ray = new Ray(new Vector2(0, 0), new Vector2(1, 0));
        CircleCollider circle = new CircleCollider(50, 0, 10);

        RayCastHit hit = RayCaster.cast(ray, circle);

        assertTrue(hit.hit);
        assertNotNull(hit.point);
        assertTrue(hit.distance > 0);
        assertEquals(circle, hit.collider);
    }

    @Test
    void testCastCircleMiss() {
        Ray ray = new Ray(new Vector2(0, 0), new Vector2(1, 0));
        CircleCollider circle = new CircleCollider(0, 50, 10);

        RayCastHit hit = RayCaster.cast(ray, circle);

        assertFalse(hit.hit);
    }

    @Test
    void testCastCircleStartInside() {
        Ray ray = new Ray(new Vector2(0, 0), new Vector2(1, 0));
        CircleCollider circle = new CircleCollider(0, 0, 10);

        RayCastHit hit = RayCaster.cast(ray, circle);

        assertTrue(hit.hit);
        assertEquals(0, hit.distance, 0.001f);
    }

    @Test
    void testCastPolygonHit() {
        Ray ray = new Ray(new Vector2(0, 0), new Vector2(1, 0));
        PolygonCollider polygon = PolygonCollider.createRectangle(50, 0, 20, 20);

        RayCastHit hit = RayCaster.cast(ray, polygon);

        assertTrue(hit.hit);
        assertNotNull(hit.point);
    }

    @Test
    void testCastPolygonMiss() {
        Ray ray = new Ray(new Vector2(0, 0), new Vector2(1, 0));
        PolygonCollider polygon = PolygonCollider.createRectangle(0, 50, 20, 20);

        RayCastHit hit = RayCaster.cast(ray, polygon);

        assertFalse(hit.hit);
    }

    @Test
    void testCastWithMaxDistanceHit() {
        Ray ray = new Ray(new Vector2(0, 0), new Vector2(1, 0));
        BoxCollider box = new BoxCollider(50, 0, 20, 20);

        RayCastHit hit = RayCaster.cast(ray, box, 100);

        assertTrue(hit.hit);
        assertTrue(hit.distance < 100);
    }

    @Test
    void testCastWithMaxDistanceMiss() {
        Ray ray = new Ray(new Vector2(0, 0), new Vector2(1, 0));
        BoxCollider box = new BoxCollider(50, 0, 20, 20);

        RayCastHit hit = RayCaster.cast(ray, box, 30);

        assertFalse(hit.hit);
    }

    @Test
    void testCastAll() {
        Ray ray = new Ray(new Vector2(0, 0), new Vector2(1, 0));
        BoxCollider box1 = new BoxCollider(50, 0, 20, 20);
        BoxCollider box2 = new BoxCollider(150, 0, 20, 20);
        BoxCollider box3 = new BoxCollider(0, 50, 20, 20); // 不会命中

        List<Collider> colliders = Arrays.asList(box1, box2, box3);

        List<RayCastHit> hits = RayCaster.castAllToList(ray, colliders);

        assertEquals(2, hits.size());
        assertTrue(hits.get(0).distance < hits.get(1).distance);
    }

    @Test
    void testCastAllFindClosest() {
        Ray ray = new Ray(new Vector2(0, 0), new Vector2(1, 0));
        BoxCollider box1 = new BoxCollider(150, 0, 20, 20);
        BoxCollider box2 = new BoxCollider(50, 0, 20, 20);

        List<Collider> colliders = Arrays.asList(box1, box2);

        RayCastHit closestHit = RayCaster.castAll(ray, colliders);

        assertTrue(closestHit.hit);
        assertEquals(box2, closestHit.collider);
    }

    @Test
    void testRayCastHitCopy() {
        RayCastHit original = new RayCastHit();
        original.set(true, new Vector2(10, 0), new Vector2(-1, 0), 10.0f,
                new BoxCollider(50, 0, 20, 20));

        RayCastHit copy = original.copy();

        assertEquals(original.hit, copy.hit);
        assertEquals(original.point.x, copy.point.x, 0.001f);
        assertEquals(original.point.y, copy.point.y, 0.001f);
        assertEquals(original.distance, copy.distance, 0.001f);
    }

    @Test
    void testRayCastHitClear() {
        RayCastHit hit = new RayCastHit();
        hit.set(true, new Vector2(10, 0), new Vector2(-1, 0), 10.0f,
                new BoxCollider(50, 0, 20, 20));

        hit.clear();

        assertFalse(hit.hit);
        assertEquals(0, hit.distance, 0.001f);
        assertNull(hit.collider);
    }

    @Test
    void testCastVerticalRay() {
        Ray ray = new Ray(new Vector2(50, -100), new Vector2(0, 1));
        BoxCollider box = new BoxCollider(50, 0, 20, 20);

        RayCastHit hit = RayCaster.cast(ray, box);

        assertTrue(hit.hit);
        assertEquals(90, hit.distance, 1.0f);
    }
}
