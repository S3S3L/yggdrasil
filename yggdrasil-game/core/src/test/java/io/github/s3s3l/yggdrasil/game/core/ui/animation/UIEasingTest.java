package io.github.s3s3l.yggdrasil.game.core.ui.animation;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * UI 缓动函数测试
 */
class UIEasingTest {

    @Test
    void testLinear() {
        assertEquals(0f, UIEasing.LINEAR.apply(0f), 0.001f);
        assertEquals(0.5f, UIEasing.LINEAR.apply(0.5f), 0.001f);
        assertEquals(1f, UIEasing.LINEAR.apply(1f), 0.001f);
    }

    @Test
    void testInQuad() {
        assertEquals(0f, UIEasing.IN_QUAD.apply(0f), 0.001f);
        assertEquals(0.25f, UIEasing.IN_QUAD.apply(0.5f), 0.001f);
        assertEquals(1f, UIEasing.IN_QUAD.apply(1f), 0.001f);
    }

    @Test
    void testOutQuad() {
        assertEquals(0f, UIEasing.OUT_QUAD.apply(0f), 0.001f);
        assertEquals(0.75f, UIEasing.OUT_QUAD.apply(0.5f), 0.001f);
        assertEquals(1f, UIEasing.OUT_QUAD.apply(1f), 0.001f);
    }

    @Test
    void testInOutQuad() {
        assertEquals(0f, UIEasing.IN_OUT_QUAD.apply(0f), 0.001f);
        assertEquals(0.5f, UIEasing.IN_OUT_QUAD.apply(0.5f), 0.001f);
        assertEquals(1f, UIEasing.IN_OUT_QUAD.apply(1f), 0.001f);
    }

    @Test
    void testInCubic() {
        assertEquals(0f, UIEasing.IN_CUBIC.apply(0f), 0.001f);
        assertEquals(0.125f, UIEasing.IN_CUBIC.apply(0.5f), 0.001f);
        assertEquals(1f, UIEasing.IN_CUBIC.apply(1f), 0.001f);
    }

    @Test
    void testOutCubic() {
        assertEquals(0f, UIEasing.OUT_CUBIC.apply(0f), 0.001f);
        assertEquals(0.875f, UIEasing.OUT_CUBIC.apply(0.5f), 0.001f);
        assertEquals(1f, UIEasing.OUT_CUBIC.apply(1f), 0.001f);
    }

    @Test
    void testInOutCubic() {
        assertEquals(0f, UIEasing.IN_OUT_CUBIC.apply(0f), 0.001f);
        assertEquals(0.5f, UIEasing.IN_OUT_CUBIC.apply(0.5f), 0.001f);
        assertEquals(1f, UIEasing.IN_OUT_CUBIC.apply(1f), 0.001f);
    }

    @Test
    void testInQuint() {
        assertEquals(0f, UIEasing.IN_QUINT.apply(0f), 0.001f);
        assertEquals(0.03125f, UIEasing.IN_QUINT.apply(0.5f), 0.001f);
        assertEquals(1f, UIEasing.IN_QUINT.apply(1f), 0.001f);
    }

    @Test
    void testOutQuint() {
        assertEquals(0f, UIEasing.OUT_QUINT.apply(0f), 0.001f);
        assertEquals(0.96875f, UIEasing.OUT_QUINT.apply(0.5f), 0.001f);
        assertEquals(1f, UIEasing.OUT_QUINT.apply(1f), 0.001f);
    }

    @Test
    void testInSine() {
        assertEquals(0f, UIEasing.IN_SINE.apply(0f), 0.001f);
        assertEquals(1f, UIEasing.IN_SINE.apply(1f), 0.001f);
    }

    @Test
    void testOutSine() {
        assertEquals(0f, UIEasing.OUT_SINE.apply(0f), 0.001f);
        assertEquals(1f, UIEasing.OUT_SINE.apply(1f), 0.001f);
    }

    @Test
    void testInOutSine() {
        assertEquals(0f, UIEasing.IN_OUT_SINE.apply(0f), 0.001f);
        assertEquals(0.5f, UIEasing.IN_OUT_SINE.apply(0.5f), 0.001f);
        assertEquals(1f, UIEasing.IN_OUT_SINE.apply(1f), 0.001f);
    }

    @Test
    void testInCirc() {
        assertEquals(0f, UIEasing.IN_CIRC.apply(0f), 0.001f);
        assertEquals(1f, UIEasing.IN_CIRC.apply(1f), 0.001f);
    }

    @Test
    void testOutCirc() {
        assertEquals(0f, UIEasing.OUT_CIRC.apply(0f), 0.001f);
        assertEquals(1f, UIEasing.OUT_CIRC.apply(1f), 0.001f);
    }

    @Test
    void testEasingRange() {
        // 测试所有缓动函数的输出范围在 0-1 之间
        UIEasing[] easings = {
            UIEasing.LINEAR,
            UIEasing.IN_QUAD, UIEasing.OUT_QUAD, UIEasing.IN_OUT_QUAD,
            UIEasing.IN_CUBIC, UIEasing.OUT_CUBIC, UIEasing.IN_OUT_CUBIC,
            UIEasing.IN_QUINT, UIEasing.OUT_QUINT, UIEasing.IN_OUT_QUINT,
            UIEasing.IN_SINE, UIEasing.OUT_SINE, UIEasing.IN_OUT_SINE,
            UIEasing.IN_CIRC, UIEasing.OUT_CIRC, UIEasing.IN_OUT_CIRC
        };

        for (UIEasing easing : easings) {
            float result = easing.apply(0f);
            assertTrue(result >= 0f && result <= 1f,
                easing.getClass().getName() + " at t=0 should be in [0,1], got: " + result);

            result = easing.apply(1f);
            assertTrue(result >= 0f && result <= 1f,
                easing.getClass().getName() + " at t=1 should be in [0,1], got: " + result);
        }
    }
}
