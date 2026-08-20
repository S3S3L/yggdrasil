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
public class Circle2D extends AbstractShape2D {

    /**
     * 圆心
     */
    private Vector2D center;
    /**
     * 半径
     */
    private float radius;

    @Override
    public boolean contains(Vector2D point) {
        double dx = point.getX() - center.getX();
        double dy = point.getY() - center.getY();
        return dx * dx + dy * dy <= radius * radius;
    }

    @Override
    public int type() {
        return ShapeType2D.CIRCLE.code();
    }
    
}
