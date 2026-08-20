package io.github.s3s3l.yggdrasil.game._2d.shape;

import io.github.s3s3l.yggdrasil.game._2d.vector.Vector2D;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Line2D extends AbstractShape2D {
    
    private Vector2D point;
    private Vector2D direction;

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
