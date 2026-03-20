package io.github.s3s3l.yggdrasil.game.core.ui.theme;

import io.github.s3s3l.yggdrasil.game.core.ui.UIColor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 主题加载器单元测试
 */
class ThemeLoaderTest {

    private ThemeLoader loader;

    @BeforeEach
    void setUp() {
        loader = new ThemeLoader();
    }

    @Test
    void testParseThemeEmpty() {
        String json = "{}";
        UITheme theme = loader.parseTheme("Empty", json, null);

        assertEquals("Empty", theme.getName());
        assertEquals(0, theme.getColors().size());
    }

    @Test
    void testParseThemeWithColors() {
        String json = "{ \"colors\": { \"primary\": \"#FF0000\", \"secondary\": \"#00FF00\" } }";
        UITheme theme = loader.parseTheme("Test", json, null);

        assertEquals(2, theme.getColors().size());
        assertNotNull(theme.getColor("primary"));
        assertNotNull(theme.getColor("secondary"));
    }

    @Test
    void testParseThemeWithStyles() {
        String json = "{ \"styles\": { \"Button\": { \"backgroundColor\": \"#FF0000\" } } }";
        UITheme theme = loader.parseTheme("Test", json, null);

        assertTrue(theme.hasStyle("Button"));
        assertNotNull(theme.getStyle("Button"));
    }

    @Test
    void testParseThemeWithFonts() {
        String json = "{ \"fonts\": { \"default\": \"/fonts/arial.ttf\" } }";
        UITheme theme = loader.parseTheme("Test", json, null);

        assertEquals("/fonts/arial.ttf", theme.getFont("default"));
    }

    @Test
    void testParseThemeWithParent() {
        UITheme parent = new UITheme("Parent");
        parent.setColor("primary", UIColor.BLUE);

        String json = "{ \"colors\": { \"secondary\": \"#00FF00\" } }";
        UITheme child = loader.parseTheme("Child", json, parent);

        assertEquals(UIColor.BLUE, child.getColor("primary")); // 继承自父主题
        assertNotNull(child.getColor("secondary")); // 自己的颜色
    }

    @Test
    void testLoadThemeFromStream() throws Exception {
        String json = "{ \"colors\": { \"primary\": \"#FF0000\" } }";
        InputStream stream = new ByteArrayInputStream(json.getBytes(StandardCharsets.UTF_8));

        UITheme theme = loader.loadTheme("Test", stream, null);

        assertEquals("Test", theme.getName());
        assertEquals(1, theme.getColors().size());
    }

    @Test
    void testAddListener() {
        TestListener listener = new TestListener();
        loader.addListener("test", listener);

        String json = "{ \"colors\": { \"primary\": \"#FF0000\" } }";
        loader.parseTheme("Test", json, null);

        assertTrue(listener.loaded);
        assertNotNull(listener.theme);
    }

    @Test
    void testRemoveListener() {
        TestListener listener = new TestListener();
        loader.addListener("test", listener);
        loader.removeListener("test");

        String json = "{ \"colors\": { \"primary\": \"#FF0000\" } }";
        loader.parseTheme("Test", json, null);

        assertFalse(listener.loaded);
    }

    @Test
    void testGetNinePatchLoader() {
        assertNotNull(loader.getNinePatchLoader());
    }

    @Test
    void testParseColor6Digits() {
        String json = "{ \"colors\": { \"test\": \"#FF00FF\" } }";
        UITheme theme = loader.parseTheme("Test", json, null);

        UIColor color = theme.getColor("test");
        assertNotNull(color);
    }

    @Test
    void testParseColor8Digits() {
        String json = "{ \"colors\": { \"test\": \"#FF00FF80\" } }";
        UITheme theme = loader.parseTheme("Test", json, null);

        UIColor color = theme.getColor("test");
        assertNotNull(color);
    }

    @Test
    void testParseInvalidJson() {
        String json = "{ invalid json }";
        UITheme theme = loader.parseTheme("Test", json, null);

        assertEquals("Test", theme.getName());
        // Should not throw, returns empty theme
    }

    private static class TestListener implements ThemeLoader.ThemeLoaderListener {
        boolean loaded = false;
        UITheme theme = null;

        @Override
        public void onThemeLoaded(UITheme theme) {
            this.loaded = true;
            this.theme = theme;
        }
    }
}
