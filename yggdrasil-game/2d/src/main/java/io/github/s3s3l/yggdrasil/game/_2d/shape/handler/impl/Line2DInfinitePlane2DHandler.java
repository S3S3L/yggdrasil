package io.github.s3s3l.yggdrasil.game._2d.shape.handler.impl;

import io.github.s3s3l.yggdrasil.game._2d.shape.InfinitePlane2D;
import io.github.s3s3l.yggdrasil.game._2d.shape.Line2D;
import io.github.s3s3l.yggdrasil.game._2d.shape.ShapeType2D;
import io.github.s3s3l.yggdrasil.game._2d.shape.handler.ShapeHandler2D;
import io.github.s3s3l.yggdrasil.game._2d.vector.Vector2D;
import io.github.s3s3l.yggdrasil.game.core.shape.Shape;

public class Line2DInfinitePlane2DHandler implements ShapeHandler2D {
    @Override
    public boolean contains(Shape<Vector2D> src, Shape<Vector2D> target) {
        // 直线无法包含无限平面
        return false;
    }

    @Override
    public boolean intersects(Shape<Vector2D> src, Shape<Vector2D> target) {
        if (!(src instanceof Line2D) || !(target instanceof InfinitePlane2D))
            return false;
        // 直线与无限平面总是相交
        return true;
    }

    @Override
    public int srcType() {
        return ShapeType2D.LINE.code();
    }

    @Override
    public int targetType() {
        return ShapeType2D.INFINITE_PLANE.code();
    }
}
