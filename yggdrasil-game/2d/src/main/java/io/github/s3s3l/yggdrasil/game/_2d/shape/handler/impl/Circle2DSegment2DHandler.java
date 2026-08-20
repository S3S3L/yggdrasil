package io.github.s3s3l.yggdrasil.game._2d.shape.handler.impl;

import io.github.s3s3l.yggdrasil.game._2d.shape.Circle2D;
import io.github.s3s3l.yggdrasil.game._2d.shape.Segment2D;
import io.github.s3s3l.yggdrasil.game._2d.shape.ShapeType2D;
import io.github.s3s3l.yggdrasil.game._2d.shape.handler.ShapeHandler2D;
import io.github.s3s3l.yggdrasil.game._2d.utils.Shape2DHelper;
import io.github.s3s3l.yggdrasil.game._2d.vector.Vector2D;
import io.github.s3s3l.yggdrasil.game.core.shape.Shape;

public class Circle2DSegment2DHandler implements ShapeHandler2D {
    @Override
    public boolean contains(Shape<Vector2D> src, Shape<Vector2D> target) {
        if (!(src instanceof Circle2D) || !(target instanceof Segment2D))
            return false;
        Circle2D circle = (Circle2D) src;
        Segment2D seg = (Segment2D) target;
        // 线段两端都在圆内
        Vector2D s = seg.getStart();
        Vector2D e = seg.getEnd();
        float dx1 = s.getX() - circle.getCenter().getX();
        float dy1 = s.getY() - circle.getCenter().getY();
        float dx2 = e.getX() - circle.getCenter().getX();
        float dy2 = e.getY() - circle.getCenter().getY();
        double dist1 = Math.sqrt(dx1 * dx1 + dy1 * dy1);
        double dist2 = Math.sqrt(dx2 * dx2 + dy2 * dy2);
        return dist1 <= circle.getRadius() && dist2 <= circle.getRadius();
    }

    @Override
    public boolean intersects(Shape<Vector2D> src, Shape<Vector2D> target) {
        if (!(src instanceof Circle2D) || !(target instanceof Segment2D))
            return false;
        Circle2D circle = (Circle2D) src;
        Segment2D seg = (Segment2D) target;
        return Shape2DHelper.segmentCircleIntersect(seg, circle);
    }

    @Override
    public int srcType() {
        return ShapeType2D.CIRCLE.code();
    }

    @Override
    public int targetType() {
        return ShapeType2D.SEGMENT.code();
    }
}
