package io.github.s3s3l.yggdrasil.game.core.ai;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 行为树单元测试
 */
class BehaviorTreeTest {

    private BehaviorContext context;
    private BehaviorTreeController controller;

    @BeforeEach
    void setUp() {
        context = new BehaviorContext();
        controller = new BehaviorTreeController(1L);
        controller.getContext().setData("test", "value");
    }

    @Test
    void testControllerCreation() {
        assertEquals(1L, controller.getId());
        assertEquals(BehaviorState.SUCCESS, controller.getCurrentState());
    }

    @Test
    void testContextData() {
        assertEquals("value", controller.getContext().getData("test"));
    }

    @Test
    void testActionNode() {
        AtomicBoolean executed = new AtomicBoolean(false);
        ActionNode action = new ActionNode(1L, "Test Action", ctx -> executed.set(true));

        assertEquals(BehaviorState.SUCCESS, action.execute(context));
        assertTrue(executed.get());
    }

    @Test
    void testConditionNodeSuccess() {
        ConditionNode condition = new ConditionNode(1L, "Test Condition", ctx -> true);
        assertEquals(BehaviorState.SUCCESS, condition.execute(context));
    }

    @Test
    void testConditionNodeFailure() {
        ConditionNode condition = new ConditionNode(1L, "Test Condition", ctx -> false);
        assertEquals(BehaviorState.FAILURE, condition.execute(context));
    }

    @Test
    void testSelectorNodeSuccess() {
        SelectorNode selector = new SelectorNode(1L, "Test Selector");
        selector.addChild(new ConditionNode(1L, "Fail", ctx -> false));
        selector.addChild(new ConditionNode(2L, "Success", ctx -> true));
        selector.addChild(new ConditionNode(3L, "Success2", ctx -> true));

        assertEquals(BehaviorState.SUCCESS, selector.execute(context));
    }

    @Test
    void testSelectorNodeFailure() {
        SelectorNode selector = new SelectorNode(1L, "Test Selector");
        selector.addChild(new ConditionNode(1L, "Fail1", ctx -> false));
        selector.addChild(new ConditionNode(2L, "Fail2", ctx -> false));

        assertEquals(BehaviorState.FAILURE, selector.execute(context));
    }

    @Test
    void testSequenceNodeSuccess() {
        SequenceNode sequence = new SequenceNode(1L, "Test Sequence");
        sequence.addChild(new ConditionNode(1L, "Success1", ctx -> true));
        sequence.addChild(new ConditionNode(2L, "Success2", ctx -> true));

        assertEquals(BehaviorState.SUCCESS, sequence.execute(context));
    }

    @Test
    void testSequenceNodeFailure() {
        SequenceNode sequence = new SequenceNode(1L, "Test Sequence");
        sequence.addChild(new ConditionNode(1L, "Success", ctx -> true));
        sequence.addChild(new ConditionNode(2L, "Fail", ctx -> false));
        sequence.addChild(new ConditionNode(3L, "Success2", ctx -> true));

        assertEquals(BehaviorState.FAILURE, sequence.execute(context));
    }

    @Test
    void testControllerTick() {
        ActionNode action = new ActionNode(1L, "Test Action", ctx -> {});
        controller.setRoot(action);

        assertEquals(BehaviorState.SUCCESS, controller.tick());
        assertEquals(BehaviorState.SUCCESS, controller.getCurrentState());
    }

    @Test
    void testControllerReset() {
        controller.getContext().setData("key", "value");
        controller.reset();

        assertFalse(controller.getContext().hasData("key"));
    }
}
