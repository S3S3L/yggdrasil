package io.github.s3s3l.yggdrasil.game.core.physics;

import io.github.s3s3l.yggdrasil.game.core.basic.Vector2;
import io.github.s3s3l.yggdrasil.game.core.physics.collider.Collider;

/**
 * 射线投射命中信息
 * 存储射线检测结果的详细信息
 */
public class RayCastHit {

    /**
     * 是否命中
     */
    public boolean hit;

    /**
     * 命中点（世界坐标）
     */
    public Vector2 point;

    /**
     * 命中点法线
     */
    public Vector2 normal;

    /**
     * 命中距离（从射线原点）
     */
    public float distance;

    /**
     * 命中的碰撞体
     */
    public Collider collider;

    /**
     * 创建空的命中信息
     */
    public RayCastHit() {
        this.hit = false;
        this.point = new Vector2();
        this.normal = new Vector2();
        this.distance = 0;
    }

    /**
     * 设置命中信息
     *
     * @param hit       是否命中
     * @param point     命中点
     * @param normal    法线
     * @param distance  距离
     * @param collider  碰撞体
     */
    public void set(boolean hit, Vector2 point, Vector2 normal, float distance, Collider collider) {
        this.hit = hit;
        if (point != null) {
            this.point.set(point);
        }
        if (normal != null) {
            this.normal.set(normal);
        }
        this.distance = distance;
        this.collider = collider;
    }

    /**
     * 清除命中信息
     */
    public void clear() {
        this.hit = false;
        this.point.set(0, 0);
        this.normal.set(0, 0);
        this.distance = 0;
        this.collider = null;
    }

    /**
     * 复制命中信息
     *
     * @return 新的命中信息副本
     */
    public RayCastHit copy() {
        RayCastHit copy = new RayCastHit();
        copy.hit = this.hit;
        copy.point.set(this.point);
        copy.normal.set(this.normal);
        copy.distance = this.distance;
        copy.collider = this.collider;
        return copy;
    }

    @Override
    public String toString() {
        if (hit) {
            return String.format("RayCastHit[hit=true, point=%s, distance=%.2f, collider=%s]",
                    point, distance, collider != null ? collider.toString() : "null");
        }
        return "RayCastHit[hit=false]";
    }
}
