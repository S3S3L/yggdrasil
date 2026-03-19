package io.github.s3s3l.yggdrasil.game.core.campaign;

import io.github.s3s3l.yggdrasil.game.core.basic.ID;

import java.util.*;

/**
 * 外交系统
 * 管理势力间的外交关系
 */
public class Diplomacy implements ID {

    private final long numericId;
    private final Map<Long, FactionRelation> relations;
    private final DiplomacyConfig config;

    private Diplomacy(Builder builder) {
        this.numericId = builder.id;
        this.relations = builder.relations;
        this.config = builder.config;
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
     * 获取所有关系
     *
     * @return 关系映射
     */
    public Map<Long, FactionRelation> getRelations() {
        return Collections.unmodifiableMap(relations);
    }

    /**
     * 获取与指定势力的关系
     *
     * @param factionId 势力 ID
     * @return 关系对象
     */
    public FactionRelation getRelation(long factionId) {
        return relations.get(factionId);
    }

    /**
     * 设置与指定势力的关系
     *
     * @param factionId 势力 ID
     * @param relation  关系对象
     */
    public void setRelation(long factionId, FactionRelation relation) {
        relations.put(factionId, relation);
    }

    /**
     * 获取外交状态
     *
     * @param factionId 势力 ID
     * @return 外交状态
     */
    public DiplomaticState getState(long factionId) {
        FactionRelation relation = relations.get(factionId);
        return relation != null ? relation.getState() : DiplomaticState.NEUTRAL;
    }

    /**
     * 设置外交状态
     *
     * @param factionId 势力 ID
     * @param state     外交状态
     */
    public void setState(long factionId, DiplomaticState state) {
        relations.computeIfPresent(factionId, (id, relation) ->
                new FactionRelation(
                        relation.getFactionId(),
                        state,
                        relation.getOpinion(),
                        relation.getTradeAgreement(),
                        relation.getNonAggressionPact(),
                        relation.getAlliance()
                ));
    }

    /**
     * 获取好感度
     *
     * @param factionId 势力 ID
     * @return 好感度 (-100 到 100)
     */
    public int getOpinion(long factionId) {
        FactionRelation relation = relations.get(factionId);
        return relation != null ? relation.getOpinion() : 0;
    }

    /**
     * 设置好感度
     *
     * @param factionId 势力 ID
     * @param opinion   好感度
     */
    public void setOpinion(long factionId, int opinion) {
        relations.computeIfPresent(factionId, (id, relation) ->
                new FactionRelation(
                        relation.getFactionId(),
                        relation.getState(),
                        Math.max(-100, Math.min(100, opinion)),
                        relation.getTradeAgreement(),
                        relation.getNonAggressionPact(),
                        relation.getAlliance()
                ));
    }

    /**
     * 修改好感度
     *
     * @param factionId 势力 ID
     * @param delta     变化值
     */
    public void modifyOpinion(long factionId, int delta) {
        FactionRelation relation = relations.get(factionId);
        if (relation != null) {
            setOpinion(factionId, relation.getOpinion() + delta);
        }
    }

    /**
     * 检查是否有贸易协定
     *
     * @param factionId 势力 ID
     * @return true 如果有贸易协定
     */
    public boolean hasTradeAgreement(long factionId) {
        FactionRelation relation = relations.get(factionId);
        return relation != null && relation.getTradeAgreement();
    }

    /**
     * 设置贸易协定
     *
     * @param factionId 势力 ID
     * @param agreed    是否达成贸易协定
     */
    public void setTradeAgreement(long factionId, boolean agreed) {
        relations.computeIfPresent(factionId, (id, relation) ->
                new FactionRelation(
                        relation.getFactionId(),
                        relation.getState(),
                        relation.getOpinion(),
                        agreed,
                        relation.getNonAggressionPact(),
                        relation.getAlliance()
                ));
    }

    /**
     * 检查是否有互不侵犯条约
     *
     * @param factionId 势力 ID
     * @return true 如果有互不侵犯条约
     */
    public boolean hasNonAggressionPact(long factionId) {
        FactionRelation relation = relations.get(factionId);
        return relation != null && relation.getNonAggressionPact();
    }

    /**
     * 设置互不侵犯条约
     *
     * @param factionId 势力 ID
     * @param agreed    是否达成互不侵犯条约
     */
    public void setNonAggressionPact(long factionId, boolean agreed) {
        relations.computeIfPresent(factionId, (id, relation) ->
                new FactionRelation(
                        relation.getFactionId(),
                        relation.getState(),
                        relation.getOpinion(),
                        relation.getTradeAgreement(),
                        agreed,
                        relation.getAlliance()
                ));
    }

    /**
     * 检查是否结盟
     *
     * @param factionId 势力 ID
     * @return true 如果结盟
     */
    public boolean isAllied(long factionId) {
        FactionRelation relation = relations.get(factionId);
        return relation != null && relation.getAlliance();
    }

    /**
     * 设置结盟状态
     *
     * @param factionId 势力 ID
     * @param allied    是否结盟
     */
    public void setAlliance(long factionId, boolean allied) {
        relations.computeIfPresent(factionId, (id, relation) ->
                new FactionRelation(
                        relation.getFactionId(),
                        relation.getState(),
                        relation.getOpinion(),
                        relation.getTradeAgreement(),
                        relation.getNonAggressionPact(),
                        allied
                ));
    }

    /**
     * 检查是否处于战争状态
     *
     * @param factionId 势力 ID
     * @return true 如果处于战争
     */
    public boolean isAtWar(long factionId) {
        DiplomaticState state = getState(factionId);
        return state == DiplomaticState.WAR || state == DiplomaticState.HOSTILE;
    }

    /**
     * 宣战
     *
     * @param factionId 势力 ID
     */
    public void declareWar(long factionId) {
        setState(factionId, DiplomaticState.WAR);
        setOpinion(factionId, -100);
        setTradeAgreement(factionId, false);
        setNonAggressionPact(factionId, false);
        setAlliance(factionId, false);
    }

    /**
     * 议和
     *
     * @param factionId 势力 ID
     */
    public void makePeace(long factionId) {
        setState(factionId, DiplomaticState.NEUTRAL);
        setOpinion(factionId, 0);
    }

    /**
     * 获取外交配置
     *
     * @return 外交配置
     */
    public DiplomacyConfig getConfig() {
        return config;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private long id;
        private Map<Long, FactionRelation> relations = new HashMap<>();
        private DiplomacyConfig config = new DiplomacyConfig();

        public Builder id(long id) {
            this.id = id;
            return this;
        }

        public Builder relations(Map<Long, FactionRelation> relations) {
            this.relations = relations;
            return this;
        }

        public Builder config(DiplomacyConfig config) {
            this.config = config;
            return this;
        }

        public Diplomacy build() {
            return new Diplomacy(this);
        }
    }
}
