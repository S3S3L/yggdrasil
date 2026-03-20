package io.github.s3s3l.yggdrasil.game.core.physics;

import io.github.s3s3l.yggdrasil.game.core.basic.Vector2;
import io.github.s3s3l.yggdrasil.game.core.physics.collider.BoxCollider;
import io.github.s3s3l.yggdrasil.game.core.physics.collider.CircleCollider;
import io.github.s3s3l.yggdrasil.game.core.physics.collider.Collider;
import io.github.s3s3l.yggdrasil.game.core.physics.collider.PolygonCollider;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * 射线投射器
 * 提供射线与碰撞体的相交检测
 */
public class RayCaster {

    private RayCaster() {
        // 工具类，禁止实例化
    }

    /**
     * 投射射线检测单个碰撞体
     *
     * @param ray      射线
     * @param collider 碰撞体
     * @param maxDistance 最大检测距离（<=0 表示无限）
     * @return 命中信息（未命中时 hit=false）
     */
    public static RayCastHit cast(Ray ray, Collider collider, float maxDistance) {
        RayCastHit hit = new RayCastHit();

        if (collider instanceof BoxCollider) {
            castBox(ray, (BoxCollider) collider, hit, maxDistance);
        } else if (collider instanceof CircleCollider) {
            castCircle(ray, (CircleCollider) collider, hit, maxDistance);
        } else if (collider instanceof PolygonCollider) {
            castPolygon(ray, (PolygonCollider) collider, hit, maxDistance);
        }

        hit.collider = collider;
        return hit;
    }

    /**
     * 投射射线检测单个碰撞体（无距离限制）
     *
     * @param ray      射线
     * @param collider 碰撞体
     * @return 命中信息（未命中时 hit=false）
     */
    public static RayCastHit cast(Ray ray, Collider collider) {
        return cast(ray, collider, 0);
    }

    /**
     * 投射射线检测多个碰撞体，返回最近的命中（无距离限制）
     *
     * @param ray       射线
     * @param colliders 碰撞体列表
     * @return 最近的命中信息（未命中时 hit=false）
     */
    public static RayCastHit castAll(Ray ray, List<Collider> colliders) {
        return castAll(ray, colliders, 0);
    }

    /**
     * 投射射线检测多个碰撞体，返回最近的命中
     *
     * @param ray         射线
     * @param colliders   碰撞体列表
     * @param maxDistance 最大检测距离（<=0 表示无限）
     * @return 最近的命中信息（未命中时 hit=false）
     */
    public static RayCastHit castAll(Ray ray, List<Collider> colliders, float maxDistance) {
        RayCastHit closestHit = new RayCastHit();
        float closestDistance = maxDistance <= 0 ? Float.MAX_VALUE : maxDistance;

        for (Collider collider : colliders) {
            RayCastHit hit = cast(ray, collider, maxDistance);
            if (hit.hit && hit.distance < closestDistance) {
                closestDistance = hit.distance;
                closestHit.set(hit.hit, hit.point, hit.normal, hit.distance, hit.collider);
            }
        }

        return closestHit;
    }

    /**
     * 投射射线检测多个碰撞体，返回所有命中（无距离限制）
     *
     * @param ray       射线
     * @param colliders 碰撞体列表
     * @return 所有命中信息列表（按距离排序）
     */
    public static List<RayCastHit> castAllToList(Ray ray, List<Collider> colliders) {
        return castAllToList(ray, colliders, 0);
    }

    /**
     * 投射射线检测多个碰撞体，返回所有命中
     *
     * @param ray         射线
     * @param colliders   碰撞体列表
     * @param maxDistance 最大检测距离（<=0 表示无限）
     * @return 所有命中信息列表（按距离排序）
     */
    public static List<RayCastHit> castAllToList(Ray ray, List<Collider> colliders, float maxDistance) {
        List<RayCastHit> hits = new ArrayList<>();

        for (Collider collider : colliders) {
            RayCastHit hit = cast(ray, collider, maxDistance);
            if (hit.hit) {
                hits.add(hit);
            }
        }

        // 按距离排序
        Collections.sort(hits, new Comparator<RayCastHit>() {
            @Override
            public int compare(RayCastHit h1, RayCastHit h2) {
                return Float.compare(h1.distance, h2.distance);
            }
        });

        return hits;
    }

    /**
     * 射线与矩形碰撞体检测
     */
    private static void castBox(Ray ray, BoxCollider box, RayCastHit hit, float maxDistance) {
        float minX = box.getLeft();
        float maxX = box.getRight();
        float minY = box.getBottom();
        float maxY = box.getTop();

        float tMin = 0;
        float tMax = maxDistance <= 0 ? Float.MAX_VALUE : maxDistance;

        // 检查与 X 轴的边界
        if (Math.abs(ray.direction.x) < 1e-8f) {
            // 射线平行于 Y 轴
            if (ray.origin.x < minX || ray.origin.x > maxX) {
                hit.set(false, null, null, 0, null);
                return;
            }
        } else {
            float t1 = (minX - ray.origin.x) / ray.direction.x;
            float t2 = (maxX - ray.origin.x) / ray.direction.x;

            if (t1 > t2) {
                float temp = t1;
                t1 = t2;
                t2 = temp;
            }

            tMin = Math.max(tMin, t1);
            tMax = Math.min(tMax, t2);

            if (tMin > tMax) {
                hit.set(false, null, null, 0, null);
                return;
            }
        }

        // 检查与 Y 轴的边界
        if (Math.abs(ray.direction.y) < 1e-8f) {
            // 射线平行于 X 轴
            if (ray.origin.y < minY || ray.origin.y > maxY) {
                hit.set(false, null, null, 0, null);
                return;
            }
        } else {
            float t1 = (minY - ray.origin.y) / ray.direction.y;
            float t2 = (maxY - ray.origin.y) / ray.direction.y;

            if (t1 > t2) {
                float temp = t1;
                t1 = t2;
                t2 = temp;
            }

            tMin = Math.max(tMin, t1);
            tMax = Math.min(tMax, t2);

            if (tMin > tMax) {
                hit.set(false, null, null, 0, null);
                return;
            }
        }

        // 计算命中点
        Vector2 hitPoint = ray.getPoint(tMin);
        hit.set(true, hitPoint, new Vector2(), tMin, box);

        // 计算法线
        float epsilon = 0.0001f;
        if (Math.abs(hitPoint.x - minX) < epsilon) {
            hit.normal.set(-1, 0);
        } else if (Math.abs(hitPoint.x - maxX) < epsilon) {
            hit.normal.set(1, 0);
        } else if (Math.abs(hitPoint.y - minY) < epsilon) {
            hit.normal.set(0, -1);
        } else if (Math.abs(hitPoint.y - maxY) < epsilon) {
            hit.normal.set(0, 1);
        }
    }

    /**
     * 射线与圆形碰撞体检测
     */
    private static void castCircle(Ray ray, CircleCollider circle, RayCastHit hit, float maxDistance) {
        Vector2 toCenter = new Vector2(
                circle.getCenterX() - ray.origin.x,
                circle.getCenterY() - ray.origin.y
        );

        float radius = circle.getRadius();

        // 检查起点是否在圆内
        float distanceToCenter = toCenter.length();
        if (distanceToCenter < radius) {
            hit.set(true, ray.origin, toCenter.normalized(), 0, circle);
            return;
        }

        // 投影到射线方向
        float projection = toCenter.dot(ray.direction);

        // 如果圆心在射线后方，不可能相交
        if (projection < 0) {
            hit.set(false, null, null, 0, null);
            return;
        }

        // 计算从圆心到射线的最短距离
        Vector2 closestPoint = new Vector2(
                ray.origin.x + ray.direction.x * projection,
                ray.origin.y + ray.direction.y * projection
        );

        float closestDistance = closestPoint.distance(circle.getCenter());

        if (closestDistance > radius) {
            hit.set(false, null, null, 0, null);
            return;
        }

        // 计算交点距离
        float halfChord = (float) Math.sqrt(radius * radius - closestDistance * closestDistance);
        float t = projection - halfChord;

        if (t < 0) {
            t = projection + halfChord;
        }

        if (maxDistance > 0 && t > maxDistance) {
            hit.set(false, null, null, 0, null);
            return;
        }

        Vector2 hitPoint = ray.getPoint(t);
        Vector2 normal = new Vector2(
                hitPoint.x - circle.getCenterX(),
                hitPoint.y - circle.getCenterY()
        );
        normal.normalize();

        hit.set(true, hitPoint, normal, t, circle);
    }

    /**
     * 射线与多边形碰撞体检测
     */
    private static void castPolygon(Ray ray, PolygonCollider polygon, RayCastHit hit, float maxDistance) {
        float closestT = maxDistance <= 0 ? Float.MAX_VALUE : maxDistance;
        Vector2 closestPoint = null;
        Vector2 closestNormal = null;

        int n = polygon.getVertexCount();
        for (int i = 0; i < n; i++) {
            Vector2 p1 = polygon.getTransformedVertex(i);
            Vector2 p2 = polygon.getTransformedVertex((i + 1) % n);

            RayCastHit edgeHit = castSegment(ray, p1, p2, maxDistance);
            if (edgeHit.hit && edgeHit.distance < closestT) {
                closestT = edgeHit.distance;
                closestPoint = edgeHit.point;
                closestNormal = edgeHit.normal;
            }
        }

        if (closestPoint != null) {
            hit.set(true, closestPoint, closestNormal, closestT, polygon);
        } else {
            hit.set(false, null, null, 0, null);
        }
    }

    /**
     * 射线与线段检测
     */
    private static RayCastHit castSegment(Ray ray, Vector2 p1, Vector2 p2, float maxDistance) {
        RayCastHit hit = new RayCastHit();

        float x1 = p1.x, y1 = p1.y;
        float x2 = p2.x, y2 = p2.y;
        float x3 = ray.origin.x, y3 = ray.origin.y;
        float x4 = ray.origin.x + ray.direction.x, y4 = ray.origin.y + ray.direction.y;

        float denom = (y4 - y3) * (x2 - x1) - (x4 - x3) * (y2 - y1);

        if (Math.abs(denom) < 1e-8f) {
            hit.set(false, null, null, 0, null);
            return hit;
        }

        float t = ((x4 - x3) * (y1 - y3) - (y4 - y3) * (x1 - x3)) / denom;
        float u = ((x2 - x1) * (y1 - y3) - (y2 - y1) * (x1 - x3)) / denom;

        if (t >= 0 && t <= 1 && u >= 0 && (maxDistance <= 0 || u <= maxDistance)) {
            float hitX = x1 + t * (x2 - x1);
            float hitY = y1 + t * (y2 - y1);
            hit.set(true, new Vector2(hitX, hitY), new Vector2(-(y2 - y1), x2 - x1).normalize(), u, null);
        } else {
            hit.set(false, null, null, 0, null);
        }

        return hit;
    }
}
