package io.github.s3s3l.yggdrasil.game._2d.shape.handler.impl;

import io.github.s3s3l.yggdrasil.game._2d.shape.Line2D;
import io.github.s3s3l.yggdrasil.game._2d.shape.ShapeType2D;
import io.github.s3s3l.yggdrasil.game._2d.shape.handler.ShapeHandler2D;
import io.github.s3s3l.yggdrasil.game._2d.utils.Shape2DHelper;
import io.github.s3s3l.yggdrasil.game._2d.vector.Vector2D;
import io.github.s3s3l.yggdrasil.game.core.shape.Shape;

public class Line2DLine2DHandler implements ShapeHandler2D {
    @Override
    public boolean contains(Shape<Vector2D> src, Shape<Vector2D> target) {
        // 直线无法包含直线
        return false;
    }

    @Override
    public boolean intersects(Shape<Vector2D> src, Shape<Vector2D> target) {
        if (!(src instanceof Line2D) || !(target instanceof Line2D))
            return false;
        Line2D l1 = (Line2D) src;
        Line2D l2 = (Line2D) target;
        return Shape2DHelper.linesIntersect(l1, l2);
    }

    @Override
    public int srcType() {
        return ShapeType2D.LINE.code();
    }

    @Override
    public int targetType() {
        return ShapeType2D.LINE.code();
    }
}
