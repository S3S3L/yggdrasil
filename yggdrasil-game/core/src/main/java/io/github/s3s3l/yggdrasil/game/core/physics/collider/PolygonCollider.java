package io.github.s3s3l.yggdrasil.game.core.physics.collider;

import io.github.s3s3l.yggdrasil.game.core.basic.Vector2;
import io.github.s3s3l.yggdrasil.game.core.pool.PoolableObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 多边形碰撞体
 * 使用 SAT（分离轴定理）进行碰撞检测
 * 支持凸多边形
 * 支持对象池化
 */
public class PolygonCollider implements Collider, PoolableObject<PolygonCollider> {

    private float centerX;
    private float centerY;
    private final List<Vector2> vertices;
    private final List<Vector2> transformedVertices;
    private float rotation;
    private boolean isTrigger;  // 是否是触发器

    /**
     * 创建多边形碰撞体
     *
     * @param centerX  中心 X 坐标
     * @param centerY  中心 Y 坐标
     * @param vertices 顶点列表（相对于中心）
     */
    public PolygonCollider(float centerX, float centerY, List<Vector2> vertices) {
        this.centerX = centerX;
        this.centerY = centerY;
        this.vertices = new ArrayList<>(vertices);
        this.transformedVertices = new ArrayList<>(vertices.size());
        this.rotation = 0;
        updateTransformedVertices();
    }

    /**
     * 从矩形创建多边形
     *
     * @param centerX 中心 X 坐标
     * @param centerY 中心 Y 坐标
     * @param width   宽度
     * @param height  高度
     * @return 矩形多边形碰撞体
     */
    public static PolygonCollider createRectangle(float centerX, float centerY, float width, float height) {
        List<Vector2> vertices = new ArrayList<>(4);
        float hw = width / 2f;
        float hh = height / 2f;
        vertices.add(new Vector2(-hw, -hh));
        vertices.add(new Vector2(hw, -hh));
        vertices.add(new Vector2(hw, hh));
        vertices.add(new Vector2(-hw, hh));
        return new PolygonCollider(centerX, centerY, vertices);
    }

    /**
     * 从正多边形创建
     *
     * @param centerX    中心 X 坐标
     * @param centerY    中心 Y 坐标
     * @param radius     外接圆半径
     * @param numSides   边数
     * @param rotation   初始旋转角度（弧度）
     * @return 正多边形碰撞体
     */
    public static PolygonCollider createRegularPolygon(float centerX, float centerY, float radius, int numSides, float rotation) {
        List<Vector2> vertices = new ArrayList<>(numSides);
        float angleStep = (float) (2 * Math.PI / numSides);
        for (int i = 0; i < numSides; i++) {
            float angle = i * angleStep + rotation;
            vertices.add(new Vector2(
                    (float) (radius * Math.cos(angle)),
                    (float) (radius * Math.sin(angle))
            ));
        }
        return new PolygonCollider(centerX, centerY, vertices);
    }

    /**
     * 获取顶点数量
     *
     * @return 顶点数量
     */
    public int getVertexCount() {
        return vertices.size();
    }

    /**
     * 清空顶点列表并设置新的顶点
     *
     * @param vertices 新的顶点列表
     */
    public void setVertices(List<Vector2> vertices) {
        this.vertices.clear();
        this.vertices.addAll(vertices);
        updateTransformedVertices();
    }

    /**
     * 获取指定索引的顶点（相对于中心）
     *
     * @param index 顶点索引
     * @return 顶点向量
     */
    public Vector2 getVertex(int index) {
        return vertices.get(index);
    }

    /**
     * 获取变换后的顶点（世界坐标）
     *
     * @param index 顶点索引
     * @return 世界坐标顶点
     */
    public Vector2 getTransformedVertex(int index) {
        return transformedVertices.get(index);
    }

    /**
     * 设置旋转角度
     *
     * @param rotation 旋转角度（弧度）
     */
    public void setRotation(float rotation) {
        this.rotation = rotation;
        updateTransformedVertices();
    }

    /**
     * 获取旋转角度
     *
     * @return 旋转角度（弧度）
     */
    public float getRotation() {
        return rotation;
    }

    /**
     * 更新变换后的顶点
     */
    private void updateTransformedVertices() {
        float cos = (float) Math.cos(rotation);
        float sin = (float) Math.sin(rotation);
        transformedVertices.clear();
        for (Vector2 v : vertices) {
            float x = v.x * cos - v.y * sin;
            float y = v.x * sin + v.y * cos;
            transformedVertices.add(new Vector2(centerX + x, centerY + y));
        }
    }

    /**
     * 获取所有边的法线（用于 SAT 检测）
     *
     * @return 法线列表
     */
    private List<Vector2> getAxes() {
        List<Vector2> axes = new ArrayList<>(transformedVertices.size());
        for (int i = 0; i < transformedVertices.size(); i++) {
            Vector2 p1 = transformedVertices.get(i);
            Vector2 p2 = transformedVertices.get((i + 1) % transformedVertices.size());
            Vector2 edge = new Vector2(p2.x - p1.x, p2.y - p1.y);
            // 获取法线（垂直于边）
            axes.add(new Vector2(-edge.y, edge.x));
        }
        return axes;
    }

    /**
     * 在指定轴上投影多边形，返回投影区间
     *
     * @param axis 投影轴
     * @return 投影区间 [min, max]
     */
    private float[] project(Vector2 axis) {
        float min = Float.POSITIVE_INFINITY;
        float max = Float.NEGATIVE_INFINITY;
        for (Vector2 vertex : transformedVertices) {
            float projection = vertex.x * axis.x + vertex.y * axis.y;
            min = Math.min(min, projection);
            max = Math.max(max, projection);
        }
        return new float[]{min, max};
    }

    /**
     * 检查两个投影区间是否重叠
     *
     * @param proj1 第一个投影区间
     * @param proj2 第二个投影区间
     * @return 是否重叠
     */
    private boolean projectionsOverlap(float[] proj1, float[] proj2) {
        return proj1[0] < proj2[1] && proj2[0] < proj1[1];
    }

    @Override
    public ColliderType getType() {
        return ColliderType.POLYGON;
    }

    @Override
    public Vector2 getPosition() {
        return new Vector2(centerX, centerY);
    }

    @Override
    public void setPosition(float x, float y) {
        this.centerX = x;
        this.centerY = y;
        updateTransformedVertices();
    }

    @Override
    public void setPosition(Vector2 position) {
        this.centerX = position.x;
        this.centerY = position.y;
        updateTransformedVertices();
    }

    @Override
    public Rectangle getBounds() {
        float minX = Float.POSITIVE_INFINITY;
        float minY = Float.POSITIVE_INFINITY;
        float maxX = Float.NEGATIVE_INFINITY;
        float maxY = Float.NEGATIVE_INFINITY;
        for (Vector2 v : transformedVertices) {
            minX = Math.min(minX, v.x);
            minY = Math.min(minY, v.y);
            maxX = Math.max(maxX, v.x);
            maxY = Math.max(maxY, v.y);
        }
        return new Rectangle(minX, minY, maxX, maxY);
    }

    @Override
    public boolean contains(float x, float y) {
        // 使用射线法判断点是否在多边形内
        int intersections = 0;
        int n = transformedVertices.size();
        for (int i = 0; i < n; i++) {
            Vector2 p1 = transformedVertices.get(i);
            Vector2 p2 = transformedVertices.get((i + 1) % n);
            // 检查射线是否与边相交
            if (((p1.y > y) != (p2.y > y)) &&
                    (x < (p2.x - p1.x) * (y - p1.y) / (p2.y - p1.y) + p1.x)) {
                intersections++;
            }
        }
        return (intersections % 2) == 1;
    }

    /**
     * 检查是否与另一个多边形碰撞体相交（使用 SAT）
     *
     * @param other 另一个多边形碰撞体
     * @return 是否相交
     */
    public boolean intersects(PolygonCollider other) {
        // 检查此多边形的所有边
        for (Vector2 axis : this.getAxes()) {
            if (!projectionsOverlap(this.project(axis), other.project(axis))) {
                return false;
            }
        }
        // 检查另一个多边形的所有边
        for (Vector2 axis : other.getAxes()) {
            if (!projectionsOverlap(this.project(axis), other.project(axis))) {
                return false;
            }
        }
        return true;
    }

    /**
     * 检查是否与矩形碰撞体相交
     *
     * @param box 矩形碰撞体
     * @return 是否相交
     */
    public boolean intersects(BoxCollider box) {
        // 将矩形转换为多边形进行检测
        PolygonCollider boxPoly = PolygonCollider.createRectangle(
                box.getPosition().x, box.getPosition().y,
                box.getWidth(), box.getHeight()
        );
        return intersects(boxPoly);
    }

    /**
     * 检查是否与圆形碰撞体相交
     *
     * @param circle 圆形碰撞体
     * @return 是否相交
     */
    public boolean intersects(CircleCollider circle) {
        // 找到多边形上距离圆心最近的点
        Vector2 circleCenter = circle.getCenter();
        float radius = circle.getRadius();

        // 检查圆心是否在多边形内
        if (contains(circleCenter.x, circleCenter.y)) {
            return true;
        }

        // 检查所有边与圆的关系
        int n = transformedVertices.size();
        for (int i = 0; i < n; i++) {
            Vector2 p1 = transformedVertices.get(i);
            Vector2 p2 = transformedVertices.get((i + 1) % n);

            // 找到线段上距离圆心最近的点
            Vector2 closest = closestPointOnSegment(p1, p2, circleCenter);
            float distanceSquared = circleCenter.distanceSquared(closest);

            if (distanceSquared < radius * radius) {
                return true;
            }
        }
        return false;
    }

    /**
     * 找到线段上距离给定点最近的点
     */
    private Vector2 closestPointOnSegment(Vector2 p1, Vector2 p2, Vector2 point) {
        Vector2 segment = new Vector2(p2.x - p1.x, p2.y - p1.y);
        Vector2 pointToP1 = new Vector2(point.x - p1.x, point.y - p1.y);
        float segmentLengthSquared = segment.lengthSquared();

        if (segmentLengthSquared == 0) {
            return p1;
        }

        float t = pointToP1.dot(segment) / segmentLengthSquared;
        t = Math.max(0, Math.min(1, t));

        return new Vector2(
                p1.x + t * segment.x,
                p1.y + t * segment.y
        );
    }

    @Override
    public boolean intersects(Collider other) {
        if (other instanceof PolygonCollider) {
            return intersects((PolygonCollider) other);
        } else if (other instanceof BoxCollider) {
            return intersects((BoxCollider) other);
        } else if (other instanceof CircleCollider) {
            return intersects((CircleCollider) other);
        }
        return false;
    }

    @Override
    public PolygonCollider copy() {
        List<Vector2> newVertices = new ArrayList<>();
        for (Vector2 v : vertices) {
            newVertices.add(new Vector2(v));
        }
        PolygonCollider copy = new PolygonCollider(centerX, centerY, newVertices);
        copy.setRotation(rotation);
        copy.isTrigger = this.isTrigger;
        return copy;
    }

    @Override
    public boolean isTrigger() {
        return isTrigger;
    }

    @Override
    public void setTrigger(boolean isTrigger) {
        this.isTrigger = isTrigger;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PolygonCollider that = (PolygonCollider) o;
        return Float.compare(that.centerX, centerX) == 0 &&
               Float.compare(that.centerY, centerY) == 0 &&
               Float.compare(that.rotation, rotation) == 0 &&
               vertices.equals(that.vertices);
    }

    @Override
    public int hashCode() {
        int result = (centerX != 0.0f ? Float.floatToIntBits(centerX) : 0);
        result = 31 * result + (centerY != 0.0f ? Float.floatToIntBits(centerY) : 0);
        result = 31 * result + (rotation != 0.0f ? Float.floatToIntBits(rotation) : 0);
        result = 31 * result + vertices.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return String.format("PolygonCollider[center=(%.2f, %.2f), vertices=%d, rotation=%.2f]",
                centerX, centerY, vertices.size(), rotation);
    }

    @Override
    public void reset() {
        this.centerX = 0;
        this.centerY = 0;
        this.rotation = 0;
        this.vertices.clear();
        this.transformedVertices.clear();
        this.isTrigger = false;
    }

    @Override
    public void onReturn() {
        reset();
    }

    @Override
    public boolean isValid() {
        return true;
    }

    /**
     * 设置碰撞体数据（用于从池中借出后初始化）
     *
     * @param centerX 中心 X 坐标
     * @param centerY 中心 Y 坐标
     * @param vertices 顶点列表
     */
    public void set(float centerX, float centerY, List<Vector2> vertices) {
        this.centerX = centerX;
        this.centerY = centerY;
        this.vertices.clear();
        this.vertices.addAll(vertices);
        this.transformedVertices.clear();
        this.transformedVertices.addAll(vertices);
        this.rotation = 0;
        updateTransformedVertices();
    }
}
