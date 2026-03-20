package io.github.s3s3l.yggdrasil.game.core.ui.animation;

import org.junit.jupiter.api.Test;

import io.github.s3s3l.yggdrasil.game.core.ui.UIComponent;
import io.github.s3s3l.yggdrasil.game.core.ui.UIColor;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 透明度动画测试
 */
class AlphaActionTest {

    @Test
    void testFadeOut() {
        TestUIComponent component = new TestUIComponent();
        component.setBackgroundColor(new UIColor(1, 1, 1, 1));

        AlphaAction action = AlphaAction.fadeOut(1f);
        action.start(component);

        // fromAlpha 在 start() 时从当前透明度获取
        assertEquals(1f, action.getFromAlpha(), 0.001f);
        assertEquals(0f, action.getToAlpha(), 0.001f);
        assertEquals(1f, action.getDuration(), 0.001f);
    }

    @Test
    void testFadeIn() {
        TestUIComponent component = new TestUIComponent();
        component.setBackgroundColor(new UIColor(1, 1, 1, 0));

        AlphaAction action = AlphaAction.fadeIn(1f);
        action.start(component);

        assertEquals(0f, action.getFromAlpha(), 0.001f);
        assertEquals(1f, action.getToAlpha(), 0.001f);
    }

    @Test
    void testFadeInOut() {
        TestUIComponent component = new TestUIComponent();
        component.setBackgroundColor(new UIColor(1, 1, 1, 0));

        AlphaAction action = AlphaAction.fadeInOut(2f);
        action.start(component);

        assertTrue(action.isAutoReverse());
        assertEquals(2f, action.getDuration(), 0.001f);
    }

    @Test
    void testUpdate() {
        TestUIComponent component = new TestUIComponent();
        component.setBackgroundColor(new UIColor(1, 1, 1, 1));

        AlphaAction action = new AlphaAction(1f, 0f);
        action.setDuration(1f);
        action.start(component);

        // 更新 0.5 秒后
        action.update(0.5f);
        assertEquals(0.5f, component.getBackgroundColor().getA(), 0.001f);
        assertFalse(action.isFinished());

        // 更新 1 秒后
        action.update(0.5f);
        assertEquals(0f, component.getBackgroundColor().getA(), 0.001f);
        assertTrue(action.isFinished());
    }

    @Test
    void testWithEasing() {
        TestUIComponent component = new TestUIComponent();
        component.setBackgroundColor(new UIColor(1, 1, 1, 1));

        AlphaAction action = new AlphaAction(1f, 0f);
        action.setDuration(1f);
        action.setEasing(UIEasing.IN_QUAD);
        action.start(component);

        // 使用 IN_QUAD，在 0.5 进度时实际值为 0.25
        action.update(0.5f);
        assertEquals(0.75f, component.getBackgroundColor().getA(), 0.001f);
    }

    @Test
    void testReset() {
        TestUIComponent component = new TestUIComponent();
        component.setBackgroundColor(new UIColor(1, 1, 1, 1));

        AlphaAction action = new AlphaAction(1f, 0f);
        action.setDuration(1f);
        action.start(component);

        action.update(0.5f);
        action.reset();

        assertFalse(action.isFinished());
        assertEquals(1f, component.getBackgroundColor().getA(), 0.001f);
    }

    @Test
    void testOnFinishListener() {
        TestUIComponent component = new TestUIComponent();
        component.setBackgroundColor(new UIColor(1, 1, 1, 1));

        final boolean[] finished = {false};

        AlphaAction action = new AlphaAction(1f, 0f);
        action.setDuration(0.5f);
        action.setOnFinishListener(() -> finished[0] = true);
        action.start(component);

        assertFalse(finished[0]);

        action.update(0.5f);

        assertTrue(finished[0]);
    }

    static class TestUIComponent extends UIComponent {
        @Override
        public void render() {
            // 不需要实现
        }
    }
}
