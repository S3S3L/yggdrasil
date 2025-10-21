package io.github.s3s3l.yggdrasil.game._2d.shape;

import io.github.s3s3l.yggdrasil.game._2d.vector.Vector2D;
import lombok.Getter;

public class Circle2D extends AbstractShape2D {

    /**
     * 圆心
     */
    @Getter
    private final Vector2D center;
    /**
     * 半径
     */
    @Getter
    private final float radius;

    public Circle2D(Vector2D center, float radius) {
        this.center = center;
        this.radius = radius;
    }

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
