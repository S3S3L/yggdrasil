package io.github.s3s3l.yggdrasil.game._2d.shape.handler.impl;

import io.github.s3s3l.yggdrasil.game._2d.shape.Polygon2D;
import io.github.s3s3l.yggdrasil.game._2d.shape.Ray2D;
import io.github.s3s3l.yggdrasil.game._2d.shape.ShapeType2D;
import io.github.s3s3l.yggdrasil.game._2d.shape.handler.ShapeHandler2D;
import io.github.s3s3l.yggdrasil.game._2d.utils.Shape2DHelper;
import io.github.s3s3l.yggdrasil.game._2d.vector.Vector2D;
import io.github.s3s3l.yggdrasil.game.core.shape.Shape;

public class Ray2DPolygon2DHandler implements ShapeHandler2D {
    @Override
    public boolean contains(Shape<Vector2D> src, Shape<Vector2D> target) {
        // 射线包含多边形：所有顶点都在射线上
        if (!(src instanceof Ray2D) || !(target instanceof Polygon2D))
            return false;
        Ray2D ray = (Ray2D) src;
        Polygon2D poly = (Polygon2D) target;
        for (Vector2D v : poly.getVertices()) {
            if (!ray.contains(v))
                return false;
        }
        return true;
    }

    @Override
    public boolean intersects(Shape<Vector2D> src, Shape<Vector2D> target) {
        if (!(src instanceof Ray2D) || !(target instanceof Polygon2D))
            return false;
        Ray2D ray = (Ray2D) src;
        Polygon2D poly = (Polygon2D) target;
        return Shape2DHelper.polygonRayIntersect(poly, ray);
    }

    @Override
    public int srcType() {
        return ShapeType2D.RAY.code();
    }

    @Override
    public int targetType() {
        return ShapeType2D.POLYGON.code();
    }
}
