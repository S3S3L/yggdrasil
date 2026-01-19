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
public class Ray2D extends AbstractShape2D {
    private Vector2D origin;
    private Vector2D direction;

    @Override
    public boolean contains(Vector2D point) {
        // 判断点point是否在射线origin和direction组成的射线上
        // 1. 向量叉积为0（共线）
        // 2. 点在origin的方向上
        double cross = (point.getY() - origin.getY()) * direction.getX() -
                      (point.getX() - origin.getX()) * direction.getY();
        if (Math.abs(cross) > 1e-10) {
            return false;
        }
        double dot = (point.getX() - origin.getX()) * direction.getX() +
                     (point.getY() - origin.getY()) * direction.getY();
        return dot >= -1e-10;
    }

    @Override
    public int type() {
        return ShapeType2D.RAY.code();
    }
    
}
