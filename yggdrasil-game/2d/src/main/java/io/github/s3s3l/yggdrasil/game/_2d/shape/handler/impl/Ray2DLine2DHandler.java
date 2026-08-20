package io.github.s3s3l.yggdrasil.game._2d.shape.handler.impl;

import io.github.s3s3l.yggdrasil.game._2d.shape.Line2D;
import io.github.s3s3l.yggdrasil.game._2d.shape.Ray2D;
import io.github.s3s3l.yggdrasil.game._2d.shape.ShapeType2D;
import io.github.s3s3l.yggdrasil.game._2d.shape.handler.ShapeHandler2D;
import io.github.s3s3l.yggdrasil.game._2d.utils.Shape2DHelper;
import io.github.s3s3l.yggdrasil.game._2d.vector.Vector2D;
import io.github.s3s3l.yggdrasil.game.core.shape.Shape;

public class Ray2DLine2DHandler implements ShapeHandler2D {
    @Override
    public boolean contains(Shape<Vector2D> src, Shape<Vector2D> target) {
        // 射线无法包含直线
        return false;
    }

    @Override
    public boolean intersects(Shape<Vector2D> src, Shape<Vector2D> target) {
        if (!(src instanceof Ray2D) || !(target instanceof Line2D))
            return false;
        Ray2D ray = (Ray2D) src;
        Line2D line = (Line2D) target;
        // 复用 Shape2dHelper 的 lineRayIntersect，参数顺序与 Line2DRay2DHandler 相反
        return Shape2DHelper.lineRayIntersect(line, ray);
    }

    @Override
    public int srcType() {
        return ShapeType2D.RAY.code();
    }

    @Override
    public int targetType() {
        return ShapeType2D.LINE.code();
    }
}
