package io.github.s3s3l.yggdrasil.game._2d.shape;

import io.github.s3s3l.yggdrasil.game._2d.vector.Vector2D;
import lombok.Getter;

public class Line2D extends AbstractShape2D {
    
    @Getter
    private final Vector2D point;
    @Getter
    private final Vector2D direction;

    public Line2D(Vector2D point, Vector2D direction) {
        this.point = point;
        this.direction = direction;
    }

    @Override
    public boolean contains(Vector2D point) {
        // 判断点point是否在直线point和direction组成的直线上
        // 1. 向量叉积为0（共线）
        double cross = (point.getY() - this.point.getY()) * direction.getX() -
                      (point.getX() - this.point.getX()) * direction.getY();
        return Math.abs(cross) <= 1e-10;
    }

    @Override
    public int type() {
        return ShapeType2D.LINE.code();
    }
    
}
