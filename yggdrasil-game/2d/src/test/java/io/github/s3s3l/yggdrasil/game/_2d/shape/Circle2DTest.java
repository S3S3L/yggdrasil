package io.github.s3s3l.yggdrasil.game._2d.shape;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import io.github.s3s3l.yggdrasil.game._2d.vector.Vector2D;

public class Circle2DTest {
    @Test
    public void testContains_centerAndEdge() {
        Circle2D circle = new Circle2D(new Vector2D(0f, 0f), 1.0f);
        assertTrue(circle.contains(new Vector2D(0f, 0f)), "圆心应在圆内");
        assertTrue(circle.contains(new Vector2D(1f, 0f)), "半径上的点应在圆内");
        assertTrue(circle.contains(new Vector2D(0f, 1f)), "半径上的点应在圆内");
        assertTrue(circle.contains(new Vector2D(-1f, 0f)), "半径上的点应在圆内");
        assertTrue(circle.contains(new Vector2D(0f, -1f)), "半径上的点应在圆内");
    }

    @Test
    public void testContains_outsideAndInside() {
        Circle2D circle = new Circle2D(new Vector2D(0f, 0f), 1.0f);
        assertFalse(circle.contains(new Vector2D(2f, 0f)), "圆外点应不在圆内");
        assertTrue(circle.contains(new Vector2D(0.5f, 0.5f)), "圆内点应在圆内");
    }

    @Test
    public void testContains_extremeValues() {
        Circle2D circle = new Circle2D(new Vector2D(Float.MAX_VALUE, Float.MAX_VALUE), Float.MAX_VALUE);
        assertTrue(circle.contains(new Vector2D(Float.MAX_VALUE, Float.MAX_VALUE)), "极大值圆心应在圆内");
        assertTrue(circle.contains(new Vector2D(0f, 0f)), "原点应在极大值圆内");
        Circle2D smallCircle = new Circle2D(new Vector2D(0f, 0f), Float.MIN_VALUE);
        assertTrue(smallCircle.contains(new Vector2D(0f, 0f)), "极小圆心应在圆内");
        assertFalse(smallCircle.contains(new Vector2D(Float.MIN_VALUE, 0f)), "极小圆外点应不在圆内");
    }
}
