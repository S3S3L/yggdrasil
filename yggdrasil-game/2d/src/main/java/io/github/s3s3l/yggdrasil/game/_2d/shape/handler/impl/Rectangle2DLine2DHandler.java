package io.github.s3s3l.yggdrasil.game._2d.shape.handler.impl;

import io.github.s3s3l.yggdrasil.game._2d.shape.Line2D;
import io.github.s3s3l.yggdrasil.game._2d.shape.Rectangle2D;
import io.github.s3s3l.yggdrasil.game._2d.shape.ShapeType2D;
import io.github.s3s3l.yggdrasil.game._2d.shape.handler.ShapeHandler2D;
import io.github.s3s3l.yggdrasil.game._2d.utils.Shape2DHelper;
import io.github.s3s3l.yggdrasil.game._2d.vector.Vector2D;
import io.github.s3s3l.yggdrasil.game.core.shape.Shape;

public class Rectangle2DLine2DHandler implements ShapeHandler2D {
    @Override
    public boolean contains(Shape<Vector2D> src, Shape<Vector2D> target) {
        // 矩形包含直线：直线的任意两点都在矩形内（理论上直线无限长，实际一般不成立）
        // 这里约定：直线的基点和基点+d方向都在矩形内
        if (!(src instanceof Rectangle2D) || !(target instanceof Line2D))
            return false;
        Rectangle2D rect = (Rectangle2D) src;
        Line2D line = (Line2D) target;
        Vector2D p = line.getPoint();
        Vector2D d = line.getDirection();
        Vector2D p2 = new Vector2D(p.getX() + d.getX(), p.getY() + d.getY());
        return rect.contains(p) && rect.contains(p2);
    }

    @Override
    public boolean intersects(Shape<Vector2D> src, Shape<Vector2D> target) {
        if (!(src instanceof Rectangle2D) || !(target instanceof Line2D))
            return false;
        Rectangle2D rect = (Rectangle2D) src;
        Line2D line = (Line2D) target;
        return Shape2DHelper.lineRectangleIntersect(line, rect);
    }

    @Override
    public int srcType() {
        return ShapeType2D.RECTANGLE.code();
    }

    @Override
    public int targetType() {
        return ShapeType2D.LINE.code();
    }
}
