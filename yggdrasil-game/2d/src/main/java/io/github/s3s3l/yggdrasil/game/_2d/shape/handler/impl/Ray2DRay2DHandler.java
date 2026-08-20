package io.github.s3s3l.yggdrasil.game._2d.shape.handler.impl;

import io.github.s3s3l.yggdrasil.game._2d.shape.Ray2D;
import io.github.s3s3l.yggdrasil.game._2d.shape.ShapeType2D;
import io.github.s3s3l.yggdrasil.game._2d.shape.handler.ShapeHandler2D;
import io.github.s3s3l.yggdrasil.game._2d.vector.Vector2D;
import io.github.s3s3l.yggdrasil.game.core.shape.Shape;

public class Ray2DRay2DHandler implements ShapeHandler2D {
    @Override
    public boolean contains(Shape<Vector2D> src, Shape<Vector2D> target) {
        // 射线无法包含射线
        return false;
    }
    @Override
    public boolean intersects(Shape<Vector2D> src, Shape<Vector2D> target) {
        if (!(src instanceof Ray2D) || !(target instanceof Ray2D)) return false;
        Ray2D r1 = (Ray2D) src;
        Ray2D r2 = (Ray2D) target;
        // 判断两射线是否共线且方向相同，或有交点
        return raysIntersect(r1.getOrigin(), r1.getDirection(), r2.getOrigin(), r2.getDirection());
    }
    private boolean raysIntersect(Vector2D o1, Vector2D d1, Vector2D o2, Vector2D d2) {
        float det = d1.getX() * (-d2.getY()) - d1.getY() * (-d2.getX());
        if (Math.abs(det) < 1e-8) return false;
        float t = ((o2.getX() - o1.getX()) * (-d2.getY()) - (o2.getY() - o1.getY()) * (-d2.getX())) / det;
        float s = (d1.getX() * (o2.getY() - o1.getY()) - d1.getY() * (o2.getX() - o1.getX())) / det;
        return t >= 0 && s >= 0;
    }
    @Override
    public int srcType() {
        return ShapeType2D.RAY.code();
    }
    @Override
    public int targetType() {
        return ShapeType2D.RAY.code();
    }
}
