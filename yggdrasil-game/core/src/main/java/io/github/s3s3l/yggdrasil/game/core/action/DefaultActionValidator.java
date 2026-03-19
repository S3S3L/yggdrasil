package io.github.s3s3l.yggdrasil.game.core.action;

import io.github.s3s3l.yggdrasil.game.core.grid.Coordinate;
import io.github.s3s3l.yggdrasil.game.core.unit.Unit;

import java.util.List;

/**
 * 默认动作验证器实现
 */
public class DefaultActionValidator implements ActionValidator {

    @Override
    public ValidationResult validate(Action action, Unit unit, Object target) {
        // 检查单位是否存活
        if (unit == null || !unit.isAlive()) {
            return ValidationResult.invalid("单位未存活");
        }

        // 检查行动力
        if (unit.getCurrentAp() < action.getApCost()) {
            return ValidationResult.invalid("行动力不足 (需要 " + action.getApCost() + ", 当前 " + unit.getCurrentAp() + ")");
        }

        // 检查目标是否为空
        if (target == null && action.getTargetType() != Action.TargetType.NONE) {
            return ValidationResult.invalid("目标不能为空");
        }

        // 根据目标类型验证
        if (target instanceof Unit) {
            Unit targetUnit = (Unit) target;
            switch (action.getTargetType()) {
                case UNIT_FRIENDLY:
                    if (!unit.getFaction().isFriendlyWith(targetUnit.getFaction())) {
                        return ValidationResult.invalid("目标必须是友方单位");
                    }
                    break;
                case UNIT_HOSTILE:
                    if (!unit.getFaction().isHostileTo(targetUnit.getFaction())) {
                        return ValidationResult.invalid("目标必须是敌方单位");
                    }
                    break;
                case UNIT_ANY:
                    // 任何单位都可以
                    break;
                case POSITION:
                    return ValidationResult.invalid("该动作需要地面位置作为目标");
            }

            // 检查目标单位是否存活（如果需要）
            if (!targetUnit.isAlive() && action.getTargetType() != Action.TargetType.UNIT_ANY) {
                return ValidationResult.invalid("目标单位已死亡");
            }
        } else if (target instanceof Coordinate) {
            if (action.getTargetType() != Action.TargetType.POSITION &&
                action.getTargetType() != Action.TargetType.POSITION_OR_UNIT) {
                return ValidationResult.invalid("该动作需要单位作为目标");
            }
        }

        // 检查距离
        if (target instanceof Coordinate) {
            Coordinate targetCoord = (Coordinate) target;
            // 距离检查由具体动作处理
        } else if (target instanceof Unit) {
            Unit targetUnit = (Unit) target;
            // 距离检查由具体动作处理
        }

        return ValidationResult.valid();
    }
}
