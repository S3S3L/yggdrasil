
package io.github.s3s3l.yggdrasil.game._2d.utils;

import java.util.function.Predicate;

import io.github.s3s3l.yggdrasil.game._2d.shape.Circle2D;
import io.github.s3s3l.yggdrasil.game._2d.shape.Line2D;
import io.github.s3s3l.yggdrasil.game._2d.shape.Polygon2D;
import io.github.s3s3l.yggdrasil.game._2d.shape.Ray2D;
import io.github.s3s3l.yggdrasil.game._2d.shape.Rectangle2D;
import io.github.s3s3l.yggdrasil.game._2d.shape.Segment2D;
import io.github.s3s3l.yggdrasil.game._2d.vector.Vector2D;

public class Shape2DHelper {

    /**
     * 判断线段与直线是否相交
     * 
     * @param segment 线段
     * @param line    直线
     * @return 是否相交
     */
    public static boolean segmentLineIntersect(Segment2D segment, Line2D line) {
        Vector2D segStart = segment.getStart();
        Vector2D segEnd = segment.getEnd();
        Vector2D linePoint = line.getPoint();
        Vector2D lineDir = line.getDirection();

        float cross1 = (segStart.getX() - linePoint.getX()) * lineDir.getY()
                - (segStart.getY() - linePoint.getY()) * lineDir.getX();
        float cross2 = (segEnd.getX() - linePoint.getX()) * lineDir.getY()
                - (segEnd.getY() - linePoint.getY()) * lineDir.getX();
        if (Math.abs(cross1) < 1e-8 || Math.abs(cross2) < 1e-8)
            return true;
        return cross1 * cross2 < 0;
    }

    /**
     * 判断线段与射线是否相交
     * 
     * @param segment 线段
     * @param ray     射线
     * @return 是否相交
     */
    public static boolean segmentRayIntersect(Segment2D segment, Ray2D ray) {
        Vector2D a = segment.getStart();
        Vector2D b = segment.getEnd();
        Vector2D o = ray.getOrigin();
        Vector2D d = ray.getDirection();

        float dx = b.getX() - a.getX();
        float dy = b.getY() - a.getY();
        float det = d.getX() * (-dy) - d.getY() * (-dx);
        if (Math.abs(det) < 1e-8)
            return false;
        float t = ((a.getX() - o.getX()) * (-dy) - (a.getY() - o.getY()) * (-dx)) / det;
        float s = (d.getX() * (a.getY() - o.getY()) - d.getY() * (a.getX() - o.getX())) / det;
        return t >= 0 && s >= 0 && s <= 1;
    }

    /**
     * 判断射线与矩形是否相交
     * 
     * @param ray  射线
     * @param rect 矩形
     * @return 是否相交
     */
    public static boolean rayRectangleIntersect(Ray2D ray, Rectangle2D rect) {
        Vector2D rayOrigin = ray.getOrigin();
        Vector2D rayDir = ray.getDirection();
        Vector2D rectTopLeft = rect.getTopLeft();
        float width = rect.getWidth();
        float height = rect.getHeight();

        Vector2D[] corners = new Vector2D[] {
                rectTopLeft,
                new Vector2D(rectTopLeft.getX() + width, rectTopLeft.getY()),
                new Vector2D(rectTopLeft.getX() + width, rectTopLeft.getY() + height),
                new Vector2D(rectTopLeft.getX(), rectTopLeft.getY() + height)
        };
        for (int i = 0; i < 4; i++) {
            Vector2D a = corners[i];
            Vector2D b = corners[(i + 1) % 4];
            float det = rayDir.getX() * (b.getY() - a.getY()) - rayDir.getY() * (b.getX() - a.getX());
            if (Math.abs(det) < 1e-8)
                continue;
            float t = ((a.getX() - rayOrigin.getX()) * (b.getY() - a.getY())
                    - (a.getY() - rayOrigin.getY()) * (b.getX() - a.getX())) / det;
            float s = (rayDir.getX() * (a.getY() - rayOrigin.getY()) - rayDir.getY() * (a.getX() - rayOrigin.getX()))
                    / det;
            if (t >= 0 && s >= 0 && s <= 1)
                return true;
        }
        // 起点在矩形内
        return rect.contains(rayOrigin);
    }

    /**
     * 判断线段与多边形是否相交
     * 
     * @param segment 线段
     * @param polygon 多边形
     * @return 是否相交
     */
    public static boolean segmentPolygonIntersect(Segment2D segment, Polygon2D polygon) {
        Vector2D segStart = segment.getStart();
        Vector2D segEnd = segment.getEnd();
        Vector2D[] polygonPoints = polygon.getVertices();

        for (int i = 0; i < polygonPoints.length; i++) {
            Vector2D a = polygonPoints[i];
            Vector2D b = polygonPoints[(i + 1) % polygonPoints.length];
            if (segmentsIntersect(segment, new Segment2D(a, b)))
                return true;
        }
        // 线段完全在多边形内
        if (polygon.contains(segStart) && polygon.contains(segEnd))
            return true;
        return false;
    }

    /**
     * 判断直线与矩形是否相交
     * 
     * @param line 直线
     * @param rect 矩形
     * @return 是否相交
     */
    public static boolean lineRectangleIntersect(Line2D line, Rectangle2D rect) {
        Vector2D linePoint = line.getPoint();
        Vector2D lineDirection = line.getDirection();
        Vector2D rectTopLeft = rect.getTopLeft();
        float width = rect.getWidth();
        float height = rect.getHeight();

        Vector2D[] corners = new Vector2D[] {
                rectTopLeft,
                new Vector2D(rectTopLeft.getX() + width, rectTopLeft.getY()),
                new Vector2D(rectTopLeft.getX() + width, rectTopLeft.getY() + height),
                new Vector2D(rectTopLeft.getX(), rectTopLeft.getY() + height)
        };
        for (int i = 0; i < 4; i++) {
            Vector2D a = corners[i];
            Vector2D b = corners[(i + 1) % 4];
            float cross1 = (a.getX() - linePoint.getX()) * lineDirection.getY()
                    - (a.getY() - linePoint.getY()) * lineDirection.getX();
            float cross2 = (b.getX() - linePoint.getX()) * lineDirection.getY()
                    - (b.getY() - linePoint.getY()) * lineDirection.getX();
            if (Math.abs(cross1) < 1e-8 || Math.abs(cross2) < 1e-8 || cross1 * cross2 < 0) {
                return true;
            }
        }
        // 检查是否有顶点在直线上
        for (Vector2D v : corners) {
            float cross = (v.getX() - linePoint.getX()) * lineDirection.getY()
                    - (v.getY() - linePoint.getY()) * lineDirection.getX();
            if (Math.abs(cross) < 1e-8)
                return true;
        }
        return false;
    }

    /**
     * 判断直线与射线是否相交
     * 
     * @param line 直线
     * @param ray  射线
     * @return 是否相交
     */
    public static boolean lineRayIntersect(Line2D line, Ray2D ray) {
        Vector2D linePoint = line.getPoint();
        Vector2D lineDirection = line.getDirection();
        Vector2D rayOrigin = ray.getOrigin();
        Vector2D rayDirection = ray.getDirection();

        float det = lineDirection.getX() * (-rayDirection.getY()) - lineDirection.getY() * (-rayDirection.getX());
        if (Math.abs(det) < 1e-8) {
            // 平行或重合，判断射线起点是否在直线上
            float cross = (rayOrigin.getX() - linePoint.getX()) * lineDirection.getY()
                    - (rayOrigin.getY() - linePoint.getY()) * lineDirection.getX();
            return Math.abs(cross) < 1e-8;
        }
        float dx = rayOrigin.getX() - linePoint.getX();
        float dy = rayOrigin.getY() - linePoint.getY();
        float t2 = (lineDirection.getX() * dy - lineDirection.getY() * dx) / det;
        return t2 >= -1e-8;
    }

    /**
     * 判断直线与圆是否相交
     * 
     * @param line   直线
     * @param circle 圆
     * @return 是否相交
     */
    public static boolean lineCircleIntersect(Line2D line, Circle2D circle) {
        Vector2D linePoint = line.getPoint();
        Vector2D lineDirection = line.getDirection();
        Vector2D circleCenter = circle.getCenter();
        float radius = circle.getRadius();

        float px = linePoint.getX(), py = linePoint.getY();
        float dx = lineDirection.getX(), dy = lineDirection.getY();
        float cx = circleCenter.getX(), cy = circleCenter.getY();
        float A = -dy;
        float B = dx;
        float C = dy * px - dx * py;
        float dist = Math.abs(A * cx + B * cy + C) / (float) Math.sqrt(A * A + B * B);
        return dist <= radius + 1e-8;
    }

    /**
     * 判断多边形与矩形是否相交
     * 
     * @param poly 多边形
     * @param rect 矩形
     * @return 是否相交
     */
    public static boolean polygonRectangleIntersect(Polygon2D poly, Rectangle2D rect) {
        Vector2D[] polygon = poly.getVertices();
        Predicate<Vector2D> polygonContains = poly::contains;
        Vector2D rectTopLeft = rect.getTopLeft();
        float width = rect.getWidth();
        float height = rect.getHeight();

        Vector2D tl = rectTopLeft;
        float w = width;
        float h = height;
        Vector2D[] rectCorners = new Vector2D[] {
                tl,
                new Vector2D(tl.getX() + w, tl.getY()),
                new Vector2D(tl.getX() + w, tl.getY() + h),
                new Vector2D(tl.getX(), tl.getY() + h)
        };
        // 多边形顶点在矩形内
        for (Vector2D v : polygon) {
            if (rect.contains(v))
                return true;
        }
        // 矩形顶点在多边形内
        for (Vector2D v : rectCorners) {
            if (polygonContains != null && polygonContains.test(v))
                return true;
        }
        // 边相交
        for (int i = 0; i < polygon.length; i++) {
            Vector2D a = polygon[i];
            Vector2D b = polygon[(i + 1) % polygon.length];
            for (int j = 0; j < 4; j++) {
                Vector2D c = rectCorners[j];
                Vector2D d = rectCorners[(j + 1) % 4];
                if (segmentsIntersect(new Segment2D(a, b), new Segment2D(c, d)))
                    return true;
            }
        }
        return false;
    }

    /**
     * 判断两射线是否相交
     * 
     * @param ray1 射线1
     * @param ray2 射线2
     * @return 是否相交
     */
    public static boolean raysIntersect(Ray2D ray1, Ray2D ray2) {
        Vector2D o1 = ray1.getOrigin();
        Vector2D d1 = ray1.getDirection();
        Vector2D o2 = ray2.getOrigin();
        Vector2D d2 = ray2.getDirection();

        float det = d1.getX() * (-d2.getY()) - d1.getY() * (-d2.getX());
        if (Math.abs(det) < 1e-8)
            return false;
        float t = ((o2.getX() - o1.getX()) * (-d2.getY()) - (o2.getY() - o1.getY()) * (-d2.getX())) / det;
        float s = (d1.getX() * (o2.getY() - o1.getY()) - d1.getY() * (o2.getX() - o1.getX())) / det;
        return t >= 0 && s >= 0;
    }

    /**
     * 判断两直线是否相交（不平行即视为相交）
     * 
     * @param line1 直线1
     * @param line2 直线2
     * @return 是否相交
     */
    public static boolean linesIntersect(Line2D line1, Line2D line2) {
        Vector2D d1 = line1.getDirection();
        Vector2D d2 = line2.getDirection();
        float det = d1.getX() * (-d2.getY()) - d1.getY() * (-d2.getX());
        return Math.abs(det) > 1e-8;
    }

    /**
     * 判断多边形与直线是否相交
     * 
     * @param poly 多边形
     * @param line 直线
     * @return 是否相交
     */
    public static boolean polygonLineIntersect(Polygon2D poly, Line2D line) {
        Vector2D[] polygon = poly.getVertices();
        Vector2D linePoint = line.getPoint();
        Vector2D lineDirection = line.getDirection();

        for (int i = 0; i < polygon.length; i++) {
            Vector2D a = polygon[i];
            Vector2D b = polygon[(i + 1) % polygon.length];
            float cross1 = (a.getX() - linePoint.getX()) * lineDirection.getY()
                    - (a.getY() - linePoint.getY()) * lineDirection.getX();
            float cross2 = (b.getX() - linePoint.getX()) * lineDirection.getY()
                    - (b.getY() - linePoint.getY()) * lineDirection.getX();
            if (Math.abs(cross1) < 1e-8 || Math.abs(cross2) < 1e-8 || cross1 * cross2 < 0) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断线段与矩形是否相交
     * 
     * @param segment 线段
     * @param rect    矩形
     * @return 是否相交
     */
    public static boolean segmentRectangleIntersect(Segment2D segment, Rectangle2D rect) {
        Vector2D segStart = segment.getStart();
        Vector2D segEnd = segment.getEnd();
        Vector2D rectTopLeft = rect.getTopLeft();
        float width = rect.getWidth();
        float height = rect.getHeight();

        float x = rectTopLeft.getX();
        float y = rectTopLeft.getY();
        float w = width;
        float h = height;
        Vector2D[] points = new Vector2D[] {
                new Vector2D(x, y),
                new Vector2D(x + w, y),
                new Vector2D(x, y + h),
                new Vector2D(x + w, y + h)
        };
        // 检查线段与矩形四条边是否相交
        for (int i = 0; i < 4; i++) {
            Vector2D a = points[i];
            Vector2D b = points[(i + 1) % 4];
            if (segmentsIntersect(segment, new Segment2D(a, b)))
                return true;
        }
        // 线段完全在矩形内
        if (segStart.getX() >= x && segStart.getX() <= x + w && segStart.getY() >= y && segStart.getY() <= y + h &&
                segEnd.getX() >= x && segEnd.getX() <= x + w && segEnd.getY() >= y && segEnd.getY() <= y + h)
            return true;
        return false;
    }

    /**
     * 判断多边形与射线是否相交
     * 
     * @param poly 多边形
     * @param ray  射线
     * @return 是否相交
     */
    public static boolean polygonRayIntersect(Polygon2D poly, Ray2D ray) {
        Vector2D[] polygon = poly.getVertices();
        Vector2D rayOrigin = ray.getOrigin();
        Vector2D rayDirection = ray.getDirection();

        for (int i = 0; i < polygon.length; i++) {
            Vector2D a = polygon[i];
            Vector2D b = polygon[(i + 1) % polygon.length];
            float det = rayDirection.getX() * (b.getY() - a.getY()) - rayDirection.getY() * (b.getX() - a.getX());
            if (Math.abs(det) < 1e-8)
                continue;
            float t = ((a.getX() - rayOrigin.getX()) * (b.getY() - a.getY())
                    - (a.getY() - rayOrigin.getY()) * (b.getX() - a.getX())) / det;
            float s = (rayDirection.getX() * (a.getY() - rayOrigin.getY())
                    - rayDirection.getY() * (a.getX() - rayOrigin.getX())) / det;
            if (t >= 0 && s >= 0 && s <= 1)
                return true;
        }
        // 起点在多边形内
        return poly.contains(rayOrigin);
    }

    /**
     * 判断圆与矩形是否相交
     * 
     * @param circle 圆
     * @param rect   矩形
     * @return 是否相交
     */
    public static boolean circleRectangleIntersect(Circle2D circle, Rectangle2D rect) {
        Vector2D circleCenter = circle.getCenter();
        float radius = circle.getRadius();
        Vector2D rectTopLeft = rect.getTopLeft();
        float width = rect.getWidth();
        float height = rect.getHeight();

        float cx = circleCenter.getX();
        float cy = circleCenter.getY();
        float x = rectTopLeft.getX();
        float y = rectTopLeft.getY();
        float closestX = Math.max(x, Math.min(cx, x + width));
        float closestY = Math.max(y, Math.min(cy, y + height));
        float dx = closestX - cx;
        float dy = closestY - cy;
        double dist = Math.sqrt(dx * dx + dy * dy);
        return dist <= radius;
    }

    /**
     * 判断多边形与圆是否相交（顶点在圆内/圆心在多边形内/边与圆相交）
     * 
     * @param poly   多边形
     * @param circle 圆
     * @return 是否相交
     */
    public static boolean polygonCircleIntersect(Polygon2D poly, Circle2D circle) {
        Vector2D[] polygon = poly.getVertices();
        Vector2D circleCenter = circle.getCenter();
        float radius = circle.getRadius();

        // 多边形顶点在圆内
        for (Vector2D v : polygon) {
            float dx = v.getX() - circleCenter.getX();
            float dy = v.getY() - circleCenter.getY();
            double dist = Math.sqrt(dx * dx + dy * dy);
            if (dist <= radius)
                return true;
        }
        // 圆心在多边形内
        if (poly.contains(circleCenter))
            return true;
        // 多边形边与圆是否相交
        for (int i = 0; i < polygon.length; i++) {
            Vector2D a = polygon[i];
            Vector2D b = polygon[(i + 1) % polygon.length];
            if (segmentCircleIntersect(new Segment2D(a, b), circle))
                return true;
        }
        return false;
    }

    /**
     * 判断射线与圆是否相交
     * 
     * @param ray    射线
     * @param circle 圆
     * @return 是否相交
     */
    public static boolean rayCircleIntersect(Ray2D ray, Circle2D circle) {
        Vector2D o = ray.getOrigin();
        Vector2D d = ray.getDirection();
        Vector2D c = circle.getCenter();
        float r = circle.getRadius();

        float fx = o.getX() - c.getX();
        float fy = o.getY() - c.getY();
        float a = d.getX() * d.getX() + d.getY() * d.getY();
        float b = 2 * (fx * d.getX() + fy * d.getY());
        float cc = fx * fx + fy * fy - r * r;
        float discriminant = b * b - 4 * a * cc;
        if (discriminant < 0)
            return false;
        discriminant = (float) Math.sqrt(discriminant);
        float t1 = (-b - discriminant) / (2 * a);
        float t2 = (-b + discriminant) / (2 * a);
        return t1 >= 0 || t2 >= 0;
    }

    /**
     * 判断线段与圆是否相交
     * 
     * @param segment 线段
     * @param circle  圆
     * @return 是否相交
     */
    public static boolean segmentCircleIntersect(Segment2D segment, Circle2D circle) {
        var a = segment.getStart();
        var b = segment.getEnd();
        var c = circle.getCenter();
        var r = circle.getRadius();

        float dx = b.getX() - a.getX();
        float dy = b.getY() - a.getY();
        float fx = a.getX() - c.getX();
        float fy = a.getY() - c.getY();
        float a_ = dx * dx + dy * dy;
        float b_ = 2 * (fx * dx + fy * dy);
        float c_ = fx * fx + fy * fy - r * r;
        float discriminant = b_ * b_ - 4 * a_ * c_;
        if (discriminant < 0)
            return false;
        discriminant = (float) Math.sqrt(discriminant);
        float t1 = (-b_ - discriminant) / (2 * a_);
        float t2 = (-b_ + discriminant) / (2 * a_);
        return (t1 >= 0 && t1 <= 1) || (t2 >= 0 && t2 <= 1);
    }

    /**
     * 判断两线段是否相交
     * 
     * @param seg1 线段1
     * @param seg2 线段2
     * @return 是否相交
     */
    public static boolean segmentsIntersect(Segment2D seg1, Segment2D seg2) {

        var p1 = seg1.getStart();
        var p2 = seg1.getEnd();
        var q1 = seg2.getStart();
        var q2 = seg2.getEnd();

        float d1 = direction(q1, q2, p1);
        float d2 = direction(q1, q2, p2);
        float d3 = direction(p1, p2, q1);
        float d4 = direction(p1, p2, q2);
        if (((d1 > 0 && d2 < 0) || (d1 < 0 && d2 > 0)) &&
                ((d3 > 0 && d4 < 0) || (d3 < 0 && d4 > 0)))
            return true;
        if (d1 == 0 && onSegment(q1, q2, p1))
            return true;
        if (d2 == 0 && onSegment(q1, q2, p2))
            return true;
        if (d3 == 0 && onSegment(p1, p2, q1))
            return true;
        if (d4 == 0 && onSegment(p1, p2, q2))
            return true;
        return false;
    }

    /**
     * 计算点c相对于有向线段ab的方向。
     * <p>
     * 该方法用于判断三点a、b、c的相对位置关系，常用于计算几何中的叉积判断：
     * <ul>
     * <li>返回值 &gt; 0：点c在ab的左侧（a→b→c为逆时针方向）</li>
     * <li>返回值 &lt; 0：点c在ab的右侧（a→b→c为顺时针方向）</li>
     * <li>返回值 = 0：三点共线</li>
     * </ul>
     * 该函数常用于判断线段相交、点在多边形内等几何问题。
     *
     * @param a 起点a
     * @param b 终点b
     * @param c 待判断的点c
     * @return 点c相对于有向线段ab的方向，&gt;0表示左侧，&lt;0表示右侧，=0表示共线
     */
    public static float direction(Vector2D a, Vector2D b, Vector2D c) {
        return (b.getX() - a.getX()) * (c.getY() - a.getY()) - (b.getY() - a.getY()) * (c.getX() - a.getX());
    }

    /**
     * 判断点c是否在线段ab上。
     * <p>
     * 该方法用于判断三点共线时，点c是否处于线段ab的端点之间（含端点）。
     * 只进行区间范围的判断，不判断三点是否共线，通常与direction方法配合使用。
     *
     * @param a 线段起点
     * @param b 线段终点
     * @param c 待判断的点
     * @return true表示点c在以a、b为端点的线段上（含端点），false表示不在
     */
    public static boolean onSegment(Vector2D a,
            Vector2D b,
            Vector2D c) {
        return Math.min(a.getX(), b.getX()) <= c.getX() && c.getX() <= Math.max(a.getX(), b.getX()) &&
                Math.min(a.getY(), b.getY()) <= c.getY() && c.getY() <= Math.max(a.getY(), b.getY());
    }

    /**
     * 计算两点之间的欧氏距离
     * 
     * @param a
     * @param b
     * @return 距离
     */
    public static float distance(Vector2D a, Vector2D b) {
        float dx = a.getX() - b.getX();
        float dy = a.getY() - b.getY();
        return (float) Math.sqrt(dx * dx + dy * dy);
    }

    /**
     * 计算点到直线的距离
     * 
     * @param point 点
     * @param line  直线
     * @return 距离
     */
    public static float distance(Vector2D point, Line2D line) {
        Vector2D linePoint = line.getPoint();
        Vector2D lineDir = line.getDirection();
        float A = -lineDir.getY();
        float B = lineDir.getX();
        float C = lineDir.getY() * linePoint.getX() - lineDir.getX() * linePoint.getY();
        return Math.abs(A * point.getX() + B * point.getY() + C) / (float) Math.sqrt(A * A + B * B);
    }
}
