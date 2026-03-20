package io.github.s3s3l.yggdrasil.game.core.ai;

import io.github.s3s3l.yggdrasil.game.core.basic.ID;

import java.util.Objects;

/**
 * 行为树控制器
 * 管理和执行行为树
 */
public class BehaviorTreeController implements ID {

    private long numericId;
    private BehaviorNode root;
    private final BehaviorContext context;
    private BehaviorState currentState;

    public BehaviorTreeController(long id) {
        this.numericId = id;
        this.context = new BehaviorContext();
        this.currentState = BehaviorState.SUCCESS;
    }

    @Override
    public long getId() {
        return numericId;
    }

    @Override
    public void setId(long id) {
        // ID is immutable
    }

    public BehaviorNode getRoot() {
        return root;
    }

    public void setRoot(BehaviorNode root) {
        this.root = root;
    }

    public BehaviorContext getContext() {
        return context;
    }

    /**
     * 执行行为树
     *
     * @return 执行状态
     */
    public BehaviorState tick() {
        if (root != null) {
            currentState = root.execute(context);
        }
        return currentState;
    }

    /**
     * 获取当前状态
     *
     * @return 当前状态
     */
    public BehaviorState getCurrentState() {
        return currentState;
    }

    /**
     * 重置行为树
     */
    public void reset() {
        context.clear();
        currentState = BehaviorState.SUCCESS;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BehaviorTreeController that = (BehaviorTreeController) o;
        return Objects.equals(numericId, that.numericId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(numericId);
    }
}
