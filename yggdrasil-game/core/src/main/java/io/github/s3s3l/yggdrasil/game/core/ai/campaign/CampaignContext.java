package io.github.s3s3l.yggdrasil.game.core.ai.campaign;

import java.util.*;

/**
 * 战役上下文
 * 存储战役 AI 执行过程中的共享数据
 */
public class CampaignContext {

    private Map<String, Integer> resources;
    private List<Long> controlledRegions;
    private List<Long> enemyRegions;
    private List<Long> neutralRegions;
    private List<Long> adjacentEnemies;
    private float strategicScore;
    private StrategicType currentStrategy;
    private Map<Long, Float> diplomaticRelations;
    private List<String> availableTechnologies;
    private List<String> availableBuildings;

    public CampaignContext() {
        this.resources = new HashMap<>();
        this.controlledRegions = new ArrayList<>();
        this.enemyRegions = new ArrayList<>();
        this.neutralRegions = new ArrayList<>();
        this.adjacentEnemies = new ArrayList<>();
        this.strategicScore = 50f;
        this.currentStrategy = StrategicType.CONSOLIDATION;
        this.diplomaticRelations = new HashMap<>();
        this.availableTechnologies = new ArrayList<>();
        this.availableBuildings = new ArrayList<>();
    }

    public Map<String, Integer> getResources() {
        return resources;
    }

    public void setResources(Map<String, Integer> resources) {
        this.resources = resources;
    }

    public int getResource(String type) {
        return resources.getOrDefault(type, 0);
    }

    public void setResource(String type, int amount) {
        resources.put(type, amount);
    }

    public List<Long> getControlledRegions() {
        return controlledRegions;
    }

    public void setControlledRegions(List<Long> controlledRegions) {
        this.controlledRegions = controlledRegions;
    }

    public List<Long> getEnemyRegions() {
        return enemyRegions;
    }

    public void setEnemyRegions(List<Long> enemyRegions) {
        this.enemyRegions = enemyRegions;
    }

    public List<Long> getNeutralRegions() {
        return neutralRegions;
    }

    public void setNeutralRegions(List<Long> neutralRegions) {
        this.neutralRegions = neutralRegions;
    }

    public List<Long> getAdjacentEnemies() {
        return adjacentEnemies;
    }

    public void setAdjacentEnemies(List<Long> adjacentEnemies) {
        this.adjacentEnemies = adjacentEnemies;
    }

    public float getStrategicScore() {
        return strategicScore;
    }

    public void setStrategicScore(float strategicScore) {
        this.strategicScore = strategicScore;
    }

    public StrategicType getCurrentStrategy() {
        return currentStrategy;
    }

    public void setCurrentStrategy(StrategicType currentStrategy) {
        this.currentStrategy = currentStrategy;
    }

    public Map<Long, Float> getDiplomaticRelations() {
        return diplomaticRelations;
    }

    public void setDiplomaticRelations(Map<Long, Float> diplomaticRelations) {
        this.diplomaticRelations = diplomaticRelations;
    }

    public float getDiplomaticRelation(long factionId) {
        return diplomaticRelations.getOrDefault(factionId, 0f);
    }

    public void setDiplomaticRelation(long factionId, float value) {
        diplomaticRelations.put(factionId, value);
    }

    public List<String> getAvailableTechnologies() {
        return availableTechnologies;
    }

    public void setAvailableTechnologies(List<String> availableTechnologies) {
        this.availableTechnologies = availableTechnologies;
    }

    public List<String> getAvailableBuildings() {
        return availableBuildings;
    }

    public void setAvailableBuildings(List<String> availableBuildings) {
        this.availableBuildings = availableBuildings;
    }

    /**
     * 清空上下文
     */
    public void clear() {
        resources.clear();
        controlledRegions.clear();
        enemyRegions.clear();
        neutralRegions.clear();
        adjacentEnemies.clear();
        strategicScore = 50f;
        currentStrategy = StrategicType.CONSOLIDATION;
        diplomaticRelations.clear();
        availableTechnologies.clear();
        availableBuildings.clear();
    }
}
