package io.github.s3s3l.yggdrasil.game._2d.shape.handler.impl;

import io.github.s3s3l.yggdrasil.game._2d.shape.Polygon2D;
import io.github.s3s3l.yggdrasil.game._2d.shape.Segment2D;
import io.github.s3s3l.yggdrasil.game._2d.shape.ShapeType2D;
import io.github.s3s3l.yggdrasil.game._2d.shape.handler.ShapeHandler2D;
import io.github.s3s3l.yggdrasil.game._2d.utils.Shape2DHelper;
import io.github.s3s3l.yggdrasil.game._2d.vector.Vector2D;
import io.github.s3s3l.yggdrasil.game.core.shape.Shape;

public class Polygon2DPolygon2DHandler implements ShapeHandler2D {
    @Override
    public boolean contains(Shape<Vector2D> src, Shape<Vector2D> target) {
        if (!(src instanceof Polygon2D) || !(target instanceof Polygon2D))
            return false;
        Polygon2D poly1 = (Polygon2D) src;
        Polygon2D poly2 = (Polygon2D) target;
        for (Vector2D v : poly2.getVertices()) {
            if (!poly1.contains(v))
                return false;
        }
        return true;
    }

    @Override
    public boolean intersects(Shape<Vector2D> src, Shape<Vector2D> target) {
        if (!(src instanceof Polygon2D) || !(target instanceof Polygon2D))
            return false;
        Polygon2D poly1 = (Polygon2D) src;
        Polygon2D poly2 = (Polygon2D) target;
        // 顶点包含
        for (Vector2D v : poly2.getVertices()) {
            if (poly1.contains(v))
                return true;
        }
        for (Vector2D v : poly1.getVertices()) {
            if (poly2.contains(v))
                return true;
        }
        // 边相交
        Vector2D[] vs1 = poly1.getVertices();
        Vector2D[] vs2 = poly2.getVertices();
        for (int i = 0; i < vs1.length; i++) {
            Vector2D a1 = vs1[i];
            Vector2D a2 = vs1[(i + 1) % vs1.length];
            for (int j = 0; j < vs2.length; j++) {
                Vector2D b1 = vs2[j];
                Vector2D b2 = vs2[(j + 1) % vs2.length];
                if (Shape2DHelper.segmentsIntersect(new Segment2D(a1, a2), new Segment2D(b1, b2)))
                    return true;
            }
        }
        return false;
    }

    @Override
    public int srcType() {
        return ShapeType2D.POLYGON.code();
    }

    @Override
    public int targetType() {
        return ShapeType2D.POLYGON.code();
    }
}
