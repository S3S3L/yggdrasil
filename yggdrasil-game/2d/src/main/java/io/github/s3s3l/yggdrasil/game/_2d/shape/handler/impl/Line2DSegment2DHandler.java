package io.github.s3s3l.yggdrasil.game._2d.shape.handler.impl;

import io.github.s3s3l.yggdrasil.game._2d.shape.Line2D;
import io.github.s3s3l.yggdrasil.game._2d.shape.Segment2D;
import io.github.s3s3l.yggdrasil.game._2d.shape.ShapeType2D;
import io.github.s3s3l.yggdrasil.game._2d.shape.handler.ShapeHandler2D;
import io.github.s3s3l.yggdrasil.game._2d.utils.Shape2DHelper;
import io.github.s3s3l.yggdrasil.game._2d.vector.Vector2D;
import io.github.s3s3l.yggdrasil.game.core.shape.Shape;

public class Line2DSegment2DHandler implements ShapeHandler2D {
    @Override
    public boolean contains(Shape<Vector2D> src, Shape<Vector2D> target) {
        // 直线包含线段：线段两端点都在直线上
        if (!(src instanceof Line2D) || !(target instanceof Segment2D))
            return false;
        Line2D line = (Line2D) src;
        Segment2D seg = (Segment2D) target;
        return line.contains(seg.getStart()) && line.contains(seg.getEnd());
    }

    @Override
    public boolean intersects(Shape<Vector2D> src, Shape<Vector2D> target) {
        if (!(src instanceof Line2D) || !(target instanceof Segment2D))
            return false;
        Line2D line = (Line2D) src;
        Segment2D seg = (Segment2D) target;
        return Shape2DHelper.segmentLineIntersect(
                seg, line);
    }

    @Override
    public int srcType() {
        return ShapeType2D.LINE.code();
    }

    @Override
    public int targetType() {
        return ShapeType2D.SEGMENT.code();
    }
}
