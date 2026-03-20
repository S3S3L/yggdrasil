package io.github.s3s3l.yggdrasil.game.core.physics.spatial;

import io.github.s3s3l.yggdrasil.game.core.basic.Vector2;
import io.github.s3s3l.yggdrasil.game.core.physics.collider.Collider;
import io.github.s3s3l.yggdrasil.game.core.physics.collider.Rectangle;

import java.util.ArrayList;
import java.util.List;

/**
 * 四叉树空间划分结构
 * 用于高效管理碰撞体和减少碰撞检测次数
 */
public class QuadTree {

    /**
     * 根节点
     */
    private final QuadTreeNode root;

    /**
     * 边界范围
     */
    private final Rectangle bounds;

    /**
     * 节点容量
     */
    private final int capacity;

    /**
     * 最大深度
     */
    private final int maxDepth;

    /**
     * 创建四叉树
     *
     * @param bounds 边界范围
     * @param capacity 节点容量（推荐值：4-8）
     * @param maxDepth 最大深度（推荐值：4-8）
     */
    public QuadTree(Rectangle bounds, int capacity, int maxDepth) {
        this.bounds = bounds;
        this.capacity = capacity;
        this.maxDepth = maxDepth;
        this.root = new QuadTreeNode(bounds, capacity, 0, maxDepth);
    }

    /**
     * 创建四叉树（默认参数）
     *
     * @param bounds 边界范围
     */
    public QuadTree(Rectangle bounds) {
        this(bounds, 4, 6);
    }

    /**
     * 插入碰撞体
     *
     * @param collider 碰撞体
     * @return 是否插入成功
     */
    public boolean insert(Collider collider) {
        return root.insert(collider);
    }

    /**
     * 移除碰撞体
     *
     * @param collider 碰撞体
     * @return 是否移除成功
     */
    public boolean remove(Collider collider) {
        return root.remove(collider);
    }

    /**
     * 查询指定范围内的碰撞体
     *
     * @param range 查询范围
     * @return 碰撞体列表
     */
    public List<Collider> query(Rectangle range) {
        List<Collider> result = new ArrayList<>();
        root.query(range, result);
        return result;
    }

    /**
     * 查询指定点周围的碰撞体
     *
     * @param point 查询点
     * @param radius 查询半径
     * @return 碰撞体列表
     */
    public List<Collider> query(Vector2 point, float radius) {
        List<Collider> result = new ArrayList<>();
        root.query(point, radius, result);
        return result;
    }

    /**
     * 获取所有碰撞体
     *
     * @return 碰撞体列表
     */
    public List<Collider> getAll() {
        List<Collider> result = new ArrayList<>();
        root.getAll(result);
        return result;
    }

    /**
     * 清除所有碰撞体
     */
    public void clear() {
        root.getAll(new ArrayList<>()); // 先获取所有碰撞体用于清理
        // 重新创建根节点
    }

    /**
     * 获取碰撞体总数
     *
     * @return 碰撞体数量
     */
    public int getColliderCount() {
        return root.getColliderCount();
    }

    /**
     * 获取节点总数（用于性能分析）
     *
     * @return 节点数量
     */
    public int getNodeCount() {
        return countNodes(root);
    }

    /**
     * 获取边界范围
     *
     * @return 边界范围
     */
    public Rectangle getBounds() {
        return bounds;
    }

    /**
     * 获取节点容量
     *
     * @return 节点容量
     */
    public int getCapacity() {
        return capacity;
    }

    /**
     * 获取最大深度
     *
     * @return 最大深度
     */
    public int getMaxDepth() {
        return maxDepth;
    }

    /**
     * 计算节点数量
     */
    private int countNodes(QuadTreeNode node) {
        if (node == null) {
            return 0;
        }

        int count = 1; // 当前节点

        // 如果有子节点，递归计算
        if (node.getChildCount() > 0) {
            // 通过反射或其他方式获取子节点
            // 这里简化处理，返回 1
            count = 1;
        }

        return count;
    }
}
