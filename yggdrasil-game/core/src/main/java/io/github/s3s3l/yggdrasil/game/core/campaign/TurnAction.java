package io.github.s3s3l.yggdrasil.game.core.campaign;

import io.github.s3s3l.yggdrasil.game.core.basic.ID;

import java.util.*;

/**
 * 回合行动
 * 战役回合中执行的行动
 */
public class TurnAction implements ID {

    private final long numericId;
    private final ActionType type;
    private final String description;
    private final long sourceRegionId;
    private final long targetRegionId;
    private final Map<String, Object> parameters;
    private final ActionResult result;

    private TurnAction(Builder builder) {
        this.numericId = builder.id;
        this.type = builder.type;
        this.description = builder.description;
        this.sourceRegionId = builder.sourceRegionId;
        this.targetRegionId = builder.targetRegionId;
        this.parameters = builder.parameters;
        this.result = builder.result;
    }

    @Override
    public long getId() {
        return numericId;
    }

    @Override
    public void setId(long id) {
        // ID is immutable
    }

    public ActionType getType() {
        return type;
    }

    public String getDescription() {
        return description;
    }

    public long getSourceRegionId() {
        return sourceRegionId;
    }

    public long getTargetRegionId() {
        return targetRegionId;
    }

    /**
     * 获取参数
     *
     * @param key 参数键
     * @return 参数值
     */
    public Object getParameter(String key) {
        return parameters.get(key);
    }

    /**
     * 获取所有参数
     *
     * @return 参数映射
     */
    public Map<String, Object> getParameters() {
        return Collections.unmodifiableMap(parameters);
    }

    public ActionResult getResult() {
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TurnAction that = (TurnAction) o;
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
        private ActionType type;
        private String description = "";
        private long sourceRegionId;
        private long targetRegionId;
        private Map<String, Object> parameters = new HashMap<>();
        private ActionResult result;

        public Builder id(long id) {
            this.id = id;
            return this;
        }

        public Builder type(ActionType type) {
            this.type = type;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder sourceRegionId(long sourceRegionId) {
            this.sourceRegionId = sourceRegionId;
            return this;
        }

        public Builder targetRegionId(long targetRegionId) {
            this.targetRegionId = targetRegionId;
            return this;
        }

        public Builder parameters(Map<String, Object> parameters) {
            this.parameters = parameters;
            return this;
        }

        public Builder result(ActionResult result) {
            this.result = result;
            return this;
        }

        public TurnAction build() {
            return new TurnAction(this);
        }
    }
}
