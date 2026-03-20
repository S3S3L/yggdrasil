package io.github.s3s3l.yggdrasil.game.core.ui;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * UI 切换开关组件单元测试
 */
class UIToggleTest {

    private UIToggle toggle;

    @BeforeEach
    void setUp() {
        toggle = new UIToggle("Test Toggle", false);
        toggle.setId(1L);
    }

    @Test
    void testToggleCreation() {
        assertNotNull(toggle);
        assertEquals("Test Toggle", toggle.getText());
        assertFalse(toggle.isOn());
        assertEquals(0, toggle.getAnimationProgress(), 0.01f);
    }

    @Test
    void testDefaultConstructor() {
        UIToggle t = new UIToggle();
        assertNotNull(t);
        assertFalse(t.isOn());
    }

    @Test
    void testConstructorWithText() {
        UIToggle t = new UIToggle("Hello");
        assertEquals("Hello", t.getText());
        assertFalse(t.isOn());
    }

    @Test
    void testConstructorWithOnState() {
        UIToggle t = new UIToggle("Test", true);
        assertEquals("Test", t.getText());
        assertTrue(t.isOn());
        assertEquals(1, t.getAnimationProgress(), 0.01f);
    }

    @Test
    void testSetOn() {
        toggle.setOn(true);
        assertTrue(toggle.isOn());
    }

    @Test
    void testToggle() {
        assertFalse(toggle.isOn());
        toggle.toggle();
        assertTrue(toggle.isOn());
        toggle.toggle();
        assertFalse(toggle.isOn());
    }

    @Test
    void testSetText() {
        toggle.setText("New Text");
        assertEquals("New Text", toggle.getText());
    }

    @Test
    void testSetTextColor() {
        UIColor color = UIColor.RED;
        toggle.setTextColor(color);
        assertEquals(color, toggle.getTextColor());
    }

    @Test
    void testSetOnColor() {
        UIColor color = UIColor.BLUE;
        toggle.setOnColor(color);
        assertEquals(color, toggle.getOnColor());
    }

    @Test
    void testSetOffColor() {
        UIColor color = UIColor.GRAY;
        toggle.setOffColor(color);
        assertEquals(color, toggle.getOffColor());
    }

    @Test
    void testSetKnobColor() {
        UIColor color = UIColor.WHITE;
        toggle.setKnobColor(color);
        assertEquals(color, toggle.getKnobColor());
    }

    @Test
    void testSetAnimationDuration() {
        toggle.setAnimationDuration(0.5f);
        assertEquals(0.5f, toggle.getAnimationDuration(), 0.01f);
    }

    @Test
    void testToggleListener() {
        final boolean[] toggled = {false};
        final boolean[] capturedState = {false};

        toggle.setListener((t, isOn) -> {
            toggled[0] = true;
            capturedState[0] = isOn;
        });

        toggle.toggle();
        assertTrue(toggled[0]);
        assertTrue(capturedState[0]);
    }

    @Test
    void testToggleListenerNotCalledOnSameState() {
        final int[] callCount = {0};
        toggle.setListener((t, isOn) -> callCount[0]++);

        // 已经是 off 状态，设置为 off 不应触发监听器
        toggle.setOn(false);
        assertEquals(0, callCount[0]);

        // 切换到 on 应触发
        toggle.setOn(true);
        assertEquals(1, callCount[0]);
    }

    @Test
    void testUpdate_AnimationInProgress() {
        toggle.setOn(true);
        toggle.setAnimationDuration(0.5f);
        toggle.setAnimationProgress(0);

        toggle.update(0.25f);
        assertEquals(0.5f, toggle.getAnimationProgress(), 0.01f);

        toggle.update(0.25f);
        assertEquals(1.0f, toggle.getAnimationProgress(), 0.01f);
    }

    @Test
    void testUpdate_AnimationComplete() {
        toggle.setOn(true);
        toggle.setAnimationProgress(0.5f);

        toggle.update(1.0f);
        assertEquals(1.0f, toggle.getAnimationProgress(), 0.01f);
    }

    @Test
    void testOnClick() {
        assertFalse(toggle.isOn());
        toggle.onClick();
        assertTrue(toggle.isOn());
    }

    @Test
    void testRender() {
        assertDoesNotThrow(() -> toggle.render());
    }

    @Test
    void testRenderHidden() {
        toggle.setVisibility(UIVisibility.HIDDEN);
        assertDoesNotThrow(() -> toggle.render());
    }

    @Test
    void testGetKnobX() {
        toggle.setBounds(0, 0, 100, 30);
        toggle.setAnimationProgress(0.5f);

        float knobX = toggle.getKnobX();
        assertTrue(knobX >= 0 && knobX <= 70);
    }

    @Test
    void testGetCurrentColor() {
        toggle.setOnColor(UIColor.GREEN);
        toggle.setOffColor(UIColor.RED);
        toggle.setAnimationProgress(0.5f);

        UIColor current = toggle.getCurrentColor();
        assertNotNull(current);
    }

    @Test
    void testSetOnColorWithLerp() {
        toggle.setOnColor(UIColor.WHITE);
        toggle.setOffColor(UIColor.BLACK);
        toggle.setAnimationProgress(0.5f);

        UIColor current = toggle.getCurrentColor();
        // 颜色应该在黑白之间
        assertTrue(current.getR() >= 0.4f && current.getR() <= 0.6f);
    }
}
