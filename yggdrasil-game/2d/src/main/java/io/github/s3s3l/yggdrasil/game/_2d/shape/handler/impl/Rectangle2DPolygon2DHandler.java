package io.github.s3s3l.yggdrasil.game._2d.shape.handler.impl;

import io.github.s3s3l.yggdrasil.game._2d.shape.Polygon2D;
import io.github.s3s3l.yggdrasil.game._2d.shape.Rectangle2D;
import io.github.s3s3l.yggdrasil.game._2d.shape.ShapeType2D;
import io.github.s3s3l.yggdrasil.game._2d.shape.handler.ShapeHandler2D;
import io.github.s3s3l.yggdrasil.game._2d.utils.Shape2DHelper;
import io.github.s3s3l.yggdrasil.game._2d.vector.Vector2D;
import io.github.s3s3l.yggdrasil.game.core.shape.Shape;

public class Rectangle2DPolygon2DHandler implements ShapeHandler2D {
    @Override
    public boolean contains(Shape<Vector2D> src, Shape<Vector2D> target) {
        // 矩形包含多边形：所有顶点都在矩形内
        if (!(src instanceof Rectangle2D) || !(target instanceof Polygon2D))
            return false;
        Rectangle2D rect = (Rectangle2D) src;
        Polygon2D poly = (Polygon2D) target;
        for (Vector2D v : poly.getVertices()) {
            if (!rect.contains(v))
                return false;
        }
        return true;
    }

    @Override
    public boolean intersects(Shape<Vector2D> src, Shape<Vector2D> target) {
        if (!(src instanceof Rectangle2D) || !(target instanceof Polygon2D))
            return false;
        Rectangle2D rect = (Rectangle2D) src;
        Polygon2D poly = (Polygon2D) target;
        return Shape2DHelper.polygonRectangleIntersect(poly, rect);
    }

    @Override
    public int srcType() {
        return ShapeType2D.RECTANGLE.code();
    }

    @Override
    public int targetType() {
        return ShapeType2D.POLYGON.code();
    }
}
