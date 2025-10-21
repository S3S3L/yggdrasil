package io.github.s3s3l.yggdrasil.game._2d.shape.handler.impl;

import io.github.s3s3l.yggdrasil.game._2d.shape.Circle2D;
import io.github.s3s3l.yggdrasil.game._2d.shape.ShapeType2D;
import io.github.s3s3l.yggdrasil.game._2d.shape.handler.ShapeHandler2D;
import io.github.s3s3l.yggdrasil.game._2d.vector.Vector2D;
import io.github.s3s3l.yggdrasil.game.core.shape.Shape;

public class Circle2DCircle2DHandler implements ShapeHandler2D {
    @Override
    public boolean contains(Shape<Vector2D> src, Shape<Vector2D> target) {
        if (!(src instanceof Circle2D) || !(target instanceof Circle2D))
            return false;
        Circle2D c1 = (Circle2D) src;
        Circle2D c2 = (Circle2D) target;
        float dx = c1.getCenter().getX() - c2.getCenter().getX();
        float dy = c1.getCenter().getY() - c2.getCenter().getY();
        double dist = Math.sqrt(dx * dx + dy * dy);
        return dist + c2.getRadius() <= c1.getRadius();
    }

    @Override
    public boolean intersects(Shape<Vector2D> src, Shape<Vector2D> target) {
        if (!(src instanceof Circle2D) || !(target instanceof Circle2D))
            return false;
        Circle2D c1 = (Circle2D) src;
        Circle2D c2 = (Circle2D) target;
        float dx = c1.getCenter().getX() - c2.getCenter().getX();
        float dy = c1.getCenter().getY() - c2.getCenter().getY();
        double dist = Math.sqrt(dx * dx + dy * dy);
        return dist <= c1.getRadius() + c2.getRadius();
    }

    @Override
    public int srcType() {
        return ShapeType2D.CIRCLE.code();
    }

    @Override
    public int targetType() {
        return ShapeType2D.CIRCLE.code();
    }
}
