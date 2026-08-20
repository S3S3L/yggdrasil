package io.github.s3s3l.yggdrasil.game._2d.shape;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import io.github.s3s3l.yggdrasil.game._2d.vector.Vector2D;

public class InfinitePlane2DTest {
    @Test
    public void testContains_anyPoint() {
        InfinitePlane2D plane = new InfinitePlane2D();
        assertTrue(plane.contains(new Vector2D(0, 0)), "任意点应在无限平面内");
        assertTrue(plane.contains(new Vector2D(Float.MAX_VALUE, Float.MIN_VALUE)), "极值点应在无限平面内");
        assertTrue(plane.contains(new Vector2D(-1000, 1000)), "负值点应在无限平面内");
    }
}
