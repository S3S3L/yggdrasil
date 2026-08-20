package io.github.s3s3l.yggdrasil.game._2d.shape.handler.impl;

import io.github.s3s3l.yggdrasil.game._2d.shape.Line2D;
import io.github.s3s3l.yggdrasil.game._2d.shape.Polygon2D;
import io.github.s3s3l.yggdrasil.game._2d.shape.ShapeType2D;
import io.github.s3s3l.yggdrasil.game._2d.shape.handler.ShapeHandler2D;
import io.github.s3s3l.yggdrasil.game._2d.utils.Shape2DHelper;
import io.github.s3s3l.yggdrasil.game._2d.vector.Vector2D;
import io.github.s3s3l.yggdrasil.game.core.shape.Shape;

public class Polygon2DLine2DHandler implements ShapeHandler2D {
    @Override
    public boolean contains(Shape<Vector2D> src, Shape<Vector2D> target) {
        // 多边形包含直线：直线的任意两点都在多边形内（近似）
        if (!(src instanceof Polygon2D) || !(target instanceof Line2D))
            return false;
        Polygon2D poly = (Polygon2D) src;
        Line2D line = (Line2D) target;
        Vector2D p = line.getPoint();
        Vector2D d = line.getDirection();
        Vector2D p2 = new Vector2D(p.getX() + d.getX(), p.getY() + d.getY());
        return poly.contains(p) && poly.contains(p2);
    }

    @Override
    public boolean intersects(Shape<Vector2D> src, Shape<Vector2D> target) {
        if (!(src instanceof Polygon2D) || !(target instanceof Line2D))
            return false;
        Polygon2D poly = (Polygon2D) src;
        Line2D line = (Line2D) target;
        return Shape2DHelper.polygonLineIntersect(poly, line);
    }

    @Override
    public int srcType() {
        return ShapeType2D.POLYGON.code();
    }

    @Override
    public int targetType() {
        return ShapeType2D.LINE.code();
    }
}
