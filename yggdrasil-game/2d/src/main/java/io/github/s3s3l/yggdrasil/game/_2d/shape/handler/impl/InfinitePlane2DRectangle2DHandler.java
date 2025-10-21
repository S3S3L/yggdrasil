package io.github.s3s3l.yggdrasil.game._2d.shape.handler.impl;

import io.github.s3s3l.yggdrasil.game._2d.shape.InfinitePlane2D;
import io.github.s3s3l.yggdrasil.game._2d.shape.Rectangle2D;
import io.github.s3s3l.yggdrasil.game._2d.shape.ShapeType2D;
import io.github.s3s3l.yggdrasil.game._2d.shape.handler.ShapeHandler2D;
import io.github.s3s3l.yggdrasil.game._2d.vector.Vector2D;
import io.github.s3s3l.yggdrasil.game.core.shape.Shape;

public class InfinitePlane2DRectangle2DHandler implements ShapeHandler2D {
    @Override
    public boolean contains(Shape<Vector2D> src, Shape<Vector2D> target) {
        // 无限平面无法包含矩形
        return false;
    }

    @Override
    public boolean intersects(Shape<Vector2D> src, Shape<Vector2D> target) {
        if (!(src instanceof InfinitePlane2D) || !(target instanceof Rectangle2D))
            return false;
        // 无限平面与矩形总是相交
        return true;
    }

    @Override
    public int srcType() {
        return ShapeType2D.INFINITE_PLANE.code();
    }

    @Override
    public int targetType() {
        return ShapeType2D.RECTANGLE.code();
    }
}
