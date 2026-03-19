package io.github.s3s3l.yggdrasil.game.core.campaign;

/**
 * 外交配置
 */
public class DiplomacyConfig {

    /**
     * 默认好感度变化衰减率
     */
    private double opinionDecayRate;

    /**
     * 默认好感度上限
     */
    private int maxOpinion;

    /**
     * 默认好感度下限
     */
    private int minOpinion;

    /**
     * 宣战所需最低好感度
     */
    private int warThreshold;

    /**
     * 结盟所需最低好感度
     */
    private int allianceThreshold;

    public DiplomacyConfig() {
        this.opinionDecayRate = 0.1;
        this.maxOpinion = 100;
        this.minOpinion = -100;
        this.warThreshold = -50;
        this.allianceThreshold = 75;
    }

    public double getOpinionDecayRate() {
        return opinionDecayRate;
    }

    public void setOpinionDecayRate(double opinionDecayRate) {
        this.opinionDecayRate = opinionDecayRate;
    }

    public int getMaxOpinion() {
        return maxOpinion;
    }

    public void setMaxOpinion(int maxOpinion) {
        this.maxOpinion = maxOpinion;
    }

    public int getMinOpinion() {
        return minOpinion;
    }

    public void setMinOpinion(int minOpinion) {
        this.minOpinion = minOpinion;
    }

    public int getWarThreshold() {
        return warThreshold;
    }

    public void setWarThreshold(int warThreshold) {
        this.warThreshold = warThreshold;
    }

    public int getAllianceThreshold() {
        return allianceThreshold;
    }

    public void setAllianceThreshold(int allianceThreshold) {
        this.allianceThreshold = allianceThreshold;
    }
}
