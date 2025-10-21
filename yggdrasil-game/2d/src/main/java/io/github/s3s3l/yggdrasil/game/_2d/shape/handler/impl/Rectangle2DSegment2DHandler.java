package io.github.s3s3l.yggdrasil.game._2d.shape.handler.impl;

import io.github.s3s3l.yggdrasil.game._2d.shape.Rectangle2D;
import io.github.s3s3l.yggdrasil.game._2d.shape.Segment2D;
import io.github.s3s3l.yggdrasil.game._2d.shape.ShapeType2D;
import io.github.s3s3l.yggdrasil.game._2d.shape.handler.ShapeHandler2D;
import io.github.s3s3l.yggdrasil.game._2d.utils.Shape2DHelper;
import io.github.s3s3l.yggdrasil.game._2d.vector.Vector2D;
import io.github.s3s3l.yggdrasil.game.core.shape.Shape;

public class Rectangle2DSegment2DHandler implements ShapeHandler2D {
    @Override
    public boolean contains(Shape<Vector2D> src, Shape<Vector2D> target) {
        if (!(src instanceof Rectangle2D) || !(target instanceof Segment2D))
            return false;
        Rectangle2D rect = (Rectangle2D) src;
        Segment2D seg = (Segment2D) target;
        float x = rect.getTopLeft().getX();
        float y = rect.getTopLeft().getY();
        float w = rect.getWidth();
        float h = rect.getHeight();
        // 线段两端都在矩形内
        return seg.getStart().getX() >= x && seg.getStart().getX() <= x + w && seg.getStart().getY() >= y
                && seg.getStart().getY() <= y + h &&
                seg.getEnd().getX() >= x && seg.getEnd().getX() <= x + w && seg.getEnd().getY() >= y
                && seg.getEnd().getY() <= y + h;
    }

    @Override
    public boolean intersects(Shape<Vector2D> src, Shape<Vector2D> target) {
        if (!(src instanceof Rectangle2D) || !(target instanceof Segment2D))
            return false;
        Rectangle2D rect = (Rectangle2D) src;
        Segment2D seg = (Segment2D) target;
        return Shape2DHelper.segmentRectangleIntersect(seg, rect);
    }

    @Override
    public int srcType() {
        return ShapeType2D.RECTANGLE.code();
    }

    @Override
    public int targetType() {
        return ShapeType2D.SEGMENT.code();
    }
}
