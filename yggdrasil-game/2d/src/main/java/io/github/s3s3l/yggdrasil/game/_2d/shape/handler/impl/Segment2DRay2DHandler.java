package io.github.s3s3l.yggdrasil.game._2d.shape.handler.impl;

import io.github.s3s3l.yggdrasil.game._2d.shape.Ray2D;
import io.github.s3s3l.yggdrasil.game._2d.shape.Segment2D;
import io.github.s3s3l.yggdrasil.game._2d.shape.ShapeType2D;
import io.github.s3s3l.yggdrasil.game._2d.shape.handler.ShapeHandler2D;
import io.github.s3s3l.yggdrasil.game._2d.utils.Shape2DHelper;
import io.github.s3s3l.yggdrasil.game._2d.vector.Vector2D;
import io.github.s3s3l.yggdrasil.game.core.shape.Shape;

public class Segment2DRay2DHandler implements ShapeHandler2D {
    @Override
    public boolean contains(Shape<Vector2D> src, Shape<Vector2D> target) {
        // 线段无法包含射线
        return false;
    }

    @Override
    public boolean intersects(Shape<Vector2D> src, Shape<Vector2D> target) {
        if (!(src instanceof Segment2D) || !(target instanceof Ray2D))
            return false;
        Segment2D seg = (Segment2D) src;
        Ray2D ray = (Ray2D) target;
        return Shape2DHelper.segmentRayIntersect(seg, ray);
    }

    @Override
    public int srcType() {
        return ShapeType2D.SEGMENT.code();
    }

    @Override
    public int targetType() {
        return ShapeType2D.RAY.code();
    }
}
