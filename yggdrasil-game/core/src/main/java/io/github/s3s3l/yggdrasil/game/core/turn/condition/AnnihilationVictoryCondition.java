package io.github.s3s3l.yggdrasil.game.core.turn.condition;

import io.github.s3s3l.yggdrasil.game.core.turn.TurnManager;
import io.github.s3s3l.yggdrasil.game.core.turn.VictoryCondition;
import io.github.s3s3l.yggdrasil.game.core.unit.Faction;
import io.github.s3s3l.yggdrasil.game.core.unit.Unit;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 歼灭胜利条件
 * 消灭所有敌方单位即获胜
 */
public class AnnihilationVictoryCondition implements VictoryCondition {

    private final Faction targetFaction;

    /**
     * 创建歼灭胜利条件
     *
     * @param targetFaction 需要消灭的目标阵营
     */
    public AnnihilationVictoryCondition(Faction targetFaction) {
        this.targetFaction = targetFaction;
    }

    @Override
    public String getName() {
        return "歼灭战";
    }

    @Override
    public String getDescription() {
        return "消灭所有敌方单位";
    }

    @Override
    public VictoryResult check(List<Unit> units, TurnManager turnManager) {
        // 检查目标阵营是否还有存活的单位
        boolean hasAliveTarget = units.stream()
                .filter(unit -> unit.getFaction() == targetFaction)
                .anyMatch(Unit::isAlive);

        if (!hasAliveTarget) {
            // 目标阵营全灭，检查哪个阵营获胜
            // 简化处理：玩家和盟友获胜
            return VictoryResult.victory(-1, targetFaction + " 已被全灭");
        }

        return VictoryResult.none();
    }
}
