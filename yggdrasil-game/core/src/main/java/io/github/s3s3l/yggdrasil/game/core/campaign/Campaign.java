package io.github.s3s3l.yggdrasil.game.core.campaign;

import io.github.s3s3l.yggdrasil.game.core.basic.ID;

import java.util.*;

/**
 * 战役
 * 管理整个战役流程，包括多个回合和势力
 */
public class Campaign implements ID {

    private final long numericId;
    private final String name;
    private final String description;
    private final CampaignMap map;
    private final List<CampaignTurn> turns;
    private CampaignTurn currentTurn;
    private final CampaignState state;
    private final int currentTurnNumber;
    private final VictoryCondition victoryCondition;
    private final Faction winner;

    private Campaign(Builder builder) {
        this.numericId = builder.id;
        this.name = builder.name;
        this.description = builder.description;
        this.map = builder.map;
        this.turns = builder.turns;
        this.currentTurn = builder.currentTurn;
        this.state = builder.state;
        this.currentTurnNumber = builder.currentTurnNumber;
        this.victoryCondition = builder.victoryCondition;
        this.winner = builder.winner;
    }

    @Override
    public long getId() {
        return numericId;
    }

    @Override
    public void setId(long id) {
        // ID is immutable
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    /**
     * 获取战役地图
     *
     * @return 战役地图
     */
    public CampaignMap getMap() {
        return map;
    }

    /**
     * 获取所有回合
     *
     * @return 回合列表
     */
    public List<CampaignTurn> getTurns() {
        return Collections.unmodifiableList(turns);
    }

    /**
     * 获取当前回合
     *
     * @return 当前回合
     */
    public CampaignTurn getCurrentTurn() {
        return currentTurn;
    }

    /**
     * 获取战役状态
     *
     * @return 战役状态
     */
    public CampaignState getState() {
        return state;
    }

    /**
     * 获取当前回合数
     *
     * @return 回合数
     */
    public int getCurrentTurnNumber() {
        return currentTurnNumber;
    }

    /**
     * 获取胜利条件
     *
     * @return 胜利条件
     */
    public VictoryCondition getVictoryCondition() {
        return victoryCondition;
    }

    /**
     * 获取胜利者
     *
     * @return 胜利者势力，未结束返回 null
     */
    public Faction getWinner() {
        return winner;
    }

    /**
     * 检查战役是否结束
     *
     * @return true 如果战役已结束
     */
    public boolean isFinished() {
        return state == CampaignState.FINISHED;
    }

    /**
     * 检查势力是否满足胜利条件
     *
     * @param faction 势力
     * @return true 如果满足胜利条件
     */
    public boolean checkVictory(Faction faction) {
        if (victoryCondition == null) {
            return false;
        }
        return victoryCondition.isVictoryAchieved(this, faction);
    }

    /**
     * 结束战役
     *
     * @param winner 胜利者
     */
    public void endCampaign(Faction winner) {
        // 战役结束逻辑由外部管理，此方法仅用于状态记录
    }

    /**
     * 获取指定势力的回合
     *
     * @param factionId 势力 ID
     * @return 回合列表
     */
    public List<CampaignTurn> getTurnsForFaction(long factionId) {
        List<CampaignTurn> result = new ArrayList<>();
        for (CampaignTurn turn : turns) {
            if (turn.getFaction().getId() == factionId) {
                result.add(turn);
            }
        }
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Campaign campaign = (Campaign) o;
        return numericId == campaign.numericId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(numericId);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private long id;
        private String name;
        private String description = "";
        private CampaignMap map;
        private List<CampaignTurn> turns = new ArrayList<>();
        private CampaignTurn currentTurn;
        private CampaignState state = CampaignState.IN_PROGRESS;
        private int currentTurnNumber = 0;
        private VictoryCondition victoryCondition;
        private Faction winner;

        public Builder id(long id) {
            this.id = id;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder map(CampaignMap map) {
            this.map = map;
            return this;
        }

        public Builder turns(List<CampaignTurn> turns) {
            this.turns = turns;
            return this;
        }

        public Builder currentTurn(CampaignTurn currentTurn) {
            this.currentTurn = currentTurn;
            return this;
        }

        public Builder state(CampaignState state) {
            this.state = state;
            return this;
        }

        public Builder currentTurnNumber(int currentTurnNumber) {
            this.currentTurnNumber = currentTurnNumber;
            return this;
        }

        public Builder victoryCondition(VictoryCondition victoryCondition) {
            this.victoryCondition = victoryCondition;
            return this;
        }

        public Builder winner(Faction winner) {
            this.winner = winner;
            return this;
        }

        public Campaign build() {
            return new Campaign(this);
        }
    }
}
