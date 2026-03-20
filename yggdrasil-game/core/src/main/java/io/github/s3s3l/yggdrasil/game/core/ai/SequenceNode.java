package io.github.s3s3l.yggdrasil.game.core.ai;

/**
 * 序列节点
 * 依次执行子节点，直到有一个子节点返回失败
 * 如果所有子节点都成功，则返回成功
 */
public class SequenceNode extends BehaviorNode {

    public SequenceNode(long id, String name) {
        super(id, name);
    }

    @Override
    public BehaviorState execute(BehaviorContext context) {
        for (BehaviorNode child : getChildren()) {
            BehaviorState state = child.execute(context);
            if (state == BehaviorState.FAILURE) {
                return BehaviorState.FAILURE;
            }
            if (state == BehaviorState.RUNNING) {
                return BehaviorState.RUNNING;
            }
        }
        return BehaviorState.SUCCESS;
    }
}
