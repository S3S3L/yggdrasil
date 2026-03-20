package io.github.s3s3l.yggdrasil.game.core.ai;

import java.util.function.Predicate;

/**
 * 条件节点
 * 根据条件判断返回成功或失败
 */
public class ConditionNode extends BehaviorNode {

    private final Predicate<BehaviorContext> condition;

    public ConditionNode(long id, String name, Predicate<BehaviorContext> condition) {
        super(id, name);
        this.condition = condition;
    }

    @Override
    public BehaviorState execute(BehaviorContext context) {
        if (condition.test(context)) {
            return BehaviorState.SUCCESS;
        }
        return BehaviorState.FAILURE;
    }
}
