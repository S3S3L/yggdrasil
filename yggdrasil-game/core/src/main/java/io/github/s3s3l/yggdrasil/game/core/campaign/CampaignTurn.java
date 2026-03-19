package io.github.s3s3l.yggdrasil.game.core.campaign;

import io.github.s3s3l.yggdrasil.game.core.basic.ID;

import java.util.*;

/**
 * 战役回合
 * 表示战役中的一个回合
 */
public class CampaignTurn implements ID {

    private final long numericId;
    private final int turnNumber;
    private final Faction faction;
    private final TurnPhase phase;
    private final List<TurnAction> actions;
    private final TurnState state;

    private CampaignTurn(Builder builder) {
        this.numericId = builder.id;
        this.turnNumber = builder.turnNumber;
        this.faction = builder.faction;
        this.phase = builder.phase;
        this.actions = builder.actions;
        this.state = builder.state;
    }

    @Override
    public long getId() {
        return numericId;
    }

    @Override
    public void setId(long id) {
        // ID is immutable
    }

    /**
     * 获取回合编号
     *
     * @return 回合编号
     */
    public int getTurnNumber() {
        return turnNumber;
    }

    /**
     * 获取所属势力
     *
     * @return 势力
     */
    public Faction getFaction() {
        return faction;
    }

    /**
     * 获取当前阶段
     *
     * @return 回合阶段
     */
    public TurnPhase getPhase() {
        return phase;
    }

    /**
     * 获取所有行动
     *
     * @return 行动列表
     */
    public List<TurnAction> getActions() {
        return Collections.unmodifiableList(actions);
    }

    /**
     * 添加行动
     *
     * @param action 行动
     */
    public void addAction(TurnAction action) {
        actions.add(action);
    }

    /**
     * 获取回合状态
     *
     * @return 回合状态
     */
    public TurnState getState() {
        return state;
    }

    /**
     * 检查回合是否结束
     *
     * @return true 如果回合已结束
     */
    public boolean isFinished() {
        return state == TurnState.FINISHED;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CampaignTurn that = (CampaignTurn) o;
        return Objects.equals(numericId, that.numericId);
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
        private int turnNumber;
        private Faction faction;
        private TurnPhase phase = TurnPhase.START;
        private List<TurnAction> actions = new ArrayList<>();
        private TurnState state = TurnState.IN_PROGRESS;

        public Builder id(long id) {
            this.id = id;
            return this;
        }

        public Builder turnNumber(int turnNumber) {
            this.turnNumber = turnNumber;
            return this;
        }

        public Builder faction(Faction faction) {
            this.faction = faction;
            return this;
        }

        public Builder phase(TurnPhase phase) {
            this.phase = phase;
            return this;
        }

        public Builder actions(List<TurnAction> actions) {
            this.actions = actions;
            return this;
        }

        public Builder state(TurnState state) {
            this.state = state;
            return this;
        }

        public CampaignTurn build() {
            return new CampaignTurn(this);
        }
    }
}
