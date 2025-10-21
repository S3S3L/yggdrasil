package io.github.s3s3l.yggdrasil.game._2d.shape;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import io.github.s3s3l.yggdrasil.game._2d.vector.Vector2D;

public class Polygon2DTest {
    @Test
    public void testContains_insideAndEdge() {
        Polygon2D triangle = new Polygon2D(new Vector2D[]{
            new Vector2D(0, 0), new Vector2D(2, 0), new Vector2D(1, 2)
        });
        assertTrue(triangle.contains(new Vector2D(1, 1)), "三角形内部点应在多边形内");
        assertTrue(triangle.contains(new Vector2D(1, 0)), "三角形边上点应在多边形内");
        assertFalse(triangle.contains(new Vector2D(2, 2)), "三角形外部点应不在多边形内");
    }

    @Test
    public void testContains_convexAndConcave() {
        Polygon2D convex = new Polygon2D(new Vector2D[]{
            new Vector2D(0, 0), new Vector2D(2, 0), new Vector2D(2, 2), new Vector2D(0, 2)
        });
        assertTrue(convex.contains(new Vector2D(1, 1)), "凸四边形内部点应在多边形内");
        Polygon2D concave = new Polygon2D(new Vector2D[]{
            new Vector2D(0, 0), new Vector2D(2, 0), new Vector2D(1, 1), new Vector2D(2, 2), new Vector2D(0, 2)
        });
        // 调整测试点到凹陷外部
    assertFalse(concave.contains(new Vector2D(1.5f, 1f)), "凹多边形凹陷外部点应不在多边形内");
    }

    @Test
    public void testContains_extremeValues() {
        Polygon2D poly = new Polygon2D(new Vector2D[]{
            new Vector2D(Float.MIN_VALUE, Float.MIN_VALUE),
            new Vector2D(Float.MAX_VALUE, Float.MIN_VALUE),
            new Vector2D(Float.MAX_VALUE, Float.MAX_VALUE),
            new Vector2D(Float.MIN_VALUE, Float.MAX_VALUE)
        });
        assertTrue(poly.contains(new Vector2D(1, 1)), "极大矩形内点应在多边形内");
        assertFalse(poly.contains(new Vector2D(-1, -1)), "极大矩形外点应不在多边形内");
    }
}
