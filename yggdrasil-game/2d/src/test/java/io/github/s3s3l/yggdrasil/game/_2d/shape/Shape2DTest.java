package io.github.s3s3l.yggdrasil.game._2d.shape;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import io.github.s3s3l.yggdrasil.game._2d.vector.Vector2D;

public class Shape2DTest {
    @Test
    @DisplayName("各种Shape2D的intersects(Shape<Vector2D>)互测")
    public void testIntersectsShape2D() {
        // 构造各种形状
        Circle2D circle = new Circle2D(new Vector2D(0, 0), 2f);
        Rectangle2D rect = new Rectangle2D(new Vector2D(-1, -1), 2f, 2f);
        Line2D line = new Line2D(new Vector2D(0, 0), new Vector2D(1, 1));
        Segment2D segment = new Segment2D(new Vector2D(-1, 0), new Vector2D(1, 0));
        Ray2D ray = new Ray2D(new Vector2D(0, 0), new Vector2D(1, 0));
        Polygon2D triangle = new Polygon2D(
                new Vector2D[] { new Vector2D(0, 0), new Vector2D(2, 0), new Vector2D(1, 2) });
        InfinitePlane2D plane = new InfinitePlane2D();

        Shape2D[] shapes = new Shape2D[] { circle, rect, line, segment, ray, triangle, plane };
        String[] names = new String[] { "Circle2D", "Rectangle2D", "Line2D", "Segment2D", "Ray2D", "Polygon2D",
                "InfinitePlane2D" };

        // 互测intersects(Shape<Vector2D>)
        for (int i = 0; i < shapes.length; i++) {
            final int fi = i;
            for (int j = 0; j < shapes.length; j++) {
                final int fj = j;
                // 这里只断言不抛异常，具体几何关系由ShapeHandlerManager2D实现
                assertDoesNotThrow(() -> shapes[fi].intersects(shapes[fj]),
                        names[fi] + ".intersects(" + names[fj] + ") 不应抛异常");
            }
        }

        // 边界值和特殊情况
        // 1. 零面积形状
        Circle2D zeroCircle = new Circle2D(new Vector2D(0, 0), 0f);
        Rectangle2D zeroRect = new Rectangle2D(new Vector2D(0, 0), 0f, 0f);
        Segment2D zeroSegment = new Segment2D(new Vector2D(1, 1), new Vector2D(1, 1));
        Polygon2D zeroPolygon = new Polygon2D(
                new Vector2D[] { new Vector2D(2, 2), new Vector2D(2, 2), new Vector2D(2, 2) });
        assertDoesNotThrow(() -> zeroCircle.intersects(zeroCircle), "零半径圆自相交不应抛异常");
        assertDoesNotThrow(() -> zeroRect.intersects(zeroRect), "零面积矩形自相交不应抛异常");
        assertDoesNotThrow(() -> zeroSegment.intersects(zeroSegment), "零长度线段自相交不应抛异常");
        assertDoesNotThrow(() -> zeroPolygon.intersects(zeroPolygon), "零面积多边形自相交不应抛异常");

        // 2. 极值坐标
        Circle2D bigCircle = new Circle2D(new Vector2D(Float.MAX_VALUE, Float.MAX_VALUE), Float.MAX_VALUE);
        Rectangle2D bigRect = new Rectangle2D(new Vector2D(-Float.MAX_VALUE, -Float.MAX_VALUE), Float.MAX_VALUE,
                Float.MAX_VALUE);
        assertDoesNotThrow(() -> bigCircle.intersects(circle), "极大圆与普通圆相交不应抛异常");
        assertDoesNotThrow(() -> bigRect.intersects(rect), "极大矩形与普通矩形相交不应抛异常");

        // 3. 完全重叠
        Circle2D circle2 = new Circle2D(new Vector2D(0, 0), 2f);
        Rectangle2D rect2 = new Rectangle2D(new Vector2D(-1, -1), 2f, 2f);
        assertDoesNotThrow(() -> circle.intersects(circle2), "完全重叠圆相交不应抛异常");
        assertDoesNotThrow(() -> rect.intersects(rect2), "完全重叠矩形相交不应抛异常");

        // 4. 部分重叠
        Circle2D overlapCircle = new Circle2D(new Vector2D(1, 0), 2f);
        Rectangle2D overlapRect = new Rectangle2D(new Vector2D(0, 0), 2f, 2f);
        assertDoesNotThrow(() -> circle.intersects(overlapCircle), "部分重叠圆相交不应抛异常");
        assertDoesNotThrow(() -> rect.intersects(overlapRect), "部分重叠矩形相交不应抛异常");

        // 5. 相切
        Circle2D tangentCircle = new Circle2D(new Vector2D(4, 0), 2f);
        assertDoesNotThrow(() -> circle.intersects(tangentCircle), "相切圆相交不应抛异常");

        // 6. 无交集
        Circle2D farCircle = new Circle2D(new Vector2D(100, 100), 1f);
        Rectangle2D farRect = new Rectangle2D(new Vector2D(100, 100), 1f, 1f);
        assertDoesNotThrow(() -> circle.intersects(farCircle), "无交集圆不应抛异常");
        assertDoesNotThrow(() -> rect.intersects(farRect), "无交集矩形不应抛异常");

        // 7. 退化为点的多边形
        Polygon2D pointPoly = new Polygon2D(new Vector2D[] { new Vector2D(0, 0) });
        assertDoesNotThrow(() -> pointPoly.intersects(pointPoly), "单点多边形自相交不应抛异常");

        // 8. 退化为线的多边形
        Polygon2D linePoly = new Polygon2D(new Vector2D[] { new Vector2D(0, 0), new Vector2D(1, 1) });
        assertDoesNotThrow(() -> linePoly.intersects(linePoly), "线段多边形自相交不应抛异常");

        // 9. 无限平面与任意形状相交
        assertDoesNotThrow(() -> plane.intersects(circle), "无限平面与圆相交不应抛异常");
        assertDoesNotThrow(() -> plane.intersects(rect), "无限平面与矩形相交不应抛异常");
        assertDoesNotThrow(() -> plane.intersects(triangle), "无限平面与多边形相交不应抛异常");
        assertDoesNotThrow(() -> plane.intersects(plane), "无限平面自相交不应抛异常");
    }

    @Test
    @DisplayName("各种Shape2D的contains(Shape<Vector2D>)互测")
    public void testContainsShape2D() {
        // 构造各种形状
        Circle2D circle = new Circle2D(new Vector2D(0, 0), 2f);
        Rectangle2D rect = new Rectangle2D(new Vector2D(-1, -1), 2f, 2f);
        Line2D line = new Line2D(new Vector2D(0, 0), new Vector2D(1, 1));
        Segment2D segment = new Segment2D(new Vector2D(-1, 0), new Vector2D(1, 0));
        Ray2D ray = new Ray2D(new Vector2D(0, 0), new Vector2D(1, 0));
        Polygon2D triangle = new Polygon2D(
                new Vector2D[] { new Vector2D(0, 0), new Vector2D(2, 0), new Vector2D(1, 2) });
        InfinitePlane2D plane = new InfinitePlane2D();

        Shape2D[] shapes = new Shape2D[] { circle, rect, line, segment, ray, triangle, plane };
        String[] names = new String[] { "Circle2D", "Rectangle2D", "Line2D", "Segment2D", "Ray2D", "Polygon2D",
                "InfinitePlane2D" };

        // 互测contains(Shape<Vector2D>)
        for (int i = 0; i < shapes.length; i++) {
            final int fi = i;
            for (int j = 0; j < shapes.length; j++) {
                final int fj = j;
                // 这里只断言不抛异常，具体几何关系由ShapeHandlerManager2D实现
                assertDoesNotThrow(() -> shapes[fi].contains(shapes[fj]),
                        names[fi] + ".contains(" + names[fj] + ") 不应抛异常");
            }
        }

        // 边界值和特殊情况
        // 1. 零面积形状
        Circle2D zeroCircle = new Circle2D(new Vector2D(0, 0), 0f);
        Rectangle2D zeroRect = new Rectangle2D(new Vector2D(0, 0), 0f, 0f);
        Segment2D zeroSegment = new Segment2D(new Vector2D(1, 1), new Vector2D(1, 1));
        Polygon2D zeroPolygon = new Polygon2D(
                new Vector2D[] { new Vector2D(2, 2), new Vector2D(2, 2), new Vector2D(2, 2) });
        assertDoesNotThrow(() -> zeroCircle.contains(zeroCircle), "零半径圆自包含不应抛异常");
        assertDoesNotThrow(() -> zeroRect.contains(zeroRect), "零面积矩形自包含不应抛异常");
        assertDoesNotThrow(() -> zeroSegment.contains(zeroSegment), "零长度线段自包含不应抛异常");
        assertDoesNotThrow(() -> zeroPolygon.contains(zeroPolygon), "零面积多边形自包含不应抛异常");

        // 2. 极值坐标
        Circle2D bigCircle = new Circle2D(new Vector2D(Float.MAX_VALUE, Float.MAX_VALUE), Float.MAX_VALUE);
        Rectangle2D bigRect = new Rectangle2D(new Vector2D(-Float.MAX_VALUE, -Float.MAX_VALUE), Float.MAX_VALUE,
                Float.MAX_VALUE);
        assertDoesNotThrow(() -> bigCircle.contains(circle), "极大圆包含普通圆不应抛异常");
        assertDoesNotThrow(() -> bigRect.contains(rect), "极大矩形包含普通矩形不应抛异常");

        // 3. 完全重叠
        Circle2D circle2 = new Circle2D(new Vector2D(0, 0), 2f);
        Rectangle2D rect2 = new Rectangle2D(new Vector2D(-1, -1), 2f, 2f);
        assertDoesNotThrow(() -> circle.contains(circle2), "完全重叠圆不应抛异常");
        assertDoesNotThrow(() -> rect.contains(rect2), "完全重叠矩形不应抛异常");

        // 4. 部分重叠
        Circle2D overlapCircle = new Circle2D(new Vector2D(1, 0), 2f);
        Rectangle2D overlapRect = new Rectangle2D(new Vector2D(0, 0), 2f, 2f);
        assertDoesNotThrow(() -> circle.contains(overlapCircle), "部分重叠圆不应抛异常");
        assertDoesNotThrow(() -> rect.contains(overlapRect), "部分重叠矩形不应抛异常");

        // 5. 相切
        Circle2D tangentCircle = new Circle2D(new Vector2D(4, 0), 2f);
        assertDoesNotThrow(() -> circle.contains(tangentCircle), "相切圆不应抛异常");

        // 6. 无交集
        Circle2D farCircle = new Circle2D(new Vector2D(100, 100), 1f);
        Rectangle2D farRect = new Rectangle2D(new Vector2D(100, 100), 1f, 1f);
        assertDoesNotThrow(() -> circle.contains(farCircle), "无交集圆不应抛异常");
        assertDoesNotThrow(() -> rect.contains(farRect), "无交集矩形不应抛异常");

        // 7. 退化为点的多边形
        Polygon2D pointPoly = new Polygon2D(new Vector2D[] { new Vector2D(0, 0) });
        assertDoesNotThrow(() -> pointPoly.contains(pointPoly), "单点多边形自包含不应抛异常");

        // 8. 退化为线的多边形
        Polygon2D linePoly = new Polygon2D(new Vector2D[] { new Vector2D(0, 0), new Vector2D(1, 1) });
        assertDoesNotThrow(() -> linePoly.contains(linePoly), "线段多边形自包含不应抛异常");

        // 9. 无限平面包含任意形状
        assertDoesNotThrow(() -> plane.contains(circle), "无限平面包含圆不应抛异常");
        assertDoesNotThrow(() -> plane.contains(rect), "无限平面包含矩形不应抛异常");
        assertDoesNotThrow(() -> plane.contains(triangle), "无限平面包含多边形不应抛异常");
        assertDoesNotThrow(() -> plane.contains(plane), "无限平面自包含不应抛异常");
    }
}
