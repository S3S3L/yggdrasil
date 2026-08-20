package io.github.s3s3l.yggdrasil.game._2d.shape.handler.impl;

import io.github.s3s3l.yggdrasil.game._2d.shape.Polygon2D;
import io.github.s3s3l.yggdrasil.game._2d.shape.Ray2D;
import io.github.s3s3l.yggdrasil.game._2d.shape.ShapeType2D;
import io.github.s3s3l.yggdrasil.game._2d.shape.handler.ShapeHandler2D;
import io.github.s3s3l.yggdrasil.game._2d.utils.Shape2DHelper;
import io.github.s3s3l.yggdrasil.game._2d.vector.Vector2D;
import io.github.s3s3l.yggdrasil.game.core.shape.Shape;

public class Polygon2DRay2DHandler implements ShapeHandler2D {
    @Override
    public boolean contains(Shape<Vector2D> src, Shape<Vector2D> target) {
        // 多边形包含射线：射线起点和起点+方向都在多边形内
        if (!(src instanceof Polygon2D) || !(target instanceof Ray2D))
            return false;
        Polygon2D poly = (Polygon2D) src;
        Ray2D ray = (Ray2D) target;
        Vector2D o = ray.getOrigin();
        Vector2D d = ray.getDirection();
        Vector2D p2 = new Vector2D(o.getX() + d.getX(), o.getY() + d.getY());
        return poly.contains(o) && poly.contains(p2);
    }

    @Override
    public boolean intersects(Shape<Vector2D> src, Shape<Vector2D> target) {
        if (!(src instanceof Polygon2D) || !(target instanceof Ray2D))
            return false;
        Polygon2D poly = (Polygon2D) src;
        Ray2D ray = (Ray2D) target;
        return Shape2DHelper.polygonRayIntersect(poly, ray);
    }

    @Override
    public int srcType() {
        return ShapeType2D.POLYGON.code();
    }

    @Override
    public int targetType() {
        return ShapeType2D.RAY.code();
    }
}
