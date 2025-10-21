package io.github.s3s3l.yggdrasil.game._2d.shape.handler.impl;

import io.github.s3s3l.yggdrasil.game._2d.shape.Circle2D;
import io.github.s3s3l.yggdrasil.game._2d.shape.Polygon2D;
import io.github.s3s3l.yggdrasil.game._2d.shape.ShapeType2D;
import io.github.s3s3l.yggdrasil.game._2d.shape.handler.ShapeHandler2D;
import io.github.s3s3l.yggdrasil.game._2d.utils.Shape2DHelper;
import io.github.s3s3l.yggdrasil.game._2d.vector.Vector2D;
import io.github.s3s3l.yggdrasil.game.core.shape.Shape;

public class Polygon2DCircle2DHandler implements ShapeHandler2D {
    @Override
    public boolean contains(Shape<Vector2D> src, Shape<Vector2D> target) {
        if (!(src instanceof Polygon2D) || !(target instanceof Circle2D)) return false;
        Polygon2D poly = (Polygon2D) src;
        Circle2D circle = (Circle2D) target;
        // 检查圆心和圆上四个象限点是否都在多边形内
        float cx = circle.getCenter().getX();
        float cy = circle.getCenter().getY();
        float r = circle.getRadius();
        Vector2D[] testPoints = new Vector2D[] {
            new Vector2D(cx, cy),
            new Vector2D(cx + r, cy),
            new Vector2D(cx - r, cy),
            new Vector2D(cx, cy + r),
            new Vector2D(cx, cy - r)
        };
        for (Vector2D p : testPoints) {
            if (!poly.contains(p)) return false;
        }
        return true;
    }

    @Override
    public boolean intersects(Shape<Vector2D> src, Shape<Vector2D> target) {
        if (!(src instanceof Polygon2D) || !(target instanceof Circle2D)) return false;
        Polygon2D poly = (Polygon2D) src;
        Circle2D circle = (Circle2D) target;
        return Shape2DHelper.polygonCircleIntersect(poly, circle);
    }



    @Override
    public int srcType() {
        return ShapeType2D.POLYGON.code();
    }

    @Override
    public int targetType() {
        return ShapeType2D.CIRCLE.code();
    }
}
