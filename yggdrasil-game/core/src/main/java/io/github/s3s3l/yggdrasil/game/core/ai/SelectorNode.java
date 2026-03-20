package io.github.s3s3l.yggdrasil.game.core.ai;

/**
 * 选择节点
 * 依次执行子节点，直到有一个子节点返回成功
 * 如果所有子节点都失败，则返回失败
 */
public class SelectorNode extends BehaviorNode {

    public SelectorNode(long id, String name) {
        super(id, name);
    }

    @Override
    public BehaviorState execute(BehaviorContext context) {
        for (BehaviorNode child : getChildren()) {
            BehaviorState state = child.execute(context);
            if (state == BehaviorState.SUCCESS) {
                return BehaviorState.SUCCESS;
            }
            if (state == BehaviorState.RUNNING) {
                return BehaviorState.RUNNING;
            }
        }
        return BehaviorState.FAILURE;
    }
}
