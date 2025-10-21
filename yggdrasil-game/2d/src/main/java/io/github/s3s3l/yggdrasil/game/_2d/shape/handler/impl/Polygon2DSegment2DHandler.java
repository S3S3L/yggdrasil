package io.github.s3s3l.yggdrasil.game._2d.shape.handler.impl;

import io.github.s3s3l.yggdrasil.game._2d.shape.Polygon2D;
import io.github.s3s3l.yggdrasil.game._2d.shape.Segment2D;
import io.github.s3s3l.yggdrasil.game._2d.shape.ShapeType2D;
import io.github.s3s3l.yggdrasil.game._2d.shape.handler.ShapeHandler2D;
import io.github.s3s3l.yggdrasil.game._2d.utils.Shape2DHelper;
import io.github.s3s3l.yggdrasil.game._2d.vector.Vector2D;
import io.github.s3s3l.yggdrasil.game.core.shape.Shape;

public class Polygon2DSegment2DHandler implements ShapeHandler2D {
    @Override
    public boolean contains(Shape<Vector2D> src, Shape<Vector2D> target) {
        if (!(src instanceof Polygon2D) || !(target instanceof Segment2D))
            return false;
        Polygon2D poly = (Polygon2D) src;
        Segment2D seg = (Segment2D) target;
        // 线段两端都在多边形内
        return poly.contains(seg.getStart()) && poly.contains(seg.getEnd());
    }

    @Override
    public boolean intersects(Shape<Vector2D> src, Shape<Vector2D> target) {
        if (!(src instanceof Polygon2D) || !(target instanceof Segment2D))
            return false;
        Polygon2D poly = (Polygon2D) src;
        Segment2D seg = (Segment2D) target;
        return Shape2DHelper.segmentPolygonIntersect(seg, poly);
    }

    @Override
    public int srcType() {
        return ShapeType2D.POLYGON.code();
    }

    @Override
    public int targetType() {
        return ShapeType2D.SEGMENT.code();
    }
}
