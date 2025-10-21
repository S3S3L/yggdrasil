package io.github.s3s3l.yggdrasil.game._2d.shape.handler.impl;

import io.github.s3s3l.yggdrasil.game._2d.shape.Line2D;
import io.github.s3s3l.yggdrasil.game._2d.shape.Polygon2D;
import io.github.s3s3l.yggdrasil.game._2d.shape.ShapeType2D;
import io.github.s3s3l.yggdrasil.game._2d.shape.handler.ShapeHandler2D;
import io.github.s3s3l.yggdrasil.game._2d.utils.Shape2DHelper;
import io.github.s3s3l.yggdrasil.game._2d.vector.Vector2D;
import io.github.s3s3l.yggdrasil.game.core.shape.Shape;

public class Line2DPolygon2DHandler implements ShapeHandler2D {
    @Override
    public boolean contains(Shape<Vector2D> src, Shape<Vector2D> target) {
        // 直线包含多边形：多边形所有顶点都在直线上
        if (!(src instanceof Line2D) || !(target instanceof Polygon2D))
            return false;
        Line2D line = (Line2D) src;
        Polygon2D poly = (Polygon2D) target;
        for (Vector2D v : poly.getVertices()) {
            if (!line.contains(v))
                return false;
        }
        return true;
    }

    @Override
    public boolean intersects(Shape<Vector2D> src, Shape<Vector2D> target) {
        if (!(src instanceof Line2D) || !(target instanceof Polygon2D))
            return false;
        Line2D line = (Line2D) src;
        Polygon2D poly = (Polygon2D) target;
        return Shape2DHelper.polygonLineIntersect(poly, line);
    }

    @Override
    public int srcType() {
        return ShapeType2D.LINE.code();
    }

    @Override
    public int targetType() {
        return ShapeType2D.POLYGON.code();
    }
}
