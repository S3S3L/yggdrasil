package io.github.s3s3l.yggdrasil.game._2d.shape.handler.impl;

import io.github.s3s3l.yggdrasil.game._2d.shape.Line2D;
import io.github.s3s3l.yggdrasil.game._2d.shape.Ray2D;
import io.github.s3s3l.yggdrasil.game._2d.shape.ShapeType2D;
import io.github.s3s3l.yggdrasil.game._2d.shape.handler.ShapeHandler2D;
import io.github.s3s3l.yggdrasil.game._2d.utils.Shape2DHelper;
import io.github.s3s3l.yggdrasil.game._2d.vector.Vector2D;
import io.github.s3s3l.yggdrasil.game.core.shape.Shape;

public class Line2DRay2DHandler implements ShapeHandler2D {
    @Override
    public boolean contains(Shape<Vector2D> src, Shape<Vector2D> target) {
        // 直线包含射线：射线的起点在直线上，且方向共线
        if (!(src instanceof Line2D) || !(target instanceof Ray2D))
            return false;
        Line2D line = (Line2D) src;
        Ray2D ray = (Ray2D) target;
        // 射线起点在直线上，且射线方向与直线方向共线
        boolean onLine = line.contains(ray.getOrigin());
        // 判断方向是否共线
        float cross = ray.getDirection().getX() * line.getDirection().getY()
                - ray.getDirection().getY() * line.getDirection().getX();
        boolean colinear = Math.abs(cross) < 1e-8;
        return onLine && colinear;
    }

    @Override
    public boolean intersects(Shape<Vector2D> src, Shape<Vector2D> target) {
        if (!(src instanceof Line2D) || !(target instanceof Ray2D))
            return false;
        Line2D line = (Line2D) src;
        Ray2D ray = (Ray2D) target;
        return Shape2DHelper.lineRayIntersect(line, ray);
    }

    @Override
    public int srcType() {
        return ShapeType2D.LINE.code();
    }

    @Override
    public int targetType() {
        return ShapeType2D.RAY.code();
    }
}
