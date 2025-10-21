package io.github.s3s3l.yggdrasil.game._2d.shape.handler.impl;

import io.github.s3s3l.yggdrasil.game._2d.shape.InfinitePlane2D;
import io.github.s3s3l.yggdrasil.game._2d.shape.Segment2D;
import io.github.s3s3l.yggdrasil.game._2d.shape.ShapeType2D;
import io.github.s3s3l.yggdrasil.game._2d.shape.handler.ShapeHandler2D;
import io.github.s3s3l.yggdrasil.game._2d.vector.Vector2D;
import io.github.s3s3l.yggdrasil.game.core.shape.Shape;

public class Segment2DInfinitePlane2DHandler implements ShapeHandler2D {
    @Override
    public boolean contains(Shape<Vector2D> src, Shape<Vector2D> target) {
        // 线段无法包含无限平面
        return false;
    }

    @Override
    public boolean intersects(Shape<Vector2D> src, Shape<Vector2D> target) {
        if (!(src instanceof Segment2D) || !(target instanceof InfinitePlane2D))
            return false;
        // 线段与无限平面总是相交
        return true;
    }

    @Override
    public int srcType() {
        return ShapeType2D.SEGMENT.code();
    }

    @Override
    public int targetType() {
        return ShapeType2D.INFINITE_PLANE.code();
    }
}
