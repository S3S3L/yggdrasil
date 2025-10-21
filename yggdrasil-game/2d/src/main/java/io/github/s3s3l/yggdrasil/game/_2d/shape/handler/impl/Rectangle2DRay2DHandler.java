package io.github.s3s3l.yggdrasil.game._2d.shape.handler.impl;

import io.github.s3s3l.yggdrasil.game._2d.shape.Ray2D;
import io.github.s3s3l.yggdrasil.game._2d.shape.Rectangle2D;
import io.github.s3s3l.yggdrasil.game._2d.shape.ShapeType2D;
import io.github.s3s3l.yggdrasil.game._2d.shape.handler.ShapeHandler2D;
import io.github.s3s3l.yggdrasil.game._2d.utils.Shape2DHelper;
import io.github.s3s3l.yggdrasil.game._2d.vector.Vector2D;
import io.github.s3s3l.yggdrasil.game.core.shape.Shape;

public class Rectangle2DRay2DHandler implements ShapeHandler2D {
    @Override
    public boolean contains(Shape<Vector2D> src, Shape<Vector2D> target) {
        // 矩形包含射线：射线起点和起点+方向都在矩形内
        if (!(src instanceof Rectangle2D) || !(target instanceof Ray2D))
            return false;
        Rectangle2D rect = (Rectangle2D) src;
        Ray2D ray = (Ray2D) target;
        Vector2D o = ray.getOrigin();
        Vector2D d = ray.getDirection();
        Vector2D p2 = new Vector2D(o.getX() + d.getX(), o.getY() + d.getY());
        return rect.contains(o) && rect.contains(p2);
    }

    @Override
    public boolean intersects(Shape<Vector2D> src, Shape<Vector2D> target) {
        if (!(src instanceof Rectangle2D) || !(target instanceof Ray2D))
            return false;
        Rectangle2D rect = (Rectangle2D) src;
        Ray2D ray = (Ray2D) target;
        return Shape2DHelper.rayRectangleIntersect(ray, rect);
    }

    @Override
    public int srcType() {
        return ShapeType2D.RECTANGLE.code();
    }

    @Override
    public int targetType() {
        return ShapeType2D.RAY.code();
    }
}
