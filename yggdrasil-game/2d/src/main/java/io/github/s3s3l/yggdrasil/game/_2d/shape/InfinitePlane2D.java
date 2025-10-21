package io.github.s3s3l.yggdrasil.game._2d.shape;

import io.github.s3s3l.yggdrasil.game._2d.vector.Vector2D;

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
