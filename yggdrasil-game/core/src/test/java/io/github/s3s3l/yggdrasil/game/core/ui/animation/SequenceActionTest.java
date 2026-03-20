package io.github.s3s3l.yggdrasil.game.core.ui.animation;

import org.junit.jupiter.api.Test;

import io.github.s3s3l.yggdrasil.game.core.ui.UIComponent;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 动画序列测试
 */
class SequenceActionTest {

    @Test
    void testSequence() {
        TestUIComponent component = new TestUIComponent();

        AlphaAction action1 = AlphaAction.fadeOut(0.5f);
        AlphaAction action2 = AlphaAction.fadeIn(0.5f);

        SequenceAction sequence = SequenceAction.sequence(action1, action2);
        sequence.start(component);

        assertFalse(sequence.isFinished());

        // 执行第一个动画
        sequence.update(0.5f);
        assertFalse(sequence.isFinished());

        // 执行第二个动画
        sequence.update(0.5f);
        assertTrue(sequence.isFinished());
    }

    @Test
    void testSequenceWithAppend() {
        TestUIComponent component = new TestUIComponent();

        SequenceAction sequence = new SequenceAction();
        sequence.append(AlphaAction.fadeOut(0.5f))
                .append(AlphaAction.fadeIn(0.5f))
                .append(AlphaAction.fadeOut(0.5f));

        sequence.start(component);

        // 执行所有动画
        sequence.update(0.5f);
        sequence.update(0.5f);
        sequence.update(0.5f);

        assertTrue(sequence.isFinished());
    }

    @Test
    void testSequenceDuration() {
        AlphaAction action1 = AlphaAction.fadeOut(0.5f);
        AlphaAction action2 = AlphaAction.fadeIn(1f);

        SequenceAction sequence = SequenceAction.sequence(action1, action2);

        assertEquals(1.5f, sequence.getDuration(), 0.001f);
    }

    @Test
    void testSequenceOnFinish() {
        TestUIComponent component = new TestUIComponent();
        final boolean[] finished = {false};

        SequenceAction sequence = SequenceAction.sequence(
            AlphaAction.fadeOut(0.3f),
            AlphaAction.fadeIn(0.3f)
        );
        sequence.setOnFinishListener(() -> finished[0] = true);
        sequence.start(component);

        assertFalse(finished[0]);

        sequence.update(0.6f);

        assertTrue(finished[0]);
    }

    @Test
    void testSequenceReset() {
        TestUIComponent component = new TestUIComponent();

        SequenceAction sequence = SequenceAction.sequence(
            AlphaAction.fadeOut(0.5f),
            AlphaAction.fadeIn(0.5f)
        );
        sequence.start(component);
        sequence.update(0.3f);
        sequence.reset();

        assertFalse(sequence.isFinished());
        assertEquals(0, sequence.getCurrentIndex());
    }

    static class TestUIComponent extends UIComponent {
        @Override
        public void render() {
            // 不需要实现
        }
    }
}
