package io.github.s3s3l.yggdrasil.game.core.ai.combat;

import java.util.ArrayList;
import java.util.List;

/**
 * 战斗上下文
 * 存储战斗 AI 执行过程中的共享数据
 */
public class CombatContext {

    private List<CombatTarget> visibleEnemies;
    private List<CombatTarget> visibleAllies;
    private float forceAdvantage;
    private float positionAdvantage;
    private float healthAdvantage;
    private float numberAdvantage;
    private float tacticalScore;
    private CombatTarget currentTarget;
    private TargetSelectionStrategy targetSelectionStrategy;

    public CombatContext() {
        this.visibleEnemies = new ArrayList<>();
        this.visibleAllies = new ArrayList<>();
        this.forceAdvantage = 50f;
        this.positionAdvantage = 50f;
        this.healthAdvantage = 50f;
        this.numberAdvantage = 50f;
        this.tacticalScore = 50f;
        this.targetSelectionStrategy = TargetSelectionStrategy.MOST_DANGEROUS;
    }

    public List<CombatTarget> getVisibleEnemies() {
        return visibleEnemies;
    }

    public void setVisibleEnemies(List<CombatTarget> visibleEnemies) {
        this.visibleEnemies = visibleEnemies;
    }

    public List<CombatTarget> getVisibleAllies() {
        return visibleAllies;
    }

    public void setVisibleAllies(List<CombatTarget> visibleAllies) {
        this.visibleAllies = visibleAllies;
    }

    public float getForceAdvantage() {
        return forceAdvantage;
    }

    public void setForceAdvantage(float forceAdvantage) {
        this.forceAdvantage = forceAdvantage;
    }

    public float getPositionAdvantage() {
        return positionAdvantage;
    }

    public void setPositionAdvantage(float positionAdvantage) {
        this.positionAdvantage = positionAdvantage;
    }

    public float getHealthAdvantage() {
        return healthAdvantage;
    }

    public void setHealthAdvantage(float healthAdvantage) {
        this.healthAdvantage = healthAdvantage;
    }

    public float getNumberAdvantage() {
        return numberAdvantage;
    }

    public void setNumberAdvantage(float numberAdvantage) {
        this.numberAdvantage = numberAdvantage;
    }

    public float getTacticalScore() {
        return tacticalScore;
    }

    public void setTacticalScore(float tacticalScore) {
        this.tacticalScore = tacticalScore;
    }

    public CombatTarget getCurrentTarget() {
        return currentTarget;
    }

    public void setCurrentTarget(CombatTarget currentTarget) {
        this.currentTarget = currentTarget;
    }

    public TargetSelectionStrategy getTargetSelectionStrategy() {
        return targetSelectionStrategy;
    }

    public void setTargetSelectionStrategy(TargetSelectionStrategy targetSelectionStrategy) {
        this.targetSelectionStrategy = targetSelectionStrategy;
    }

    /**
     * 清空上下文
     */
    public void clear() {
        visibleEnemies.clear();
        visibleAllies.clear();
        forceAdvantage = 50f;
        positionAdvantage = 50f;
        healthAdvantage = 50f;
        numberAdvantage = 50f;
        tacticalScore = 50f;
        currentTarget = null;
    }
}
