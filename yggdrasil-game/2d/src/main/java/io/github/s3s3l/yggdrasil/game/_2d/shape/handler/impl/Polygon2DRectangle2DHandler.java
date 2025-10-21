package io.github.s3s3l.yggdrasil.game._2d.shape.handler.impl;

import io.github.s3s3l.yggdrasil.game._2d.shape.Polygon2D;
import io.github.s3s3l.yggdrasil.game._2d.shape.Rectangle2D;
import io.github.s3s3l.yggdrasil.game._2d.shape.ShapeType2D;
import io.github.s3s3l.yggdrasil.game._2d.shape.handler.ShapeHandler2D;
import io.github.s3s3l.yggdrasil.game._2d.utils.Shape2DHelper;
import io.github.s3s3l.yggdrasil.game._2d.vector.Vector2D;
import io.github.s3s3l.yggdrasil.game.core.shape.Shape;

public class Polygon2DRectangle2DHandler implements ShapeHandler2D {
    @Override
    public boolean contains(Shape<Vector2D> src, Shape<Vector2D> target) {
        // 多边形包含矩形：矩形四个顶点都在多边形内
        if (!(src instanceof Polygon2D) || !(target instanceof Rectangle2D))
            return false;
        Polygon2D poly = (Polygon2D) src;
        Rectangle2D rect = (Rectangle2D) target;
        Vector2D tl = rect.getTopLeft();
        float w = rect.getWidth();
        float h = rect.getHeight();
        Vector2D[] corners = new Vector2D[] {
                tl,
                new Vector2D(tl.getX() + w, tl.getY()),
                new Vector2D(tl.getX() + w, tl.getY() + h),
                new Vector2D(tl.getX(), tl.getY() + h)
        };
        for (Vector2D v : corners) {
            if (!poly.contains(v))
                return false;
        }
        return true;
    }

    @Override
    public boolean intersects(Shape<Vector2D> src, Shape<Vector2D> target) {
        if (!(src instanceof Polygon2D) || !(target instanceof Rectangle2D))
            return false;
        Polygon2D poly = (Polygon2D) src;
        Rectangle2D rect = (Rectangle2D) target;
        return Shape2DHelper.polygonRectangleIntersect(poly, rect);
    }

    @Override
    public int srcType() {
        return ShapeType2D.POLYGON.code();
    }

    @Override
    public int targetType() {
        return ShapeType2D.RECTANGLE.code();
    }
}
