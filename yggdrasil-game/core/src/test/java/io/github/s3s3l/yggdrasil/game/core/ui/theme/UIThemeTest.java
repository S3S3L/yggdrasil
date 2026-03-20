package io.github.s3s3l.yggdrasil.game.core.ui.theme;

import io.github.s3s3l.yggdrasil.game.core.ui.UIColor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * UI 主题单元测试
 */
class UIThemeTest {

    private UITheme theme;

    @BeforeEach
    void setUp() {
        theme = new UITheme("TestTheme");
    }

    @Test
    void testGetName() {
        assertEquals("TestTheme", theme.getName());
    }

    @Test
    void testCreateWithParent() {
        UITheme parent = new UITheme("Parent");
        UITheme child = new UITheme("Child", parent);

        assertEquals("Child", child.getName());
        assertEquals(parent, child.getParent());
    }

    @Test
    void testSetColor() {
        theme.setColor("primary", UIColor.RED);
        assertEquals(UIColor.RED, theme.getColor("primary"));
    }

    @Test
    void testGetColorFromParent() {
        UITheme parent = new UITheme("Parent");
        parent.setColor("primary", UIColor.BLUE);

        UITheme child = new UITheme("Child", parent);
        assertEquals(UIColor.BLUE, child.getColor("primary"));
    }

    @Test
    void testHasColor() {
        assertFalse(theme.hasColor("primary"));
        theme.setColor("primary", UIColor.RED);
        assertTrue(theme.hasColor("primary"));
    }

    @Test
    void testRemoveColor() {
        theme.setColor("primary", UIColor.RED);
        assertTrue(theme.hasColor("primary"));

        theme.removeColor("primary");
        assertFalse(theme.hasColor("primary"));
    }

    @Test
    void testSetStyle() {
        UIStyle style = new UIStyle();
        style.setBackgroundColor(UIColor.GREEN);

        theme.setStyle("Button", style);
        assertTrue(theme.hasStyle("Button"));

        UIStyle retrieved = theme.getStyle("Button");
        assertEquals(UIColor.GREEN, retrieved.getBackgroundColor());
    }

    @Test
    void testGetStyleFromParent() {
        UITheme parent = new UITheme("Parent");
        UIStyle parentStyle = new UIStyle();
        parentStyle.setBackgroundColor(UIColor.YELLOW);
        parent.setStyle("Button", parentStyle);

        UITheme child = new UITheme("Child", parent);
        UIStyle childStyle = child.getStyle("Button");

        assertEquals(UIColor.YELLOW, childStyle.getBackgroundColor());
    }

    @Test
    void testHasStyle() {
        assertFalse(theme.hasStyle("Button"));
        theme.setStyle("Button", new UIStyle());
        assertTrue(theme.hasStyle("Button"));
    }

    @Test
    void testRemoveStyle() {
        theme.setStyle("Button", new UIStyle());
        assertTrue(theme.hasStyle("Button"));

        theme.removeStyle("Button");
        assertFalse(theme.hasStyle("Button"));
    }

    @Test
    void testSetFont() {
        theme.setFont("default", "/fonts/arial.ttf");
        assertEquals("/fonts/arial.ttf", theme.getFont("default"));
    }

    @Test
    void testGetFontFromParent() {
        UITheme parent = new UITheme("Parent");
        parent.setFont("default", "/fonts/arial.ttf");

        UITheme child = new UITheme("Child", parent);
        assertEquals("/fonts/arial.ttf", child.getFont("default"));
    }

    @Test
    void testHasFont() {
        assertFalse(theme.hasFont("default"));
        theme.setFont("default", "/fonts/arial.ttf");
        assertTrue(theme.hasFont("default"));
    }

    @Test
    void testRemoveFont() {
        theme.setFont("default", "/fonts/arial.ttf");
        theme.removeFont("default");
        assertFalse(theme.hasFont("default"));
    }

    @Test
    void testGetColors() {
        theme.setColor("primary", UIColor.RED);
        theme.setColor("secondary", UIColor.BLUE);

        assertEquals(2, theme.getColors().size());
    }

    @Test
    void testGetStyles() {
        theme.setStyle("Button", new UIStyle());
        theme.setStyle("Label", new UIStyle());

        assertEquals(2, theme.getStyles().size());
    }

    @Test
    void testGetFonts() {
        theme.setFont("default", "/fonts/arial.ttf");
        theme.setFont("bold", "/fonts/arial-bold.ttf");

        assertEquals(2, theme.getFonts().size());
    }

    @Test
    void testSetParent() {
        UITheme parent = new UITheme("Parent");
        theme.setParent(parent);
        assertEquals(parent, theme.getParent());
    }

    @Test
    void testEquals() {
        UITheme a = new UITheme("Theme");
        UITheme b = new UITheme("Theme");
        assertEquals(a, b);
    }

    @Test
    void testHashCode() {
        UITheme a = new UITheme("Theme");
        UITheme b = new UITheme("Theme");
        assertEquals(a.hashCode(), b.hashCode());
    }

    @Test
    void testToString() {
        theme.setColor("primary", UIColor.RED);
        theme.setStyle("Button", new UIStyle());

        String str = theme.toString();
        assertTrue(str.contains("TestTheme"));
        assertTrue(str.contains("1")); // colors count
    }
}
