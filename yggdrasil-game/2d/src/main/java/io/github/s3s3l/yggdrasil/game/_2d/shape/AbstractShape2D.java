package io.github.s3s3l.yggdrasil.game._2d.shape;

import io.github.s3s3l.yggdrasil.game._2d.shape.handler.ShapeHandlerManager2D;
import io.github.s3s3l.yggdrasil.game._2d.vector.Vector2D;
import io.github.s3s3l.yggdrasil.game.core.shape.Shape;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public abstract class AbstractShape2D implements Shape2D {

    protected int zIndex;

    @Override
    public boolean contains(Shape<Vector2D> other) {
        return ShapeHandlerManager2D.get().contains(this, other);
    }

    @Override
    public boolean intersects(Shape<Vector2D> other) {
        return ShapeHandlerManager2D.get().intersects(this, other);
    }
}
