package io.github.s3s3l.yggdrasil.game._2d.shape.handler.impl;

import io.github.s3s3l.yggdrasil.game._2d.shape.Circle2D;
import io.github.s3s3l.yggdrasil.game._2d.shape.Line2D;
import io.github.s3s3l.yggdrasil.game._2d.shape.ShapeType2D;
import io.github.s3s3l.yggdrasil.game._2d.shape.handler.ShapeHandler2D;
import io.github.s3s3l.yggdrasil.game._2d.utils.Shape2DHelper;
import io.github.s3s3l.yggdrasil.game._2d.vector.Vector2D;
import io.github.s3s3l.yggdrasil.game.core.shape.Shape;

public class Circle2DLine2DHandler implements ShapeHandler2D {
    @Override
    public boolean contains(Shape<Vector2D> src, Shape<Vector2D> target) {
        // 圆包含直线：只有半径为0且圆心在直线上才可能
        if (!(src instanceof Circle2D) || !(target instanceof Line2D))
            return false;
        Circle2D circle = (Circle2D) src;
        Line2D line = (Line2D) target;
        return circle.getRadius() == 0 && line.contains(circle.getCenter());
    }

    @Override
    public boolean intersects(Shape<Vector2D> src, Shape<Vector2D> target) {
        if (!(src instanceof Circle2D) || !(target instanceof Line2D))
            return false;
        Circle2D circle = (Circle2D) src;
        Line2D line = (Line2D) target;
        return Shape2DHelper.lineCircleIntersect(line, circle);
    }

    @Override
    public int srcType() {
        return ShapeType2D.CIRCLE.code();
    }

    @Override
    public int targetType() {
        return ShapeType2D.LINE.code();
    }
}
