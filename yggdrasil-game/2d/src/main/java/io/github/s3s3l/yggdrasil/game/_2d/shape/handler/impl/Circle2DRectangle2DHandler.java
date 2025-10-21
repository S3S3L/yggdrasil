package io.github.s3s3l.yggdrasil.game._2d.shape.handler.impl;

import io.github.s3s3l.yggdrasil.game._2d.shape.Circle2D;
import io.github.s3s3l.yggdrasil.game._2d.shape.Rectangle2D;
import io.github.s3s3l.yggdrasil.game._2d.shape.ShapeType2D;
import io.github.s3s3l.yggdrasil.game._2d.shape.handler.ShapeHandler2D;
import io.github.s3s3l.yggdrasil.game._2d.vector.Vector2D;
import io.github.s3s3l.yggdrasil.game.core.shape.Shape;

public class Circle2DRectangle2DHandler implements ShapeHandler2D {
    @Override
    public boolean contains(Shape<Vector2D> src, Shape<Vector2D> target) {
        if (!(src instanceof Circle2D) || !(target instanceof Rectangle2D))
            return false;
        Circle2D circle = (Circle2D) src;
        Rectangle2D rect = (Rectangle2D) target;
        // 检查矩形四个顶点是否都在圆内
        float x = rect.getTopLeft().getX();
        float y = rect.getTopLeft().getY();
        float w = rect.getWidth();
        float h = rect.getHeight();
        Vector2D[] points = new Vector2D[] {
                new Vector2D(x, y),
                new Vector2D(x + w, y),
                new Vector2D(x, y + h),
                new Vector2D(x + w, y + h)
        };
        for (Vector2D p : points) {
            float dx = p.getX() - circle.getCenter().getX();
            float dy = p.getY() - circle.getCenter().getY();
            double dist = Math.sqrt(dx * dx + dy * dy);
            if (dist > circle.getRadius())
                return false;
        }
        return true;
    }

    @Override
    public boolean intersects(Shape<Vector2D> src, Shape<Vector2D> target) {
        if (!(src instanceof Circle2D) || !(target instanceof Rectangle2D))
            return false;
        Circle2D circle = (Circle2D) src;
        Rectangle2D rect = (Rectangle2D) target;
        return io.github.s3s3l.yggdrasil.game._2d.utils.Shape2DHelper.circleRectangleIntersect(circle, rect);
    }

    @Override
    public int srcType() {
        return ShapeType2D.CIRCLE.code();
    }

    @Override
    public int targetType() {
        return ShapeType2D.RECTANGLE.code();
    }
}
