package io.github.s3s3l.yggdrasil.game.core.physics.collider;

import io.github.s3s3l.yggdrasil.game.core.basic.Vector2;

/**
 * 碰撞检测器
 * 提供所有碰撞体类型的碰撞检测功能
 */
public class CollisionDetector {

    private CollisionDetector() {
        // 工具类，禁止实例化
    }

    /**
     * 检查两个碰撞体是否相交
     *
     * @param a 第一个碰撞体
     * @param b 第二个碰撞体
     * @return 是否相交
     */
    public static boolean checkCollision(Collider a, Collider b) {
        return a.intersects(b);
    }

    /**
     * 获取碰撞信息
     *
     * @param a 第一个碰撞体
     * @param b 第二个碰撞体
     * @return 碰撞信息（如果未碰撞，collided=false）
     */
    public static CollisionInfo getCollisionInfo(Collider a, Collider b) {
        CollisionInfo info = new CollisionInfo();

        if (!a.intersects(b)) {
            info.set(false, null, null, 0, a, b);
            return info;
        }

        // 计算碰撞信息
        if (a instanceof BoxCollider && b instanceof BoxCollider) {
            computeBoxBoxCollision((BoxCollider) a, (BoxCollider) b, info);
        } else if (a instanceof CircleCollider && b instanceof CircleCollider) {
            computeCircleCircleCollision((CircleCollider) a, (CircleCollider) b, info);
        } else if (a instanceof CircleCollider && b instanceof BoxCollider) {
            computeCircleBoxCollision((CircleCollider) a, (BoxCollider) b, info);
        } else if (a instanceof BoxCollider && b instanceof CircleCollider) {
            computeCircleBoxCollision((CircleCollider) b, (BoxCollider) a, info);
            info.normal.scaleLocal(-1); // 反转向量
        } else if (a instanceof PolygonCollider) {
            computePolygonCollision((PolygonCollider) a, b, info);
        } else if (b instanceof PolygonCollider) {
            computePolygonCollision((PolygonCollider) b, a, info);
            info.normal.scaleLocal(-1); // 反转向量
        }

        return info;
    }

    /**
     * 计算两个矩形碰撞体的碰撞信息
     */
    private static void computeBoxBoxCollision(BoxCollider a, BoxCollider b, CollisionInfo info) {
        float dx = b.getPosition().x - a.getPosition().x;
        float dy = b.getPosition().y - a.getPosition().y;

        float overlapX = (a.getHalfWidth() + b.getHalfWidth()) - Math.abs(dx);
        float overlapY = (a.getHalfHeight() + b.getHalfHeight()) - Math.abs(dy);

        // 找到最小穿透轴
        if (overlapX < overlapY) {
            info.penetrationDepth = overlapX;
            info.normal.set(dx > 0 ? 1 : -1, 0);
        } else {
            info.penetrationDepth = overlapY;
            info.normal.set(0, dy > 0 ? 1 : -1);
        }

        // 计算接触点
        Vector2 aCenter = a.getPosition();
        Vector2 bCenter = b.getPosition();
        info.contactPoint.set(
                (aCenter.x + bCenter.x) / 2f,
                (aCenter.y + bCenter.y) / 2f
        );

        info.set(true, info.contactPoint, info.normal, info.penetrationDepth, a, b);
    }

    /**
     * 计算两个圆形碰撞体的碰撞信息
     */
    private static void computeCircleCircleCollision(CircleCollider a, CircleCollider b, CollisionInfo info) {
        Vector2 aCenter = a.getPosition();
        Vector2 bCenter = b.getPosition();

        float dx = bCenter.x - aCenter.x;
        float dy = bCenter.y - aCenter.y;
        float distanceSquared = dx * dx + dy * dy;
        float distance = (float) Math.sqrt(distanceSquared);

        if (distance == 0) {
            // 圆心重合
            info.penetrationDepth = a.getRadius() + b.getRadius();
            info.normal.set(1, 0);
            info.contactPoint.set(aCenter);
        } else {
            info.penetrationDepth = (a.getRadius() + b.getRadius()) - distance;
            info.normal.set(dx / distance, dy / distance);
            info.contactPoint.set(
                    aCenter.x + info.normal.x * a.getRadius(),
                    aCenter.y + info.normal.y * a.getRadius()
            );
        }

        info.set(true, info.contactPoint, info.normal, info.penetrationDepth, a, b);
    }

    /**
     * 计算圆形和矩形碰撞体的碰撞信息
     */
    private static void computeCircleBoxCollision(CircleCollider circle, BoxCollider box, CollisionInfo info) {
        Vector2 circleCenter = circle.getPosition();

        // 找到矩形上距离圆心最近的点
        float closestX = clamp(circleCenter.x, box.getLeft(), box.getRight());
        float closestY = clamp(circleCenter.y, box.getBottom(), box.getTop());

        float dx = circleCenter.x - closestX;
        float dy = circleCenter.y - closestY;
        float distanceSquared = dx * dx + dy * dy;
        float distance = (float) Math.sqrt(distanceSquared);

        if (distance == 0) {
            // 圆心在矩形内部或边界上
            info.contactPoint.set(closestX, closestY);
            // 计算穿透深度
            float penetrationX = Math.min(
                    Math.abs(circleCenter.x - box.getLeft()),
                    Math.abs(circleCenter.x - box.getRight())
            );
            float penetrationY = Math.min(
                    Math.abs(circleCenter.y - box.getBottom()),
                    Math.abs(circleCenter.y - box.getTop())
            );

            if (penetrationX < penetrationY) {
                info.penetrationDepth = penetrationX + circle.getRadius();
                info.normal.set(circleCenter.x < box.getPosition().x ? -1 : 1, 0);
            } else {
                info.penetrationDepth = penetrationY + circle.getRadius();
                info.normal.set(0, circleCenter.y < box.getPosition().y ? -1 : 1);
            }
        } else {
            info.contactPoint.set(closestX, closestY);
            info.penetrationDepth = circle.getRadius() - distance;
            info.normal.set(dx / distance, dy / distance);
        }

        info.set(true, info.contactPoint, info.normal, info.penetrationDepth, circle, box);
    }

    /**
     * 计算多边形碰撞体的碰撞信息
     */
    private static void computePolygonCollision(PolygonCollider polygon, Collider other, CollisionInfo info) {
        // 简化处理：使用多边形的边界框
        Rectangle polyBounds = polygon.getBounds();
        Rectangle otherBounds = other.getBounds();

        // 计算重叠区域中心作为接触点
        float contactX = (Math.max(polyBounds.minX, otherBounds.minX) +
                          Math.min(polyBounds.maxX, otherBounds.maxX)) / 2f;
        float contactY = (Math.max(polyBounds.minY, otherBounds.minY) +
                          Math.min(polyBounds.maxY, otherBounds.maxY)) / 2f;

        info.contactPoint.set(contactX, contactY);

        // 计算从多边形中心指向其他碰撞体中心的法线
        Vector2 polygonCenter = polygon.getPosition();
        Vector2 otherCenter = other.getPosition();
        float dx = otherCenter.x - polygonCenter.x;
        float dy = otherCenter.y - polygonCenter.y;
        float distance = (float) Math.sqrt(dx * dx + dy * dy);

        if (distance > 0) {
            info.normal.set(dx / distance, dy / distance);
        } else {
            info.normal.set(1, 0);
        }

        // 估算穿透深度
        info.penetrationDepth = Math.min(
                Math.min(polyBounds.maxX, otherBounds.maxX) - Math.max(polyBounds.minX, otherBounds.minX),
                Math.min(polyBounds.maxY, otherBounds.maxY) - Math.max(polyBounds.minY, otherBounds.minY)
        );

        info.set(true, info.contactPoint, info.normal, info.penetrationDepth, polygon, other);
    }

    /**
     * 钳制值到指定范围
     */
    private static float clamp(float value, float min, float max) {
        return Math.max(min, Math.min(max, value));
    }
}
