package io.github.s3s3l.yggdrasil.game.core.ai;

import java.util.function.Consumer;

/**
 * 动作节点
 * 执行具体的动作
 */
public class ActionNode extends BehaviorNode {

    private final Consumer<BehaviorContext> action;

    public ActionNode(long id, String name, Consumer<BehaviorContext> action) {
        super(id, name);
        this.action = action;
    }

    @Override
    public BehaviorState execute(BehaviorContext context) {
        try {
            action.accept(context);
            return BehaviorState.SUCCESS;
        } catch (Exception e) {
            return BehaviorState.FAILURE;
        }
    }
}
