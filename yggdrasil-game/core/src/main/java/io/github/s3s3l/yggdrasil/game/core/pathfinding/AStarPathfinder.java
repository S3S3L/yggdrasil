package io.github.s3s3l.yggdrasil.game.core.pathfinding;

import io.github.s3s3l.yggdrasil.game.core.grid.*;

import java.util.*;

/**
 * A* 寻路算法实现
 * 支持正方形和六边形网格
 */
public class AStarPathfinder implements Pathfinder {

    /**
     * 网格系统
     */
    private final Grid grid;

    /**
     * 可行走地形集合
     */
    private final Set<String> walkableTerrains;

    /**
     * 创建 A* 寻路器
     *
     * @param grid 网格系统
     */
    public AStarPathfinder(Grid grid) {
        this.grid = grid;
        this.walkableTerrains = new HashSet<>();
    }

    /**
     * 创建 A* 寻路器（指定可行走地形）
     *
     * @param grid              网格系统
     * @param walkableTerrains  可行走地形名称
     */
    public AStarPathfinder(Grid grid, Collection<String> walkableTerrains) {
        this.grid = grid;
        this.walkableTerrains = new HashSet<>(walkableTerrains);
    }

    @Override
    public List<Coordinate> findPath(Coordinate start, Coordinate end) {
        return findPath(start, end, Integer.MAX_VALUE);
    }

    @Override
    public List<Coordinate> findPath(Coordinate start, Coordinate end, int maxDistance) {
        if (start == null || end == null) {
            return Collections.emptyList();
        }

        if (!grid.isValid(start) || !grid.isValid(end)) {
            return Collections.emptyList();
        }

        // 开放列表（待处理节点）
        PriorityQueue<PathNode> openSet = new PriorityQueue<>();
        // 关闭列表（已处理节点）
        Set<Coordinate> closedSet = new HashSet<>();
        // 坐标到节点的映射
        Map<Coordinate, PathNode> nodeMap = new HashMap<>();

        // 初始化起点
        int hCost = calculateHeuristic(start, end);
        PathNode startNode = new PathNode(start, null, 0, hCost);
        openSet.add(startNode);
        nodeMap.put(start, startNode);

        while (!openSet.isEmpty()) {
            PathNode current = openSet.poll();

            // 到达终点
            if (current.getCoordinate().equals(end)) {
                List<Coordinate> path = current.backtrackPath();
                // 检查路径长度限制
                if (path.size() - 1 <= maxDistance) {
                    return path;
                }
                return Collections.emptyList();
            }

            // 添加到关闭列表
            closedSet.add(current.getCoordinate());

            // 遍历邻居
            for (Coordinate neighbor : grid.getNeighbors(current.getCoordinate())) {
                if (!grid.isValid(neighbor) || closedSet.contains(neighbor)) {
                    continue;
                }

                // 检查是否是可行走地形
                if (!isWalkable(neighbor)) {
                    continue;
                }

                // 计算新的 G 值
                int newGCost = current.getGCost() + getMovementCost(current.getCoordinate(), neighbor);

                // 检查是否需要更新
                PathNode existingNode = nodeMap.get(neighbor);
                if (existingNode != null && newGCost >= existingNode.getGCost()) {
                    continue;
                }

                // 创建或更新节点
                int neighborHCost = calculateHeuristic(neighbor, end);
                PathNode neighborNode = new PathNode(neighbor, current, newGCost, neighborHCost);
                openSet.add(neighborNode);
                nodeMap.put(neighbor, neighborNode);
            }
        }

        // 找不到路径
        return Collections.emptyList();
    }

    @Override
    public int calculateDistance(Coordinate start, Coordinate end) {
        return (int) grid.getDistance(start, end);
    }

    @Override
    public boolean canReach(Coordinate start, Coordinate end) {
        List<Coordinate> path = findPath(start, end);
        return !path.isEmpty();
    }

    /**
     * 启发式函数：计算从节点到终点的估计代价
     *
     * @param from 当前坐标
     * @param to   目标坐标
     * @return 估计代价
     */
    protected int calculateHeuristic(Coordinate from, Coordinate to) {
        // 使用网格的距离计算
        return (int) grid.getDistance(from, to);
    }

    /**
     * 获取从一个坐标移动到另一个坐标的消耗
     *
     * @param from 起始坐标
     * @param to   目标坐标
     * @return 移动消耗
     */
    protected int getMovementCost(Coordinate from, Coordinate to) {
        // 默认消耗为 1
        return 1;
    }

    /**
     * 检查坐标是否可行走
     *
     * @param coord 坐标
     * @return true 如果可行走
     */
    protected boolean isWalkable(Coordinate coord) {
        // 如果没有指定可行走地形，则默认所有坐标都可行走
        if (walkableTerrains.isEmpty()) {
            return true;
        }
        // 需要地形信息，这里简化处理
        return true;
    }
}
