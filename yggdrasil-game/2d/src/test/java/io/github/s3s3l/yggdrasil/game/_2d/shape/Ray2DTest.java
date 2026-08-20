package io.github.s3s3l.yggdrasil.game._2d.shape;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import io.github.s3s3l.yggdrasil.game._2d.vector.Vector2D;

public class Ray2DTest {
    @Test
    public void testContains_onRayAndNotOnRay() {
        Ray2D ray = new Ray2D(new Vector2D(0, 0), new Vector2D(1, 0));
        assertTrue(ray.contains(new Vector2D(1, 0)), "射线上点应在射线内");
        assertTrue(ray.contains(new Vector2D(100, 0)), "射线上远点应在射线内");
        assertFalse(ray.contains(new Vector2D(-1, 0)), "反向点应不在射线内");
        assertFalse(ray.contains(new Vector2D(0, 1)), "不共线点应不在射线内");
    }

    @Test
    public void testContains_extremeValues() {
        Ray2D ray = new Ray2D(new Vector2D(Float.MIN_VALUE, Float.MIN_VALUE), new Vector2D(1, 1));
        assertTrue(ray.contains(new Vector2D(Float.MIN_VALUE + 1, Float.MIN_VALUE + 1)), "极小值射线上点应在射线内");
        assertFalse(ray.contains(new Vector2D(Float.MAX_VALUE, 0)), "极大值远离射线点应不在射线内");
    }
}
