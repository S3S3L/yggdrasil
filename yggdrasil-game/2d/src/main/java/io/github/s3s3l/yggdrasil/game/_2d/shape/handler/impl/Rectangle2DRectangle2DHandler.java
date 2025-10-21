package io.github.s3s3l.yggdrasil.game._2d.shape.handler.impl;

import io.github.s3s3l.yggdrasil.game._2d.shape.Rectangle2D;
import io.github.s3s3l.yggdrasil.game._2d.shape.ShapeType2D;
import io.github.s3s3l.yggdrasil.game._2d.shape.handler.ShapeHandler2D;
import io.github.s3s3l.yggdrasil.game._2d.vector.Vector2D;
import io.github.s3s3l.yggdrasil.game.core.shape.Shape;

public class Rectangle2DRectangle2DHandler implements ShapeHandler2D {
    @Override
    public boolean contains(Shape<Vector2D> src, Shape<Vector2D> target) {
        if (!(src instanceof Rectangle2D) || !(target instanceof Rectangle2D))
            return false;
        Rectangle2D r1 = (Rectangle2D) src;
        Rectangle2D r2 = (Rectangle2D) target;
        float x1 = r1.getTopLeft().getX();
        float y1 = r1.getTopLeft().getY();
        float w1 = r1.getWidth();
        float h1 = r1.getHeight();
        float x2 = r2.getTopLeft().getX();
        float y2 = r2.getTopLeft().getY();
        float w2 = r2.getWidth();
        float h2 = r2.getHeight();
        return x2 >= x1 && y2 >= y1 && x2 + w2 <= x1 + w1 && y2 + h2 <= y1 + h1;
    }

    @Override
    public boolean intersects(Shape<Vector2D> src, Shape<Vector2D> target) {
        if (!(src instanceof Rectangle2D) || !(target instanceof Rectangle2D))
            return false;
        Rectangle2D r1 = (Rectangle2D) src;
        Rectangle2D r2 = (Rectangle2D) target;
        float x1 = r1.getTopLeft().getX();
        float y1 = r1.getTopLeft().getY();
        float w1 = r1.getWidth();
        float h1 = r1.getHeight();
        float x2 = r2.getTopLeft().getX();
        float y2 = r2.getTopLeft().getY();
        float w2 = r2.getWidth();
        float h2 = r2.getHeight();
        return x1 < x2 + w2 && x1 + w1 > x2 && y1 < y2 + h2 && y1 + h1 > y2;
    }

    @Override
    public int srcType() {
        return ShapeType2D.RECTANGLE.code();
    }

    @Override
    public int targetType() {
        return ShapeType2D.RECTANGLE.code();
    }
}
