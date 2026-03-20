package io.github.s3s3l.yggdrasil.game.core.ai.campaign;

/**
 * 战役决策
 * 表示战役 AI 的决策结果
 */
public class CampaignDecision {

    private final StrategicType type;
    private final long targetRegionId;
    private final String action;
    private final String reason;

    private CampaignDecision(Builder builder) {
        this.type = builder.type;
        this.targetRegionId = builder.targetRegionId;
        this.action = builder.action;
        this.reason = builder.reason;
    }

    public StrategicType getType() {
        return type;
    }

    public long getTargetRegionId() {
        return targetRegionId;
    }

    public String getAction() {
        return action;
    }

    public String getReason() {
        return reason;
    }

    public boolean hasTarget() {
        return targetRegionId != 0;
    }

    public boolean hasAction() {
        return action != null && !action.isEmpty();
    }

    @Override
    public String toString() {
        return "CampaignDecision{" +
                "type=" + type +
                ", targetRegionId=" + targetRegionId +
                ", action='" + action + '\'' +
                ", reason='" + reason + '\'' +
                '}';
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private StrategicType type = StrategicType.CONSOLIDATION;
        private long targetRegionId;
        private String action = "";
        private String reason = "";

        public Builder type(StrategicType type) {
            this.type = type;
            return this;
        }

        public Builder targetRegionId(long targetRegionId) {
            this.targetRegionId = targetRegionId;
            return this;
        }

        public Builder action(String action) {
            this.action = action;
            return this;
        }

        public Builder reason(String reason) {
            this.reason = reason;
            return this;
        }

        public CampaignDecision build() {
            return new CampaignDecision(this);
        }
    }
}
