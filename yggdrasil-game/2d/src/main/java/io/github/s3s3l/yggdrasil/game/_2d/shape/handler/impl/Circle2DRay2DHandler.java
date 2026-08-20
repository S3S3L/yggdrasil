package io.github.s3s3l.yggdrasil.game._2d.shape.handler.impl;

import io.github.s3s3l.yggdrasil.game._2d.shape.Circle2D;
import io.github.s3s3l.yggdrasil.game._2d.shape.Ray2D;
import io.github.s3s3l.yggdrasil.game._2d.shape.ShapeType2D;
import io.github.s3s3l.yggdrasil.game._2d.shape.handler.ShapeHandler2D;
import io.github.s3s3l.yggdrasil.game._2d.utils.Shape2DHelper;
import io.github.s3s3l.yggdrasil.game._2d.vector.Vector2D;
import io.github.s3s3l.yggdrasil.game.core.shape.Shape;

public class Circle2DRay2DHandler implements ShapeHandler2D {
    @Override
    public boolean contains(Shape<Vector2D> src, Shape<Vector2D> target) {
        // 圆无法完全包含射线
        return false;
    }

    @Override
    public boolean intersects(Shape<Vector2D> src, Shape<Vector2D> target) {
        if (!(src instanceof Circle2D) || !(target instanceof Ray2D))
            return false;
        Circle2D circle = (Circle2D) src;
        Ray2D ray = (Ray2D) target;
        return Shape2DHelper.rayCircleIntersect(ray, circle);
    }

    @Override
    public int srcType() {
        return ShapeType2D.CIRCLE.code();
    }

    @Override
    public int targetType() {
        return ShapeType2D.RAY.code();
    }
}
