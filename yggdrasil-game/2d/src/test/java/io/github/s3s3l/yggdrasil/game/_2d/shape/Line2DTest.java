package io.github.s3s3l.yggdrasil.game._2d.shape;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import io.github.s3s3l.yggdrasil.game._2d.vector.Vector2D;

public class Line2DTest {
    @Test
    public void testContains_onLineAndNotOnLine() {
        Line2D line = new Line2D(new Vector2D(0, 0), new Vector2D(1, 1));
        assertTrue(line.contains(new Vector2D(2, 2)), "直线上点应在直线内");
        assertTrue(line.contains(new Vector2D(-1, -1)), "直线上负方向点应在直线内");
        assertFalse(line.contains(new Vector2D(1, 2)), "不共线点应不在直线内");
    }

    @Test
    public void testContains_extremeValues() {
        Line2D line = new Line2D(new Vector2D(Float.MIN_VALUE, Float.MIN_VALUE), new Vector2D(1, 1));
        assertTrue(line.contains(new Vector2D(Float.MAX_VALUE, Float.MAX_VALUE)), "极大值共线点应在直线内");
        assertFalse(line.contains(new Vector2D(Float.MAX_VALUE, 0)), "极大值不共线点应不在直线内");
    }
}
