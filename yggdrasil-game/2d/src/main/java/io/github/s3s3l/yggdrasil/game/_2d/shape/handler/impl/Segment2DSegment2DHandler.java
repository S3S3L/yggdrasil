package io.github.s3s3l.yggdrasil.game._2d.shape.handler.impl;

import io.github.s3s3l.yggdrasil.game._2d.shape.Segment2D;
import io.github.s3s3l.yggdrasil.game._2d.shape.ShapeType2D;
import io.github.s3s3l.yggdrasil.game._2d.shape.handler.ShapeHandler2D;
import io.github.s3s3l.yggdrasil.game._2d.utils.Shape2DHelper;
import io.github.s3s3l.yggdrasil.game._2d.vector.Vector2D;
import io.github.s3s3l.yggdrasil.game.core.shape.Shape;

public class Segment2DSegment2DHandler implements ShapeHandler2D {
    @Override
    public boolean contains(Shape<Vector2D> src, Shape<Vector2D> target) {
        if (!(src instanceof Segment2D) || !(target instanceof Segment2D))
            return false;
        Segment2D s1 = (Segment2D) src;
        Segment2D s2 = (Segment2D) target;
        // s1包含s2需s2两端都在s1上
        return Shape2DHelper.onSegment(s1.getStart(), s1.getEnd(), s2.getStart()) &&
                Shape2DHelper.onSegment(s1.getStart(), s1.getEnd(), s2.getEnd());
    }

    @Override
    public boolean intersects(Shape<Vector2D> src, Shape<Vector2D> target) {
        if (!(src instanceof Segment2D) || !(target instanceof Segment2D))
            return false;
        Segment2D s1 = (Segment2D) src;
        Segment2D s2 = (Segment2D) target;
        return Shape2DHelper.segmentsIntersect(s1, s2);
    }

    @Override
    public int srcType() {
        return ShapeType2D.SEGMENT.code();
    }

    @Override
    public int targetType() {
        return ShapeType2D.SEGMENT.code();
    }
}
