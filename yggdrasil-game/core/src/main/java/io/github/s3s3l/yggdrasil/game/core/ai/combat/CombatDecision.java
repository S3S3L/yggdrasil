package io.github.s3s3l.yggdrasil.game.core.ai.combat;

/**
 * 战斗决策
 * 表示一个战斗 AI 的决策结果
 */
public class CombatDecision {

    private final TacticalType type;
    private final CombatTarget target;
    private final int targetPositionX;
    private final int targetPositionY;
    private final String skillId;
    private final String reason;

    private CombatDecision(Builder builder) {
        this.type = builder.type;
        this.target = builder.target;
        this.targetPositionX = builder.targetPositionX;
        this.targetPositionY = builder.targetPositionY;
        this.skillId = builder.skillId;
        this.reason = builder.reason;
    }

    public TacticalType getType() {
        return type;
    }

    public CombatTarget getTarget() {
        return target;
    }

    public int getTargetPositionX() {
        return targetPositionX;
    }

    public int getTargetPositionY() {
        return targetPositionY;
    }

    public String getSkillId() {
        return skillId;
    }

    public String getReason() {
        return reason;
    }

    public boolean hasTarget() {
        return target != null;
    }

    public boolean hasSkill() {
        return skillId != null && !skillId.isEmpty();
    }

    public boolean hasPosition() {
        return targetPositionX != 0 || targetPositionY != 0;
    }

    @Override
    public String toString() {
        return "CombatDecision{" +
                "type=" + type +
                ", target=" + (target != null ? target.getName() : "none") +
                ", skill='" + skillId + '\'' +
                ", reason='" + reason + '\'' +
                '}';
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private TacticalType type = TacticalType.WAIT;
        private CombatTarget target;
        private int targetPositionX;
        private int targetPositionY;
        private String skillId = "";
        private String reason = "";

        public Builder type(TacticalType type) {
            this.type = type;
            return this;
        }

        public Builder target(CombatTarget target) {
            this.target = target;
            return this;
        }

        public Builder targetPosition(int x, int y) {
            this.targetPositionX = x;
            this.targetPositionY = y;
            return this;
        }

        public Builder skill(String skillId) {
            this.skillId = skillId;
            return this;
        }

        public Builder reason(String reason) {
            this.reason = reason;
            return this;
        }

        public CombatDecision build() {
            return new CombatDecision(this);
        }
    }
}
