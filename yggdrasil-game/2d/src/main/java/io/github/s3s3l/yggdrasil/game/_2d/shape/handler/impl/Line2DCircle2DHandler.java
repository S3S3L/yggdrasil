package io.github.s3s3l.yggdrasil.game._2d.shape.handler.impl;

import io.github.s3s3l.yggdrasil.game._2d.shape.Circle2D;
import io.github.s3s3l.yggdrasil.game._2d.shape.Line2D;
import io.github.s3s3l.yggdrasil.game._2d.shape.ShapeType2D;
import io.github.s3s3l.yggdrasil.game._2d.shape.handler.ShapeHandler2D;
import io.github.s3s3l.yggdrasil.game._2d.utils.Shape2DHelper;
import io.github.s3s3l.yggdrasil.game._2d.vector.Vector2D;
import io.github.s3s3l.yggdrasil.game.core.shape.Shape;

public class Line2DCircle2DHandler implements ShapeHandler2D {
    @Override
    public boolean contains(Shape<Vector2D> src, Shape<Vector2D> target) {
        // 直线包含圆：圆心在直线上，且圆上任意一点都在直线上（即半径为0）
        if (!(src instanceof Line2D) || !(target instanceof Circle2D))
            return false;
        Line2D line = (Line2D) src;
        Circle2D circle = (Circle2D) target;
        // 只有半径为0时才可能被直线完全包含
        return circle.getRadius() == 0 && line.contains(circle.getCenter());
    }

    @Override
    public boolean intersects(Shape<Vector2D> src, Shape<Vector2D> target) {
        if (!(src instanceof Line2D) || !(target instanceof Circle2D))
            return false;
        Line2D line = (Line2D) src;
        Circle2D circle = (Circle2D) target;
        return Shape2DHelper.lineCircleIntersect(line, circle);
    }

    @Override
    public int srcType() {
        return ShapeType2D.LINE.code();
    }

    @Override
    public int targetType() {
        return ShapeType2D.CIRCLE.code();
    }
}
