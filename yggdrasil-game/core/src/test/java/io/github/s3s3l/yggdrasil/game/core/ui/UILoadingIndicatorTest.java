package io.github.s3s3l.yggdrasil.game.core.ui;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * UI 加载指示器组件单元测试
 */
class UILoadingIndicatorTest {

    private UILoadingIndicator indicator;

    @BeforeEach
    void setUp() {
        indicator = new UILoadingIndicator(UILoadingIndicator.LoadingStyle.SPINNER);
        indicator.setId(1L);
    }

    @Test
    void testLoadingIndicatorCreation() {
        assertNotNull(indicator);
        assertEquals(UILoadingIndicator.LoadingStyle.SPINNER, indicator.getLoadingStyle());
        assertEquals(UIColor.BLUE, indicator.getColor());
        assertEquals(1.0f, indicator.getAnimationSpeed(), 0.01f);
        assertTrue(indicator.isAutoStart());
        assertTrue(indicator.isAnimating());
    }

    @Test
    void testDefaultConstructor() {
        UILoadingIndicator i = new UILoadingIndicator();
        assertNotNull(i);
        assertEquals(UILoadingIndicator.LoadingStyle.SPINNER, i.getLoadingStyle());
    }

    @Test
    void testConstructorWithStyle() {
        UILoadingIndicator i = new UILoadingIndicator(UILoadingIndicator.LoadingStyle.PULSE);
        assertEquals(UILoadingIndicator.LoadingStyle.PULSE, i.getLoadingStyle());
    }

    @Test
    void testSetStyle() {
        indicator.setLoadingStyle(UILoadingIndicator.LoadingStyle.PULSE);
        assertEquals(UILoadingIndicator.LoadingStyle.PULSE, indicator.getLoadingStyle());
    }

    @Test
    void testSetColor() {
        UIColor color = UIColor.RED;
        indicator.setColor(color);
        assertEquals(color, indicator.getColor());
    }

    @Test
    void testSetAnimationSpeed() {
        indicator.setAnimationSpeed(2.0f);
        assertEquals(2.0f, indicator.getAnimationSpeed(), 0.01f);
    }

    @Test
    void testSetAutoStart() {
        indicator.setAutoStart(false);
        assertFalse(indicator.isAutoStart());
        assertFalse(indicator.isAnimating());
    }

    @Test
    void testSetSegmentCount() {
        indicator.setSegmentCount(24);
        assertEquals(24, indicator.getSegmentCount());
    }

    @Test
    void testSetStrokeWidth() {
        indicator.setStrokeWidth(8);
        assertEquals(8, indicator.getStrokeWidth(), 0.01f);
    }

    @Test
    void testSetLoadingText() {
        indicator.setLoadingText("Loading...");
        assertEquals("Loading...", indicator.getLoadingText());
    }

    @Test
    void testSetTextColor() {
        UIColor color = UIColor.WHITE;
        indicator.setTextColor(color);
        assertEquals(color, indicator.getTextColor());
    }

    @Test
    void testSetFontSize() {
        indicator.setFontSize(18);
        assertEquals(18, indicator.getFontSize(), 0.01f);
    }

    @Test
    void testStart() {
        indicator.setAutoStart(false);
        assertFalse(indicator.isAnimating());

        indicator.start();
        assertTrue(indicator.isAnimating());
    }

    @Test
    void testStop() {
        assertTrue(indicator.isAnimating());

        indicator.stop();
        assertFalse(indicator.isAnimating());
        assertEquals(0, indicator.getAnimationTimer(), 0.01f);
    }

    @Test
    void testToggle() {
        assertTrue(indicator.isAnimating());

        indicator.toggle();
        assertFalse(indicator.isAnimating());

        indicator.toggle();
        assertTrue(indicator.isAnimating());
    }

    @Test
    void testUpdate_Animation() {
        indicator.setAnimationSpeed(1.0f);
        indicator.update(0.5f);

        assertEquals(0.5f, indicator.getAnimationTimer(), 0.01f);
    }

    @Test
    void testUpdate_AnimationReset() {
        indicator.setAnimationSpeed(2.0f);
        indicator.update(0.6f);

        // 计时器应该重置为 0.2 (1.2 - 1.0)
        assertEquals(0.2f, indicator.getAnimationTimer(), 0.01f);
    }

    @Test
    void testUpdate_NotAnimating() {
        indicator.stop();
        indicator.update(1.0f);

        assertEquals(0, indicator.getAnimationTimer(), 0.01f);
    }

    @Test
    void testGetRotationAngle() {
        indicator.setAnimationSpeed(1.0f);
        indicator.update(0.25f);

        assertEquals(90, indicator.getRotationAngle(), 0.01f);
    }

    @Test
    void testGetPulseScale() {
        indicator.setAnimationSpeed(1.0f);
        indicator.update(0.25f);

        float scale = indicator.getPulseScale();
        assertTrue(scale >= 0.5f && scale <= 1.5f);
    }

    @Test
    void testGetPulseAlpha() {
        indicator.setAnimationSpeed(1.0f);
        indicator.update(0.25f);

        float alpha = indicator.getPulseAlpha();
        assertTrue(alpha >= 0.3f && alpha <= 1.0f);
    }

    @Test
    void testGetProgress() {
        indicator.setAnimationSpeed(1.0f);
        indicator.update(0.5f);

        assertEquals(0.5f, indicator.getProgress(), 0.01f);
    }

    @Test
    void testRender() {
        assertDoesNotThrow(() -> indicator.render());
    }

    @Test
    void testRenderHidden() {
        indicator.setVisibility(UIVisibility.HIDDEN);
        assertDoesNotThrow(() -> indicator.render());
    }

    @Test
    void testRenderAllStyles() {
        for (UILoadingIndicator.LoadingStyle style : UILoadingIndicator.LoadingStyle.values()) {
            indicator.setLoadingStyle(style);
            assertDoesNotThrow(() -> indicator.render());
        }
    }

    @Test
    void testGetAnimationTimer() {
        indicator.setAnimationSpeed(1.0f);
        indicator.update(0.3f);
        assertEquals(0.3f, indicator.getAnimationTimer(), 0.01f);
    }
}
