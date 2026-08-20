package io.github.s3s3l.yggdrasil.game._2d.shape;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import io.github.s3s3l.yggdrasil.game._2d.vector.Vector2D;

public class Segment2DTest {
    @Test
    public void testContains_pointOnSegment() {
        Segment2D segment = new Segment2D(new Vector2D(0, 0), new Vector2D(2, 2));
        assertTrue(segment.contains(new Vector2D(1, 1)));
        assertTrue(segment.contains(new Vector2D(0, 0)));
        assertTrue(segment.contains(new Vector2D(2, 2)));
    }

    @Test
    public void testContains_pointNotOnSegment() {
        Segment2D segment = new Segment2D(new Vector2D(0, 0), new Vector2D(2, 2));
        assertFalse(segment.contains(new Vector2D(3, 3)));
        assertFalse(segment.contains(new Vector2D(-1, -1)));
        assertFalse(segment.contains(new Vector2D(1, 2)));
    }

    @Test
    public void testContains_verticalSegment() {
        Segment2D segment = new Segment2D(new Vector2D(1, 0), new Vector2D(1, 2));
        assertTrue(segment.contains(new Vector2D(1, 1)));
        assertFalse(segment.contains(new Vector2D(2, 1)));
    }

    @Test
    public void testContains_horizontalSegment() {
        Segment2D segment = new Segment2D(new Vector2D(0, 1), new Vector2D(2, 1));
        assertTrue(segment.contains(new Vector2D(1, 1)));
        assertFalse(segment.contains(new Vector2D(1, 2)));
    }
}
