package io.github.s3s3l.yggdrasil.game.core.ui.theme;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * UI 样式管理器单元测试
 */
class UIStyleManagerTest {

    @BeforeEach
    void setUp() {
        UIStyleManager.resetInstance();
    }

    @AfterEach
    void tearDown() {
        UIStyleManager.resetInstance();
    }

    @Test
    void testGetInstance() {
        UIStyleManager instance1 = UIStyleManager.getInstance();
        UIStyleManager instance2 = UIStyleManager.getInstance();
        assertSame(instance1, instance2);
    }

    @Test
    void testRegisterTheme() {
        UIStyleManager manager = UIStyleManager.getInstance();
        UITheme theme = new UITheme("TestTheme");

        manager.registerTheme(theme);
        assertTrue(manager.hasTheme("TestTheme"));
    }

    @Test
    void testUnregisterTheme() {
        UIStyleManager manager = UIStyleManager.getInstance();
        UITheme theme = new UITheme("TestTheme");

        manager.registerTheme(theme);
        manager.unregisterTheme("TestTheme");
        assertFalse(manager.hasTheme("TestTheme"));
    }

    @Test
    void testGetTheme() {
        UIStyleManager manager = UIStyleManager.getInstance();
        UITheme theme = new UITheme("TestTheme");

        manager.registerTheme(theme);
        UITheme retrieved = manager.getTheme("TestTheme");

        assertEquals(theme, retrieved);
    }

    @Test
    void testGetThemes() {
        UIStyleManager manager = UIStyleManager.getInstance();
        manager.registerTheme(new UITheme("Theme1"));
        manager.registerTheme(new UITheme("Theme2"));

        assertEquals(2, manager.getThemes().size());
    }

    @Test
    void testApplyThemeByName() {
        UIStyleManager manager = UIStyleManager.getInstance();
        UITheme theme = new UITheme("TestTheme");
        manager.registerTheme(theme);

        manager.applyTheme("TestTheme");
        assertEquals(theme, manager.getCurrentTheme());
    }

    @Test
    void testApplyThemeDirectly() {
        UIStyleManager manager = UIStyleManager.getInstance();
        UITheme theme = new UITheme("TestTheme");

        manager.applyTheme(theme);
        assertEquals(theme, manager.getCurrentTheme());
        assertTrue(manager.hasTheme("TestTheme"));
    }

    @Test
    void testGetCurrentThemeName() {
        UIStyleManager manager = UIStyleManager.getInstance();
        UITheme theme = new UITheme("TestTheme");
        manager.registerTheme(theme);
        manager.applyTheme(theme);

        assertEquals("TestTheme", manager.getCurrentThemeName());
    }

    @Test
    void testAddThemeChangeListener() {
        UIStyleManager manager = UIStyleManager.getInstance();
        TestListener listener = new TestListener();

        manager.addThemeChangeListener(listener);

        UITheme theme = new UITheme("TestTheme");
        manager.registerTheme(theme);
        manager.applyTheme(theme);

        assertTrue(listener.changed);
        assertEquals(theme, listener.newTheme);
    }

    @Test
    void testRemoveThemeChangeListener() {
        UIStyleManager manager = UIStyleManager.getInstance();
        TestListener listener = new TestListener();

        manager.addThemeChangeListener(listener);
        manager.removeThemeChangeListener(listener);

        UITheme theme = new UITheme("TestTheme");
        manager.registerTheme(theme);
        manager.applyTheme(theme);

        assertFalse(listener.changed);
    }

    @Test
    void testClearThemes() {
        UIStyleManager manager = UIStyleManager.getInstance();
        manager.registerTheme(new UITheme("Theme1"));
        manager.registerTheme(new UITheme("Theme2"));
        manager.applyTheme("Theme1");

        manager.clearThemes();

        assertEquals(0, manager.getThemes().size());
        assertNull(manager.getCurrentTheme());
    }

    @Test
    void testApplyNullTheme() {
        UIStyleManager manager = UIStyleManager.getInstance();
        manager.applyTheme((UITheme) null);
        // Should not throw
    }

    private static class TestListener implements UIStyleManager.ThemeChangeListener {
        boolean changed = false;
        UITheme newTheme = null;

        @Override
        public void onThemeChanged(UITheme oldTheme, UITheme newTheme) {
            changed = true;
            this.newTheme = newTheme;
        }
    }
}
