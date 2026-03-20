package io.github.s3s3l.yggdrasil.game.core.ui;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * UI 工具提示组件单元测试
 */
class UITooltipTest {

    private UITooltip tooltip;

    @BeforeEach
    void setUp() {
        tooltip = new UITooltip("Test Tooltip");
        tooltip.setId(1L);
    }

    @Test
    void testTooltipCreation() {
        assertNotNull(tooltip);
        assertEquals("Test Tooltip", tooltip.getText());
        assertEquals(UITooltip.TooltipPosition.FOLLOW_MOUSE, tooltip.getPosition());
        assertEquals(10, tooltip.getOffsetX(), 0.01f);
        assertEquals(10, tooltip.getOffsetY(), 0.01f);
        assertEquals(0.3f, tooltip.getShowDelay(), 0.01f);
        assertEquals(0.2f, tooltip.getFadeDuration(), 0.01f);
    }

    @Test
    void testDefaultConstructor() {
        UITooltip t = new UITooltip();
        assertNotNull(t);
        assertEquals("", t.getText());
    }

    @Test
    void testSetText() {
        tooltip.setText("New Text");
        assertEquals("New Text", tooltip.getText());
    }

    @Test
    void testSetTextColor() {
        UIColor color = UIColor.YELLOW;
        tooltip.setTextColor(color);
        assertEquals(color, tooltip.getTextColor());
    }

    @Test
    void testSetBackgroundColor() {
        UIColor color = UIColor.BLUE;
        tooltip.setBackgroundColor(color);
        assertEquals(color, tooltip.getBackgroundColor());
    }

    @Test
    void testSetFontSize() {
        tooltip.setFontSize(18);
        assertEquals(18, tooltip.getFontSize(), 0.01f);
    }

    @Test
    void testSetPosition() {
        tooltip.setPosition(UITooltip.TooltipPosition.TOP);
        assertEquals(UITooltip.TooltipPosition.TOP, tooltip.getPosition());
    }

    @Test
    void testSetOffsetX() {
        tooltip.setOffsetX(20);
        assertEquals(20, tooltip.getOffsetX(), 0.01f);
    }

    @Test
    void testSetOffsetY() {
        tooltip.setOffsetY(25);
        assertEquals(25, tooltip.getOffsetY(), 0.01f);
    }

    @Test
    void testSetShowDelay() {
        tooltip.setShowDelay(0.5f);
        assertEquals(0.5f, tooltip.getShowDelay(), 0.01f);
    }

    @Test
    void testSetHideDelay() {
        tooltip.setHideDelay(0.3f);
        assertEquals(0.3f, tooltip.getHideDelay(), 0.01f);
    }

    @Test
    void testSetFadeDuration() {
        tooltip.setFadeDuration(0.4f);
        assertEquals(0.4f, tooltip.getFadeDuration(), 0.01f);
    }

    @Test
    void testSetTarget() {
        UIComponent target = new UIPanel("Target");
        tooltip.setTarget(target);
        assertSame(target, tooltip.getTarget());
    }

    @Test
    void testShow() {
        tooltip.setVisibility(UIVisibility.HIDDEN);
        tooltip.show();
        assertTrue(tooltip.isFadingIn());
    }

    @Test
    void testHide() {
        tooltip.show();
        // 完成淡入
        tooltip.update(tooltip.getFadeDuration());
        tooltip.hide();
        assertTrue(tooltip.isFadingOut());
    }

    @Test
    void testAttachTo() {
        UIComponent target = new UIPanel("Target");
        tooltip.attachTo(target);
        assertSame(target, tooltip.getTarget());
    }

    @Test
    void testUpdate_FadeIn() {
        tooltip.setVisibility(UIVisibility.HIDDEN);
        tooltip.setFadeDuration(0.2f);
        tooltip.show();

        tooltip.update(0.1f);
        assertEquals(0.5f, tooltip.getFadeProgress(), 0.01f);

        tooltip.update(0.1f);
        assertEquals(1.0f, tooltip.getFadeProgress(), 0.01f);
        assertFalse(tooltip.isFadingIn());
    }

    @Test
    void testUpdate_FadeOut() {
        tooltip.show();
        tooltip.setFadeDuration(0.2f);
        // 模拟淡入完成
        tooltip.update(0.2f);

        tooltip.hide();
        tooltip.update(0.1f);
        assertEquals(0.5f, tooltip.getFadeProgress(), 0.01f);
    }

    @Test
    void testUpdate_Position() {
        UIComponent target = new UIPanel("Target");
        target.setBounds(100, 100, 50, 50);
        tooltip.attachTo(target);
        tooltip.setPosition(UITooltip.TooltipPosition.TOP);
        tooltip.show();

        tooltip.update(0.016f);

        // 提示框应该在目标上方
        assertTrue(tooltip.getY() < target.getY());
    }

    @Test
    void testRender() {
        tooltip.show();
        assertDoesNotThrow(() -> tooltip.render());
    }

    @Test
    void testRenderHidden() {
        tooltip.setVisibility(UIVisibility.HIDDEN);
        assertDoesNotThrow(() -> tooltip.render());
    }

    @Test
    void testGetFadeProgress() {
        tooltip.setFadeDuration(0);
        assertEquals(1, tooltip.getFadeProgress(), 0.01f);
    }

    @Test
    void testSetListener() {
        final boolean[] shown = {false};
        final boolean[] hidden = {false};

        tooltip.setListener(new UITooltip.TooltipListener() {
            @Override
            public void onShow(UITooltip tooltip) {
                shown[0] = true;
            }

            @Override
            public void onHide(UITooltip tooltip) {
                hidden[0] = true;
            }
        });

        tooltip.setVisibility(UIVisibility.HIDDEN);
        tooltip.show();
        assertTrue(shown[0]);

        // 完成淡入
        tooltip.update(tooltip.getFadeDuration());
        tooltip.hide();
        assertTrue(hidden[0]);
    }

    @Test
    void testIsFadingIn_Default() {
        assertFalse(tooltip.isFadingIn());
    }

    @Test
    void testIsFadingOut_Default() {
        assertFalse(tooltip.isFadingOut());
    }

    @Test
    void testOpacity_AfterShow() {
        tooltip.setFadeDuration(0.2f);
        tooltip.show();
        tooltip.update(0.1f);

        assertTrue(tooltip.getOpacity() > 0);
        assertTrue(tooltip.getOpacity() <= 1);
    }
}
