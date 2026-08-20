package io.github.s3s3l.yggdrasil.game._2d.shape;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import io.github.s3s3l.yggdrasil.game._2d.vector.Vector2D;

public class Rectangle2DTest {
    @Test
    public void testContains_insideAndEdge() {
        Rectangle2D rect = new Rectangle2D(new Vector2D(0, 0), 2f, 2f);
        assertTrue(rect.contains(new Vector2D(1, 1)), "矩形内部点应在矩形内");
        assertTrue(rect.contains(new Vector2D(0, 0)), "左上角应在矩形内");
        assertTrue(rect.contains(new Vector2D(2, 2)), "右下角应在矩形内");
        assertTrue(rect.contains(new Vector2D(2, 0)), "右上角应在矩形内");
        assertTrue(rect.contains(new Vector2D(0, 2)), "左下角应在矩形内");
    }

    @Test
    public void testContains_outside() {
        Rectangle2D rect = new Rectangle2D(new Vector2D(0, 0), 2f, 2f);
        assertFalse(rect.contains(new Vector2D(-1, 1)), "矩形外部点应不在矩形内");
        assertFalse(rect.contains(new Vector2D(1, 3)), "矩形外部点应不在矩形内");
    }

    @Test
    public void testContains_extremeValues() {
        Rectangle2D rect = new Rectangle2D(new Vector2D(Float.MIN_VALUE, Float.MIN_VALUE), Float.MAX_VALUE, Float.MAX_VALUE);
        assertTrue(rect.contains(new Vector2D(Float.MIN_VALUE, Float.MIN_VALUE)), "极小值左上角应在矩形内");
        assertTrue(rect.contains(new Vector2D(Float.MAX_VALUE, Float.MAX_VALUE)), "极大值右下角应在矩形内");
        assertFalse(rect.contains(new Vector2D(-1, -1)), "极小值外部点应不在矩形内");
    }
}
