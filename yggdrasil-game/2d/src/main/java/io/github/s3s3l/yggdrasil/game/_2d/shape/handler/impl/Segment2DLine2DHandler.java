package io.github.s3s3l.yggdrasil.game._2d.shape.handler.impl;

import io.github.s3s3l.yggdrasil.game._2d.shape.Line2D;
import io.github.s3s3l.yggdrasil.game._2d.shape.Segment2D;
import io.github.s3s3l.yggdrasil.game._2d.shape.ShapeType2D;
import io.github.s3s3l.yggdrasil.game._2d.shape.handler.ShapeHandler2D;
import io.github.s3s3l.yggdrasil.game._2d.utils.Shape2DHelper;
import io.github.s3s3l.yggdrasil.game._2d.vector.Vector2D;
import io.github.s3s3l.yggdrasil.game.core.shape.Shape;

public class Segment2DLine2DHandler implements ShapeHandler2D {
    @Override
    public boolean contains(Shape<Vector2D> src, Shape<Vector2D> target) {
        // 线段包含直线：理论上不成立，返回false
        return false;
    }

    @Override
    public boolean intersects(Shape<Vector2D> src, Shape<Vector2D> target) {
        if (!(src instanceof Segment2D) || !(target instanceof Line2D))
            return false;
        Segment2D seg = (Segment2D) src;
        Line2D line = (Line2D) target;
        return Shape2DHelper.segmentLineIntersect(seg, line);
    }

    @Override
    public int srcType() {
        return ShapeType2D.SEGMENT.code();
    }

    @Override
    public int targetType() {
        return ShapeType2D.LINE.code();
    }
}
