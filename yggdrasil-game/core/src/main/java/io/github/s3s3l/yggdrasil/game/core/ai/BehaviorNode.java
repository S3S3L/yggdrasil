package io.github.s3s3l.yggdrasil.game.core.ai;

import io.github.s3s3l.yggdrasil.game.core.basic.ID;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 行为节点
 * 行为树的基本组成单元
 */
public class BehaviorNode implements ID {

    private long numericId;
    private final String name;
    private final List<BehaviorNode> children;

    public BehaviorNode(long id, String name) {
        this.numericId = id;
        this.name = name;
        this.children = new ArrayList<>();
    }

    @Override
    public long getId() {
        return numericId;
    }

    @Override
    public void setId(long id) {
        // ID is immutable
    }

    public String getName() {
        return name;
    }

    public List<BehaviorNode> getChildren() {
        return children;
    }

    public void addChild(BehaviorNode child) {
        children.add(child);
    }

    public void removeChild(BehaviorNode child) {
        children.remove(child);
    }

    /**
     * 执行行为节点
     *
     * @param context 行为上下文
     * @return 行为状态
     */
    public BehaviorState execute(BehaviorContext context) {
        return BehaviorState.SUCCESS;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BehaviorNode that = (BehaviorNode) o;
        return Objects.equals(numericId, that.numericId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(numericId);
    }
}
