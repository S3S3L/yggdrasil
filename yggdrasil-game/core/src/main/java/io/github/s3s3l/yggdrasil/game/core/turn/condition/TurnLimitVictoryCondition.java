package io.github.s3s3l.yggdrasil.game.core.turn.condition;

import io.github.s3s3l.yggdrasil.game.core.turn.TurnManager;
import io.github.s3s3l.yggdrasil.game.core.turn.VictoryCondition;
import io.github.s3s3l.yggdrasil.game.core.unit.Unit;

import java.util.List;

/**
 * 回合数限制胜利条件
 * 在指定回合数内达成目标获胜
 */
public class TurnLimitVictoryCondition implements VictoryCondition {

    private final int maxTurns;
    private final long targetPlayerId;

    /**
     * 创建回合数限制胜利条件
     *
     * @param maxTurns       最大回合数
     * @param targetPlayerId 目标玩家 ID
     */
    public TurnLimitVictoryCondition(int maxTurns, long targetPlayerId) {
        this.maxTurns = maxTurns;
        this.targetPlayerId = targetPlayerId;
    }

    @Override
    public String getName() {
        return "时间限制";
    }

    @Override
    public String getDescription() {
        return "在 " + maxTurns + " 回合内达成目标";
    }

    @Override
    public VictoryResult check(List<Unit> units, TurnManager turnManager) {
        if (turnManager.getCurrentTurn() > maxTurns) {
            // 超过回合数限制，当前回合玩家失败
            if (turnManager.getCurrentTurnOwnerId() == targetPlayerId) {
                return VictoryResult.victory(-1, "超过回合数限制，任务失败");
            } else {
                return VictoryResult.victory(targetPlayerId, "超过回合数限制，防守方获胜");
            }
        }
        return VictoryResult.none();
    }

    /**
     * 获取剩余回合数
     *
     * @param turnManager 回合管理器
     * @return 剩余回合数
     */
    public int getRemainingTurns(TurnManager turnManager) {
        return Math.max(0, maxTurns - turnManager.getCurrentTurn());
    }
}
