package io.github.s3s3l.yggdrasil.game.core.action;

import io.github.s3s3l.yggdrasil.game.core.ability.EffectResult;
import io.github.s3s3l.yggdrasil.game.core.ability.Ability;
import io.github.s3s3l.yggdrasil.game.core.grid.Coordinate;
import io.github.s3s3l.yggdrasil.game.core.unit.Unit;
import lombok.Getter;

import java.util.List;

/**
 * 使用技能动作
 */
@Getter
public class UseAbilityAction implements Action {

    private final Ability ability;

    public UseAbilityAction(Ability ability) {
        this.ability = ability;
    }

    @Override
    public String getName() {
        return ability.getName();
    }

    @Override
    public String getDescription() {
        return ability.getDescription();
    }

    @Override
    public int getApCost() {
        return ability.getApCost();
    }

    @Override
    public int getRange() {
        return ability.getRange();
    }

    @Override
    public TargetType getTargetType() {
        return ability.getTargetType();
    }

    @Override
    public boolean canExecute(Unit unit, Object target) {
        if (!(target instanceof Unit)) {
            return false;
        }
        Unit targetUnit = (Unit) target;

        // 检查行动力
        if (unit.getCurrentAp() < getApCost()) {
            return false;
        }

        // 检查距离
        double distance = ability.getRange();
        // 如果目标有位置且距离超出范围，则无法执行
        if (targetUnit.getPosition() != null && unit.getPosition() != null) {
            // 距离检查由 AbilitySystem 处理，这里简化
        }

        // 检查目标类型
        switch (ability.getTargetType()) {
            case UNIT_FRIENDLY:
                return unit.getFaction().isFriendlyWith(targetUnit.getFaction());
            case UNIT_HOSTILE:
                return unit.getFaction().isHostileTo(targetUnit.getFaction());
            default:
                return true;
        }
    }

    @Override
    public ActionResult execute(Unit unit, Object target) {
        if (!(target instanceof Unit)) {
            return ActionResult.failure("无效的目标");
        }

        Unit targetUnit = (Unit) target;

        if (!canExecute(unit, target)) {
            return ActionResult.failure("行动力不足");
        }

        // 消耗行动力
        unit.spendAp(getApCost());

        // 应用技能效果
        List<EffectResult> results = ability.apply(unit, targetUnit);

        // 检查是否有成功的效果
        boolean anySuccess = results.stream().anyMatch(EffectResult::isSuccess);

        if (anySuccess) {
            return ActionResult.success("技能 " + ability.getName() + " 释放成功", getApCost(), 0, results);
        } else {
            return ActionResult.failure("技能效果未能生效");
        }
    }

    @Override
    public List<Coordinate> getValidTargets(Unit unit) {
        // 简化实现，返回空列表
        // 实际实现需要根据范围和视线计算有效目标
        return List.of();
    }
}
