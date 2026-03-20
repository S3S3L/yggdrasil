package io.github.s3s3l.yggdrasil.game.core.ai.combat;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 目标选择器
 * 根据策略选择战斗目标
 */
public class TargetSelector {

    /**
     * 选择最近的目标
     *
     * @param targets 目标列表
     * @return 最近的目标，如果列表为空则返回 null
     */
    public CombatTarget selectNearest(List<CombatTarget> targets) {
        if (targets.isEmpty()) {
            return null;
        }
        return targets.stream()
                .min(Comparator.comparing(CombatTarget::getDistance))
                .orElse(null);
    }

    /**
     * 选择生命百分比最低的目标
     *
     * @param targets 目标列表
     * @return 生命百分比最低的目标
     */
    public CombatTarget selectLowestHealth(List<CombatTarget> targets) {
        if (targets.isEmpty()) {
            return null;
        }
        return targets.stream()
                .min(Comparator.comparing(CombatTarget::getHealthPercent))
                .orElse(null);
    }

    /**
     * 选择威胁最高的目标
     *
     * @param targets 目标列表
     * @return 威胁最高的目标
     */
    public CombatTarget selectHighestThreat(List<CombatTarget> targets) {
        if (targets.isEmpty()) {
            return null;
        }
        return targets.stream()
                .max(Comparator.comparing(CombatTarget::getThreatLevel))
                .orElse(null);
    }

    /**
     * 选择伤害最高的目标
     *
     * @param targets 目标列表
     * @return 伤害最高的目标
     */
    public CombatTarget selectHighestDamage(List<CombatTarget> targets) {
        if (targets.isEmpty()) {
            return null;
        }
        return targets.stream()
                .max(Comparator.comparing(CombatTarget::getDamage))
                .orElse(null);
    }

    /**
     * 根据策略选择目标
     *
     * @param targets  目标列表
     * @param strategy 策略
     * @return 选中的目标
     */
    public CombatTarget selectTarget(List<CombatTarget> targets, TargetSelectionStrategy strategy) {
        if (targets.isEmpty()) {
            return null;
        }

        return switch (strategy) {
            case NEAREST -> selectNearest(targets);
            case WEAKEST -> selectLowestHealth(targets);
            case MOST_DANGEROUS -> selectHighestThreat(targets);
            case HIGHEST_DAMAGE -> selectHighestDamage(targets);
        };
    }

    /**
     * 按威胁排序所有目标
     *
     * @param targets 目标列表
     * @return 按威胁降序排序的目标列表
     */
    public List<CombatTarget> sortByThreat(List<CombatTarget> targets) {
        return targets.stream()
                .sorted(Comparator.comparing(CombatTarget::getThreatLevel).reversed())
                .collect(Collectors.toList());
    }

    /**
     * 过滤在攻击范围内的目标
     *
     * @param targets    目标列表
     * @param attackRange 攻击范围
     * @return 在范围内的目标列表
     */
    public List<CombatTarget> filterInRange(List<CombatTarget> targets, float attackRange) {
        return targets.stream()
                .filter(t -> t.isInRange(attackRange))
                .collect(Collectors.toList());
    }
}
