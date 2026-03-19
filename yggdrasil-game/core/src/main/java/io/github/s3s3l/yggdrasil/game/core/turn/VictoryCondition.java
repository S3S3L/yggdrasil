package io.github.s3s3l.yggdrasil.game.core.turn;

import io.github.s3s3l.yggdrasil.game.core.unit.Faction;
import io.github.s3s3l.yggdrasil.game.core.unit.Unit;

import java.util.Collection;
import java.util.List;

/**
 * 胜利条件接口
 * 定义游戏胜利/失败的条件
 */
public interface VictoryCondition {

    /**
     * 获取条件名称
     *
     * @return 名称
     */
    String getName();

    /**
     * 获取条件描述
     *
     * @return 描述
     */
    String getDescription();

    /**
     * 检查是否满足胜利条件
     *
     * @param units 所有单位
     * @param turnManager 回合管理器
     * @return 胜利检查结果
     */
    VictoryResult check(List<Unit> units, TurnManager turnManager);

    /**
     * 胜利检查结果
     */
    class VictoryResult {
        /**
         * 是否满足条件
         */
        private final boolean met;

        /**
         * 胜利者 ID（-1 表示平局或未确定）
         */
        private final long winnerId;

        /**
         * 胜利原因
         */
        private final String reason;

        public VictoryResult(boolean met, long winnerId, String reason) {
            this.met = met;
            this.winnerId = winnerId;
            this.reason = reason;
        }

        public boolean isMet() {
            return met;
        }

        public long getWinnerId() {
            return winnerId;
        }

        public String getReason() {
            return reason;
        }

        public static VictoryResult none() {
            return new VictoryResult(false, -1, null);
        }

        public static VictoryResult victory(long winnerId, String reason) {
            return new VictoryResult(true, winnerId, reason);
        }

        public static VictoryResult draw(String reason) {
            return new VictoryResult(true, -1, reason);
        }
    }
}
