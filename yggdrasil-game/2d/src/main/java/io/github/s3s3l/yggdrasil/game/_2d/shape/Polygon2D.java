package io.github.s3s3l.yggdrasil.game._2d.shape;

import io.github.s3s3l.yggdrasil.game._2d.vector.Vector2D;
import lombok.Getter;

public class Polygon2D extends AbstractShape2D {

    @Getter
    private final Vector2D[] vertices;

    public Polygon2D(Vector2D[] vertices) {
        this.vertices = vertices;
    }

    @Override
    public boolean contains(Vector2D point) {
        int n = vertices.length;
        boolean inside = false;
        final float EPS = 1e-6f;
        for (int i = 0, j = n - 1; i < n; j = i++) {
            float xi = vertices[i].getX(), yi = vertices[i].getY();
            float xj = vertices[j].getX(), yj = vertices[j].getY();
            // 边界点判断
            float minX = Math.min(xi, xj), maxX = Math.max(xi, xj);
            float minY = Math.min(yi, yj), maxY = Math.max(yi, yj);
            float dx = xj - xi, dy = yj - yi;
            if (Math.abs(dx) > EPS || Math.abs(dy) > EPS) {
                float cross = (point.getX() - xi) * dy - (point.getY() - yi) * dx;
                if (Math.abs(cross) < EPS &&
                    point.getX() >= minX - EPS && point.getX() <= maxX + EPS &&
                    point.getY() >= minY - EPS && point.getY() <= maxY + EPS) {
                    return true;
                }
            }
            // 射线法
            boolean intersect = ((yi > point.getY()) != (yj > point.getY()));
            if (intersect) {
                float denom = yj - yi;
                if (Math.abs(denom) < EPS) continue;
                float xCross = (xj - xi) * (point.getY() - yi) / denom + xi;
                if (point.getX() < xCross + EPS) {
                    inside = !inside;
                }
            }
        }
        return inside;
    }

    @Override
    public int type() {
        return ShapeType2D.POLYGON.code();
    }
    
}
