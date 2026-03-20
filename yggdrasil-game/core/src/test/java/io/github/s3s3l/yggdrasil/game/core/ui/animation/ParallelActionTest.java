package io.github.s3s3l.yggdrasil.game.core.ui.animation;

import org.junit.jupiter.api.Test;

import io.github.s3s3l.yggdrasil.game.core.ui.UIComponent;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 并行动画测试
 */
class ParallelActionTest {

    @Test
    void testParallel() {
        TestUIComponent component = new TestUIComponent();

        MoveToAction move = new MoveToAction(0f, 0f, 100f, 50f);
        move.setDuration(1f);

        AlphaAction alpha = new AlphaAction(1f, 0f);
        alpha.setDuration(1f);

        ParallelAction parallel = ParallelAction.parallel(move, alpha);
        parallel.start(component);

        assertFalse(parallel.isFinished());

        // 更新 0.5 秒
        parallel.update(0.5f);
        assertFalse(parallel.isFinished());

        // 更新 1 秒
        parallel.update(0.5f);
        assertTrue(parallel.isFinished());
    }

    @Test
    void testParallelWithDifferentDurations() {
        TestUIComponent component = new TestUIComponent();

        MoveToAction move = new MoveToAction(0f, 0f, 100f, 0f);
        move.setDuration(0.5f);

        AlphaAction alpha = new AlphaAction(1f, 0f);
        alpha.setDuration(1f);

        ParallelAction parallel = ParallelAction.parallel(move, alpha);
        parallel.start(component);

        // 0.5 秒后，move 完成，alpha 未完成
        parallel.update(0.5f);
        assertFalse(parallel.isFinished());

        // 1 秒后，所有动画完成
        parallel.update(0.5f);
        assertTrue(parallel.isFinished());
    }

    @Test
    void testParallelWithAppend() {
        TestUIComponent component = new TestUIComponent();

        ParallelAction parallel = new ParallelAction();
        parallel.append(AlphaAction.fadeOut(0.5f));
        MoveToAction moveAction = new MoveToAction(0f, 0f, 100f, 50f);
        moveAction.setDuration(0.5f);
        parallel.append(moveAction);

        parallel.start(component);
        parallel.update(0.5f);

        assertTrue(parallel.isFinished());
    }

    @Test
    void testParallelDuration() {
        MoveToAction move = new MoveToAction();
        move.setDuration(0.5f);

        AlphaAction alpha = new AlphaAction();
        alpha.setDuration(1f);

        ParallelAction parallel = ParallelAction.parallel(move, alpha);

        // 并行动画的持续时间由最长的动画决定
        assertEquals(1f, parallel.getDuration(), 0.001f);
    }

    @Test
    void testParallelOnFinish() {
        TestUIComponent component = new TestUIComponent();
        final boolean[] finished = {false};

        ParallelAction parallel = ParallelAction.parallel(
            AlphaAction.fadeOut(0.5f),
            AlphaAction.fadeIn(0.5f)
        );
        parallel.setOnFinishListener(() -> finished[0] = true);
        parallel.start(component);

        assertFalse(finished[0]);

        parallel.update(0.5f);

        assertTrue(finished[0]);
    }

    @Test
    void testParallelReset() {
        TestUIComponent component = new TestUIComponent();

        ParallelAction parallel = ParallelAction.parallel(
            AlphaAction.fadeOut(0.5f),
            AlphaAction.fadeIn(0.5f)
        );
        parallel.start(component);
        parallel.update(0.3f);
        parallel.reset();

        assertFalse(parallel.isFinished());
    }

    static class TestUIComponent extends UIComponent {
        @Override
        public void render() {
            // 不需要实现
        }
    }
}
