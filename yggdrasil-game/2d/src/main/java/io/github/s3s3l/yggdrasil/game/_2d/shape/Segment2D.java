package io.github.s3s3l.yggdrasil.game._2d.shape;

import io.github.s3s3l.yggdrasil.game._2d.vector.Vector2D;
import lombok.Getter;

public class Segment2D extends AbstractShape2D {
    
    @Getter
    private final Vector2D start;
    @Getter
    private final Vector2D end;

    public Segment2D(Vector2D start, Vector2D end) {
        this.start = start;
        this.end = end;
    }

    @Override
    public boolean contains(Vector2D point) {
        // 判断点point是否在start和end组成的线段上
        // 1. 向量叉积为0（共线）
        // 2. 点在线段的包围盒内
        double cross = (point.getY() - start.getY()) * (end.getX() - start.getX()) -
                      (point.getX() - start.getX()) * (end.getY() - start.getY());
        if (Math.abs(cross) > 1e-10) {
            return false;
        }
        double minX = Math.min(start.getX(), end.getX());
        double maxX = Math.max(start.getX(), end.getX());
        double minY = Math.min(start.getY(), end.getY());
        double maxY = Math.max(start.getY(), end.getY());
        return point.getX() >= minX - 1e-10 && point.getX() <= maxX + 1e-10 &&
               point.getY() >= minY - 1e-10 && point.getY() <= maxY + 1e-10;
    }

    @Override
    public int type() {
        return ShapeType2D.SEGMENT.code();
    }
    
}
