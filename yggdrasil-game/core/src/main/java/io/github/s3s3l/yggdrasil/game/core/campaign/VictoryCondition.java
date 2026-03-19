package io.github.s3s3l.yggdrasil.game.core.campaign;

/**
 * 胜利条件接口
 */
public interface VictoryCondition {

    /**
     * 检查势力是否达成胜利
     *
     * @param campaign 战役
     * @param faction  势力
     * @return true 如果达成胜利
     */
    boolean isVictoryAchieved(Campaign campaign, Faction faction);

    /**
     * 获取胜利条件描述
     *
     * @return 描述
     */
    String getDescription();
}
