package io.github.s3s3l.yggdrasil.game._2d.shape;

import io.github.s3s3l.yggdrasil.game._2d.vector.Vector2D;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class InfinitePlane2D extends AbstractShape2D {

    @Override
    public boolean contains(Vector2D point) {
        return true;
    }

    @Override
    public int type() {
        return ShapeType2D.INFINITE_PLANE.code();
    }
    
}
