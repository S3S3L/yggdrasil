package io.github.s3s3l.yggdrasil.game.core.action;

import io.github.s3s3l.yggdrasil.game.core.grid.Coordinate;
import io.github.s3s3l.yggdrasil.game.core.grid.Grid;
import io.github.s3s3l.yggdrasil.game.core.unit.Unit;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * 移动动作
 */
@Getter
public class MoveAction implements Action {

    private final Grid grid;
    private final int baseMovementCost;

    public MoveAction(Grid grid) {
        this.grid = grid;
        this.baseMovementCost = 1;
    }

    @Override
    public String getName() {
        return "移动";
    }

    @Override
    public String getDescription() {
        return "移动到目标位置";
    }

    @Override
    public int getApCost() {
        return 0; // 移动不消耗行动力，由移动系统处理
    }

    @Override
    public int getRange() {
        return Integer.MAX_VALUE; // 移动范围由单位的移动力决定
    }

    @Override
    public TargetType getTargetType() {
        return TargetType.POSITION;
    }

    @Override
    public boolean canExecute(Unit unit, Object target) {
        if (!(target instanceof Coordinate)) {
            return false;
        }
        if (!unit.isAlive()) {
            return false;
        }
        Coordinate currentPos = unit.getPosition();
        Coordinate targetPos = (Coordinate) target;

        // 检查是否是相邻位置
        double distance = grid.getDistance(currentPos, targetPos);
        return distance <= 1;
    }

    @Override
    public ActionResult execute(Unit unit, Object target) {
        if (!(target instanceof Coordinate)) {
            return ActionResult.failure("无效的目标位置");
        }

        Coordinate targetPos = (Coordinate) target;

        if (!canExecute(unit, target)) {
            return ActionResult.failure("无法移动到该位置");
        }

        // 执行移动（实际移动由 MovementSystem 处理）
        unit.setPosition(targetPos);

        return ActionResult.success("移动到了 " + targetPos);
    }

    @Override
    public List<Coordinate> getValidTargets(Unit unit) {
        List<Coordinate> validTargets = new ArrayList<>();
        Coordinate currentPos = unit.getPosition();

        // 获取相邻位置
        List<Coordinate> neighbors = grid.getNeighbors(currentPos);
        for (Coordinate neighbor : neighbors) {
            if (canExecute(unit, neighbor)) {
                validTargets.add(neighbor);
            }
        }

        return validTargets;
    }
}
