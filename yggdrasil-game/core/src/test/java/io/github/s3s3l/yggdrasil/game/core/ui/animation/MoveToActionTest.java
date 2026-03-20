package io.github.s3s3l.yggdrasil.game.core.ui.animation;

import org.junit.jupiter.api.Test;

import io.github.s3s3l.yggdrasil.game.core.ui.UIComponent;
import io.github.s3s3l.yggdrasil.game.core.ui.UIColor;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 移动动画测试
 */
class MoveToActionTest {

    @Test
    void testMoveTo() {
        TestUIComponent component = new TestUIComponent();
        component.setPosition(0f, 0f);

        MoveToAction action = MoveToAction.moveTo(1f, 100f, 50f);
        action.start(component);

        assertEquals(0f, action.getFromX(), 0.001f);
        assertEquals(0f, action.getFromY(), 0.001f);
        assertEquals(100f, action.getToX(), 0.001f);
        assertEquals(50f, action.getToY(), 0.001f);
    }

    @Test
    void testMoveBy() {
        TestUIComponent component = new TestUIComponent();
        component.setPosition(10f, 20f);

        MoveToAction action = MoveToAction.moveBy(1f, 50f, 30f);
        action.start(component);

        assertTrue(action.isRelative());
        action.update(1f);

        assertEquals(60f, component.getX(), 0.001f);
        assertEquals(50f, component.getY(), 0.001f);
    }

    @Test
    void testUpdate() {
        TestUIComponent component = new TestUIComponent();
        component.setPosition(0f, 0f);

        MoveToAction action = new MoveToAction(0f, 0f, 100f, 50f);
        action.setDuration(1f);
        action.start(component);

        // 更新 0.5 秒后
        action.update(0.5f);
        assertEquals(50f, component.getX(), 0.001f);
        assertEquals(25f, component.getY(), 0.001f);

        // 更新 1 秒后
        action.update(0.5f);
        assertEquals(100f, component.getX(), 0.001f);
        assertEquals(50f, component.getY(), 0.001f);
        assertTrue(action.isFinished());
    }

    @Test
    void testWithEasing() {
        TestUIComponent component = new TestUIComponent();
        component.setPosition(0f, 0f);

        MoveToAction action = new MoveToAction(0f, 0f, 100f, 50f);
        action.setDuration(1f);
        action.setEasing(UIEasing.IN_OUT_QUAD);
        action.start(component);

        // 在 0.5 进度时，IN_OUT_QUAD 值为 0.5
        action.update(0.5f);
        assertEquals(50f, component.getX(), 0.001f);
        assertEquals(25f, component.getY(), 0.001f);
    }

    @Test
    void testLoop() {
        TestUIComponent component = new TestUIComponent();
        component.setPosition(0f, 0f);

        MoveToAction action = new MoveToAction(0f, 0f, 100f, 0f);
        action.setDuration(1f);
        action.setLoop(true);
        action.start(component);

        // 更新 0.5 秒
        action.update(0.5f);
        assertEquals(50f, component.getX(), 5f);
        assertFalse(action.isFinished());

        // 再更新 0.7 秒，进入第二次循环
        action.update(0.7f);
        assertEquals(20f, component.getX(), 15f);
    }

    @Test
    void testAutoReverse() {
        TestUIComponent component = new TestUIComponent();
        component.setPosition(0f, 0f);

        MoveToAction action = new MoveToAction(0f, 0f, 100f, 0f);
        action.setDuration(1f);
        action.setAutoReverse(true);
        action.start(component);

        // 更新 0.5 秒，到中点
        action.update(0.5f);
        assertEquals(50f, component.getX(), 5f);
        assertFalse(action.isFinished());
        assertFalse(action.isReversing());

        // 再更新 0.5 秒，到达终点，触发反向
        action.update(0.5f);
        assertEquals(100f, component.getX(), 5f);
        assertTrue(action.isReversing());
        assertFalse(action.isFinished());

        // 再更新 0.5 秒，反向运动到中点
        action.update(0.5f);
        assertEquals(50f, component.getX(), 5f);
        assertTrue(action.isReversing());
    }

    static class TestUIComponent extends UIComponent {
        @Override
        public void render() {
            // 不需要实现
        }
    }
}
