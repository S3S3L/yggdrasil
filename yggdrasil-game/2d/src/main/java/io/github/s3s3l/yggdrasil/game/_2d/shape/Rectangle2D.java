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
public class Rectangle2D extends AbstractShape2D {

    private Vector2D topLeft;
    private float width;
    private float height;
    
    @Override
    public boolean contains(Vector2D point) {
        return point.getX() >= topLeft.getX() && point.getX() <= topLeft.getX() + width
                && point.getY() >= topLeft.getY() && point.getY() <= topLeft.getY() + height;
    }

    @Override
    public int type() {
        return ShapeType2D.RECTANGLE.code();
    }
    
}
