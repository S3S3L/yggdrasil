package io.github.s3s3l.yggdrasil.game.core.ui.theme;

import io.github.s3s3l.yggdrasil.game.core.ui.UIColor;
import io.github.s3s3l.yggdrasil.game.core.ui.UIRect;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * UI 样式单元测试
 */
class UIStyleTest {

    private UIStyle style;

    @BeforeEach
    void setUp() {
        style = new UIStyle();
    }

    @Test
    void testDefaultValues() {
        assertEquals(UIColor.TRANSPARENT, style.getBackgroundColor());
        assertEquals("default", style.getFontName());
        assertEquals(16f, style.getFontSize());
        assertEquals(UIColor.WHITE, style.getFontColor());
        assertEquals(BorderStyle.NONE, style.getBorder());
        assertEquals(1f, style.getOpacity());
        assertTrue(style.isBackgroundVisible());
    }

    @Test
    void testCreateCopy() {
        UIStyle original = new UIStyle();
        original.setBackgroundColor(UIColor.RED);
        original.setFontSize(20f);
        original.setOpacity(0.5f);

        UIStyle copy = new UIStyle(original);

        assertEquals(original.getBackgroundColor(), copy.getBackgroundColor());
        assertEquals(original.getFontSize(), copy.getFontSize());
        assertEquals(original.getOpacity(), copy.getOpacity());
    }

    @Test
    void testSetters() {
        style.setBackgroundColor(UIColor.BLUE);
        style.setFontName("Arial");
        style.setFontSize(18f);
        style.setFontColor(UIColor.BLACK);
        style.setBorder(new BorderStyle(2f, UIColor.WHITE, 4f));
        style.setOpacity(0.8f);

        assertEquals(UIColor.BLUE, style.getBackgroundColor());
        assertEquals("Arial", style.getFontName());
        assertEquals(18f, style.getFontSize());
        assertEquals(UIColor.BLACK, style.getFontColor());
        assertEquals(2f, style.getBorder().getWidth());
        assertEquals(0.8f, style.getOpacity());
    }

    @Test
    void testSetPadding() {
        UIRect padding = new UIRect(10f, 10f, 20f, 20f);
        style.setPadding(padding);

        assertEquals(10f, style.getPadding().getX());
        assertEquals(10f, style.getPadding().getY());
        assertEquals(20f, style.getPadding().getWidth());
        assertEquals(20f, style.getPadding().getHeight());
    }

    @Test
    void testSetMargin() {
        UIRect margin = new UIRect(5f, 5f, 15f, 15f);
        style.setMargin(margin);

        assertEquals(5f, style.getMargin().getX());
        assertEquals(5f, style.getMargin().getY());
        assertEquals(15f, style.getMargin().getWidth());
        assertEquals(15f, style.getMargin().getHeight());
    }

    @Test
    void testMerge() {
        UIStyle other = new UIStyle();
        other.setBackgroundColor(UIColor.GREEN);
        other.setFontSize(24f);
        other.setFontColor(UIColor.YELLOW);

        style.merge(other);

        assertEquals(UIColor.GREEN, style.getBackgroundColor());
        assertEquals(24f, style.getFontSize());
        assertEquals(UIColor.YELLOW, style.getFontColor());
    }

    @Test
    void testMergeNull() {
        style.merge(null);
        // Should not throw
    }

    @Test
    void testCopy() {
        style.setBackgroundColor(UIColor.MAGENTA);
        style.setFontSize(22f);

        UIStyle copy = style.copy();

        assertEquals(style.getBackgroundColor(), copy.getBackgroundColor());
        assertEquals(style.getFontSize(), copy.getFontSize());
        assertNotSame(style, copy);
    }

    @Test
    void testEquals() {
        UIStyle a = new UIStyle();
        a.setBackgroundColor(UIColor.RED);
        a.setFontSize(18f);

        UIStyle b = new UIStyle();
        b.setBackgroundColor(UIColor.RED);
        b.setFontSize(18f);

        assertEquals(a, b);
    }

    @Test
    void testHashCode() {
        UIStyle a = new UIStyle();
        a.setBackgroundColor(UIColor.RED);

        UIStyle b = new UIStyle();
        b.setBackgroundColor(UIColor.RED);

        assertEquals(a.hashCode(), b.hashCode());
    }

    @Test
    void testOpacityClamping() {
        style.setOpacity(1.5f);
        assertEquals(1f, style.getOpacity());

        style.setOpacity(-0.5f);
        assertEquals(0f, style.getOpacity());
    }

    @Test
    void testSetNullBorder() {
        style.setBorder(null);
        assertEquals(BorderStyle.NONE, style.getBorder());
    }
}
