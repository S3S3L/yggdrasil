package io.github.s3s3l.yggdrasil.game.core.pathfinding;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import io.github.s3s3l.yggdrasil.game.core.grid.Coordinate;

/**
 * 寻路节点
 * 用于 A* 算法的路径节点
 */
@Getter
@RequiredArgsConstructor
@EqualsAndHashCode
public class PathNode implements Comparable<PathNode> {

    /**
     * 节点坐标
     */
    private final Coordinate coordinate;

    /**
     * 父节点（用于回溯路径）
     */
    private final PathNode parent;

    /**
     * G 值：从起点到当前节点的实际代价
     */
    private final int gCost;

    /**
     * H 值：从当前节点到终点的估计代价
     */
    private final int hCost;

    /**
     * F 值：总代价 (G + H)
     */
    private final int fCost;

    /**
     * 创建新的寻路节点
     *
     * @param coordinate 坐标
     * @param parent     父节点
     * @param gCost      G 值
     * @param hCost      H 值
     */
    public PathNode(Coordinate coordinate, PathNode parent, int gCost, int hCost) {
        this.coordinate = coordinate;
        this.parent = parent;
        this.gCost = gCost;
        this.hCost = hCost;
        this.fCost = gCost + hCost;
    }

    @Override
    public int compareTo(PathNode other) {
        // F 值小的优先，F 值相同时 H 值小的优先
        int diff = Integer.compare(this.fCost, other.fCost);
        if (diff != 0) {
            return diff;
        }
        return Integer.compare(this.hCost, other.hCost);
    }

    /**
     * 回溯路径
     *
     * @return 从起点到当前节点的路径
     */
    public java.util.List<Coordinate> backtrackPath() {
        java.util.List<Coordinate> path = new java.util.ArrayList<>();
        PathNode current = this;

        while (current != null) {
            path.add(0, current.coordinate);
            current = current.parent;
        }

        return path;
    }
}
