package io.github.s3s3l.yggdrasil.game.core.movement;

import io.github.s3s3l.yggdrasil.game.core.grid.Coordinate;
import io.github.s3s3l.yggdrasil.game.core.grid.Grid;
import io.github.s3s3l.yggdrasil.game.core.grid.GridMetrics;
import io.github.s3s3l.yggdrasil.game.core.map.GameMap;
import io.github.s3s3l.yggdrasil.game.core.map.Terrain;
import io.github.s3s3l.yggdrasil.game.core.pathfinding.AStarPathfinder;
import io.github.s3s3l.yggdrasil.game.core.pathfinding.Pathfinder;
import io.github.s3s3l.yggdrasil.game.core.unit.Unit;

import java.util.ArrayList;
import java.util.List;

/**
 * 移动系统
 * 处理单位的移动逻辑
 */
public class MovementSystem implements MovementValidator {

    /**
     * 地图
     */
    private final GameMap map;

    /**
     * 寻路器
     */
    private final Pathfinder pathfinder;

    /**
     * 创建移动系统
     *
     * @param map 地图
     */
    public MovementSystem(GameMap map) {
        this.map = map;
        this.pathfinder = new AStarPathfinder(map.getGrid());
    }

    /**
     * 创建移动系统（自定义寻路器）
     *
     * @param map        地图
     * @param pathfinder 寻路器
     */
    public MovementSystem(GameMap map, Pathfinder pathfinder) {
        this.map = map;
        this.pathfinder = pathfinder;
    }

    /**
     * 移动单位到目标位置
     *
     * @param unit   单位
     * @param target 目标位置
     * @return 移动结果
     */
    public MovementResult moveUnit(Unit unit, Coordinate target) {
        if (unit == null || target == null) {
            return MovementResult.failure("Unit or target is null");
        }

        if (!unit.isAlive()) {
            return MovementResult.failure("Unit is not alive");
        }

        Coordinate from = unit.getPosition();
        MovementResult validateResult = validate(unit, from, target, unit.getStats().getMovement());

        if (!validateResult.isValid()) {
            return validateResult;
        }

        // 找到路径
        List<Coordinate> path = pathfinder.findPath(from, target, unit.getStats().getMovement());

        if (path.isEmpty()) {
            return MovementResult.failure("No valid path found");
        }

        // 验证路径
        if (!validatePath(unit, path)) {
            return MovementResult.failure("Path is not valid");
        }

        // 移动单位
        unit.setPosition(target);

        return MovementResult.success(validateResult.getMovementCost());
    }

    /**
     * 获取单位可以移动到的所有位置
     *
     * @param unit 单位
     * @return 可移动位置列表
     */
    @Override
    public List<Coordinate> getReachablePositions(Unit unit, Coordinate position) {
        List<Coordinate> reachable = new ArrayList<>();
        Grid grid = map.getGrid();
        GridMetrics metrics = grid.getMetrics();
        int movement = unit.getStats().getMovement();

        // 遍历范围内的所有坐标
        for (int x = metrics.getMinX(); x < metrics.getMaxX(); x++) {
            for (int y = metrics.getMinY(); y < metrics.getMaxY(); y++) {
                Coordinate coord = grid.toCoordinate(x + y * metrics.getWidth());
                if (grid.isValid(coord) && !coord.equals(position)) {
                    MovementResult result = validate(unit, position, coord, movement);
                    if (result.isValid()) {
                        reachable.add(coord);
                    }
                }
            }
        }

        return reachable;
    }

    @Override
    public MovementResult validate(Unit unit, Coordinate from, Coordinate to, int movement) {
        if (!map.isValid(to)) {
            return MovementResult.failure("Target position is outside map bounds");
        }

        Terrain terrain = map.getTerrain(to);
        if (terrain == null || !terrain.isPassable()) {
            return MovementResult.impassable(to);
        }

        // 计算移动消耗
        float movementCost = terrain.getMovementCost();
        int distance = (int) map.getGrid().getDistance(from, to);
        int requiredMovement = (int) (distance * movementCost);

        if (requiredMovement > movement) {
            return MovementResult.insufficientMovement(requiredMovement, movement);
        }

        return MovementResult.success(requiredMovement);
    }

    @Override
    public boolean validatePath(Unit unit, List<Coordinate> path) {
        if (path == null || path.isEmpty()) {
            return false;
        }

        int remainingMovement = unit.getStats().getMovement();
        Coordinate prev = path.get(0);

        for (int i = 1; i < path.size(); i++) {
            Coordinate current = path.get(i);
            MovementResult result = validate(unit, prev, current, remainingMovement);

            if (!result.isValid()) {
                return false;
            }

            remainingMovement -= result.getMovementCost();
            prev = current;
        }

        return true;
    }

    /**
     * 获取寻路器
     *
     * @return 寻路器
     */
    public Pathfinder getPathfinder() {
        return pathfinder;
    }
}
