package io.github.s3s3l.yggdrasil.game.core.campaign;

import io.github.s3s3l.yggdrasil.game.core.basic.ID;

import java.util.Objects;

/**
 * 势力关系
 */
public class FactionRelation {

    /**
     * 势力 ID
     */
    private final long factionId;

    /**
     * 外交状态
     */
    private final DiplomaticState state;

    /**
     * 好感度 (-100 到 100)
     */
    private final int opinion;

    /**
     * 贸易协定
     */
    private final boolean tradeAgreement;

    /**
     * 互不侵犯条约
     */
    private final boolean nonAggressionPact;

    /**
     * 联盟
     */
    private final boolean alliance;

    public FactionRelation(long factionId, DiplomaticState state, int opinion,
                           boolean tradeAgreement, boolean nonAggressionPact, boolean alliance) {
        this.factionId = factionId;
        this.state = state;
        this.opinion = opinion;
        this.tradeAgreement = tradeAgreement;
        this.nonAggressionPact = nonAggressionPact;
        this.alliance = alliance;
    }

    public long getFactionId() {
        return factionId;
    }

    public DiplomaticState getState() {
        return state;
    }

    public int getOpinion() {
        return opinion;
    }

    public boolean getTradeAgreement() {
        return tradeAgreement;
    }

    public boolean getNonAggressionPact() {
        return nonAggressionPact;
    }

    public boolean getAlliance() {
        return alliance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FactionRelation that = (FactionRelation) o;
        return factionId == that.factionId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(factionId);
    }
}
