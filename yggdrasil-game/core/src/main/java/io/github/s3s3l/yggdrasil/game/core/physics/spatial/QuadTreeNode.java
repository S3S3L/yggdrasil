package io.github.s3s3l.yggdrasil.game.core.physics.spatial;

import io.github.s3s3l.yggdrasil.game.core.basic.Vector2;
import io.github.s3s3l.yggdrasil.game.core.physics.collider.BoxCollider;
import io.github.s3s3l.yggdrasil.game.core.physics.collider.Collider;
import io.github.s3s3l.yggdrasil.game.core.physics.collider.Rectangle;

import java.util.ArrayList;
import java.util.List;

/**
 * 四叉树节点
 * 用于空间划分和碰撞体管理
 */
public class QuadTreeNode {

    /**
     * 边界范围
     */
    private final Rectangle bounds;

    /**
     * 节点容量（超过此数量后细分）
     */
    private final int capacity;

    /**
     * 当前深度
     */
    private final int depth;

    /**
     * 最大深度
     */
    private final int maxDepth;

    /**
     * 存储的碰撞体
     */
    private final List<Collider> colliders;

    /**
     * 子节点（null 表示未细分）
     */
    private QuadTreeNode[] children;

    /**
     * 创建四叉树节点
     *
     * @param bounds 边界范围
     * @param capacity 节点容量
     * @param depth 当前深度
     * @param maxDepth 最大深度
     */
    public QuadTreeNode(Rectangle bounds, int capacity, int depth, int maxDepth) {
        this.bounds = bounds;
        this.capacity = capacity;
        this.depth = depth;
        this.maxDepth = maxDepth;
        this.colliders = new ArrayList<>();
        this.children = null;
    }

    /**
     * 插入碰撞体
     *
     * @param collider 碰撞体
     * @return 是否插入成功
     */
    public boolean insert(Collider collider) {
        // 检查是否在边界内
        if (!isColliderInBounds(collider)) {
            return false;
        }

        // 如果未细分且未达最大深度，尝试添加到当前节点
        if (children == null) {
            colliders.add(collider);

            // 超过容量且未达最大深度，进行细分
            if (colliders.size() > capacity && depth < maxDepth) {
                subdivide();
                // 重新分配碰撞体到子节点
                redistribute();
            }

            return true;
        }

        // 已细分，尝试插入到子节点
        for (QuadTreeNode child : children) {
            if (child.insert(collider)) {
                return true;
            }
        }

        // 如果无法插入任何子节点（跨越边界），保留在当前节点
        colliders.add(collider);
        return true;
    }

    /**
     * 移除碰撞体
     *
     * @param collider 碰撞体
     * @return 是否移除成功
     */
    public boolean remove(Collider collider) {
        boolean removed = colliders.remove(collider);

        if (children != null) {
            for (QuadTreeNode child : children) {
                if (child.remove(collider)) {
                    removed = true;
                }
            }
        }

        return removed;
    }

    /**
     * 查询指定范围内的碰撞体
     *
     * @param range 查询范围
     * @param result 结果列表
     */
    public void query(Rectangle range, List<Collider> result) {
        // 如果查询范围与当前节点不相交，直接返回
        if (!bounds.intersects(range)) {
            return;
        }

        // 检查当前节点的碰撞体
        for (Collider collider : colliders) {
            Rectangle colliderBounds = collider.getBounds();
            if (colliderBounds != null && colliderBounds.intersects(range)) {
                result.add(collider);
            }
        }

        // 递归查询子节点
        if (children != null) {
            for (QuadTreeNode child : children) {
                child.query(range, result);
            }
        }
    }

    /**
     * 查询指定范围内的碰撞体（包括点查询）
     *
     * @param point 查询点
     * @param radius 查询半径
     * @param result 结果列表
     */
    public void query(Vector2 point, float radius, List<Collider> result) {
        Rectangle range = Rectangle.fromCenter(point.x, point.y, radius * 2, radius * 2);
        query(range, result);
    }

    /**
     * 获取所有碰撞体
     *
     * @param result 结果列表
     */
    public void getAll(List<Collider> result) {
        result.addAll(colliders);
        if (children != null) {
            for (QuadTreeNode child : children) {
                child.getAll(result);
            }
        }
    }

    /**
     * 细分节点
     */
    private void subdivide() {
        float centerX = bounds.getCenterX();
        float centerY = bounds.getCenterY();
        float halfWidth = bounds.getWidth() / 4;
        float halfHeight = bounds.getHeight() / 4;

        children = new QuadTreeNode[4];

        // 左上
        children[0] = new QuadTreeNode(
                new Rectangle(bounds.minX, centerY, centerX, bounds.maxY),
                capacity, depth + 1, maxDepth
        );

        // 右上
        children[1] = new QuadTreeNode(
                new Rectangle(centerX, centerY, bounds.maxX, bounds.maxY),
                capacity, depth + 1, maxDepth
        );

        // 左下
        children[2] = new QuadTreeNode(
                new Rectangle(bounds.minX, bounds.minY, centerX, centerY),
                capacity, depth + 1, maxDepth
        );

        // 右下
        children[3] = new QuadTreeNode(
                new Rectangle(centerX, bounds.minY, bounds.maxX, centerY),
                capacity, depth + 1, maxDepth
        );
    }

    /**
     * 重新分配碰撞体到子节点
     */
    private void redistribute() {
        List<Collider> toRedistribute = new ArrayList<>(colliders);
        colliders.clear();

        for (Collider collider : toRedistribute) {
            boolean inserted = false;
            for (QuadTreeNode child : children) {
                if (child.insert(collider)) {
                    inserted = true;
                    break;
                }
            }
            // 如果无法插入任何子节点，保留在当前节点
            if (!inserted) {
                colliders.add(collider);
            }
        }
    }

    /**
     * 检查碰撞体是否在边界内
     */
    private boolean isColliderInBounds(Collider collider) {
        Rectangle colliderBounds = collider.getBounds();
        if (colliderBounds == null) {
            return false;
        }

        return bounds.minX <= colliderBounds.minX &&
               colliderBounds.maxX <= bounds.maxX &&
               bounds.minY <= colliderBounds.minY &&
               colliderBounds.maxY <= bounds.maxY;
    }

    /**
     * 获取节点深度
     */
    public int getDepth() {
        return depth;
    }

    /**
     * 获取碰撞体数量
     */
    public int getColliderCount() {
        int count = colliders.size();
        if (children != null) {
            for (QuadTreeNode child : children) {
                count += child.getColliderCount();
            }
        }
        return count;
    }

    /**
     * 获取子节点数量
     */
    public int getChildCount() {
        return children != null ? 4 : 0;
    }

    /**
     * 是否已细分
     */
    public boolean isSubdivided() {
        return children != null;
    }
}
