package io.github.s3s3l.yggdrasil.game._2d.shape.handler.impl;

import io.github.s3s3l.yggdrasil.game._2d.shape.Circle2D;
import io.github.s3s3l.yggdrasil.game._2d.shape.Polygon2D;
import io.github.s3s3l.yggdrasil.game._2d.shape.ShapeType2D;
import io.github.s3s3l.yggdrasil.game._2d.shape.handler.ShapeHandler2D;
import io.github.s3s3l.yggdrasil.game._2d.utils.Shape2DHelper;
import io.github.s3s3l.yggdrasil.game._2d.vector.Vector2D;
import io.github.s3s3l.yggdrasil.game.core.shape.Shape;

public class Circle2DPolygon2DHandler implements ShapeHandler2D {
    @Override
    public boolean contains(Shape<Vector2D> src, Shape<Vector2D> target) {
        if (!(src instanceof Circle2D) || !(target instanceof Polygon2D))
            return false;
        Circle2D circle = (Circle2D) src;
        Polygon2D poly = (Polygon2D) target;
        for (Vector2D v : poly.getVertices()) {
            float dx = v.getX() - circle.getCenter().getX();
            float dy = v.getY() - circle.getCenter().getY();
            double dist = Math.sqrt(dx * dx + dy * dy);
            if (dist > circle.getRadius())
                return false;
        }
        return true;
    }

    @Override
    public boolean intersects(Shape<Vector2D> src, Shape<Vector2D> target) {
        if (!(src instanceof Circle2D) || !(target instanceof Polygon2D))
            return false;
        Circle2D circle = (Circle2D) src;
        Polygon2D poly = (Polygon2D) target;
        return Shape2DHelper.polygonCircleIntersect(poly, circle);
    }

    @Override
    public int srcType() {
        return ShapeType2D.CIRCLE.code();
    }

    @Override
    public int targetType() {
        return ShapeType2D.POLYGON.code();
    }
}
