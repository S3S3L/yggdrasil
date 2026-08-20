package io.github.s3s3l.yggdrasil.game._2d.shape.handler.impl;

import io.github.s3s3l.yggdrasil.game._2d.shape.Circle2D;
import io.github.s3s3l.yggdrasil.game._2d.shape.Rectangle2D;
import io.github.s3s3l.yggdrasil.game._2d.shape.ShapeType2D;
import io.github.s3s3l.yggdrasil.game._2d.shape.handler.ShapeHandler2D;
import io.github.s3s3l.yggdrasil.game._2d.utils.Shape2DHelper;
import io.github.s3s3l.yggdrasil.game._2d.vector.Vector2D;
import io.github.s3s3l.yggdrasil.game.core.shape.Shape;

public class Rectangle2DCircle2DHandler implements ShapeHandler2D {
    @Override
    public boolean contains(Shape<Vector2D> src, Shape<Vector2D> target) {
        if (!(src instanceof Rectangle2D) || !(target instanceof Circle2D))
            return false;
        Rectangle2D rect = (Rectangle2D) src;
        Circle2D circle = (Circle2D) target;
        float x = rect.getTopLeft().getX();
        float y = rect.getTopLeft().getY();
        float w = rect.getWidth();
        float h = rect.getHeight();
        float cx = circle.getCenter().getX();
        float cy = circle.getCenter().getY();
        float r = circle.getRadius();
        // 圆心到矩形四边距离都大于等于半径
        if (cx - r < x || cx + r > x + w || cy - r < y || cy + r > y + h)
            return false;
        return true;
    }

    @Override
    public boolean intersects(Shape<Vector2D> src, Shape<Vector2D> target) {
        if (!(src instanceof Rectangle2D) || !(target instanceof Circle2D))
            return false;
        Rectangle2D rect = (Rectangle2D) src;
        Circle2D circle = (Circle2D) target;
        return Shape2DHelper.circleRectangleIntersect(circle, rect);
    }

    @Override
    public int srcType() {
        return ShapeType2D.RECTANGLE.code();
    }

    @Override
    public int targetType() {
        return ShapeType2D.CIRCLE.code();
    }
}
