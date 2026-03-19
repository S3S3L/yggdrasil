package io.github.s3s3l.yggdrasil.game.core.action;

import io.github.s3s3l.yggdrasil.game.core.grid.Coordinate;
import io.github.s3s3l.yggdrasil.game.core.grid.Grid;
import io.github.s3s3l.yggdrasil.game.core.unit.Unit;
import lombok.Getter;

import java.util.List;

/**
 * 基础攻击动作
 */
@Getter
public class BasicAttackAction implements Action {

    private final String name = "基础攻击";
    private final String description = "对目标造成物理伤害";
    private final int apCost = 2;
    private final int range = 1;
    private final Action.TargetType targetType = Action.TargetType.UNIT_HOSTILE;
    private final Grid grid;
    private final int baseDamage;

    public BasicAttackAction(Grid grid, int baseDamage) {
        this.grid = grid;
        this.baseDamage = baseDamage;
    }

    @Override
    public boolean canExecute(Unit unit, Object target) {
        if (!(target instanceof Unit)) {
            return false;
        }
        if (!unit.isAlive()) {
            return false;
        }

        Unit targetUnit = (Unit) target;

        // 检查目标是否存活
        if (!targetUnit.isAlive()) {
            return false;
        }

        // 检查行动力
        if (unit.getCurrentAp() < apCost) {
            return false;
        }

        // 检查是否是敌方单位
        if (!unit.getFaction().isHostileTo(targetUnit.getFaction())) {
            return false;
        }

        // 检查距离
        double distance = grid.getDistance(unit.getPosition(), targetUnit.getPosition());
        return distance <= range;
    }

    @Override
    public ActionResult execute(Unit unit, Object target) {
        if (!(target instanceof Unit)) {
            return ActionResult.failure("无效的目标");
        }

        Unit targetUnit = (Unit) target;

        if (!canExecute(unit, target)) {
            return ActionResult.failure("无法执行攻击");
        }

        // 消耗行动力
        unit.spendAp(apCost);

        // 计算伤害
        int damage = baseDamage + unit.getStats().getAttack();
        int actualDamage = targetUnit.takeDamage(damage);

        return ActionResult.success(
                "对 " + targetUnit.getName() + " 造成了 " + actualDamage + " 点伤害",
                apCost,
                0,
                (Object) actualDamage
        );
    }

    @Override
    public List<Coordinate> getValidTargets(Unit unit) {
        List<Coordinate> validTargets = new java.util.ArrayList<>();

        if (!unit.isAlive()) {
            return validTargets;
        }

        // 简单实现：检查范围内所有位置是否有敌方单位
        // 实际实现应该更高效
        Coordinate unitPos = unit.getPosition();
        for (int dx = -range; dx <= range; dx++) {
            for (int dy = -range; dy <= range; dy++) {
                if (dx == 0 && dy == 0) continue;

                // 这里简化处理，实际需要检查是否是有效的相邻位置
                // 对于六边形网格，需要使用六边形距离
            }
        }

        return validTargets;
    }
}
