package io.github.s3s3l.yggdrasil.game._2d.shape;

import io.github.s3s3l.yggdrasil.game._2d.vector.Vector2D;
import lombok.Getter;

public class Rectangle2D extends AbstractShape2D {

    @Getter
    private final Vector2D topLeft;
    @Getter
    private final float width;
    @Getter
    private final float height;
    
    public Rectangle2D(Vector2D topLeft, float width, float height) {
        this.topLeft = topLeft;
        this.width = width;
        this.height = height;
    }

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
